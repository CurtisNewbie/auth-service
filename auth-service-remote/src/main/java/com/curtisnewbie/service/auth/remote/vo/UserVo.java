package com.curtisnewbie.service.auth.remote.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * User vo
 *
 * @author yongjie.zhuang
 */
public class UserVo implements Serializable {

    /** primary key */
    private Integer id;

    /** username (must be unique) */
    private String username;

    /** role */
    private String role;

    /** when the user is created */
    private Date createTime;

    /** when the user is updated */
    private Date updateTime;

    /** who updated this user */
    private String updateBy;

    /** who created this user */
    private String createBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}