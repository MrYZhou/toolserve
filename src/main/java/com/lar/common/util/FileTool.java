package com.lar.common.util;

import lombok.Cleanup;
import org.apache.catalina.core.ApplicationPart;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    /**
     * File转MultipartFile
     *
     * @param file
     * @return
     */
    public static MultipartFile createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            @Cleanup FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = new MyStandardMultipartFile(new ApplicationPart(item, null), file.getName());
        return multipartFile;
    }

}
