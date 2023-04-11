CREATE TABLE IF NOT EXISTS endpoint_hits
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY,
    app       VARCHAR                     NOT NULL,
    uri       VARCHAR                     NOT NULL,
    ip        VARCHAR                     NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_hit PRIMARY KEY (id)
);