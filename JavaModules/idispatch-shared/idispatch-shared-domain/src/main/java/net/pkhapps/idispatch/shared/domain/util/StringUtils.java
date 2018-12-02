package net.pkhapps.idispatch.shared.domain.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * TODO Document me
 */
public final class StringUtils {

    private StringUtils() {
    }

    @Contract("null, _ -> null")
    @Nullable
    public static String ensureMaxLength(@Nullable String s, int maxLength) {
        if (s == null) {
            return null;
        } else if (s.length() > maxLength) {
            throw new IllegalArgumentException("String cannot be longer than " + maxLength + " characters");
        } else {
            return s;
        }
    }
}
