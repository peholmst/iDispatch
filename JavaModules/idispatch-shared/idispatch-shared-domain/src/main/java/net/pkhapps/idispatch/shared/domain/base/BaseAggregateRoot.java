package net.pkhapps.idispatch.shared.domain.base;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.*;

/**
 * Base class for aggregate roots.
 *
 * @param <ID>       the real ID type to store in the database.
 * @param <DomainId> the {@link DomainObjectId} type to use in the API.
 */
@MappedSuperclass
public abstract class BaseAggregateRoot<ID extends Serializable, DomainId extends DomainObjectId<ID>>
        extends BaseEntity<ID, DomainId> {

    @Transient
    private List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * Default constructor for new instances.
     */
    public BaseAggregateRoot() {
    }

    /**
     * Copy constructor. Please note that domain events will not be copied.
     *
     * @param source the entity to copy from.
     */
    public BaseAggregateRoot(@NotNull BaseAggregateRoot<ID, DomainId> source) {
        super(source);
    }

    /**
     * Registers the given {@code domainEvent} for publication on a call to a Spring Data repository's save methods.
     */
    protected void registerDomainEvent(@NotNull DomainEvent domainEvent) {
        Objects.requireNonNull(domainEvent, "domainEvent must not be null");
        this.domainEvents.add(domainEvent);
    }

    /**
     * Clears all domain events currently registered by the aggregate root. Usually invoked by Spring Data
     * repositories.
     */
    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

    /**
     * Returns all domain events currently registered by the aggregate root.
     */
    @DomainEvents
    protected Collection<DomainEvent> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
