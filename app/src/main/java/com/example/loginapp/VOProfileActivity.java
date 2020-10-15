package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

public class VOProfileActivity extends AppCompatActivity {


    private EditText editNameeditText;
    private EditText editIdeditText;
    private EditText editPhoneeditText;
    private EditText editAddresseditText;
    private EditText editVehicleTypeeditText;
    private EditText editVehicleeditText;
    private EditText editVehicleRegeditText;
    private EditText editOtherNumeditText;
    private Button btnEditNew;
    private Button btnSaveNew;

    
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voprofile);




        editNameeditText=(EditText)findViewById(R.id.editNameeditText);
        editIdeditText=(EditText)findViewById(R.id.editIdeditText);
        editPhoneeditText=(EditText)findViewById(R.id.editMobileeditText);
        editAddresseditText=(EditText)findViewById(R.id.editCityyeditText);
        editVehicleTypeeditText=(EditText)findViewById(R.id.editDistrictteditText);
        editVehicleeditText=(EditText)findViewById(R.id.editAddressseditText);
        editVehicleRegeditText=(EditText)findViewById(R.id.editSSeditText);
        editOtherNumeditText=(EditText)findViewById(R.id.editOtherNumededitText);
        btnEditNew = (Button)findViewById(R.id.buttonedit);
        btnSaveNew=(Button)findViewById(R.id.buttonsave);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();


        final DatabaseReference dataref=firebaseDatabase.getReference().child("UserInformation").child("Vehicle Owners").child(firebaseAuth.getUid());


        btnEditNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                set();
            }
        });

        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                VehicleOwnerInformation vehicleOwnerInformation=dataSnapshot.getValue(VehicleOwnerInformation.class);

                editNameeditText.setText(vehicleOwnerInformation.getFullname(), TextView.BufferType.EDITABLE);
                editIdeditText.setText(vehicleOwnerInformation.getId(),TextView.BufferType.EDITABLE);
                editPhoneeditText.setText(vehicleOwnerInformation.getMobile(),TextView.BufferType.EDITABLE);
                editAddresseditText.setText(vehicleOwnerInformation.getAddress(),TextView.BufferType.EDITABLE);
                editVehicleTypeeditText.setText(vehicleOwnerInformation.getVehicletype(),TextView.BufferType.EDITABLE);
                editVehicleeditText.setText(vehicleOwnerInformation.getVehicle(),TextView.BufferType.EDITABLE);
                editVehicleRegeditText.setText(vehicleOwnerInformation.getRegno(),TextView.BufferType.EDITABLE);
                editOtherNumeditText.setText(vehicleOwnerInformation.getContactnumber(),TextView.BufferType.EDITABLE);



            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(VOProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });


        btnSaveNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String updateName=editNameeditText.getText().toString();
                String updateId=editIdeditText.getText().toString();
                String updatePhone=editPhoneeditText.getText().toString();
                String updateAddress=editAddresseditText.getText().toString();
                String updateVehicleType=editVehicleTypeeditText.getText().toString();
                String updateVehicle=editVehicleeditText.getText().toString();
                String updateReg=editVehicleRegeditText.getText().toString();
                String updateOther=editOtherNumeditText.getText().toString();


                VehicleOwnerInformation vo=new VehicleOwnerInformation(updateName,updateId,updatePhone,updateAddress,updateVehicleType,
                        updateVehicle,updateReg,updateOther);

                dataref.setValue(vo);
                Toast.makeText(VOProfileActivity.this,"Profile Information Updated",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }



    public void edit(){
        editNameeditText.setEnabled(false);
        editIdeditText.setEnabled(false);
        editPhoneeditText.setEnabled(false);
        editAddresseditText.setEnabled(false);
        editVehicleTypeeditText.setEnabled(false);
        editVehicleeditText.setEnabled(false);
        editVehicleRegeditText.setEnabled(false);
        editOtherNumeditText.setEnabled(false);
    }
    public void set(){
        editNameeditText.setEnabled(true);
        editIdeditText.setEnabled(true);
        editPhoneeditText.setEnabled(true);
        editAddresseditText.setEnabled(true);
        editVehicleTypeeditText.setEnabled(true);
        editVehicleeditText.setEnabled(true);
        editVehicleRegeditText.setEnabled(true);
        editOtherNumeditText.setEnabled(true);
    }
}
