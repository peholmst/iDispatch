CREATE TABLE gis.municipality
(
  id       int          not null,
  name_fin varchar(200) not null,
  name_swe varchar(200) not null,
  PRIMARY KEY (id)
);

ALTER TABLE gis.municipality
  OWNER TO idispatch_gis;

CREATE INDEX municipality_name_fin_ix
  ON gis.municipality (name_fin);

CREATE INDEX municipality_name_swe_ix
  ON gis.municipality (name_swe);
