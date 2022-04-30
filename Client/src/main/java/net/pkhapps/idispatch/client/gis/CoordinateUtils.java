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

import org.geotools.geometry.DirectPosition1D;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.DirectPosition3D;
import org.geotools.geometry.jts.JTS;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public final class CoordinateUtils {

    private CoordinateUtils() {
    }

    private static @NotNull MathTransform findMathTransform(@NotNull CoordinateReferenceSystem source,
                                                            @NotNull CoordinateReferenceSystem target) {
        try {
            return org.geotools.referencing.CRS.findMathTransform(source, target);
        } catch (FactoryException ex) {
            throw new IllegalArgumentException("Could not find suitable math transform", ex);
        }
    }

    public static @NotNull DirectPosition transform(@NotNull DirectPosition source,
                                                    @NotNull CoordinateReferenceSystem targetCRS) {
        if (source.getCoordinateReferenceSystem().equals(targetCRS)) {
            return source;
        }
        try {
            DirectPosition destination = switch (source.getDimension()) {
                case 1 -> new DirectPosition1D(targetCRS);
                case 2 -> new DirectPosition2D(targetCRS);
                case 3 -> new DirectPosition3D(targetCRS);
                default -> throw new IllegalArgumentException("Unsupported dimension");
            };
            return findMathTransform(source.getCoordinateReferenceSystem(), targetCRS).transform(source, destination);
        } catch (TransformException ex) {
            throw new IllegalArgumentException("Could not transform position", ex);
        }
    }

    public static @NotNull Coordinate transform(@NotNull Coordinate source,
                                                @NotNull CoordinateReferenceSystem sourceCRS,
                                                @NotNull CoordinateReferenceSystem targetCRS) {
        if (sourceCRS.equals(targetCRS)) {
            return source;
        }
        try {
            return JTS.transform(source, null, findMathTransform(sourceCRS, targetCRS));
        } catch (TransformException ex) {
            throw new IllegalArgumentException("Could not transform point", ex);
        }
    }
}
