package com.example.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button signinbutton;
    private EditText emaileditText2;
    private EditText pweditText2;
    private TextView signintextView;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private TextView forgotpwtextView;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        signinbutton =(Button)findViewById(R.id.signinbutton);
        emaileditText2=(EditText) findViewById(R.id.emaileditText2);
        pweditText2=(EditText)findViewById(R.id.pweditText2);
        signintextView=(TextView)findViewById(R.id.signintextView);
        progressDialog=new ProgressDialog(this);
        forgotpwtextView=(TextView)findViewById(R.id.forgotpwtextView);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();



        Intent gotoVOMap=new Intent(getApplicationContext(),VOMapActivity.class);



        if(firebaseAuth.getCurrentUser() !=null){
            signinType();
        }

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogIn();

            }
        });


        signintextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);


            }
        });

        forgotpwtextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentpassword=new Intent(LoginActivity.this,PasswordActivity.class);
                startActivity(intentpassword);
            }
        });
    }

    private void userLogIn(){
        String email=emaileditText2.getText().toString().trim();
        String pw=pweditText2.getText().toString().trim();

        if(TextUtils.isEmpty(email)){

            Toast.makeText(this,"You can not leave email field empty",Toast.LENGTH_SHORT).show();
            return;

        }


        if(TextUtils.isEmpty(pw)){

            Toast.makeText(this,"You can not leave password field empty",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing in process..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()){
                    checkEmailVerification();

                }

                else{

                    Toast.makeText(LoginActivity.this,"Sign in failed!",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    public void checkEmailVerification(){

        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();


        if (emailflag){

           signinType();
        }

        else{
            Toast.makeText(this,"Verify your email first",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }


    public void signinType(){



        DatabaseReference VOref=firebaseDatabase.getReference().child("UserInformation").child("Vehicle Owners").child(firebaseAuth.getUid());



        VOref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot!=null) {

VehicleOwnerInformation vh=dataSnapshot.getValue(VehicleOwnerInformation.class);


                    try {

                        String veh=vh.getVehicle();

                        if (veh != null) {
                            Intent gotoVOMap = new Intent(LoginActivity.this, VOMapActivity.class);
                            finish();
                            startActivity(gotoVOMap);
                        }
                    }

                    catch (NullPointerException n)
                    {

                        Intent gotoSSMap=new Intent(LoginActivity.this,SStMapActivity.class);
                        finish();
                        startActivity(gotoSSMap);
                    }

                }

                    else {


                        Toast.makeText(LoginActivity.this,"Couldn't identify login type",Toast.LENGTH_LONG).show();
                    }
                }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









        /*
        if (authId.equals(databaseId)){

            Intent gotoSSMap=new Intent(LoginActivity.this,SSMapActivityNeww.class);
            finish();
            startActivity(gotoSSMap);

        }

        else {
            Intent gotoVOMap=new Intent(LoginActivity.this,VOMapActivity.class);
            finish();
            startActivity(gotoVOMap);

        }*/

    /*    DatabaseReference ref=firebaseDatabase.getReference("UserInformation");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){

                    String type= dataSnapshot.getValue().toString();

                    if (type.equals("Vehicle Owners")){


                        Intent gotoSSMap=new Intent(LoginActivity.this,SSMapActivityNeww.class);
                        finish();
                        startActivity(gotoSSMap);
                    }

                    else {


                        Intent gotoVOMap=new Intent(LoginActivity.this,VOMapActivity.class);
                        finish();
                        startActivity(gotoVOMap);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/

    }
}
