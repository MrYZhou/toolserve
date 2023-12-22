package com.lar.service;

import org.noear.dami.spring.boot.annotation.DamiTopic;

@DamiTopic("toolserve.event")
public interface ServiceApi {
    void reload(String name);
     void test2(String name);
}
