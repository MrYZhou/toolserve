package com.lar.dubbo;

import org.apache.dubbo.config.annotation.DubboReference;

import com.lar.api.SystemDubboApi;
import com.lar.vo.AppResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/localCall2")
public class MainService2 {
    @DubboReference
    SystemDubboApi serviceApi;
    @GetMapping
    public AppResult<?> test(){
        return AppResult.success(serviceApi.getName());
    }
}
