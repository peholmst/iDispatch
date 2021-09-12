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

package net.pkhapps.idispatch.alert.client.admin.contact;

import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Value object representing a valid e-mail address.
 *
 * @param emailAddress the e-mail address as a string, never {@code null}.
 */
public record EmailAddress(String emailAddress) {

    // Regex copied from: https://www.javatpoint.com/java-email-validation
    private static final Pattern REGEX = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    /**
     * Creates a new {@code EmailAddress} from the given string. Any whitespace characters are automatically stripped
     * from the string.
     *
     * @param emailAddress the e-mail address to use, must not be {@code null}.
     * @throws IllegalArgumentException if the e-mail address is not valid even after the whitespace has been stripped.
     */
    public EmailAddress(String emailAddress) {
        this.emailAddress = sanitizeAndValidate(emailAddress);
    }

    private static String sanitizeAndValidate(String emailAddress) {
        requireNonNull(emailAddress, "emailAddress must not be null");
        var trimmed = emailAddress.trim();
        if (!REGEX.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(emailAddress + " is not a valid e-mail address");
        }
        return trimmed;
    }
}
