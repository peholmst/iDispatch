package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.shared.domain.base.ValueObject;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

/**
 * Base class for data that has been imported from NLS / National Land Survey of Finland / Maanmittauslaitos.
 */
@MappedSuperclass
public abstract class ImportedGeographicalMaterial implements ValueObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused")
    private long id;

    @Column(name = "material_import_id", nullable = false)
    private MaterialImportId materialImport;

    public @NotNull MaterialImportId materialImport() {
        return materialImport;
    }

    void setMaterialImport(@NotNull MaterialImportId materialImport) {
        this.materialImport = Objects.requireNonNull(materialImport, "materialImport must not be null");
    }
}
