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

package net.pkhapps.idispatch.alert.server.application.text;

import net.pkhapps.idispatch.alert.server.data.Alert;

/**
 * An interface defining an object that knows how to produce a formatted text string from an {@link Alert} and a
 * template. This can be used to produce alert e-mail messages, SMS-messages, JSON-strings, etc.
 */
public interface AlertTextMessageFormatter {

    /**
     * Creates a string representation of the given alert based on the given template.
     *
     * @param alert    the alert to format into a string, must not be {@code null}.
     * @param template a template string that the formatter knows how to interpret, must not be {@code null}.
     * @return the formatted alert message, never {@code null}.
     * @throws AlertTextMessageFormatException if the alert message could not be formatted for some reason.
     */
    String formatAlertMessage(Alert alert, String template);
}
