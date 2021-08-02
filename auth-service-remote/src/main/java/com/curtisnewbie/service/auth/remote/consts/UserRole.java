package com.curtisnewbie.service.auth.remote.consts;

import com.curtisnewbie.common.enums.ValueEnum;
import com.curtisnewbie.common.util.EnumUtils;

import java.util.Objects;

/**
 * User's role enum
 *
 * @author yongjie.zhuang
 */
public enum UserRole implements ValueEnum<String> {

    /** Administrator */
    ADMIN("admin"),

    /** Normal user */
    USER("user"),

    /** Guest */
    GUEST("guest");

    private final String val;

    UserRole(String v) {
        this.val = v;
    }

    public static UserRole parseUserRole(String userRole) {
        Objects.requireNonNull(userRole);
        return EnumUtils.parse(userRole, UserRole.class);
    }

    @Override
    public String getValue() {
        return this.val;
    }
}
