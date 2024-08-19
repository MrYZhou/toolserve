package com.lar.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileTool {
    public static File multipartFileToFile(MultipartFile file) {
        File toFile = null;
        InputStream ins = null;
        try {
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            //获取流文件
            try {
                OutputStream os = new FileOutputStream(toFile);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                ins.close();
            } catch (Exception ex) {
            }
            ins.close();
        } catch (Exception ex) {
        }
        return toFile;
    }

}
