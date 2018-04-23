package net.pkhapps.idispatch.domain.base;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base class for aggregate roots.
 */
@MappedSuperclass
public abstract class AbstractAggregateRoot<ID extends AbstractAggregateRootId> extends AbstractPersistable<ID> {

    @Transient
    private transient final List<Object> domainEvents = new ArrayList<>();

    @Version
    private Long version;

    /**
     * Returns the version number used for optimistic locking.
     */
    @Nullable
    public Long getVersion() {
        return version;
    }

    /**
     * Registers the given event object for publication on a call to a Spring Data repository's save methods.
     */
    @NonNull
    protected <T> T registerEvent(@NonNull T event) {
        this.domainEvents.add(event);
        return event;
    }

    /**
     * Registers the given event object for publication on a call to a Spring Data repository's save methods. Any
     * other events of the same class that have already been registered will be deleted.
     */
    @NonNull
    protected <T> T registerSingletonEvent(@NonNull T event) {
        domainEvents.removeIf(old -> old.getClass().equals(event.getClass()));
        this.domainEvents.add(event);
        return event;
    }

    /**
     * Clears all domain events currently held. Usually invoked by the infrastructure in place in Spring Data
     * repositories.
     */
    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

    /**
     * All domain events currently captured by the aggregate.
     */
    @DomainEvents
    @NonNull
    protected Collection<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
