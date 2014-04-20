package net.pkhsolutions.idispatch.utils;

/**
 * Utilities for working with Enums.
 */
public final class EnumUtils {

    private EnumUtils() {
    }

    /**
     * Returns the enumeration value if not {@code null}, or the default value if {@code null}.
     */
    public static <E extends Enum<E>> E nullToDefault(E value, E defaultValue) {
        return value == null ? defaultValue : value;
    }
}
