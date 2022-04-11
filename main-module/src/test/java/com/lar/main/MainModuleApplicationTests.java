package com.lar.main;

import common.util.EmailTask;
import common.util.RedisUtil;
import middle.task.email.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainModuleApplicationTests {
    @Autowired
    RedisUtil redisUtil;
//    @Autowired
    EmailTask emailTask;

    @Autowired
    private MailService mailService;

    @Test
    void redisTest() {
        redisUtil.set("testutil","success");
    }



    @Test
    void emailTest() {

        mailService.sendSimpleTextMailActual("发送主题","发送内容",new String[]{"2271952106@qq.com"},null,null,null);

    }


    @Test
    void getload() {
        emailTask.sendMessge("主题","hello","1762861715@qq.com","2271952106@qq.com");
    }

}
