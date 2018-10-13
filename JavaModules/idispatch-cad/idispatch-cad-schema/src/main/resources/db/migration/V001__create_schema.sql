DROP ROLE IF EXISTS idispatch_cad;
CREATE USER idispatch_cad
  nologin;

DROP ROLE IF EXISTS idispatch_cad_role;
CREATE ROLE idispatch_cad_role;

-- Schema has already been created by Flyway at this point
ALTER SCHEMA cad
  OWNER TO idispatch_cad;

GRANT USAGE ON SCHEMA cad TO idispatch_cad_role;

DROP SCHEMA IF EXISTS public;

ALTER TABLE cad.flyway_schema_history
  OWNER TO idispatch_cad;
