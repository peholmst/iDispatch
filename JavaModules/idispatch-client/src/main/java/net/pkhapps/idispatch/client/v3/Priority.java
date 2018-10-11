package net.pkhapps.idispatch.client.v3;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Enumeration of assignment priorities.
 */
public enum Priority {
    A, B, C, D;

    public boolean isHigherThan(@Nonnull Priority other) {
        Objects.requireNonNull(other, "other must not be null");
        return this.ordinal() < other.ordinal();
    }

    public boolean isLowerThan(@Nonnull Priority other) {
        Objects.requireNonNull(other, "other must not be null");
        return this.ordinal() > other.ordinal();
    }
}
