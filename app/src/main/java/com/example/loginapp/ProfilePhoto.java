package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfilePhoto extends AppCompatActivity {

    private ImageView profimg;
    private Button saveimage;
    private Uri resulturi;
    private String uid;
    private FirebaseDatabase firebaseDatabase;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);

        profimg=(ImageView)findViewById(R.id.profimage);

        saveimage=(Button)findViewById(R.id.saveimage);
        firebaseDatabase=FirebaseDatabase.getInstance();

        profimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent inte=new Intent(Intent.ACTION_PICK);
                inte.setType("image/*");
                startActivityForResult(inte,1);

            }
        });


        getProfileImage();

        saveimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (resulturi!=null){

                    final StorageReference filePath= FirebaseStorage.getInstance().getReference().child("Profile Images").child(uid);
                   final DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Profile Images").child(uid);

                    Bitmap bitmap=null;

                    try {
                        bitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resulturi);
                    }

                    catch (IOException e) {
                        e.printStackTrace();
                    }


                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
                    byte[] data=baos.toByteArray();
                    UploadTask uploadTask=filePath.putBytes(data);


                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map newImage=new HashMap();

                                    newImage.put("profileImageUrl",uri.toString());
                                    db.updateChildren(newImage);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    finish();
                                }
                            });
                        }
                    });
                }

            }
        });

    }


    public void getProfileImage(){

        String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference db=firebaseDatabase.getReference().child("Profile Images").child(userId);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                    if (map.get("profileImageUrl")!=null){
               imageUrl=map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(imageUrl).into(profimg);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1&& resultCode==Activity.RESULT_OK ){

            final Uri imageUri=data.getData();
            resulturi=imageUri;
            profimg.setImageURI(resulturi);
        }
    }
}
