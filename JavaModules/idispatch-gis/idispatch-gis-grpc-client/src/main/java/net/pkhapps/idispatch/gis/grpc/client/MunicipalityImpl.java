package net.pkhapps.idispatch.gis.grpc.client;

import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

import java.util.Locale;
import java.util.Optional;

/**
 * TODO Document me
 */
class MunicipalityImpl implements Municipality {

    private final MunicipalityCode nationalCode;
    private final Point center;
    private final MultilingualStringWrapper name;

    MunicipalityImpl(@NotNull GIS.Municipality message) {
        this.nationalCode = MunicipalityCode.of(message.getNationalCode());
        this.center = GeometryConverter.fromMessage(message.getCenter());
        this.name = new MultilingualStringWrapper(message.getName());
    }

    @Override
    public @NotNull MunicipalityCode getNationalCode() {
        return nationalCode;
    }

    @Override
    public @NotNull Point getCenter() {
        return center;
    }

    @Override
    public @NotNull Optional<String> getName(@NotNull Locale locale) {
        return name.getName(locale);
    }
}
