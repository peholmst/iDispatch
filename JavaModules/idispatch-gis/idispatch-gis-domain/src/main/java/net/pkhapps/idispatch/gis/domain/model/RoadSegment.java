package net.pkhapps.idispatch.gis.domain.model;

import com.vividsolutions.jts.geom.LineString;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.shared.domain.model.Language;
import net.pkhapps.idispatch.shared.domain.model.MultilingualString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.shared.domain.util.StringUtils.ensureMaxLength;

/**
 * TODO Document me
 */
@Entity
@Table(name = "road_segment", schema = "gis")
public class RoadSegment extends ImportedGeographicalMaterial {

    private static final int NAME_MAX_LENGTH = 200;

    @Column(name = "location", nullable = false)
    private LineString location;

    @Column(name = "elevation", nullable = false)
    @Enumerated(EnumType.STRING)
    private Elevation elevation;

    @Column(name = "road_number")
    private Long roadNumber;

    @Column(name = "name_fin", length = NAME_MAX_LENGTH)
    private String nameFin;

    @Column(name = "name_swe", length = NAME_MAX_LENGTH)
    private String nameSwe;

    @Column(name = "municipality_id", nullable = false)
    private MunicipalityId municipality;

    @Column(name = "min_addr_number_left")
    private Integer minAddressNumberLeft;

    @Column(name = "max_addr_number_left")
    private Integer maxAddressNumberLeft;

    @Column(name = "min_addr_number_right")
    private Integer minAddressNumberRight;

    @Column(name = "max_addr_number_right")
    private Integer maxAddressNumberRight;

    @SuppressWarnings("unused")
        // Used by JPA only
    RoadSegment() {
    }

    public RoadSegment(long gid, @NotNull LocationAccuracy locationAccuracy, @NotNull LineString location,
                       @NotNull Elevation elevation, @Nullable Long roadNumber, @Nullable String nameFin,
                       @Nullable String nameSwe, @NotNull MunicipalityId municipality,
                       @Nullable Integer minAddressNumberLeft, @Nullable Integer maxAddressNumberLeft,
                       @Nullable Integer minAddressNumberRight, @Nullable Integer maxAddressNumberRight,
                       @NotNull LocalDate validFrom, @Nullable LocalDate validTo) {
        super(gid, locationAccuracy, validFrom, validTo);
        setLocation(location);
        setElevation(elevation);
        setRoadNumber(roadNumber);
        setNameFin(nameFin);
        setNameSwe(nameSwe);
        setMinAddressNumberLeft(minAddressNumberLeft);
        setMaxAddressNumberLeft(maxAddressNumberLeft);
        setMinAddressNumberRight(minAddressNumberRight);
        setMaxAddressNumberRight(maxAddressNumberRight);
        setMunicipality(municipality);
    }

    public @NotNull LineString location() {
        return (LineString) location.clone();
    }

    private void setLocation(@NotNull LineString location) {
        this.location = (LineString) requireNonNull(location, "location must not be null").clone();
    }

    public @NotNull Elevation elevation() {
        return elevation;
    }

    private void setElevation(@NotNull Elevation elevation) {
        this.elevation = requireNonNull(elevation, "elevation must not be null");
    }

    public @NotNull Optional<Long> roadNumber() {
        return Optional.ofNullable(roadNumber);
    }

    private void setRoadNumber(@Nullable Long roadNumber) {
        this.roadNumber = roadNumber;
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

    public @NotNull Optional<Integer> minAddressNumberLeft() {
        return Optional.ofNullable(minAddressNumberLeft);
    }

    private void setMinAddressNumberLeft(@Nullable Integer minAddressNumberLeft) {
        this.minAddressNumberLeft = minAddressNumberLeft;
    }

    public @NotNull Optional<Integer> maxAddressNumberLeft() {
        return Optional.ofNullable(maxAddressNumberLeft);
    }

    private void setMaxAddressNumberLeft(@Nullable Integer maxAddressNumberLeft) {
        this.maxAddressNumberLeft = maxAddressNumberLeft;
    }

    public @NotNull Optional<Integer> minAddressNumberRight() {
        return Optional.ofNullable(minAddressNumberRight);
    }

    private void setMinAddressNumberRight(@Nullable Integer minAddressNumberRight) {
        this.minAddressNumberRight = minAddressNumberRight;
    }

    public @NotNull Optional<Integer> maxAddressNumberRight() {
        return Optional.ofNullable(maxAddressNumberRight);
    }

    private void setMaxAddressNumberRight(@Nullable Integer maxAddressNumberRight) {
        this.maxAddressNumberRight = maxAddressNumberRight;
    }
    // TODO Methods for getting an approximate Point of a particular address number


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RoadSegment that = (RoadSegment) o;
        return Objects.equals(location, that.location) &&
                elevation == that.elevation &&
                Objects.equals(roadNumber, that.roadNumber) &&
                Objects.equals(nameFin, that.nameFin) &&
                Objects.equals(nameSwe, that.nameSwe) &&
                Objects.equals(municipality, that.municipality) &&
                Objects.equals(minAddressNumberLeft, that.minAddressNumberLeft) &&
                Objects.equals(maxAddressNumberLeft, that.maxAddressNumberLeft) &&
                Objects.equals(minAddressNumberRight, that.minAddressNumberRight) &&
                Objects.equals(maxAddressNumberRight, that.maxAddressNumberRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location, elevation, roadNumber, nameFin, nameSwe, municipality,
                minAddressNumberLeft, maxAddressNumberLeft, minAddressNumberRight, maxAddressNumberRight);
    }
}
