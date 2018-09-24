package net.pkhapps.idispatch.domain.base;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.Persistable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base class for aggregate roots. Remember to create a type for the {@link AbstractAggregateRootId ID} as well.
 */
@MappedSuperclass
public abstract class AbstractAggregateRoot<ID extends AbstractAggregateRootId> implements Persistable<ID> {

    @Transient
    private transient final List<Object> domainEvents = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id; // You will need a custom type for the JPA implementation to understand this.

    @Version
    private Long version;

    @Override
    @Nullable
    public ID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

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

    @Override
    public String toString() {
        return super.toString();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    // Yes it does, but it uses ClassUtils.getUserClass(), which IntelliJ does not understand
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !getClass().equals(ClassUtils.getUserClass(obj))) {
            return false;
        }
        var other = (AbstractAggregateRoot<?>) obj;
        var id = getId();
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        var id = getId();
        return id == null ? System.identityHashCode(this) : id.hashCode() * 31;
    }
}
