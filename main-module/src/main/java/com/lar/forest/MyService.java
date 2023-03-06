package com.lar.forest;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    // 注入自定义的 Forest 接口实例
    @Autowired(required = false)
    private MyClient myClient;

    public void testClient() {
        // 调用自定义的 Forest 接口方法
        // 等价于发送 HTTP 请求，请求地址和参数即为 helloForest 方法上注解所标识的内容
        String result = myClient.helloForest();
        // result 即为 HTTP 请求响应后返回的字符串类型数据
        System.out.println(result);
    }

}
