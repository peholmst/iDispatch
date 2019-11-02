package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * Enumeration of address point classes (straight from the NLS source material).
 */
@SuppressWarnings("unused")
public enum AddressPointClass {
    ADDRESS(96001),
    PASSAGE(96002);

    private final int code;

    AddressPointClass(int code) {
        this.code = code;
    }

    /**
     * Returns the address point class enum constant that corresponds to the given numerical code.
     *
     * @param code the numerical code as used in the NLS source material
     * @return the address point class enum constant or {@code null} if the numerical code was {@code null}
     * @throws IllegalArgumentException if the numerical code is invalid
     */
    @Contract("null -> null")
    public static AddressPointClass valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(apc -> apc.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }
}
