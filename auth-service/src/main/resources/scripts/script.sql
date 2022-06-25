
CREATE TABLE IF NOT EXISTS user (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    user_no VARCHAR(64) NOT NULL UNIQUE COMMENT 'user no',
    username VARCHAR(255) NOT NULL UNIQUE COMMENT "username (must be unique)",
    password VARCHAR(255) NOT NULL COMMENT "password in hash",
    salt VARCHAR(10) NOT NULL COMMENT "salt",
    role VARCHAR(20) NOT NULL COMMENT "role",
    is_disabled INT NOT NULL DEFAULT 0 COMMENT 'whether the user is disabled, 0-normal, 1-disabled',
    review_status VARCHAR(25) NOT NULL COMMENT 'Review Status',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created',
    create_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who created this record',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated',
    update_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who updated this record',
    is_del TINYINT NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted'
) ENGINE=InnoDB COMMENT 'user';

CREATE table if not exists user_key (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    user_id INT UNSIGNED NOT NULL COMMENT "user.id",
    name VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'name of the key',
    secret_key VARCHAR(255) NOT NULL UNIQUE COMMENT "secret key",
    expiration_time DATETIME NOT NULL COMMENT 'when the key is expired',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created',
    create_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who created this record',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated',
    update_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who updated this record',
    is_del TINYINT NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted'
) ENGINE=InnoDB COMMENT "user's key";

CREATE TABLE IF NOT EXISTS app (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    name VARCHAR (255) UNIQUE NOT NULL COMMENT 'name of the application',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created',
    create_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who created this record',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated',
    update_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who updated this record',
    is_del TINYINT NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted'
) ENGINE=InnoDB COMMENT 'application';

CREATE TABLE IF NOT EXISTS user_app (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    user_id INT UNSIGNED NOT NULL COMMENT "user's id",
    app_id INT UNSIGNED NOT NULL COMMENT "app's id",
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'when the record is created',
    create_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who created this record',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'when the record is updated',
    update_by VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'who updated this record',
    is_del TINYINT NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted',
    CONSTRAINT uk_user_app UNIQUE (user_id, app_id)
) ENGINE=InnoDB COMMENT 'join table between application and user';

CREATE TABLE IF NOT EXISTS access_log (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'primary key',
    access_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT 'when the user signed in',
    ip_address VARCHAR(255) NOT NULL COMMENT 'ip address',
    username VARCHAR(255) NOT NULL COMMENT 'username',
    user_id INT UNSIGNED NOT NULL COMMENT 'primary key of user',
    url VARCHAR(255) DEFAULT '' COMMENT 'request url',
    token VARCHAR(1000) DEFAULT '' COMMENT 'token'
) ENGINE=InnoDB COMMENT 'access log';

CREATE TABLE IF NOT EXISTS access_log_history (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'primary key',
    access_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT 'when the user signed in',
    ip_address VARCHAR(255) NOT NULL COMMENT 'ip address',
    username VARCHAR(255) NOT NULL COMMENT 'username',
    user_id INT UNSIGNED NOT NULL COMMENT 'primary key of user',
    url VARCHAR(255) DEFAULT '' COMMENT 'request url',
    token VARCHAR(1000) DEFAULT '' COMMENT 'token'
) ENGINE=InnoDB COMMENT 'access log (for history only)';

CREATE TABLE IF NOT EXISTS operate_log (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'primary key',
    operate_name VARCHAR(255) NOT NULL COMMENT 'name of operation',
    operate_desc VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'description of operation',
    operate_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT 'when the operation happens',
    operate_param VARCHAR(1000) NOT NULL  COMMENT 'parameters used for the operation',
    username VARCHAR(255) NOT NULL COMMENT 'username',
    user_id INT UNSIGNED NOT NULL COMMENT 'primary key of user'
) ENGINE=InnoDB COMMENT "operate log";

CREATE TABLE IF NOT EXISTS operate_log_history (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'primary key',
    operate_name VARCHAR(255) NOT NULL COMMENT 'name of operation',
    operate_desc VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'description of operation',
    operate_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT 'when the operation happens',
    operate_param VARCHAR(1000) NOT NULL  COMMENT 'parameters used for the operation',
    username VARCHAR(255) NOT NULL COMMENT 'username',
    user_id INT UNSIGNED NOT NULL COMMENT 'primary key of user'
) ENGINE=InnoDB COMMENT "operate log history (for history only)";

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
    update_by VARCHAR(255) COMMENT 'updated by'
) ENGINE=InnoDB COMMENT "task";

-- copied from distributed-task-module
CREATE TABLE task_history (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT "id",
    task_id INT COMMENT "task id",
    start_time TIMESTAMP COMMENT "start time",
    end_time TIMESTAMP COMMENT "end time",
    run_by VARCHAR(255) COMMENT "task triggered by",
    run_result VARCHAR(255) COMMENT "result of last execution",
    create_time DATETIME COMMENT "create time"
) ENGINE=InnoDB COMMENT "task history";

