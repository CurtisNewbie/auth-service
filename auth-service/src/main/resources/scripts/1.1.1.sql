
update user set create_by = '' where create_by is null;
update user set update_by = '' where update_by is null;

alter table user modify column create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created';
alter table user modify column create_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who created this record';
alter table user modify column update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated';
alter table user modify column update_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who updated this record';

update user_key set create_by = '' where create_by is null;
update user_key set update_by = '' where update_by is null;

alter table user_key modify column create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created';
alter table user_key modify column create_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who created this record';
alter table user_key modify column update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated';
alter table user_key modify column update_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who updated this record';

update app set create_by = '' where create_by is null;
update app set update_by = '' where update_by is null;

alter table app modify column create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created';
alter table app modify column create_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who created this record';
alter table app modify column update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated';
alter table app modify column update_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who updated this record';
alter table app add column is_del TINYINT NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted';

update user_app set create_by = '' where create_by is null;
update user_app set update_by = '' where update_by is null;

alter table user_app modify column create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created';
alter table user_app modify column create_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who created this record';
alter table user_app modify column update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated';
alter table user_app modify column update_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who updated this record';



