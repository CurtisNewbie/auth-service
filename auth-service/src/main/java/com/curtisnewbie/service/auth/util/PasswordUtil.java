package com.curtisnewbie.service.auth.util;


import com.curtisnewbie.service.auth.dao.User;

import java.util.Objects;

/**
 * Utility class for password handling (e.g., password validation, password encoding)
 *
 * @author yongjie.zhuang
 */
public final class PasswordUtil {

    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactory.getPasswordEncoder("SHA-256");

    private PasswordUtil() {

    }

    /**
     * Get {@code PasswordValidator} for password validation
     * <p>
     * Use it as follows:
     *
     * <pre>
     * final boolean isPwdCorrect = PasswordUtil.getValidator(user)
     *      .givenPassword(password)
     *      .isMatched();
     * </pre>
     */
    public static PasswordValidator getValidator(User u) {
        return new PasswordValidator(u);
    }

    /**
     * Encode password with salt
     *
     * @param plainPwd password
     * @param salt     salt
     */
    public static String encodePassword(String plainPwd, String salt) {
        return passwordEncoder.encodePassword(concatPasswordAndSalt(plainPwd, salt));
    }

    /**
     * Concatenate password and salt (this is to provide a uniform way to concat the password and salt)
     *
     * @param plainPwd password
     * @param salt     salt
     */
    public static String concatPasswordAndSalt(String plainPwd, String salt) {
        return plainPwd.concat(salt);
    }


    public static class PasswordValidator {
        private String passwordAndSalt;
        private final User user;

        private PasswordValidator(User user) {
            this.user = user;
        }

        /**
         * The password that is being validated (which is the one user entered)
         *
         * @param password the password provided by the user
         */
        public PasswordValidator givenPassword(String password) {
            Objects.requireNonNull(password);
            Objects.requireNonNull(user);
            this.passwordAndSalt = concatPasswordAndSalt(password, user.getSalt());
            return this;
        }

        /**
         * Check if the passwords are matched
         */
        public boolean isMatched() {
            return passwordEncoder.matches(passwordAndSalt, user.getPassword());
        }
    }
}
