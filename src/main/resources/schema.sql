-- Delete and recreate database
DROP DATABASE IF EXISTS laboration2;
CREATE DATABASE laboration2;
USE laboration2;

-- Delete the tables if existing
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS places;

-- Delete users if exist
DROP USER IF EXISTS dev@localhost;
DROP USER IF EXISTS web@localhost;

-- Create tables in database
CREATE TABLE IF NOT EXISTS categories
(
    id          INT AUTO_INCREMENT,
    name        VARCHAR(255) UNIQUE NOT NULL,
    symbol      VARCHAR(255)        NOT NULL,
    description VARCHAR(255)        NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT,
    username   VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS places
(
    id                INT AUTO_INCREMENT,
    name              VARCHAR(255) UNIQUE        NOT NULL,
    category_id       INT                        NOT NULL,
    added_by_user_id  INT,
    public_or_private ENUM ('public', 'private') NOT NULL DEFAULT 'public',
    last_updated      TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP(),
    description       VARCHAR(255)               NOT NULL,
    coordinates       GEOMETRY SRID 4326         NOT NULL,
    created           TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (added_by_user_id) REFERENCES users (id)
);

-- Trigger to update the date after the row is changed
CREATE TRIGGER update_date
    BEFORE UPDATE
    ON places
    FOR EACH ROW
    SET NEW.last_updated = CURRENT_TIMESTAMP();

-- Trigger that automatically adds the created and updated date if not provided
DELIMITER //
CREATE TRIGGER fill_created_and_updated_date
    BEFORE INSERT
    ON places
    FOR EACH ROW
BEGIN
    IF NEW.last_updated IS NULL THEN
        SET NEW.last_updated = CURRENT_TIMESTAMP();
    END IF;
    IF NEW.created IS NULL THEN
        SET NEW.created = CURRENT_TIMESTAMP();
    END IF;
END;
DELIMITER ;
-- Creating users
-- Webserver
CREATE USER web@localhost
    IDENTIFIED WITH 'caching_sha2_password' by 'webpassword';
GRANT DELETE, INSERT, SELECT, UPDATE on laboration2.* to web@localhost;

-- Developer
CREATE USER dev@localhost
    IDENTIFIED WITH 'caching_sha2_password' by 'devpassword';
GRANT ALTER, CREATE VIEW, CREATE ROUTINE, TRIGGER, UPDATE, DELETE, INSERT, SELECT on laboration2.* to dev@localhost;