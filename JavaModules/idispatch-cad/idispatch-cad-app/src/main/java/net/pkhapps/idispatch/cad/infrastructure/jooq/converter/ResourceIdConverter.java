package net.pkhapps.idispatch.cad.infrastructure.jooq.converter;

import net.pkhapps.idispatch.application.support.infrastructure.jooq.converter.DomainObjectIdConverter;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceId;

import javax.annotation.concurrent.Immutable;

/**
 * JOOQ converter for {@link ResourceId}, assuming the ID is persisted as a string.
 */
@Immutable
public class ResourceIdConverter extends DomainObjectIdConverter<ResourceId, String> {

    public ResourceIdConverter() {
        super(ResourceId.class, String.class, ResourceId::new);
    }
}
