package com.lar.main.example.file;

import common.base.AppResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public AppResult<Object> upload(MultipartFile multipartFile, FileData fileData) {
        return null;
    }
}
