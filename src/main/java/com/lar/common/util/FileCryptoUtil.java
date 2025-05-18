package com.lar.common.util;


import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

@Component
public class FileCryptoUtil  implements InitializingBean {
    static AES aes = null;
    private static String aesKey;
    @Value("${config.aesKey:1560526548464287746}")
    public void setAesKey(String key) {
        aesKey = key; // 通过非静态方法注入静态变量
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        aes = SecureUtil.aes(aesKey.getBytes());
    }
    // 加密方法
    public static void encryptFile(File file) {
        byte[] fileBytes = FileUtil.readBytes(file);
        byte[] encrypted = aes.encrypt(fileBytes);
        FileUtil.writeBytes(encrypted, file);
    }
    public static byte[] encryptFile(byte[] fileBytes) {
        return aes.encrypt(fileBytes);
    }
    // 解密方法
    public static void decryptFile(File file) {
        try {
            byte[] fileBytes = FileUtil.readBytes(file);
            byte[] decrypted = aes.decrypt(fileBytes);
            FileUtil.writeBytes(decrypted, file);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] decryptFile(byte[] fileBytes) {
        try{
            return aes.decrypt(fileBytes);
        } catch (Exception e) {
            return fileBytes;
        }
    }


}
