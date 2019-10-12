CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE IF NOT EXISTS municipalities (
    national_code VARCHAR(3) NOT NULL,
    name_fi VARCHAR(100) NOT NULL,
    name_sv VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    location GEOMETRY(POINT,3067),
    bounds GEOMETRY(MULTIPOLYGON,3067),
    PRIMARY KEY (national_code)
);

CREATE INDEX IF NOT EXISTS municipality_bounds
  ON municipalities
  USING GIST (bounds);

CREATE INDEX IF NOT EXISTS municipality_name
  ON municipalities (name_fi, name_sv);
