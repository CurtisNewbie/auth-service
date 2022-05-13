alter table access_log add column url VARCHAR(255) DEFAULT '' COMMENT 'request url' after user_id;
alter table access_log_history add column url VARCHAR(255) DEFAULT '' COMMENT 'request url' after user_id;
