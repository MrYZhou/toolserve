package com.lar.api;

import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 提供api-module对应的实现层
 */
@DamiTopic("toolserve.systemEvent")
public class SystemService {
    public void test2(String name){
        System.out.println("reload system:"+name);
    }

}
