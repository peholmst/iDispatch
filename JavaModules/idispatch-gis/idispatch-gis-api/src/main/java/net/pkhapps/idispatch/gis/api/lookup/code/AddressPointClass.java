package net.pkhapps.idispatch.gis.api.lookup.code;

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
     * @return
     */
    public int getCode() {
        return code;
    }
}
