package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;

/**
 * Base entity that contains a state of a {@link net.pkhsolutions.idispatch.entity.Resource}.
 */
@MappedSuperclass
public abstract class AbstractResourceStateChange extends AbstractTimestampedEntity {

    public static final String PROP_STATE = "state";
    public static final String PROP_RESOURCE = "resource";
    public static final String PROP_ASSIGNMENT = "assignment";

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ResourceState state = ResourceState.OUT_OF_SERVICE;
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    protected AbstractResourceStateChange() {
    }

    public abstract Resource getResource();

    public ResourceState getState() {
        return state;
    }

    protected void setState(ResourceState state) {
        this.state = state;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    protected void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
