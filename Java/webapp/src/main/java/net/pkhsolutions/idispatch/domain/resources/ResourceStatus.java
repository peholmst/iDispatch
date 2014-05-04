package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity that contains the current state of a {@link net.pkhsolutions.idispatch.domain.resources.Resource}.
 */
@Entity
@Table(name = "resource_status", indexes = @Index(columnList = "ts"))
public class ResourceStatus extends AbstractResourceStateChange {

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
}
