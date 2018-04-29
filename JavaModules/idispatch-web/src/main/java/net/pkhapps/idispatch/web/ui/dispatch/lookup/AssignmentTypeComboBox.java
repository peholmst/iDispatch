package net.pkhapps.idispatch.web.ui.dispatch.lookup;

import net.pkhapps.idispatch.application.lookup.AssignmentTypeLookupDTO;
import net.pkhapps.idispatch.application.lookup.AssignmentTypeLookupService;
import net.pkhapps.idispatch.domain.assignment.AssignmentTypeId;
import net.pkhapps.idispatch.web.ui.common.AbstractLookupComboBox;
import org.springframework.lang.NonNull;

/**
 * Combo box for looking up/selecting assignment types.
 */
public class AssignmentTypeComboBox extends AbstractLookupComboBox<AssignmentTypeId, AssignmentTypeLookupDTO> {

    public AssignmentTypeComboBox(@NonNull AssignmentTypeLookupService lookupService) {
        super(lookupService);
    }
}
