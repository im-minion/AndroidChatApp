package com.androidchatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference messageRef, chatRef;
    String type1, type2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        messageRef = FirebaseDatabase.getInstance().getReference("/messages");


        type1 = UserDetails.userID + "_" + UserDetails.chatwithID;
        type2 = UserDetails.chatwithID + "_" + UserDetails.userID;

        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot child1 = dataSnapshot.child(type1);
                DataSnapshot child2 = dataSnapshot.child(type2);
                if (child1.exists()) {
                    UserDetails.userType = "type1";
                    //chatRef2 = messageRef.child(type1);
                } else if (child2.exists()) {
                    UserDetails.userType = "type2";
                } else {
                    //dataSnapshot.child(type1);
                    UserDetails.userType = "not_exists";
                    //chatRef2 = messageRef.child(type2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        switch (UserDetails.userType) {
            case "type1":
                //type 1 child already exists :)
                Toast.makeText(Chat.this, "type 1 child already exists", Toast.LENGTH_SHORT).show();
                chatRef = messageRef.child(type1);
                break;
            case "type2":
                //type 2 child already exixts :)
                Toast.makeText(Chat.this, "type 2 child already exists", Toast.LENGTH_SHORT).show();
                chatRef = messageRef.child(type2);
                break;
            case "not_exists":
                //messageRef.child(type1).se
                Toast.makeText(Chat.this, "new Chat", Toast.LENGTH_SHORT).show();
                chatRef = null;
                break;
        }
        fun1();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.userID);
                    if (chatRef == null) {
                        chatRef = messageRef.child(type1);
                        chatRef.push().setValue(map);
                        fun1();
                        Toast.makeText(Chat.this, "First MSG sent!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Chat.this, "not First MSG!", Toast.LENGTH_SHORT).show();
                        chatRef.push().setValue(map);
                    }
                    messageArea.setText("");
                }
            }
        });
    }

    private void fun1() {
        if (chatRef != null) {
            Toast.makeText(Chat.this, "Called Fun1", Toast.LENGTH_SHORT).show();
            chatRef.addChildEventListener(new ChildEventListener() {
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
}