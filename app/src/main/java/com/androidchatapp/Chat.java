package com.androidchatapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Chat extends AppCompatActivity {

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
        layoutManager.setStackFromEnd(true);
        chatRecView.setHasFixedSize(true);
        chatRecView.setLayoutManager(layoutManager);

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
        //int count = 0;
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
                            viewHolder.setUserText(model.getUser(), type);

                        }
                    };
            chatRecView.setAdapter(firebaseRecyclerAdapter);
        } else {
            //count = count + 1;
            Utils.intentWithClear(Chat.this, Chat.class);
            finish();
            //Toast.makeText(getApplicationContext(),"hppnd",Toast.LENGTH_SHORT).show();
            //Log.d("mytag", "happend " + count);
            //10 times it happens for the first time
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
            if (type) {
                //current logged in user
                linearLayout.setBackgroundResource(R.drawable.bubble_in);
                linearLayout.setGravity(Gravity.END);

            } else {
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
