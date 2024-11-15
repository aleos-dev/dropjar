--liquibase formatted sql

--changeset aleos:1
CREATE TABLE USERS (
    id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY ,
    email varchar(100) NOT NULL ,
    password VARCHAR NOT NULL ,
    role VARCHAR(20) NOT NULL ,

    CONSTRAINT  uq_users_email UNIQUE (email)
)