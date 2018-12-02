CREATE SEQUENCE gis.road_segment_id_seq;

ALTER SEQUENCE gis.road_segment_id_seq
  OWNER TO idispatch_gis;

CREATE TABLE gis.road_segment
(
  id                    bigint                     not null default nextval('gis.road_segment_id_seq'),
  version               bigint                     not null,
  gid                   bigint                     not null,
  location_accuracy     int                        not null default 0,
  location              geometry(LINESTRING, 3067) not null,
  elevation             int                        not null default 10,
  road_number           varchar(50),
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

CREATE INDEX road_segment_gid
  ON gis.road_segment (gid);

CREATE INDEX road_segment_name_fin
  ON gis.road_segment (name_fin);

CREATE INDEX road_segment_name_swe
  ON gis.road_segment (name_swe);

CREATE INDEX road_segment_location
  ON gis.road_segment USING GIST (location);

CREATE INDEX road_segment_validity
  ON gis.road_segment (valid_from, valid_to);
