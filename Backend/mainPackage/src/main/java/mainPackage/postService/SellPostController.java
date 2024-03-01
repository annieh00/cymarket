package mainPackage.postService;

import com.google.gson.Gson;
import mainPackage.errorMsg.ErrorMsg;
import mainPackage.usersPackage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private Post2UserMappingRepository post2UserMappingRepository;
    //DO NOT DROP sequence table in db

    @Autowired
    private GeneralUserRepository generalUserRepository;


    //create
    @PostMapping("/posts")
    public String createPost(@RequestBody Posting p){
//        if(generalUserRepository.findGeneralUserByUserName(p.getUserName()) == null){
//            //System.out.println("user "+p.getUserName()+ " does not exist");
//            ErrorMsg e = new ErrorMsg();
//            e.setErrormsg("user does not exist, and therefore cannot create post");
//            return e;
//        }
        postingRepository.save(p);
        //Post2UserMapping p2u = new Post2UserMapping();
        //p2u.setPid(p.getId());
        //p2u.setUid(p.getUserName());
        //post2UserMappingRepository.save(p2u);
        return "{\"serverResponse\" : true}";
    }


    //Read/list
    @GetMapping("/posts")
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
    @PostMapping("posts/update")
    public Posting updatePost(@RequestBody Posting update){
        Posting p = postingRepository.findPostingById(update.getId());
        if(p == null)return null;
        updatePost(p,update);
        postingRepository.save(p);
        return p;
    }

    @PostMapping("posts/del/{pid}")
    public String deletePost(@PathVariable(name = "pid") int pid){
        Posting p = postingRepository.findPostingById(pid);
        if(p==null)return "post does not exist";
        String msg = "Successfully deleted"+ p.getDescription() + "written by: " + p.getUserName();
        postingRepository.delete(p);
        return  msg;
    }
}
