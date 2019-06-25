package ch.fhnw.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketEndpoint extends TextWebSocketHandler {
    private Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);

    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.sessions.put(session.getId(), session);
        logger.info("Connection established");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        sendMessage(message);
    }

    public void sendMessage(TextMessage message) throws IOException {
        if (this.sessions != null){


            for (WebSocketSession session : this.sessions.values()) {
                session.sendMessage(message);
                logger.info("Sent text-message to session " + session.getId());
            }
        }
        else
        {
            logger.info("No session available to push message");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Closed session " + session.getId());
        this.sessions.remove(session.getId());
    }
}
