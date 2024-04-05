package mainPackage.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;



import mainPackage.usersPackage.GeneralUser;
import mainPackage.usersPackage.GeneralUserRepository;

import mainPackage.usersPackage.PostingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;




@Controller
@ServerEndpoint(value = "/auction/{associatedPostID}/{username}")
public class Auction {
    //manually allocating causes the error


    private static GeneralUserRepository generalUserRepository;

    @Autowired
    public void setGeneralUserRepository(GeneralUserRepository repo) {
        generalUserRepository = repo;  // we are setting the static variable
    }

    private static PostingRepository postingRepository;

    @Autowired
    public void setPostingRepository(GeneralUserRepository repo) {
        generalUserRepository = repo;  // we are setting the static variable
    }

    private static AuctionTableRepository auctionTableRepository;

    @Autowired
    public void setAuctionTableRepository(AuctionTableRepository repo) {
        auctionTableRepository= repo;  // we are setting the static variable
    }

    //only stores the current "live" session & user pair
    private static HashMap< Session, String > usernameFromSession = new HashMap<>();

    //only stores the current "live" session & user pair
    private static HashMap <String, Session> sessionFromUsername = new HashMap<>();
    private static HashMap < String, HashMap<String, Boolean>> participatingAuctionsFromUsername = new HashMap<>();

    private static HashMap <String, HashMap<String, Boolean>> participatingUsersFromAuctionID = new HashMap<>();

    private static HashMap<Session, String> auctionIDFromSession = new HashMap<>();


    private final Logger logger = LoggerFactory.getLogger(Auction.class);




    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("associatedPostID") String auctionID) throws IOException {
        if(username == null || username == ""
                && generalUserRepository.findGeneralUserByUserName(username) == null
                || auctionTableRepository.getAuctionTableById(Integer.parseInt(auctionID)) == null){

            session.close();
            return;
        }

        logger.info("[onOpen] Auction associatedPostID: " + auctionID + " user joined: " + username);


        //user is trying to participate in an auction, but did not close the previous session properly
        if (sessionFromUsername.containsKey(username)) {
            //case1: user is trying to join another auction
            //Normally, this would not be the case, as the app should have properly sent a signal to
            //close the previous session before joining a new one.
            //Thus, in this case, we should remove the user from the hashmap to reset the session

            usernameFromSession.remove(sessionFromUsername.get(username));
            sessionFromUsername.get(username).getBasicRemote().sendText("Closed session");
            sessionFromUsername.get(username).close();
        }

        //put current session that the user just joined
        sessionFromUsername.put(username,session);

        // map current session with username
        usernameFromSession.put(session, username);

        //add the auction to the user's participation map
        if(!participatingAuctionsFromUsername.containsKey(username)){
            participatingAuctionsFromUsername.put(username,new HashMap<String,Boolean>());
        }

        participatingAuctionsFromUsername.get(username).put(auctionID,true);

        //this should have been in the DB from the moment seller posts
        if(!participatingUsersFromAuctionID.containsKey(auctionID)){
            participatingUsersFromAuctionID.put(auctionID, new HashMap<String,Boolean>());
        }

        participatingUsersFromAuctionID.get(auctionID).put(username,true);


        // send to the user that they joined the auction (from DB)
        sendMessageToPArticularUser(username, "user connected: "+username);

        // send to everyone in the chat
        broadcast("User: " + username + " has Joined the Auction");


        //we need to add new auction, as the user is trying to join a new auction
        GeneralUser user = generalUserRepository.findGeneralUserByUserName(username);
        Set<AuctionTable> participatingAuctions = user.getConnectedSessions();



        AuctionTable auction = auctionTableRepository.getAuctionTableById(Integer.parseInt(auctionID.trim()));
        if(auction != null){
            participatingAuctions.add(auction);
            auction.getConnectedUsers().add(user);
            auctionTableRepository.save(auction);
            generalUserRepository.save(user);
            String m = auction.getBidHistory();
            if(m != null && m != ""){
                System.out.println(m);
                String[] split_msg =  m.split("\\s+");

                for(int i = 0; i < split_msg.length; i++){
                    int splitPoint = split_msg[0].indexOf("-");
                    String msg = split_msg[i].substring(0,splitPoint) + " bid $ " +  split_msg[i].substring(splitPoint+1);
                    sendMessageToPArticularUser(username,msg);
                }
            }
        }
        //
        auctionIDFromSession.put(session,auctionID);





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
        String auctionId = auctionIDFromSession.get(session);

        // server side log


        // Direct message to
        // split by space
        String[] split_msg =  message.split("\\s+");

        //first text should be a number

        int bid = Integer.parseInt(split_msg[0]);
        if(auctionId != "" && auctionId != null){
            AuctionTable a = auctionTableRepository.getAuctionTableById(Integer.parseInt(auctionId));
            if( bid > a.getHighestBidAmount()){
                a.setHighestBidder(generalUserRepository.findGeneralUserByUserName(username));
                a.setHighestBidAmount(bid);
                String msg = username + " bid $" + bid + ":";
                a.setBidHistory(a.getBidHistory()+username+"-"+bid+" ");
                auctionTableRepository.save(a);
                broadcast(username + " bid $" + bid);
            }
        }



        //logger.info("[onMessage] bid placed from: " + username + "\"" + message + "\" to: " + split_msg[0]);

        //sendMessageToPArticularUser(split_msg[0], "[DM from " + username + "]: " + actualMessage);
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
