-- liquibase formatted sql

-- changeset hellhorseman:1
CREATE TABLE infos
(
    id           BIGSERIAL primary key,
    command_info TEXT,
    info         TEXT,
    shelter_id   BIGINT
);