package com.androidchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al;
    int totalUsers = 0;
    private Button signout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference userRef;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        signout = (Button) findViewById(R.id.sign_out);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Intent intent = new Intent(Users.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        userRef = FirebaseDatabase.getInstance().getReference("/users");


        usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        //String url = "https://rtchat-6d4d7.firebaseio.com/users.json";
        String url = "https://androidchatapp-5321f.firebaseio.com/users.json";
//
//
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.w("MYTAG2 : ", "" + s);
//                doOnSuccess(s);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                System.out.println("" + volleyError);
//            }
//        });
//
//        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
//        rQueue.add(request);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al = new ArrayList<>();
                Log.w("MYTAG2 : ", "" + dataSnapshot.getChildrenCount());
//                Iterable<DataSnapshot> imagesDir = dataSnapshot.getChildren();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i("MyTag", child.getValue().toString());
                    al.add(String.valueOf(child.getValue()));
                }
                if (dataSnapshot.getChildrenCount() <= 1) {
                    noUsersText.setVisibility(View.VISIBLE);
                    usersList.setVisibility(View.GONE);
                } else {
                    noUsersText.setVisibility(View.GONE);
                    usersList.setVisibility(View.VISIBLE);
                    usersList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_list, al));
                }
                Log.i("vbhg", "" + al);
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                startActivity(new Intent(Users.this, Chat.class));
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

//    public void doOnSuccess(String s) {
//        try {
//            JSONObject obj = new JSONObject(s);
//
//            Iterator i = obj.keys();
//            String key = "";
//
//            while (i.hasNext()) {
//                key = i.next().toString();
//
//                if (!key.equals(UserDetails.username)) {
//                    al.add(key);
//                }
//                Log.w("MYTAG : ", "" + al);
//
//                totalUsers++;
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if (totalUsers <= 1) {
//            noUsersText.setVisibility(View.VISIBLE);
//            usersList.setVisibility(View.GONE);
//        } else {
//            noUsersText.setVisibility(View.GONE);
//            usersList.setVisibility(View.VISIBLE);
//            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
//        }
//
//        pd.dismiss();
//    }
}