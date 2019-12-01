package net.pkhapps.idispatch.gis.api;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * TODO Document me
 */
public interface Etrs89Tm35FinFormat {

    /**
     *
     */
    Etrs89Tm35FinFormat DEFAULT = new Etrs89Tm35FinFormat() {

        private @NotNull String format(double coordinate, @NotNull Locale locale) {
            return String.format(locale, "%.3f", coordinate);
        }

        private double parse(@NotNull String coordinate, @NotNull Locale locale) {
            try {
                return NumberFormat.getInstance(locale).parse(coordinate.strip()).doubleValue();
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Error parsing coordinate string", ex);
            }
        }

        @Override
        public @NotNull String formatNorthing(double northing, @NotNull Locale locale) {
            return format(northing, locale);
        }

        @Override
        public @NotNull String formatEasting(double easting, @NotNull Locale locale) {
            return format(easting, locale);
        }

        @Override
        public double parseNorthing(@NotNull String northing, @NotNull Locale locale) {
            return parse(northing, locale);
        }

        @Override
        public double parseEasting(@NotNull String easting, @NotNull Locale locale) {
            return parse(easting, locale);
        }
    };

    /**
     * @param northing
     * @param locale
     * @return
     */
    @NotNull String formatNorthing(double northing, @NotNull Locale locale);

    /**
     * @param easting
     * @param locale
     * @return
     */
    @NotNull String formatEasting(double easting, @NotNull Locale locale);

    /**
     * @param northing
     * @param locale
     * @return
     */
    double parseNorthing(@NotNull String northing, @NotNull Locale locale);

    /**
     * @param easting
     * @param locale
     * @return
     */
    double parseEasting(@NotNull String easting, @NotNull Locale locale);
}
