package net.pkhapps.idispatch.cad.domain.model.resource;

import net.pkhapps.idispatch.domain.support.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID class for {@link ResourceType}.
 */
@Immutable
public class ResourceTypeId extends DomainObjectId {

    public ResourceTypeId(@Nonnull String id) {
        super(id);
    }
}
