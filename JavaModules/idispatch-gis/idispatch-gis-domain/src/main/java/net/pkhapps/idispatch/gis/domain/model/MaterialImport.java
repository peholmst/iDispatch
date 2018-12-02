package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.shared.domain.base.BaseAggregateRoot;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
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
@SequenceGenerator(name = "materialImportId", sequenceName = "material_import_id_seq", schema = "gis")
public class MaterialImport extends BaseAggregateRoot<Long, MaterialImportId> {

    @Column(name = "imported_on", nullable = false)
    private Instant importedOn;

    @Column(name = "source", nullable = false, length = 200)
    private String source;

    @SuppressWarnings("unused") // Used by JPA only
    protected MaterialImport() {
    }

    public MaterialImport(@NotNull Instant importedOn, @NotNull String source) {
        setImportedOn(importedOn);
        setSource(source);
    }

    @NotNull
    public Instant importedOn() {
        return importedOn;
    }

    private void setImportedOn(@NotNull Instant importedOn) {
        this.importedOn = requireNonNull(importedOn, "importedOn must not be null");
    }

    @NotNull
    public String source() {
        return source;
    }

    private void setSource(@NotNull String source) {
        this.source = ensureMaxLength(requireNonNull(source, "source must not be null"), 200);
    }
}
