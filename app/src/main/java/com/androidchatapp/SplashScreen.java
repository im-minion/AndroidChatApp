package com.androidchatapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import gr.net.maroulis.library.EasySplashScreen;

import static com.androidchatapp.R.drawable.ic_speech_bubble_large;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        EasySplashScreen config = new EasySplashScreen(SplashScreen.this);
        config.withFullScreen();
        config.withTargetActivity(Users.class);
        config.withSplashTimeOut(1000);
        config.withBackgroundColor(Color.parseColor("#ffffff"));
        config.withLogo(ic_speech_bubble_large);


        config.withAfterLogoText("Chat :)");

        //text

        config.getAfterLogoTextView().setTextColor(getResources().getColor(R.color.colorPrimary));
        config.getAfterLogoTextView().setTextSize(24);
        config.getLogo().setPadding(0, 0, 0, 10);
        //now for view
        View view = config.create();

        //set view
        setContentView(view);

    }
}
