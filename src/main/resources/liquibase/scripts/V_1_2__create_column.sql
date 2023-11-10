-- liquibase formatted sql

-- changeset Ependi:1.2
ALTER TABLE users
    ADD COLUMN role TEXT;


-- changeset Vasyan:1
ALTER TABLE users
    ADD COLUMN username TEXT;

-- changeset fifimova:2
ALTER TABLE tickets
    ADD COLUMN issue_description TEXT;

-- changeset fifimova:3
ALTER TABLE tickets
    ADD COLUMN is_closed BOOLEAN;

--changeset fifimova:4
ALTER TABLE photo
    ADD COLUMN file_id TEXT;