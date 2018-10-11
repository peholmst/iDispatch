package net.pkhapps.idispatch.client.v3.util;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value object that represents a color.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class Color implements Serializable {

    private String hexRgb;

    public Color(@Nonnull String hexRgb) {
        setHexRgb(hexRgb);
    }

    @Nonnull
    public String toHexRGBString() {
        return '#' + hexRgb;
    }

    private void setHexRgb(@Nonnull String hexRgb) {
        Objects.requireNonNull(hexRgb, "hexRgb must not be null");
        if (hexRgb.length() < 6) {
            throw new IllegalArgumentException("hexRgb string must contain at least 6 characters");
        }
        if (!hexRgb.matches("#?[0-9a-fA-f]*")) {
            throw new IllegalArgumentException(hexRgb + " is not a valid hexRgb string");
        }

        if (hexRgb.charAt(0) == '#') {
            this.hexRgb = hexRgb.substring(1);
        } else {
            this.hexRgb = hexRgb;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Objects.equals(hexRgb, color.hexRgb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hexRgb);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), hexRgb);
    }
}
