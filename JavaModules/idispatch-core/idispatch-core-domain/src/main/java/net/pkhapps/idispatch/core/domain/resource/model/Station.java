package net.pkhapps.idispatch.core.domain.resource.model;

import net.pkhapps.idispatch.core.domain.common.AggregateRoot;
import net.pkhapps.idispatch.core.domain.common.Deactivatable;
import net.pkhapps.idispatch.core.domain.geo.MunicipalityId;
import net.pkhapps.idispatch.core.domain.geo.StreetAddress;
import net.pkhapps.idispatch.core.domain.organization.OrganizationId;
import net.pkhapps.idispatch.core.domain.organization.OrganizationSpecificObject;
import org.opengis.geometry.DirectPosition;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Station extends AggregateRoot<StationId> implements OrganizationSpecificObject, Deactivatable {

    private final OrganizationId organization;
    private String name;
    private MunicipalityId municipality;
    private StreetAddress streetAddress;
    private DirectPosition location;
    private Deactivatable.ActivationState activationState = ActivationState.active();

    public Station(StationId stationId, OrganizationId organization, String name) {
        super(stationId);
        this.organization = requireNonNull(organization);
        setName(name);
    }

    public final void setName(String name) {
        this.name = requireNonNull(name);
    }
    
    @Override
    public OrganizationId owningOrganization() {
        return organization;
    }

    public String name() {
        return name;
    }

    public Optional<DirectPosition> location() {
        return Optional.empty();
    }

    public Optional<MunicipalityId> municipality() {
        return Optional.empty();
    }

    public Optional<StreetAddress> streetAddress() {
        return Optional.empty();
    }

    @Override
    public ActivationState activationState() {
        return activationState;
    }

    @Override
    public void activate() {
        if (activationState.isInactive()) {
            activationState = ActivationState.active();
        }
    }

    @Override
    public void deactivate(String reason) {
        if (activationState.isActive()) {
            activationState = ActivationState.inactive(reason);
        }
    }
}
