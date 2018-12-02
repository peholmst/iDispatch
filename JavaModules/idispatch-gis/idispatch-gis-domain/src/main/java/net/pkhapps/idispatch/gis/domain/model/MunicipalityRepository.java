package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.shared.domain.base.BaseRepository;

/**
 * Repository interface for {@link Municipality}.
 */
public interface MunicipalityRepository extends BaseRepository<Long, MunicipalityId, Municipality> {

    boolean existsByCode(int code);
}
