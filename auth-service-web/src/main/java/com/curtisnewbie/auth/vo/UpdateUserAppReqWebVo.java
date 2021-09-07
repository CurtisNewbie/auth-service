package com.curtisnewbie.auth.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdateUserAppReqWebVo implements Serializable {

    /**
     * User's id
     */
    private Integer userId;

    /**
     * List of app id
     */
    private List<Integer> appIdList;
}
