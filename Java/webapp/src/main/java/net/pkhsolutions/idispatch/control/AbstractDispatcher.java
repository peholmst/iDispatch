package net.pkhsolutions.idispatch.control;

import net.pkhsolutions.idispatch.boundary.events.DispatchNotificationReceived;
import net.pkhsolutions.idispatch.boundary.events.DispatchNotificationSent;
import net.pkhsolutions.idispatch.entity.Destination;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import net.pkhsolutions.idispatch.entity.Receipt;
import net.pkhsolutions.idispatch.entity.repository.ReceiptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

abstract class AbstractDispatcher implements ApplicationListener<DispatchNotificationSent> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ReceiptRepository receiptRepository;

    @Autowired
    ApplicationContext applicationContext;

    protected void createReceipt(Destination destination, DispatchNotification notification) {
        logger.debug("Storing receipt for destination {} and notification {}", destination, notification);
        final Receipt receipt = receiptRepository.saveAndFlush(new Receipt(destination, notification));
        applicationContext.publishEvent(new DispatchNotificationReceived(this, receipt));
    }

}
