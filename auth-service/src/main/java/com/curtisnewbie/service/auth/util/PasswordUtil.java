package com.curtisnewbie.service.auth.util;


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
     * <pre>
     * boolean isPasswordMatched = PasswordValidUtil.getValidator()
     *  .givenPassword(enteredPassword)
     *  .compareTo(userEntity.getPassword())
     *  .withSalt(userEntity.getSalt())
     *  .isMatched();
     * </pre>
     */
    public static PasswordValidator getValidator() {
        return new PasswordValidator();
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
        private String passwordHash;

        private PasswordValidator() {
        }

        /**
         * The password that is being validated (which is the one user entered)
         *
         * @param password the password provided by the user
         * @param salt     the salt of the user
         */
        public PasswordValidator givenPasswordAndSalt(String password, String salt) {
            Objects.requireNonNull(password);
            Objects.requireNonNull(salt);
            this.passwordAndSalt = concatPasswordAndSalt(password, salt);
            return this;
        }

        /**
         * The correct password in database (the hashed password)
         */
        public PasswordValidator compareToPasswordHash(String passwordHash) {
            Objects.requireNonNull(passwordHash);
            this.passwordHash = passwordHash;
            return this;
        }

        /**
         * Check if the passwords are matched
         */
        public boolean isMatched() {
            return passwordEncoder.matches(passwordAndSalt, passwordHash);
        }
    }
}
