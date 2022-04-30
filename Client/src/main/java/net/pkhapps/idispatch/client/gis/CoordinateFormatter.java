/*
 * iDispatch Client
 *
 * Copyright (c) 2022 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.pkhapps.idispatch.client.gis;

import org.geotools.referencing.CRS;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.geometry.DirectPosition;

public interface CoordinateFormatter {

    @NotNull String format(double ordinate);

    default @NotNull String format(double north, double east) {
        return "N%s, E%s".formatted(format(north), format(east));
    }

    default @NotNull String format(@NotNull Coordinate coordinate) {
        return format(coordinate.y, coordinate.x);
    }

    default @NotNull String format(@NotNull DirectPosition directPosition) {
        var axisOrder = CRS.getAxisOrder(directPosition.getCoordinateReferenceSystem());
        return switch (axisOrder) {
            case EAST_NORTH -> format(directPosition.getOrdinate(1), directPosition.getOrdinate(0));
            case NORTH_EAST -> format(directPosition.getOrdinate(0), directPosition.getOrdinate(1));
            default -> throw new IllegalArgumentException("Cannot format given position");
        };
    }
}
