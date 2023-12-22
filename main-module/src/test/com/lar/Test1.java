package com.lar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@SpringBootTest
public class Test1 {
    @Test
    public void test1() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 10000 ; i++) {
            System.out.println(1);
        }
        stopWatch.stop();

        System.out.println(stopWatch.getTotalTimeMillis());
    }


}
