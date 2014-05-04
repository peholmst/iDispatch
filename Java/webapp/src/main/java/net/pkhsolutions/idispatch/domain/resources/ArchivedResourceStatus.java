package net.pkhsolutions.idispatch.domain.resources;

import javax.persistence.*;

/**
 * Entity that contains past states of a {@link net.pkhsolutions.idispatch.domain.resources.Resource}.
 */
@Entity
@Table(name = "resource_status_archive", indexes = @Index(columnList = "ts"))
public class ArchivedResourceStatus extends AbstractResourceStateChange {

    @ManyToOne(optional = false)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    protected ArchivedResourceStatus() {
    }

    public ArchivedResourceStatus(ResourceStatus status) {
        this.resource = status.getResource();
        setTimestamp(status.getTimestamp());
        setTicket(status.getTicket());
        setState(status.getState());
    }

    @Override
    public Resource getResource() {
        return resource;
    }
}
