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

import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Value object representing a valid phone number in international E.164 format.
 *
 * @param phoneNumber the phone number as a string, never {@code null}.
 */
public record PhoneNumber(String phoneNumber) {

    private static final Set<Character> WHITESPACE = Set.of(' ', '_', '.', '-', ',', '\t');

    /**
     * Creates a new {@code PhoneNumber} from the given string. Any whitespace characters are automatically stripped
     * from the string, leaving only digits and the initial '+' sign.
     *
     * @param phoneNumber the phone number to use, must not be {@code null}.
     * @throws IllegalArgumentException if the phone number is not valid even after the whitespace has been stripped.
     */
    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = sanitizeAndValidate(phoneNumber);
    }

    private static String sanitizeAndValidate(String phoneNumber) {
        requireNonNull(phoneNumber, "phoneNumber must not be null");
        var sb = new StringBuilder();
        phoneNumber.codePoints().forEach(codePoint -> {
            if (codePoint == '+') {
                if (sb.length() == 0) {
                    sb.appendCodePoint(codePoint);
                } else {
                    throw new IllegalArgumentException("+ must be the first character");
                }
            } else if (Character.isDigit(codePoint)) {
                if (sb.length() == 0) {
                    throw new IllegalArgumentException("Phone number must start with a +");
                }
                sb.appendCodePoint(codePoint);
            } else if (!WHITESPACE.contains((char) codePoint)) {
                throw new IllegalArgumentException(phoneNumber + " is not a valid phone number");
            }
        });
        if (sb.length() == 0) {
            throw new IllegalArgumentException("Phone number must not be empty");
        }
        return sb.toString();
    }
}
