CREATE TABLE IF NOT EXISTS address_points
(
    id                         VARCHAR(50) NOT NULL,
    source                     VARCHAR(50) NOT NULL,
    address_point_class        INT         NOT NULL,
    number                     VARCHAR(50)  DEFAULT NULL,
    location_accuracy          INT         NOT NULL,
    location                   GEOMETRY(POINT, 3067),
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

CREATE INDEX IF NOT EXISTS address_point_location
    ON address_points
        USING GIST (location);

CREATE INDEX IF NOT EXISTS address_point_name
    ON address_points (name_fi, name_sv, name_se, name_smn, name_sms);

CREATE INDEX IF NOT EXISTS address_point_address
    ON address_points (municipality_national_code,
                       name_fi, name_sv, name_se, name_smn, name_sms,
                       number);
