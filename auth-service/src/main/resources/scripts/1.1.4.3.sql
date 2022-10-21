alter table access_log drop column token;
alter table access_log add column user_agent varchar(512) not null default "" comment 'User Agent';
