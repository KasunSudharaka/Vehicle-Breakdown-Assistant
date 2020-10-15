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

public class MainActivity extends AppCompatActivity {

    private Button signupbutton;
    private EditText emaileditText;
    private EditText pweditText;
    private TextView signuptextView;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signupbutton=(Button)findViewById(R.id.signupbutton);
        emaileditText=(EditText)findViewById(R.id.emaileditText1);
        pweditText=(EditText)findViewById(R.id.pweditText1);
        signuptextView=(TextView)findViewById(R.id.signuptextView);
        progressDialog=new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();

        Intent gotoVOMap=new Intent(getApplicationContext(),VOMapActivity.class);



        if(firebaseAuth.getCurrentUser() !=null){
            finish();

            startActivity(gotoVOMap);

        }


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });




        signuptextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 finish();
                Intent gotosignin=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(gotosignin);

            }
        });
    }

    private void registerUser(){

        String email=emaileditText.getText().toString().trim();
        String password=pweditText.getText().toString().trim();


        if(TextUtils.isEmpty(email)){

            Toast.makeText(this,"You can not leave email field empty",Toast.LENGTH_SHORT).show();
            return;

        }


        if(TextUtils.isEmpty(password)){

            Toast.makeText(this,"You can not leave password field empty",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering the user");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    sendEmailVerification();
                    progressDialog.dismiss();


                }

                else {
                    Toast.makeText(MainActivity.this,"Registering is not Successfull",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void sendEmailVerification(){

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if (firebaseUser!=null){

            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        Toast.makeText(MainActivity.this,"Successfully Registered, Verification mail has been sent",Toast.LENGTH_SHORT).show();
                        Intent gotogetinfo=new Intent(getApplicationContext(),UserInfo.class);
                        finish();
                        startActivity(gotogetinfo);
                    }

                    else{

                        Toast.makeText(MainActivity.this,"Verification mail hasn't been sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
