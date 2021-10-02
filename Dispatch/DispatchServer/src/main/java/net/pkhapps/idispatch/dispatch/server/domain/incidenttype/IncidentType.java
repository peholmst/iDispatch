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

package net.pkhapps.idispatch.dispatch.server.domain.incidenttype;

import net.pkhapps.idispatch.dispatch.server.domain.incidenttype.api.*;
import net.pkhapps.idispatch.dispatch.server.util.MultilingualStringLiteral;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateRoot;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * Incident types are used to define the type of incidents, such as automatic fire alarms, structural fires, motor
 * vehicle accidents, and so on.
 */
@AggregateRoot
public class IncidentType {

    @AggregateIdentifier
    private IncidentTypeId id;
    private String code;
    private MultilingualStringLiteral description;

    @CommandHandler
    public IncidentType(CreateIncidentType command) {
        apply(new IncidentTypeCreated(command.id(), command.code(), command.description()));
    }

    protected IncidentType() {
        // NOP
    }

    @CommandHandler
    public void handle(UpdateIncidentType command) {
        if (!Objects.equals(description, command.description())) {
            apply(new IncidentTypeUpdated(command.id(), command.description()));
        }
    }

    @EventSourcingHandler
    public void on(IncidentTypeCreated event) {
        id = requireNonNull(event.id(), "id must not be null");
        code = requireNonNull(event.code(), "code must not be null");
        description = requireNonNull(event.description(), "description must not be null");
    }

    @EventSourcingHandler
    public void on(IncidentTypeUpdated event) {
        description = requireNonNull(event.description(), "description must not be null");
    }
}
