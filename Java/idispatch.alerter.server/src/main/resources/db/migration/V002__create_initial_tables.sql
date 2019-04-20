CREATE TABLE idispatch_alerter.recipient
(
    id               BIGSERIAL    NOT NULL,
    opt_lock_version BIGINT       NOT NULL DEFAULT 0,
    type             VARCHAR(200) NOT NULL,
    description      VARCHAR(200) NOT NULL,
    org_id           BIGINT       NOT NULL,
    priority         SMALLINT     NOT NULL,
    active           BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

ALTER TABLE idispatch_alerter.recipient
    OWNER TO idispatch_alerter;
GRANT SELECT, INSERT, UPDATE, DELETE ON idispatch_alerter.recipient TO idispatch_alerter_role;
ALTER SEQUENCE idispatch_alerter.recipient_id_seq
    OWNER TO idispatch_alerter;
GRANT USAGE, SELECT ON SEQUENCE idispatch_alerter.recipient_id_seq TO idispatch_alerter_role;

--
-- ########################
--

CREATE TABLE idispatch_alerter.recipient_resource
(
    recipient_id  BIGINT      NOT NULL,
    resource_code VARCHAR(50) NOT NULL,
    PRIMARY KEY (recipient_id, resource_code),
    FOREIGN KEY (recipient_id) REFERENCES idispatch_alerter.recipient (id)
);

ALTER TABLE idispatch_alerter.recipient_resource
    OWNER TO idispatch_alerter;
GRANT SELECT, INSERT, UPDATE, DELETE ON idispatch_alerter.recipient_resource TO idispatch_alerter_role;

--
-- ########################
--

CREATE TABLE idispatch_alerter.recipient_sms
(
    recipient_id BIGINT      NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    PRIMARY KEY (recipient_id, phone_number),
    FOREIGN KEY (recipient_id) REFERENCES idispatch_alerter.recipient (id)
);

ALTER TABLE idispatch_alerter.recipient_sms
    OWNER TO idispatch_alerter;
GRANT SELECT, INSERT, UPDATE, DELETE ON idispatch_alerter.recipient_sms TO idispatch_alerter_role;

--
-- ########################
--

CREATE TABLE idispatch_alerter.alert
(
    id               BIGSERIAL    NOT NULL,
    opt_lock_version BIGINT       NOT NULL DEFAULT 0,
    priority         SMALLINT     NOT NULL,
    alert_date       timestamptz  NOT NULL,
    content_type     VARCHAR(200) NOT NULL,
    content          jsonb        NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE idispatch_alerter.alert
    OWNER TO idispatch_alerter;
GRANT SELECT, INSERT ON idispatch_alerter.alert TO idispatch_alerter_role;
ALTER SEQUENCE idispatch_alerter.alert_id_seq
    OWNER TO idispatch_alerter;
GRANT USAGE, SELECT ON SEQUENCE idispatch_alerter.alert_id_seq TO idispatch_alerter_role;

--
-- ########################
--

CREATE TABLE idispatch_alerter.alert_recipient
(
    alert_id     BIGINT NOT NULL,
    recipient_id BIGINT NOT NULL,
    PRIMARY KEY (alert_id, recipient_id),
    FOREIGN KEY (alert_id) REFERENCES idispatch_alerter.alert (id),
    FOREIGN KEY (recipient_id) REFERENCES idispatch_alerter.recipient (id)
);

ALTER TABLE idispatch_alerter.alert_recipient
    OWNER TO idispatch_alerter;
GRANT SELECT, INSERT ON idispatch_alerter.alert_recipient TO idispatch_alerter_role;

--
-- ########################
--

CREATE TABLE idispatch_alerter.alert_resource
(
    alert_id      BIGINT      NOT NULL,
    resource_code VARCHAR(50) NOT NULL,
    PRIMARY KEY (alert_id, resource_code),
    FOREIGN KEY (alert_id) REFERENCES idispatch_alerter.alert (id)
);

ALTER TABLE idispatch_alerter.alert_resource
    OWNER TO idispatch_alerter;
GRANT SELECT, INSERT ON idispatch_alerter.alert_resource TO idispatch_alerter_role;

--
-- ########################
--

CREATE TABLE idispatch_alerter.alert_ack
(
    id               BIGSERIAL   NOT NULL,
    opt_lock_version BIGINT      NOT NULL DEFAULT 0,
    ack_date         timestamptz NOT NULL,
    alert_id         BIGINT      NOT NULL,
    recipient_id     BIGINT      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (alert_id) REFERENCES idispatch_alerter.alert (id),
    FOREIGN KEY (recipient_id) REFERENCES idispatch_alerter.recipient (id),
    UNIQUE (alert_id, recipient_id)
);

ALTER TABLE idispatch_alerter.alert_ack
    OWNER TO idispatch_alerter;
GRANT SELECT, INSERT ON idispatch_alerter.alert_ack TO idispatch_alerter_role;
ALTER SEQUENCE idispatch_alerter.alert_ack_id_seq
    OWNER TO idispatch_alerter;
GRANT USAGE, SELECT ON SEQUENCE idispatch_alerter.alert_ack_id_seq TO idispatch_alerter_role;
