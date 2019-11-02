package net.pkhapps.idispatch.gis.api.lookup.code;

import org.jetbrains.annotations.Contract;

import java.util.stream.Stream;

/**
 * TODO Document me
 */
public enum RoadClass {
    ROADWAY_I_A(12111),
    ROADWAY_I_B(12112),
    ROADWAY_II_A(12121),
    ROADWAY_II_B(12122),
    ROADWAY_III_A(12131),
    ROADWAY_III_B(12132),
    ROADWAY(12141),
    FERRY(12151),
    CABLE_FERRY(12152),
    SERVICE_ROAD_WITHOUT_BOOM_BARRIER(12153),
    SERVICE_ROAD_WITH_BOOM_BARRIER(12154),
    SPECIAL_TRANSPORT_CONNECTION_WITHOUT_BOOM_BARRIER(12155),
    SPECIAL_TRANSPORT_CONNECTION_WITH_BOOM_BARRIER(12156),
    OLD_ROADWAY(12311),
    WINTER_WAY(12312),
    PATH(12313),
    PEDESTRIAN_BICYCLE_WAY(12314),
    ROAD_PATH(12316);

    private final int code;

    RoadClass(int code) {
        this.code = code;
    }

    /**
     * @param code
     * @return
     */
    @Contract("null -> null")
    public static RoadClass valueOf(Integer code) {
        return code == null ? null : Stream.of(values()).filter(rc -> rc.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown code: " + code));
    }

    /**
     * @return
     */
    public int getCode() {
        return code;
    }
}
