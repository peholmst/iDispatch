package net.pkhapps.idispatch.gis.api;

/**
 * TODO Document me!
 */
abstract class AbstractWGS84Format implements WGS84Format {

    double validateLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        return latitude;
    }

    double validateLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        return longitude;
    }
}
