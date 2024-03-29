package com.lar.message.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 进一步优化方案就是用双map获取，在创建的时候，随机新建一个sessionid 从satoken取userid作为key,sessionid为value.
 * 然后sessionid为key，value是session
 *
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")  // 接口路径 ws://localhost:8087/webSocket/userId;
public class WebsocketServer {

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
    //  注：底下WebsocketServer是当前类名
    private static final CopyOnWriteArraySet<WebsocketServer> webSockets = new CopyOnWriteArraySet<>();
    // 用来存在线连接数
    private static final Map<String, Session> sessionPool = new HashMap<String, Session>();
    // key是用户id,value是sessionId
    private static Map<String,String> userBindMap = new HashMap<>();

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("【websocket消息】有新的连接，总数为:" + webSockets.size());
        } catch (Exception e) {
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            log.info("【websocket消息】连接断开，总数为:" + webSockets.size());
        } catch (Exception e) {
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端消息:" + message);
    }

    /**
     * 发送错误时的处理
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        log.error("用户错误,原因:" + error.getMessage());
        error.printStackTrace();
    }


    // 此为广播消息,全部人都可以收到
    public void sendAllMessage(String message) {
        log.info("【websocket消息】广播消息:" + message);
        for (WebsocketServer webSocket : webSockets) {
            this.sendText(webSocket.session,message);
        }
    }

    public void sendText(Session session,String message) {
        try {
            if (session!=null && session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 此为单点消息
    public void sendMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        this.sendText(session,message);
    }

    // 此为单点消息(多人)
    public void sendMessageMultiUser(ArrayList<String> userIds, String message) {
        for (String userId : userIds) {
            this.sendMessage(userId,message);
        }
    }

}
