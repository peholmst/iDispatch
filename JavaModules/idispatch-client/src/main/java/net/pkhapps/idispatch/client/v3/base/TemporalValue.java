package net.pkhapps.idispatch.client.v3.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Value object representing a value that is associated with a particular instant in time.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class TemporalValue<T> implements Serializable {

    private T value;
    private Instant changedOn;

    public TemporalValue(@Nullable T value, @Nonnull Instant changedOn) {
        this.value = value;
        this.changedOn = Objects.requireNonNull(changedOn, "changedOn must not be null");
    }

    public TemporalValue(@Nonnull Instant changedOn) {
        this(null, changedOn);
    }

    @Nullable
    public T value() {
        return value;
    }

    @Nonnull
    public Instant changedOn() {
        return changedOn;
    }

    public boolean hasValue() {
        return value != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemporalValue<?> that = (TemporalValue<?>) o;
        return Objects.equals(value, that.value) &&
                Objects.equals(changedOn, that.changedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, changedOn);
    }

    @Override
    public String toString() {
        return String.format("%s[value=%s, changedOn=%s]", getClass().getSimpleName(), value, changedOn);
    }
}
