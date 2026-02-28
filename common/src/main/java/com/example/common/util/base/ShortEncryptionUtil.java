package com.example.common.util.base;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ShortEncryptionUtil {

    // 加密方法（输出短字符串）
    public static String encrypt(String data, String key) throws Exception {
        // 1. 生成128位AES密钥（自动截取/填充至16字节）
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] paddedKey = new byte[16];
        System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 16));
        SecretKeySpec secretKey = new SecretKeySpec(paddedKey, "AES");

        // 2. 使用AES/ECB/PKCS5Padding加密
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // 3. 使用URL安全的Base64编码（无填充，缩短长度）
        return Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedBytes);
    }

    // 解密方法
    public static String decrypt(String encryptedData, String key) throws Exception {
        // 1. 生成相同密钥
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] paddedKey = new byte[16];
        System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 16));
        SecretKeySpec secretKey = new SecretKeySpec(paddedKey, "AES");

        // 2. 解码Base64 URL安全格式
        byte[] encryptedBytes = Base64.getUrlDecoder().decode(encryptedData);

        // 3. 解密数据
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        String originalText = "5342";
        String secretKey = "1123"; // 密钥（自动处理长度）

        // 加密
        String encrypted = encrypt(originalText, secretKey);
        System.out.println("加密后 (" + encrypted.length() + "字符): " + encrypted);

        // 解密
        String decrypted = decrypt(encrypted, secretKey);
        System.out.println("解密后: " + decrypted);
    }
}