package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FinishActivity extends AppCompatActivity {

    private static int Splash_time_out=2500;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (firebaseAuth.getCurrentUser()!=null){

                    signinType();
                }

                else {
                    Intent homeIntent=new Intent(FinishActivity.this, LoginActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }



        },Splash_time_out);


    }

    public void signinType() {


        DatabaseReference VOref = firebaseDatabase.getReference().child("UserInformation").child("Vehicle Owners").child(firebaseAuth.getUid());


        VOref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    VehicleOwnerInformation vh = dataSnapshot.getValue(VehicleOwnerInformation.class);


                    try {

                        String veh = vh.getVehicle();

                        if (veh != null) {
                            Intent gotoVOMap = new Intent(FinishActivity.this, VOMapActivity.class);
                            finish();
                            startActivity(gotoVOMap);
                        }
                    } catch (NullPointerException n) {

                        Intent gotoSSMap = new Intent(FinishActivity.this, SStMapActivity.class);
                        finish();
                        startActivity(gotoSSMap);
                    }

                } else {


                    Toast.makeText(FinishActivity.this, "Couldn't identify login type", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
