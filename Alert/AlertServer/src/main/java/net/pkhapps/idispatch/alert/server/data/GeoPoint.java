// iDispatch Alert Server
// Copyright (C) 2021 Petter Holmström
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.pkhapps.idispatch.alert.server.data;

/**
 * A value object used to represent a 2D-geographical point that is CRS agnostic (but typically you would use WGS-84).
 *
 * @param lat the latitude coordinate in degrees.
 * @param lon the longitude coordinate in degrees.
 */
public record GeoPoint(double lat, double lon) {

    public GeoPoint {
        if (lat < -90.0 || lat > 90.0) {
            throw new IllegalArgumentException("Latitude must be within the interval [-90, 90] degrees");
        }
        if (lon < -180.0 || lon > 180.0) {
            throw new IllegalArgumentException("Longitude must be within the interval [-180, 180] degrees");
        }
    }

    /**
     * Creates a new {@code GeoPoint} with the given coordinates.
     *
     * @param lat the latitude coordinate.
     * @param lon the longitude coordinate.
     * @return a new {@code GeoPoint} object.
     */
    public static GeoPoint fromLatLon(double lat, double lon) {
        return new GeoPoint(lat, lon);
    }
}
