package com.example.androidexample;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainFeedAdmin extends AppCompatActivity {

    private DrawerLayout nDrawerLayout;

    private EditText adminMessage;

    private EditText adminTitle;

    private Button sendMsgBtn;

    private Button displayUsersBtn;
    private TextView allUsersTxt;
    private RequestQueue mQueue;

    private EditText deleteUser;
    private String deleteUserString;
    private Button deleteBtn;
    private boolean deleteUserBool;
//    String[] items = {"Material", "Design", "Components", "Android", "5.0 Lollipop"};
//    AutoCompleteTextView autoCompleteTextView;
//    ArrayAdapter<String> adapterItems;


//    String server_url = "http://coms-309-060.class.las.iastate.edu:8080/announcements";
    //insert url here

//  String server_url = "https://37668f7b-a5c8-475c-821b-06324c4610a1.mock.pstmn.io/admin";

    String server_url = "http://coms-309-060.class.las.iastate.edu:8080/announcements/create";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed_admin);

        adminTitle = findViewById(R.id.DescTitle);
        adminMessage = findViewById(R.id.announcementText);    // link to confirm edtext in the Signup activity XML
        sendMsgBtn = findViewById(R.id.sendMsgBtn);
        displayUsersBtn = findViewById(R.id.displayUsersBtn);
        allUsersTxt = findViewById(R.id.displayUsersTxt);
        displayUsersBtn = findViewById(R.id.displayUsersBtn);
        mQueue = Volley.newRequestQueue(this);
        deleteBtn = findViewById(R.id.deleteButton);
//        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
//        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
//        autoCompleteTextView.setAdapter(adapterItems);
//        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String item = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(MainFeedAdmin.this, "Item:" + item, Toast.LENGTH_LONG).show();
//            }
//        });
        displayUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });

        builder = new AlertDialog.Builder(MainFeedAdmin.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        NavigationView navigationView = findViewById(R.id.nav_view);
        nDrawerLayout = findViewById(R.id.drawer);
        navigationView.setItemIconTintList(null);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.darkGrey, getTheme()));

            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);

        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteUserString = deleteUser.getText().toString();

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("deleteUser", deleteUserString);
//                    jsonBody.put("description", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Const.URL_GET_ALL_USERS, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            deleteUserBool = response.getBoolean("deleteUser");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        if (deleteUserBool == true) {
                            Toast.makeText(MainFeedAdmin.this, "User successfully deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainFeedAdmin.this, "Something was wrong", Toast.LENGTH_SHORT).show();
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainFeedAdmin.this, "Error....", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
                    //                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
//
//                        params.put("title", msgTitle);
//                        params.put("description", message);
//
                        return params;
                    }
                };

                MySingleton.getInstance(MainFeedAdmin.this).addToRequestQueue(jsonObjReq);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), ViewAnnouncementAdmin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//              intent.putExtra("URL", website); IDK ABOUT THIS LINE
                getApplicationContext().startActivity(intent);


                nDrawerLayout.closeDrawers();

                return false;
            }
        });





        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String message, msgTitle;
                msgTitle = adminTitle.getText().toString();
                message = adminMessage.getText().toString();


                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("title", msgTitle);
                    jsonBody.put("description", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, server_url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        builder.setTitle("Server Response");
                        try {
                            builder.setMessage("Response " + response.getString("status"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adminMessage.setText("");
                                adminTitle.setText("");
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainFeedAdmin.this, "Error....", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
                    //                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
//
//                        params.put("title", msgTitle);
//                        params.put("description", message);
//
                        return params;
                    }
                };

                MySingleton.getInstance(MainFeedAdmin.this).addToRequestQueue(jsonObjReq);

            }
        });


    }

    private void jsonParse() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Const.URL_GET_ALL_USERS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject users = jsonArray.getJSONObject(i);

                        String firstName = users.getString("firstName");
                        String lastName = users.getString("lastName");
                        int age = users.getInt("age");
                        String mail = users.getString("mail");

                        allUsersTxt.append(mail + ", ");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);














    }






}




