CREATE SEQUENCE gis.municipality_id_seq;

ALTER SEQUENCE gis.municipality_id_seq
  OWNER TO idispatch_gis;

CREATE TABLE gis.municipality
(
  id                 bigint       not null default nextval('gis.municipality_id_seq'),
  version            bigint       not null,
  code               int          not null,
  name_fin           varchar(200) not null,
  name_swe           varchar(200) not null,
  valid_from         date         not null,
  valid_to           date,
  material_import_id bigint       not null,
  PRIMARY KEY (id),
  UNIQUE (code),
  FOREIGN KEY (material_import_id) REFERENCES gis.material_import (id)
);

ALTER TABLE gis.municipality
  OWNER TO idispatch_gis;

CREATE INDEX municipality_name_fin
  ON gis.municipality (name_fin);

CREATE INDEX municipality_name_swe
  ON gis.municipality (name_swe);

CREATE INDEX municipality_validity
  ON gis.municipality (valid_from, valid_to);
