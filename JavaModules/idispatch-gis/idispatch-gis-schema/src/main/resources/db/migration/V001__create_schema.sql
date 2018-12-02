DROP ROLE IF EXISTS idispatch_gis;
CREATE USER idispatch_gis
  nologin;

DROP ROLE IF EXISTS idispatch_gis_role;
CREATE ROLE idispatch_gis_role;

-- Schema has already been created by Flyway at this point
ALTER SCHEMA gis
  OWNER TO idispatch_gis;

GRANT USAGE ON SCHEMA gis TO idispatch_gis_role;

DROP SCHEMA IF EXISTS public;

ALTER TABLE gis.flyway_schema_history
  OWNER TO idispatch_gis;

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA gis;
