package com.curtisnewbie.service.auth.util;

/**
 * Factory of Password Encoder
 *
 * @author yongjie.zhuang
 */
public final class PasswordEncoderFactory {

    private PasswordEncoderFactory() {

    }

    /**
     * Get password encoder
     *
     * @param algorithm algorithm, e.g., "SHA-256"
     */
    public static PasswordEncoder getPasswordEncoder(String algorithm) {
        return new MessageDigestPasswordEncoder(algorithm);
    }

}
