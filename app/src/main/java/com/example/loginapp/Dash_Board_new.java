package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Dash_Board_new extends AppCompatActivity {

    private CardView profimgss;
    private CardView historyCard;
    private CardView ratinSummaryCard;
    private CardView aboutusCard,changelocation,fuelCard;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash__board_new);

        profimgss=(CardView)findViewById(R.id.profimgss);
        historyCard=(CardView)findViewById(R.id.historycardnew);
        ratinSummaryCard=(CardView)findViewById(R.id.ratingsummarycard);
        aboutusCard=(CardView)findViewById(R.id.aboutUsCard);
        changelocation = (CardView)findViewById(R.id.updatelocation);
        fuelCard = (CardView)findViewById(R.id.fuelCard);






        profimgss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dash_Board_new.this,ProfilePhoto.class));

            }
        });



        historyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Dash_Board_new.this,HistoryActivity.class);
                intent.putExtra("VehicleOwnerOrService","Service Stations");
                startActivity(intent);

            }
        });

        ratinSummaryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dash_Board_new.this,TestActivity.class));
            }
        });


        aboutusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dash_Board_new.this,AboutUs.class));

            }
        });

        changelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dash_Board_new.this,SSLocationChange.class);
                startActivity(intent);
            }
        });
        fuelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Dash_Board_new.this,FuelMap.class));


            }
        });



    }
}
