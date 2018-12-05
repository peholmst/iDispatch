package net.pkhapps.idispatch.gis.domain.model;

import com.vividsolutions.jts.geom.Point;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.shared.domain.model.Language;
import net.pkhapps.idispatch.shared.domain.model.MultilingualString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.shared.domain.util.StringUtils.ensureMaxLength;

/**
 * TODO Document me
 */
@Entity
@Table(name = "address_point", schema = "gis")
public class AddressPoint extends ImportedGeographicalMaterial {

    private static final int NUMBER_MAX_LENGTH = 50;
    private static final int NAME_MAX_LENGTH = 200;

    @Column(name = "location", nullable = false)
    private Point location;

    @Column(name = "number", length = NUMBER_MAX_LENGTH)
    private String number;

    @Column(name = "name_fin", length = NAME_MAX_LENGTH)
    private String nameFin;

    @Column(name = "name_swe", length = NAME_MAX_LENGTH)
    private String nameSwe;

    @Column(name = "municipality_id", nullable = false)
    private MunicipalityId municipality;

    @SuppressWarnings("unused")
        // Used by JPA only
    AddressPoint() {
    }

    public AddressPoint(long gid, @NotNull LocationAccuracy locationAccuracy, @NotNull Point location,
                        @Nullable String number, @Nullable String nameSwe, @Nullable String nameFin,
                        @NotNull MunicipalityId municipality, @NotNull LocalDate validFrom, @Nullable LocalDate validTo) {
        super(gid, locationAccuracy, validFrom, validTo);
        setLocation(location);
        setNumber(number);
        setNameFin(nameFin);
        setNameSwe(nameSwe);
        setMunicipality(municipality);
    }

    public @NotNull Point location() {
        return (Point) location.clone();
    }

    private void setLocation(@NotNull Point location) {
        this.location = (Point) requireNonNull(location, "location must not be null").clone();
    }

    public @NotNull Optional<String> number() {
        return Optional.ofNullable(number);
    }

    private void setNumber(@Nullable String number) {
        this.number = number;
    }

    public @NotNull Optional<MultilingualString> name() {
        return new MultilingualString.Builder()
                .with(Language.FINNISH, nameFin)
                .with(Language.SWEDISH, nameSwe)
                .buildOptional();
    }

    private void setNameFin(@Nullable String nameFin) {
        this.nameFin = ensureMaxLength(nameFin, NAME_MAX_LENGTH);
    }

    private void setNameSwe(@Nullable String nameSwe) {
        this.nameSwe = ensureMaxLength(nameSwe, NAME_MAX_LENGTH);
    }

    public @NotNull MunicipalityId municipality() {
        return municipality;
    }

    private void setMunicipality(@NotNull MunicipalityId municipality) {
        this.municipality = requireNonNull(municipality, "municipality must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AddressPoint that = (AddressPoint) o;
        return Objects.equals(location, that.location) &&
                Objects.equals(number, that.number) &&
                Objects.equals(nameFin, that.nameFin) &&
                Objects.equals(nameSwe, that.nameSwe) &&
                Objects.equals(municipality, that.municipality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location, number, nameFin, nameSwe, municipality);
    }
}
