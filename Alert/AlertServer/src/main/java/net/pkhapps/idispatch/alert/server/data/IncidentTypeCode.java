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
 * A value object used to identify an incident type (such as 103 automatic fire alarm, 202 small traffic accident, or
 * 403 large structural fire).
 */
public class IncidentTypeCode extends WrappingValueObject<String> {

    private IncidentTypeCode(String wrapped) {
        super(wrapped);
    }

    /**
     * Creates a new {@code IncidentTypeCode} from the given string value.
     *
     * @param incidentTypeCode the string representation of the incident type code, must not be {@code null}.
     * @return a new {@code IncidentTypeCode} object.
     */
    public static IncidentTypeCode fromString(String incidentTypeCode) {
        return new IncidentTypeCode(incidentTypeCode);
    }
}
