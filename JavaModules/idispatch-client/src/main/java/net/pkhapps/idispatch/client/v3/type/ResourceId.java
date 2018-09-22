package net.pkhapps.idispatch.client.v3.type;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID type for {@link Resource}.
 */
@Immutable
public class ResourceId extends DomainObjectId {
    public ResourceId(@Nonnull Long id) {
        super(id);
    }
}
