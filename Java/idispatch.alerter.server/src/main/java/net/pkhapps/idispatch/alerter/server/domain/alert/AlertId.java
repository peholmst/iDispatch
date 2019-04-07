package net.pkhapps.idispatch.alerter.server.domain.alert;

import net.pkhapps.idispatch.base.domain.DomainObjectId;

/**
 * ID class for {@link Alert}.
 */
public class AlertId extends DomainObjectId {
    public AlertId(long wrappedId) {
        super(wrappedId);
    }
}
