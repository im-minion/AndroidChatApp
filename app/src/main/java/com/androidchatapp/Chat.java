package com.androidchatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference messageRef;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        toolbar = (Toolbar) findViewById(R.id.chat_with_toolbar);
        toolbar.setTitle(UserDetails.chatwithEmail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        /**
         * 1. create messageRef to get till the message child
         * 2. from messageRef get chatRef as follows
         * >>>>>>if message ref contains child of type 1 then store chatref as the value of child type1
         * >>>>>>else if message ref contains child of type 2 then store chatref as the value of child type2
         * >>>>>>else set the chatRef to null
         * 3. send button on click listener take chatRef and inside it into push child setvalue of "map" defined below
         * 4. add the message
         *
         * chat ref globally asscebile
         * **/


        messageRef = FirebaseDatabase.getInstance().getReference("/messages");

//        final String type1, type2;
//        type1 = UserDetails.userID + "_" + UserDetails.chatwithID;
//        type2 = UserDetails.chatwithID + "_" + UserDetails.userID;

//        messageRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot child1 = dataSnapshot.child(type1);
//                DataSnapshot child2 = dataSnapshot.child(type2);
//                if (child1.exists()) {
//                    UserDetails.userType = "type1";
//                    //chatRef2 = messageRef.child(type1);
//                } else if (child2.exists()) {
//                    UserDetails.userType = "type2";
//                } else {
//                    UserDetails.userType = "type1";
//                    //chatRef2 = messageRef.child(type2);
//                    dataSnapshot.child(type1);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        if (UserDetails.userType.equals("type1")) {
//            //type 1 child already exists :)
//            chatRef = messageRef.child(type1);
//        } else {
//            //type 2 child already exixts :)
//            chatRef = messageRef.child(type2);
//        }


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.userID);
                    UserDetails.chatRef.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });
        if (UserDetails.chatRef != null) {
            UserDetails.chatRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    String message = String.valueOf(dataSnapshot.child("message").getValue());
                    String userName = String.valueOf(dataSnapshot.child("user").getValue());
                    if (userName.equals(UserDetails.userID)) {
                        addMessageBox("You:-\n" + message, 1);
                    } else {
                        addMessageBox(UserDetails.chatwithEmail + ":-\n" + message, 2);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.END;
            textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp2.gravity = Gravity.START;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Utils.intentWithClear(Chat.this, Users.class);
        return true;
    }
}