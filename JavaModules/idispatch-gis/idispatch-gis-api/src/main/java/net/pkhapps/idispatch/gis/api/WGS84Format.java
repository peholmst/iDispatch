package net.pkhapps.idispatch.gis.api;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * TODO Document me
 */
public interface WGS84Format {

    /**
     *
     */
    WGS84Format DD = new AbstractWGS84Format() {

        private double parse(@NotNull String coordinate, @NotNull Locale locale) {
            try {
                return NumberFormat.getInstance(locale).parse(coordinate.strip()).doubleValue();
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Error parsing coordinate string", ex);
            }
        }

        @Override
        public @NotNull String formatLatitude(double latitude, @NotNull Locale locale) {
            return String.format(locale, "%09.6f", latitude);
        }

        @Override
        public @NotNull String formatLongitude(double longitude, @NotNull Locale locale) {
            return String.format(locale, "%010.6f", longitude);
        }

        @Override
        public double parseLatitude(@NotNull String latitude, @NotNull Locale locale) {
            return validateLatitude(parse(latitude, locale));
        }

        @Override
        public double parseLongitude(@NotNull String longitude, @NotNull Locale locale) {
            return validateLongitude(parse(longitude, locale));
        }

        @Override
        public String toString() {
            return "DD";
        }
    };

    /**
     *
     */
    WGS84Format DDM = new AbstractWGS84Format() {

        private @NotNull Object[] splitToParts(double coordinate) {
            var degrees = (int) Math.floor(coordinate);
            var minutes = 60 * (coordinate - degrees);
            return new Object[]{degrees, minutes};
        }

        private double joinParts(@NotNull String coordinate, @NotNull Locale locale) {
            var parts = coordinate.split("°");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid coordinate string format: " + coordinate);
            }
            try {
                var degrees = NumberFormat.getIntegerInstance(locale).parse(parts[0].trim()).intValue();
                var minutes = NumberFormat.getNumberInstance(locale).parse(parts[1].trim()).doubleValue();
                return degrees + minutes / 60d;
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Error parsing coordinate string", ex);
            }
        }

        @Override
        public @NotNull String formatLatitude(double latitude, @NotNull Locale locale) {
            return String.format(locale, "%d° %.5f'", splitToParts(latitude));
        }

        @Override
        public @NotNull String formatLongitude(double longitude, @NotNull Locale locale) {
            return String.format(locale, "%03d° %.5f'", splitToParts(longitude));
        }

        @Override
        public double parseLatitude(@NotNull String latitude, @NotNull Locale locale) {
            return validateLatitude(joinParts(latitude, locale));
        }

        @Override
        public double parseLongitude(@NotNull String longitude, @NotNull Locale locale) {
            return validateLongitude(joinParts(longitude, locale));
        }

        @Override
        public String toString() {
            return "DDM";
        }
    };

    /**
     *
     */
    WGS84Format DMS = new AbstractWGS84Format() {

        private @NotNull Object[] splitToParts(double coordinate) {
            var degrees = (int) Math.floor(coordinate);
            var minutes = (int) Math.floor(60 * (coordinate - degrees));
            var seconds = 3600 * (coordinate - degrees) - 60 * minutes;
            return new Object[]{degrees, minutes, seconds};
        }

        private double joinParts(@NotNull String coordinate, @NotNull Locale locale) {
            var indexOfDegree = coordinate.indexOf("°");
            var indexOfMinutes = coordinate.indexOf("'");
            try {
                var degrees = NumberFormat.getIntegerInstance(locale).parse(coordinate.substring(0, indexOfDegree).trim()).intValue();
                var minutes = NumberFormat.getIntegerInstance(locale).parse(coordinate.substring(indexOfDegree + 1, indexOfMinutes).trim()).intValue();
                var seconds = NumberFormat.getNumberInstance(locale).parse(coordinate.substring(indexOfMinutes + 1).trim()).doubleValue();
                return degrees + minutes / 60d + seconds / 3600d;
            } catch (ParseException | IndexOutOfBoundsException ex) {
                throw new IllegalArgumentException("Error parsing coordinate string", ex);
            }
        }

        @Override
        public @NotNull String formatLatitude(double latitude, @NotNull Locale locale) {
            return String.format(locale, "%d° %d' %.3f\"", splitToParts(latitude));
        }

        @Override
        public @NotNull String formatLongitude(double longitude, @NotNull Locale locale) {
            return String.format(locale, "%03d° %d' %.3f\"", splitToParts(longitude));
        }

        @Override
        public double parseLatitude(@NotNull String latitude, @NotNull Locale locale) {
            return validateLatitude(joinParts(latitude, locale));
        }

        @Override
        public double parseLongitude(@NotNull String longitude, @NotNull Locale locale) {
            return validateLongitude(joinParts(longitude, locale));
        }

        @Override
        public String toString() {
            return "DMS";
        }
    };

    /**
     * @return
     */
    static @NotNull WGS84Format[] formats() {
        return new WGS84Format[]{DD, DDM, DMS};
    }

    /**
     * @param latitude
     * @param locale
     * @return
     */
    @NotNull String formatLatitude(double latitude, @NotNull Locale locale);

    /**
     * @param longitude
     * @param locale
     * @return
     */
    @NotNull String formatLongitude(double longitude, @NotNull Locale locale);

    /**
     * @param latitude
     * @param locale
     * @return
     */
    double parseLatitude(@NotNull String latitude, @NotNull Locale locale);

    /**
     * @param longitude
     * @param locale
     * @return
     */
    double parseLongitude(@NotNull String longitude, @NotNull Locale locale);
}
