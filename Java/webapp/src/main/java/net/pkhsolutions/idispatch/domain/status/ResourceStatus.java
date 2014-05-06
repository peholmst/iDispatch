package net.pkhsolutions.idispatch.domain.status;

import com.google.common.base.Objects;
import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableSet;

/**
 * Entity that contains the current state of a {@link net.pkhsolutions.idispatch.domain.resources.Resource}.
 */
@Entity
@Table(name = "resource_status", indexes = @Index(columnList = "ts"))
public class ResourceStatus extends AbstractResourceStateChange {

    public static final String PROP_VERSION = "version";
    public static final String PROP_AVAILABLE = "available";
    public static final String PROP_DETACHED = "detached";

    private static final Map<ResourceState, Set<ResourceState>> TRANSITIONS = new HashMap<ResourceState, Set<ResourceState>>() {{
        put(ResourceState.ASSIGNED, newHashSet(ResourceState.DISPATCHED, ResourceState.EN_ROUTE, ResourceState.ON_SCENE, ResourceState.AVAILABLE, ResourceState.UNAVAILABLE, ResourceState.AT_STATION));
        put(ResourceState.DISPATCHED, newHashSet(ResourceState.EN_ROUTE, ResourceState.AVAILABLE, ResourceState.UNAVAILABLE, ResourceState.AT_STATION));
        put(ResourceState.EN_ROUTE, newHashSet(ResourceState.ON_SCENE, ResourceState.AVAILABLE, ResourceState.UNAVAILABLE));
        put(ResourceState.ON_SCENE, newHashSet(ResourceState.AVAILABLE, ResourceState.UNAVAILABLE));
        put(ResourceState.AVAILABLE, newHashSet(ResourceState.AT_STATION, ResourceState.UNAVAILABLE, ResourceState.ASSIGNED));
        put(ResourceState.AT_STATION, newHashSet(ResourceState.AVAILABLE, ResourceState.UNAVAILABLE, ResourceState.ASSIGNED));
        put(ResourceState.UNAVAILABLE, newHashSet(ResourceState.AVAILABLE, ResourceState.AT_STATION));
    }};

    @OneToOne(optional = false)
    @JoinColumn(name = "resource_id", nullable = false, unique = true)
    private Resource resource;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "detached", nullable = false)
    private boolean detached;

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
                .filter(state -> state != ResourceState.ASSIGNED && state != ResourceState.DISPATCHED)
                .collect(Collectors.toSet());
    }

    public boolean isDetached() {
        return detached;
    }

    public boolean isAvailable() {
        return available;
    }

    private void updateBooleanFlags() {
        final ResourceState state = getState();
        detached = getTicket() == null || (state.isAvailableForNewTickets() || state == ResourceState.UNAVAILABLE);
        available = state.isAvailableForNewTickets();
    }

    @Override
    public void setTicket(Ticket ticket) {
        super.setTicket(ticket);
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
                .add(PROP_TICKET, getTicket())
                .add(PROP_TIMESTAMP, getTimestamp())
                .toString();
    }
}
