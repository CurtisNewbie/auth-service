package com.curtisnewbie.service.auth.util;

import com.curtisnewbie.common.exceptions.UnrecoverableException;

import java.util.regex.Pattern;

import static com.curtisnewbie.common.util.AssertUtils.isTrue;

/**
 * Validator of user's attributes
 *
 * @author yongj.zhuang
 */
public final class UserValidator {

    public static final int PASSWORD_LENGTH = 8;
    public static final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_\\-@.]{6,}$");

    private UserValidator() {
    }

    /**
     * Validate username pattern
     *
     * @throws UnrecoverableException if invalid
     */
    public static void validateUsername(String username) {
        isTrue(usernamePattern.matcher(username).matches(),
                "Username must have at least 6 characters, permitted characters include: 'a-z A-Z 0-9 . - _ @'");
    }

    /**
     * Validate password pattern
     *
     * @throws UnrecoverableException if invalid
     */
    public static void validatePassword(String password) {
        isTrue(password.length() >= PASSWORD_LENGTH, "Password must have at least " + PASSWORD_LENGTH + " characters");
    }
}
