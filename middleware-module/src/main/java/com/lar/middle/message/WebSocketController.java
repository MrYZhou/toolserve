package com.lar.middle.message;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webscoket")
public class WebSocketController {
    private final WebSocketServer webSocketServer;

    WebSocketController(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    @GetMapping("/send")
    public void send() {
        webSocketServer.sendAllMessage("hello");
    }
}
