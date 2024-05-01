package com.example.androidexample;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.androidexample.Post.PostAdapter;

//import com.example.androidexample.Manifest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * The create post activity makes the user to be able to post items based off of the given information.
 */
public class EditPostActivity extends AppCompatActivity{
    private EditText titleEditText;
    private ImageButton addImageBtn;
    private int price;
    private EditText descriptionEditText;
    private EditText categoryEditTxt;
    private Button cancelBtn;

    private Button postBtn;
    private String TAG = CreatePostActivity.class.getSimpleName();
    //    private EditText categoryEditTxt;
    private HorizontalScrollView imagesHorizontalScrollView;
    private Uri pic;
    private ImageView image1 = null;
    private ImageView image2 = null;
    private ImageView image3 = null;
    private ImageView image4 = null;
    private ImageView image5 = null;
    private ImageView image6 = null;
    private String titleTxt;
//    private String id;

    private int imageIndex = 0;
    private Boolean createPostSuccess;

    private volatile String title;
    private String description;
    private volatile String usernameString;
    private int id;

    private Bitmap bitmap;
    private String filePath;
    TextView textView;
    private  int userType = 0;

    private EditText getCategoryEditTxt;
    private Boolean auction;

    private ActivityResultLauncher<String> mGetContent;

    Uri selectiedUri;

    private String encodedString;
    //ArrayList<Bitmap> imageBitMaps = new ArrayList<>();

    private EditText priceEditTxt;
//    private String encodedString;

    private volatile Bitmap[] bitmap1to6 = new Bitmap[6];

    private boolean isAuction;

    public volatile JSONObject ret = new JSONObject();

    private static int ImageUploadedCounter = 0;
    private Boolean serverResponse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);            // link to Login activity XML

        getDataJSONObjectReq();
        /* initialize UI elements */
        //Text
        titleEditText = findViewById(R.id.titleEditTxt);
        titleEditText.setSaveEnabled(true);
        descriptionEditText = findViewById(R.id.DescriptionEditText);
        descriptionEditText.setSaveEnabled(true);
//        getCategoryEditTxt = findViewById(R.id.CategoryEditTxt);
//        image1 = findViewById(R.id.imageSelView1);
//
//
//        image2 = findViewById(R.id.imageSelView2);
//
//
//        image3 = findViewById(R.id.imageSelView3);
//
//
//        image4 = findViewById(R.id.imageSelView4);
//
//
//        image5 = findViewById(R.id.imageSelView5);
//
//
//        image6 = findViewById(R.id.imageSelView6);

        priceEditTxt = findViewById(R.id.priceEditTxt);

        Toolbar t = (Toolbar)findViewById(R.id.vwebtoolbar1);

        t.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent(EditPostActivity.this, PostDetailActivity.class);
                startActivity(intent);
            }
        });

        //Buttons
        postBtn = findViewById(R.id.post_button);  // link to signup button in the Login activity XML
//        addImageBtn = findViewById(R.id.addImageButton); //link to add images

        // select image from gallery
//        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
//                uri -> {
//                    // Handle the returned Uri
//                    Log.d("URI", "Received URI: " + uri);
//                    selectiedUri = uri;
//                    if (uri != null) {
//                        if (imageIndex >= 0 && imageIndex < 6) {
//                            switch (imageIndex) {
//                                case 0:
//                                    image1 = findViewById(R.id.imageSelView1);
//                                    image1.setImageURI(uri);
//                                    image1.setDrawingCacheEnabled(true);
//                                    image1.layout(0, 0, 150, 150);
//                                    image1.buildDrawingCache(true);
//                                    bitmap1to6[0] = Bitmap.createBitmap(image1.getDrawingCache());
//                                    image1.setDrawingCacheEnabled(false); // clear drawing cache
//
//                                    break;
//                                case 1:
//                                    image2 = findViewById(R.id.imageSelView2);
//                                    image2.setImageURI(uri);
//                                    image2.setDrawingCacheEnabled(true);
//                                    image2.layout(0, 0, 150, 150);
//                                    image2.buildDrawingCache(true);
//                                    bitmap1to6[1] = Bitmap.createBitmap(image2.getDrawingCache());
//                                    image2.setDrawingCacheEnabled(false); // clear drawing cache
//                                    break;
//                                case 2:
//                                    image3 = findViewById(R.id.imageSelView3);
//                                    image3.setImageURI(uri);
//                                    image3.setDrawingCacheEnabled(true);
//                                    image3.layout(0, 0, 150, 150);
//                                    image3.buildDrawingCache(true);
//                                    bitmap1to6[2] = Bitmap.createBitmap(image3.getDrawingCache());
//                                    image3.setDrawingCacheEnabled(false); // clear drawing cache
//                                    break;
//                                case 3:
//                                    image4 = findViewById(R.id.imageSelView4);
//                                    image4.setImageURI(uri);
//                                    image4.setDrawingCacheEnabled(true);
//                                    image4.layout(0, 0, 150, 150);
//                                    image4.buildDrawingCache(true);
//                                    bitmap1to6[3] = Bitmap.createBitmap(image4.getDrawingCache());
//                                    image4.setDrawingCacheEnabled(false); // clear drawing cache
//                                    break;
//                                case 4:
//                                    image5 = findViewById(R.id.imageSelView5);
//                                    image5.setImageURI(uri);
//                                    image5.setDrawingCacheEnabled(true);
//                                    image5.layout(0, 0, 150, 150);
//                                    image5.buildDrawingCache(true);
//                                    bitmap1to6[4] = Bitmap.createBitmap(image5.getDrawingCache());
//                                    image5.setDrawingCacheEnabled(false); // clear drawing cache
//                                    break;
//                                case 5:
//                                    image6 = findViewById(R.id.imageSelView6);
//                                    image6.setImageURI(uri);
//                                    image6.setDrawingCacheEnabled(true);
//                                    image6.layout(0, 0, 150, 150);
//                                    image6.buildDrawingCache(true);
//                                    bitmap1to6[5] = Bitmap.createBitmap(image6.getDrawingCache());
//                                    image6.setDrawingCacheEnabled(false); // clear drawing cache
//                                    break;
//                            }
//
//                            imageIndex++;
//                        }else{
//                            Toast.makeText(editPostActivity.this, "Unable to add more than 6 pictures", Toast.LENGTH_LONG).show();
//                        }
//
//
//                    }
//                });


        /*
         *  click listener on post button pressed
         */
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savePost();
                Intent intent = new Intent(EditPostActivity.this, MainFeed.class);
                startActivity(intent);

            }
        });





        /*
         * click listener for adding an image
         */
//        addImageBtn.setOnClickListener(v -> mGetContent.launch("image/*"));

        /*
         * click listener for uploading the post
         */
//        postBtn.setOnClickListener(v -> uploadImage());


        /* click listener on login button pressed */
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }



    private String convertBitmapToBase64(Bitmap b) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b = Bitmap.createScaledBitmap(b,150,150,false);
        b.compress(Bitmap.CompressFormat.PNG, 70, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.getEncoder().encodeToString(byteArray);
    }


    private void sendImageToServer(int postId, int imageIndexStartFrom1){
        JSONObject jo = new JSONObject();
        try {


            jo.put("id",postId);
            jo.put("title", title);
            jo.put("userName", usernameString);
            jo.put("picture"+imageIndexStartFrom1, convertBitmapToBase64(bitmap1to6[imageIndexStartFrom1-1]));
            System.out.println("ABOUT TO SEND " + jo.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, "http://coms-309-060.class.las.iastate.edu:8080/posts/update", jo , response -> {
            Log.d(TAG, response.toString());
            try {
                System.out.println("RECEIVED FROM UPDATE");
                System.out.println("SENT THIS: " + jo.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            if (createPostSuccess) {
                Toast.makeText(EditPostActivity.this, "Post is successful!", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(CreatePostActivity.this, MainFeed.class);


                //startActivity(intent);
            }else{
                Toast.makeText(EditPostActivity.this, "Post unsuccessful.", Toast.LENGTH_LONG).show();
            }





        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            Toast.makeText(EditPostActivity.this, "Post unsuccessful.", Toast.LENGTH_LONG).show();
//            txtValidity = true;
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }

        };

        //queue.add(jsonObjReq);
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
        //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().start();

    }
    /**
     * Send a JSON Object request to server that posts the data for a new post.
     * This method constructs a JSON Object containing post data and sends it to the server
     * using Volley library's JsonObjectRequest. Upon a successful post, the user is redirected
     * to the main feed activity. Upon an unsuccessful post, the user stays on that screen and
     * Toast outputs "Post unsuccessful."
     */
    private void getDataJSONObjectReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                Const.URL_GET_ALL_POSTS + "/" + PostDetailActivity.pid,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            titleTxt = response.getString("title");
                            price = response.getInt("price");
                            auction = response.getBoolean("isAuction");
                            description = response.getString("description");
                            usernameString = response.getString("userName");
                            id = response.getInt("id");



                            Log.d("This is the title:", titleTxt);
                            titleEditText.setText(titleTxt, TextView.BufferType.EDITABLE);
                            Log.d("This is the price:", String.valueOf(price));
                            priceEditTxt.setText(String.valueOf(price), TextView.BufferType.EDITABLE);
                            Log.d("This is the description:", description);
                            descriptionEditText.setText(description, TextView.BufferType.EDITABLE);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    /**
     * Send a JSON Object request to server that posts the data for a new post.
     * This method constructs a JSON Object containing post data and sends it to the server
     * using Volley library's JsonObjectRequest. Upon a successful post, the user is redirected
     * to the main feed activity. Upon an unsuccessful post, the user stays on that screen and
     * Toast outputs "Post unsuccessful."
     */
    private void savePost() {
        JSONObject jsonObject = new JSONObject();
        // JSONObject ret = new JSONObject();
        try {
            //input your API parameters

            jsonObject.put("title", titleEditText.getText().toString());
            System.out.println("THE TITLE WAS " +titleEditText.getText().toString());
            jsonObject.put("description", descriptionEditText.getText().toString());
            System.out.println("THE DESCRIPTION WAS " +descriptionEditText.getText().toString());
            jsonObject.put("price", Integer.parseInt(priceEditTxt.getText().toString()));
            System.out.println("THE PRICE WAS " +Integer.parseInt(priceEditTxt.getText().toString()));
            jsonObject.put("id",PostDetailActivity.pid);
            System.out.println("THE userName WAS " + LoginActivity.username);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_UPDATE_POST, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                Log.d("Volley Response", response.toString());
//                try {
//                    serverResponse = response.getBoolean("serverResponse");

//                    for (int i = 0; i < MainFeed.mPostList.size(); i++){
//                        if (MainFeed.mPostList.get(i).getPostID() == PostDetailActivity.pid){
//                            MainFeed.mPostList.remove(i);
//                            break;
//                        }
//                    }



                    ((PostAdapter) MainFeed.mPostAdapter).updatePosts(MainFeed.mPostList);

//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }


    /**
     * Converts the given image URI to a byte array.
     *
     * This method takes a URI pointing to an image and converts it into a byte array. The conversion
     * involves opening an InputStream from the content resolver using the provided URI, and then
     * reading the content into a byte array. This byte array represents the binary data of the image,
     * which can be used for various purposes such as uploading the image to a server.
     *
     * @param imageUri The URI of the image to be converted. This should be a content URI that points
     *                 to an image resource accessible through the content resolver.
     * @return A byte array representing the image data, or null if the conversion fails.
     * @throws IOException If an I/O error occurs while reading from the InputStream.
     */
    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}