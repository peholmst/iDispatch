package net.pkhapps.idispatch.alerter.server.domain;

import org.springframework.lang.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Utility methods for validating input.
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static String requireNonBlankAndMaxLength(@Nullable String s, int maxLength) {
        return requireMaxLength(requireNonBlank(s), maxLength);
    }

    @Nullable
    public static String requireMaxLength(@Nullable String s, int maxLength) {
        if (s != null && s.length() > maxLength) {
            throw new IllegalArgumentException("Maximum allowed string length is " + maxLength);
        }
        return s;
    }

    public static String requireNonBlank(@Nullable String s) {
        if (requireNonNull(s).isBlank()) {
            throw new IllegalArgumentException("String must not be blank");
        }
        return s;
    }
}
