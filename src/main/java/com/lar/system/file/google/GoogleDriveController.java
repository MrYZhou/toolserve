package com.lar.system.file.google;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.lar.common.util.JsonUtil;
import com.lar.common.util.RedisUtil;
import com.lar.common.vo.AppResult;
import com.lar.system.file.model.DocFileData;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/googledrive")
@Slf4j
public class GoogleDriveController {
    @Value("${google.client.client-id:663018976908-ttr6qsb6k7t8s2742no14iob1eluv7pu.apps.googleusercontent.com}")
    private String clientId;
    @Value("${google.client.client-secret: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}")
    private String clientSecret;
    @Value("${google.client.redirect-uri:http://127.0.0.1:38000/api/file/googledrive/redirectUri}")
    private String redirectUri;
    @Value("${google.client.scopes:https://www.googleapis.com/auth/drive.file}")
    private String scopes;
    @Value("${google.client.needProxy:true}")
    private Boolean needProxy;

    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private GoogleDriveServiceBuilder driveServiceBuilder;

    // 授权码检测
    @GetMapping("/checkAuth")
    public AppResult<?> checkAuth() throws IOException {
        // 获取当前用户的token信息，如果不存在进行授权
        GoogleTokenResponse token = driveServiceBuilder.getToken("userid");
        Map<String, Object> map = new HashMap<>();
        if (token == null) {
            map.put("authUrl", authUrl());
        } else {
            map.put("access_token", token.getAccessToken());
        }
        return AppResult.success(map);
    }

    // 回调授权码
    @GetMapping("/redirectUri")
    public AppResult<?> redirectUri(@RequestParam("code") String code, @RequestParam("state") String state) throws IOException {
        GoogleTokenResponse tokenResponse = new GoogleTokenResponse();
        // 换取访问令牌
        try {
            HashMap map = new HashMap();
            map.put("code", code);
            map.put("redirect_uri", redirectUri);
            map.put("client_id", clientId);
            map.put("client_secret", clientSecret);
            map.put("grant_type", "authorization_code");
            HttpRequest httpRequest = HttpRequest.post("https://oauth2.googleapis.com/token")
                    .setSSLProtocol("TLSv1.2")  // 指定 TLS 版本
                    .form(map)
                    .disableCookie();// 可选：避免会话干扰
            if (needProxy) {
                httpRequest.setHttpProxy("127.0.0.1", 7890); // 国内开发，使用代理，否则会连接超时。
            }
            String body = httpRequest.execute().body();
            Map data = JsonUtil.toBean(body, Map.class);
            tokenResponse.setAccessToken(data.get("access_token").toString());
            tokenResponse.setRefreshToken(data.get("refresh_token").toString());

            // 计算过期时间
            int expiresInSeconds = (int) data.get("expires_in");
            driveServiceBuilder.setExpireTime(tokenResponse, expiresInSeconds, "expirationTimeStr");
//            redisUtil.insert(state + "googledrive", JsonUtil.getObjectToString(tokenResponse),36000);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return AppResult.fail(e.getMessage());
        }
        return AppResult.success("Authorization Successful");
    }

    /**
     * V2版本的授权
     **/
    @GetMapping("/auth2")
    public AppResult<?> auth2() throws Exception {
        String userId = "123";
        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth";
        authUrl += "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&access_type=offline&state=" + userId
                + "&response_type=code&scope=" + scopes;
        return AppResult.success(authUrl);
    }

    // 生成授权URL
    @GetMapping("/auth")
    public AppResult<?> startOAuth() throws Exception {
        return AppResult.success(this.authUrl());
    }

    public String authUrl() {
        String authUrl = new GoogleAuthorizationCodeRequestUrl(
                clientId,
                redirectUri,
                Collections.singleton(scopes)) // 配置访问范围
                .setState("userid") // 设置当前用户的标识
                .setAccessType("offline") // 服务器返回refresh_token
                .setApprovalPrompt("force") // 重新发起授权，避免refresh_token在授权过不在生成的情况
                .build();
        log.info("auth url: {}", authUrl);
        return authUrl;
    }

    // 获取googledrive的文件列表内容
    @PostMapping("/list")
    public AppResult<?> list(@RequestBody DocFileData params) throws GeneralSecurityException, IOException {
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

    // 下载文件原生
    @GetMapping("/download/{fileId}")
    public void download(@PathVariable("fileId") String fileId, HttpServletResponse response) throws IOException, GeneralSecurityException {
        GoogleTokenResponse token = driveServiceBuilder.getToken("userid");
        String downloadUrl = "https://drive.google.com/uc?id=" + fileId + "&export=download";
        HttpRequest httpRequest = HttpRequest.get(downloadUrl)
                .header(Header.AUTHORIZATION, token.getAccessToken());
        if (needProxy) {
            httpRequest.setHttpProxy("127.0.0.1", 7890); // 国内开发，使用代理，否则会连接超时。
        }
        HttpResponse execute = httpRequest.execute();
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/octet-stream");
            IoUtil.copy(execute.bodyStream(), outputStream, IoUtil.DEFAULT_BUFFER_SIZE);
        }
    }

    // 下载文件原生
    @GetMapping("/download2/{fileId}")
    public void download2(@PathVariable("fileId") String fileId, HttpServletResponse response) throws IOException, GeneralSecurityException {
        Drive drive = driveServiceBuilder.getDriveService("userid");
        String fileName = this.getFileName(drive, fileId);
        ByteArrayOutputStream fileStream = this.downloadFile(drive, fileId);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(fileStream.size());

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(fileStream.toByteArray());
            outputStream.flush();
        }

    }

    public String getFileName(Drive drive, String fileId) throws IOException {
        return drive.files().get(fileId)
                .execute()
                .getName();
    }

    public ByteArrayOutputStream downloadFile(Drive drive, String fileId) throws IOException {
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
