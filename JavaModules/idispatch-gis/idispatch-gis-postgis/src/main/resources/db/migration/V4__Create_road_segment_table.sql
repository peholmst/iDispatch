CREATE TABLE IF NOT EXISTS road_segments
(
    id                         VARCHAR(50) NOT NULL,
    source                     VARCHAR(50) NOT NULL,
    road_class                 INT         NOT NULL,
    elevation                  INT         NOT NULL,
    surface                    INT         NOT NULL,
    direction                  INT         NOT NULL,
    road_number                BIGINT       DEFAULT NULL,
    road_part_number           BIGINT       DEFAULT NULL,
    address_number_left_min    INT          DEFAULT NULL,
    address_number_left_max    INT          DEFAULT NULL,
    address_number_right_min   INT          DEFAULT NULL,
    address_number_right_max   INT          DEFAULT NULL,
    location_accuracy          INT         NOT NULL,
    location                   GEOMETRY(LINESTRING, 3067),
    valid_from                 DATE         DEFAULT NULL,
    valid_to                   DATE         DEFAULT NULL,
    municipality_national_code VARCHAR(3)   DEFAULT NULL,
    name_fi                    VARCHAR(100) DEFAULT NULL,
    name_sv                    VARCHAR(100) DEFAULT NULL,
    name_se                    VARCHAR(100) DEFAULT NULL,
    name_smn                   VARCHAR(100) DEFAULT NULL,
    name_sms                   VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS road_segment_location
    ON road_segments
        USING GIST (location);

CREATE INDEX IF NOT EXISTS road_segment_number
    ON road_segments (road_number);

CREATE INDEX IF NOT EXISTS road_segment_name
    ON road_segments (name_fi, name_sv, name_se, name_smn, name_sms);

CREATE INDEX IF NOT EXISTS road_segment_address
    ON road_segments (municipality_national_code,
                      name_fi, name_sv, name_se, name_smn, name_sms,
                      address_number_left_min, address_number_left_max,
                      address_number_right_min, address_number_right_max);
