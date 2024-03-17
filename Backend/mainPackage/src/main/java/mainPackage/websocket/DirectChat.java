package mainPackage.websocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 *
 * This class is annotated with Spring's `@ServerEndpoint` and `@Component`
 * annotations, making it a WebSocket endpoint that can handle WebSocket
 * connections at the "/chat/{username}" endpoint.
 *
 * Example URL: ws://localhost:8080/chat/username
 *
 * The server provides functionality for broadcasting messages to all connected
 * users and sending messages to specific users.
 */
@ServerEndpoint("/chat/{username1}/{username2}")
@Component

public class DirectChat {

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map < Session, String > sessionUsername1Map = new Hashtable <>();
    private static Map < String, Session > username1SessionMap = new Hashtable <>();

    private static Map < Session, String > sessionUsername2Map = new Hashtable <>();
    private static Map < String, Session > username2SessionMap = new Hashtable <>();
    private static Map <String, String> oneToOneSessionUsersMap = new Hashtable<>();

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(DirectChat.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username1 username1 specified in path parameter.
     * @param username2 username2 specified in path parameter.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username1") String username1, @PathParam("username2") String username2) throws IOException {

        // server side log
        logger.info("[onOpen] ChatSession between  " + username1 + " and " + username2);


        // Handle the case of a duplicate username
        if (username1SessionMap.containsKey(username1) && oneToOneSessionUsersMap.get(username1).equals(username2)) {
            session.getBasicRemote().sendText("duplicate session");
            session.close();
        }else {
            // map current session with username
            sessionUsername1Map.put(session, username1);
            sessionUsername2Map.put(session, username2);

            // map current username with session
            username1SessionMap.put(username1, session);
            username2SessionMap.put(username2, session);

            oneToOneSessionUsersMap.put(username1,username2);
            oneToOneSessionUsersMap.put(username2,username1);

            // send to the user joining in
            sendMessageToPArticularUser(username1, "Chat with "+username2);
            sendMessageToPArticularUser(username2, "Chat with "+username1);

            // send to everyone in the chat
            //broadcast("User: " + username + " has Joined the Chat");
        }
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message received from the client.
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        // get the username by session
        String username1 = sessionUsername1Map.get(session);
        String username2 = oneToOneSessionUsersMap.get(username1);

        // server side log
        //logger.info("[onMessage] from: " + username1 + "\"" + message + "\" to: " + username2);

        // Direct message to
            // split by space
            String[] split_msg =  message.split("\\s+");

            // Combine the rest of message
            StringBuilder actualMessageBuilder = new StringBuilder();
            for (int i = 1; i < split_msg.length; i++) {
                actualMessageBuilder.append(split_msg[i]).append(" ");
            }

            String actualMessage = actualMessageBuilder.toString();
            sendMessageToPArticularUser(username2, "[DM from " + username1 + "]: " + actualMessage);
            sendMessageToPArticularUser(username1, "[DM from " + username2 + "]: " + actualMessage);
    }



    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username1 = sessionUsername1Map.get(session);

        // server side log
        logger.info("[onClose] " + username1);

        // remove user from memory mappings
        sessionUsername1Map.remove(session);
        username1SessionMap.remove(username1);

        oneToOneSessionUsersMap.remove(username1);

        // send the message to chat
        //broadcast(username + " disconnected");
    }

    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsername1Map.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private void sendMessageToPArticularUser(String username, String message) {
        try {
            if(username1SessionMap.containsKey(username)) {
                username1SessionMap.get(username).getBasicRemote().sendText(message);
            }else if(username2SessionMap.containsKey(username)){
                username2SessionMap.get(username).getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
    }

    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
//    private void broadcast(String message) {
//        sessionUsernameMap.forEach((session, username) -> {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                logger.info("[Broadcast Exception] " + e.getMessage());
//            }
//        });
//    }
}
