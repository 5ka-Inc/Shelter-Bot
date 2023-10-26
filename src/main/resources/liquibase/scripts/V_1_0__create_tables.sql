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
    id        bigserial PRIMARY KEY ,
    data      bytea,
    report_id bigserial


);

CREATE TABLE reports
(
    id              bigserial PRIMARY KEY,
    date            timestamp,
    diet            text,
    health          text,
    behavior        text,
    is_report_valid bool,
    photo_id        bigserial,
    user_id         bigint

);

