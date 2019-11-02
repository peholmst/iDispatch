package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * Enumeration of elevation levels (straight from the NLS source material).
 */
@SuppressWarnings("unused")
public enum Elevation {
    TUNNEL(-11),
    BELOW_SURFACE_LEVEL_3(-3),
    BELOW_SURFACE_LEVEL_2(-2),
    BELOW_SURFACE_LEVEL_1(-1),
    ON_SURFACE(0),
    ABOVE_SURFACE_LEVEL_1(1),
    ABOVE_SURFACE_LEVEL_2(2),
    ABOVE_SURFACE_LEVEL_3(3),
    ABOVE_SURFACE_LEVEL_4(4),
    ABOVE_SURFACE_LEVEL_5(5),
    UNDEFINED(10);

    private final int code;

    Elevation(int code) {
        this.code = code;
    }

    /**
     * Returns the elevation enum constant that corresponds to the given numerical code.
     *
     * @param code the numerical code as used in the NLS source material
     * @return the elevation enum constant or {@code null} if the numerical code was {@code null}
     * @throws IllegalArgumentException if the numerical code is invalid
     */
    @Contract("null -> null")
    public static Elevation valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(e -> e.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }

    /**
     * Returns whether this elevation level is below ground or above ground.
     *
     * @return true if the elevation level is below ground, false if it is on the ground level or above
     */
    public boolean isBelowGround() {
        return code < 0;
    }
}
