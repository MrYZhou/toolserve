package com.lar.system.file;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${upload.temp.dir}")
    private String tempDir;

    @Value("${upload.final.dir}")
    private String finalDir;

    @PostMapping("/chunk")
    public ResponseEntity<Map<String, Object>> uploadChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("chunkNumber") int chunkNumber,
            @RequestParam("totalChunks") int totalChunks,
            @RequestParam("identifier") String identifier,
            @RequestParam("filename") String filename) throws IOException {

        // 创建临时目录
        Path chunkDir = Paths.get(tempDir, identifier);
        if (!Files.exists(chunkDir)) {
            Files.createDirectories(chunkDir);
        }

        // 保存分片文件
        Path chunkPath = chunkDir.resolve(String.valueOf(chunkNumber));
        file.transferTo(chunkPath.toFile());

        Map<String, Object> response = new HashMap<>();
        response.put("chunkNumber", chunkNumber);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/merge")
    public ResponseEntity<Map<String, Object>> mergeChunks(
            @RequestParam("identifier") String identifier,
            @RequestParam("filename") String filename,
            @RequestParam("totalChunks") int totalChunks) throws IOException {

        // 检查所有分片是否已上传
        Path chunkDir = Paths.get(tempDir, identifier);
        for (int i = 1; i <= totalChunks; i++) {
            Path chunkPath = chunkDir.resolve(String.valueOf(i));
            if (!Files.exists(chunkPath)) {
                throw new RuntimeException("Missing chunk: " + i);
            }
        }

        // 创建最终目录
        Path finalDirPath = Paths.get(finalDir);
        if (!Files.exists(finalDirPath)) {
            Files.createDirectories(finalDirPath);
        }

        // 合并文件
        Path finalPath = finalDirPath.resolve(filename);
        try (RandomAccessFile finalFile = new RandomAccessFile(finalPath.toFile(), "rw")) {
            for (int i = 1; i <= totalChunks; i++) {
                Path chunkPath = chunkDir.resolve(String.valueOf(i));
                byte[] chunkData = Files.readAllBytes(chunkPath);
                finalFile.write(chunkData);
            }
        }

        // 删除临时分片
        Files.walk(chunkDir)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("filename", filename);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkChunk(
            @RequestParam("identifier") String identifier,
            @RequestParam("chunkNumber") int chunkNumber) {

        Path chunkPath = Paths.get(tempDir, identifier, String.valueOf(chunkNumber));
        boolean exists = Files.exists(chunkPath);

        Map<String, Object> response = new HashMap<>();
        response.put("chunkNumber", chunkNumber);
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}
