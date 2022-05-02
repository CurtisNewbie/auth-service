alter table access_log add column url VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'request url' after user_id;
