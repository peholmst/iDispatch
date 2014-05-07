package net.pkhsolutions.idispatch.boundary.events;

import net.pkhsolutions.idispatch.entity.Receipt;
import org.springframework.context.ApplicationEvent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Event published when a dispatch notification has been received by a destination.
 */
public class DispatchNotificationReceived extends ApplicationEvent {

    private final Receipt receipt;

    public DispatchNotificationReceived(Object source, Receipt receipt) {
        super(source);
        this.receipt = checkNotNull(receipt);
    }

    public Receipt getReceipt() {
        return receipt;
    }
}
