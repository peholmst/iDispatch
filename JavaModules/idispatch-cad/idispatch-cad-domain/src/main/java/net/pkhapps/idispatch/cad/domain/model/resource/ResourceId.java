package net.pkhapps.idispatch.cad.domain.model.resource;

import net.pkhapps.idispatch.domain.support.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID class for {@link Resource}.
 */
@Immutable
public class ResourceId extends DomainObjectId {

    public ResourceId(@Nonnull String id) {
        super(id);
    }
}
