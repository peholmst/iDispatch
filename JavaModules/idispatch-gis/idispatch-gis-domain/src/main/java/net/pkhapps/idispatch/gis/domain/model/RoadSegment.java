package net.pkhapps.idispatch.gis.domain.model;

import com.vividsolutions.jts.geom.LineString;
import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.gis.domain.model.identity.RoadSegmentId;
import net.pkhapps.idispatch.shared.domain.model.Language;
import net.pkhapps.idispatch.shared.domain.model.MultilingualString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.shared.domain.util.StringUtils.ensureMaxLength;

/**
 * TODO Document me
 */
@Entity
@Table(name = "road_segment", schema = "gis")
public class RoadSegment extends ImportedGeographicalMaterial<Long, RoadSegmentId> {

    private static final int NAME_MAX_LENGTH = 200;

    @Column(name = "gid", nullable = false)
    private long gid;

    @Column(name = "location_accuracy", nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationAccuracy locationAccuracy;

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
                       @NotNull Elevation elevation, @NotNull MunicipalityId municipality, @NotNull LocalDate validFrom,
                       @NotNull MaterialImportId materialImport) {
        super(validFrom, materialImport);
        setGid(gid);
        setLocationAccuracy(locationAccuracy);
        setLocation((LineString) location.clone());
        setElevation(elevation);
        setMunicipality(municipality);
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

    public void setLocationAccuracy(@NotNull LocationAccuracy locationAccuracy) {
        this.locationAccuracy = requireNonNull(locationAccuracy, "locationAccuracy must not be null");
    }

    public @NotNull LineString location() {
        return location;
    }

    public void setLocation(@NotNull LineString location) {
        this.location = requireNonNull(location, "location must not be null");
    }

    public @NotNull Elevation elevation() {
        return elevation;
    }

    public void setElevation(@NotNull Elevation elevation) {
        this.elevation = requireNonNull(elevation, "elevation must not be null");
    }

    public @NotNull Optional<Long> roadNumber() {
        return Optional.ofNullable(roadNumber);
    }

    public void setRoadNumber(@Nullable Long roadNumber) {
        this.roadNumber = roadNumber;
    }

    public @NotNull MultilingualString name() {
        return new MultilingualString.Builder()
                .with(Language.FINNISH, nameFin)
                .with(Language.SWEDISH, nameSwe)
                .build();
    }

    public void setNameFin(@NotNull String nameFin) {
        this.nameFin = ensureMaxLength(requireNonNull(nameFin, "nameFin must not be null"), NAME_MAX_LENGTH);
    }

    public void setNameSwe(@NotNull String nameSwe) {
        this.nameSwe = ensureMaxLength(requireNonNull(nameSwe, "nameFin must not be null"), NAME_MAX_LENGTH);
    }

    public @NotNull MunicipalityId municipality() {
        return municipality;
    }

    public void setMunicipality(@NotNull MunicipalityId municipality) {
        this.municipality = requireNonNull(municipality, "municipality must not be null");
    }

    public @NotNull Optional<Integer> minAddressNumberLeft() {
        return Optional.ofNullable(minAddressNumberLeft);
    }

    public void setMinAddressNumberLeft(@Nullable Integer minAddressNumberLeft) {
        this.minAddressNumberLeft = minAddressNumberLeft;
    }

    public @NotNull Optional<Integer> maxAddressNumberLeft() {
        return Optional.ofNullable(maxAddressNumberLeft);
    }

    public void setMaxAddressNumberLeft(@Nullable Integer maxAddressNumberLeft) {
        this.maxAddressNumberLeft = maxAddressNumberLeft;
    }

    public @NotNull Optional<Integer> minAddressNumberRight() {
        return Optional.ofNullable(minAddressNumberRight);
    }

    public void setMinAddressNumberRight(@Nullable Integer minAddressNumberRight) {
        this.minAddressNumberRight = minAddressNumberRight;
    }

    public @NotNull Optional<Integer> maxAddressNumberRight() {
        return Optional.ofNullable(maxAddressNumberRight);
    }

    public void setMaxAddressNumberRight(@Nullable Integer maxAddressNumberRight) {
        this.maxAddressNumberRight = maxAddressNumberRight;
    }
    // TODO Methods for getting an approximate Point of a particular address number
}
