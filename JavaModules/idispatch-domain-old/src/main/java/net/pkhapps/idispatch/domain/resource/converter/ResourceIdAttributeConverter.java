package net.pkhapps.idispatch.domain.resource.converter;

import net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdAttributeConverter;
import net.pkhapps.idispatch.domain.resource.ResourceId;

import javax.persistence.Converter;

/**
 * Attribute hibernate for {@link ResourceId}.
 */
@Converter(autoApply = true)
public class ResourceIdAttributeConverter extends AbstractAggregateRootIdAttributeConverter<ResourceId> {

    public ResourceIdAttributeConverter() {
        super(ResourceId.class);
    }
}
