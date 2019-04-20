DO $$
    BEGIN
        CREATE USER idispatch_alerter NOLOGIN;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'user idispatch_alerter already exists';
    END $$;

DO $$
    BEGIN
        CREATE ROLE idispatch_alerter_role;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'role idispatch_alerter_role already exists';
    END $$;

DO $$
    BEGIN
        CREATE USER idispatch_alerter_pool PASSWORD 'idispatch_alerter_pool';
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'user idispatch_alerter_pool already exists';
    END $$;

GRANT idispatch_alerter_role TO idispatch_alerter_pool;


-- Schema 'idispatch_alerter' has already been created by Flyway at this point

ALTER SCHEMA idispatch_alerter OWNER TO idispatch_alerter;

GRANT USAGE ON SCHEMA idispatch_alerter TO idispatch_alerter_role;

DROP SCHEMA IF EXISTS public;

ALTER TABLE idispatch_alerter.flyway_schema_history
    OWNER TO idispatch_alerter;
