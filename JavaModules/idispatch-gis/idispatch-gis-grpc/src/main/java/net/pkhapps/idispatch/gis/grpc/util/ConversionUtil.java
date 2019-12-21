package net.pkhapps.idispatch.gis.grpc.util;

import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import com.google.type.Date;
import net.pkhapps.idispatch.gis.api.Locales;
import net.pkhapps.idispatch.gis.api.lookup.AddressNumberRange;
import net.pkhapps.idispatch.gis.api.lookup.NamedFeature;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * TODO Document me
 */
public final class ConversionUtil {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    private ConversionUtil() {
    }

    public static @NotNull GIS.Coordinate toMessage(@NotNull Coordinate coordinate) {
        return GIS.Coordinate.newBuilder()
                .setX(coordinate.getX())
                .setY(coordinate.getY())
                .build();
    }

    public static @NotNull Coordinate fromMessage(@NotNull GIS.Coordinate coordinate) {
        return new Coordinate(coordinate.getX(), coordinate.getY());
    }

    public static @NotNull GIS.Point toMessage(@NotNull Point point) {
        return GIS.Point.newBuilder()
                .setCoordinate(toMessage(point.getCoordinate()))
                .setSrid(point.getSRID())
                .build();
    }

    public static @NotNull Point fromMessage(@NotNull GIS.Point point) {
        var p = GEOMETRY_FACTORY.createPoint(fromMessage(point.getCoordinate()));
        p.setSRID(point.getSrid());
        return p;
    }

    public static @NotNull GIS.LineString toMessage(@NotNull LineString lineString) {
        var builder = GIS.LineString.newBuilder();
        Stream.of(lineString.getCoordinates())
                .map(ConversionUtil::toMessage)
                .forEach(builder::addCoordinates);
        return builder
                .setSrid(lineString.getSRID())
                .build();
    }

    public static @NotNull LineString fromMessage(@NotNull GIS.LineString lineString) {
        throw new UnsupportedOperationException("Not implemented");// TODO Implement me
    }

    public static @NotNull GIS.Name toMessage(@NotNull NamedFeature namedFeature) {
        var builder = GIS.Name.newBuilder();
        namedFeature.getName(Locales.FINNISH).ifPresent(builder::setFin);
        namedFeature.getName(Locales.SWEDISH).ifPresent(builder::setSwe);
        namedFeature.getName(Locales.NORTHERN_SAMI).ifPresent(builder::setSme);
        namedFeature.getName(Locales.SKOLT_SAMI).ifPresent(builder::setSms);
        namedFeature.getName(Locales.INARI_SAMI).ifPresent(builder::setSmn);
        return builder.build();
    }

    public static @NotNull NamedFeature fromMessage(@NotNull GIS.Name name) {
        return new NamedFeature() {

            private boolean hasSameLanguage(@NotNull Locale l1, @NotNull Locale l2) {
                return l1.getLanguage().equals(l2.getLanguage());
            }

            @Override
            public @NotNull Optional<String> getName(@NotNull Locale locale) {
                if (hasSameLanguage(locale, Locales.FINNISH)) {
                    return Optional.ofNullable(name.getFin());
                } else if (hasSameLanguage(locale, Locales.SWEDISH)) {
                    return Optional.ofNullable(name.getSwe());
                } else if (hasSameLanguage(locale, Locales.NORTHERN_SAMI)) {
                    return Optional.ofNullable(name.getSme());
                } else if (hasSameLanguage(locale, Locales.INARI_SAMI)) {
                    return Optional.ofNullable(name.getSmn());
                } else if (hasSameLanguage(locale, Locales.SKOLT_SAMI)) {
                    return Optional.ofNullable(name.getSms());
                } else {
                    return Optional.empty();
                }
            }
        };
    }

    public static @NotNull Date toMessage(@NotNull LocalDate localDate) {
        return Date.newBuilder()
                .setYear(localDate.getYear())
                .setMonth(localDate.getMonthValue())
                .setDay(localDate.getDayOfMonth())
                .build();
    }

    public static @NotNull LocalDate fromMessage(@NotNull Date date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public static @NotNull GIS.MunicipalityCode toMessage(@NotNull MunicipalityCode municipalityCode) {
        return GIS.MunicipalityCode.newBuilder()
                .setCode(municipalityCode.getCode())
                .build();
    }

    public static @NotNull MunicipalityCode fromMessage(@NotNull GIS.MunicipalityCode municipalityCode) {
        return MunicipalityCode.of(municipalityCode.getCode());
    }

    public static @NotNull GIS.AddressNumberRange toMessage(@NotNull AddressNumberRange addressNumberRange) {
        return GIS.AddressNumberRange.newBuilder()
                .setMax(addressNumberRange.getMaximum())
                .setMin(addressNumberRange.getMinimum())
                .build();
    }

    public static @NotNull AddressNumberRange fromMessage(@NotNull GIS.AddressNumberRange addressNumberRange) {
        return new AddressNumberRange() {
            @Override
            public int getMinimum() {
                return addressNumberRange.getMin();
            }

            @Override
            public int getMaximum() {
                return addressNumberRange.getMax();
            }
        };
    }

    public static @NotNull StringValue wrap(@NotNull String value) {
        return StringValue.newBuilder().setValue(value).build();
    }

    public static @NotNull Int32Value wrap(int value) {
        return Int32Value.newBuilder().setValue(value).build();
    }

    public static @NotNull Int64Value wrap(long value) {
        return Int64Value.newBuilder().setValue(value).build();
    }

    public static <T> @NotNull Stream<T> toStream(@NotNull Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.NONNULL), false);
    }
}
