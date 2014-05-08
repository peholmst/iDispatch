package net.pkhsolutions.idispatch.utils;

/**
 * TODO Document me
 */
public final class NestedPropertyUtils {

    private NestedPropertyUtils() {
    }

    public static String buildNestedProperty(String... path) {
        return String.join(".", path);
    }
}
