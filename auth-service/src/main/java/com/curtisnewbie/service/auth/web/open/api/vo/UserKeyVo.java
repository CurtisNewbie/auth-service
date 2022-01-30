package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.dao.DaoSkeleton;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
public class UserKeyVo extends DaoSkeleton {

    /** user.id */
    private Integer userId;

    /** secret key */
    private String secretKey;

    /** when the key is expired */
    private LocalDateTime expirationTime;
}
