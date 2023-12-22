package com.lar.api;

import org.noear.dami.spring.boot.annotation.DamiTopic;

@DamiTopic("toolserve.event")
public class SystemService {
    public void test2(String name){
        System.out.println("reload system:"+name);
    }

}
