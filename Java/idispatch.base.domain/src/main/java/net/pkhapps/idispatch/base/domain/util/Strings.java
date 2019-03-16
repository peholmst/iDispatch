package net.pkhapps.idispatch.base.domain.util;

import org.springframework.lang.Nullable;

/**
 * Utility class for working with strings.
 */
public final class Strings {

    private Strings() {
    }

    /**
     * Requires the given string to have a specific maximum length.
     *
     * @param s         the string to check.
     * @param maxLength the max length.
     * @throws IllegalArgumentException if the string is not {@code null} and longer than {@code maxLength}.
     */
    @Nullable
    public static String requireMaxLength(@Nullable String s, int maxLength) {
        if (s != null && s.length() > maxLength) {
            throw new IllegalArgumentException("The string " + s + " is longer than " + maxLength + " characters");
        }
        return s;
    }
}
