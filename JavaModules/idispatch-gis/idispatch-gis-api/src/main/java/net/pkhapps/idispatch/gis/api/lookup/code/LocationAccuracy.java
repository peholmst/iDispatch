package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * Enumeration of location accuracies (straight from the NLS source material).
 */
@SuppressWarnings("unused")
public enum LocationAccuracy {
    UNDEFINED(0),
    MM500(500),
    MM800(800),
    MM1000(1000),
    MM2000(2000),
    MM3000(3000),
    MM4000(4000),
    MM5000(5000),
    MM7500(7500),
    MM8000(8000),
    MM10000(10000),
    MM12500(12500),
    MM15000(15000),
    MM20000(20000),
    MM25000(25000),
    MM30000(30000),
    MM40000(40000),
    MM80000(80000),
    MM100000(100000);

    private final int code;

    LocationAccuracy(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    /**
     * Returns the location accuracy enum constant that corresponds to the given numerical code.
     *
     * @param code the numerical code as used in the NLS source material
     * @return the location accuracy enum constant or {@code null} if the numerical code was {@code null}
     * @throws IllegalArgumentException if the numerical code is invalid
     */
    @Contract("null -> null")
    public static LocationAccuracy valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(la -> la.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }
}
