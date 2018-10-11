package net.pkhapps.idispatch.domain.dispatch.converter;

import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdAttributeConverter;
import net.pkhapps.idispatch.domain.dispatch.DispatchNotificationReceiptId;

import javax.persistence.Converter;

/**
 * Attribute hibernate for {@link DispatchNotificationReceiptId}.
 */
@Converter(autoApply = true)
public class DispatchNotificationReceiptIdAttributeConverter extends AbstractAggregateRootIdAttributeConverter<DispatchNotificationReceiptId> {

    public DispatchNotificationReceiptIdAttributeConverter() {
        super(DispatchNotificationReceiptId.class);
    }
}
