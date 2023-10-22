-- liquibase formatted sql

-- changeset fifimova:1
CREATE TABLE users
(
    id           UUID DEFAULT gen_random_uuid(),
    chat_id      BIGINT NOT NULL,
    name         TEXT,
    phone        TEXT,
    shelter_id   UUID,
    volunteer_id UUID,
    is_adopter   BOOLEAN,

    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE shelters
(
    id   UUID DEFAULT gen_random_uuid(),
    name TEXT,
    type TEXT,

    CONSTRAINT shelter_pk PRIMARY KEY (id)
);

CREATE TABLE volunteers
(
    id         UUID DEFAULT gen_random_uuid(),
    chat_id    BIGINT NOT NULL,
    name       TEXT,
    shelter_id UUID,

    CONSTRAINT volunteer_pk PRIMARY KEY (id)
);

-- changeset vasyan:1

CREATE TABLE users
(
    id           BIGINT,
    chat_id      BIGINT NOT NULL,
    name         TEXT,
    phone        TEXT,
    shelter_id   UUID,
    volunteer_id UUID,
    is_adopter   BOOLEAN,

    CONSTRAINT user_pk PRIMARY KEY (id)
);
