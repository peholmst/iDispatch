package net.pkhapps.idispatch.client.v3.type;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ID type for {@link ResourceType}.
 */
@Immutable
public class ResourceTypeId extends DomainObjectId {
    public ResourceTypeId(@Nonnull Long id) {
        super(id);
    }
}
