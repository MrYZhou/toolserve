package com.lar.example;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FreeController {

    @Autowired
    FreeMarkerUtil freeMarkerUtil;

    @GetMapping("gen")
    public void gen() throws IOException, TemplateException {
        freeMarkerUtil.getTemplate("1.ftl");

        //获取数据的过程,这里自定义封装成了方法
        Map params = new HashMap();
        params.put("package", "books");
        params.put("json", "'{\"name\":\"larry\"}'");

        freeMarkerUtil.render(params, "D:/book2.java");
    }
}

