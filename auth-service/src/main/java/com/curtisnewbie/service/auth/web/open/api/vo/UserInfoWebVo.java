package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.util.DateUtils;
import com.curtisnewbie.service.auth.remote.consts.ReviewStatus;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * UserInfo web vo
 *
 * @author yongjie.zhuang
 */
@Data
public class UserInfoWebVo {

    /** id */
    private Integer id;

    /** username */
    private String username;

    /** role */
    private UserRole role;

    /** userNo */
    private String userNo;

    /** whether the user is disabled, 0-normal, 1-disabled */
    private UserIsDisabled isDisabled;

    /** User registration review status */
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
