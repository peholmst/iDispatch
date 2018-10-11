package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.AssignmentType;
import net.pkhsolutions.idispatch.entity.AssignmentUrgency;
import net.pkhsolutions.idispatch.entity.Municipality;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ReportDTO implements Serializable {

    private final Assignment assignment;

    private final List<ReportResourceDTO> resources;

    public ReportDTO(Assignment assignment, List<ReportResourceDTO> resources) {
        this.assignment = assignment;
        this.resources = Collections.unmodifiableList(resources);
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public Date getOpened() {
        return assignment.getOpened();
    }

    public Date getClosed() {
        return assignment.getClosed();
    }

    public AssignmentUrgency getUrgency() {
        return assignment.getUrgency();
    }

    public AssignmentType getType() {
        return assignment.getType();
    }

    public String getDescription() {
        return assignment.getDescription();
    }

    public Municipality getMunicipality() {
        return assignment.getMunicipality();
    }

    public String getAddress() {
        return assignment.getAddress();
    }

    public Long getNumber() {
        return assignment.getId();
    }

    public List<ReportResourceDTO> getResources() {
        return resources;
    }
}
