package com.lar.api;

import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 提供common的api层的实现，是一一对应的
 */
@DamiTopic("toolserve.systemEvent")
public class SystemService {
    public void test2(String name){
        System.out.println("reload system:"+name);
    }

}
