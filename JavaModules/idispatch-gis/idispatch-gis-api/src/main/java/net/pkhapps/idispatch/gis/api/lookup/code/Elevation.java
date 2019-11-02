package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * TODO Document me
 */
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

    @Contract("null -> null")
    public static Elevation valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(e -> e.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }

    /**
     * @return
     */
    public boolean isBelowGround() {
        return code < 0;
    }

    /**
     * @return
     */
    public int getCode() {
        return code;
    }
}
