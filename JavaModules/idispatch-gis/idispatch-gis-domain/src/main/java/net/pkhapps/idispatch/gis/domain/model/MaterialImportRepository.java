package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.shared.domain.base.BaseRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Repository interface for {@link MaterialImport}.
 */
interface MaterialImportRepository extends BaseRepository<Long, MaterialImportId, MaterialImport> {

    @NotNull
    Optional<MaterialImport> findBySource(@NotNull String source);
}
