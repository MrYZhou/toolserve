package com.lar.main.example;

import lombok.Data;

@Data
public class DbInfo {
    String uri;
    String port;
    String dbName;
    String user;
    String password;
}
