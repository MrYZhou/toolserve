package com.lar.file;

import com.lar.common.base.AppResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public AppResult<Object> upload(MultipartFile multipartFile, HttpServletRequest httpServletRequest) {

        String user = httpServletRequest.getParameter("user");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", "http://www.baidu.com");
        map.put("name", "file1");
        return AppResult.success(map);
    }
}
