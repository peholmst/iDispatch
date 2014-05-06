package net.pkhsolutions.idispatch.domain.dispatch.event;

import net.pkhsolutions.idispatch.domain.dispatch.Receipt;
import org.springframework.context.ApplicationEvent;

public class DispatchNotificationReceived extends ApplicationEvent {

    private final Receipt receipt;

    public DispatchNotificationReceived(Object source, Receipt receipt) {
        super(source);
        this.receipt = receipt;
    }

    public Receipt getReceipt() {
        return receipt;
    }
}
