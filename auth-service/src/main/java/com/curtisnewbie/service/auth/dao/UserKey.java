package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.curtisnewbie.common.dao.DaoSkeleton;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
@TableName("user_key")
public class UserKey extends DaoSkeleton {

    /** user.id */
    @TableField("user_id")
    private Integer userId;

    /** secret key */
    @TableField("secret_key")
    private String secretKey;

    /** name of the key */
    @TableField("name")
    private String name;

    /** when the key is expired */
    @TableField("expiration_time")
    private LocalDateTime expirationTime;
}
