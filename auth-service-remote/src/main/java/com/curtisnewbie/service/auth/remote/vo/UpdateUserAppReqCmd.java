package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdateUserAppReqCmd implements Serializable {

    /**
     * User's id
     */
    private int userId;

    /**
     * List of app id
     */
    private List<Integer> appIdList;

    public void validate() {
        Assert.notNull(userId, "user_id must not be null");
    }

    /**
     * Has app records to assign to the user
     */
    public boolean hasAppToAssign() {
        return appIdList != null && !appIdList.isEmpty();
    }
}
