package net.pkhsolutions.idispatch.domain.resources;

import com.google.common.base.Objects;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity that contains the current state of a {@link net.pkhsolutions.idispatch.domain.resources.Resource}.
 */
@Entity
@Table(name = "resource_status", indexes = @Index(columnList = "ts"))
public class ResourceStatus extends AbstractResourceStateChange {

    public static final String PROP_VERSION = "version";

    @OneToOne(optional = false)
    @JoinColumn(name = "resource_id", nullable = false, unique = true)
    private Resource resource;

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
    }

    @Override
    public void setTicket(Ticket ticket) {
        super.setTicket(ticket);
        setTimestamp(new Date());
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
