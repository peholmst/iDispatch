package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.shared.domain.base.BaseAggregateRoot;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.shared.domain.util.StringUtils.ensureMaxLength;

/**
 * {@code MaterialImport}s are used to keep track of when GIS material has been imported into the database and from
 * where.
 */
@Entity
@Table(name = "material_import", schema = "gis")
public class MaterialImport extends BaseAggregateRoot<Long, MaterialImportId> {

    private static final int SOURCE_MAX_LENGTH = 300;

    @Column(name = "imported_on", nullable = false)
    private Instant importedOn;

    @Column(name = "source", nullable = false, unique = true, length = SOURCE_MAX_LENGTH)
    private String source;

    @Column(name = "source_timestamp", nullable = false)
    private Instant sourceTimestamp;

    @SuppressWarnings("unused")
        // Used by JPA only
    MaterialImport() {
    }

    public MaterialImport(@NotNull Instant importedOn, @NotNull String source, @NotNull Instant sourceTimestamp) {
        setImportedOn(importedOn);
        setSource(source);
        setSourceTimestamp(sourceTimestamp);
    }

    public @NotNull Instant importedOn() {
        return importedOn;
    }

    private void setImportedOn(@NotNull Instant importedOn) {
        this.importedOn = requireNonNull(importedOn, "importedOn must not be null");
    }

    public @NotNull String source() {
        return source;
    }

    private void setSource(@NotNull String source) {
        this.source = ensureMaxLength(requireNonNull(source, "source must not be null"), SOURCE_MAX_LENGTH);
    }

    public @NotNull Instant sourceTimestamp() {
        return sourceTimestamp;
    }

    private void setSourceTimestamp(@NotNull Instant sourceTimestamp) {
        this.sourceTimestamp = requireNonNull(sourceTimestamp, "sourceTimestamp must not be null");
    }
}
