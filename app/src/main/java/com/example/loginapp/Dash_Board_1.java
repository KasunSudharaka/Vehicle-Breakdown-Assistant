package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Dash_Board_1 extends AppCompatActivity {

    private CardView profileimg;
    private CardView historyCard;
    private CardView ratingCard;
    private CardView fuelCard;
    private CardView aboutusCard,online,website;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash__board_1);

        profileimg=(CardView)findViewById(R.id.profileimg);
        historyCard=(CardView)findViewById(R.id.historycard);
        ratingCard=(CardView)findViewById(R.id.online);
        fuelCard=(CardView)findViewById(R.id.fuelCard);
        aboutusCard=(CardView)findViewById(R.id.aboutUsCard);
        online=(CardView)findViewById(R.id.online);
        website=(CardView)findViewById(R.id.website);





        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Dash_Board_1.this,ProfilePhoto.class));
            }
        });


        historyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Dash_Board_1.this,HistoryActivity.class);
                intent.putExtra("VehicleOwnerOrService","Vehicle Owners");
                startActivity(intent);

            }
        });


        ratingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // startActivity(new Intent(Dash_Board_1.this,selct_type.class));


            }
        });


        fuelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Dash_Board_1.this,FuelMap.class));


            }
        });


        aboutusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dash_Board_1.this,AboutUs.class));

            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dash_Board_1.this,selct_type.class));

            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dash_Board_1.this,website.class));

            }
        });

    }
}
