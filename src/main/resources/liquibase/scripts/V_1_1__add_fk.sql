-- liquibase formatted sql

-- changeset fifimova:1.1
ALTER TABLE users
    ADD CONSTRAINT users_shelter_fk FOREIGN KEY (shelter_id) REFERENCES shelters (id) ON DELETE CASCADE;

--changeset fifimova:3
ALTER TABLE photo
    ADD CONSTRAINT report_fk FOREIGN KEY (report_id) REFERENCES reports (id) ON DELETE CASCADE;

ALTER TABLE reports
    ADD CONSTRAINT photo_fk FOREIGN KEY (photo_id) REFERENCES photo (id) ON DELETE CASCADE;

