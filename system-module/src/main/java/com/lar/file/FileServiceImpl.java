package com.lar.file;

import com.lar.vo.AppResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public AppResult<Object> upload(MultipartFile multipartFile, HttpServletRequest httpServletRequest) {

        @SuppressWarnings("unused")
        String user = httpServletRequest.getParameter("user");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", "http://www.baidu.com");
        map.put("name", "file1");
        return AppResult.success(map);
    }
}
