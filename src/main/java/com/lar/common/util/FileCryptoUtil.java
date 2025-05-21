package com.lar.common.util;


import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.noear.snack.core.utils.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class FileCryptoUtil implements InitializingBean {
    static AES aes = null;

    @Value("${config.aesKey:1560526548464287}")
    private String key;


    // 加密方法
    public static void encryptFile(File file) {
        if (aes == null) return;
        byte[] fileBytes = FileUtil.readBytes(file);
        byte[] encrypted = aes.encrypt(fileBytes);
        FileUtil.writeBytes(encrypted, file);
    }

    public static byte[] encryptFile(byte[] fileBytes) {
        if (aes == null) {
            return fileBytes;
        }
        return aes.encrypt(fileBytes);
    }

    // 解密方法
    public static void decryptFile(File file) {
        try {
            if (aes == null) return;
            byte[] fileBytes = FileUtil.readBytes(file);
            byte[] decrypted = aes.decrypt(fileBytes);
            FileUtil.writeBytes(decrypted, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] decryptFile(byte[] fileBytes) {
        try {
            if (aes == null) return fileBytes;
            return aes.decrypt(fileBytes);
        } catch (Exception e) {
            return fileBytes;
        }
    }

//    @Value("${config.aesKey:1560526548464287}")
//    public void setAesKey(String key) {
//        // UTF-8编码后截取前32字节（强制对齐256bits）
//        // 由于加密算法要求，需要保证128/192/256 bits.的密钥才能使用。也就是字符长度在16，24，32的密钥
//        System.out.println("------------------------>>>>>"+key);
//        aesKey = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 32);
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtil.isEmpty(key))return;
        byte[] aesKey = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 32);
        aes = SecureUtil.aes(aesKey);
    }





}
