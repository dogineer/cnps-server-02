package com.develop.web.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
@Slf4j
public class MyWebSocketHandler extends AbstractWebSocketHandler {
    private final MyWebSocketClient webSocketClient;

    @Autowired
    public MyWebSocketHandler(MyWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        webSocketClient.setSessionUserId(session, Integer.valueOf(message.getPayload()));
        log.info(webSocketClient.getCurrentUsers());
        log.info("[Socket Join] 사용자 ID(UK): " + message.getPayload() + ", " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketClient.removeSession(session);
    }
}

