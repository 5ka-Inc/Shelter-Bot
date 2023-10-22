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
