package com.lar.system.file.sharepoint;


import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.lar.common.util.JsonUtil;
import com.lar.common.util.RedisUtil;
import com.lar.common.vo.AppResult;
import com.lar.system.file.model.DocFileData;
import com.lar.system.file.model.OneDriveModel;
import com.lar.system.file.onedrive.OneDriveServiceBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.noear.wood.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sharepoint")
@Slf4j
public class SharePointController {
    @Autowired
    OneDriveServiceBuilder driveServiceBuilder;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${sharepoint.client.client-id:d6029edc-9b6b-45b9-bac4-bef3370a6510}")
    private String clientId;
    @Value("${sharepoint.client.client-secret:BdD8Q~6iMK676URfgVz6OUUH3NiLfFemNXdufdez}")
    private String clientSecret;
    @Value("${sharepoint.client.redirect-uri:http://localhost:38000/api/file/sharepoint/redirectUri}")
    private String redirectUri;
    @Value("${sharepoint.client.scopes:files.readwrite offline_access}")
    private String scopes;
    @Autowired
    private RestTemplate restTemplate;


    // sharepoint授权码检测
    @GetMapping("/checkAuth")
    public AppResult<?> checkAuth() throws IOException {
        // 获取当前用户的token信息，如果不存在进行授权
        OneDriveModel token = driveServiceBuilder.getToken("userid");
        Map<String, Object> map = new HashMap<>();
        if (token == null) {
            map.put("authUrl", authUrl());
        } else {
            map.put("access_token", token.getAccessToken());
        }
        return AppResult.success(map);
    }

    public String authUrl() {
        String userId = "userid";
        String authUrl = "https://login.live.com/oauth20_authorize.srf";
        authUrl += "?client_id=" + clientId + "&access_type=offline"
                + "&response_type=code&scope=" + scopes + "&redirect_uri=" + redirectUri + "&state=" + userId;
        log.info("auth url: {}", authUrl);
        return authUrl;
    }
    @GetMapping("/redirectUri")
    public AppResult<?> redirectUri2(@RequestParam("state") String state, @RequestParam("code") String code) {
        OneDriveModel tokenResponse = new OneDriveModel();
        String tokenUrl = "https://login.live.com/oauth20_token.srf";
        return AppResult.success(code);
    }
    // 获取授权码
    @GetMapping("/redirectUri2")
    public AppResult<?> redirectUri(@RequestParam("state") String state, @RequestParam("code") String code) {
        OneDriveModel tokenResponse = new OneDriveModel();
        String tokenUrl = "https://login.live.com/oauth20_token.srf";

        // 换取访问令牌
        try {
            HashMap map = new HashMap();
            map.put("code", code);
            map.put("redirect_uri", redirectUri);
            map.put("client_id", clientId);
            map.put("client_secret", clientSecret);
            map.put("grant_type", "authorization_code");
            HttpRequest httpRequest = HttpRequest.post(tokenUrl)
                    .setSSLProtocol("TLSv1.2")  // 指定 TLS 版本
                    .form(map)
                    .disableCookie();// 可选：避免会话干扰


            String body = httpRequest.execute().body();
            Map data = JsonUtil.toBean(body, Map.class);
            tokenResponse.setAccessToken(data.get("access_token").toString());
            tokenResponse.setRefreshToken(data.get("refresh_token").toString());
            // 计算过期时间
            int expiresInSeconds = (int) data.get("expires_in");
            driveServiceBuilder.setExpireTime(tokenResponse, expiresInSeconds);
//            redisUtil.insert(state + "sharepoint", JsonUtil.getObjectToString(tokenResponse), 36000);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return AppResult.fail(e.getMessage());
        }
        return AppResult.success("Authorization Successful");

    }

    // 上传文件，在文档中心里面新建一个文档使用
    @PostMapping("/upload")
    public String upload(@RequestBody DocFileData params) throws IOException {

        String uploadUrl = "https://graph.microsoft.com/v1.0/me/drive/root:/uploadfile/";
        File file = new File("D:\\Users\\JNPF\\Desktop\\project\\toolserve\\src\\main\\resources\\config\\1.docx");
        uploadUrl = uploadUrl + "1.docx:/content";

        byte[] bytes = FileUtils.readFileToByteArray(file);
        String body = HttpRequest.put(uploadUrl)
                .header(Header.AUTHORIZATION, "")
                .header(Header.CONTENT_TYPE, "application/octet-stream")
                .body(bytes)//表单内容
                .execute().body();
        return body;
    }


    // 在线编辑使用
//    @PostMapping("/newPage")
//    public AppResult<?> newPage(@RequestBody DocFileData params) throws IOException {
//        String fileName = params.getFileName();
//        // 无后缀加后缀
//        if (!params.getFileName().endsWith(".docx") && !params.getFileName().endsWith(".xlsx")) {
//            String suffix = "word".equals(params.getFileType()) ? ".docx" : ".xlsx";
//            fileName = params.getFileName() + suffix;
//        }
//
//        String uploadUrl = "https://graph.microsoft.com/v1.0/me/drive/root:/uploadfile/" + fileName + ":/content";
//        OneDriveModel model = driveServiceBuilder.getToken("userid");
//        if (model == null) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("authUrl", authUrl());
//            return AppResult.success(map);
//        }
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("name", fileName);
//        map.put("file", new HashMap<>());
//
//        String filePath = fileApi.getPath(FileTypeConstant.DOCUMENT);
//        String fileId = params.getFilePath();
//        if(filePath.startsWith("/")) {
//            filePath = filePath.substring(1);
//        }
//        if(filePath.endsWith("/")) {
//            filePath = filePath.substring(0, filePath.length()-1);
//        }
//        byte[] bytes = null;
//        try {
//            bytes = fileUploadApi.getByte(fileId, filePath);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        String body = HttpRequest.put(uploadUrl)
//                .header(Header.AUTHORIZATION, model.getAccessToken())
//                .header(Header.CONTENT_TYPE, "application/octet-stream")
//                .body(bytes)//表单内容
//                .execute().body();
//        Map result = JsonUtil.toBean(body, Map.class);
//        return AppResult.success(result);
//    }


    // 下载文件，sharepoint下载文件
//    @PostMapping("/download/{itemId}")
//    public void download(@PathVariable("itemId") String itemId, HttpServletResponse response) throws IOException {
//        String tempDownloadUrl = "https://my.microsoftpersonalcontent.com/personal/9417dd4c80831e92/_layouts/15/download.aspx?UniqueId=c124051f-8667-4136-ade8-ddde07186dc4&Translate=false&tempauth=v1e.eyJzaXRlaWQiOiIzYmE0OWQyOC00ZDcwLTRjOWEtYWMxOC1iOGRhZTA4NTM0ZDYiLCJhcHBfZGlzcGxheW5hbWUiOiLmtYvor5VhcHAyIiwiYXBwaWQiOiJkNjAyOWVkYy05YjZiLTQ1YjktYmFjNC1iZWYzMzcwYTY1MTAiLCJhdWQiOiIwMDAwMDAwMy0wMDAwLTBmZjEtY2UwMC0wMDAwMDAwMDAwMDAvbXkubWljcm9zb2Z0cGVyc29uYWxjb250ZW50LmNvbUA5MTg4MDQwZC02YzY3LTRjNWItYjExMi0zNmEzMDRiNjZkYWQiLCJleHAiOiIxNzQ1MDUyNTkzIn0.nOADkpjtyy25z5JG0nbBLkDKhNi0y_tLDfM4netwKCePRvTE9afFigwaFYZMCxT59aTckZ9NUQ6bsCbNr17Jyk1k86ahaLpQErAsViqpLiIfNobBidMC1ghoOXB1MYBo8MVz9vtfD45lmZQV6WqMLt4ooGYsNUaPCSG3hgN9goc_232geUib67hM3mowo4ZNoVYPN2D08NXUnPflF2CsmJsJ-fqpB5UgucF2KAT092Uq13h2QjoqkzGyKl9YS3WEpBUTM6kLVBaXcE6Yr0hzZ1h-m_kMaiHGCOsyQr3Ml76GrdqHrOk0tG5dIOuRIXZMuC8pkb118XMsmRRvcok4_vnwJsvqDLJ9r5O5dkaj3Pe8ZMWdq6paXiiON4bdVb6V47b4VwYzA4KBHxI48k5auEaYU5vUzjbdjpEoOyfHPvHFc07e2nvxId3WebDc3A-I.FREvrEUFQUjA4lHP90G4Dk8TpohgtrnHPxeEV7XWlFs&ApiVersion=2.0";
//
//        HttpUtil.download(tempDownloadUrl, response.getOutputStream(), true);
//    }
    // 文档中心的文件下载使用
    @PostMapping("/saveDocument")
    public AppResult<?> saveDocument(@RequestBody DocFileData params) throws IOException {

        String downloadUrl = "https://graph.microsoft.com/v1.0/me/drive/items/" + params.getItemId() + "/content";
        OneDriveModel model = driveServiceBuilder.getToken("userid");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", model.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<byte[]> result = restTemplate.exchange(
                downloadUrl,
                HttpMethod.GET,
                entity,
                byte[].class
        );
        String fileName = params.getFileName();
        // 无后缀加后缀
        if (!params.getFileName().endsWith(".docx") && !params.getFileName().endsWith(".xlsx")) {
            String suffix = "word".equals(params.getFileType()) ? ".docx" : ".xlsx";
            fileName = params.getFileName() + suffix;
        }
        byte[] bytes = result.getBody();
//        MultipartFile multipartFile = new MockMultipartFile("file", fileName, "application/octet-stream", bytes);
//        DocumentUploader documentUploader = new DocumentUploader(multipartFile, params.getParentId());
//        documentUploader.setOperateType(params.getOperateType());
//        documentUploader.setId(params.getLocalFileId()!=null? params.getLocalFileId():RandomUtil.uuId());
//        documentService.uploaderVO(documentUploader);
//        return AppResult.success(I18nUtil.getMessageStr("VS007", null));
        return AppResult.success();
    }

    // 下载文件，通过id从sharepoint下载文件内容回来
    @GetMapping("/download/{itemId}")
    public ResponseEntity<Resource> download(@PathVariable("itemId") String itemId, HttpServletResponse response) throws IOException {
        String downloadUrl = "https://graph.microsoft.com/v1.0/me/drive/items/" + itemId + "/content";
        OneDriveModel model = driveServiceBuilder.getToken("userid");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", model.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<byte[]> result = restTemplate.exchange(
                downloadUrl,
                HttpMethod.GET,
                entity,
                byte[].class
        );
        byte[] fileContent = result.getBody();

        ByteArrayResource resource = new ByteArrayResource(fileContent);
        HttpHeaders headers2 = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=downloaded_file");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        return ResponseEntity.ok()
                .headers(headers2)
                .contentLength(fileContent.length)
                .body(resource);
    }

    // 获取sharepoint的文件列表内容
    @PostMapping("/list")
    public AppResult<?> list(@RequestBody DocFileData params) throws IOException {
        // 如果获取子集id,默认是根目录的内容
        String itemId = params.getItemId() != null ? params.getItemId() : "root";

        String list = "https://graph.microsoft.com/v1.0/me/drive/items/" + itemId + "/children";
        // 如果有关键字查询
        if (StringUtils.isNotEmpty(params.getKeyword())) {
            list = "https://graph.microsoft.com/v1.0/me/drive/root/search(q='" + params.getKeyword() + "')";
        }

        OneDriveModel model = driveServiceBuilder.getToken("userid");
        String token = model.getAccessToken();
        String body = HttpRequest.get(list)
                .header(Header.AUTHORIZATION, token)
                .execute().body();
        Map result = JsonUtil.toBean(body, Map.class);
        return AppResult.success(result);
    }

    /**
     * 撤销授权
     *
     * @throws IOException
     */
//    @GetMapping("/revoke")
//    public void revoke() throws IOException {
//        String userId = userProvider.get().getUserId();
//        // 获取当前的token
//        Object data = redisUtil.getString(userId + "sharepoint");
//        if (Objects.isNull(data)) {
////            return AppResult.success("");
//        }
//        OneDriveModel model = JsonUtil.toBean(data, OneDriveModel.class);
//        // 删除Redis中的令牌
//        redisUtil.remove(userId + "sharepoint");
//    }

}
