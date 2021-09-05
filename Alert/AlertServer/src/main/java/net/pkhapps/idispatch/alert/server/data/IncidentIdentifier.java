// iDispatch Alert Server
// Copyright (C) 2021 Petter Holmström
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.pkhapps.idispatch.alert.server.data;

/**
 * A value object used to identify an incident (the value comes from the dispatch system, whatever it happens to be).
 */
public class IncidentIdentifier extends WrappingValueObject<String> {

    private IncidentIdentifier(String wrapped) {
        super(wrapped);
    }

    /**
     * Creates a new {@code IncidentIdentifier} from the given string value.
     *
     * @param incidentIdentifier the string representation of the incident identifier.
     * @return a new {@code IncidentIdentifier} object.
     */
    public static IncidentIdentifier fromString(String incidentIdentifier) {
        return new IncidentIdentifier(incidentIdentifier);
    }
}
