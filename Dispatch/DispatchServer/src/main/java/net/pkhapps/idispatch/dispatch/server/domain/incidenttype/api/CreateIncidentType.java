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
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Command for creating a new incident type.
 *
 * @param id          the ID of the incident type. The ID is scoped to the Dispatch Server only.
 * @param code        the code of the incident type (like "401" for "structure fire: small"). The code can be used to
 *                    uniquely identify the incident type across system boundaries and also by human users.
 * @param description a multilingual description of the incident type.
 */
public record CreateIncidentType(@TargetAggregateIdentifier IncidentTypeId id,
                                 String code,
                                 MultilingualStringLiteral description) {
}
