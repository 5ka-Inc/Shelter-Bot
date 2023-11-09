-- liquibase formatted sql

-- changeset fifimova:1
INSERT INTO shelters (name, type)
VALUES ('Raise and shine', 'DOG'),
       ('Dogs only', 'DOG'),
       ('Meow', 'CAT'),
       ('Paradise', 'CAT');

-- changeset fifimova:2
INSERT INTO users (id, chat_id, name, phone, shelter_id, is_adopter, role, username)
VALUES (1061919855, 1061919855, 'fifimova', '89997776655', 1, false, 'VOLUNTEER', 'fifimova');