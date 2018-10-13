CREATE TABLE cad.resource_type (
  id      varchar(36) not null,
  version bigint      not null,
  active  boolean     not null default true,
  PRIMARY KEY (id)
);

ALTER TABLE cad.resource_type
  OWNER TO idispatch_cad;

CREATE TABLE cad.resource_type_name (
  resource_type_id varchar(36)  not null,
  locale           varchar(3)   not null,
  name             varchar(200) not null,
  PRIMARY KEY (resource_type_id, locale),
  FOREIGN KEY (resource_type_id) REFERENCES cad.resource_type (id)
);

ALTER TABLE cad.resource_type_name
  OWNER TO idispatch_cad;

CREATE INDEX resource_type_name_idx
  ON cad.resource_type_name (name);

CREATE TABLE cad.resource (
  id               varchar(36) not null,
  version          bigint      not null,
  active           boolean     not null default true,
  resource_type_id varchar(36) not null,
  station_id       varchar(36) not null,
  designation      varchar(50) not null,
  PRIMARY KEY (id),
  FOREIGN KEY (resource_type_id) REFERENCES cad.resource_type (id),
  FOREIGN KEY (station_id) REFERENCES cad.station (id)
);

ALTER TABLE cad.resource
  OWNER TO idispatch_cad;

CREATE INDEX resource_designation_idx
  ON cad.resource (designation);
