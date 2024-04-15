package mainPackage.postService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
        p.setId(0); //explicitly setting the ID by the user is not allowed, setting it to 0 tells the JPA to auto-configure it
        GeneralUser u2 = generalUserRepository.findGeneralUserByUserName(p.getUserName());
        if(u2 == null){
            //System.out.println("user "+p.getUserName()+ " does not exist");
            ErrorMsg e = new ErrorMsg();
            System.out.println("NO USER FOUND");
            e.setErrormsg("user does not exist, and therefore cannot create post");
            return "{\"serverResponse\" : \"No User Found\"}";
        }

        Posting p2 = postingRepository.findPostingByTitle(p.getTitle());
        if(p2 != null && p2.getUserName().equals(p.getUserName())){

            return "{\"serverResponse\" : \"duplicate entry\"}";
        }





        p.setDate((new java.util.Date()).toString());
        postingRepository.save(p);
        u2.getPublishedPosts().add(p);
        generalUserRepository.save(u2);

        if(p.getIsAuction()){//this post is an auction
            GeneralUser u = generalUserRepository.findGeneralUserByUserName(p.getUserName());
            AuctionTable auction = new AuctionTable();
            auction.setPost(p);
            auction.setHighestBidder(u); //no one has placed a bid yet
            auction.setHighestBidAmount(p.getPrice());
            //ArrayList<Posting> pa = postingRepository.findPostingsByTitle(p.getTitle());

            auction.setId(p.getId());
//            if(p.getTimeAliveInMinutes() == 0){
//                p.setTimeAliveInMinutes(5);
//            }
            auctionTableRepository.save(auction);
        }



        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();

        String json = gson.toJson(p);
        return json;
    }


    //Read/list
    @Operation(summary = "get all posts in DB", description = "gets all posts in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned a JSON array of posts", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "bad request")
    })
    @GetMapping("/getAllPosts")
    public String getPosts(){
        List<Posting> mylist = postingRepository.findAll();
        ArrayList<Posting> ret = new ArrayList<>();

        try {
            for(int i = 0; i < mylist.size(); i++){
                //System.out.println(i);
                Posting p = getPicturePaths(mylist.get(i));
                if(p != null && !p.getIsAuction()){
                    ret.add(p);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();
        String json = gson.toJson(ret);
        return "{ \"posts\" :" +json + "}";
    }


    @Operation(summary = "get all donations in DB", description = "gets all donations in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned a JSON array of donations", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "bad request")
    })
    @GetMapping("/getAllDonations")
    public String getDonations(){
        List<Posting> mylist = postingRepository.findAll();
        ArrayList<Posting> ret = new ArrayList<>();

        try {
            for(int i = 0; i < mylist.size(); i++){
                //System.out.println(i);
                Posting p = getPicturePaths(mylist.get(i));
                if(p != null && p.getIsDonation()){
                    ret.add(p);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();
        String json = gson.toJson(ret);
        return "{ \"donations\" :" +json + "}";
    }

    @Operation(summary = "gets specific post in DB", description = "gets specific post in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned a JSON Object regarding a specific post", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "bad request")
    })
    @GetMapping("/getAllPosts/{id}")
    public String getSpecificPost(@PathVariable String id){
        Posting p = postingRepository.findPostingById(Integer.parseInt(id));
        if(p != null){
            //p = getPictures(p);
            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();
            Gson gson = builder.setPrettyPrinting().create();
            String json = gson.toJson(p);
            return json;
        }
        return null;
    }

    @Operation(summary = "gets all posts marked as auctions", description = "gets auctions in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned a JSON Array of auctions", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "bad request")
    })
    @GetMapping("/auctions")
    public String getAuctions(){
        List<Posting> mylist = postingRepository.findAll();
        ArrayList<Posting> ret = new ArrayList<>();

        try {
            for(int i = 0; i < mylist.size(); i++){
                if(mylist.get(i).getIsAuction() && !mylist.get(i).getIsClosed()){
                    ret.add(mylist.get(i));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();
        String json = gson.toJson(ret);
        return "{ \"auctions\" :" +json + "}";

    }

    @Operation(summary = "returns byte array for the specified image", description = "gets the specified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "image successfully retreived", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "image not found")
    })
    @GetMapping(value = "/image/{postId}/{imageIndex}")
    public String getImage(@PathVariable String postId, @PathVariable String imageIndex) throws IOException {
        System.out.println("getImage Called! to postID:" + postId + " imageIndex: " + imageIndex);
        Posting p = postingRepository.findPostingById(Integer.parseInt(postId));
        String imgName = "";

        int index = Integer.parseInt(imageIndex);
        System.out.println("IMAGE NAME FOR PIC1 " + p.getPicture1());
        if(p != null){
            switch (index){
                case 1:
                    imgName = p.getPicture1();
                    break;
                case 2:
                    imgName = p.getPicture2();
                    break;
                case 3:
                    imgName = p.getPicture3();
                    break;
                case 4:
                    imgName = p.getPicture4();
                    break;

                case 5:
                    imgName = p.getPicture5();
                    break;

                case 6:
                    imgName = p.getPicture6();
                    break;
            }
            System.out.println("IMAGE NAME IS " + imgName);

            if(imgName != null){
                File initialFile = new File("./"+imgName);
                byte[] fileContent = FileUtils.readFileToByteArray(initialFile);
                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                return "{\"image\" : \"" + encodedString +"\"}";
                //InputStream in = new FileInputStream(initialFile);
                //return IOUtils.toByteArray(in);
            }

        }

        return "{\"image\" : \"\"}";

    }



    private Posting setPictures(Posting p){
        System.out.println("setPictures were called!");
        GeneralUser u2 = generalUserRepository.findGeneralUserByUserName(p.getUserName());
        if(u2 == null){
            return null;
        }



        try {
            System.out.println(p.getPicture1());
            nu.pattern.OpenCV.loadLocally();
            String img1 = p.getPicture1();
            if(img1 != null && !img1.equals("")){
                String fileName = "./" +  p.getUserName() + p.getTitle()+"Pic1.png";
                byte[] decoded = Base64.getDecoder().decode(p.getPicture1());
                p.setPicture1(p.getUserName() + p.getTitle()+"Pic1.png");
                FileUtils.writeByteArrayToFile(new File(fileName), decoded);
                System.out.println("PRINTED FIRST IMAGE");
            }else{
                //p.setPicture1("");
            }

            String img2 = p.getPicture2();
            if((img2 != null) && !img2.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic2.png";
                byte[] decoded = Base64.getDecoder().decode(p.getPicture2());
                p.setPicture2(p.getUserName() + p.getTitle()+"Pic2.png");
                FileUtils.writeByteArrayToFile(new File(fileName), decoded);
            }else{
                //p.setPicture2("");
            }

            String img3 = p.getPicture3();
            if((img3 != null) && !img3.equals("") ){
                String fileName ="./"+ p.getUserName() + p.getTitle()+"Pic3.png";
                byte[] decoded = Base64.getDecoder().decode(p.getPicture3());
                p.setPicture3(p.getUserName() + p.getTitle()+"Pic3.png");
                FileUtils.writeByteArrayToFile(new File(fileName), decoded);
            }else{
                //p.setPicture3("");
            }

            String img4 = p.getPicture4();
            if((img4 != null) && !img4.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic4.png";
                byte[] decoded = Base64.getDecoder().decode(p.getPicture4());
                p.setPicture4(p.getUserName() + p.getTitle()+"Pic4.png");
                FileUtils.writeByteArrayToFile(new File(fileName), decoded);
            }else{
                //p.setPicture4("");
            }

            String img5 = p.getPicture5();
            if((img5 != null) && !img5.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic5.png";
                byte[] decoded = Base64.getDecoder().decode(p.getPicture5());
                p.setPicture5(p.getUserName() + p.getTitle()+"Pic5.png");
                FileUtils.writeByteArrayToFile(new File(fileName), decoded);
            }else{
                //p.setPicture5("");
            }

            String img6 = p.getPicture6();
            if((img6 != null) && !img6.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic6.png";
                byte[] decoded = Base64.getDecoder().decode(p.getPicture6());
                p.setPicture6(p.getUserName() + p.getTitle()+"Pic6.png");
                FileUtils.writeByteArrayToFile(new File(fileName), decoded);
            }else{
                //p.setPicture6("");
            }

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }


        return p;
    }

    private Posting getPictures(Posting p){
        GeneralUser u2 = generalUserRepository.findGeneralUserByUserName(p.getUserName());
        if(u2 == null){
            u2 = generalUserRepository.findById(p.getId());
            if(u2 == null){
                return null;
            }
        }



        try {
            nu.pattern.OpenCV.loadLocally();


            String img1 = p.getPicture1();
            if((img1 != null) && !img1.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic1.png";
                File f = new File(fileName);
                if(f.exists()){
                    byte[] fileContent = FileUtils.readFileToByteArray(f);
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    p.setPicture1(encodedString);
                }else{
                    p.setPicture1("");
                }
            }else{
                p.setPicture1("");
            }

            String img2 = p.getPicture2();
            if((img2 != null) && !img2.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic2.png";
                File f = new File(fileName);
                if(f.exists()){
                    byte[] fileContent = FileUtils.readFileToByteArray(f);
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    p.setPicture2(encodedString);
                }else{
                    p.setPicture2("");
                }
            }else{
                p.setPicture2("");
            }

            String img3 = p.getPicture3();
            if((img3 != null) && !img3.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic3.png";
                File f = new File(fileName);
                if(f.exists()){
                    byte[] fileContent = FileUtils.readFileToByteArray(f);
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    p.setPicture3(encodedString);
                }else{
                    p.setPicture3("");
                }
            }else{
                p.setPicture3("");
            }

            String img4 = p.getPicture4();
            if((img4 != null) && !img4.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic4.png";
                File f = new File(fileName);
                if(f.exists()){
                    byte[] fileContent = FileUtils.readFileToByteArray(f);
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    p.setPicture4(encodedString);
                }else{
                    p.setPicture4("");
                }
            }else{
                p.setPicture4("");
            }

            String img5 = p.getPicture5();
            if((img5 != null) && !img5.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic5.png";
                File f = new File(fileName);
                if(f.exists()){
                    byte[] fileContent = FileUtils.readFileToByteArray(f);
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    p.setPicture5(encodedString);
                }else{
                    p.setPicture5("");
                }
            }else{
                p.setPicture5("");
            }

            String img6 = p.getPicture6();
            if((img6 != null) && !img6.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic6.png";
                File f = new File(fileName);
                if(f.exists()){
                    byte[] fileContent = FileUtils.readFileToByteArray(f);
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    p.setPicture6(encodedString);
                }else{
                    p.setPicture6("");
                }
            }else{
                p.setPicture6("");
            }

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }


        return p;
    }


    private Posting getPicturePaths(Posting p){
        GeneralUser u2 = generalUserRepository.findGeneralUserByUserName(p.getUserName());
        if(u2 == null){
            return null;
        }

        try {
            nu.pattern.OpenCV.loadLocally();
            String img1 = p.getPicture1();
            if((img1 != null) && !img1.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic1.png";
                File f = new File(fileName);
                if(f.exists()){
                    p.setPicture1(p.getUserName() + p.getTitle()+"Pic1.png");
                }else{
                    p.setPicture1("");
                }
            }else{
                p.setPicture1("");
            }

            String img2 = p.getPicture2();
            if((img2 != null) && !img2.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic2.png";
                File f = new File(fileName);
                if(f.exists()){
                    p.setPicture1(p.getUserName() + p.getTitle()+"Pic2.png");
                }else{
                    p.setPicture2("");
                }
            }else{
                p.setPicture2("");
            }

            String img3 = p.getPicture3();
            if((img3 != null) && !img3.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic3.png";
                File f = new File(fileName);
                if(f.exists()){
                    p.setPicture1(p.getUserName() + p.getTitle()+"Pic3.png");
                }else{
                    p.setPicture3("");
                }
            }else{
                p.setPicture3("");
            }

            String img4 = p.getPicture4();
            if((img4 != null) && !img4.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic4.png";
                File f = new File(fileName);
                if(f.exists()){
                    p.setPicture1(p.getUserName() + p.getTitle()+"Pic4.png");
                }else{
                    p.setPicture4("");
                }
            }else{
                p.setPicture4("");
            }

            String img5 = p.getPicture5();
            if((img5 != null) && !img5.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic5.png";
                File f = new File(fileName);
                if(f.exists()){
                    p.setPicture1(p.getUserName() + p.getTitle()+"Pic5.png");
                }else{
                    p.setPicture5("");
                }
            }else{
                p.setPicture5("");
            }

            String img6 = p.getPicture6();
            if((img6 != null) && !img6.equals("") ){
                String fileName = "./"+p.getUserName() + p.getTitle()+"Pic6.png";
                File f = new File(fileName);
                if(f.exists()){
                    p.setPicture1(p.getUserName() + p.getTitle()+"Pic6.png");
                }else{
                    p.setPicture6("");
                }
            }else{
                p.setPicture6("");
            }

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }


        return p;
    }

    @Operation(summary = "updates a post in DB", description = "updates a post in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post successfully got updated", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Post does not exist in DB")
    })
    @PostMapping("/posts/update")
    public String updatePost(@RequestBody Posting editpost){

        Posting p = postingRepository.findPostingById(editpost.getId());

        if(p == null){
            return "{\"serverResponse\" : 0}";
        }

        try {
            p.setPicture1(editpost.getPicture1());
            p.setPicture2(editpost.getPicture2());
            p.setPicture3(editpost.getPicture3());
            p.setPicture4(editpost.getPicture4());
            p.setPicture5(editpost.getPicture5());
            p.setPicture6(editpost.getPicture6());
            setPictures(p);

        }catch (Exception e){
            e.printStackTrace();
        }
        //p.setTitle(editpost.getTitle());
        //p.setDescription(editpost.getDescription());
        postingRepository.save(p);

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();
        String json = gson.toJson(p);
        System.out.println("saving: " + json);
        return json;
    }
    @Operation(summary = "fetches the user's published posts", description = "fetches the user's published posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "fetch successful", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Post does not exist in DB")
    })
    @GetMapping("/getSpecificPosts/{userName}")
    public String getMyPosts(@PathVariable String userName){
        ArrayList<Posting> myposts = postingRepository.findPostingByUserName(userName);
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();
        String json = gson.toJson(myposts);
        return "{ \"posts\" :" +json + "}";
    }

    @Operation(summary = "delete a post in DB", description = "deletes a post (listing) in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post successfully got deleted", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Post does not exist in DB")
    })
    @PostMapping("/posts/delete")
    public String deletePost(@RequestBody Posting delete){
        //System.out.println(delete.getUserName());
        Posting p = postingRepository.findPostingById(delete.getId());

        if(p == null){
            return "{ \"serverResponse\" : false}";
        }
        GeneralUser u = generalUserRepository.findGeneralUserByUserName(delete.getUserName());
        System.out.println(delete.getUserName());

       if(u != null ){
            Set<Posting> hs = u.getPublishedPosts();
            for(Posting p3 : hs){
                if(p3.getId() == delete.getId()){
                    hs.remove(p3);
                    break;
                }
            }
            u.setPublishedPosts(hs);
            generalUserRepository.save(u);
        }

        AuctionTable a = auctionTableRepository.getAuctionTableById(p.getId());
        if (a != null) {
            auctionTableRepository.delete(a);
        }
        postingRepository.delete(p);

        return  "{ \"serverResponse\" : true}";
    }

    @Operation(summary = "closes an auction in DB", description = "deletes a post (listing) in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post successfully got deleted", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Post does not exist in DB")
    })
    @PostMapping("/auction/close")
    public String CloseAuction(@RequestBody Posting edit){

        Posting p = postingRepository.findPostingById(edit.getId());

        if(p == null ){
            return" {\"winner\" : null}";
        }

        if(p.getIsAuction()){
            p.setIsClosed(true);
            postingRepository.save(p);
        }
        AuctionTable a = auctionTableRepository.getAuctionTableByPost(p);
        if(a.getHighestBidder() != null){
            return "{\"winner\" : \"" + a.getHighestBidder().getUserName() +"\"}";
        }else{
            return "{\"winner\" : null}";
        }

    }


}
//testing


