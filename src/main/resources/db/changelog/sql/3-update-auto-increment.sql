--liquibase formatted sql

--changeset oleksandr:3_update_auto_increment

SET @max_account_id = (SELECT IFNULL(MAX(id), 0) FROM account);
SET @max_product_id = (SELECT IFNULL(MAX(id), 0) FROM product);
SET @max_task_id = (SELECT IFNULL(MAX(id), 0) FROM task);

SET @new_account_id = @max_account_id + 1;
SET @new_product_id = @max_product_id + 1;
SET @new_task_id = @max_task_id + 1;

SET @sql_account = CONCAT('ALTER TABLE account AUTO_INCREMENT = ', @new_account_id);
SET @sql_product = CONCAT('ALTER TABLE product AUTO_INCREMENT = ', @new_product_id);
SET @sql_task = CONCAT('ALTER TABLE task AUTO_INCREMENT = ', @new_task_id);

PREPARE stmt_account FROM @sql_account;
EXECUTE stmt_account;
DEALLOCATE PREPARE stmt_account;

PREPARE stmt_product FROM @sql_product;
EXECUTE stmt_product;
DEALLOCATE PREPARE stmt_product;

PREPARE stmt_task FROM @sql_task;
EXECUTE stmt_task;
DEALLOCATE PREPARE stmt_task;

SELECT @new_account_id AS new_account_auto_increment,
       @new_product_id AS new_product_auto_increment,
       @new_task_id AS new_task_auto_increment;