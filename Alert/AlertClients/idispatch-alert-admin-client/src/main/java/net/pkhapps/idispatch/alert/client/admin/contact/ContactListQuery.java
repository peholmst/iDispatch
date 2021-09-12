/*
 * iDispatch Alert Clients
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

package net.pkhapps.idispatch.alert.client.admin.contact;

/**
 * Specification object for querying {@link ContactList}s.
 *
 * @param searchTerm a search string intended to be used for free text searching, can be {@code null}.
 * @param sortBy     how the query result should be sorted, defaults to {@link SortBy#NAME}.
 */
public record ContactListQuery(String searchTerm, SortBy sortBy) {

    @Override
    public SortBy sortBy() {
        return sortBy == null ? SortBy.NAME : sortBy;
    }

    public enum SortBy {
        NAME
    }
}
