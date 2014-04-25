package net.pkhsolutions.idispatch.domain.resources;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity that stores the latest {@link net.pkhsolutions.idispatch.domain.resources.ResourceStateChange} of a
 * {@link Resource} (used to make certain queries faster).
 *
 * @see net.pkhsolutions.idispatch.domain.resources.CurrentResourceStateUpdater
 */
@Entity
@Table(name = "current_resource_state_changes")
class CurrentResourceState implements Persistable<Long>, Serializable {

    @Id
    @Column(name = "resource_id", nullable = false, updatable = false, insertable = false)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @OneToOne(optional = false)
    @JoinColumn(name = "state_change_id", nullable = false)
    private ResourceStateChange lastResourceStateChange;

    protected CurrentResourceState() {
    }

    public CurrentResourceState(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    public Resource getResource() {
        return resource;
    }

    public ResourceStateChange getLastResourceStateChange() {
        return lastResourceStateChange;
    }

    public void setLastResourceStateChange(ResourceStateChange lastResourceStateChange) {
        this.lastResourceStateChange = lastResourceStateChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CurrentResourceState that = (CurrentResourceState) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

}
