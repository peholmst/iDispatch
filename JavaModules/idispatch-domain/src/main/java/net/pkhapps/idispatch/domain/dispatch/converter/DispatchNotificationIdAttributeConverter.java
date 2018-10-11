package net.pkhapps.idispatch.domain.dispatch.converter;

import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdAttributeConverter;
import net.pkhapps.idispatch.domain.dispatch.DispatchNotificationId;

import javax.persistence.Converter;

/**
 * Attribute hibernate for {@link DispatchNotificationId}.
 */
@Converter(autoApply = true)
public class DispatchNotificationIdAttributeConverter extends AbstractAggregateRootIdAttributeConverter<DispatchNotificationId> {

    public DispatchNotificationIdAttributeConverter() {
        super(DispatchNotificationId.class);
    }
}
