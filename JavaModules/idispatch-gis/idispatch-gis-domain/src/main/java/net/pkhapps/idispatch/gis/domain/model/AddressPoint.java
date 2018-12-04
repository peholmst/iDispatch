package net.pkhapps.idispatch.gis.domain.model;

import com.vividsolutions.jts.geom.Point;
import net.pkhapps.idispatch.gis.domain.model.identity.AddressPointId;
import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
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
@Table(name = "address_point", schema = "gis")
public class AddressPoint extends ImportedGeographicalMaterial<Long, AddressPointId> {

    private static final int NUMBER_MAX_LENGTH = 50;
    private static final int NAME_MAX_LENGTH = 200;

    @Column(name = "gid", nullable = false)
    private long gid;

    @Column(name = "location_accuracy", nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationAccuracy locationAccuracy;

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
                        @NotNull MunicipalityId municipality, @NotNull LocalDate validFrom,
                        @NotNull MaterialImportId materialImport) {
        super(validFrom, materialImport);
        setGid(gid);
        setLocationAccuracy(locationAccuracy);
        setLocation((Point) location.clone());
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

    public @NotNull Point location() {
        return location;
    }

    public void setLocation(@NotNull Point location) {
        this.location = requireNonNull(location, "location must not be null");
    }

    public @NotNull Optional<String> number() {
        return Optional.ofNullable(number);
    }

    public void setNumber(@Nullable String number) {
        this.number = number;
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
}
