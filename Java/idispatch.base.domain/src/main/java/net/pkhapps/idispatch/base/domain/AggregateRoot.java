package net.pkhapps.idispatch.base.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Base class for aggregate roots.
 */
@MappedSuperclass
public abstract class AggregateRoot<ID extends DomainObjectId> extends Entity<ID> {

    @Transient
    private transient final List<DomainEvent> domainEvents = new ArrayList<>();
    @Transient
    private final DomainObjectIdConverter<ID> domainObjectIdConverter;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Transient
    private ID wrappedId;

    @CreatedDate
    @Column(name = "created_on")
    private Instant createdOn;

    @CreatedBy
    @Column(name = "created_by")
    private UserId createdBy;

    @LastModifiedDate
    @Column(name = "last_modified_on")
    private Instant lastModifiedOn;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private UserId lastModifiedBy;

    protected AggregateRoot(@NonNull DomainObjectIdConverter<ID> domainObjectIdConverter) {
        this.domainObjectIdConverter = Objects.requireNonNull(domainObjectIdConverter);
    }

    @Override
    @Nullable
    public ID getId() {
        if (id != null && wrappedId == null) {
            wrappedId = wrapId(id);
        }
        return wrappedId;
    }

    protected void setId(@Nullable ID id) {
        this.wrappedId = id;
        this.id = id == null ? null : id.toLong();
    }

    @NonNull
    protected ID wrapId(@NonNull Long id) {
        return requireNonNull(domainObjectIdConverter).convertToEntityAttribute(id);
    }

    @Nullable
    public Instant getCreatedOn() {
        return createdOn;
    }

    protected void setCreatedOn(@Nullable Instant createdOn) {
        this.createdOn = createdOn;
    }

    @Nullable
    public UserId getCreatedBy() {
        return createdBy;
    }

    protected void setCreatedBy(@Nullable UserId createdBy) {
        this.createdBy = createdBy;
    }

    @Nullable
    public Instant getLastModifiedOn() {
        return lastModifiedOn;
    }

    protected void setLastModifiedOn(@Nullable Instant lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    @Nullable
    public UserId getLastModifiedBy() {
        return lastModifiedBy;
    }

    protected void setLastModifiedBy(@Nullable UserId lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * Registers the given event object for publication on a call to a Spring Data repository's save methods.
     */
    protected <T extends DomainEvent> T registerEvent(T event) {
        this.domainEvents.add(requireNonNull(event));
        return event;
    }

    /**
     * Clears all domain events currently held. Usually invoked by the infrastructure in place in Spring Data
     * repositories.
     */
    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        domainEvents.clear();
    }

    /**
     * All domain events currently captured by the aggregate.
     */
    @DomainEvents
    protected Collection<DomainEvent> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
