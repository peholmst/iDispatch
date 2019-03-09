CREATE SCHEMA IF NOT EXISTS idispatch_identity;

CREATE TABLE idispatch_identity.organization
(
  id      IDENTITY                NOT NULL,
  name    VARCHAR_IGNORECASE(255) NOT NULL,
  enabled BOOLEAN                 NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

CREATE TABLE idispatch_identity.client
(
  id                     IDENTITY                NOT NULL,
  client_id              VARCHAR_IGNORECASE(255) NOT NULL,
  client_secret          VARCHAR_IGNORECASE(255),
  access_token_validity  INTEGER                 NOT NULL,
  refresh_token_validity INTEGER                 NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (client_id)
);

CREATE TABLE idispatch_identity.client_resource_id
(
  client_id   BIGINT                  NOT NULL,
  resource_id VARCHAR_IGNORECASE(255) NOT NULL,
  PRIMARY KEY (client_id, resource_id),
  FOREIGN KEY (client_id) REFERENCES idispatch_identity.client (id)
);

CREATE TABLE idispatch_identity.client_grant_type
(
  client_id  BIGINT                  NOT NULL,
  grant_type VARCHAR_IGNORECASE(255) NOT NULL,
  PRIMARY KEY (client_id, grant_type),
  FOREIGN KEY (client_id) REFERENCES idispatch_identity.client (id)
);

CREATE TABLE idispatch_identity.client_redirect_uri
(
  client_id    BIGINT                  NOT NULL,
  redirect_uri VARCHAR_IGNORECASE(255) NOT NULL,
  PRIMARY KEY (client_id, redirect_uri),
  FOREIGN KEY (client_id) REFERENCES idispatch_identity.client (id)
);

CREATE TABLE idispatch_identity.client_authority
(
  client_id BIGINT                  NOT NULL,
  authority VARCHAR_IGNORECASE(255) NOT NULL,
  PRIMARY KEY (client_id, authority),
  FOREIGN KEY (client_id) REFERENCES idispatch_identity.client (id)
);

CREATE TABLE idispatch_identity.user
(
  id              IDENTITY                 NOT NULL,
  organization_id BIGINT                   NOT NULL,
  username        VARCHAR_IGNORECASE(255)  NOT NULL,
  password        VARCHAR_IGNORECASE(255)  DEFAULT NULL,
  full_name       VARCHAR_IGNORECASE(255)  NOT NULL,
  enabled         BOOLEAN                  NOT NULL,
  user_type       VARCHAR_IGNORECASE(255)  NOT NULL,
  valid_from      TIMESTAMP WITH TIME ZONE NOT NULL,
  valid_to        TIMESTAMP WITH TIME ZONE DEFAULT NULL,
  locked_at       TIMESTAMP WITH TIME ZONE DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE (username),
  FOREIGN KEY (organization_id) REFERENCES idispatch_identity.organization (id)
);

CREATE TABLE idispatch_identity.user_authority
(
  user_id   BIGINT                  NOT NULL,
  authority VARCHAR_IGNORECASE(255) NOT NULL,
  PRIMARY KEY (user_id, authority),
  FOREIGN KEY (user_id) REFERENCES idispatch_identity.user (id)
);