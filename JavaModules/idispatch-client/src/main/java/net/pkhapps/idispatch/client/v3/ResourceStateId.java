package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID type for {@link ResourceState}.
 */
@Immutable
public class ResourceStateId extends DomainObjectId {
    public ResourceStateId(@Nonnull Long id) {
        super(id);
    }
}
