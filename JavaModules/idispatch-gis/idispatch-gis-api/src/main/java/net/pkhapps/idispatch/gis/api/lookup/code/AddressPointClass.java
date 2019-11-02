package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * TODO Document me
 */
public enum AddressPointClass {
    ADDRESS(96001),
    PASSAGE(96002);

    private final int code;

    AddressPointClass(int code) {
        this.code = code;
    }

    /**
     * @param code
     * @return
     */
    @Contract("null -> null")
    public static AddressPointClass valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(apc -> apc.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }

    /**
     * @return
     */
    public int getCode() {
        return code;
    }
}
