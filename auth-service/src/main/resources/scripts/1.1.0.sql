alter table access_log_history add column token VARCHAR(1000) DEFAULT '' COMMENT 'token' after url;
alter table access_log add column token VARCHAR(1000) DEFAULT '' COMMENT 'token' after url;

-- add review_status, for the existing records, we set them to "APPROVED"
alter table user add column review_status VARCHAR(25) NOT NULL COMMENT 'Review Status' after is_disabled;
update user set review_status = "APPROVED" where is_del = 0;

-- drop event_handling table, it's deprecated
drop table event_handling if exists;

-- before generating the user_no using a task or filling them manually
alter table user add column user_no VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'user no';

-- add the new job to generate user_no, then trigger job
INSERT INTO task (job_name,target_bean,cron_expr,app_group,last_run_start_time,last_run_end_time,last_run_by,enabled,concurrent_enabled,last_run_result,update_date,update_by)
	VALUES ('GenerateUserNoJob','generateUserNoJob','0 0 0 ? * *','auth-service',now(),now(),'',1,0,'',now(),'');

-- after generating the user_no for existing records, add required constraints back to it
alter table user modify column user_no VARCHAR(64) NOT NULL UNIQUE COMMENT 'user no';
