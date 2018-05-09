package net.pkhapps.idispatch.domain.resource.converter;

import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdAttributeConverter;
import net.pkhapps.idispatch.domain.resource.ResourceTypeId;

import javax.persistence.Converter;

/**
 * Attribute hibernate for {@link ResourceTypeId}.
 */
@Converter(autoApply = true)
public class ResourceTypeIdAttributeConverter extends AbstractAggregateRootIdAttributeConverter<ResourceTypeId> {

    public ResourceTypeIdAttributeConverter() {
        super(ResourceTypeId.class);
    }
}