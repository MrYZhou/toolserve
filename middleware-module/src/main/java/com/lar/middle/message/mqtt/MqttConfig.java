package com.lar.middle.message.mqtt;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//MQTT订阅
@Slf4j
@Configuration
@ConfigurationProperties("mqtt")
@Data
@Component
public class MqttConfig {

    String host;
    String clientId;
    String topic;
    String username;
    String password;
    Integer timeout;
    Integer keepalive;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setCleanSession(true);
        options.setConnectionTimeout(timeout);
        options.setKeepAliveInterval(keepalive);
        return options;
    }

    @Bean
    public MqttClient mqttClient(MqttConnectOptions mqttConnectOptions) {
        try {
            MqttClient client = new MqttClient(host, clientId);

            client.setCallback(new MessageCallback());
            IMqttToken iMqttToken = client.connectWithResult(mqttConnectOptions);
            boolean complete = iMqttToken.isComplete();
            log.info("mqtt建立连接：{}", complete);

            // 订阅主题
            client.subscribe(topic, 0);
            log.info("已订阅topic：{}", topic);

            return client;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("mqtt 连接异常");
        }
    }



}