package com.lar.system.message.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket 相关配置
 */
@Configuration
@EnableWebSocket
public class WebSocketConfigNew implements WebSocketConfigurer {

    // 注册 WebSocket 处理器
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                // WebSocket 连接处理器
                .addHandler(new MyWebSocketHandler(), "/ws-connect")
                // WebSocket 拦截器
                .addInterceptors(new WebSocketInterceptor())
                // 允许跨域
                .setAllowedOrigins("*");
    }

}
