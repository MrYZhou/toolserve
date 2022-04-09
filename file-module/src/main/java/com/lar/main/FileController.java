package com.lar.main;

import com.lar.minio.MinioServe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    MinioServe minioServe;
    @PostMapping("/upload")
    public void uploadFile(@RequestPart MultipartFile file) throws IOException {
        minioServe.upload(file);

    }

    @PostMapping("/uploadMulti")
    public void uploadFile(@RequestPart MultipartFile[] file){

    }

}
