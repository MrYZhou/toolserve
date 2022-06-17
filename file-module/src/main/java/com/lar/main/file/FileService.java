package com.lar.main.file;

import common.base.AppResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    AppResult<Object> upload(MultipartFile multipartFile, FileData fileData);
}
