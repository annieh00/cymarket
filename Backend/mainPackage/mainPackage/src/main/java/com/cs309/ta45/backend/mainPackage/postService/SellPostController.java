package com.cs309.ta45.backend.mainPackage.postService;

import com.cs309.ta45.backend.mainPackage.dbmsPackage.Connect2DBHibernate;
import com.cs309.ta45.backend.mainPackage.dbmsPackage.ConnectToDB;
import com.cs309.ta45.backend.mainPackage.usersPackage.GeneralUser;
import com.cs309.ta45.backend.mainPackage.usersPackage.GeneralUserRepository;
import com.cs309.ta45.backend.mainPackage.usersPackage.Posting;
import com.cs309.ta45.backend.mainPackage.usersPackage.PostingRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Junhyung Shim
 * This controller will handle the post data for login
 * for post data it will create a user to db
 * */
@RestController
public class SellPostController {

    @Autowired
    private PostingRepository postingRepository;
    //DO NOT DROP sequence table in db

    //create
    @PostMapping("/posts")
    public Posting createUser(@RequestBody Posting p){
        postingRepository.save(p);
        return p;
    }


    //Read/list
    @GetMapping("/posts/{author}")
    public ArrayList<Posting> getPosts(@PathVariable(name = "author") String author){
        return postingRepository.findPostingByAuthor(author);
    }

    private void updatePost(Posting db, Posting userRequest){
        if(userRequest.getAuthor() != null){
            db.setAuthor(userRequest.getAuthor());
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
    @PostMapping("posts/update/{pid}")
    public Posting updatePost(@PathVariable(name = "pid") int pid, @RequestBody Posting update){
        Posting p = postingRepository.findPostingById(pid);
        if(p == null)return null;
        updatePost(p,update);
        postingRepository.save(p);
        return p;
    }

    @PostMapping("posts/del/{pid}")
    public String deletePost(@PathVariable(name = "pid") int pid){
        Posting p = postingRepository.findPostingById(pid);
        if(p==null)return "post does not exist";
        String msg = "Successfully deleted"+ p.getDescription() + "written by: " + p.getAuthor();
        postingRepository.delete(p);
        return  msg;
    }
}
