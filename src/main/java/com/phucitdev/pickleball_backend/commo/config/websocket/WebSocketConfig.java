package com.phucitdev.pickleball_backend.commo.config.websocket;
import com.phucitdev.pickleball_backend.commo.config.websocket.handler.AuthWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final AuthWebSocketHandler handler;
    public WebSocketConfig(AuthWebSocketHandler handler) {
        this.handler = handler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/auth")
                .setAllowedOrigins("*");
    }
}