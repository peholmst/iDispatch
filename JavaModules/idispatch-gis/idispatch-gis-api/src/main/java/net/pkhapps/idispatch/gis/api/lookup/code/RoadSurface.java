package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * Enumeration of road surfaces (straight from the NLS source material).
 */
@SuppressWarnings("unused")
public enum RoadSurface {
    /**
     * The surface of the road is unknown.
     */
    UNKNOWN(0),
    /**
     * The road has no durable surface, eg. it can be made of sand.
     */
    NO_SURFACE(1),
    /**
     * The road has a durable surface such as asphalt.
     */
    DURABLE_SURFACE(2);

    private final int code;

    RoadSurface(int code) {
        this.code = code;
    }

    /**
     * Returns the road surface enum constant that corresponds to the given numerical code.
     *
     * @param code the numerical code as used in the NLS source material
     * @return the road surface enum constant or {@code null} if the numerical code was {@code null}
     * @throws IllegalArgumentException if the numerical code is invalid
     */
    @Contract("null -> null")
    public static RoadSurface valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(rs -> rs.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }

    public int code() {
        return code;
    }
}
