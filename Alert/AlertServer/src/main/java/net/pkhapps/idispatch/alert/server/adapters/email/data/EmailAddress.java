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
package net.pkhapps.idispatch.alert.server.adapters.email.data;

import net.pkhapps.idispatch.alert.server.data.WrappingValueObject;

/**
 * A value object representing an e-mail address.
 */
public class EmailAddress extends WrappingValueObject<String> {

    private EmailAddress(String wrapped) {
        super(validate(wrapped));
    }

    /**
     * Checks if the given e-mail address is valid.
     *
     * @param emailAddress the e-mail address to validate, must not be {@code null}.
     * @return the validated e-mail address (to allow for method chaining).
     * @throws IllegalArgumentException if the e-mail address is invalid.
     */
    public static String validate(String emailAddress) {
        if (!isValid(emailAddress)) {
            throw new IllegalArgumentException("The given e-mail address is not valid");
        }
        return emailAddress;
    }

    /**
     * Checks if the given e-mail address is valid.
     *
     * @param emailAddress the e-mail address to validate, must not be {@code null}.
     * @return true if the address is valid, false otherwise.
     */
    public static boolean isValid(String emailAddress) {
        // TODO Validate e-mail address!
        return true;
    }

    /**
     * Creates a new {@code EmailAddress}.
     *
     * @param emailAddress the e-mail address as a string, must not be {@code null}.
     * @return a new {@code EmailAddress}.
     * @throws IllegalArgumentException if the e-mail address is invalid.
     */
    public static EmailAddress fromString(String emailAddress) {
        return new EmailAddress(emailAddress);
    }
}
