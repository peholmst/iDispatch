package net.pkhapps.idispatch.gis.domain.model;

import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.shared.domain.model.Language;
import net.pkhapps.idispatch.shared.domain.model.MultilingualString;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.shared.domain.util.StringUtils.ensureMaxLength;

/**
 * {@code Municipality}-aggregates represent Finnish municipalities.
 */
@Entity
@Table(name = "municipality", schema = "gis")
public class Municipality extends ImportedGeographicalMaterial<Long, MunicipalityId> {

    private static final int NAME_MAX_LENGTH = 200;

    @Column(name = "code", nullable = false, unique = true)
    private int code;

    @Column(name = "name_fin", nullable = false, length = NAME_MAX_LENGTH)
    private String nameFin;

    @Column(name = "name_swe", nullable = false, length = NAME_MAX_LENGTH)
    private String nameSwe;

    @SuppressWarnings("unused") // Used by JPA only
    protected Municipality() {
    }

    public Municipality(int code, @NotNull String nameFin, @NotNull String nameSwe,
                        @NotNull LocalDate validFrom, @NotNull MaterialImportId materialImport) {
        setCode(code);
        setNameFin(nameFin);
        setNameSwe(nameSwe);
        setValidFrom(validFrom);
        setMaterialImport(materialImport);
    }

    public int code() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
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
}
