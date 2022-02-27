/*
 * iDispatch Gateway Server
 * Copyright (C) 2022 Petter Holmström
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

package net.pkhapps.idispatch.gateway.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

/**
 * Implementation of the Time-based One-Time Password algorithm (RFC-6238) that always uses the {@value #HMAC_ALGORITHM}
 * algorithm with a key length of {@value #HMAC_KEY_LENGTH} bytes, a {@code T0} value of 0 and an {@code X} value of 30
 * seconds.
 *
 * @see #generateOneTimePassword(SharedSecret, Instant)
 * @see #generateOneTimePassword(SharedSecret, BigInteger)
 */
public final class TOTP {

    private static final String HMAC_ALGORITHM = "HmacSHA512";
    private static final int HMAC_KEY_LENGTH = 64;
    private static final BigInteger T0 = BigInteger.valueOf(0);
    private static final BigInteger X = BigInteger.valueOf(30);

    private TOTP() {
    }

    static byte[] leastSignificantBytesWithZeroPadding(byte[] source, int length) {
        var result = new byte[length];
        for (var i = 1; i <= result.length; ++i) {
            result[result.length - i] = source[source.length - i];
            if (source.length == i) {
                break;
            }
        }
        return result;
    }

    static byte[] hexStringToBytes(String hexString) {
        if (hexString.length() % 2 == 1) {
            hexString = "0" + hexString;
        }
        var length = hexString.length() / 2;
        var bytes = new byte[length];
        byte b;
        for (var i = 0; i < hexString.length(); i += 2) {
            b = (byte) (HexFormat.fromHexDigit(hexString.charAt(i)) << 4);
            b += HexFormat.fromHexDigit(hexString.charAt(i + 1));
            bytes[i / 2] = b;
        }
        return bytes;
    }

    /**
     * Generates a new one-time password based on the given shared secret and timestamp.
     *
     * @param sharedSecret the shared secret.
     * @param timestamp    the timestamp.
     * @return the one-time password.
     */
    public static OneTimePassword generateOneTimePassword(SharedSecret sharedSecret, Instant timestamp) {
        return generateOneTimePassword(sharedSecret, calculateTimeStep(timestamp));
    }

    /**
     * Generates a new one-time password based on the given shared secret and time step.
     *
     * @param sharedSecret the shared secret.
     * @param timeStep     the time step.
     * @return the one-time password.
     * @throws RuntimeException if the one-time password could not be generated (should never happen if the JVM has the
     *                          correct algorithm installed).
     */
    public static OneTimePassword generateOneTimePassword(SharedSecret sharedSecret, BigInteger timeStep) {
        try {
            var mac = Mac.getInstance(HMAC_ALGORITHM);
            sharedSecret.initMac(mac, HMAC_KEY_LENGTH);
            var msg = leastSignificantBytesWithZeroPadding(timeStep.toByteArray(), 8);
            var result = mac.doFinal(msg);
            return new OneTimePassword(result);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new RuntimeException("Error generating one time password", ex);
        }
    }

    /**
     * Calculates the time step for the given timestamp.
     *
     * @param timestamp the timestamp to calculate the time step for.
     * @return the time step.
     */
    public static BigInteger calculateTimeStep(Instant timestamp) {
        return calculateTimeStep(BigInteger.valueOf(timestamp.getEpochSecond()));
    }

    /**
     * Calculates the time step for the given epoch seconds.
     *
     * @param epochSeconds the epoch seconds to calculate the time step for.
     * @return the time step.
     */
    public static BigInteger calculateTimeStep(BigInteger epochSeconds) {
        return epochSeconds.subtract(T0).divide(X);
    }

    /**
     * Class representing a shared secret used to calculate a one-time password.
     */
    @SuppressWarnings("ClassCanBeRecord")
    public static final class SharedSecret {

        private final byte[] data;

        private SharedSecret(byte[] data) {
            this.data = data;
        }

        /**
         * Creates a new shared secret from the given hexadecimal string.
         *
         * @param hexString the hexadecimal representation of the shared secret.
         * @return the shared secret.
         */
        public static SharedSecret fromHexString(String hexString) {
            return new SharedSecret(hexStringToBytes(hexString));
        }

        /**
         * Creates a new shared secret from the given Base64 encoded string.
         *
         * @param base64String a Base64 representation of the shared secret.
         * @return the shared secret.
         */
        public static SharedSecret fromBase64String(String base64String) {
            return new SharedSecret(Base64.getDecoder().decode(base64String));
        }

        /**
         * Creates a new, random shared secret. Please note that this secret must be shared with the other party in
         * order to be useful. A cryptographically strong algorithm will be used to generate the secret.
         *
         * @return the shared secret.
         * @throws RuntimeException if the shared secret could not be generated (should never happen if the JVM has the
         *                          correct algorithm installed)
         * @see #toHexString()
         * @see #toBase64String()
         */
        public static SharedSecret fromRandom() {
            try {
                var random = SecureRandom.getInstanceStrong();
                var bytes = new byte[HMAC_KEY_LENGTH];
                random.nextBytes(bytes);
                return new SharedSecret(bytes);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException("Error generating shared secret", ex);
            }
        }

        /**
         * Returns the shared secret as a hexadecimal string.
         *
         * @return a hexadecimal string.
         * @see #fromHexString(String)
         */
        public String toHexString() {
            var sb = new StringBuilder();
            for (byte b : data) {
                sb.append(Integer.toHexString(b & 0xff));
            }
            return sb.toString();
        }

        /**
         * Returns the shared secret as a Base64 encoded string.
         *
         * @return a Base64 encoded string.
         * @see #fromBase64String(String)
         */
        public String toBase64String() {
            return Base64.getEncoder().encodeToString(data);
        }

        private void initMac(Mac mac, int keyLength) throws InvalidKeyException {
            if (keyLength == data.length) {
                mac.init(new SecretKeySpec(data, "RAW"));
            } else {
                byte[] key = new byte[keyLength];
                for (var i = 0; i < keyLength; ++i) {
                    key[i] = data[i % data.length];
                }
                mac.init(new SecretKeySpec(key, "RAW"));
            }
        }
    }

    /**
     * Class representing a one-time password.
     *
     * @see #truncate(int)
     */
    @SuppressWarnings("ClassCanBeRecord")
    public static final class OneTimePassword {

        private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
        private final byte[] data;

        private OneTimePassword(byte[] data) {
            this.data = data;
        }

        /**
         * Truncates the password into a numerical string with the given number of digits.
         *
         * @param digits a number between 0 (not useful) and 8 (inclusive).
         * @return the truncated password.
         * @throws IllegalArgumentException if the number of digits is not within the given range.
         */
        public String truncate(int digits) {
            if (digits > 8) {
                throw new IllegalArgumentException("Number of digits must not be greater than 8");
            }
            var offset = data[data.length - 1] & 0xf;
            var binCode = (data[offset] & 0x7f) << 24 | (data[offset + 1] & 0xff) << 16 | (data[offset + 2] & 0xff) << 8 | (data[offset + 3] & 0xff);
            var otp = binCode % DIGITS_POWER[digits];
            var sb = new StringBuilder(Integer.toString(otp));
            while (sb.length() < digits) {
                sb.insert(0, "0");
            }
            return sb.toString();
        }

    }
}
