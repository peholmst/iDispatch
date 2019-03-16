package net.pkhapps.idispatch.base.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * Base class for aggregate roots that store auditing information.
 */
@MappedSuperclass
public abstract class AuditedAggregateRoot<ID extends DomainObjectId> extends AggregateRoot<ID> {

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

    protected AuditedAggregateRoot(@NonNull DomainObjectIdConverter<ID> domainObjectIdConverter) {
        super(domainObjectIdConverter);
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
}
