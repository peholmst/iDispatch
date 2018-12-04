package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.shared.domain.base.BaseRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Repository interface for {@link Municipality}.
 */
public interface MunicipalityRepository extends BaseRepository<Long, MunicipalityId, Municipality> {

    @NotNull Optional<Municipality> findByCode(int code);

    boolean existsByCode(int code);
}
