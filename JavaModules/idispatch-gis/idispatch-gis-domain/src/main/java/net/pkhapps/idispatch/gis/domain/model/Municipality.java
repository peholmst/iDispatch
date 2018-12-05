package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.shared.domain.base.IdentifiableDomainObject;
import net.pkhapps.idispatch.shared.domain.model.Language;
import net.pkhapps.idispatch.shared.domain.model.MultilingualString;
import net.pkhapps.idispatch.shared.domain.model.StandardType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.shared.domain.util.StringUtils.ensureMaxLength;

/**
 * {@code Municipality}-aggregates represent Finnish municipalities.
 */
@Entity
@Table(name = "municipality", schema = "gis")
public class Municipality implements IdentifiableDomainObject<MunicipalityId>, StandardType<MunicipalityId> {

    private static final int NAME_MAX_LENGTH = 200;

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "name_fin", nullable = false, length = NAME_MAX_LENGTH)
    private String nameFin;

    @Column(name = "name_swe", nullable = false, length = NAME_MAX_LENGTH)
    private String nameSwe;

    @Column(name = "material_import_id", nullable = false)
    private MaterialImportId materialImport;

    @SuppressWarnings("unused")
        // Used by JPA only
    Municipality() {
    }

    public Municipality(int id, @NotNull String nameFin, @NotNull String nameSwe,
                        @NotNull MaterialImportId materialImport) {
        this.id = id;
        setNameFin(nameFin);
        setNameSwe(nameSwe);
        setMaterialImport(materialImport);
    }

    @NotNull
    @Override
    public MunicipalityId id() {
        return new MunicipalityId(id);
    }

    @Override
    public boolean hasId() {
        return id != 0;
    }

    @Override
    public @NotNull MultilingualString name() {
        return new MultilingualString.Builder()
                .with(Language.FINNISH, nameFin)
                .with(Language.SWEDISH, nameSwe)
                .build();
    }

    private void setNameFin(@NotNull String nameFin) {
        this.nameFin = ensureMaxLength(requireNonNull(nameFin, "nameFin must not be null"), NAME_MAX_LENGTH);
    }

    private void setNameSwe(@NotNull String nameSwe) {
        this.nameSwe = ensureMaxLength(requireNonNull(nameSwe, "nameSwe must not be null"), NAME_MAX_LENGTH);
    }

    @NotNull
    public MaterialImportId materialImport() {
        return materialImport;
    }

    private void setMaterialImport(@NotNull MaterialImportId materialImport) {
        this.materialImport = requireNonNull(materialImport, "materialImport must not be null");
    }

    @Override
    public String toString() {
        return String.format("%s@%d[nameFin=%s, nameSwe=%s]", getClass().getSimpleName(), id, nameFin, nameSwe);
    }
}
