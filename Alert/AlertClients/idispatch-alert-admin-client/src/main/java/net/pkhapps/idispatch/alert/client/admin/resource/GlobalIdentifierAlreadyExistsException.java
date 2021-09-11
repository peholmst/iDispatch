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

package net.pkhapps.idispatch.alert.client.admin.resource;

import static java.util.Objects.requireNonNull;

/**
 * Exception thrown when an attempt is made to create a new {@link Resource} with a {@linkplain
 * Resource#globalIdentifier() global identifier} that is already in use.
 */
public class GlobalIdentifierAlreadyExistsException extends IllegalArgumentException {

    private final String globalIdentifier;

    public GlobalIdentifierAlreadyExistsException(String globalIdentifier) {
        this.globalIdentifier = requireNonNull(globalIdentifier, "globalIdentifier must not be null");
    }

    public String globalIdentifier() {
        return globalIdentifier;
    }
}
