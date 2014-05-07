package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableSet;

/**
 * TODO Document me
 */
@Entity
@Table(name = "dispatch_notifications")
public class DispatchNotification extends AbstractTimestampedEntity {

    @ManyToMany
    @JoinTable(name = "dispatched_notification_resources",
            joinColumns = @JoinColumn(name = "dispatch_notification_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private Set<Resource> assignedResources;

    @ManyToMany
    @JoinTable(name = "dispatch_notification_destinations",
            joinColumns = @JoinColumn(name = "dispatch_notification_id"),
            inverseJoinColumns = @JoinColumn(name = "destination_id"))
    private Set<Destination> destinations;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignment_municipality_id", nullable = false)
    private Municipality municipality;

    @Column(name = "assignment_address", nullable = false)
    private String address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignment_type_id", nullable = false)
    private AssignmentType assignmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_urgency", nullable = false)
    private AssignmentUrgency urgency;

    @Column(name = "assignment_description", nullable = false)
    private String description;

    protected DispatchNotification() {
    }

    public DispatchNotification(Assignment assignment, Collection<Resource> assignedResources, Collection<Destination> destinations) {
        this.assignedResources = newHashSet(checkNotNull(assignedResources));
        this.destinations = newHashSet(checkNotNull(destinations));
        this.assignment = checkNotNull(assignment);
        municipality = assignment.getMunicipality();
        address = assignment.getAddress();
        assignmentType = assignment.getType();
        urgency = assignment.getUrgency();
        description = assignment.getDescription();
    }

    public Set<Resource> getAssignedResources() {
        return unmodifiableSet(assignedResources);
    }

    public Set<Destination> getDestinations() {
        return unmodifiableSet(destinations);
    }

    public <D extends Destination> Set<D> getDestinationsOfType(Class<D> type) {
        return destinations.stream().filter(type::isInstance).map(type::cast).collect(Collectors.toSet());
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public String getAddress() {
        return address;
    }

    public AssignmentType getAssignmentType() {
        return assignmentType;
    }

    public AssignmentUrgency getUrgency() {
        return urgency;
    }

    public String getDescription() {
        return description;
    }
}
