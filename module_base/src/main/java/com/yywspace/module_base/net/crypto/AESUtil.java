package com.yywspace.module_base.net.crypto;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESUtil {
    public static String SECRET_KEY;

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
        byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));// 初始化为加密模式的密码器
        byte[] result = cipher.doFinal(byteContent);// 加密
        return Base64.encodeBase64String(result);//通过Base64转码返回

    }

    /**
     * AES 解密操作
     *
     * @param content 被加密内容
     * @param key     key
     * @return 内容
     */
    public static String decrypt(String content, String key) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
        //执行操作
        byte[] result = cipher.doFinal(Base64.decodeBase64(content));
        return new String(result, StandardCharsets.UTF_8);
    }

    private static SecretKeySpec getSecretKey(String key) {
        byte[] decodedKey = Base64.decodeBase64(key.getBytes());
        return new SecretKeySpec(decodedKey, KEY_ALGORITHM);
    }

    /**
     * 生成加密秘钥
     *
     * @return key
     */
    public static String generateSecretKey(String seed) throws NoSuchAlgorithmException {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed.getBytes(StandardCharsets.UTF_8));
        //AES 要求密钥长度为 128
        kg.init(128, secureRandom);
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        return Base64.encodeBase64String(secretKeySpec.getEncoded());
    }

    public static void main(String[] args) throws Exception {
        String content = "hello,您好";
        String key = generateSecretKey("qqq");
        System.out.println("content:" + content);
        System.out.println("key:" + key);
        String s1 = AESUtil.encrypt(content, key);
        System.out.println("s1:" + s1);
        System.out.println("s2:" + AESUtil.decrypt(s1, key));

    }

}
