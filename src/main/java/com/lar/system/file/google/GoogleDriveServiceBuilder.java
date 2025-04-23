package com.lar.system.file.google;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class GoogleDriveServiceBuilder {
    @Value("${google.client.client-id:}")
    private String clientId;
    @Value("${google.client.client-secret:}")
    private String clientSecret;

    public Drive getDriveService(String userId) throws IOException, GeneralSecurityException {
//        GoogleTokenResponse token = tokenStore.getToken(userId);
        // 从redis获取对象
        GoogleTokenResponse token = new GoogleTokenResponse() ;
        if (token == null) throw new IllegalStateException("User not authorized");

        // 自动刷新令牌
        if (token.getExpiresInSeconds() < 60) {
            token = refreshToken(userId, token.getRefreshToken());
        }


        // 构建凭证
        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setJsonFactory(GsonFactory.getDefaultInstance())
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .build()
                .setAccessToken(token.getAccessToken());

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("SpringDriveApp")
                .build();
    }

    private GoogleTokenResponse refreshToken(String userId, String refreshToken) throws IOException {
        GoogleTokenResponse newToken = new GoogleRefreshTokenRequest(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                refreshToken,
                clientId,
                clientSecret)
                .execute();
        // todo 设置新的token
//        tokenStore.storeToken(userId, newToken);
        return newToken;
    }
}
