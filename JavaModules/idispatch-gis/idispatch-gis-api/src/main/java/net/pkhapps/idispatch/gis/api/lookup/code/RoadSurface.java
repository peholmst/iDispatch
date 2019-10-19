package net.pkhapps.idispatch.gis.api.lookup.code;

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

    /**
     * @return
     */
    public int getCode() {
        return code;
    }
}
