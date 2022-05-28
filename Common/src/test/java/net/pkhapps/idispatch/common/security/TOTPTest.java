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

import java.math.BigInteger;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TOTPTest {

    private static final TOTP.SharedSecret SECRET = TOTP.SharedSecret.fromHexString("31323334353637383930");

    @Test
    public void leastSignificantBytesWithZeroPadding_smallerSource() {
        assertArrayEquals(new byte[]{0, 0, 0, (byte) 0xff}, TOTP.leastSignificantBytesWithZeroPadding(new byte[]{(byte) 0xff}, 4));
    }

    @Test
    public void leastSignificantBytesWithZeroPadding_equalSource() {
        assertArrayEquals(new byte[]{0x12, 0x23, 0x34, 0x45}, TOTP.leastSignificantBytesWithZeroPadding(new byte[]{0x12, 0x23, 0x34, 0x45}, 4));
    }

    @Test
    public void leastSignificantBytesWithZeroPadding_largerSource() {
        assertArrayEquals(new byte[]{0x12, 0x23, 0x34, 0x45}, TOTP.leastSignificantBytesWithZeroPadding(new byte[]{0x01, 0x12, 0x23, 0x34, 0x45}, 4));
    }

    @Test
    public void hexStringToBytes_evenNumberOfChars() {
        assertArrayEquals(new byte[]{0x12, 0x23, 0x34, 0x45}, TOTP.hexStringToBytes("12233445"));
    }

    @Test
    public void hexStringToBytes_oddNumberOfChars() {
        assertArrayEquals(new byte[]{0x02, 0x23, 0x34, 0x45}, TOTP.hexStringToBytes("2233445"));
    }

    @Test
    public void generateOneTimePassword_rfc6238TestVectors() {
        assertEquals("90693936", TOTP.generateOneTimePassword(SECRET, Instant.ofEpochSecond(59)).truncate(8));
        assertEquals("25091201", TOTP.generateOneTimePassword(SECRET, Instant.ofEpochSecond(1111111109)).truncate(8));
        assertEquals("99943326", TOTP.generateOneTimePassword(SECRET, Instant.ofEpochSecond(1111111111)).truncate(8));
        assertEquals("93441116", TOTP.generateOneTimePassword(SECRET, Instant.ofEpochSecond(1234567890)).truncate(8));
        assertEquals("38618901", TOTP.generateOneTimePassword(SECRET, Instant.ofEpochSecond(2000000000)).truncate(8));
        assertEquals("47863826", TOTP.generateOneTimePassword(SECRET, TOTP.calculateTimeStep(new BigInteger("20000000000"))).truncate(8));
    }

    @Test
    public void oneTimePasswordTruncate_differentLengths() {
        var otp = TOTP.generateOneTimePassword(SECRET, Instant.ofEpochSecond(2000000000));
        assertEquals("38618901", otp.truncate(8));
        assertEquals("8618901", otp.truncate(7));
        assertEquals("618901", otp.truncate(6));
        assertEquals("18901", otp.truncate(5));
        assertEquals("8901", otp.truncate(4));
        assertEquals("901", otp.truncate(3));
        assertEquals("01", otp.truncate(2));
        assertEquals("1", otp.truncate(1));
        assertEquals("0", otp.truncate(0));
    }

    @Test
    public void oneTimePasswordEqualsAndHashCode() {
        var otp = TOTP.generateOneTimePassword(SECRET, Instant.ofEpochSecond(2000000000));
        var otpCopy = TOTP.generateOneTimePassword(SECRET, Instant.ofEpochSecond(2000000000));
        assertNotSame(otp, otpCopy);
        assertEquals(otp, otpCopy);
        assertEquals(otp.hashCode(), otpCopy.hashCode());
    }

    @Test
    public void sharedSecretFromRandom_noExceptionThrown() {
        var secret = TOTP.SharedSecret.fromRandom();
        // No assertions, just shouldn't throw any exceptions
        assertEquals(128, secret.toHexString().length());
    }

    @Test
    public void sharedSecretToHexAndBack() {
        var hex = "00112233445566778899aabbccddeeff";
        var secret = TOTP.SharedSecret.fromHexString(hex);
        assertEquals(hex, secret.toHexString());
    }

    @Test
    public void sharedSecretToBase64AndBack() {
        var base64 = "YWJjZGVmZwo=";
        var secret = TOTP.SharedSecret.fromBase64String(base64);
        assertEquals(base64, secret.toBase64String());
    }

    @Test
    public void sharedSecretEqualsAndHashCode() {
        var secretCopy = TOTP.SharedSecret.fromHexString(SECRET.toHexString());
        assertNotSame(SECRET, secretCopy);
        assertEquals(SECRET, secretCopy);
        assertEquals(SECRET.hashCode(), secretCopy.hashCode());
    }
}
