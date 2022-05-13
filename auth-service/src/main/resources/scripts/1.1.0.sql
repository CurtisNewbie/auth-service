alter table access_log_history add column token VARCHAR(1000) DEFAULT '' COMMENT 'token' after url;
alter table access_log add column token VARCHAR(1000) DEFAULT '' COMMENT 'token' after url;
