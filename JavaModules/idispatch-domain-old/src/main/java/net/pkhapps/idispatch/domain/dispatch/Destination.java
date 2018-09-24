package net.pkhapps.idispatch.domain.dispatch;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRoot;
import net.pkhapps.idispatch.domain.base.SupportsSoftDelete;
import net.pkhapps.idispatch.domain.resource.ResourceId;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for a destination that receives dispatch notifications.
 */
@Entity
@Table(name = "destinations")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Destination extends AbstractAggregateRoot<DestinationId> implements SupportsSoftDelete {

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ElementCollection
    @CollectionTable(name = "destination_resources", joinColumns = @JoinColumn(name = "destination_id"))
    @Column(name = "resource_id")
    private Set<ResourceId> resources = new HashSet<>();

    @NonNull
    public Set<ResourceId> getResources() {
        return Collections.unmodifiableSet(resources);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void undelete() {
        active = true;
    }

    @Override
    public void delete() {
        active = false;
    }
}
