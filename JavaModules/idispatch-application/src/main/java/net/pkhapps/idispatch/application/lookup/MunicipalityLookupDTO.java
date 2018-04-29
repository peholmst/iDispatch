package net.pkhapps.idispatch.application.lookup;

import net.pkhapps.idispatch.domain.common.MunicipalityId;

public class MunicipalityLookupDTO extends AbstractLookupDTO<MunicipalityId> {

    public MunicipalityLookupDTO(MunicipalityId municipalityId, String displayName) {
        super(municipalityId, displayName);
    }
}
