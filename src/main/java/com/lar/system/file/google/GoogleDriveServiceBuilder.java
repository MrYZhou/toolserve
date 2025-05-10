package com.lar.system.file.google;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.lar.common.util.JsonUtil;
import com.lar.common.util.RedisUtil;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class GoogleDriveServiceBuilder {
    @Value("${google.client.client-id:663018976908-ttr6qsb6k7t8s2742no14iob1eluv7pu.apps.googleusercontent.com}")
    private String clientId;
    @Value("${google.client.client-secret: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}")
    private String clientSecret;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${google.client.needProxy:true}")
    private Boolean needProxy;

    public GoogleTokenResponse getToken(String userId) throws IOException {
//        Object data = redisUtil.getString("userid" + "googledrive");
        Object data =null;
        if (Objects.isNull(data)) {
            return null;
        }
        Map map = JSONUtil.toBean((String) data, Map.class);
        if (map.get("refresh_token") == null) return null;
        GoogleTokenResponse token = new GoogleTokenResponse();
        token.setAccessToken((String) map.get("access_token"));
        token.setRefreshToken((String) map.get("refresh_token"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        // 先判断刷新令牌有没有过期
        if (!Objects.isNull(map.get("refreshTokenExpiresIn"))) {
            String refreshTokenExpiresIn = (String) map.get("refreshTokenExpiresIn");
            LocalDateTime targetRefreshTime = LocalDateTime.parse(refreshTokenExpiresIn, formatter);
            Duration remainingRefresh = Duration.between(currentTime, targetRefreshTime);
            long remainingMinutesRefresh = remainingRefresh.toMinutes();
            if (remainingMinutesRefresh < 30) {
//                redisUtil.remove("userid" + "googledrive");
                return null;
            }
        }

        // 自动刷新令牌
        String expirationTimeStr = (String) map.get("expirationTimeStr");
        LocalDateTime targetTime = LocalDateTime.parse(expirationTimeStr, formatter);
        Duration remaining = Duration.between(currentTime, targetTime);
        long remainingMinutes = remaining.toMinutes();
        if (remainingMinutes < 30) {
            token = refreshToken(userId, token.getRefreshToken());
        }
        return token;
    }

    public Drive getDriveService(String userId) throws IOException, GeneralSecurityException {
        GoogleTokenResponse token = getToken(userId);
        // 创建带代理配置的 HttpClient
        HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                .setProxy(new HttpHost("127.0.0.1", 7890));

        // 构建自定义的 HttpTransport
        HttpTransport transport = new ApacheHttpTransport(clientBuilder.build());

        // 构建凭证
        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setJsonFactory(GsonFactory.getDefaultInstance())
                .setTransport(transport)
                .build()
                .setAccessToken(token.getAccessToken());

        return new Drive.Builder(
                transport,
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("SpringDriveApp")
                .build();
    }

    private GoogleTokenResponse refreshToken(String userId, String refreshToken) throws IOException {
        HashMap map = new HashMap();
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("grant_type", "refresh_token");
        map.put("refresh_token", refreshToken);
        HttpRequest httpRequest = HttpRequest.post("https://oauth2.googleapis.com/token")
                .setSSLProtocol("TLSv1.2")  // 指定 TLS 版本
                .form(map)
                .disableCookie();// 可选：避免会话干扰
        if (needProxy) {
            httpRequest.setHttpProxy("127.0.0.1", 7890); // 国内开发，使用代理，否则会连接超时。
        }
        String body = httpRequest.execute().body();
        Map data = JsonUtil.toBean(body, Map.class);
        GoogleTokenResponse tokenResponse = new GoogleTokenResponse();
        tokenResponse.setAccessToken(data.get("access_token").toString());
        tokenResponse.setRefreshToken(refreshToken);
        // 判断刷新令牌的失效时间，如果刷新令牌也失效了，则需要重新授权
        int refreshTokenExpiresIn = (int) data.get("refresh_token_expires_in");
        this.setExpireTime(tokenResponse, refreshTokenExpiresIn, "refreshTokenExpiresIn");
        // 计算过期时间
        int expiresInSeconds = (int) data.get("expires_in");
        this.setExpireTime(tokenResponse, expiresInSeconds, "expirationTimeStr");
//        redisUtil.insert(userId + "googledrive", JsonUtil.getObjectToString(tokenResponse),36000);

        return tokenResponse;
    }

    public GoogleTokenResponse setExpireTime(GoogleTokenResponse tokenResponse, long expiresInSeconds, String key) {
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 计算过期时间点
        LocalDateTime expirationTime = now.plusSeconds(expiresInSeconds);
        // 格式化为字符串（可选）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expirationTimeStr = expirationTime.format(formatter);
        Map info = new HashMap<>();
        info.put(key, expirationTimeStr);
        tokenResponse.setUnknownKeys(info);
        return tokenResponse;
    }
}
