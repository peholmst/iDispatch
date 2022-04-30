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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DegreeFormattersTest {

    @Test
    void dd() {
        var formatted = DegreeFormatters.DD.format(60.305358224, 22.295304155);
        assertEquals("N60.305358224°, E22.295304155°", formatted);
    }

    @Test
    void ddm() {
        var formatted = DegreeFormatters.DDM.format(60.305358224, 22.295304155);
        assertEquals("N60°18.32149', E22°17.71825'", formatted);
    }

    @Test
    void dms() {
        var formatted = DegreeFormatters.DMS.format(60.305358224, 22.295304155);
        assertEquals("N60°18'19.290\", E22°17'43.095\"", formatted);
    }
}
