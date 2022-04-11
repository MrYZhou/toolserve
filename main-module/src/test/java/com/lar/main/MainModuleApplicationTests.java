package com.lar.main;

import common.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainModuleApplicationTests {
    @Autowired
    RedisUtil redisUtil;
    @Test
    void contextLoads() {
        redisUtil.set("testutil","success");
    }

    @Test
    void getload() {
        System.out.println(redisUtil.get("testutil"));
    }

}
