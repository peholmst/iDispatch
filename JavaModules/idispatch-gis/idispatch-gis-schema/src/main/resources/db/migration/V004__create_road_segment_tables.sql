CREATE SEQUENCE gis.road_segment_id_seq;

ALTER SEQUENCE gis.road_segment_id_seq
  OWNER TO idispatch_gis;

CREATE TABLE gis.road_segment
(
  id                    bigint                     not null default nextval('gis.road_segment_id_seq'),
  gid                   bigint                     not null,
  location_accuracy     varchar(50)                not null default '0',
  location              geometry(LINESTRING, 3067) not null,
  elevation             varchar(50)                not null default '10',
  road_number           bigint,
  name_swe              varchar(200),
  name_fin              varchar(200),
  municipality_id       bigint                     not null,
  min_addr_number_left  int,
  max_addr_number_left  int,
  min_addr_number_right int,
  max_addr_number_right int,
  valid_from            date                       not null,
  valid_to              date,
  material_import_id    bigint                     not null,
  PRIMARY KEY (id),
  FOREIGN KEY (municipality_id) REFERENCES gis.municipality (id),
  FOREIGN KEY (material_import_id) REFERENCES gis.material_import (id)
);

ALTER TABLE gis.road_segment
  OWNER TO idispatch_gis;

CREATE INDEX road_segment_gid_ix
  ON gis.road_segment (gid);

CREATE INDEX road_segment_name_fin_ix
  ON gis.road_segment (name_fin);

CREATE INDEX road_segment_name_swe_ix
  ON gis.road_segment (name_swe);

CREATE INDEX road_segment_location_ix
  ON gis.road_segment USING GIST (location);

CREATE INDEX road_segment_validity_ix
  ON gis.road_segment (valid_from, valid_to);

CREATE VIEW gis.road_name AS
SELECT DISTINCT name_fin, name_swe, municipality_id
FROM gis.road_segment
WHERE name_fin IS NOT NULL
   OR name_swe IS NOT NULL
ORDER BY name_fin, name_swe;

ALTER VIEW gis.road_name
  OWNER TO idispatch_gis;

CREATE INDEX road_name_ix
  ON gis.road_segment (name_fin, name_swe, municipality_id); -- Used by the road_name view
