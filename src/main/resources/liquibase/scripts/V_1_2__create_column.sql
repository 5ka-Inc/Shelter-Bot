-- liquibase formatted sql

-- changeset Ependi:1.2
ALTER TABLE users
    ADD COLUMN role TEXT;


-- changeset Vasyan:1
ALTER TABLE users
    ADD COLUMN username TEXT;

