package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * Enumeration of road directions (straight from the NLS source material).
 */
@SuppressWarnings("unused")
public enum RoadDirection {
    /**
     * The road has traffic in both directions.
     */
    TWO_WAY(0),
    /**
     * The road has traffic in one direction only, in the direction that the road was digitalized in.
     */
    ONE_WAY(1),
    /**
     * The road has traffic in one direction only, against the direction that the road was digitalized in.
     */
    REVERSE_ONE_WAY(2);

    private final int code;

    RoadDirection(int code) {
        this.code = code;
    }

    /**
     * Returns the road direction enum constant that corresponds to the given numerical code.
     *
     * @param code the numerical code as used in the NLS source material
     * @return the road direction enum constant or {@code null} if the numerical code was {@code null}
     * @throws IllegalArgumentException if the numerical code is invalid
     */
    @Contract("null -> null")
    public static RoadDirection valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(rd -> rd.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }

    public int code() {
        return code;
    }
}
