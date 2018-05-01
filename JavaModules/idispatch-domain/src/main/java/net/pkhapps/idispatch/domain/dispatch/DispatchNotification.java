package net.pkhapps.idispatch.domain.dispatch;

import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.assignment.AssignmentTypeId;
import net.pkhapps.idispatch.domain.assignment.AssignmentPriority;
import net.pkhapps.idispatch.domain.base.AbstractAggregateRoot;
import net.pkhapps.idispatch.domain.common.MunicipalityId;
import net.pkhapps.idispatch.domain.resource.ResourceId;

import javax.persistence.*;
import java.util.Set;

/**
 * TODO Document me
 */
@Entity
@Table(name = "dispatch_notifications")
public class DispatchNotification extends AbstractAggregateRoot<DispatchNotificationId> {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dispatch_notification_assigned_resources",
            joinColumns = @JoinColumn(name = "dispatch_notification_id"))
    @Column(name = "resource_id", nullable = false)
    private Set<ResourceId> assignedResources;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dispatch_notification_destinations",
            joinColumns = @JoinColumn(name = "dispatch_notification_id"))
    @Column(name = "destination_id", nullable = false)
    private Set<DestinationId> destinations;

    @Column(name = "assignment_id", nullable = false)
    private AssignmentId assignment;

    @Column(name = "assignment_municipality_id", nullable = false)
    private MunicipalityId municipality;

    @Column(name = "assignment_address", nullable = false)
    private String address;

    @Column(name = "assignment_type_id", nullable = false)
    private AssignmentTypeId assignmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_urgency", nullable = false)
    private AssignmentPriority urgency;

    @Column(name = "assignment_description", nullable = false)
    private String description;

    @SuppressWarnings("unused")
    private DispatchNotification() {
        // Used by JPA only.
    }

    // TODO Implement init constructor


}
