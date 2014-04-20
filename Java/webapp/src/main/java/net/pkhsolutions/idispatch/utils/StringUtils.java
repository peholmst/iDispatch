package net.pkhsolutions.idispatch.utils;

/**
 * Utilities for working with Strings.
 */
public final class StringUtils {

    private StringUtils() {
    }

    /**
     * Returns the specified string if not {@code null}, or an empty string if {@code null}.
     */
    public static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
