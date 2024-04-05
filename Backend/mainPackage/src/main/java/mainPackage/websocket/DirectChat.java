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
import mainPackage.meetingLocationPackage.MeetingLocation;
import mainPackage.meetingLocationPackage.MeetingLocationRepository;
import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;
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
@ServerEndpoint("/chat/{username}")
@Component
@Controller
public class DirectChat {

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private static GeneralUserRepository generalUserRepository;

    private static MeetingLocationRepository meetingLocationRepository;
    private static ImageRepository imageRepository;
    // cannot autowire static directly (instead we do it by the below
    // method
    private static MessageRepository messageRepository;

    /*
     * Grabs the MessageRepository singleton from the Spring Application
     * Context.  This works because of the @Controller annotation on this
     * class and because the variable is declared as static.
     * There are other ways to set this. However, this approach is
     * easiest.
     */
    @Autowired
    public void setMessageRepository(MessageRepository mr) { messageRepository = mr; }

    @Autowired
    public void setImageRepository(ImageRepository ir) { imageRepository = ir; }

    @Autowired
    public void setGeneralUserRepository(GeneralUserRepository gur) { generalUserRepository = gur; }

    @Autowired
    public void setMeetingLocationRepository(MeetingLocationRepository mlr) { meetingLocationRepository = mlr; }

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(DirectChat.class);

    private static int count = 0;

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session  represents the WebSocket session for the connected user.
     * @param username username1 specified in path parameter.
     */

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        // server side log
        logger.info("[onOpen] " + username + " has joined.");

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("duplicate user");
            session.close();
        }

        // store connecting user information
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        // Send messages and broadcast
        sendMessageToParticularUser(username, "user connected: " + username);
        // Check if there is another user in the session
        if (sessionUsernameMap.size() == 2) {
            // Get usernames of both users
            List<String> usernames = new ArrayList<>(sessionUsernameMap.values());

            // Retrieve chat history between these two users
            String chatHistory = getChatHistory(usernames.get(0), usernames.get(1));

            // Send chat history to both users
            sendMessageToParticularUser(usernames.get(0), chatHistory);
            sendMessageToParticularUser(usernames.get(1), chatHistory);
        }
        broadcast("User: " + username + " has joined the chat.");
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

        GeneralUser sender = generalUserRepository.findGeneralUserByUserName(username);

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
            try {
                String encodedImageString = split_msg[2]; // Assuming 12 characters for "!saveimage "

                // Create an Image object with the extracted Base64 string
                Image imageToSave = new Image(null, encodedImageString);
                // Call the saveImage method from ImageProcessingController to save and return Base64 string
                String savedImageB64 = onMessageHelper(imageToSave, username + count + ".jpg");
                count++;
                // Send the processed (saved) image Base64 string back to the user
                sendMessageToParticularUser(split_msg[1], "[DM from " + username + "]: " + savedImageB64);
                logger.info("[onMessage] from: " + username + "\"" + savedImageB64 + "\" to: " + split_msg[0]);
            } catch (Exception e) {
                logger.error("[Image Save Error] for user " + username + ": " + e.getMessage());
                sendMessageToParticularUser(username, "Error saving image. Make sure you start with !saveimage followed by the user you are sending to followed by your image.");
            }
        } else if(message.startsWith("!location ")) {
            try {
//                String otherUser = split_msg[1];
                // Extract meeting location data from the message
                double latitude = Double.parseDouble(split_msg[1]);
                double longitude = Double.parseDouble(split_msg[2]);

                // Process the meeting location message
                processMeetingLocation(username, latitude, longitude);
                broadcast("Meeting location has been set by " + username);
                // Check the number of users in the session
                if (sessionUsernameMap.size() < 3) {
                    // If there are 1-2 users, send JSON data to all users
                    sendLocationDataToAllUsers(latitude, longitude);
                }
            } catch (NumberFormatException e) {
                // Handle invalid latitude or longitude format
                sendMessageToParticularUser(username, "Error: Invalid latitude or longitude format.");
                logger.error("Invalid latitude or longitude format in message: " + message);
            }
        } else {
            String otherUser = split_msg[0];
            // Check if the other user is in the session to send a message to them
            if (sessionUsernameMap.containsValue(otherUser)) {
                // Combine the rest of message
                StringBuilder actualMessageBuilder = new StringBuilder();
                for (int i = 1; i < split_msg.length; i++) {
                    actualMessageBuilder.append(split_msg[i]).append(" ");
                }
                logger.info("[onMessage] from: " + username + "\"" + message + "\" to: " + otherUser);
                String actualMessage = actualMessageBuilder.toString();
                sendMessageToParticularUser(username, "[DM sent to " + otherUser + "]: " + actualMessage);
                sendMessageToParticularUser(split_msg[0], "[DM from " + username + "]: " + actualMessage);

                GeneralUser loggedInUser = generalUserRepository.findGeneralUserByUserName(username);
                GeneralUser sendingToUser = generalUserRepository.findGeneralUserByUserName(otherUser);
                messageRepository.save(new Message(loggedInUser, sendingToUser, message));
            } else {
                // Send a response to the user instructing them to start the message with the username
                sendMessageToParticularUser(username, "Error: Please start your message with the " +
                        "username you are trying to message, followed by a space, followed by your message.");
            }
        }

    }


    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        // Get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // Server-side log
        logger.info("[onClose] " + (username != null ? username : "Unknown user"));

        // Remove user from memory mappings
        if (username != null) {
            sessionUsernameMap.remove(session);
            usernameSessionMap.remove(username);

            // Send the message to chat
            broadcast(username + " disconnected");
        } else {
            // Handle the case where the username is null (optional)
            logger.warn("Username is null for session: " + session.getId());
        }
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
    private String getChatHistory(String user1, String user2) {
        // Fetch messages sent between user1 and user2
        List<Message> messages = messageRepository.findByUserSentUserNameAndUserReceivedUserNameOrUserSentUserNameAndUserReceivedUserNameOrderBySent(user1, user2, user2, user1);

        StringBuilder sb = new StringBuilder();
        for (Message message : messages) {
            // Perform null check on userSent and userReceived
            if (message.getUserSent() != null && message.getUserReceived() != null) {
                sb.append(message.getUserSent().getUserName() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }


    // Method to process meeting location messages
    private void processMeetingLocation(String username, double latitude, double longitude) {
        // Create a MeetingLocation object with the extracted latitude and longitude
        MeetingLocation meetingLocation = new MeetingLocation();
        meetingLocation.setX(Double.toString(latitude));
        meetingLocation.setY(Double.toString(longitude));

        // Save the meeting location using the repository
        meetingLocationRepository.save(meetingLocation);

//        // Optionally, can send a confirmation message back to the user
//        sendMessageToParticularUser(username, "Meeting location set successfully at Latitude: " + latitude + ", Longitude: " + longitude);
    }

    private void sendLocationDataToAllUsers(double latitude, double longitude) {
        // Send JSON data to all users
        for (Session session : sessionUsernameMap.keySet()) {
            try {
                session.getBasicRemote().sendText("{\"latitude\" : \"" + latitude + "\", \"longitude\" : \"" + longitude + "\"}");
                // return "{\"fromServer\" : true, \"permission\" :" + user.getUserType() + ",\"username\" :" + user.getUserName() + "}";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
