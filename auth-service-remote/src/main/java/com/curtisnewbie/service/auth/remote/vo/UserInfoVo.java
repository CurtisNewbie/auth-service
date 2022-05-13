package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.service.auth.remote.consts.ReviewStatus;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Basic info of user
 *
 * @author yongjie.zhuang
 */
@Data
public class UserInfoVo implements Serializable {

    /** user id */
    private Integer id;

    /** username */
    private String username;

    /** role */
    private UserRole role;

    /** whether the user is disabled, 0-normal, 1-disabled */
    private UserIsDisabled isDisabled;

    /** Registration Review Status */
    private ReviewStatus reviewStatus;

    /** when the user is created */
    private LocalDateTime createTime;

    /** when the user is updated */
    private LocalDateTime updateTime;

    /** who updated this user */
    private String updateBy;

    /** who created this user */
    private String createBy;
}
