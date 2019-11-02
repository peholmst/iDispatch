package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * TODO Document me
 */
public enum RoadDirection {
    TWO_WAY(0),
    ONE_WAY(1),
    REVERSE_ONE_WAY(2);

    private final int code;

    RoadDirection(int code) {
        this.code = code;
    }

    @Contract("null -> null")
    public static RoadDirection valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(rd -> rd.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }

    /**
     * @return
     */
    public int getCode() {
        return code;
    }
}
