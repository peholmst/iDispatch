package net.pkhapps.idispatch.domain.dispatch;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRootId;

/**
 * ID type for {@link DispatchNotification}.
 */
public class DispatchNotificationId extends AbstractAggregateRootId {

    public DispatchNotificationId(Long id) {
        super(id);
    }
}
