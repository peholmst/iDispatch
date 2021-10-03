/*
 * iDispatch Dispatch Server
 * Copyright (C) 2021 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.pkhapps.idispatch.dispatch.server.ports.gis;

import static java.util.Objects.requireNonNull;

/**
 * Value object used to identify a Finnish municipality.
 *
 * @param code the municipality code, must not be {@code null}.
 */
public record MunicipalityCode(String code) {

    public MunicipalityCode {
        requireNonNull(code, "code must not be null");
    }
}
