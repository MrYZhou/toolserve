package com.lar.file;

import com.lar.vo.AppResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;



public interface FileService {

    AppResult<Object> upload(MultipartFile multipartFile, HttpServletRequest httpServletRequest);
}
