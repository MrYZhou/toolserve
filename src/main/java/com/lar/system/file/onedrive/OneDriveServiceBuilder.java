package com.lar.system.file.onedrive;


import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.lar.common.util.JsonUtil;
import com.lar.common.util.RedisUtil;
import com.lar.system.file.model.OneDriveModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class OneDriveServiceBuilder {
    @Value("${onedrive.client.client-id:d6029edc-9b6b-45b9-bac4-bef3370a6510}")
    private String clientId;
    @Value("${onedrive.client.client-secret:BdD8Q~6iMK676URfgVz6OUUH3NiLfFemNXdufdez}")
    private String clientSecret;
    @Autowired
    private RedisUtil redisUtil;

    public OneDriveModel getToken(String userId) throws IOException {
//        Object data = redisUtil.getString("userid" + "onedrive");
        Object data = null;
        if (Objects.isNull(data)) {
            return null;
        }
        OneDriveModel model = JSONUtil.toBean((String) data, OneDriveModel.class);
        if (model.getRefreshToken() == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        // 先判断刷新令牌有没有过期

//        if (!Objects.isNull(map.get("refreshTokenExpiresIn"))) {
//            String refreshTokenExpiresIn = (String) map.get("refreshTokenExpiresIn");
//            LocalDateTime targetRefreshTime = LocalDateTime.parse(refreshTokenExpiresIn, formatter);
//            Duration remainingRefresh = Duration.between(currentTime, targetRefreshTime);
//            long remainingMinutesRefresh = remainingRefresh.toMinutes();
//            if (remainingMinutesRefresh < 30) {
//                redisUtil.remove("userid" + "onedrive");
//                return null;
//            }
//        }

        // 自动刷新令牌
        String expirationTimeStr = model.getExpirationTimeStr();
        LocalDateTime targetTime = LocalDateTime.parse(expirationTimeStr, formatter);
        Duration remaining = Duration.between(currentTime, targetTime);
        long remainingMinutes = remaining.toMinutes();
        if (remainingMinutes < 30) {
            model = refreshToken(userId, model.getRefreshToken());
        }
        return model;
    }

    private OneDriveModel refreshToken(String userId, String refreshToken) throws IOException {
        HashMap map = new HashMap();
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("grant_type", "refresh_token");
        map.put("refresh_token", refreshToken);
        HttpRequest httpRequest = HttpRequest.post("https://login.live.com/oauth20_token.srf")
                .setSSLProtocol("TLSv1.2")  // 指定 TLS 版本
                .form(map)
                .disableCookie();// 可选：避免会话干扰

        String body = httpRequest.execute().body();
        Map data = JsonUtil.toBean(body, Map.class);
        OneDriveModel tokenResponse = new OneDriveModel();
        tokenResponse.setAccessToken(data.get("access_token").toString());
        tokenResponse.setRefreshToken(refreshToken);

        // 计算过期时间
        int expiresInSeconds = (int) data.get("expires_in");
        this.setExpireTime(tokenResponse, expiresInSeconds);
//        redisUtil.insert(userId + "onedrive", JsonUtil.getObjectToString(tokenResponse),36000);

        return tokenResponse;
    }

    public OneDriveModel setExpireTime(OneDriveModel tokenResponse, long expiresInSeconds) {
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 计算过期时间点
        LocalDateTime expirationTime = now.plusSeconds(expiresInSeconds);
        // 格式化为字符串（可选）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expirationTimeStr = expirationTime.format(formatter);

        tokenResponse.setExpirationTimeStr(expirationTimeStr);
        return tokenResponse;
    }
}
