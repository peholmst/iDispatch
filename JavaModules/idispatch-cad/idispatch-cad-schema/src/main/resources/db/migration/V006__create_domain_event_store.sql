CREATE SEQUENCE cad.domain_event_id;

ALTER SEQUENCE cad.domain_event_id
  OWNER TO idispatch_cad;

CREATE TABLE cad.domain_event (
  id          bigint                   not null,
  occurred_on timestamp with time zone not null,
  event_type  varchar(200)             not null,
  event       json,
  PRIMARY KEY (id)
);

ALTER TABLE cad.domain_event
  OWNER TO idispatch_cad;

CREATE INDEX domain_event_type_ids
  ON cad.domain_event (event_type);
