package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class selct_type extends AppCompatActivity {

    CardView cardView,cardView1,cardView2,cardView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selct_type);

        cardView  = findViewById(R.id.tire1);
        cardView1  = findViewById(R.id.heat);
        cardView2 = findViewById(R.id.battery);
        cardView3  = findViewById(R.id.breakdown);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(selct_type.this, help1.class);
                startActivity(intent);


            }
        });

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(selct_type.this, help2.class);
                startActivity(intent);


            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(selct_type.this, help3.class);
                startActivity(intent);


            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(selct_type.this, help4.class);
                startActivity(intent);


            }
        });


    }
}
