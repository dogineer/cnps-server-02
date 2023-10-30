package com.develop.web.websocket;

import com.develop.web.ingest.dto.SendMessageDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Component
@Slf4j
public class MyWebSocketClient {
    private static final Map<Integer, WebSocketSession> currentUsers = new HashMap<>();

    private Integer getSessionUserId(WebSocketSession session) {
        for (Map.Entry<Integer, WebSocketSession> entry : currentUsers.entrySet()) {
            if (entry.getValue() == session) {
                log.info("[Socket Info] 사용자 세션 엔트리: " + entry.getValue());
                return entry.getKey();
            }
        }
        return null;
    }

    public void setSessionUserId(WebSocketSession session, Integer memberId) {
        currentUsers.put(memberId, session);
    }

    public String getCurrentUsers() {
        return currentUsers.values().toString();
    }

    public void removeSession(WebSocketSession session) {
        Integer userId = getSessionUserId(session);
        currentUsers.remove(userId);
        log.info("[Socket Disconnect] 사용자 ID(UK): " + userId + "\n");
    }

    public static void sendMessageToAll(SendMessageDto message) throws IOException {
        for (WebSocketSession session : currentUsers.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message.toJsonString()));
            }
        }
    }

    public static void sendMessageToUser(Integer memberId, String message) throws IOException {
        WebSocketSession session = currentUsers.get(memberId);

        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        } else throw new RuntimeException("[Socket Error] 현재 세션을 찾지 못했습니다.");
    }
}



