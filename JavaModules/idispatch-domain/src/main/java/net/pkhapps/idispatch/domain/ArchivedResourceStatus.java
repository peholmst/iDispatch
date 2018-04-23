package net.pkhapps.idispatch.domain;

import net.pkhapps.idispatch.domain.resource.Resource;
import net.pkhapps.idispatch.domain.status.ResourceStatus;

import javax.persistence.*;

/**
 * Entity that contains past states of a {@link Resource}.
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
        setAssignment(status.getAssignment());
        setState(status.getState());
    }

    @Override
    public Resource getResource() {
        return resource;
    }
}
