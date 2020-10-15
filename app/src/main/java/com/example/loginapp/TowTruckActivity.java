 package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

 public class TowTruckActivity extends AppCompatActivity {

    String serviceid;
    //private TextView extrasss;
    private Button towbtn,donebtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    ArrayList<String> trouble=new ArrayList<String>();
     ArrayList<String> facility=new ArrayList<String>();
     private EditText nameed,contacted;
      String customerName;
      String customerContact;
      String otherinfo;
     private String troubleList;
     private String facilityList;
 String Facilities,Problem,Other,helpInfo;
     private CheckBox tow,tech,engine,battery,brakes,tires,steering,overheat;
     private EditText othered;
     private TextView servicename;


     private static final int REQUEST_CALL=1;
     private ImageView phone;
     private String servicephone;
     private ImageView email;
     private String servicemail;




     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tow_truck);

       // extrasss=(TextView)findViewById(R.id.extras);
        towbtn=(Button)findViewById(R.id.towbtn);
        nameed=(EditText)findViewById(R.id.nameed);
       // donebtn=(Button)findViewById(R.id.donebtn);
        contacted=(EditText)findViewById(R.id.contacted);
        tow=(CheckBox)findViewById(R.id.Tow);
        tech=(CheckBox)findViewById(R.id.tech);
        engine=(CheckBox)findViewById(R.id.engine);
        battery=(CheckBox)findViewById(R.id.battery);
        brakes=(CheckBox)findViewById(R.id.brakes);
        tires=(CheckBox)findViewById(R.id.tires);
        steering=(CheckBox)findViewById(R.id.steering);
        overheat=(CheckBox)findViewById(R.id.overheat);
        othered=(EditText)findViewById(R.id.othered);
        phone=(ImageView)findViewById(R.id.phone) ;
        email=(ImageView)findViewById(R.id.email);
        servicename=(TextView)findViewById(R.id.servicename);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();


        if (savedInstanceState==null){

            Bundle extras=getIntent().getExtras();
            if(extras==null){

                serviceid=null;
            }

            else{

                serviceid=extras.getString("serviceid");

                Toast.makeText(TowTruckActivity.this,"Service id received : "+serviceid,Toast.LENGTH_LONG).show();
                //extrasss.setText(serviceid);
            }
        }

        else {
            serviceid=(String)savedInstanceState.getSerializable("serviceid");
        }

//load info to edittexts
         loadUserInfo();

        //new code

        towbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference serviceref=firebaseDatabase.getReference().child("Users").child("Service").child(serviceid);
                String VoId=firebaseAuth.getCurrentUser().getUid();

                HashMap hmap=new HashMap();
                hmap.put("HelpReqId",VoId);
                serviceref.updateChildren(hmap);

                getTowLocation();

                saveToFirebase();
               // extrasss.setText(facilityList);

             //   VOMapActivity vv=new VOMapActivity();
              //  vv.startTimer();

               finish();

            }
        });


    /*    donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameed.setEnabled(false);
                contacted.setEnabled(false);
                tow.setEnabled(false);
                tech.setEnabled(false);
                engine.setEnabled(false);
                battery.setEnabled(false);
                brakes.setEnabled(false);
                tires.setEnabled(false);
                steering.setEnabled(false);
                overheat.setEnabled(false);
                othered.setEnabled(false);

StringBuilder stringBuilder=new StringBuilder();
for (String s :facility)
    stringBuilder.append(s).append("\n");

extrasss.setText(stringBuilder.toString());

                System.out.println("THIS IS THE ARRAYY"+facility);
                System.out.println("STRINGBUILDER"+stringBuilder.toString());

            }
        });*/



   tow.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           if (tow.isChecked()){

               facility.add("Tow truck");

           }

           else {

               facility.remove("Tow truck");
           }

       }
   });


tech.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (tech.isChecked()){

            facility.add("Technician");

        }

        else {
            facility.remove("Technician");

        }

    }
});



    engine.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (engine.isChecked()){
                trouble.add("Trouble in engine");

            }

            else {
                trouble.remove("Trouble in engine");

            }

        }
    });


        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (battery.isChecked()){
                    trouble.add("Battery trouble");

                }

                else {
                    trouble.remove("Battery trouble");

                }
            }
        });


   brakes.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           if (brakes.isChecked()){
               trouble.add("Brakes Not Working Or Grinding");

           }

           else {
               trouble.remove("Brakes Not Working Or Grinding");

           }
       }
   });


tires.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (tires.isChecked()){
            trouble.add("Flat Tires");

        }

        else {
            trouble.remove("Flat Tires");

        }
    }
});



steering.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (steering.isChecked()){
            trouble.add("Steering Wheel Trouble");

        }

        else {
            trouble.remove("Steering Wheel Trouble");

        }
    }
});

       overheat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (overheat.isChecked()){
                   trouble.add("Overheating");

               }

               else {
                   trouble.remove("Overheating");

               }
           }
       });

       phone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               makePhoneCall();
           }
       });


       email.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+servicemail.toString()));
               intent.putExtra(Intent.EXTRA_SUBJECT,"Customer Information");

               StringBuilder stringBuilder=new StringBuilder();
               for (String s :facility)
                   stringBuilder.append(s).append(",");


               StringBuilder stringBuildernew=new StringBuilder();
               for (String x:trouble)
                   stringBuildernew.append(x).append(",");


               helpInfo="Name:"+nameed.getText().toString()+"\\"+"Contact No:"+contacted.getText().toString()+"\\"+"Facilities need: "+stringBuilder.toString()+ "\\"+"Troubles having: "+stringBuildernew+"\\"+"Other info+"+otherinfo;


               intent.putExtra(Intent.EXTRA_TEXT,helpInfo);
               startActivity(intent);

               Toast.makeText(TowTruckActivity.this, "Email sent successfully", Toast.LENGTH_LONG).show();

           }
       });

    }

    //method to make phonecall

    public void makePhoneCall(){

if (servicephone.trim().length()>0){

    if (ContextCompat.checkSelfPermission(TowTruckActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

        ActivityCompat.requestPermissions(TowTruckActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
    }

    else {
        String dial="tel:" +servicephone;
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
    }
}

else {
    Toast.makeText(TowTruckActivity.this,"No phone number found",Toast.LENGTH_LONG).show();
}


    }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
if (requestCode==REQUEST_CALL){

    if (grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){

        makePhoneCall();
    }

    else {Toast.makeText(TowTruckActivity.this,"Permission denied", Toast.LENGTH_LONG).show();}
}

     }

     public void selectitem(View view){

        boolean checked=((CheckBox) view).isChecked();

        switch (view.getId()){


            case R.id.Tow:
                boolean towchecked=((CheckBox) view).isChecked();
                if (checked){

                    facility.add("Tow truck");
                }

                else {

                    facility.remove("Tow truck");
                }
                break;


            case R.id.tech:

                if (checked){

                    facility.add("Technician");

                }

                else {
                    facility.remove("Technician");

                }
                break;



            case R.id.engine:

                if (checked){
                    trouble.add("Trouble in engine");

                }

                else {
                    trouble.remove("Trouble in engine");

                }
                break;


            case R.id.battery:

                if (checked){
                    trouble.add("Battery trouble");

                }

                else {
                    trouble.remove("Battery trouble");

                }
                break;


            case R.id.brakes:

                if (checked){
                    trouble.add("Brakes Not Working Or Grinding");

                }

                else {
                    trouble.remove("Brakes Not Working Or Grinding");

                }
                break;

            case R.id.tires:

                if (checked){
                    trouble.add("Flat Tires");

                }

                else {
                    trouble.remove("Flat Tires");

                }
                break;

            case R.id.steering:

                if (checked){
                    trouble.add("Steering Wheel Trouble");

                }

                else {
                    trouble.remove("Steering Wheel Trouble");

                }
                break;

            case R.id.overheat:

                if (checked){
                    trouble.add("Overheating");

                }

                else {
                    trouble.remove("Overheating");

                }
                break;




        }


    }

    public void loadUserInfo(){

        final DatabaseReference vref=firebaseDatabase.getReference().child("UserInformation").child("Vehicle Owners").child(firebaseAuth.getCurrentUser().getUid());
        final DatabaseReference sref=firebaseDatabase.getReference().child("UserInformation").child("Service Stations").child(serviceid);


        vref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                VehicleOwnerInformation Vo=dataSnapshot.getValue(VehicleOwnerInformation.class);

                nameed.setText(Vo.getFullname(),TextView.BufferType.EDITABLE);
                contacted.setText(Vo.getMobile(),TextView.BufferType.EDITABLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInformation US=dataSnapshot.getValue(UserInformation.class);

                servicename.setText("Contact "+US.getSsname());
                servicephone=US.getContactnumber();
                servicemail=US.getServiceemail();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void saveToFirebase(){

         customerName=nameed.getText().toString().trim();
         customerContact=contacted.getText().toString().trim();
         otherinfo=othered.getText().toString().trim();


        nameed.setEnabled(false);
        contacted.setEnabled(false);
        tow.setEnabled(false);
        tech.setEnabled(false);
        engine.setEnabled(false);
        battery.setEnabled(false);
        brakes.setEnabled(false);
        tires.setEnabled(false);
        steering.setEnabled(false);
        overheat.setEnabled(false);
        othered.setEnabled(false);

        StringBuilder stringBuilder=new StringBuilder();
        for (String s :facility)
            stringBuilder.append(s).append(",");


        StringBuilder stringBuildernew=new StringBuilder();
        for (String x:trouble)
            stringBuildernew.append(x).append(",");

      //  extrasss.setText(stringBuilder.toString());


         Facilities=stringBuilder.toString();
         Problem=stringBuildernew.toString();
         Other=otherinfo;

        String vid=firebaseAuth.getCurrentUser().getUid();
        DatabaseReference sref=firebaseDatabase.getReference().child("HelpInfo").child(serviceid).child(vid);

       HashMap<String, Object> h=new HashMap<String, Object>();
        h.put("Voname",customerName);
        h.put("Vocontact",customerContact);
        h.put("Facilities",Facilities);
        h.put("Troubles",Problem);
        h.put("Other",Other);
        sref.updateChildren(h);


      /* HelpInformation he= new HelpInformation(customerName,customerContact,helpInfo);
       sref.setValue(he);
        System.out.println("ARRRRRRRRRRRRRRRAYYYY"+facilityList);*/



    }

  /*  public void finalSelection(View view){


         for (String fList:facility){

             facilityList=facilityList +facility;

             extrasss.setText(facilityList);
         }

for (String tList:trouble){

    troubleList=troubleList+trouble;

}

    }*/




private Marker towMarker;

     public void getTowLocation(){

        final VOMapActivity vo=new VOMapActivity();

        DatabaseReference servicelocationref=firebaseDatabase.getReference().child("CurrentlyServing").child(serviceid).child("l");
        servicelocationref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    List<Object> hMap=(List<Object>)dataSnapshot.getValue();
                    double locationLat=0;
                    double locationLong=0;
                  //  extrasss.setText("Service is on the way");

                    if (hMap.get(0)!=null){

                        locationLat=Double.parseDouble(hMap.get(0).toString());
                    }

                    if (hMap.get(1)!=null){

                        locationLong=Double.parseDouble(hMap.get(1).toString());
                    }

                    LatLng towlatlng=new LatLng(locationLat,locationLong);
                    if (towMarker!=null){

                        towMarker.remove();
                    }

                    try {
                        towMarker = vo.nMap.addMarker(new MarkerOptions().position(towlatlng).title("Service on the way"));
                        System.out.println("Marking successful");
                    }

                    catch (NullPointerException e){

                        System.out.println("ERORRRRRR"+e);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


 }
