package mainPackage.websocket;
import java.io.File;
import java.io.IOException;
import java.util.*;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import mainPackage.imageProcess.Image;
import mainPackage.imageProcess.ImageProcessingController;
import mainPackage.imageProcess.ImageRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@ServerEndpoint("/chat/{chatID}/{username}")
@Component
@Controller
public class DirectChat {

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private static ImageRepository imageRepository;


    // cannot autowire static directly (instead we do it by the below
    // method
    private static MessageRepository msgRepo;

    /*
     * Grabs the MessageRepository singleton from the Spring Application
     * Context.  This works because of the @Controller annotation on this
     * class and because the variable is declared as static.
     * There are other ways to set this. However, this approach is
     * easiest.
     */
    @Autowired
    public void setMessageRepository(MessageRepository repo) {
        msgRepo = repo;  // we are setting the static variable
    }

    private static int count = 0;

    @Autowired
    public void setImageRepository(ImageRepository ir) {
        imageRepository = ir;
    }

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(DirectChat.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session  represents the WebSocket session for the connected user.
     * @param username username1 specified in path parameter.
     */

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("chatID") String chatID) throws IOException {

        // server side log
        logger.info("[onOpen] ChatSession ID " + chatID + " joined user: " + username);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("duplicate user");
            session.close();
        } else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);

            // send to the user joining in
            sendMessageToParticularUser(username, "user connected: " + username);

            // send to everyone in the chat
            broadcast("User: " + username + " has joined the chat.");
        }
    }

    /**
     * Helper method for OnMessage. Can't use ImageProcessingController within onMessage.
     */
    private String onMessageHelper(Image img, String fn) {
        try {
            nu.pattern.OpenCV.loadLocally();

            byte[] decoded = Base64.getDecoder().decode(img.getBase64Encoding());
            img.setFileName(fn);
            String fileName = img.getFileName();

            // Save the decoded image to the specified file path
            FileUtils.writeByteArrayToFile(new File(fileName), decoded);

            // Read the saved image as bytes again
            byte[] savedImageBytes = FileUtils.readFileToByteArray(new File(fileName));

            // Encode the saved image bytes as Base64 string
            imageRepository.save(img);
            return Base64.getEncoder().encodeToString(savedImageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "internal server error";
        }
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message sent from the client.
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        // get the username by session
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onMessage] from: " + username + " message: " + message);

        // Direct message to
        // split by space
        String[] split_msg = message.split("\\s+");

        /**
         user1 sends base64 encoded string
         user2 receives base64 encoded string
         */

        // Check if the message starts with the specific command for saving an image
        if (message.startsWith("!saveimage ")) {
            String encodedImageString = split_msg[2]; // Assuming 12 characters for "!saveimage "

            // Create an Image object with the extracted Base64 string
            Image imageToSave = new Image(null, encodedImageString);
            try {
                // Call the saveImage method from ImageProcessingController to save and return Base64 string
                String savedImageB64 = onMessageHelper(imageToSave, username + count + ".jpg");
                count++;
                // Send the processed (saved) image Base64 string back to the user
                sendMessageToParticularUser(split_msg[1], "[DM from " + username + "]: " + savedImageB64);
                logger.info("[onMessage] from: " + username + "\"" + savedImageB64 + "\" to: " + split_msg[0]);
            } catch (Exception e) {
                logger.error("[Image Save Error] for user " + username + ": " + e.getMessage());
                sendMessageToParticularUser(username, "Error saving image. Please try again.");
            }
        } else {
            // Combine the rest of message
            StringBuilder actualMessageBuilder = new StringBuilder();
            for (int i = 1; i < split_msg.length; i++) {
                actualMessageBuilder.append(split_msg[i]).append(" ");
            }
            logger.info("[onMessage] from: " + username + "\"" + message + "\" to: " + split_msg[0]);
            String actualMessage = actualMessageBuilder.toString();
            sendMessageToParticularUser(username, "[DM sent to " + split_msg[0] + "]: " + actualMessage);
            sendMessageToParticularUser(split_msg[0], "[DM from " + username + "]: " + actualMessage);
        }

    }


    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // send the message to chat
        broadcast(username + " disconnected");
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
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private void sendMessageToParticularUser(String username, String message) {
        try {
            if (usernameSessionMap.containsKey(username)) {
                usernameSessionMap.get(username).getBasicRemote().sendText(message);
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
    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }

    // Gets the Chat history from the repository
    private String getChatHistory() {
        List<Message> messages = msgRepo.findAll();
        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(messages != null && messages.size() != 0) {
            for (Message message : messages) {
                sb.append(message.getUserName() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }

}
