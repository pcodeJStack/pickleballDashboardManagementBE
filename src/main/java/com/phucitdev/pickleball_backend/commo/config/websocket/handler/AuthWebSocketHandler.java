package com.phucitdev.pickleball_backend.commo.config.websocket.handler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthWebSocketHandler extends TextWebSocketHandler {

    private final Map<UUID, Map<String, WebSocketSession>> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        UUID userId = getUserId(session);
        String sessionId = getSessionId(session);

        sessions
                .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                .put(sessionId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        UUID userId = getUserId(session);
        String sessionId = getSessionId(session);

        Map<String, WebSocketSession> userSessions = sessions.get(userId);
        if (userSessions != null) {
            userSessions.remove(sessionId);
        }
    }

    //  force logout nhưng bỏ qua session hiện tại
    public void forceLogout(UUID userId, String currentSessionId) {
        Map<String, WebSocketSession> userSessions = sessions.get(userId);

        if (userSessions == null) return;

        for (Map.Entry<String, WebSocketSession> entry : userSessions.entrySet()) {
            String sessionId = entry.getKey();

            if (sessionId.equals(currentSessionId)) continue;

            WebSocketSession ws = entry.getValue();

            if (ws.isOpen()) {
                try {
                    ws.sendMessage(new TextMessage("FORCE_LOGOUT"));
                    ws.close();
                } catch (Exception ignored) {}
            }
        }
    }


    private UUID getUserId(WebSocketSession session) {
        String token = getQueryParam(session, "token");
//         jwtTokenProvider.validateAccessToken(token);
//         return jwtTokenProvider.getAccountId(token);

        return UUID.fromString(getQueryParam(session, "userId")); // tạm thời
    }

    private String getSessionId(WebSocketSession session) {
        return getQueryParam(session, "sessionId");
    }

    private String getQueryParam(WebSocketSession session, String key) {
        String query = session.getUri().getQuery(); // token=xxx&sessionId=abc

        if (query == null) return null;

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(key)) {
                return pair[1];
            }
        }
        return null;
    }
}