package com.example.loginapp;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    private EditText emeditText;
    private Button resetPwButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        emeditText=(EditText)findViewById(R.id.emeditText);
        resetPwButton=(Button)findViewById(R.id.resetpwbutton);
        firebaseAuth=FirebaseAuth.getInstance();


        resetPwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useremail=emeditText.getText().toString().trim();

                if (useremail.equals("")){

                    Toast.makeText(PasswordActivity.this,"Please Enter your registered email",Toast.LENGTH_LONG).show();
                }

                else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(PasswordActivity.this,"Password reset email sent",Toast.LENGTH_LONG).show();
                                finish();
                                Intent in=new Intent(PasswordActivity.this,LoginActivity.class);
                                startActivity(in);
                            }

                            else{

                                Toast.makeText(PasswordActivity.this,"Error in sending the password reset email",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
