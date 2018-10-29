package net.pkhapps.idispatch.workstation.ui.model;

import net.pkhapps.appmodel4flow.property.DefaultObservableValue;
import net.pkhapps.appmodel4flow.property.DefaultProperty;
import net.pkhapps.appmodel4flow.property.ObservableValue;
import net.pkhapps.appmodel4flow.property.Property;
import net.pkhapps.idispatch.client.v3.*;
import net.pkhapps.idispatch.client.v3.base.Principal;
import net.pkhapps.idispatch.client.v3.util.PhoneNumber;
import net.pkhapps.idispatch.client.v3.geo.GeographicLocation;

import java.io.Serializable;
import java.time.Instant;

public class AssignmentSheetModel implements Serializable {

    private final DefaultObservableValue<AssignmentId> id = new DefaultObservableValue<>();
    private final DefaultObservableValue<Integer> version = new DefaultObservableValue<>();
    private final DefaultObservableValue<Instant> lastUpdated = new DefaultObservableValue<>();
    private final DefaultObservableValue<Principal> lastUpdatedBy = new DefaultObservableValue<>();
    private final DefaultObservableValue<AssignmentState> state = new DefaultObservableValue<>();

    private final DefaultProperty<MunicipalityId> municipality = new DefaultProperty<>();
    private final DefaultProperty<GeographicLocation> locationCoordinates = new DefaultProperty<>();
    private final DefaultProperty<String> locationAddressName = new DefaultProperty<>();
    private final DefaultProperty<String> locationAddressNumber = new DefaultProperty<>();
    private final DefaultProperty<String> locationCrossingAddressName = new DefaultProperty<>();

    private final DefaultProperty<AssignmentTypeId> type = new DefaultProperty<>();
    private final DefaultProperty<Priority> priority = new DefaultProperty<>();

    private final DefaultObservableValue<Instant> callReceived = new DefaultObservableValue<>();
    private final DefaultProperty<String> callerName = new DefaultProperty<>();
    private final DefaultProperty<PhoneNumber> callerNumber = new DefaultProperty<>();

    private final DefaultProperty<String> details = new DefaultProperty<>();

    public ObservableValue<AssignmentId> id() {
        return id;
    }

    public ObservableValue<Integer> version() {
        return version;
    }

    public ObservableValue<Instant> lastUpdated() {
        return lastUpdated;
    }

    public ObservableValue<Principal> lastUpdatedBy() {
        return lastUpdatedBy;
    }

    public ObservableValue<AssignmentState> state() {
        return state;
    }

    public Property<MunicipalityId> municipality() {
        return municipality;
    }

    public Property<GeographicLocation> locationCoordinates() {
        return locationCoordinates;
    }

    public Property<String> locationAddressName() {
        return locationAddressName;
    }

    public Property<String> locationAddressNumber() {
        return locationAddressNumber;
    }

    public Property<String> locationCrossingAddressName() {
        return locationCrossingAddressName;
    }

    public Property<AssignmentTypeId> type() {
        return type;
    }

    public Property<Priority> priority() {
        return priority;
    }

    public ObservableValue<Instant> callReceived() {
        return callReceived;
    }

    public Property<String> callerName() {
        return callerName;
    }

    public Property<PhoneNumber> callerNumber() {
        return callerNumber;
    }

    public Property<String> details() {
        return details;
    }
}
