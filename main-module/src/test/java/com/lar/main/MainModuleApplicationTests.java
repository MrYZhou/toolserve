package com.lar.main;

import com.lar.example.FreeMarkerUtil;
import common.util.EmailTask;
import common.util.RedisUtil;
import freemarker.template.TemplateException;
import com.lar.middle.task.email.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    FreeMarkerUtil freeMarkerUtil;

    @Test
    void redisTest() {
        redisUtil.set("testutil", "success");
    }

    @Test
    void testencode() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("1234");
        System.out.println(encode);
    }

  
    @Test
    public void gen() throws IOException, TemplateException {
        //生成freemarker的模板对象，调用方法获取模板
//        Template template = configuration.getTemplate("1.ftl");

        freeMarkerUtil.getTemplate("1.ftl");

        //获取数据的过程,这里自定义封装成了方法
        Map params = new HashMap();
        params.put("package", "books");
        params.put("json", "'{\"name\":\"larry\"}'");
        freeMarkerUtil.render(params, "D:/book.java");
        //合成
//        template.process(params, new FileWriter("D:/book.java")); //第一个参数：数据模型，第二个参数：输出流
    }

}
