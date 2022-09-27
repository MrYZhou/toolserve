package com.lar.main.file;

import common.base.AppResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService {

    AppResult<Object> upload(MultipartFile multipartFile, HttpServletRequest httpServletRequest);
}
