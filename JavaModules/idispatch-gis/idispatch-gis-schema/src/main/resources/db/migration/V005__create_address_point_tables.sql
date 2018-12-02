CREATE SEQUENCE gis.address_point_id_seq;

ALTER SEQUENCE gis.address_point_id_seq
  OWNER TO idispatch_gis;

CREATE TABLE gis.address_point
(
  id                 bigint not null,
  version            bigint not null,
  gid                bigint not null,
  location_accuracy  int    not null default 0,
  location           geometry(POINT, 3067),
  number             varchar(50),
  name_swe           varchar(200),
  name_fin           varchar(200),
  municipality_id    bigint not null,
  material_import_id bigint not null,
  PRIMARY KEY (id),
  FOREIGN KEY (municipality_id) REFERENCES gis.municipality (id),
  FOREIGN KEY (material_import_id) REFERENCES gis.material_import (id)
);

ALTER TABLE gis.address_point
  OWNER TO idispatch_gis;

CREATE INDEX address_point_gid
  ON gis.address_point (gid);

CREATE INDEX address_point_name_fin
  ON gis.address_point (name_fin);

CREATE INDEX address_point_name_swe
  ON gis.address_point (name_swe);

CREATE INDEX address_point_location
  ON gis.address_point USING GIST (location);
