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



public class Tab2Fragment extends Fragment{

    private static final String TAG="Tab2Fragment" ;

    private DatabaseReference databaseReference;

    private EditText fullnameEditText1,ideditText1,mobileeditText1,addresseditText1,vehicleeditText1,regeditText1,othereditText1;
    private Spinner vehicletypeeditText1;
    private Button savebutton1;
    private FirebaseAuth firebaseAuth;

public static final String[] Vehicletype={"Car","Jeep","Bike","Van","Three Wheeler"};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab2fragment,container,false);


        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        savebutton1=(Button)view.findViewById(R.id.savebutton1);
        fullnameEditText1=(EditText)view.findViewById(R.id.fullnameeditText1);
        ideditText1=(EditText)view.findViewById(R.id.ideditText1);
        mobileeditText1=(EditText)view.findViewById(R.id.mobileeditText1);
        addresseditText1=(EditText)view.findViewById(R.id.addresseditText1);
        vehicletypeeditText1=(Spinner)view.findViewById(R.id.vehicletypeeditText1);
        vehicleeditText1=(EditText)view.findViewById(R.id.vehicleeditText1);
        regeditText1=(EditText)view.findViewById(R.id.regeditText1);
        othereditText1=(EditText)view.findViewById(R.id.othereditText1);

        vehicletypeeditText1.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,Vehicletype));

        savebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveUserInfo();            }
        });


        return view;
    }

    public void saveUserInfo(){

        String name1=fullnameEditText1.getText().toString().trim();
        String id1=ideditText1.getText().toString().trim();
        String mob1=mobileeditText1.getText().toString().trim();
        String addr1=addresseditText1.getText().toString().trim();
        String veht=Vehicletype[vehicletypeeditText1.getSelectedItemPosition()];
        String vehicle=vehicleeditText1.getText().toString().trim();
        String reg=regeditText1.getText().toString().trim();
        String ot1=othereditText1.getText().toString().trim();

        VehicleOwnerInformation ve=new VehicleOwnerInformation(name1,id1,mob1,addr1,veht,vehicle,reg,ot1);

        FirebaseUser user=firebaseAuth.getCurrentUser();

        databaseReference.child("UserInformation").child("Vehicle Owners").child(user.getUid()).setValue(ve);

        Toast.makeText(getActivity(),"Information saved , Please verify your email to continue",Toast.LENGTH_LONG).show();

        Intent gotoSignin=new Intent(getActivity(),LoginActivity.class);
        firebaseAuth.signOut();
        gotoSignin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotoSignin);

    }

}
