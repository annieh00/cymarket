package mainPackage.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



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

@Controller
@ServerEndpoint(value = "/auction/{auctionID}/{username}")
public class Auction {
    private static HashMap< Session, String > usernameFromSession = new HashMap<>();
    private static HashMap < String, Session > sessionFromUsername = new HashMap<>();

    private static HashMap <Session, String> AuctionIDFromSession = new HashMap<>();

    private static HashMap <String,HashMap<Session,Boolean>> connectedSessionsFromAuctionId = new HashMap<>();


    private final Logger logger = LoggerFactory.getLogger(Auction.class);


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("auctionID") String auctionID) throws IOException {
        if(username == null || username == ""){
            return;
        }

        logger.info("[onOpen] auctionID " + auctionID + " user joined: " + username);


        // Handle the case of a duplicate session
        if (sessionFromUsername.containsKey(username)) {
            //case1: user is trying to join another auction
            //Normally, this would not be the case, as the app should have properly sent a signal to
            //close the previous session before joining a new one.
            //Thus, in this case, we should remove the user from the hashmap to reset the session

            if(usernameFromSession.containsKey(sessionFromUsername.get(username))){
                usernameFromSession.remove(sessionFromUsername.get(username));
            }



            if(AuctionIDFromSession.containsKey(sessionFromUsername.get(username))){

                if(connectedSessionsFromAuctionId.containsKey(AuctionIDFromSession.get(sessionFromUsername.get(username)))){
                    HashMap<Session,Boolean> sMap = connectedSessionsFromAuctionId.get(AuctionIDFromSession.get(sessionFromUsername.get(username)));
                    if(sMap.containsKey(sessionFromUsername.get(username))){
                        sMap.remove(sessionFromUsername.get(username));
                    }
                }

                AuctionIDFromSession.remove(sessionFromUsername.get(username));
            }


            sessionFromUsername.get(username).getBasicRemote().sendText("Closed session because you are trying to open another session without closing the other one");
            sessionFromUsername.get(username).close();
            sessionFromUsername.remove(username);


            //db.get auction name that is related to this session (use AuctionID)
            session.getBasicRemote().sendText("Please Try Again");
            session.close();

        }else {
            // map current session with username
            usernameFromSession.put(session, username);

            // map current username with session
            sessionFromUsername.put(username, session);

            //map current session with auctionID
            AuctionIDFromSession.put(session,auctionID);

            //check if there is an arraylist of sessions corresponding to a unique auction ID
            if(connectedSessionsFromAuctionId.containsKey(auctionID)){
                HashMap<Session,Boolean> temp = connectedSessionsFromAuctionId.get(auctionID);
                if(temp != null){
                    temp.put(session,true);
                }else{
                    temp = new HashMap<>();
                    temp.put(session,true);
                    connectedSessionsFromAuctionId.put(auctionID,temp);
                }

            }



            // send to the user that they joined the auction (from DB)
            sendMessageToPArticularUser(username, "user connected: "+username);

            // send to everyone in the chat
            broadcast("User: " + username + " has Joined the Auction");
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
        String username = usernameFromSession.get(session);


        // server side log


        // Direct message to
        // split by space
        String[] split_msg =  message.split("\\s+");

        // Combine the rest of message
        StringBuilder actualMessageBuilder = new StringBuilder();
        for (int i = 1; i < split_msg.length; i++) {
            actualMessageBuilder.append(split_msg[i]).append(" ");
        }
        logger.info("[onMessage] from: " + username + "\"" + message + "\" to: " + split_msg[0]);
        String actualMessage = actualMessageBuilder.toString();
        sendMessageToPArticularUser(split_msg[0], "[DM from " + username + "]: " + actualMessage);
    }



    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username = usernameFromSession.get(session);

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        usernameFromSession.remove(session);
        sessionFromUsername.remove(username);



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
        String username = usernameFromSession.get(session);

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
            if(sessionFromUsername.containsKey(username)) {
                sessionFromUsername.get(username).getBasicRemote().sendText(message);
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
        usernameFromSession.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }



}
