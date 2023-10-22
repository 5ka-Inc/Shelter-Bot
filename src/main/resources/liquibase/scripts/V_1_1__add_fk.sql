-- liquibase formatted sql

-- changeset fifimova:1.1
ALTER TABLE users
    ADD CONSTRAINT users_shelter_fk FOREIGN KEY (shelter_id) REFERENCES shelters (id) ON DELETE CASCADE;