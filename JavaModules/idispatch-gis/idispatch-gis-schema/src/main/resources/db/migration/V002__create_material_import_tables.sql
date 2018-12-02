CREATE SEQUENCE gis.material_import_id_seq;

ALTER SEQUENCE gis.material_import_id_seq
  OWNER TO idispatch_gis;

CREATE TABLE gis.material_import
(
  id          bigint                   not null,
  version     bigint                   not null,
  imported_on timestamp with time zone not null,
  source      varchar(200)             not null,
  PRIMARY KEY (id)
);

ALTER TABLE gis.material_import
  OWNER TO idispatch_gis;
