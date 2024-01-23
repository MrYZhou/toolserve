package com.lar.config;


public class WhiteList {
    static String[] list;

    static {
        list = new String[]{
                "user/login",
        };
    }

    static String[] get() {
        return list;
    }
}
