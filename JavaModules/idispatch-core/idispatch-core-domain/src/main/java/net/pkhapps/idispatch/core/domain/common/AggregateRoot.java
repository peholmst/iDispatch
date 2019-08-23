package net.pkhapps.idispatch.core.domain.common;

/**
 * Base class for aggregate roots.
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {

    public AggregateRoot(ID id) {
        super(id);
    }
}
