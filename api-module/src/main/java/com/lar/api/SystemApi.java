package com.lar.api;

import org.noear.dami.spring.boot.annotation.DamiTopic;

@DamiTopic("toolserve.systemEvent")
public interface SystemApi {

    void testService(String name);
}
