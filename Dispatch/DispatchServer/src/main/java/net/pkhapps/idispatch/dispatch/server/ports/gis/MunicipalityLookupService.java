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

import java.util.stream.Stream;

/**
 * A service for looking up information about municipalities.
 */
public interface MunicipalityLookupService {

    /**
     * Finds all municipalities.
     *
     * @return a stream of {@linkplain Municipality municipalities}.
     */
    Stream<Municipality> findAll();

    /**
     * Finds all municipalities that match the given search term. If the search term is empty or {@code null}, this
     * method acts as {@link #findAll()}.
     *
     * @param searchTerm the search term.
     * @return a stream of matching {@linkplain Municipality municipalities}.
     */
    default Stream<Municipality> findBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return findAll();
        }
        var stLowerCase = searchTerm.toLowerCase();
        return findAll().filter(m -> m.name().anyValueMatches(n -> n.toLowerCase().startsWith(stLowerCase)));
    }
}
