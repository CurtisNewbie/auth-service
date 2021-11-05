package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdateUserAppReqVo implements Serializable {

    /**
     * User's id
     */
    private int userId;

    /**
     * List of app id
     */
    private List<Integer> appIdList;

    /**
     * Has app records to assign to the user
     */
    public boolean hasAppToAssign() {
        return appIdList != null && !appIdList.isEmpty();
    }
}
