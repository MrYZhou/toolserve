package com.lar.config;


public class WhiteList {
    static String[] list;

    static {
        list = new String[]{
                "/user/login",
                "/user/register",
                "/websocket/**"
        };
    }

    static String[] get() {
        return list;
    }
}