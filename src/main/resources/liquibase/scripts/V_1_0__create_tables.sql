-- liquibase formatted sql

-- changeset fifimova:1
CREATE TABLE users
(
    id         BIGINT NOT NULL,
    chat_id    BIGINT NOT NULL,
    name       TEXT,
    phone      TEXT,
    shelter_id BIGINT,
    is_adopter BOOLEAN,

    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE shelters
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name TEXT,
    type TEXT,

    CONSTRAINT shelter_pk PRIMARY KEY (id)
);

-- changeset vasyan:1

CREATE TABLE photo
(
    id        BIGSERIAL PRIMARY KEY,
    data      bytea,
    report_id bigint
);

CREATE TABLE reports
(
    id              BIGSERIAL PRIMARY KEY,
    date            timestamp,
    diet            text,
    health          text,
    behavior        text,
    is_report_valid bool,
    photo_id        bigint,
    user_id         bigint
);

-- changeset vasyan:2

CREATE TABLE tickets
(
    id            BIGSERIAL PRIMARY KEY,
    creation_time timestamp not null,
    received_time timestamp,
    user_id       bigint,
    username      TEXT      not null,
    volunteer_id  bigint
);

