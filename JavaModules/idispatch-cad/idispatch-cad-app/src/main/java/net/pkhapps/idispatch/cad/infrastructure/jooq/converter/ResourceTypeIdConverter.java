package net.pkhapps.idispatch.cad.infrastructure.jooq.converter;

import net.pkhapps.idispatch.application.support.infrastructure.jooq.converter.DomainObjectIdConverter;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceTypeId;

import javax.annotation.concurrent.Immutable;

/**
 * JOOQ converter for {@link ResourceTypeId}, assuming the ID is persisted as a string.
 */
@Immutable
public class ResourceTypeIdConverter extends DomainObjectIdConverter<ResourceTypeId, String> {

    public ResourceTypeIdConverter() {
        super(ResourceTypeId.class, String.class, ResourceTypeId::new);
    }
}
