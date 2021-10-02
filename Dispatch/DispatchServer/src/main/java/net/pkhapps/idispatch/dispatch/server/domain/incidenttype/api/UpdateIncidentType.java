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
 * Command for updating an incident type. Only the description of an already created incident type can be changed. If
 * the code needs to be updated, a new incident type should be created instead.
 *
 * @param id          the ID of the incident type to update.
 * @param description the updated multilingual description of the incident type.
 */
public record UpdateIncidentType(@TargetAggregateIdentifier IncidentTypeId id,
                                 MultilingualStringLiteral description) {
}
