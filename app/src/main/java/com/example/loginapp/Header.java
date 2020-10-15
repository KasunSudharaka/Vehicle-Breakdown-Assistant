package com.example.loginapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Header extends AppCompatActivity {

    private ImageView profileimage;
    private FirebaseDatabase firebaseDatabase;
    private String imageUrl;
    private TextView usersname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);


        profileimage=(ImageView)findViewById(R.id.profileimage);
        usersname=(TextView)findViewById(R.id.usersname);
        firebaseDatabase=FirebaseDatabase.getInstance();

        loadProfImg();
        getUsersName();

            }


            public void loadProfImg(){


                String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference db=firebaseDatabase.getReference().child("Profile Images").child(userId);

                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                            if (map.get("profileImageUrl")!=null){
                                imageUrl=map.get("profileImageUrl").toString();
                                Glide.with(getApplication()).load(imageUrl).into(profileimage);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            public void getUsersName(){

        String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference vo=firebaseDatabase.getReference().child("UserInformation").child("Vehicle Owners").child(userid);
                DatabaseReference ss=firebaseDatabase.getReference().child("UserInformation").child("Service Stations").child(userid);


                vo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            VehicleOwnerInformation veh=dataSnapshot.getValue(VehicleOwnerInformation.class);

                            usersname.setText("Welcome "+veh.getFullname());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                ss.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            UserInformation use=dataSnapshot.getValue(UserInformation.class);
                            usersname.setText("Welcome "+use.getFullname());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            }

