package com.lar.system.file.model;

import lombok.Data;

@Data
public class OneDriveModel {
    private String accessToken;
    private String refreshToken;
    // 过期时间
    private  String expirationTimeStr;
}
