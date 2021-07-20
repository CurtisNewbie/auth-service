package com.curtisnewbie.service.auth.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

/**
 * PasswordEncoder that uses {@link java.security.MessageDigest}
 *
 * @author yongjie.zhuang
 */
public class MessageDigestPasswordEncoder implements PasswordEncoder {

    private final String algorithm;

    public MessageDigestPasswordEncoder(String algorithm) {
        Objects.requireNonNull(algorithm);
        this.algorithm = algorithm;
    }

    @Override
    public String encodePassword(String password) {
        MessageDigest md = getMessageDigest(algorithm);
        byte[] digested = md.digest(password.getBytes(Charset.forName("UTF-8")));
        return Base64.getEncoder().encodeToString(digested);
    }

    @Override
    public boolean matches(String plainText, String encodedPassword) {
        Objects.requireNonNull(plainText);
        Objects.requireNonNull(encodedPassword);
        return Objects.equals(encodePassword(plainText), encodedPassword);
    }

    private MessageDigest getMessageDigest(String algorithm) {
        try {
            // check if the algorithm is valid
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
