package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * TODO Document me
 */
public enum RoadSurface {
    UNKNOWN(0),
    NO_SURFACE(1),
    DURABLE_SURFACE(2);

    private final int code;

    RoadSurface(int code) {
        this.code = code;
    }

    @Contract("null -> null")
    public static RoadSurface valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(rs -> rs.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }

    /**
     * @return
     */
    public int getCode() {
        return code;
    }
}
