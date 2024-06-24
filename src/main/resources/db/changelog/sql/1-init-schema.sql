--liquibase formatted sql

--changeset oleksandr:1_init-schema

CREATE TABLE account (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         email VARCHAR(255) NOT NULL,
                         password VARCHAR(255),
                         name VARCHAR(255),
                         picture_url VARCHAR(255),
                         role VARCHAR(50) NOT NULL
);

INSERT INTO account (email, password, name, role)
VALUES ('admin@admin.com', '$2a$10$.vI1CGhq.rlRNXMuWNy0v.Qmf17HydKzEsqfB6K0gTJk/B5fR8yzC', 'Admin', 'ADMIN');

CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         unit_price BIGINT NOT NULL
);

INSERT INTO product (name, unit_price)
VALUES
    ('Вода', 10),
    ('Іонізована вода', 15);

CREATE TABLE task (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      date TIMESTAMP NOT NULL,
                      address VARCHAR(255) NOT NULL,
                      phone VARCHAR(255) NOT NULL,
                      quantity INT NOT NULL,
                      price BIGINT NOT NULL,
                      status VARCHAR(50) NOT NULL,
                      account_id BIGINT NOT NULL,
                      FOREIGN KEY (account_id) REFERENCES account(id)
);