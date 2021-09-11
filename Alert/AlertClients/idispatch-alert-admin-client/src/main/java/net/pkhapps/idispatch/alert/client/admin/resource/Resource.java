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
 * A resource is something that can be alerted to respond to an incident, such as equipment and/or personnel. Resources
 * need to be globally identifiable across tenant boundaries since dispatch centers can alert resources from different
 * fire departments (tenants) to the same incidents.
 *
 * @param id               the ID of the resource within the Alert Server, must not be {@code null}.
 * @param globalIdentifier the global identifier of the resource (typically a call sign), used in other systems such as
 *                         dispatcher or command systems, must not be {@code null}.
 * @param active           whether the resource is active or deleted.
 */
public record Resource(ResourceId id, String globalIdentifier, boolean active) {

    public Resource {
        requireNonNull(id, "id must not be null");
        requireNonNull(globalIdentifier, "globalIdentifier must not be null");
    }
}
