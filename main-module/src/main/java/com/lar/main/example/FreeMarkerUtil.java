package com.lar.main.example;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Component
public class FreeMarkerUtil implements InitializingBean {
    @Autowired
    Configuration configuration;


    Template template;

    public Template getTemplate(String name) throws IOException {
        Template template = configuration.getTemplate(name);
        this.template = template;
        return template;
    }

    public void render(Map params, FileWriter fileWriter) throws IOException, TemplateException {
        template.process(params, fileWriter);
    }

    public void render(Map params, String path) throws IOException, TemplateException {
        FileWriter fileWriter = new FileWriter(path);
        this.render(params, fileWriter);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configuration.setClassForTemplateLoading(this.getClass(), "/code/");
    }
}
