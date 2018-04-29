package net.pkhapps.idispatch.application.lookup;

import net.pkhapps.idispatch.domain.assignment.AssignmentTypeId;

public class AssignmentTypeLookupDTO extends AbstractLookupDTO<AssignmentTypeId> {

    public AssignmentTypeLookupDTO(AssignmentTypeId assignmentTypeId, String displayName) {
        super(assignmentTypeId, displayName);
    }
}
