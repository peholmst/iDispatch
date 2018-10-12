package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * @param <ID>
 */
public abstract class AggregateRoot<ID extends DomainObjectId> extends Entity<ID>
        implements IdentifiableDomainObject<ID> {

    @UsedByPersistenceFramework
    protected AggregateRoot() {
    }

    protected AggregateRoot(@Nonnull ID id) {
        super(id);
    }

    /**
     * @param domainEvent
     */
    protected void registerDomainEvent(@Nonnull DomainEvent domainEvent) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * @return
     */
    @Nonnull
    protected Stream<DomainEvent> domainEvents() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     *
     */
    protected void clearDomainEvents() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    boolean isNew() {
        return false;
    }

    void setNew(boolean isNew) {

    }
}
