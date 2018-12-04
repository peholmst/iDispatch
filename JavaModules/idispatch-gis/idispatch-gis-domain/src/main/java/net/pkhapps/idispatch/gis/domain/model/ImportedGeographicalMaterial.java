package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.shared.domain.base.BaseAggregateRoot;
import net.pkhapps.idispatch.shared.domain.base.DomainObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for aggregates that have been imported from NLS / National Land Survey of Finland / Maanmittauslaitos.
 */
@MappedSuperclass
public abstract class ImportedGeographicalMaterial<ID extends Serializable, DomainId extends DomainObjectId<ID>>
        extends BaseAggregateRoot<ID, DomainId> {

    @Column(name = "material_import_id", nullable = false)
    private MaterialImportId materialImport;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    ImportedGeographicalMaterial() {
    }

    ImportedGeographicalMaterial(@NotNull LocalDate validFrom, @NotNull MaterialImportId materialImport) {
        setValidFrom(validFrom);
        setMaterialImport(materialImport);
    }

    public @NotNull MaterialImportId materialImport() {
        return materialImport;
    }

    private void setMaterialImport(@NotNull MaterialImportId materialImport) {
        this.materialImport = Objects.requireNonNull(materialImport, "materialImport must not be null");
    }

    public @NotNull LocalDate validFrom() {
        return validFrom;
    }

    private void setValidFrom(@NotNull LocalDate validFrom) {
        this.validFrom = Objects.requireNonNull(validFrom, "validFrom must not be null");
    }

    public @NotNull Optional<LocalDate> validTo() {
        return Optional.ofNullable(validTo);
    }

    public void setValidTo(@Nullable LocalDate validTo) {
        if (validTo != null && validTo.isBefore(validFrom)) {
            throw new IllegalArgumentException("validTo must not be before " + validFrom);
        }
        this.validTo = validTo;
    }
}
