
CREATE TABLE IF NOT EXISTS user (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    username VARCHAR(255) NOT NULL UNIQUE COMMENT "username (must be unique)",
    password VARCHAR(255) NOT NULL COMMENT "password in hash",
    salt VARCHAR(10) NOT NULL COMMENT "salt",
    role VARCHAR(20) NOT NULL COMMENT "role",
    is_disabled INT NOT NULL DEFAULT 0 COMMENT 'whether the user is disabled, 0-normal, 1-disabled',
    create_time DATETIME NOT NULL DEFAULT NOW() COMMENT 'when the user is created',
    create_by VARCHAR(255) NOT NULL COMMENT 'who created this user',
    update_time DATETIME COMMENT 'when the user is updated',
    update_by VARCHAR(255) COMMENT 'who updated this user',
    is_del TINYINT NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted'
) ENGINE=InnoDB COMMENT 'user';

CREATE table if not exists user_key (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    user_id INT UNSIGNED NOT NULL COMMENT "user.id",
    secret_key VARCHAR(255) NOT NULL UNIQUE COMMENT "secret key",
    expiration_time DATETIME NOT NULL COMMENT 'when the key is expired',
    create_time DATETIME NOT NULL DEFAULT NOW() COMMENT 'when the user is created',
    create_by VARCHAR(255) NOT NULL COMMENT 'who created this user',
    update_time DATETIME COMMENT 'when the user is updated',
    update_by VARCHAR(255) COMMENT 'who updated this user',
    is_del TINYINT NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted'
) ENGINE=InnoDB COMMENT "user's key";

CREATE TABLE IF NOT EXISTS app (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    name VARCHAR (255) UNIQUE NOT NULL COMMENT 'name of the application',
    create_time DATETIME DEFAULT NOW() COMMENT 'when the record is created',
    create_by VARCHAR(255) COMMENT 'who created this record',
    update_time DATETIME COMMENT 'when the record is updated',
    update_by VARCHAR(255) COMMENT 'who updated this record'
) ENGINE=InnoDB COMMENT 'application';

CREATE TABLE IF NOT EXISTS user_app (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    user_id INT UNSIGNED NOT NULL COMMENT "user's id",
    app_id INT UNSIGNED NOT NULL COMMENT "app's id",
    create_time DATETIME DEFAULT NOW() COMMENT 'when the record is created',
    create_by VARCHAR(255) COMMENT 'who created this record',
    update_time DATETIME COMMENT 'when the record is updated',
    update_by VARCHAR(255) COMMENT 'who updated this record',
    is_del TINYINT NOT NULL DEFAULT '0' COMMENT '0-normal, 1-deleted',
    CONSTRAINT uk_user_app UNIQUE (user_id, app_id)
) ENGINE=InnoDB COMMENT 'join table between application and user';

CREATE TABLE IF NOT EXISTS event_handling (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT "primary key",
    type INT NOT NULL COMMENT "type of event, 1-registration",
    body VARCHAR(1000) NOT NULL COMMENT "body of the event",
    description VARCHAR(255) NOT NULL default '' COMMENT 'description of the event',
    status INT NOT NULL COMMENT "status of event, 0-no need to handle, 1-to be handled, 2-handled",
    handle_result TINYINT COMMENT "handle result, 1-accept, 2-reject",
    handler_id INT UNSIGNED COMMENT "id of user who handled the event",
    handle_time DATETIME COMMENT 'when the event is handled'
) ENGINE=InnoDB COMMENT 'events that need to be handled by someone, e.g., administrators';

CREATE TABLE IF NOT EXISTS access_log (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'primary key',
    access_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT 'when the user signed in',
    ip_address VARCHAR(255) NOT NULL COMMENT 'ip address',
    username VARCHAR(255) NOT NULL COMMENT 'username',
    user_id INT UNSIGNED NOT NULL COMMENT 'primary key of user',
    url VARCHAR(255) DEFAULT '' COMMENT 'request url',
) ENGINE=InnoDB COMMENT 'access log';

CREATE TABLE IF NOT EXISTS access_log_history (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'primary key',
    access_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT 'when the user signed in',
    ip_address VARCHAR(255) NOT NULL COMMENT 'ip address',
    username VARCHAR(255) NOT NULL COMMENT 'username',
    user_id INT UNSIGNED NOT NULL COMMENT 'primary key of user'
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
