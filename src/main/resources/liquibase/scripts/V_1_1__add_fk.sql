-- liquibase formatted sql

-- changeset fifimova:1.1
ALTER TABLE users
    ADD CONSTRAINT users_shelter_fk FOREIGN KEY (shelter_id) REFERENCES shelters (id) ON DELETE CASCADE;

ALTER TABLE users
    ADD CONSTRAINT users_volunteer_fk FOREIGN KEY (volunteer_id) REFERENCES volunteers (id) ON DELETE CASCADE;

ALTER TABLE volunteers
ADD CONSTRAINT volunteers_shelter_fk FOREIGN KEY (shelter_id) REFERENCES shelters (id) ON DELETE CASCADE;