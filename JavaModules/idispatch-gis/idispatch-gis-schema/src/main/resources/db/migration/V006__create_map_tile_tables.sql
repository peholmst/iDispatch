CREATE SEQUENCE gis.map_tile_id_seq;

ALTER SEQUENCE gis.map_tile_id_seq
  OWNER TO idispatch_gis;

CREATE TABLE gis.map_tile
(
  id                 bigint                  not null default nextval('gis.map_tile_id_seq'),
  zoom               int                     not null,
  col                int                     not null,
  row                int                     not null,
  envelope           geometry(POLYGON, 3067) not null,
  south_west         geometry(POINT, 3067)   not null,
  north_east         geometry(POINT, 3067)   not null,
  scale_x            numeric(5, 2)           not null,
  scale_y            numeric(5, 2)           not null,
  image              bytea                   not null,
  mime_type          varchar(50)             not null,
  material_import_id bigint                  not null,
  PRIMARY KEY (id),
  UNIQUE (zoom, col, row),
  FOREIGN KEY (material_import_id) REFERENCES gis.material_import (id)
);

ALTER TABLE gis.map_tile
  OWNER TO idispatch_gis;

CREATE INDEX map_tile_xyz_ix
  ON gis.map_tile (col, row, zoom);

CREATE INDEX map_tile_envelope_ix
  ON gis.map_tile USING GIST (envelope);

CREATE INDEX map_tile_south_west_ix
  ON gis.map_tile USING GIST (south_west);

CREATE INDEX map_tile_north_east_ix
  ON gis.map_tile USING GIST (north_east);
