package net.pkhapps.idispatch.domain.dispatch.converter;

import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdAttributeConverter;
import net.pkhapps.idispatch.domain.dispatch.DestinationId;

import javax.persistence.Converter;

/**
 * Attribute hibernate for {@link DestinationId}.
 */
@Converter(autoApply = true)
public class DestinationIdAttributeConverter extends AbstractAggregateRootIdAttributeConverter<DestinationId> {

    public DestinationIdAttributeConverter() {
        super(DestinationId.class);
    }
}
