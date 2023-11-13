-- liquibase formatted sql

-- changeset hellhorseman:1
CREATE TABLE info
(
    id BIGINT   primary key,
    command_info SMALLINT,
    info TEXT,
    shelter_id BIGINT

);