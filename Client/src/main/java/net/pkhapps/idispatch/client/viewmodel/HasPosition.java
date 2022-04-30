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

package net.pkhapps.idispatch.client.viewmodel;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import net.pkhapps.idispatch.client.gis.CoordinateFormatter;
import net.pkhapps.idispatch.client.gis.CoordinateUtils;
import org.geotools.referencing.CRS;
import org.jetbrains.annotations.NotNull;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public interface HasPosition {

    @NotNull ReadOnlyObjectProperty<DirectPosition> position();

    default void copyPositionToClipboard(@NotNull CoordinateReferenceSystem crs,
                                         @NotNull CoordinateFormatter formatter) {
        var position = position().get();
        if (position != null) {
            var converted = CoordinateUtils.transform(position, crs);
            var content = new ClipboardContent();
            content.putString(formatter.format(converted));
            Clipboard.getSystemClipboard().setContent(content);
        }
    }

    default void copyNorthOrdinateToClipboard(@NotNull CoordinateReferenceSystem crs,
                                              @NotNull CoordinateFormatter formatter) {
        var position = position().get();
        if (position != null) {
            var converted = CoordinateUtils.transform(position, crs);
            copyOrdinate(converted, getNorthDimension(converted), formatter);
        }
    }


    default void copyEastOrdinateToClipboard(@NotNull CoordinateReferenceSystem crs,
                                             @NotNull CoordinateFormatter formatter) {
        var position = position().get();
        if (position != null) {
            var converted = CoordinateUtils.transform(position, crs);
            copyOrdinate(converted, getEastDimension(converted), formatter);
        }
    }

    private int getEastDimension(@NotNull DirectPosition position) {
        var axisOrder = CRS.getAxisOrder(position.getCoordinateReferenceSystem());
        return switch (axisOrder) {
            case EAST_NORTH -> 0;
            case NORTH_EAST -> 1;
            default -> throw new IllegalArgumentException("Cannot determine east dimension");
        };
    }

    private int getNorthDimension(@NotNull DirectPosition position) {
        var axisOrder = CRS.getAxisOrder(position.getCoordinateReferenceSystem());
        return switch (axisOrder) {
            case EAST_NORTH -> 1;
            case NORTH_EAST -> 0;
            default -> throw new IllegalArgumentException("Cannot determine north dimension");
        };
    }

    private void copyOrdinate(@NotNull DirectPosition position, int dimension, @NotNull CoordinateFormatter formatter) {
        var content = new ClipboardContent();
        content.putString(formatter.format(position.getOrdinate(dimension)));
        Clipboard.getSystemClipboard().setContent(content);
    }
}
