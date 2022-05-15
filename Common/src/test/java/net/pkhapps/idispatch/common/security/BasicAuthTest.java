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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BasicAuthTest {

    @Test
    void encodeAuthorizationHeaderValue_emptyPair() {
        var header = BasicAuth.encodeAuthorizationHeaderValue(new BasicAuth.UsernamePasswordPair("", ""));
        assertEquals("Basic Og==", header);
    }

    @Test
    void decodeAuthorizationHeaderValue_emptyPair() {
        var usernamePassword = BasicAuth.decodeAuthorizationHeaderValue("Basic Og==");
        assertEquals("", usernamePassword.username());
        assertEquals("", usernamePassword.password());
    }

    @Test
    void encodeAuthorizationHeaderValue_usernameOnly() {
        var header = BasicAuth.encodeAuthorizationHeaderValue(new BasicAuth.UsernamePasswordPair("joecool", ""));
        assertEquals("Basic am9lY29vbDo=", header);
    }

    @Test
    void decodeAuthorizationHeaderValue_usernameOnly() {
        var usernamePassword = BasicAuth.decodeAuthorizationHeaderValue("Basic am9lY29vbDo=");
        assertEquals("joecool", usernamePassword.username());
        assertEquals("", usernamePassword.password());
    }

    @Test
    void encodeAuthorizationHeaderValue_passwordOnly() {
        var header = BasicAuth.encodeAuthorizationHeaderValue(new BasicAuth.UsernamePasswordPair("", "s3cr3t"));
        assertEquals("Basic OnMzY3IzdA==", header);
    }

    @Test
    void decodeAuthorizationHeaderValue_passwordOnly() {
        var usernamePassword = BasicAuth.decodeAuthorizationHeaderValue("Basic OnMzY3IzdA==");
        assertEquals("", usernamePassword.username());
        assertEquals("s3cr3t", usernamePassword.password());
    }

    @Test
    void encodeAuthorizationHeaderValue_usernamePasswordOnly() {
        var header = BasicAuth.encodeAuthorizationHeaderValue(new BasicAuth.UsernamePasswordPair("joecool", "s3cr3t"));
        assertEquals("Basic am9lY29vbDpzM2NyM3Q=", header);
    }

    @Test
    void decodeAuthorizationHeaderValue_usernamePasswordOnly() {
        var usernamePassword = BasicAuth.decodeAuthorizationHeaderValue("Basic am9lY29vbDpzM2NyM3Q=");
        assertEquals("joecool", usernamePassword.username());
        assertEquals("s3cr3t", usernamePassword.password());
    }

    @Test
    void decodeAuthorizationHeaderValue_colonInPassword() {
        var usernamePassword = BasicAuth.decodeAuthorizationHeaderValue("Basic am9lY29vbDpzOmNyOnQ=");
        assertEquals("joecool", usernamePassword.username());
        assertEquals("s:cr:t", usernamePassword.password());
    }

    @Test
    void encodeAuthorizationHeaderValue_colonInUsername() {
        assertThrows(IllegalArgumentException.class, () -> BasicAuth.encodeAuthorizationHeaderValue("joe:cool", ""));
    }

    @Test
    void encodeAuthorizationHeaderValue_nullUsernameAndPassword() {
        var header = BasicAuth.encodeAuthorizationHeaderValue(null, null);
        assertEquals("Basic Og==", header);
    }
}
