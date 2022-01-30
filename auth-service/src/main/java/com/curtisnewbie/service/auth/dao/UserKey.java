package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.curtisnewbie.common.dao.DaoSkeleton;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    /** when the key is expired */
    @TableField("expiration_time")
    private LocalDateTime expirationTime;
}
