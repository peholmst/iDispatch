package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base interface for repositories of {@link ImportedGeographicalMaterial}. Please note that the objects returned are
 * modeled as value objects, meaning they should never be references. Clients should never access these repositories
 * directly but use a dedicated query API instead.
 *
 * @param <T> the type of {@link ImportedGeographicalMaterial}.
 */
@NoRepositoryBean
interface ImportedGeographicalMaterialRepository<T extends ImportedGeographicalMaterial>
        extends JpaRepository<T, Long> {

    void deleteByMaterialImport(MaterialImportId materialImport);
}
