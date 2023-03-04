package com.lar.file;

import com.lar.common.base.AppResult;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/file")
@AllArgsConstructor
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AppResult<Object> upload(@RequestPart("file") MultipartFile multipartFile, HttpServletRequest httpServletRequest) {

        try {

            return fileService.upload(multipartFile, httpServletRequest);
        } catch (Exception e) {
            return AppResult.fail("上传失败");
        }
    }

    @GetMapping("/downloadFile")
    public String downloadFile(String fileVersionId) {
        return "";
    }


    @RequestMapping(value = "/preview",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] preview(String fileVersionId) throws IOException {

        File file = new File("C:\\Users\\lg\\Desktop\\vue\\xlj\\static\\1.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }



}
