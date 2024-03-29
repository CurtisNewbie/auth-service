CREATE TABLE IF NOT EXISTS user (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `username` varchar(50) NOT NULL COMMENT 'username',
  `password` varchar(255) NOT NULL COMMENT 'password in hash',
  `salt` varchar(10) NOT NULL COMMENT 'salt',
  `role` varchar(20) NOT NULL COMMENT 'role',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated',
  `is_disabled` int NOT NULL DEFAULT '0' COMMENT 'whether the user is disabled, 0-normal, 1-disabled',
  `review_status` varchar(25) NOT NULL COMMENT 'Review Status',
  `update_by` varchar(255) NOT NULL DEFAULT '' COMMENT 'who updated this record',
  `create_by` varchar(255) NOT NULL DEFAULT '' COMMENT 'who created this record',
  `is_del` tinyint NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted',
  `user_no` varchar(32) NOT NULL COMMENT 'user no',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `user_no` (`user_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMNET = 'users';

CREATE TABLE IF NOT EXISTS user_key (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `user_id` int unsigned NOT NULL COMMENT 'user.id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT 'name of the key',
  `secret_key` varchar(255) NOT NULL COMMENT 'secret key',
  `expiration_time` datetime NOT NULL COMMENT 'when the key is expired',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created',
  `create_by` varchar(255) NOT NULL DEFAULT '' COMMENT 'who created this record',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated',
  `update_by` varchar(255) NOT NULL DEFAULT '' COMMENT 'who updated this record',
  `is_del` tinyint NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted',
  PRIMARY KEY (`id`),
  UNIQUE KEY `secret_key` (`secret_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT="user's key";

CREATE TABLE IF NOT EXISTS access_log (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `access_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'when the user signed in',
  `ip_address` varchar(255) NOT NULL COMMENT 'ip address',
  `username` varchar(255) NOT NULL COMMENT 'username',
  `user_id` int unsigned NOT NULL COMMENT 'primary key of user',
  `url` varchar(255) DEFAULT '' COMMENT 'request url',
  `user_agent` varchar(512) NOT NULL DEFAULT '' COMMENT 'User Agent',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='access log';

CREATE TABLE IF NOT EXISTS operate_log (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `operate_name` varchar(255) NOT NULL COMMENT 'name of operation',
  `operate_desc` varchar(255) NOT NULL DEFAULT '' COMMENT 'description of operation',
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'when the operation happens',
  `operate_param` varchar(1000) NOT NULL COMMENT 'parameters used for the operation',
  `username` varchar(255) NOT NULL COMMENT 'username',
  `user_id` int unsigned NOT NULL COMMENT 'primary key of user',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='operate log';

-- copied from distributed-task-module
CREATE TABLE task (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT "id",
    job_name VARCHAR(255) NOT NULL COMMENT "job's name",
    target_bean VARCHAR(255) NOT NULL COMMENT "name of bean that will be executed",
    cron_expr VARCHAR(255) NOT NULL COMMENT "cron expression",
    app_group VARCHAR(255) NOT NULL COMMENT "app group that runs this task",
    last_run_start_time TIMESTAMP COMMENT "the last time this task was executed",
    last_run_end_time TIMESTAMP COMMENT "the last time this task was finished",
    last_run_by VARCHAR(255) COMMENT "task triggered by",
    last_run_result VARCHAR(255) COMMENT "result of last execution",
    enabled INT NOT NULL DEFAULT 0 COMMENT "whether the task is enabled: 0-disabled, 1-enabled",
    concurrent_enabled INT NULL DEFAULT 0 COMMENT "whether the task can be executed concurrently: 0-disabled, 1-enabled",
    update_date TIMESTAMP COMMENT 'update time',
    update_by VARCHAR(255) COMMENT 'updated by',
    KEY app_group_idx (app_group)
) ENGINE=InnoDB COMMENT "task";

-- copied from distributed-task-module
CREATE TABLE task_history (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT "id",
    task_id INT COMMENT "task id",
    start_time TIMESTAMP COMMENT "start time",
    end_time TIMESTAMP COMMENT "end time",
    run_by VARCHAR(255) COMMENT "task triggered by",
    run_result VARCHAR(255) COMMENT "result of last execution",
    create_time DATETIME COMMENT "create time",
    KEY task_id_idx (task_id)
) ENGINE=InnoDB COMMENT "task history";

