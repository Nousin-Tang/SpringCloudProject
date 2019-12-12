package com.nousin.springcloud.auth.framework.security.util;

/**
 * TODO
 *
 * @author tangwc
 * @since 2019/12/11
 */
public class PasswordUtil {

    public static final int HASH_ITERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    public static String encode(CharSequence rawPassword) {
        String plain = Encodes.unescapeHtml(rawPassword.toString());
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_ITERATIONS);
        return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        if(rawPassword.equals(encodedPassword)){
            return true;
        }
        String plain = Encodes.unescapeHtml(rawPassword.toString());
        byte[] salt = Encodes.decodeHex(encodedPassword.substring(0, 16));
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_ITERATIONS);
        return encodedPassword.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
    }
}
