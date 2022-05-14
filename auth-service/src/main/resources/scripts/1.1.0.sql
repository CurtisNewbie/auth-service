alter table access_log_history add column token VARCHAR(1000) DEFAULT '' COMMENT 'token' after url;
alter table access_log add column token VARCHAR(1000) DEFAULT '' COMMENT 'token' after url;
alter table user add column review_status VARCHAR(25) NOT NULL COMMENT 'Review Status' after is_disabled;
update user set review_status = "APPROVED" where is_del = 0;
drop table event_handling if exists;