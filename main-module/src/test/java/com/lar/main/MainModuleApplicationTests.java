package com.lar.main;

import common.util.EmailTask;
import common.util.RedisUtil;
import middle.task.email.MailService;
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
    EmailTask emailTask;

    @Autowired
    MockMvc mockMvc;


    @Autowired
    private MailService mailService;




    @Test
    void redisTest() {
        redisUtil.set("testutil","success");
    }






}
