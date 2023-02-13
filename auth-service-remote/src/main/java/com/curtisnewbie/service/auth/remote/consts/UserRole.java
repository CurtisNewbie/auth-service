package com.curtisnewbie.service.auth.remote.consts;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.curtisnewbie.common.enums.ValueEnum;
import com.curtisnewbie.common.util.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * User's role enum
 *
 * @author yongjie.zhuang
 */
@Deprecated // TODO remove this
public enum UserRole implements ValueEnum<String> {

    /** Administrator */
    ADMIN("admin"),

    /** Normal user */
    USER("user"),

    /** Guest */
    GUEST("guest");

    @EnumValue
    @JsonValue
    private final String val;

    UserRole(String v) {
        this.val = v;
    }

    @Override
    public String getValue() {
        return this.val;
    }

    @JsonCreator
    public static UserRole parseUserRole(String userRole) {
        if (userRole == null) return null;
        return EnumUtils.parse(userRole, UserRole.class);
    }

    public static boolean isRole(String s, UserRole userRole) {
        UserRole role = parseUserRole(s);
        if (role == null)
            return false;

        return role == userRole;
    }

    public static boolean isAdmin(String s) {
        return isRole(s, UserRole.ADMIN);
    }

}
