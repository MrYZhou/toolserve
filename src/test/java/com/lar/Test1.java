package com.lar;

import org.junit.jupiter.api.Test;
import org.noear.folkmq.FolkMQ;
import org.noear.folkmq.client.MqClient;
import org.noear.folkmq.client.MqMessage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
public class Test1 {
    @Test
    public void test1() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 10000; i++) {
            System.out.println(1);
        }
        stopWatch.stop();

        System.out.println(stopWatch.getTotalTimeMillis());
    }

    @Test
    public void testfolk() throws Exception {
        //创建客户端，并连接
        MqClient client = FolkMQ.createClient("folkmq://127.0.0.1:18602").connect();

        //订阅主题，并指定加入的消费者分组
        client.subscribe("demo", "demoapp", message -> {
            System.out.println("========================================");
            System.out.println(message);
        });

        //发布消息
        client.publishAsync("demo", new MqMessage("hello world!"));
    }

}
