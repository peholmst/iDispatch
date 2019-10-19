package net.pkhapps.idispatch.gis.api.lookup.code;

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

    /**
     * @return
     */
    public int getCode() {
        return code;
    }
}
