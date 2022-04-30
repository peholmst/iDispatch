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

import org.geotools.geometry.DirectPosition2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoordinateUtilsTest {

    @Test
    void transform_directPosition() {
        var wgs84 = new DirectPosition2D(CRS.WGS84, 22.292882513, 60.299029094);
        var tm35fin = CoordinateUtils.transform(wgs84, CRS.TM35FIN);
        assertEquals(6694000, (int) tm35fin.getOrdinate(1));
        assertEquals(239969, (int) tm35fin.getOrdinate(0));
        assertEquals(CRS.TM35FIN, tm35fin.getCoordinateReferenceSystem());
    }
}
