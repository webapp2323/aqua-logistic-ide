--liquibase formatted sql

--changeset oleksandr:2_add-product-to-task
ALTER TABLE task ADD product_id BIGINT;

ALTER TABLE task ADD CONSTRAINT fk_task_product FOREIGN KEY (product_id) REFERENCES product(id);
