package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.curtisnewbie.common.dao.DaoSkeleton;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Join table between application and user
 * </p>
 *
 * @author yongjie.zhuang
 */
@Data
@TableName("user_app")
public class UserApp extends DaoSkeleton {

    /** user's id */
    @TableField("user_id")
    private Integer userId;

    /** app's id */
    @TableField("app_id")
    private Integer appId;

}