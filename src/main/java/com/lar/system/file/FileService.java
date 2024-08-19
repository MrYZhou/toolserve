package com.lar.system.file;


import com.lar.common.vo.AppResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;



public interface FileService {

    AppResult<Object> upload(MultipartFile multipartFile, HttpServletRequest httpServletRequest);
}
