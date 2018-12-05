CREATE SEQUENCE gis.material_import_id_seq;

ALTER SEQUENCE gis.material_import_id_seq
  OWNER TO idispatch_gis;

CREATE TABLE gis.material_import
(
  id               bigint                   not null default nextval('gis.material_import_id_seq'),
  version          bigint                   not null,
  imported_on      timestamp with time zone not null,
  source           varchar(300)             not null,
  source_timestamp timestamp with time zone not null,
  PRIMARY KEY (id),
  UNIQUE (source)
);

ALTER TABLE gis.material_import
  OWNER TO idispatch_gis;

CREATE INDEX material_import_source_ix ON gis.material_import (source);
CREATE INDEX material_import_source_timestamp_ix ON gis.material_import (source_timestamp);
