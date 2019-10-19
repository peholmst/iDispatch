package net.pkhapps.idispatch.gis.api.lookup.code;

/**
 * TODO Document me
 */
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

    /**
     * @return
     */
    public int getCode() {
        return code;
    }
}
