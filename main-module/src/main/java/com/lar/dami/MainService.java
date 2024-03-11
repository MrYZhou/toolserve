package com.lar.dami;

import com.lar.api.SystemApi;
import com.lar.vo.AppResult;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/localCall")
//@DamiTopic("toolserve.systemEvent")
public class MainService {
    @Autowired
    SystemApi serviceApi;
//    public void reload(String name){
//        System.out.println("reload book:"+name);
//    }
    @GetMapping
    public AppResult<?> list(){
        serviceApi.test2("test");
        return AppResult.success();
    }
}
