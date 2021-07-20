package com.curtisnewbie.service.auth.util;

/**
 * Password Encoder
 *
 * @author yongjie.zhuang
 */
public interface PasswordEncoder {


    /**
     * Encode password
     *
     * @param password password in plain text
     * @return encoded password (e.g., a Hash)
     */
    String encodePassword(String password);

    /**
     * Check if the passwords are matched
     *
     * @param plainText       password in plain text (if salt is used, then this will have salt appended)
     * @param encodedPassword encoded password
     */
    boolean matches(String plainText, String encodedPassword);

}
