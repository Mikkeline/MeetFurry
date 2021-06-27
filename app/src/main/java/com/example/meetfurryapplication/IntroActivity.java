package com.example.meetfurryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class IntroActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 4000;

    ImageView logo, img;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        logo = findViewById(R.id.logo);
        img = findViewById(R.id.img);
        lottieAnimationView = findViewById(R.id.lottie);

        img.animate().translationY(-1600).setDuration(1000).setStartDelay(3000);
        logo.animate().translationY(1400).setDuration(1000).setStartDelay(3000);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(3000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this,
                        StartingPage.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}