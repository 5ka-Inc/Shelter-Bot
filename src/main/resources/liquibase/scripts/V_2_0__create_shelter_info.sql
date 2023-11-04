-- liquibase formatted sql

-- changeset fifimova:1
CREATE TABLE shelter_info
(
    shelter_id BIGINT       NOT NULL,
    property   VARCHAR(255) NOT NULL,
    info       VARCHAR(255),

    CONSTRAINT shelter_info_pk PRIMARY KEY (shelter_id, property),
    CONSTRAINT shelter_info_fk FOREIGN KEY (shelter_id) REFERENCES shelters (id)
);