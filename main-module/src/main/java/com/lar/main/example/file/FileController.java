package com.lar.main.example.file;

import common.base.AppResult;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/file")
@AllArgsConstructor
public class FileController {
    @Autowired
    private FileService  fileService;
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AppResult<Object> upload(@RequestPart("multipartFile") MultipartFile multipartFile, @RequestBody FileData fileData) {
        try {
            return fileService.upload(multipartFile,fileData);
        }catch(Exception e){
            return AppResult.fail("上传失败");
        }
    }
}
