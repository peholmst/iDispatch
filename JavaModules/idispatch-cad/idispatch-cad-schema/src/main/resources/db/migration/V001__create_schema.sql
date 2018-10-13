CREATE USER idispatch_cad nologin;
CREATE ROLE idispatch_cad_role;

-- Schema has already been created by Flyway at this point
ALTER SCHEMA cad OWNER TO idispatch_cad;

GRANT USAGE ON SCHEMA cad TO idispatch_cad_role;

DROP SCHEMA public;
