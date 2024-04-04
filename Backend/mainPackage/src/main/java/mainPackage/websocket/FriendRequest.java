package mainPackage.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/friend-request/{username}")
@Component
public class FriendRequest {

    private static final Set<FriendRequest> endpoints = new CopyOnWriteArraySet<>();
    private static final Map<String, Session> sessionUsernameMap = new HashMap<>();
    private static final Map<String, Set<String>> pendingFriendRequests = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        sessionUsernameMap.put(username, session);
        endpoints.add(this);
        sendPendingFriendRequests(session, username);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String[] parts = message.split(":");
        String action = parts[0];
        String otherUsername = parts[1];

        if ("ACCEPT_FRIEND_REQUEST".equals(action)) {
            acceptFriendRequest(session, otherUsername);
        } else if ("SEND_FRIEND_REQUEST".equals(action)) {
            sendFriendRequest(session, otherUsername);
        } else {
            // Handle unknown action
            session.getBasicRemote().sendText("Unknown action: " + action);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        endpoints.remove(this);
        sessionUsernameMap.values().remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        // Handle WebSocket error event
        // Log or handle error
        session.getBasicRemote().sendText("An error occurred: " + throwable.getMessage());
    }

    private void acceptFriendRequest(Session session, String otherUsername) throws IOException {
        String username = getUsernameFromSession(session);
        if (username == null) {
            session.getBasicRemote().sendText("User not authenticated");
            return;
        }

        Session recipientSession = sessionUsernameMap.get(otherUsername);
        if (recipientSession == null) {
            session.getBasicRemote().sendText("User " + otherUsername + " not found");
            return;
        }

        Set<String> userRequests = pendingFriendRequests.get(username);
        if (userRequests != null && userRequests.contains(otherUsername)) {
            // Logic to accept friend request
            // Example: Add otherUsername to session user's friend list

            // Notify sender about acceptance
            recipientSession.getBasicRemote().sendText("Friend request accepted by " + username);

            // Remove from pending requests
            userRequests.remove(otherUsername);
        }
    }

    private void sendFriendRequest(Session session, String otherUsername) throws IOException {
        String username = getUsernameFromSession(session);
        if (username == null) {
            session.getBasicRemote().sendText("User not authenticated");
            return;
        }

        Session recipientSession = sessionUsernameMap.get(otherUsername);
        if (recipientSession == null) {
            session.getBasicRemote().sendText("User " + otherUsername + " not found");
            return;
        }

        // Notify recipient about friend request
        recipientSession.getBasicRemote().sendText("You have a new friend request from " + username);

        // Add to recipient's pending requests
        Set<String> recipientRequests = pendingFriendRequests.getOrDefault(otherUsername, new CopyOnWriteArraySet<>());
        recipientRequests.add(username);
        pendingFriendRequests.put(otherUsername, recipientRequests);
    }

    private void sendPendingFriendRequests(Session session, String username) throws IOException {
        Set<String> userRequests = pendingFriendRequests.getOrDefault(username, new CopyOnWriteArraySet<>());
        for (String request : userRequests) {
            session.getBasicRemote().sendText("You have a pending friend request from " + request);
        }
        pendingFriendRequests.remove(username); // Clear pending requests after sending
    }

    private String getUsernameFromSession(Session session) {
        for (Map.Entry<String, Session> entry : sessionUsernameMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
