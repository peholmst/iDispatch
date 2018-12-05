package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.shared.domain.base.IdentifiableDomainObjectRepository;

/**
 * Repository interface for {@link Municipality}.
 */
public interface MunicipalityRepository extends IdentifiableDomainObjectRepository<Integer, MunicipalityId, Municipality> {
}
