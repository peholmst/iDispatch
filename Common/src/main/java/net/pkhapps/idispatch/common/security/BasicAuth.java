/*
 * iDispatch Common
 *
 * Copyright (c) 2022 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.pkhapps.idispatch.common.security;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static java.util.Objects.requireNonNull;

/**
 * Utility methods for working with HTTP Basic Authentication.
 */
public final class BasicAuth {

    private BasicAuth() {
    }

    /**
     * Encodes the given {@code usernamePasswordPair} into an HTTP Basic Authentication {@code Authorization} header
     * value, e.g. "{@code Basic am9lY29vbDpzM2NyM3Q=}".
     *
     * @param usernamePasswordPair the username and password pair.
     * @return the HTTP {@code Authorization} header value.
     */
    public static @NotNull String encodeAuthorizationHeaderValue(@NotNull UsernamePasswordPair usernamePasswordPair) {
        return "Basic %s".formatted(usernamePasswordPair.toBase64String());
    }

    /**
     * Encodes the given {@code username} and {@code password} into an HTTP Basic Authentication {@code Authorization}
     * header value, e.g. "{@code Basic am9lY29vbDpzM2NyM3Q=}".
     *
     * @param username the username, or {@code null} for an empty username.
     * @param password the password, or {@code null} for an empty password.
     * @return the HTTP {@code Authorization} header value.
     */
    public static @NotNull String encodeAuthorizationHeaderValue(@Nullable String username, @Nullable String password) {
        return encodeAuthorizationHeaderValue(new UsernamePasswordPair(
                username == null ? "" : username,
                password == null ? "" : password)
        );
    }

    /**
     * Decodes the given HTTP Basic Authentication {@code Authorization} header value into a
     * {@code UsernamePasswordPair}.
     *
     * @param headerValue the header value to parse.
     * @return a new instance of {@link UsernamePasswordPair}.
     * @throws IllegalArgumentException if the header value is not a valid Basic HTTP Authentication header.
     */
    public static @NotNull UsernamePasswordPair decodeAuthorizationHeaderValue(@NotNull String headerValue) {
        if (!headerValue.startsWith("Basic ")) {
            throw new IllegalArgumentException("The header value is not a Basic authentication header value");
        }
        return UsernamePasswordPair.fromBase64String(headerValue.substring(6));
    }

    /**
     * A record consisting of a username-password pair (in plaintext).
     *
     * @param username the username (never {@code null}).
     * @param password the password (never {@code null}).
     */
    public record UsernamePasswordPair(
            @NotNull String username,
            @NotNull String password
    ) {
        /**
         * Creates a new {@code UsernamePasswordPair}.
         *
         * @param username the username, must not be {@code null} and must not contain a colon (:).
         * @param password the password, must not be {@code null}.
         * @throws IllegalArgumentException if the username contains a colon (:).
         */
        public UsernamePasswordPair {
            requireNonNull(username);
            requireNonNull(password);
            if (username.contains(":")) {
                throw new IllegalArgumentException("Username must not contain a colon (:)");
            }
        }

        /**
         * Creates a Base64-encoded {@code username:password} string for use in Basic Authentication.
         *
         * @return the password string.
         * @see #fromBase64String(String)
         */
        public @NotNull String toBase64String() {
            return Base64.getEncoder()
                    .encodeToString("%s:%s".formatted(username, password).getBytes(StandardCharsets.UTF_8));
        }

        /**
         * Parses the given Base64-encoded {@code username:password} string into a {@code UsernamePasswordPair}.
         *
         * @param base64String the string to parse.
         * @return a new {@code UsernamePasswordPair}.
         * @throws IllegalArgumentException if the given {@code base64String} is invalid.
         * @see #toBase64String()
         */
        public static @NotNull UsernamePasswordPair fromBase64String(@NotNull String base64String) {
            var decoded = new String(Base64.getDecoder()
                    .decode(base64String.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            var username = new StringBuilder();
            var password = new StringBuilder();
            boolean colonFound = false;
            for (int i = 0; i < decoded.length(); ++i) {
                var chr = decoded.charAt(i);
                if (!colonFound && chr == ':') {
                    colonFound = true;
                } else if (colonFound) {
                    password.append(chr);
                } else {
                    username.append(chr);
                }
            }
            return new UsernamePasswordPair(username.toString(), password.toString());
        }
    }
}
