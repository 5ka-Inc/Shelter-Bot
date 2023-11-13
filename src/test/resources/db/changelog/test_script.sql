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

CREATE TABLE photo
(
    id        bigint PRIMARY KEY,
    data      bytea,
    report_id bigint
);

CREATE TABLE reports
(
    id              bigint PRIMARY KEY,
    date            timestamp,
    diet            text,
    health          text,
    behavior        text,
    is_report_valid bool,
    photo_id        bigint,
    user_id         bigint
);

CREATE TABLE tickets
(
    id            BIGSERIAL PRIMARY KEY,
    creation_time timestamp not null,
    received_time timestamp,
    user_id       bigint,
    username      TEXT      not null,
    volunteer_id  bigint
);

ALTER TABLE users
    ADD COLUMN role TEXT;

ALTER TABLE users
    ADD COLUMN username TEXT;

ALTER TABLE tickets
    ADD COLUMN issue_description TEXT;

ALTER TABLE tickets
    ADD COLUMN is_closed BOOLEAN;

ALTER TABLE users
    ADD CONSTRAINT users_shelter_fk FOREIGN KEY (shelter_id) REFERENCES shelters (id) ON DELETE CASCADE;

ALTER TABLE photo
    ADD CONSTRAINT report_fk FOREIGN KEY (report_id) REFERENCES reports (id) ON DELETE CASCADE;

ALTER TABLE reports
    ADD CONSTRAINT photo_fk FOREIGN KEY (photo_id) REFERENCES photo (id) ON DELETE CASCADE;

ALTER TABLE reports
    ADD CONSTRAINT report_user_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE reports
    DROP COLUMN date;

alter table reports
    add column date timestamp with time zone;

ALTER TABLE photo
    ADD COLUMN file_size BIGINT;
ALTER TABLE photo
    ADD COLUMN media_type TEXT;