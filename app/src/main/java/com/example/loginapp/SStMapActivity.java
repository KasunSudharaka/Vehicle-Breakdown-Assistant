package com.example.loginapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SStMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, RoutingListener {


    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;

    Location helpreqLocation;

    public GoogleMap nMap;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private Boolean isLoggingOut=false;
    private Boolean isNotifOpen=false;
    private Boolean finishOpen=false;


    //nav
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    //searchbareditText

    private EditText searchbarss;

    //informing cardview
    private CardView infrmcrdview;
    //Buttons
    private Button cnfrmbtn,vwbtn,cnclbtn;

    //textview
    private TextView distancetext;

    //animations
    Animation FabOpen,FabClose,FabOpenNew,FabCloseNew;
    boolean isOpen=false;
    boolean crdopen=false;
    boolean customerinfoopen=false;

    //
    private String helpreqId="";
    private LatLng towlatlng;

    //help required location
    private Marker helprequiredlocationmarker;

    //helpinfo
    private TextView namet,contactt,facilitiest,problemst,othert;

    private String imageUrl,customerImgUrl;

    private LinearLayout customerinfo;

    private ImageView customerimage;
    private TextView customername,customercontact;
    private ImageView callbtn2;
    private String customermobile;

    private Button vwbtn2;

    private float distancenew;
    private TextView arrivalss;
    private Button finishbtn;

   // private String ssname;

    private static final int REQUEST_CALL=2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sst_map);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        //searchbar

        searchbarss=(EditText)findViewById(R.id.inputSearchss);

        //informing cardview
        infrmcrdview=(CardView)findViewById(R.id.infrmcrdview);
        namet=(TextView)findViewById(R.id.namet);
        contactt=(TextView)findViewById(R.id.contactt);
        facilitiest=(TextView)findViewById(R.id.facilitiest);
        problemst=(TextView)findViewById(R.id.problemt);
        othert=(TextView)findViewById(R.id.othert);

        //Buttons
        cnfrmbtn=(Button)findViewById(R.id.cnfrmbtn);
        vwbtn=(Button)findViewById(R.id.vbtn);
        cnclbtn=(Button)findViewById(R.id.cnclbtn);
        //textviews
        distancetext=(TextView)findViewById(R.id.distancetextView);

        //animations
        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabOpenNew=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open_new);
        FabCloseNew=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close_new);

        customerinfo=(LinearLayout)findViewById(R.id.customerinfo);
        customerimage=(ImageView)findViewById(R.id.customerimage);
        customername=(TextView)findViewById(R.id.customername);
        customercontact=(TextView)findViewById(R.id.customercontact);
        callbtn2=(ImageView) findViewById(R.id.callbtn2);
        vwbtn2=(Button)findViewById(R.id.vwbtn2);
        arrivalss=(TextView)findViewById(R.id.arrivalss);
        finishbtn=(Button)findViewById(R.id.finishbtn);

        //poly lines
        polylines = new ArrayList<>();

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SStMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQ_CODE );
        }
        else {
            supportMapFragment.getMapAsync(this);
        }


        //nav

        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        frameLayout=findViewById(R.id.frame);
        navigationView=findViewById(R.id.nav_drawer);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment;

                switch (menuItem.getItemId()){
                    case R.id.profile:

                        Intent gotoSSProfile= new Intent(SStMapActivity.this,SSProfileActivity.class);

                        startActivity(gotoSSProfile);
                        break;

                    case R.id.dashboard:



                        Intent gotoDash= new Intent(SStMapActivity.this,Dash_Board_new.class);

                        startActivity(gotoDash);
                        break;

                    case R.id.about:



                        startActivity(new Intent(SStMapActivity.this,AboutUs.class));

                        break;
                }


                return false;
            }
        });



        getAssignedVehicleOwner();
        getHelpInfo();

        cnfrmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sid=firebaseAuth.getCurrentUser().getUid();
                final DatabaseReference confirmnode=firebaseDatabase.getReference().child("ConfirmRequest").child(sid).child(helpreqId);

                HashMap hmap=new HashMap();
                hmap.put("CustomerId",helpreqId);
                confirmnode.updateChildren(hmap);

                openCustomerInfo();

                if (crdopen){
                    infrmcrdview.startAnimation(FabClose);
                    crdopen=false;

                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        confirmnode.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                if (databaseError!=null){

                                    System.out.println("Confirmation deleted successfully");

                                }

                                else {
                                    System.out.println("Couldn't delete confirmation");
                                }
                            }
                        });
                    }

                },15000);


            }
        });


cnclbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

//creating rejectNode

        String csid=firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference rejectNode=firebaseDatabase.getReference().child("RejectRequest").child(csid).child(helpreqId);

        HashMap hmap=new HashMap();
        hmap.put("CustomerId",helpreqId);
        rejectNode.updateChildren(hmap);


        //deleting user node
        String servid=firebaseAuth.getCurrentUser().getUid();
        DatabaseReference usersref=FirebaseDatabase.getInstance().getReference("Users").child("Service").child(servid);
        usersref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError!=null){

                    System.out.println("Help request deleted successfully");
                    Toast.makeText(SStMapActivity.this,"Request Cancelled",Toast.LENGTH_LONG);

                }

                else {
                    System.out.println("Couldn't delete help request");
                }
            }
        });


        nMap.clear();
      erasePolyLines();
        arrivalss.setText("");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                rejectNode.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        if (databaseError!=null){

                            System.out.println("Rejection deleted successfully");

                        }

                        else {
                            System.out.println("Couldn't delete Rejection");
                        }
                    }
                });
            }

        },10000);



        //creating cancel node and deleteing

      /*  String ssssid=firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference cancelnode=firebaseDatabase.getReference().child("CancelRequest").child(helpreqId);

        HashMap cancl=new HashMap();
        cancl.put("ServiceId",ssssid);
        cancelnode.updateChildren(cancl);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                cancelnode.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        if (databaseError!=null){

                            System.out.println("Confirmation deleted successfully");

                        }

                        else {
                            System.out.println("Couldn't delete confirmation");
                        }
                    }
                });
            }

        },15000);*/


        //deleting the helpinfo record

        String ssssid=firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference helpinfonode=firebaseDatabase.getReference().child("HelpInfo").child(ssssid).child(helpreqId);

        helpinfonode.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError!=null){

                    System.out.println("Helpinfo deleted successfully");

                }

                else {
                    System.out.println("Couldn't delete helpinfo");
                }
            }
        });

        finish();
        Intent gotorejectsplash=new Intent(SStMapActivity.this,RejectSplash.class);
        startActivity(gotorejectsplash);






    }
});

finishbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


        String fid=firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference finishnode=firebaseDatabase.getReference().child("FinishRequest").child(fid).child(helpreqId);

        HashMap hfnish=new HashMap();
        hfnish.put("FinishedCustomerId",helpreqId);
        finishnode.updateChildren(hfnish);


        recordHelp();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                finishnode.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        if (databaseError!=null){

                            System.out.println("Finish deleted successfully");

                        }

                        else {
                            System.out.println("Couldn't delete Finish");
                        }
                    }
                });
            }

        },15000);


        //deleting user node
        String servid=firebaseAuth.getCurrentUser().getUid();
        DatabaseReference usersref=FirebaseDatabase.getInstance().getReference("Users").child("Service").child(servid);
        usersref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError!=null){

                    System.out.println("Help request deleted successfully");
                    Toast.makeText(SStMapActivity.this,"Request Cancelled",Toast.LENGTH_LONG);

                }

                else {
                    System.out.println("Couldn't delete help request");
                }
            }
        });

        //deleting the helpinfo record

        String ssssid=firebaseAuth.getCurrentUser().getUid();
        final DatabaseReference helpinfonode=firebaseDatabase.getReference().child("HelpInfo").child(ssssid).child(helpreqId);

        helpinfonode.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError!=null){

                    System.out.println("Helpinfo deleted successfully");

                }

                else {
                    System.out.println("Couldn't delete helpinfo");
                }
            }
        });




        nMap.clear();
        erasePolyLines();

        if (finishOpen) {
            finishbtn.startAnimation(FabCloseNew);
            finishOpen=false;
        }
        finish();
        Intent gotofinishsplash=new Intent(SStMapActivity.this,FinishActivity.class);
        startActivity(gotofinishsplash);
    }
});



updateHeader();


    }


    //history

    public void recordHelp(){

String uuuid=firebaseAuth.getCurrentUser().getUid();
         DatabaseReference serviceref=firebaseDatabase.getReference().child("UserInformation").child("Service Stations").child(uuuid).child("History");
         DatabaseReference customerref=firebaseDatabase.getReference().child("UserInformation").child("Vehicle Owners").child(helpreqId).child("History");

        DatabaseReference finishIdref= firebaseDatabase.getReference().child("FinishRequest");



         DatabaseReference historyref=firebaseDatabase.getReference().child("History");

         String helpid=historyref.push().getKey();
         serviceref.child(helpid).setValue(true);
         customerref.child(helpid).setValue(true);


//putting ride id to finish node

        HashMap finishHis=new HashMap();
        finishHis.put("historyId",helpid);

        finishIdref.updateChildren(finishHis);



/*
        final DatabaseReference ssdataref=firebaseDatabase.getReference().child("UserInformation").child("Service Stations").child(uuuid);



        ssdataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation us=dataSnapshot.getValue(UserInformation.class);

                ssname=us.getSsname();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


         HashMap hash=new HashMap();
         hash.put("driver",uuuid);
         hash.put("vehowner",helpreqId);
         hash.put("rating",0);
         hash.put("timestamp",getCurrentTimeStamp());
         hash.put("trouble",problemst.getText().toString());
         hash.put("facility",facilitiest.getText().toString());

         historyref.child(helpid).updateChildren(hash);




         //saving location


        DatabaseReference historylocation=firebaseDatabase.getReference().child("History").child(helpid).child("location");
        GeoFire geo=new GeoFire(historylocation);
        geo.setLocation(helpid,new GeoLocation(helpreqLocation.getLatitude(),helpreqLocation.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {

                    Toast.makeText(SStMapActivity.this,"Locating..",Toast.LENGTH_LONG).show();
                    System.out.println("Location saved on server successfully!");

                }
            }
        });


    }

    private Long getCurrentTimeStamp() {

        Long timestamp=System.currentTimeMillis()/1000;
        return timestamp;
    }


    //checking for cancelation and restarting the activity

    public void checkCancelation(){

        System.out.println("CANCELATIONNNNNN");

        String sssid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference checkcancel=firebaseDatabase.getReference().child("CancelRequest").child(helpreqId).child(sssid).child("ServiceId");

        checkcancel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    arrivalss.setText("");
                   erasePolyLines();
                    finish();
                    Intent gotocancelsplash=new Intent(SStMapActivity.this,RequestSplash.class);
                    startActivity(gotocancelsplash);

                    System.out.println("METHODDDDDDDDDD");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("NOOOOOOOOOOOOOOOOO");

            }
        });
    }

    //open customer info

    public void openCustomerInfo(){


        DatabaseReference cus=firebaseDatabase.getReference().child("UserInformation").child("Vehicle Owners").child(helpreqId);
        DatabaseReference url=firebaseDatabase.getReference().child("Profile Images").child(helpreqId);

        cus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    VehicleOwnerInformation vv=dataSnapshot.getValue(VehicleOwnerInformation.class);

                    customername.setText("Customer Name: "+vv.getFullname());
                    customercontact.setText("Contact No: "+vv.getMobile());
                    customermobile=vv.getMobile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        url.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                    if (map.get("profileImageUrl")!=null){
                        customerImgUrl=map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(customerImgUrl).into(customerimage);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (!customerinfoopen){

            customerinfo.startAnimation(FabOpen);
            customerinfoopen=true;

        }


callbtn2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        makePhoneCall();
    }
});

        vwbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewCustomerLocation(helpreqId);
            }
        });

    }


    public void makePhoneCall(){

        if (customermobile.trim().length()>0){

            if (ContextCompat.checkSelfPermission(SStMapActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(SStMapActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }

            else {
                String dial="tel:" +customermobile;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }

        else {
            Toast.makeText(SStMapActivity.this,"No phone number found",Toast.LENGTH_LONG).show();
        }


    }


    public void updateHeader(){

        View headerview=navigationView.getHeaderView(0);

        final TextView usersname=headerview.findViewById(R.id.usersname);
        final ImageView profileimage=headerview.findViewById(R.id.profileimage);

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




        //getting users name

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
                    usersname.setText("Welcome "+use.getSsname());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    public void getAssignedVehicleOwner(){

        String SSid=firebaseAuth.getCurrentUser().getUid();
        DatabaseReference assignedVoRef=firebaseDatabase.getReference().child("Users").child("Service").child(SSid).child("HelpReqId");


        assignedVoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                            infrmcrdview.startAnimation(FabOpen);

                        helpreqId=dataSnapshot.getValue().toString();
                        System.out.println("ID OF THE CUSTOMER THAT REQ HELP: "+helpreqId);
                        getAssignedVOPickupLocation();

                        vwbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                viewCustomerLocation(helpreqId);
                            }
                        });





                        isNotifOpen=true;
                        crdopen=true;


                        //sending a notification




                }

                else{
                    System.out.println("FAILED TO GET THE HELP LOCATION ID");

                  if (crdopen){
                        infrmcrdview.startAnimation(FabClose);
                        crdopen=true;

                    }

                    if (isNotifOpen){
                    distancetext.startAnimation(FabClose);
                    helprequiredlocationmarker.remove();
                        nMap.clear();
                    isNotifOpen=false;
                   // Toast.makeText(SStMapActivity.this,"Customer has cancelled the request",Toast.LENGTH_LONG).show();
                    helpreqId="";
                }

                if (customerinfoopen){

                    customerinfo.startAnimation(FabClose);
                    customerinfoopen=false;
                }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void getHelpInfo(){

        String SSid=firebaseAuth.getCurrentUser().getUid();

        DatabaseReference nameref=firebaseDatabase.getReference().child("HelpInfo").child(SSid);
    //    DatabaseReference contactref=firebaseDatabase.getReference().child("HelpInfo").child(SSid).child(helpreqId).child("CustomerContact");
     //   DatabaseReference helprefnew=firebaseDatabase.getReference().child("HelpInfo").child(SSid).child(helpreqId).child("Description");

        nameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    try {
                        String name = dataSnapshot.child(helpreqId).child("Voname").getValue().toString();
                        String contct = dataSnapshot.child(helpreqId).child("Vocontact").getValue().toString();
                        String facilities = dataSnapshot.child(helpreqId).child("Facilities").getValue().toString();
                        String Problems = dataSnapshot.child(helpreqId).child("Troubles").getValue().toString();
                        String Other = dataSnapshot.child(helpreqId).child("Other").getValue().toString();

                        namet.setText("Customer Name: " + name);
                        contactt.setText("Contact: " + contct);
                        facilitiest.setText("Facilities need: " + facilities);
                        problemst.setText("Troubles having: " + Problems);
                        othert.setText("Other Info: " + Other);
                        System.out.println("METHOD INVOKEDDDDDDD");


                        // contactt.setText(dataSnapshot.getValue().toString());
                        System.out.println("METHOD INVOKEDDDDDDD");


                        // helpinfot.setText(dataSnapshot.getValue().toString());
                        System.out.println("METHOD INVOKEDDDDDDD");
                    }

                    catch (NullPointerException e){

                        System.out.println("EXCEPTIONNNNN");
                    }
                }



            /*   if(dataSnapshot.exists()) {

                   HelpInformation hi = dataSnapshot.getValue(HelpInformation.class);

                   String test=hi.getDescription();
                   System.out.println("INFOOOOOOOOOOOOOOOOO"+test);

                   System.out.println("IDDDDDD"+helpreqId );
                   namet.setText(hi.getVoname());
                   contactt.setText(hi.getVocontact());
                   helpinfot.setText(hi.getDescription());
               }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        contactref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<Object> contac=(List<Object>)dataSnapshot.getValue();

                    if (contac.get(2)!=null){
                    contactt.setText(contac.get(2).toString());
                    System.out.println("METHOD INVOKEDDDDDDD");}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        helprefnew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    List<Object> descrip=(List<Object>)dataSnapshot.getValue();
                    if (descrip.get(3)!=null){
                    helpinfot.setText(descrip.get(3).toString());
                    System.out.println("METHOD INVOKEDDDDDDD");}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



    }

    public void getAssignedVOPickupLocation(){


        DatabaseReference assignedvopickuplocationref=firebaseDatabase.getReference().child("RequestHelp").child(helpreqId).child("l");
        assignedvopickuplocationref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                 List<Object> map=(List<Object>)dataSnapshot.getValue();

                    double locationLat=0;
                    double locationLong=0;

                    if (map.get(0)!=null){

                        locationLat=Double.parseDouble(map.get(0).toString());
                    }

                    if (map.get(1)!=null){

                        locationLong=Double.parseDouble(map.get(1).toString());
                    }

                    else{
                        System.out.println("FAILED TO GET THE REQUIRED LOCATION");
                    }


                    towlatlng=new LatLng(locationLat,locationLong);

                    helpreqLocation=new Location("");
                    helpreqLocation.setLatitude(locationLat);
                    helpreqLocation.setLongitude(locationLong);


                    System.out.println("LOCATION OF THE NEEDED CUSTOMER: "+towlatlng);



                    if (helprequiredlocationmarker!=null){
                        helprequiredlocationmarker.remove();
                    }

                    helprequiredlocationmarker=nMap.addMarker(new MarkerOptions().position(towlatlng).title("Pickup Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.cbfinal)));

                  //drawing route
                    try{
                   getRouteToMarker(towlatlng);}

                   catch(NullPointerException e){

                       System.out.println("ROUTEEEE null");
                   }

                    distancetext.startAnimation(FabOpen);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    //method to draw the route to help location

    private void getRouteToMarker(LatLng latlngg) {


        Routing routing = new Routing.Builder()
                .key("AIzaSyDfyH_dmAkXGavtKgupsoCjfNp9Q2O1-Pk")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()), latlngg)
                .build();
        routing.execute();
    }

    public void viewCustomerLocation(String hid){

        DatabaseReference customerref=FirebaseDatabase.getInstance().getReference().child("RequestHelp");
        GeoFire customergeo=new GeoFire(customerref);

        customergeo.getLocation(hid, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location!=null){

                    LatLng cuslatLngview=new LatLng(location.latitude,location.longitude);

                    System.out.println("Retrieved location "+cuslatLngview);
                    nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cuslatLngview,19));


                }

                else{
                    System.out.println("Can't get the service location");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //method to get distance between two points
    public void getDistance(Location l1,Location l2){

        Location loc1=new Location("");
        loc1.setLatitude(l1.getLatitude());
        loc1.setLongitude(l1.getLongitude());

        Location loc2=new Location("");
        loc2.setLatitude(l2.getLatitude());
        loc2.setLongitude(l2.getLongitude());

        distancenew=loc1.distanceTo(loc2);

        distancetext.setText("Distance: "+distancenew);



    }



    @Override
    public void onLocationChanged(Location location) {

        if (getApplicationContext() != null) {

            lastLocation = location;

            LatLng latLng=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());


          //  nMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            if (!isLoggingOut) {

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference r = FirebaseDatabase.getInstance().getReference("Help Available");
            DatabaseReference refworkingservice = FirebaseDatabase.getInstance().getReference("CurrentlyServing");

            GeoFire geoFireAvailable = new GeoFire(r);
            GeoFire geoFireServing = new GeoFire(refworkingservice);



            switch (helpreqId){

                case "":
//Geofire


                        geoFireServing.removeLocation(userId, new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                            }
                        });

                        geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null) {
                                    System.err.println("There was an error saving the location to GeoFire: " + error);
                                } else {

                                 //   Toast.makeText(SStMapActivity.this, "Locating Available..", Toast.LENGTH_LONG).show();
                                    System.out.println("Available Location saved on server successfully!");
                                }
                            }
                        });


                    break;

                    default:

//Geofire



                          //  geoFireAvailable.removeLocation(userId);

                            geoFireAvailable.removeLocation(userId, new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                }
                            });

                            geoFireServing.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if (error != null) {
                                        System.err.println("There was an error saving the location to GeoFire: " + error);
                                    } else {

                                        //Toast.makeText(SStMapActivity.this, "Locating Service..", Toast.LENGTH_LONG).show();
                                        System.out.println("Serving Location saved on server successfully!");
                                    }
                                }
                            });


                        break;

            }


                checkCancelation();


                try{


                        getDistance(lastLocation,helpreqLocation);

                    if (distancenew<100){


                        arrivalss.setText("YOU HAVE REACHED THE LOCATION!");
                        arrivalss.setTextSize(19);
                        customername.setText("");
                        customercontact.setText("");

                        if (!finishOpen) {
                            finishbtn.startAnimation(FabOpenNew);
                            finishOpen=true;
                        }
                    }

            }

            catch (NullPointerException e){

                System.out.println("METHOD FAILEDDDDDDDDDD");


            }




            }





        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildGoogleApiClient();
        nMap=googleMap;
        nMap.setMyLocationEnabled(true);


        init();

    }

    protected synchronized void buildGoogleApiClient(){

        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mfusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SStMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQ_CODE );
        }


        mfusedLocationProviderClient.requestLocationUpdates(locationRequest,new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult.getLastLocation()!=null){


                   // Toast.makeText(SStMapActivity.this,"Lati: "+locationResult.getLastLocation().getLatitude()+"Long: "+locationResult.getLastLocation().getLongitude(),Toast.LENGTH_LONG).show();
                    lastLocation=locationResult.getLastLocation();




                    //animate camera

                 /*  LatLng latLng=new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude());


                    nMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    nMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                    */


                    //geofire successfull
/*
                    String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference r=FirebaseDatabase.getInstance().getReference("Vehicles Available");

                    GeoFire geoFire=new GeoFire(r);
                    geoFire.setLocation(userId, new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (error != null) {
                                System.err.println("There was an error saving the location to GeoFire: " + error);
                            } else {
                                System.out.println("Location saved on server successfully!");
                            }
                        }
                    });
                    */

                }

                onLocationChanged(locationResult.getLastLocation());

            }
        },getMainLooper() );


        mfusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                if (task.isSuccessful()){

                    Location loc=(Location)task.getResult();

                    LatLng lt=new LatLng(loc.getLatitude(),loc.getLongitude());


                    nMap.moveCamera(CameraUpdateFactory.newLatLng(lt));
                    nMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                }

                else {Toast.makeText(SStMapActivity.this,"task failed",Toast.LENGTH_LONG).show();}
            }
        });


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//geo



    //

    final int LOCATION_REQ_CODE=1;
    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){


            //new
            case LOCATION_REQ_CODE:{

                if(grantResults.length>0  &&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    supportMapFragment.getMapAsync(this);
                }

                else{

                    Toast.makeText(getApplicationContext(), "Please give permission", Toast.LENGTH_SHORT).show();
                }

                break;


            }

            case REQUEST_CALL:{

                if (grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    makePhoneCall();
                }

                else {Toast.makeText(SStMapActivity.this,"Permission denied", Toast.LENGTH_LONG).show();}

                break;
            }

            //
        }
    }


    //geo



    //nav
    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else{
            super.onBackPressed();

        }
    }

    //menulogout


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menunew,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.logoutmenu:{

                isLoggingOut=true;
                firebaseAuth.signOut();
                finish();
                Intent gotologinactivity=new Intent(SStMapActivity.this,LoginActivity.class);
                startActivity(gotologinactivity);
                break;




            }


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();

        //disconnectLocationUpdates();
       // disconnectApi();



    }




    public void disconnectLocationUpdates(){

        mfusedLocationProviderClient.removeLocationUpdates(new LocationCallback()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(SStMapActivity.this,"Disconnected",Toast.LENGTH_LONG).show();
                }

                else{Toast.makeText(SStMapActivity.this,"Task failed",Toast.LENGTH_LONG).show();}
            }
        });
    }

    public void disconnectApi(){

        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        googleApiClient.disconnect();
    }

    //methods for searchbar

    private void init(){

        System.out.println("Initializing");
        searchbarss.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId== EditorInfo.IME_ACTION_SEARCH || actionId==EditorInfo.IME_ACTION_DONE
                        ||keyEvent.getAction()==KeyEvent.ACTION_DOWN||keyEvent.getAction()==KeyEvent.KEYCODE_ENTER){

                    geoLocate();

                }
                return false;
            }
        });
    }

    private void geoLocate(){

        System.out.println("GeoLocating.......");

        String searchString =searchbarss.getText().toString();

        Geocoder geocoder=new Geocoder(SStMapActivity.this);

        List<Address> list=new ArrayList<>();

        try{

            list=geocoder.getFromLocationName(searchString,1);
        }

        catch (IOException e){

            System.out.println("Exception thrown"+e);
        }

        if (list.size()>0){

            Address address=list.get(0);

            System.out.println("SearchBar task successful "+address.toString());

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),18,address.getAddressLine(0));

        }
    }

    public void moveCamera(LatLng latlng, float zoom, String title){

        System.out.println("Moving camera...."+latlng.latitude+latlng.longitude);
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
        nMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));

        MarkerOptions markerOptions=new MarkerOptions()
                .position(latlng)
                .title(title);

        nMap.addMarker(markerOptions);

        hideSoftKeyboard();
    }

    public void hideSoftKeyboard(){

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }




    //routing methods

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};


    @Override
    public void onRoutingFailure(RouteException e) {


        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = nMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRoutingCancelled() {

    }

    public void erasePolyLines(){

        for (Polyline line:polylines){

            line.remove();
        }

        polylines.clear();
    }
}
