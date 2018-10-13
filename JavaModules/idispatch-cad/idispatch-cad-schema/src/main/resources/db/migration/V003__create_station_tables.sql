CREATE TABLE cad.station (
  id              varchar(36) not null,
  version         bigint      not null,
  active          boolean     not null default true,
  municipality_id integer     not null,
  location        point       not null,
  PRIMARY KEY (id),
  FOREIGN KEY (municipality_id) REFERENCES cad.municipality (id)
);

CREATE TABLE cad.station_name (
  station_id varchar(36)  not null,
  locale     varchar(3)   not null,
  name       varchar(200) not null,
  PRIMARY KEY (station_id, locale),
  FOREIGN KEY (station_id) REFERENCES cad.station (id)
);

CREATE INDEX station_name_idx
  ON cad.station_name (name);
