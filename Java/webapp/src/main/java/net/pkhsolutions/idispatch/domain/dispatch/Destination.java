package net.pkhsolutions.idispatch.domain.dispatch;

import net.pkhsolutions.idispatch.domain.AbstractLockableEntity;
import net.pkhsolutions.idispatch.domain.resources.Resource;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Objects.firstNonNull;

@Entity
@Table(name = "destinations")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Destination extends AbstractLockableEntity {

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ManyToMany
    @JoinTable(name = "destination_resources",
            joinColumns = @JoinColumn(name = "destination_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private Set<Resource> resources = new HashSet<>();

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = firstNonNull(resources, new HashSet<Resource>());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
