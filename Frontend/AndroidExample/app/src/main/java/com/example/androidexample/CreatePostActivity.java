package com.example.androidexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;
//import com.example.androidexample.Manifest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * The create post activity makes the user to be able to post items based off of the given information. 
 */
public class CreatePostActivity extends AppCompatActivity{
    private EditText titleEditText;
    private ImageButton addImageBtn;
    private EditText descriptionEditText;
    private EditText categoryEditTxt;
    private Button cancelBtn;
    private static String UPLOAD_URL = "http://10.0.2.2:8080/images";

    private Button postBtn;
    private String TAG = CreatePostActivity.class.getSimpleName();
    private EditText username;
//    private EditText categoryEditTxt;
    private HorizontalScrollView imagesHorizontalScrollView;
    private Uri pic;
    private ImageView image1 = null;
    private ImageView image2 = null;
    private ImageView image3 = null;
    private ImageView image4 = null;
    private ImageView image5 = null;
    private ImageView image6 = null;
    private String imageString1 = null;
    private String imageString2 = null;
    private String imageString3 = null;
    private String imageString4 = null;
    private String imageString5 = null;
    private String imageString6 = null;
    private Boolean createPostSuccess;

    private String title;
    private String description;
    private String usernameString;

    private Bitmap bitmap;
    private String filePath;
    TextView textView;
    private  int userType = 0;

    private EditText getCategoryEditTxt;
    private Boolean auction;
    private static final String url2 = "http://10.0.2.2/android_db_pool/fileupload.php";
    String url = "https://07537acc-da80-4457-8b10-ff9e97cbea07.mock.pstmn.io/data4";
    private ActivityResultLauncher<String> mGetContent;

    Uri selectiedUri;

    private String encodedString;
    ArrayList<Uri> images = new ArrayList<>();
    ArrayList<String> encodedImages = new ArrayList<>();

    private EditText priceEditTxt;
//    private String encodedString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);            // link to Login activity XML

        /* initialize UI elements */
        //Text
        titleEditText = findViewById(R.id.titleEditTxt);
        descriptionEditText = findViewById(R.id.DescriptionEditText);
        getCategoryEditTxt = findViewById(R.id.CategoryEditTxt);
        image1 = findViewById(R.id.imageSelView1);
        image2 = findViewById(R.id.imageSelView2);
        image3 = findViewById(R.id.imageSelView3);
        image4 = findViewById(R.id.imageSelView4);
        image5 = findViewById(R.id.imageSelView5);
        image6 = findViewById(R.id.imageSelView6);
        priceEditTxt = findViewById(R.id.priceEditTxt);

        //Buttons
        postBtn = findViewById(R.id.post_button);  // link to signup button in the Login activity XML
        addImageBtn = findViewById(R.id.addImageButton); //link to add images

        // select image from gallery
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    Log.d("URI", "Received URI: " + uri);
                    if (uri != null) {
//                        pic = uri;
                        selectiedUri = uri;
                        byte[] imageData = convertImageUriToBytes(selectiedUri);
                        encodedString = Base64.getEncoder().encodeToString(imageData);
                        images.add(uri);
                        int index = images.size() - 1;
                        if (index >= 0 && index < 6) {
                            ImageView imageView = null;
                            switch (index) {
                                case 0:
                                    encodedImages.add(encodedString);
                                    imageView = findViewById(R.id.imageSelView1);
                                    break;
                                case 1:
                                    encodedImages.add(encodedString);
                                    imageView = findViewById(R.id.imageSelView2);
                                    break;
                                case 2:
                                    encodedImages.add(encodedString);
                                    imageView = findViewById(R.id.imageSelView3);
                                    break;
                                case 3:
                                    encodedImages.add(encodedString);
                                    imageView = findViewById(R.id.imageSelView4);
                                    break;
                                case 4:
                                    encodedImages.add(encodedString);
                                    imageView = findViewById(R.id.imageSelView5);
                                    break;
                                case 5:
                                    encodedImages.add(encodedString);
                                    imageView = findViewById(R.id.imageSelView6);
                                    break;
                            }
                            if (imageView != null) {
                                imageView.setImageURI(uri);
                            }
                        }else{
                            Toast.makeText(CreatePostActivity.this, "Unable to add more than 6 pictures", Toast.LENGTH_LONG).show();
                        }


                    }
        });


        /*
        *  click listener on post button pressed
        */
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendJsonObjReq();
//                /* when post button is pressed, use intent to switch to Signup Activity */
//                Intent intent = new Intent(CreatePostActivity.this, MainFeed.class);
//                startActivity(intent);  // go to SignupActivity
//                sendJsonObjReq();

            }
        });



        /*
         * click listener for adding an image
         */
        addImageBtn.setOnClickListener(v -> mGetContent.launch("image/*"));

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



//    private String getBase64String() {
//
//        // give your image file url in mCurrentPhotoPath
//        String mCurrentPhotoPath = null;
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        // In case you want to compress your image, here it's at 40%
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
//
//        return Base64.encodeToString(byteArray, Base64.DEFAULT);
//    }
    
    /**
     * Uploads an image to a remote server using a multipart Volley request.
     *
     * This method creates and executes a multipart request using the Volley library to upload
     * an image to a predefined server endpoint. The image data is sent as a byte array and the
     * request is configured to handle multipart/form-data content type. The server is expected
     * to accept the image with a specific key ("image") in the request.
     *
     */
//    private void uploadImage(){
//
//        byte[] imageData = convertImageUriToBytes(selectiedUri);
//        String encodedString = Base64.getEncoder().encodeToString(imageData);
////        Log.d("encoded String:", encodedString);
////        MultipartRequest multipartRequest = new MultipartRequest(
////                Request.Method.POST,
////                Const.URL_IMAGES,
////                imageData,
////                response -> {
////                    // Handle response
//////                    Toast.makeText(getApplicationContext(), response,Toast.LENGTH_LONG).show();
////
////                    Log.d("Upload", "Response: " + response);
////                },
////                error -> {
////                    // Handle error
//////                    Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
////                    Log.e("Upload", "Error: " + error.getMessage());
////                }
////        );
//
////        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
//    }

//    int i;

    /**
     * Send a JSON Object request to server that posts the data for a new post.
     * This method constructs a JSON Object containing post data and sends it to the server
     * using Volley library's JsonObjectRequest. Upon a successful post, the user is redirected
     * to the main feed activity. Upon an unsuccessful post, the user stays on that screen and
     * Toast outputs "Post unsuccessful."
     */
    private void sendJsonObjReq() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            //input your API parameters
//            jsonObject.put("title", titleEditText.getText().toString());
////            jsonObject.put("category", categoryEditTxt.getText().toString());
//            jsonObject.put("description", descriptionEditText.getText().toString());
//            jsonObject.put("price", Integer.getInteger(priceEditTxt.getText().toString()));
            //boolean isAuction
//                jsonObjec;
            int index = 0;
            while (index < images.size()) {
                switch (index) {
                    case 0:
                        jsonObject.put("picture1", encodedString);
                        break;
                    case 1:
                        jsonObject.put("picture2", encodedString);
                        break;
                    case 2:
                        jsonObject.put("picture3", encodedString);
                    case 3:
                        jsonObject.put("picture4", encodedString);
                    case 4:
                        jsonObject.put("picture5", encodedString);
                    case 5:
                        jsonObject.put("picture6", encodedString);
                }
                index++;
            }
            body.put("post", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url2 = "https://07537acc-da80-4457-8b10-ff9e97cbea07.mock.pstmn.io/data6";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_IMAGES, jsonObject, response -> {
            Log.d(TAG, response.toString());
            try {
                createPostSuccess = response.getBoolean("postSuccessful");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (createPostSuccess == true) {
                Toast.makeText(CreatePostActivity.this, "Post is successful!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreatePostActivity.this, MainFeed.class);
                startActivity(intent);
            }else{
                Toast.makeText(CreatePostActivity.this, "Post unsuccessful.", Toast.LENGTH_LONG).show();
            }





        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            Toast.makeText(CreatePostActivity.this, "Post unsuccessful.", Toast.LENGTH_LONG).show();
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


