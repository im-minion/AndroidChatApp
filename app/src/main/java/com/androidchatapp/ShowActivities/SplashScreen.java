package com.androidchatapp.ShowActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.androidchatapp.R;

import gr.net.maroulis.library.EasySplashScreen;

import static com.androidchatapp.R.drawable.ic_speech_bubble_large;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        EasySplashScreen configurations = new EasySplashScreen(SplashScreen.this);
        configurations.withFullScreen();
        configurations.withTargetActivity(Users.class);
        configurations.withSplashTimeOut(1000);
        configurations.withBackgroundColor(Color.parseColor("#ffffff"));
        configurations.withLogo(ic_speech_bubble_large);
        configurations.withAfterLogoText("Chat :)");

        configurations.getAfterLogoTextView().setTextColor(getResources().getColor(R.color.colorPrimary));
        configurations.getAfterLogoTextView().setTextSize(24);
        configurations.getLogo().setPadding(0, 0, 0, 10);
        View v = configurations.create();
        setContentView(v);
    }
}
