package com.lar.security.config;


public class WhiteList {
    static String[] list;

    static {
        list = new String[]{
                "/user/login",
                "/user/register",
                "/websocket/**",
                "/localCall",
                "/localCall2"
        };
    }

    static String[] get() {
        return list;
    }
}
