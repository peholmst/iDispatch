CREATE TABLE cad.municipality (
  id      integer not null, -- We use municipality IDs from the NLS. Integer is enough.
  version bigint  not null,
  active  boolean not null default true,
  PRIMARY KEY (id)
);

ALTER TABLE cad.municipality OWNER TO idispatch_cad;

CREATE TABLE cad.municipality_name (
  municipality_id integer      not null,
  locale          varchar(3)   not null,
  name            varchar(200) not null,
  PRIMARY KEY (municipality_id, locale),
  FOREIGN KEY (municipality_id) REFERENCES cad.municipality (id)
);

ALTER TABLE cad.municipality_name OWNER TO idispatch_cad;

CREATE INDEX municipality_name_idx
  ON cad.municipality_name (name);
