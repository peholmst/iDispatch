// iDispatch Alert Server
// Copyright (C) 2021 Petter Holmström
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.pkhapps.idispatch.alert.server.adapters.email;

import net.pkhapps.idispatch.alert.server.adapters.email.data.EmailAddressList;
import net.pkhapps.idispatch.alert.server.adapters.email.data.EmailAddressListEntry;
import net.pkhapps.idispatch.alert.server.adapters.email.data.EmailAddressListRepository;
import net.pkhapps.idispatch.alert.server.adapters.email.data.EmailReceiver;
import net.pkhapps.idispatch.alert.server.application.ports.receiver.Courier;
import net.pkhapps.idispatch.alert.server.application.ports.receiver.ReceiverPort;
import net.pkhapps.idispatch.alert.server.application.text.AlertTextMessageFormatException;
import net.pkhapps.idispatch.alert.server.application.text.AlertTextMessageFormatter;
import net.pkhapps.idispatch.alert.server.data.Alert;
import net.pkhapps.idispatch.alert.server.data.AlertTextMessageTemplateId;
import net.pkhapps.idispatch.alert.server.data.AlertTextMessageTemplateRepository;
import net.pkhapps.idispatch.alert.server.data.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Courier} that delivers alerts through e-mail to {@link EmailReceiver}s. An alert is considered delivered
 * when it has been sent to at least one e-mail address. The courier does not perform any checking of whether the e-mail
 * messages have actually been delivered to their recipients.
 */
@SuppressWarnings("ClassCanBeRecord")
public class EmailCourier implements Courier {

    private static final String CONTENT_TYPE_HTML = "text/html";
    private static final Logger log = LoggerFactory.getLogger(EmailCourier.class);
    private final EmailAddressListRepository emailAddressListRepository;
    private final ReceiverPort receiverPort;
    private final SmtpGateway smtpGateway;
    private final AlertTextMessageTemplateRepository alertTextMessageTemplateRepository;
    private final AlertTextMessageFormatter alertTextMessageFormatter;

    public EmailCourier(EmailAddressListRepository emailAddressListRepository,
                        ReceiverPort receiverPort,
                        SmtpGateway smtpGateway,
                        AlertTextMessageTemplateRepository alertTextMessageTemplateRepository,
                        AlertTextMessageFormatter alertTextMessageFormatter) {
        this.emailAddressListRepository = requireNonNull(emailAddressListRepository,
                "emailAddressListRepository must not be null");
        this.receiverPort = requireNonNull(receiverPort, "receiverPort must not be null");
        this.smtpGateway = requireNonNull(smtpGateway, "smtpGateway must not be null");
        this.alertTextMessageTemplateRepository = requireNonNull(alertTextMessageTemplateRepository, "alertTextMessageTemplateRepository must not be null");
        this.alertTextMessageFormatter = requireNonNull(alertTextMessageFormatter, "alertTextMessageFormatter must not be null");
    }

    @Override
    public boolean supports(Class<? extends Receiver> receiverClass) {
        return receiverClass.equals(EmailReceiver.class);
    }

    @Override
    public void deliver(Alert alert, Collection<Receiver> receivers) {
        requireNonNull(alert, "alert must not be null");
        requireNonNull(receivers, "receivers must not be null");
        receivers.forEach(r -> deliver(alert, (EmailReceiver) r));
    }

    private void deliver(Alert alert, EmailReceiver receiver) {
        log.debug("Attempting to deliver {} to {}", alert, receiver);
        var entriesByTemplate = emailAddressListRepository
                .findById(receiver.emailAddressList())
                .stream()
                .flatMap(EmailAddressList::stream)
                .collect(Collectors.groupingBy(EmailAddressListEntry::templateId, Collectors.toSet()));
        var sentTo = entriesByTemplate
                .entrySet()
                .stream()
                .parallel()
                .map(entry -> send(alert, receiver, entry.getKey(), entry.getValue()))
                .reduce(0, Integer::sum);
        if (sentTo > 0) {
            log.info("{} was sent to {} e-mail address(es)", alert, sentTo);
            receiverPort.acknowledgeDelivery(receiver.id(), alert.id());
        } else {
            log.warn("{} was not sent to a single e-mail address", alert);
        }
    }

    private int send(Alert alert, EmailReceiver receiver, AlertTextMessageTemplateId templateId, Set<EmailAddressListEntry> recipients) {
        var templateOptional = alertTextMessageTemplateRepository.findById(templateId);
        if (templateOptional.isPresent()) {
            var template = templateOptional.get();
            log.debug("Generating alert message of {} using {}", alert, template);
            try {
                var message = alertTextMessageFormatter.formatAlertMessage(alert, template.template());
                var bcc = recipients.stream().map(EmailAddressListEntry::emailAddress).collect(Collectors.toSet());
                log.debug("Sending alert {} to {} e-mail address(es)", alert, bcc.size());
                if (template.contentType().equals(CONTENT_TYPE_HTML)) {
                    smtpGateway.sendHtmlMessage(receiver.subject(), Collections.emptySet(), Collections.emptySet(), bcc, message);
                } else {
                    smtpGateway.sendTextMessage(receiver.subject(), Collections.emptySet(), Collections.emptySet(), bcc, message);
                }
                return bcc.size();
            } catch (AlertTextMessageFormatException ex) {
                log.error("Error generating alert message", ex);
            } catch (SendMessageException ex) {
                log.error("Error sending alert message", ex);
            }
        } else {
            log.warn("Template {} does not exist", templateId);
        }
        return 0;
    }
}
