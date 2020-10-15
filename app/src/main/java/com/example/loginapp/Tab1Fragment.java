package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Tab1Fragment extends Fragment{

    private static final String TAG="Tab1Fragment" ;
    private DatabaseReference databaseReference;

    private EditText fullnameEditText,ideditText,mobileeditText,cityeditText,addresseditText,ssnameeditText,othereditText,servicemaileditText,openhourseditText;
    private Spinner districteditText;
    private Button savebutton;
    private FirebaseAuth firebaseAuth;

    public static final String[] distrcits={"Ampara","Anuradhapura","Badulla","Batticaloa","Colombo","Galle","Gampaha","Hambantota","Jaffna",
    "Kalutara","Kandy","Kegalle","Kilinochchi","Kurnegala","Mannar","Matale","Matara","Monaragala","Mulativu","Nuwara Eliya","Polonnaruwa","puttalam",
    "Ratnapura","Trincomalee","Vauniya"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab1fragment,container,false);



        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        savebutton=(Button)view.findViewById(R.id.savebutton);
        fullnameEditText=(EditText)view.findViewById(R.id.fullnameeditText);
        ideditText=(EditText)view.findViewById(R.id.ideditText);
        mobileeditText=(EditText)view.findViewById(R.id.mobileeditText);
        cityeditText=(EditText)view.findViewById(R.id.cityeditText);
        districteditText=(Spinner)view.findViewById(R.id.districteditText);
        addresseditText=(EditText)view.findViewById(R.id.addresseditText);
        ssnameeditText=(EditText)view.findViewById(R.id.ssnameeditText);
        othereditText=(EditText)view.findViewById(R.id.othereditText);
        servicemaileditText=(EditText)view.findViewById(R.id.servicemaileditText);
        openhourseditText=(EditText)view.findViewById(R.id.opernHoursEditText);


        districteditText.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,distrcits));

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveUserInfo();
            }
        });



        return view;
    }

    public void saveUserInfo(){

        String name=fullnameEditText.getText().toString().trim();
        String id=ideditText.getText().toString().trim();
        String mob=mobileeditText.getText().toString().trim();
        String city=cityeditText.getText().toString().trim();
        String dis=distrcits[districteditText.getSelectedItemPosition()];
        String add=addresseditText.getText().toString().trim();
        String ss=ssnameeditText.getText().toString().trim();
        String ot=othereditText.getText().toString().trim();
        String se=servicemaileditText.getText().toString().trim();
        String oh=openhourseditText.getText().toString().trim();

        UserInformation us=new UserInformation(name,id,mob,city,dis,add,ss,ot,se,oh);

        FirebaseUser user=firebaseAuth.getCurrentUser();

        databaseReference.child("UserInformation").child("Service Stations").child(user.getUid()).setValue(us);

      //  Toast.makeText(getActivity(),"Information saved , Please verify your email to continue",Toast.LENGTH_LONG).show();

        Intent gotoCLmap=new Intent(getActivity(),ChooseLocation.class);
        //firebaseAuth.signOut();
        gotoCLmap.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotoCLmap);

    }
}
