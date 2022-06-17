package com.lar.main.example.file;

import common.base.AppResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    AppResult<Object> upload(MultipartFile multipartFile, FileData fileData);
}
