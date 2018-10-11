package net.pkhapps.idispatch.domain.status;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRootId;

/**
 * ID type for {@link ResourceStatus}.
 */
public class ResourceStatusId extends AbstractAggregateRootId {

    public ResourceStatusId(Long id) {
        super(id);
    }
}
