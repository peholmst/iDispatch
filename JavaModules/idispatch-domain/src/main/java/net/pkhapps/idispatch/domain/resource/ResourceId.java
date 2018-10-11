package net.pkhapps.idispatch.domain.resource;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRootId;

/**
 * ID type for {@link Resource}.
 */
public class ResourceId extends AbstractAggregateRootId {

    public ResourceId(Long id) {
        super(id);
    }
}
