package net.pkhapps.idispatch.core.client.address;

import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public abstract class Address {

    private final Point location;
    private final MunicipalityCode municipality;

    /**
     * @param location
     * @param municipality
     */
    protected Address(@NotNull Point location, @Nullable MunicipalityCode municipality) {
        this.location = requireNonNull(location);
        this.municipality = municipality;
    }

    /**
     * @return
     */
    public @NotNull Point getLocation() {
        return (Point) location.copy();
    }

    /**
     * @return
     */
    public @NotNull Optional<MunicipalityCode> getMunicipality() {
        return Optional.ofNullable(municipality);
    }
}
