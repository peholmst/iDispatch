package net.pkhapps.idispatch.domain.status.converter;

import net.pkhapps.idispatch.domain.base.converter.AbstractAggregateRootIdAttributeConverter;
import net.pkhapps.idispatch.domain.status.ResourceStatusId;

import javax.persistence.Converter;

/**
 * Attribute converter for {@link ResourceStatusId}.
 */
@Converter(autoApply = true)
public class ResourceStatusIdAttributeConverter extends AbstractAggregateRootIdAttributeConverter<ResourceStatusId> {

    public ResourceStatusIdAttributeConverter() {
        super(ResourceStatusId.class);
    }
}
