package net.pkhapps.idispatch.application.overview;

import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.assignment.AssignmentState;
import net.pkhapps.idispatch.domain.assignment.AssignmentTypeId;
import net.pkhapps.idispatch.domain.common.MunicipalityId;

import java.io.Serializable;
import java.time.Instant;

public class AssignmentOverviewDTO implements Serializable {

    private AssignmentId assignmentId;
    private AssignmentTypeId assignmentTypeId;
    private String assignmentTypeCode;
    private MunicipalityId municipalityId;
    private String municipalityName;
    private String address;
    private AssignmentState state;
    private Instant lastUpdated;

    public AssignmentId getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(AssignmentId assignmentId) {
        this.assignmentId = assignmentId;
    }

    public AssignmentTypeId getAssignmentTypeId() {
        return assignmentTypeId;
    }

    public void setAssignmentTypeId(AssignmentTypeId assignmentTypeId) {
        this.assignmentTypeId = assignmentTypeId;
    }

    public String getAssignmentTypeCode() {
        return assignmentTypeCode;
    }

    public void setAssignmentTypeCode(String assignmentTypeCode) {
        this.assignmentTypeCode = assignmentTypeCode;
    }

    public MunicipalityId getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(MunicipalityId municipalityId) {
        this.municipalityId = municipalityId;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AssignmentState getState() {
        return state;
    }

    public void setState(AssignmentState state) {
        this.state = state;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
