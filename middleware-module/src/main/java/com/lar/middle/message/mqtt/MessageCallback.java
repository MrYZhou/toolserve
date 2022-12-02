package com.lar.middle.message.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

/**
 * consumer 消费者
 */
@Component
public class MessageCallback implements MqttCallback {

    private static MqttClient client;

    @Override
    public void connectionLost(Throwable throwable) {
        if (client == null || !client.isConnected()) {
            System.out.println("连接断开，正在重连....");
            try {
                client.connect();
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
//        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}