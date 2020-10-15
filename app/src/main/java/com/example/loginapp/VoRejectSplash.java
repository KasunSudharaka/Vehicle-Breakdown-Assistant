package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class VoRejectSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vo_reject_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                finish();
            }



        },2500);

    }
}
