-- it's pretty save to change the length of user_no from 64 to 32,
-- because the generated one doesn't even have length 32
-- it's not so true for username tho, check before you run it

alter table user
  modify column `user_no` varchar(32) NOT NULL COMMENT 'user no',
  modify column `username` varchar(50) NOT NULL COMMENT 'username',
  drop constraint username_2
;