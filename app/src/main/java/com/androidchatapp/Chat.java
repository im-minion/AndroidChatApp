package com.androidchatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference messageRef;
    boolean type = false;
    RecyclerView chatRecView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //layout = (LinearLayout) findViewById(R.id.layout1);
        //layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        chatRecView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.chat_with_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(UserDetails.chatwithEmail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
        chatRecView.setHasFixedSize(true);
        chatRecView.setLayoutManager(layoutManager);
        /**
         * 1. create messageRef to get till the message child
         * 2. from messageRef get chatRef as follows
         * >>>>>>if message ref contains child of type 1 then store chatref as the value of child type1
         * >>>>>>else if message ref contains child of type 2 then store chatref as the value of child type2
         * >>>>>>else set the chatRef to null
         * 3. send button on click listener take chatRef and inside it into push child setvalue of "map" defined below
         * 4. add the message
         * chat ref globally asscebile
         * */

        messageRef = FirebaseDatabase.getInstance().getReference("/messages");
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (UserDetails.chatRef != null) {
            FirebaseRecyclerAdapter<ChatModel, ChatViewHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<ChatModel, ChatViewHolder>(
                            ChatModel.class,
                            R.layout.chat_row,
                            ChatViewHolder.class,
                            UserDetails.chatRef) {
                        @Override
                        protected void populateViewHolder(ChatViewHolder viewHolder, ChatModel model, int position) {
                            final String chatKey = getRef(position).getKey();
                            viewHolder.setChatMessage(model.getMessage());
                            type = Objects.equals(model.getUser(), UserDetails.userID);
                            viewHolder.setUserText(model.getUser(),type);

                        }
                    };
            chatRecView.setAdapter(firebaseRecyclerAdapter);
        } else {
            startActivity(new Intent(Chat.this, Chat.class));
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView chatMessage;
        TextView userText;
        LinearLayout linearLayout;

        public ChatViewHolder(View itemView) {
            super(itemView);
            chatMessage = (TextView) itemView.findViewById(R.id.text_chat);
            userText = (TextView) itemView.findViewById(R.id.user_chat);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.lin_lay);
        }

        public void setChatMessage(String message) {
            chatMessage.setText(message);
        }


        public void setUserText(String userName, boolean type) {
            userText.setText(userName);
            if (type){
                //current logged in user
                linearLayout.setBackgroundResource(R.drawable.bubble_in);
                linearLayout.setGravity(Gravity.END);

            }else {
                linearLayout.setBackgroundResource(R.drawable.bubble_out);
                linearLayout.setGravity(Gravity.START);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.intentWithClear(Chat.this, Users.class);
    }
}
