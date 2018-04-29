package net.pkhapps.idispatch.application.overview;

import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.resource.ResourceId;
import net.pkhapps.idispatch.domain.status.ResourceState;

import java.io.Serializable;
import java.time.Instant;

public class ResourceOverviewDTO implements Serializable {

    private ResourceId resourceId;
    private String callSign;
    private ResourceState state;
    private Instant lastUpdated;
    private AssignmentId assignmentId;
    private String assignmentSummary;

    public ResourceId getResourceId() {
        return resourceId;
    }

    public void setResourceId(ResourceId resourceId) {
        this.resourceId = resourceId;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public ResourceState getState() {
        return state;
    }

    public void setState(ResourceState state) {
        this.state = state;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public AssignmentId getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(AssignmentId assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentSummary() {
        return assignmentSummary;
    }

    public void setAssignmentSummary(String assignmentSummary) {
        this.assignmentSummary = assignmentSummary;
    }
}
