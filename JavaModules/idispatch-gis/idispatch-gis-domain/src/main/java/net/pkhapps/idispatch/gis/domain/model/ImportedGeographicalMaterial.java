package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.shared.domain.base.ValueObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Base class for aggregates that have been imported from NLS / National Land Survey of Finland / Maanmittauslaitos.
 */
@MappedSuperclass
public abstract class ImportedGeographicalMaterial implements ValueObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused")
    private long id;

    @Column(name = "gid", nullable = false)
    private long gid;

    @Column(name = "location_accuracy", nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationAccuracy locationAccuracy;

    @Column(name = "material_import_id", nullable = false)
    private MaterialImportId materialImport;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    ImportedGeographicalMaterial() {
    }

    ImportedGeographicalMaterial(long gid, LocationAccuracy locationAccuracy, @NotNull LocalDate validFrom,
                                 @Nullable LocalDate validTo) {
        setGid(gid);
        setLocationAccuracy(locationAccuracy);
        setValidFrom(validFrom);
        setValidTo(validTo);
    }

    public long gid() {
        return gid;
    }

    private void setGid(long gid) {
        this.gid = gid;
    }

    public @NotNull LocationAccuracy locationAccuracy() {
        return locationAccuracy;
    }

    private void setLocationAccuracy(@NotNull LocationAccuracy locationAccuracy) {
        this.locationAccuracy = requireNonNull(locationAccuracy, "locationAccuracy must not be null");
    }

    public @NotNull MaterialImportId materialImport() {
        return materialImport;
    }

    void setMaterialImport(@NotNull MaterialImportId materialImport) {
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

    private void setValidTo(@Nullable LocalDate validTo) {
        if (validTo != null && validTo.isBefore(validFrom)) {
            throw new IllegalArgumentException("validTo must not be before " + validFrom);
        }
        this.validTo = validTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportedGeographicalMaterial that = (ImportedGeographicalMaterial) o;
        return gid == that.gid &&
                locationAccuracy == that.locationAccuracy &&
                Objects.equals(validFrom, that.validFrom) &&
                Objects.equals(validTo, that.validTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gid, locationAccuracy, validFrom, validTo);
    }
}
