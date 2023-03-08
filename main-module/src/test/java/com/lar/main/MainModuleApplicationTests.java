package com.lar.main;

import com.lar.common.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MainModuleApplicationTests {
    @Autowired
    RedisUtil redisUtil;


    @Autowired
    MockMvc mockMvc;


    @Test
    void redisTest() {
        redisUtil.set("testutil", "success");
    }


}
