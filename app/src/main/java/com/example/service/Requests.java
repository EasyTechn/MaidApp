package com.example.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Requests extends AppCompatActivity {

    TextView heading;
    private RecyclerView recyclerView;
    ProgressBar pbar;
    private ListAdapter listAdapter;
    private List<ServiceProviders> musers;

    FirebaseUser fuser;
    DatabaseReference database;
    private List<RequestList> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        pbar = findViewById(R.id.progressBar);
        heading = findViewById(R.id.request_text);
        final String check = getIntent().getStringExtra("buttontext");
        heading.setText(check);
        recyclerView = findViewById(R.id.request_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

//
//        FirebaseMessaging.getInstance().getToken.addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Set the notification message
//                        JSONObject notification = new JSONObject();
//                        try {
//                            notification.put("title", "New service request");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            notification.put("body", "A new service request has been received.");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//// Set the data payload
//                        JSONObject data = new JSONObject();
//                        try {
//                            data.put("service_request_id", pbar.getId());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//// Set the target device token
//                        JSONArray tokens = new JSONArray();
//                        tokens.put(token);
//
//// Set the message content
//                        JSONObject message = new JSONObject();
//                        try {
//                            message.put("notification", notification);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            message.put("data", data);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            message.put("registration_ids", tokens);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//// Send the message using FCM
//                        URL url = null;
//                        try {
//                            url = new URL("https://fcm.googleapis.com/fcm/send");
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        }
//                        HttpURLConnection conn = null;
//                        try {
//                            conn = (HttpURLConnection) url.openConnection();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            conn.setRequestMethod("POST");
//                        } catch (ProtocolException e) {
//                            e.printStackTrace();
//                        }
//                        conn.setRequestProperty("Authorization", "key=" +
//                                "BLTa10fAVmpVyg0PqM_QKoiQIMIFd2dG_SA-Cc6krHj-0SAUct6pI0nAoT0JMJh071dyUkLHg1NKz91Qw80VOI4");
//                        conn.setRequestProperty("Content-Type", "application/json");
//                        conn.setDoOutput(true);
//
//                        OutputStream os = null;
//                        try {
//                            os = conn.getOutputStream();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            os.write(message.toString().getBytes("UTF-8"));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            os.flush();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            os.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        try {
//                            int responseCode = conn.getResponseCode();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//
//
//
//        class MyFirebaseMessagingService extends FirebaseMessagingService {
//            @Override
//            public void onMessageReceived(RemoteMessage remoteMessage) {
//                // Handle the push notification message here
//            }
//        }


        database = FirebaseDatabase.getInstance().getReference("Requestlist").child(fuser.getUid());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestList chatlist = snapshot.getValue(RequestList.class);
                    userList.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chatList() {
        musers = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("ServiceProviders");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ServiceProviders c = dataSnapshot1.getValue(ServiceProviders.class);
                    for (RequestList chatlist : userList) {
                        if (c.getId().equals(chatlist.getId())) {
                            musers.add(c);
                        }
                    }
                    pbar.setVisibility(GONE);
                }
                listAdapter = new ListAdapter(Requests.this, musers);
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
