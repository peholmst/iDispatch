package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.*;
import net.pkhapps.idispatch.client.v3.geo.GeographicLocation;

import javax.annotation.Nonnull;
import java.time.Instant;

public class Assignment implements IdentifiableDomainObject<AssignmentId>, AuditedDomainObject, VersionedDomainObject {

    private AssignmentId id;
    private long version;
    private Principal createdBy;
    private Instant createdOn;
    private Principal lastModifiedBy;
    private Instant lastModifiedOn;
    private TemporalValue<AssignmentStateId> state;
    private AssignmentTypeId type;
    private Priority priority;
    private MunicipalityId municipality;
    private GeographicLocation location;


    @Nonnull
    @Override
    public AssignmentId id() {
        return null;
    }

    @Override
    public long version() {
        return 0;
    }

    @Nonnull
    @Override
    public Principal createdBy() {
        return null;
    }

    @Nonnull
    @Override
    public Instant createdOn() {
        return null;
    }

    @Nonnull
    @Override
    public Principal lastModifiedBy() {
        return null;
    }

    @Nonnull
    @Override
    public Instant lastModifiedOn() {
        return null;
    }


}
