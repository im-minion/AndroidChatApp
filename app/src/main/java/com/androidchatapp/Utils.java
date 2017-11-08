package com.androidchatapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by vaibhav on 11/8/17.
 */

public class Utils {
    public static void valueEventListener(DatabaseReference dbref, final String checkChild) {
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //Log.w("checkcheck", child.getKey());
                    if (child.getValue().equals(checkChild)) {
                        UserDetails.chatwithID = child.getKey();
                        //UserDetails.setChatwithID(child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
