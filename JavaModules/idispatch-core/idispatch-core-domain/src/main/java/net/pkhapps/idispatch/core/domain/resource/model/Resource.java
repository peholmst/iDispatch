package net.pkhapps.idispatch.core.domain.resource.model;

import net.pkhapps.idispatch.core.domain.common.AggregateRoot;
import net.pkhapps.idispatch.core.domain.common.Deactivatable;
import net.pkhapps.idispatch.core.domain.organization.OrganizationId;
import net.pkhapps.idispatch.core.domain.organization.OrganizationSpecificObject;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Resource extends AggregateRoot<ResourceId> implements OrganizationSpecificObject, Deactivatable {

    private final OrganizationId organization;

    private CallSign callSign;
    private ResourceTypeId type;
    private StationId station;
    private String comment;
    private Deactivatable.ActivationState activationState = ActivationState.active();

    public Resource(ResourceId id, OrganizationId organization, CallSign callSign, ResourceTypeId type) {
        super(id);
        this.organization = requireNonNull(organization);
        this.callSign = requireNonNull(callSign);
        this.type = requireNonNull(type);
    }

    public CallSign callSign() {
        return callSign;
    }

    public ResourceTypeId type() {
        return type;
    }

    public Optional<StationId> station() {
        return Optional.empty();
    }

    public Optional<String> comment() {
        return Optional.empty();
    }

    @Override
    public ActivationState activationState() {
        return activationState;
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate(String reason) {

    }

    @Override
    public OrganizationId owningOrganization() {
        return organization;
    }
}
