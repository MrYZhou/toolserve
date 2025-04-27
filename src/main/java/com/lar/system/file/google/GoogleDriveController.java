package com.lar.system.file.google;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.lar.common.vo.AppResult;
import com.lar.system.file.FileData;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import java.util.HashMap;
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
    @GetMapping("/redirectUri2")
    public String redirectUri(@RequestParam String code, @RequestParam String state) throws IOException {
        return code;
    }
    // 回调授权码
    @GetMapping("/redirectUri")
    public AppResult<?> redirectUri2(@RequestParam String code, @RequestParam String state) throws IOException {
        GoogleTokenResponse tokenResponse = null;
        // 验证state正确
        // 换取访问令牌

        try{
            HashMap map = new HashMap();
            map.put("code", code);
            map.put("redirect_uri", redirectUri);
            map.put("client_id", clientId);
            map.put("client_secret", clientSecret);
            map.put("grant_type", "authorization_code");
            String body = HttpRequest.post("https://oauth2.googleapis.com/token")
                    .setHttpProxy("127.0.0.1", 7890) // 国内开发，使用代理，否则会连接超时。
                    .setSSLProtocol("TLSv1.2")  // 指定 TLS 版本
                    .form(map)
                    .disableCookie()  // 可选：避免会话干扰
                    .execute().body();

            System.out.println(body);

//            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
//                    new NetHttpTransport(),
//                    GsonFactory.getDefaultInstance(),
//                    "https://accounts.google.com/o/oauth2/v2/auth",
//                    clientId,
//                    clientSecret,
//                    redirectUri)
//                    .setScopes(Collections.singleton(scopes))
//                    .setCode(code)
//                    .setGrantType("authorization_code")
//                    .execute();
//            System.out.println(tokenResponse);
//            String accessToken = tokenResponse.getAccessToken();
//            String refreshToken = tokenResponse.getRefreshToken();
        }  catch (Exception e) {
            System.out.println(e.getMessage());
            AppResult.fail(e.getMessage());
        }
        // 把token存储

        return AppResult.success(tokenResponse);
    }
    /** V2版本的授权 **/
    @GetMapping("/auth2")
    public AppResult<?> auth2() throws Exception {
        String userId="123";
       String authUrl = "https://accounts.google.com/o/oauth2/v2/auth";
       authUrl+="?client_id="+clientId+"&redirect_uri="+redirectUri+"&access_type=offline&state="+userId
               +"&response_type=code&scope="+scopes;
        return AppResult.success(authUrl);
    }

    // 生成授权URL
    @GetMapping("/auth")
    public AppResult<?> startOAuth() throws Exception {

        String userId = "123";
        String authUrl = new GoogleAuthorizationCodeRequestUrl(
                clientId,
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
        ByteArrayOutputStream fileStream = this.downloadFile(drive,fileId);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(fileStream.size());

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(fileStream.toByteArray());
            outputStream.flush();
        }

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
