alter table user_key add column name VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'name of the key' after user_id;