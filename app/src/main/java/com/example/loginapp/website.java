package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;

public class website extends AppCompatActivity {


    WebView webpage;
    private String Url="https://vehiclebreakdownassistant.000webhostapp.com/load.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        webpage=(WebView)findViewById(R.id.webv);

        webpage.setWebViewClient(new WebViewClient());
        webpage.getSettings().setJavaScriptEnabled(true);
        webpage.loadUrl(Url);
    }
}
