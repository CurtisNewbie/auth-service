package com.curtisnewbie.service.auth.web.open.api.vo;


import com.curtisnewbie.common.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
public class UserKeyVo {

    private Integer id;

    /** secret key */
    private String secretKey;

    /** name of the key */
    private String name;

    /** when the key is expired */
    @JsonFormat(pattern = DateUtils.DD_MM_YYYY_HH_MM)
    private LocalDateTime expirationTime;

    /** when the record is created */
    @JsonFormat(pattern = DateUtils.DD_MM_YYYY_HH_MM)
    private LocalDateTime createTime;

    @ToString.Include(name = "secretKey")
    public String secretKeyMask() {
        return secretKey != null ? "****" : null;
    }

}
