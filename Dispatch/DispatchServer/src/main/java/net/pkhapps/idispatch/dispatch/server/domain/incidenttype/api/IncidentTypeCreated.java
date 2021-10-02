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

package net.pkhapps.idispatch.dispatch.server.domain.incidenttype.api;

import net.pkhapps.idispatch.dispatch.server.util.MultilingualStringLiteral;

/**
 * Event published when a new incident type has been created.
 *
 * @param id          the ID of the newly created incident type.
 * @param code        the code of the newly created incident type.
 * @param description a multilingual description of the incident type.
 */
public record IncidentTypeCreated(IncidentTypeId id,
                                  String code,
                                  MultilingualStringLiteral description) {
}
