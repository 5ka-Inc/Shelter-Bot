-- liquibase formatted sql

-- changeset fifimova:1.1
ALTER TABLE users
    ADD CONSTRAINT users_shelter_fk FOREIGN KEY (shelter_id) REFERENCES shelters (id) ON DELETE CASCADE;

--changeset fifimova:3

ALTER TABLE reports
    ADD CONSTRAINT photo_fk FOREIGN KEY (photo_id) REFERENCES photo (id) ON DELETE CASCADE;

--changeset Ependi:4.1
ALTER TABLE reports
    ADD CONSTRAINT report_user_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

-- changeset Ependi:4.2
ALTER TABLE photo
    DROP COLUMN data;
ALTER TABLE photo
    ADD COLUMN data oid;

-- changeset Ependi:4.3
ALTER TABLE reports
    DROP COLUMN date;

alter table reports
    add column date timestamp with time zone;
--changeset Ependi:4.4
ALTER TABLE photo
    ADD COLUMN file_size BIGINT;
ALTER TABLE photo
    ADD COLUMN media_type TEXT;
