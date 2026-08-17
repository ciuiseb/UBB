package mobile.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import mobile.model.dtos.BookDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    public void sendNewBook(BookDTO book) {
        sessions.forEach(s -> {
            try {
                s.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(book)));
            } catch (Exception e) { e.printStackTrace(); }
        });
    }
}

