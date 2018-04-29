package net.pkhapps.idispatch.web.ui.dispatch.lookup;

import net.pkhapps.idispatch.application.lookup.MunicipalityLookupDTO;
import net.pkhapps.idispatch.application.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.domain.common.MunicipalityId;
import net.pkhapps.idispatch.web.ui.common.AbstractLookupComboBox;
import org.springframework.lang.NonNull;

/**
 * Combo box for looking up/selecting municipalities.
 */
public class MunicipalityLookupComboBox extends AbstractLookupComboBox<MunicipalityId, MunicipalityLookupDTO> {

    public MunicipalityLookupComboBox(@NonNull MunicipalityLookupService lookupService) {
        super(lookupService);
    }
}
