package com.lar.dami;

import com.lar.service.ServiceApi;
import com.lar.vo.AppResult;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/localCall")
@DamiTopic("toolserve.event")
public class MainService {
    @Autowired
    ServiceApi serviceApi;
    public void reload(String name){
        System.out.println("reload book:"+name);
    }
    @GetMapping
    public AppResult<?> list(){
        serviceApi.reload("test");
        return AppResult.success();
    }
}