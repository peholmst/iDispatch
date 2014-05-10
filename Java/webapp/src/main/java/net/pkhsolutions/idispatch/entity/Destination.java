package net.pkhsolutions.idispatch.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Objects.firstNonNull;

/**
 * Base class for a destination that receives dispatch notifications.
 */
@Entity
@Table(name = "destinations")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Destination extends AbstractLockableEntity implements Deactivatable {

    public static final String PROP_RESOURCES = "resources";

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "destination_resources",
            joinColumns = @JoinColumn(name = "destination_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @NotEmpty(message = "Please select at least one resource")
    private Set<Resource> resources = new HashSet<>();

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = firstNonNull(resources, new HashSet<Resource>());
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final Destination clone = (Destination) super.clone();
        clone.resources = new HashSet<>(resources);
        return clone;
    }
}
