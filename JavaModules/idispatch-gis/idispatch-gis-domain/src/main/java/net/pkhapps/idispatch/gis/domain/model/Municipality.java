package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.shared.domain.base.BaseAggregateRoot;
import net.pkhapps.idispatch.shared.domain.model.Language;
import net.pkhapps.idispatch.shared.domain.model.MultilingualString;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.shared.domain.util.StringUtils.ensureMaxLength;

/**
 * {@code Municipality}-aggregates represent Finnish municipalities.
 */
@Entity
@Table(name = "municipality", schema = "gis")
public class Municipality extends BaseAggregateRoot<Long, MunicipalityId> {

    private static final int NAME_MAX_LENGTH = 200;

    @Column(name = "name_fin", nullable = false, length = NAME_MAX_LENGTH)
    private String nameFin;

    @Column(name = "name_swe", nullable = false, length = NAME_MAX_LENGTH)
    private String nameSwe;

    @Column(name = "material_import_id", nullable = false)
    private MaterialImportId materialImport;

    @SuppressWarnings("unused") // Used by JPA only
    protected Municipality() {
    }

    public Municipality(@NotNull MunicipalityId id, @NotNull String nameFin, @NotNull String nameSwe,
                        @NotNull MaterialImportId materialImport) {
        setId(Objects.requireNonNull(id, "id must not be null"));
        setNameFin(nameFin);
        setNameSwe(nameSwe);
        setMaterialImport(materialImport);
    }

    @NotNull
    public MultilingualString name() {
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
        this.materialImport = Objects.requireNonNull(materialImport, "materialImport must not be null");
    }
}
