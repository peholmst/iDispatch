package net.pkhsolutions.idispatch.control;

import https.webservice_aspsms_com.aspsmsx2.ASPSMSX2;
import https.webservice_aspsms_com.aspsmsx2.ASPSMSX2Soap;
import net.pkhsolutions.idispatch.boundary.events.DispatchNotificationReceived;
import net.pkhsolutions.idispatch.boundary.events.DispatchNotificationSent;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import net.pkhsolutions.idispatch.entity.Receipt;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.SmsDestination;
import net.pkhsolutions.idispatch.entity.repository.ReceiptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class SmsDispatcher implements ApplicationListener<DispatchNotificationSent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ReceiptRepository receiptRepository;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Environment environment;

    private ASPSMSX2Soap aspsmsx2Soap;
    private String userKey;
    private String password;
    private String originator;

    private boolean enabled = true;

    @PostConstruct
    void init() {
        logger.info("Initializing ASPSMS Gateway");
        aspsmsx2Soap = new ASPSMSX2().getASPSMSX2Soap();

        userKey = environment.getProperty("aspsms.userKey");
        password = environment.getProperty("aspsms.password");
        originator = environment.getProperty("aspsms.originator", "iDispatch");

        if (userKey == null || password == null) {
            logger.warn("No ASPSMS credentials found, SmsDispatcher will be disabled");
            enabled = false;
        }
    }

    @Override
    @Async
    public void onApplicationEvent(DispatchNotificationSent dispatchNotificationSent) {
        final DispatchNotification notification = dispatchNotificationSent.getDispatchNotification();
        dispatch(notification.getDestinationsOfType(SmsDestination.class), notification);
    }

    private void dispatch(Collection<SmsDestination> destinations, DispatchNotification notification) {
        if (!enabled) {
            logger.warn("Received dispatch notification, but SmsDispatcher is disabled");
            return;
        }

        final Set<String> phoneNumbers = destinations
                .stream()
                .flatMap(destination -> destination.getPhoneNumbers().stream())
                .collect(Collectors.toSet());

        if (!phoneNumbers.isEmpty()) {
            final String messageText = buildMessageText(notification);
            logger.debug("Sending SMS \"{}\" to recipients {}", messageText, phoneNumbers);
            String resultCode = aspsmsx2Soap.sendSimpleTextSMS(
                    userKey,
                    password,
                    String.join(";", phoneNumbers),
                    originator,
                    messageText
            );
            if (resultCode.equals("StatusCode:1")) {
                destinations.forEach(destination -> {
                    logger.debug("Storing receipt for destination {} and notification {}", destination, notification);
                    final Receipt receipt = receiptRepository.saveAndFlush(new Receipt(destination, notification));
                    applicationContext.publishEvent(new DispatchNotificationReceived(this, receipt));
                });
            } else {
                logger.warn("SMS messages were not successfully sent: {}", resultCode);
            }
        } else {
            logger.warn("No phone numbers to send {} to", notification);
        }
    }

    private String buildMessageText(DispatchNotification notification) {
        final StringBuilder sb = new StringBuilder();
        if (notification.getMunicipality() != null) {
            sb.append(notification.getMunicipality().getName());
            sb.append(",");
        }
        if (notification.getAssignmentType() != null) {
            sb.append(notification.getAssignmentType().getCode());
        }
        sb.append(notification.getUrgency());
        sb.append(",");
        sb.append(notification.getAddress());
        sb.append(",");
        sb.append(String.join(",", notification.getAssignedResources().stream().map(Resource::getCallSign).collect(Collectors.toSet())));
        sb.append(",");
        sb.append(notification.getDescription());
        return sb.toString();
    }
}
