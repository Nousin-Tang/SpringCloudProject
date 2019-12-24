package com.nousin.springcloud.common.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 密码加密与匹配工具
 *
 * @author Nousin
 * @since 2019/12/11
 */
public class PasswordUtil {

    public static final int HASH_ITERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    public static String encode(CharSequence rawPassword) {
        String plain = StringEscapeUtils.unescapeHtml4(rawPassword.toString());
        byte[] salt = generateSalt();
        byte[] hashPassword = digest(plain.getBytes(), salt);
        return new String(Hex.encodeHex(salt)) + new String(Hex.encodeHex(hashPassword));
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (StringUtils.equals(rawPassword, encodedPassword))
            return true;
        try {
            String plain = StringEscapeUtils.unescapeHtml4(rawPassword.toString());
            byte[] salt = Hex.decodeHex(encodedPassword.substring(0, 16).toCharArray());
            byte[] hashPassword = digest(plain.getBytes(), salt);
            return encodedPassword.equals(new String(Hex.encodeHex(salt)) + new String(Hex.encodeHex(hashPassword)));
        } catch (DecoderException e) {
            return false;
        }
    }

    /**
     * 生成随机的Byte[]作为salt
     */
    private static byte[] generateSalt() {
        byte[] bytes = new byte[SALT_SIZE];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    private static byte[] digest(byte[] input, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            if (salt != null) {
                digest.update(salt);
            }
            byte[] result = digest.digest(input);
            for (int i = 1; i < HASH_ITERATIONS; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
