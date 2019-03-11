package net.pkhapps.idispatch.cad.server.domain.resource;

import net.pkhapps.idispatch.cad.server.domain.AggregateRoot;
import net.pkhapps.idispatch.cad.server.domain.DbConstants;
import net.pkhapps.idispatch.cad.server.domain.gis.Location;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.cad.server.util.Strings.requireMaxLength;

/**
 * Aggregate root representing a station where resources are quartered.
 */
@Entity
@Table(name = "station", schema = DbConstants.SCHEMA_NAME)
public class Station extends AggregateRoot<Station> {

    @Column(name = "name_swe", nullable = false)
    private String nameSwe;
    @Column(name = "name_fin", nullable = false)
    private String nameFin;
    @Column(name = "is_active", nullable = false)
    private boolean active;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    @Column(name = "note")
    private String note;

    Station() {
    }

    public Station(@NonNull String name, @NonNull Location location) {
        this(name, name, location);
    }

    public Station(@NonNull String nameSwe, @NonNull String nameFin, @NonNull Location location) {
        setNameSwe(nameSwe);
        setNameFin(nameFin);
        setLocation(location);
        setActive(true);
    }

    public void setName(@NonNull String name) {
        setNameSwe(name);
        setNameFin(name);
    }

    @NonNull
    public String getNameSwe() {
        return nameSwe;
    }

    public void setNameSwe(@NonNull String nameSwe) {
        this.nameSwe = requireMaxLength(requireNonNull(nameSwe), DbConstants.DEFAULT_STRING_MAX_LENGTH);
    }

    @NonNull
    public String getNameFin() {
        return nameFin;
    }

    public void setNameFin(@NonNull String nameFin) {
        this.nameFin = requireMaxLength(requireNonNull(nameFin), DbConstants.DEFAULT_STRING_MAX_LENGTH);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @NonNull
    public Location getLocation() {
        return location;
    }

    public void setLocation(@NonNull Location location) {
        this.location = requireNonNull(location);
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = requireMaxLength(note, DbConstants.DEFAULT_STRING_MAX_LENGTH);
    }
}
