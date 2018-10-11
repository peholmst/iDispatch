package net.pkhsolutions.idispatch.entity;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableSet;

/**
 * Entity that contains the current state of a {@link net.pkhsolutions.idispatch.entity.Resource}.
 */
@Entity
@Table(name = "resource_status", indexes = @Index(columnList = "ts"))
public class ResourceStatus extends AbstractResourceStateChange {

    public static final String PROP_VERSION = "version";
    public static final String PROP_AVAILABLE = "available";
    public static final String PROP_DETACHED = "detached";

    private static final Map<ResourceState, Set<ResourceState>> TRANSITIONS = new HashMap<ResourceState, Set<ResourceState>>() {{
        put(ResourceState.RESERVED, newHashSet(ResourceState.DISPATCHED, ResourceState.EN_ROUTE, ResourceState.ON_SCENE, ResourceState.AVAILABLE, ResourceState.OUT_OF_SERVICE, ResourceState.AT_STATION));
        put(ResourceState.DISPATCHED, newHashSet(ResourceState.EN_ROUTE, ResourceState.AVAILABLE, ResourceState.OUT_OF_SERVICE, ResourceState.AT_STATION));
        put(ResourceState.EN_ROUTE, newHashSet(ResourceState.ON_SCENE, ResourceState.AVAILABLE, ResourceState.OUT_OF_SERVICE));
        put(ResourceState.ON_SCENE, newHashSet(ResourceState.AVAILABLE, ResourceState.OUT_OF_SERVICE));
        put(ResourceState.AVAILABLE, newHashSet(ResourceState.AT_STATION, ResourceState.OUT_OF_SERVICE, ResourceState.RESERVED));
        put(ResourceState.AT_STATION, newHashSet(ResourceState.AVAILABLE, ResourceState.OUT_OF_SERVICE, ResourceState.RESERVED));
        put(ResourceState.OUT_OF_SERVICE, newHashSet(ResourceState.AVAILABLE, ResourceState.AT_STATION));
    }};

    @OneToOne(optional = false)
    @JoinColumn(name = "resource_id", nullable = false, unique = true)
    private Resource resource;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "assigned", nullable = false)
    private boolean assigned;

    @Version
    private Long version;

    protected ResourceStatus() {
    }

    public ResourceStatus(Resource resource, ResourceState state) {
        this.resource = resource;
        setState(state);
    }

    public Long getVersion() {
        return version;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public void setState(ResourceState state) {
        super.setState(state);
        setTimestamp(new Date());
        updateBooleanFlags();
    }

    public Set<ResourceState> getAllValidNextStates() {
        return unmodifiableSet(TRANSITIONS.get(getState()));
    }

    public Set<ResourceState> getManualValidNextStates() {
        return getAllValidNextStates()
                .stream()
                .filter(state -> state != ResourceState.RESERVED && state != ResourceState.DISPATCHED)
                .collect(Collectors.toSet());
    }

    /**
     * Returns whether the resource is currently available for new assignments.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Returns whether the resource is currently assigned to another assignment. Please note that this method can
     * return false even though {@link #getAssignment()} is not null.
     */
    public boolean isAssigned() {
        return assigned;
    }

    private void updateBooleanFlags() {
        final ResourceState state = getState();
        assigned = getAssignment() != null && !state.isAvailableForNewAssignments() && state != ResourceState.OUT_OF_SERVICE;
        available = state.isAvailableForNewAssignments();
    }

    @Override
    public void setAssignment(Assignment lastAssignment) {
        super.setAssignment(lastAssignment);
        setTimestamp(new Date());
        updateBooleanFlags();
    }

    /**
     * Creates an archived version of this status entity.
     */
    public ArchivedResourceStatus toArchived() {
        return new ArchivedResourceStatus(this);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add(PROP_ID, getId())
                .add(PROP_VERSION, version)
                .add(PROP_RESOURCE, resource)
                .add(PROP_STATE, getState())
                .add(PROP_ASSIGNMENT, getAssignment())
                .add(PROP_TIMESTAMP, getTimestamp())
                .toString();
    }
}
