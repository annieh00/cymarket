package mainPackage.postService;

import com.google.gson.Gson;
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


