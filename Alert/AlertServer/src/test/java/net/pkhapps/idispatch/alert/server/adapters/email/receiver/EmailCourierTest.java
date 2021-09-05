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
package net.pkhapps.idispatch.alert.server.adapters.email.receiver;

import net.pkhapps.idispatch.alert.server.adapters.email.SmtpGateway;
import net.pkhapps.idispatch.alert.server.adapters.email.data.EmailAddress;
import net.pkhapps.idispatch.alert.server.adapters.email.receiver.data.*;
import net.pkhapps.idispatch.alert.server.application.ports.receiver.ReceiverPort;
import net.pkhapps.idispatch.alert.server.application.text.AlertTextMessageFormatter;
import net.pkhapps.idispatch.alert.server.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EmailCourierTest {

    EmailAddressListRepository emailAddressListRepository;
    ReceiverPort receiverPort;
    SmtpGateway smtpGateway;
    AlertTextMessageTemplateRepository alertTextMessageTemplateRepository;
    AlertTextMessageFormatter alertTextMessageFormatter;
    EmailCourier courier;

    @BeforeEach
    void setUp() {
        emailAddressListRepository = mock(EmailAddressListRepository.class);
        receiverPort = mock(ReceiverPort.class);
        smtpGateway = mock(SmtpGateway.class);
        alertTextMessageTemplateRepository = mock(AlertTextMessageTemplateRepository.class);
        alertTextMessageFormatter = mock(AlertTextMessageFormatter.class);
        courier = new EmailCourier(emailAddressListRepository, receiverPort, smtpGateway,
                alertTextMessageTemplateRepository, alertTextMessageFormatter);
    }

    @Test
    void supports_EmailReceiver_returnsTrue() {
        assertThat(courier.supports(EmailReceiver.class)).isTrue();
    }

    @Test
    void deliver_singleReceiver_allOk() {
        var template = new AlertTextMessageTemplate(
                AlertTextMessageTemplateId.randomAlertTextMessageTemplateId(),
                "Plain text mail",
                "text/plain",
                "my template");

        var joeEmail = EmailAddress.fromString("joe.cool@foo.bar");
        var maxEmail = EmailAddress.fromString("maxwell.smart@foo.bar");
        var emailList = new EmailAddressList(
                EmailAddressListId.randomEmailAddressListId(),
                "RVS91 Mailing List",
                Set.of(
                        new EmailAddressListEntry("Joe", joeEmail, template.id()),
                        new EmailAddressListEntry("Max", maxEmail, template.id())
                )
        );

        var receiver = new EmailReceiver(
                ReceiverId.randomReceiverId(),
                true,
                Set.of(
                        ResourceIdentifier.fromString("RVS911"),
                        ResourceIdentifier.fromString("RVS917")
                ),
                "Hälytys Alarm RVS91",
                emailList.id()
        );

        var alert = AlertTestData.createTestAlert();

        // Instruct mocks
        when(alertTextMessageTemplateRepository.findById(template.id())).thenReturn(Optional.of(template));
        when(emailAddressListRepository.findById(emailList.id())).thenReturn(Optional.of(emailList));
        when(alertTextMessageFormatter.formatAlertMessage(alert, template.template())).thenReturn("body");

        // Run test
        courier.deliver(alert, Set.of(receiver));

        // Verify mocks
        verify(smtpGateway).sendTextMessage(receiver.subject(), Collections.emptySet(),
                Collections.emptySet(), Set.of(joeEmail, maxEmail), "body");
        verify(receiverPort).acknowledgeDelivery(receiver.id(), alert.id());
    }
}
