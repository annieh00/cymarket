package mainPackage.postService;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.websocket.server.PathParam;
import mainPackage.errorMsg.ErrorMsg;
import mainPackage.usersPackage.*;
import mainPackage.websocket.AuctionTable;
import mainPackage.websocket.AuctionTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Junhyung Shim
 * This controller will handle the post data for login
 * for post data it will create a user to db
 * */
@RestController
public class SellPostController {

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private GeneralUserRepository generalUserRepository;

    private static AuctionTableRepository auctionTableRepository;

    @Autowired
    public void setAuctionTableRepository(AuctionTableRepository repo) {
        auctionTableRepository= repo;  // we are setting the static variable
    }
    //create
    @Operation(summary = "create post in DB", description = "creates a post (listing) in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post successfully got stored into DB", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "The required fields aren't filled out or the author does not exist")
    })
    @PostMapping("/posts")
    public String createPost(@RequestBody Posting p){
        GeneralUser u2 = generalUserRepository.findGeneralUserByUserName(p.getUserName());
        if(u2 == null){
            //System.out.println("user "+p.getUserName()+ " does not exist");
            ErrorMsg e = new ErrorMsg();
            e.setErrormsg("user does not exist, and therefore cannot create post");
            return "{\"serverResponse\" : false}";
        }

        postingRepository.save(p);
        u2.getPublishedPosts().add(p);
        generalUserRepository.save(u2);

        if(p.getIsAuction()){//this post is an auction
            GeneralUser u = generalUserRepository.findGeneralUserByUserName(p.getUserName());
            AuctionTable auction = new AuctionTable();
            auction.setPost(p);
            auction.setHighestBidder(u); //no one has placed a bid yet
            auction.setId(u.getUserName()+p.getTitle());
            if(p.getTimeAliveInMinutes() == 0){
                p.setTimeAliveInMinutes(5);
            }
            auctionTableRepository.save(auction);
        }

        return "{\"serverResponse\" : true}";
    }


    //Read/list
    @Operation(summary = "get all posts in DB", description = "gets all posts in DB, only for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned a JSON array of posts", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Access denied, not admin")
    })
    @GetMapping("/getAllPosts")
    public String getPosts(){
        ArrayList<Posting> mylist = postingRepository.findAll();
        String json = new Gson().toJson(mylist);
        return "{ \"posts\" :" +json + "}";
    }


    private void updatePost(Posting db, Posting userRequest){
        if(userRequest.getUserName() != null){
            db.setUserName(userRequest.getUserName());
        }

        if(userRequest.getDescription() != null){
            db.setDescription(userRequest.getDescription());
        }

        if(userRequest.getPicture1() != null){
            db.setPicture1(userRequest.getPicture1());
        }
        if(userRequest.getPicture2() != null){
            db.setPicture2(userRequest.getPicture2());
        }

        if(userRequest.getPicture3() != null){
            db.setPicture3(userRequest.getPicture3());
        }


    }

    @Operation(summary = "update post in DB", description = "updates a post (listing) in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post successfully got updated", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Post does not exist in DB")
    })
    //update
    @PostMapping("/posts/update")
    public String updatePost(@RequestBody Posting editpost){

        Posting p = postingRepository.findPostingById(editpost.getId());

        if(p == null){
            return "{\"serverResponse\" : false}";
        }
        //updatePost(p,update);
        p.setTitle(editpost.getTitle());
        Posting p2 = p;
        postingRepository.save(p2);
        return "{\"serverResponse\" : true}";
    }

    @Operation(summary = "delete a post in DB", description = "deletes a post (listing) in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post successfully got deleted", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Post does not exist in DB")
    })
    @PostMapping("/posts/delete")
    public String deletePost(@RequestBody Posting delete){
        Posting p = postingRepository.findPostingById(delete.getId());
        if(p==null){
            return "{ \"serverResponse\" : false}";
        }
        AuctionTable a = auctionTableRepository.getAuctionTableByPost(p);
        if(a != null){
            auctionTableRepository.delete(a);
        }
        postingRepository.delete(p);
        return  "{ \"serverResponse\" : true}";
    }
}
//testing


