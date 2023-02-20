package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.service.auth.remote.consts.ReviewStatus;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Basic info of user
 *
 * @author yongjie.zhuang
 */
@Data
public class UserInfoVo {

    private Integer id;
    private String username;
    private String roleName;
    private String roleNo;
    private String userNo;
    private UserIsDisabled isDisabled;
    private ReviewStatus reviewStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String updateBy;
    private String createBy;
}
