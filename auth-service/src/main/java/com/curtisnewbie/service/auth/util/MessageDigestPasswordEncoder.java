package com.curtisnewbie.service.auth.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * PasswordEncoder that uses {@link java.security.MessageDigest}
 *
 * @author yongjie.zhuang
 */
public class MessageDigestPasswordEncoder implements PasswordEncoder {

    private static final char[] HEX = "0123456789abcdef".toCharArray();
    private final String algorithm;

    public MessageDigestPasswordEncoder(String algorithm) {
        Objects.requireNonNull(algorithm);
        this.algorithm = algorithm;
    }

    @Override
    public String encodePassword(String password) {
        MessageDigest md = getMessageDigest(algorithm);
        byte[] digested = md.digest(password.getBytes(Charset.forName("UTF-8")));
        return encodeToHex(digested);
    }

    @Override
    public boolean matches(String plainText, String encodedPassword) {
        Objects.requireNonNull(plainText);
        Objects.requireNonNull(encodedPassword);

        // check if the password was encoded by spring's encoder
        String springSalt = extractSpringGeneratedSalt(encodedPassword);
        String encoded = springSalt + encodePassword(plainText + springSalt);
        return Objects.equals(encoded, encodedPassword);
    }

    private MessageDigest getMessageDigest(String algorithm) {
        try {
            // check if the algorithm is valid
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Spring's PasswordEncoder generates salt, here, we do the same thing for compatibility
     */
    private String extractSpringGeneratedSalt(String encoded) {
        int start = encoded.indexOf("{");
        if (start != 0) {
            return "";
        }
        int end = encoded.indexOf("}", start);
        if (end < 0) {
            return "";
        }
        return encoded.substring(start, end + 1);
    }

    /**
     * Adapted from Spring for backward compatibility, spring by default encodes password into Hex rather than Base64
     */
    public static String encodeToHex(byte[] bytes) {
        final int nBytes = bytes.length;
        char[] result = new char[2 * nBytes];
        int j = 0;
        for (byte aByte : bytes) {
            // Char for top 4 bits
            result[j++] = HEX[(0xF0 & aByte) >>> 4];
            // Bottom 4
            result[j++] = HEX[(0x0F & aByte)];
        }
        return String.valueOf(result);
    }
}
