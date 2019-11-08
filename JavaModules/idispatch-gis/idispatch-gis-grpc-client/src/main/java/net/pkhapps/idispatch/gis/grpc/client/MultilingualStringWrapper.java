package net.pkhapps.idispatch.gis.grpc.client;

import net.pkhapps.idispatch.gis.api.Locales;
import net.pkhapps.idispatch.gis.api.lookup.NamedFeature;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 */
class MultilingualStringWrapper implements NamedFeature {

    private final GIS.MultilingualString message;

    MultilingualStringWrapper(@NotNull GIS.MultilingualString message) {
        this.message = requireNonNull(message);
    }

    private static boolean hasSameLanguage(@NotNull Locale l1, @NotNull Locale l2) {
        return l1.getLanguage().equals(l2.getLanguage());
    }

    @Override
    public @NotNull Optional<String> getName(@NotNull Locale locale) {
        if (hasSameLanguage(locale, Locales.FINNISH)) {
            return Optional.ofNullable(message.getFin());
        } else if (hasSameLanguage(locale, Locales.SWEDISH)) {
            return Optional.ofNullable(message.getSwe());
        } else if (hasSameLanguage(locale, Locales.NORTHERN_SAMI)) {
            return Optional.ofNullable(message.getSme());
        } else if (hasSameLanguage(locale, Locales.INARI_SAMI)) {
            return Optional.ofNullable(message.getSmn());
        } else if (hasSameLanguage(locale, Locales.SKOLT_SAMI)) {
            return Optional.ofNullable(message.getSms());
        } else {
            return Optional.empty();
        }
    }
}
