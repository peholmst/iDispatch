CREATE SEQUENCE gis.address_point_id_seq;

ALTER SEQUENCE gis.address_point_id_seq
  OWNER TO idispatch_gis;

CREATE TABLE gis.address_point
(
  id                 bigint                not null default nextval('gis.address_point_id_seq'),
  version            bigint                not null,
  gid                bigint                not null,
  location_accuracy  varchar(50)           not null default '0',
  location           geometry(POINT, 3067) not null,
  number             varchar(50),
  name_swe           varchar(200),
  name_fin           varchar(200),
  municipality_id    bigint                not null,
  valid_from         date                  not null,
  valid_to           date,
  material_import_id bigint                not null,
  PRIMARY KEY (id),
  FOREIGN KEY (municipality_id) REFERENCES gis.municipality (id),
  FOREIGN KEY (material_import_id) REFERENCES gis.material_import (id)
);

ALTER TABLE gis.address_point
  OWNER TO idispatch_gis;

CREATE INDEX address_point_gid_ix
  ON gis.address_point (gid);

CREATE INDEX address_point_name_fin_ix
  ON gis.address_point (name_fin);

CREATE INDEX address_point_name_swe_ix
  ON gis.address_point (name_swe);

CREATE INDEX address_point_location_ix
  ON gis.address_point USING GIST (location);

CREATE INDEX address_point_validity_ix
  ON gis.address_point (valid_from, valid_to);

CREATE VIEW gis.address_point_name AS
SELECT DISTINCT name_fin, name_swe, municipality_id
FROM gis.address_point
WHERE name_fin IS NOT NULL
   OR name_swe IS NOT NULL
ORDER BY name_fin, name_swe;

ALTER VIEW gis.address_point_name
  OWNER TO idispatch_gis;

CREATE INDEX address_point_name_ix
  ON gis.address_point (name_fin, name_swe, municipality_id); -- Used by the address_point_name view
