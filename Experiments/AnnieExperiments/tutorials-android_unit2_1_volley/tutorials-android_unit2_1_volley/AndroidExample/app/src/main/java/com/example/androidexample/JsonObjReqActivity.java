package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjReqActivity extends AppCompatActivity {

    private Button btnJsonObjReq;

    private TextView msgResponse;
    public static int myID = 0;
    public static String username;
    public static String emailTxt;
    public static String passwordTxt;


    private static final String URL_JSON_OBJECT = "https://07537acc-da80-4457-8b10-ff9e97cbea07.mock.pstmn.io/user1"; //using my mock server instead of data given

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_obj_req);

        btnJsonObjReq = findViewById(R.id.btnJsonObj);
        msgResponse = findViewById(R.id.msgResponse);

        btnJsonObjReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonObjReq();
            }
        });
    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq() {
        JSONObject jsonObject = new JSONObject();
        try {
            //input your API parameters
            jsonObject.put("email", emailTxt.getText().toString());
            jsonObject.put("password", passwordTxt.getText().toString());
//            Toast.makeText(JsonObjReqActivity.this, "got e and p", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL_JSON_OBJECT,
                JSONObject, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
//                       msgResponse.setText(response.toString()); //got it to extract certain data from the object
                        try{
                            username = response.getString("username");
                            msgResponse.setText(username); //got it to extract certain data from the object
                        }catch(Exception e){
                            throw new RuntimeException();
                        }
//                                                try {
//                                                    myID = response.getInt("id"); //got it to extract certain data from the object
//                                                    msgResponse.setText(response.getInt("id"));
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
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
}