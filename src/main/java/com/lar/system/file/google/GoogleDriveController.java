package com.lar.system.file.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.lar.common.vo.AppResult;
import com.lar.system.file.FileData;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/googledrive")
public class GoogleDriveController {
    @Value("${google.client.client-id:}")
    private String clientId;
    @Value("${google.client.client-secret:}")
    private String clientSecret;
    @Value("${google.client.redirect-uri:}")
    private String redirectUri;
    @Value("${google.client.scopes:https://www.googleapis.com/auth/drive}")
    private String scopes;
    @Autowired
    private GoogleDriveServiceBuilder driveServiceBuilder;

    // 回调授权码
    @GetMapping("/redirectUri")
    public AppResult<?> redirectUri(@RequestParam String code, @RequestParam String state) throws IOException {
        // 验证state正确
        // 换取访问令牌
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                "https://oauth2.googleapis.com/token",
                clientId,
                clientSecret,
                code,
                redirectUri)
                .execute();
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        // 把token存储

        return AppResult.success(tokenResponse);
    }


    // 生成授权URL
    @GetMapping("/auth")
    public AppResult<?> startOAuth() throws Exception {

        String userId = "123";
        String authUrl = new GoogleAuthorizationCodeRequestUrl(
                clientSecret,
                redirectUri,
                Collections.singleton(scopes))
                .setState(userId) // 设置当前用户的标识
                .setAccessType("offline")
                .build();
        return AppResult.success(authUrl);
    }

    // 获取googledrive的文件列表内容
    @PostMapping("/list")
    public AppResult<?> list(@RequestBody FileData params) throws GeneralSecurityException, IOException {
        String userId = "123";
        Drive drive = driveServiceBuilder.getDriveService(userId);
        FileList result = drive.files().list()
                .setPageSize(1000)
//                .setQ("trashed = false and mimeType != 'application/vnd.google-apps.folder'")
                .setFields("files(id,name,createdTime,mimeType)")  // modifiedTime,parents
                .execute();
        List<String> collect = result.getFiles().stream()
                .map(File::getName)
                .collect(Collectors.toList());

//        处理分页,加载全部
//        String nextPageToken = result.getNextPageToken();
//        while (nextPageToken != null) {
//            FileList nextPage = drive.files().list()
//                    .setQ("trashed = false and mimeType != 'application/vnd.google-apps.folder'")
//                    .setPageSize(1000)
//                    .setPageToken(nextPageToken)  // 加载后续分页数据
//                    .setFields("files(id, name)")
//                    .execute();
//            result.getFiles().addAll(nextPage.getFiles());
//            nextPageToken = nextPage.getNextPageToken();
//        }

        return AppResult.success(collect);
    }
    // 下载文件
    @PostMapping("/download/{fileId}")
    public void download(@PathVariable("realFileId")  String fileId, HttpServletResponse response) throws IOException, GeneralSecurityException {
        Drive drive = driveServiceBuilder.getDriveService("12");
        String fileName = this.getFileName(drive,fileId);
        ByteArrayOutputStream stream = this.downloadFile(drive,fileId);

    }
    public String getFileName( Drive drive , String fileId) throws IOException {
        return drive.files().get(fileId)
                .execute()
                .getName();
    }
    public ByteArrayOutputStream downloadFile( Drive drive ,String fileId) throws IOException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            drive.files().get(fileId)
                    .setAlt("media")
                    .executeMediaAndDownloadTo(outputStream);
            return outputStream;
        } catch (GoogleJsonResponseException e) {
            throw new RuntimeException("文件下载失败: " + e.getDetails().getMessage());
        }
    }

    // 创建文件夹
    @PostMapping("/createFolder")
    public String createFolder(@RequestParam String userId, @RequestParam String folderName) throws Exception {
        Drive drive = driveServiceBuilder.getDriveService(userId);

        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");

        File folder = drive.files().create(folderMetadata)
                .setFields("id")
                .execute();

        return "Folder created with ID: " + folder.getId();
    }
}
