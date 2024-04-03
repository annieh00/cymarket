package mainPackage.friendService;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/friend-request/{username}")
@Component
public class FriendRequest {

    private static Map<Session, String> sessionUsernameMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        // Handle WebSocket connection open event
        sessionUsernameMap.put(session, username);
        // Add logic
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle WebSocket message received event
        // Parse message and perform corresponding actions (accept, reject, remove)
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // Handle WebSocket connection close event
        sessionUsernameMap.remove(session);
        // Add logic
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Handle WebSocket error event
        // Log or handle error
    }
}
