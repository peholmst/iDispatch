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

import java.util.Locale;

public final class DegreeFormatters {

    public static final CoordinateFormatter DD = ordinate -> String.format(Locale.ROOT, "%.9f°", ordinate);
    public static final CoordinateFormatter DDM = ordinate -> {
        var degrees = (int) ordinate;
        var decimalMinutes = 60 * (ordinate - degrees);
        return String.format(Locale.ROOT, "%d°%.5f'", degrees, decimalMinutes);
    };
    public static final CoordinateFormatter DMS = ordinate -> {
        var degrees = (int) ordinate;
        var decimalMinutes = 60 * (ordinate - degrees);
        var minutes = (int) decimalMinutes;
        var seconds = 60 * (decimalMinutes - minutes);
        return String.format(Locale.ROOT, "%d°%d'%.3f\"", degrees, minutes, seconds);
    };

    private DegreeFormatters() {
    }
}
