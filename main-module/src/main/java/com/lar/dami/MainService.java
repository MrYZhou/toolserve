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
public class MainService {
    @Autowired
    SystemApi serviceApi;
    @GetMapping
    public AppResult<?> list(){
        serviceApi.testService("test");
        return AppResult.success();
    }
}
