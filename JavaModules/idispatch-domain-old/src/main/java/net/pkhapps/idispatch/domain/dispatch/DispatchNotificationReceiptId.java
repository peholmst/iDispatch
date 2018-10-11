package net.pkhapps.idispatch.domain.dispatch;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRootId;

/**
 * ID type for {@link DispatchNotificationReceipt}.
 */
public class DispatchNotificationReceiptId extends AbstractAggregateRootId {

    public DispatchNotificationReceiptId(Long id) {
        super(id);
    }
}
