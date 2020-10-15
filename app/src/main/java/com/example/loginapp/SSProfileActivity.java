package com.example.loginapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SSProfileActivity extends AppCompatActivity {


    private EditText sseditNameeditText;
    private EditText sseditIdeditText;
    private EditText sseditPhoneeditText;
    private EditText sseditcityeditText;
    private EditText sseditDistricteditText;
    private EditText sseditAddresseditText;
    private EditText sseditSSnameeditText;
    private EditText sseditOtherNumeditText;
    private EditText sseditServiceMaileditText;
    private EditText sseditOpenHourseditText;
    private Button ssbtnEditNew;
    private Button ssbtnSaveNew;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssprofile);




        sseditNameeditText=(EditText)findViewById(R.id.editNameeditText);
        sseditIdeditText=(EditText)findViewById(R.id.editIdeditText);
        sseditPhoneeditText=(EditText)findViewById(R.id.editMobileeditText);
        sseditcityeditText=(EditText)findViewById(R.id.editCityyeditText);
        sseditDistricteditText=(EditText)findViewById(R.id.editDistrictteditText);
        sseditAddresseditText=(EditText)findViewById(R.id.editAddressseditText);
        sseditSSnameeditText=(EditText)findViewById(R.id.editSSeditText);
        sseditOtherNumeditText=(EditText)findViewById(R.id.editOtherNumededitText);
        sseditServiceMaileditText=(EditText)findViewById(R.id.editServicemaileditText);
        sseditOpenHourseditText=(EditText)findViewById(R.id.editOpenHourseditText);
        ssbtnEditNew = (Button)findViewById(R.id.buttonedit);
        ssbtnSaveNew=(Button)findViewById(R.id.buttonsave);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();


        final DatabaseReference dataref=firebaseDatabase.getReference().child("UserInformation").child("Service Stations").child(firebaseAuth.getUid());


        ssbtnEditNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                set();
            }
        });

        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInformation userInformation=dataSnapshot.getValue(UserInformation.class);

                sseditNameeditText.setText(userInformation.getFullname(), TextView.BufferType.EDITABLE);
                sseditIdeditText.setText(userInformation.getId(),TextView.BufferType.EDITABLE);
                sseditPhoneeditText.setText(userInformation.getMobile(),TextView.BufferType.EDITABLE);
                sseditcityeditText.setText(userInformation.getCity(),TextView.BufferType.EDITABLE);
                sseditDistricteditText.setText(userInformation.getDistrict(),TextView.BufferType.EDITABLE);
                sseditAddresseditText.setText(userInformation.getAddress(),TextView.BufferType.EDITABLE);
                sseditSSnameeditText.setText(userInformation.getSsname(),TextView.BufferType.EDITABLE);
                sseditOtherNumeditText.setText(userInformation.getContactnumber(),TextView.BufferType.EDITABLE);
                sseditServiceMaileditText.setText(userInformation.getServiceemail(),TextView.BufferType.EDITABLE);
                sseditOpenHourseditText.setText(userInformation.getOpenhours(),TextView.BufferType.EDITABLE);


            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(SSProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });


        ssbtnSaveNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String updateName=sseditNameeditText.getText().toString();
                String updateId=sseditIdeditText.getText().toString();
                String updatePhone=sseditPhoneeditText.getText().toString();
                String updateCity=sseditcityeditText.getText().toString();
                String updateDistrict=sseditDistricteditText.getText().toString();
                String updateAddress=sseditAddresseditText.getText().toString();
                String updateSS=sseditSSnameeditText.getText().toString();
                String updateOtherC=sseditOtherNumeditText.getText().toString();
                String updateServicemail=sseditServiceMaileditText.getText().toString();
                String updateOpenHours=sseditOpenHourseditText.getText().toString();


                UserInformation ssi=new UserInformation(updateName,updateId,updatePhone,updateCity,updateDistrict,
                        updateAddress,updateSS,updateOtherC,updateServicemail,updateOpenHours);

                dataref.setValue(ssi);
                Toast.makeText(SSProfileActivity.this,"Profile Information Updated",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void edit(){
        sseditNameeditText.setEnabled(false);
        sseditIdeditText.setEnabled(false);
        sseditPhoneeditText.setEnabled(false);
        sseditcityeditText.setEnabled(false);
        sseditDistricteditText.setEnabled(false);
        sseditAddresseditText.setEnabled(false);
        sseditSSnameeditText.setEnabled(false);
        sseditOtherNumeditText.setEnabled(false);
        sseditServiceMaileditText.setEnabled(false);
        sseditOpenHourseditText.setEnabled(false);
    };
    public void set(){
        sseditNameeditText.setEnabled(true);
        sseditIdeditText.setEnabled(true);
        sseditPhoneeditText.setEnabled(true);
        sseditcityeditText.setEnabled(true);
        sseditDistricteditText.setEnabled(true);
        sseditAddresseditText.setEnabled(true);
        sseditSSnameeditText.setEnabled(true);
        sseditOtherNumeditText.setEnabled(true);
        sseditServiceMaileditText.setEnabled(true);
        sseditOpenHourseditText.setEnabled(true);
    };
}
