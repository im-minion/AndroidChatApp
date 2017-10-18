package com.androidchatapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import gr.net.maroulis.library.EasySplashScreen;

import static com.androidchatapp.R.drawable.splash;
import static com.androidchatapp.R.drawable.splash_logo;


public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        EasySplashScreen config = new EasySplashScreen(SplashScreen.this);
        config.withFullScreen();
        config.withTargetActivity(Login.class);
        config.withSplashTimeOut(5000);
        config.withBackgroundColor(Color.parseColor("#f43d3c"));
        config.withLogo(splash_logo);
        config.withHeaderText("");
        config.withFooterText("Made by Roll No. 61,70");
        config.withBeforeLogoText("");
        config.withAfterLogoText("Chatly");

        //text
        config.getHeaderTextView().setTextColor(Color.CYAN);
        config.getFooterTextView().setTextColor(Color.CYAN);
        config.getAfterLogoTextView().setTextColor(Color.CYAN);
        config.getBeforeLogoTextView().setTextColor(Color.CYAN);

        //now for view
        View view = config.create();

        //set view
        setContentView(view);

    }
}
