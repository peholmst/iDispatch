package net.pkhapps.idispatch.gis.grpc.client;

import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.NamedFeature;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.util.ConversionUtil;
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
    private final NamedFeature name;

    MunicipalityImpl(@NotNull GIS.Municipality message) {
        this.nationalCode = ConversionUtil.fromMessage(message.getNationalCode());
        this.center = ConversionUtil.fromMessage(message.getCenter());
        this.name = ConversionUtil.fromMessage(message.getName());
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
