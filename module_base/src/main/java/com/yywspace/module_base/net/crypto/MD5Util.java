package com.yywspace.module_base.net.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5加密工具类
 * @author pibigstar
 *
 */
public class MD5Util {
    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";

    public static String encrypt(String dataStr) {
        try {
            dataStr = dataStr + slat;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = m.digest();
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                result.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}