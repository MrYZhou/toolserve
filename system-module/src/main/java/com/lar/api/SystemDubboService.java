package com.lar.api;


import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class SystemDubboService implements SystemDubboApi{
    @Override
    public String getName() {
        return "dubbo server";
    }
}
