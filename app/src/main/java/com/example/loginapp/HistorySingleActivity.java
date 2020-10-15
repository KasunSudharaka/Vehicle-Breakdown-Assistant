package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class HistorySingleActivity extends AppCompatActivity  implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private  String rideId;
    private String time;
    private String servid;
    private String SSname;
    private String SScont;
    private String Trouble;
    private String Facility;
    private String agentImageUrl;
    String logid;

    Location historyloc;

    private Marker historyMarker;

    private LatLng histlatlng;

    GoogleApiClient googleApiClient;




    private TextView servicenametext,contacttext,trouble,facility,date;

    private ImageView userim;


    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_single);

        Intent inten=getIntent();

        rideId=inten.getStringExtra("rideId").toString();
        time=inten.getStringExtra("date").toString();
        logid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        System.out.println("RIDE IDDDDD"+rideId);

        mMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapp);


        servicenametext=(TextView)findViewById(R.id.ssname);
        contacttext=(TextView)findViewById(R.id.cont);
        trouble=(TextView)findViewById(R.id.tro);
        facility=(TextView)findViewById(R.id.faci);
        date=(TextView)findViewById(R.id.date);
        userim=(ImageView)findViewById(R.id.userim);


        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();


        mMapFragment.getMapAsync(this);





        final DatabaseReference hisref= FirebaseDatabase.getInstance().getReference().child("History").child(rideId);



        hisref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                    if (map.get("driver")!=null){
                        servid=map.get("driver").toString();
                        Trouble=map.get("trouble").toString();
                        Facility=map.get("facility").toString();
                        System.out.println("IDDDDDDDDDDDD"+servid);


                    }

                    final DatabaseReference service= FirebaseDatabase.getInstance().getReference().child("UserInformation").child("Service Stations").child(servid);

                    if (!servid.equals(logid)) {
                        service.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    UserInformation us = dataSnapshot.getValue(UserInformation.class);

                                    SSname = us.getSsname();
                                    SScont = us.getContactnumber();
                                    System.out.println("NAMMEEEEEE" + SSname);
                                    servicenametext.setText("Service Station: " + SSname);
                                    contacttext.setText("Contact Number: " + SScont);

                                    trouble.setText(Trouble);
                                    facility.setText(Facility);
                                    date.setText("Date: " + time);


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }




                    DatabaseReference serurl=firebaseDatabase.getReference().child("Profile Images").child(servid);

                    if (!servid.equals(logid)) {
                        serurl.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                    if (map.get("profileImageUrl") != null) {
                                        agentImageUrl = map.get("profileImageUrl").toString();
                                        Glide.with(getApplication()).load(agentImageUrl).into(userim);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        getHistoryLocation();

                    }

                }












            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //service station interface

        hisref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                    if (map.get("vehowner")!=null){
                        servid=map.get("vehowner").toString();
                        Trouble=map.get("trouble").toString();
                        Facility=map.get("facility").toString();
                        System.out.println("IDDDDDDDDDDDD"+servid);


                    }

                    final DatabaseReference service= FirebaseDatabase.getInstance().getReference().child("UserInformation").child("Vehicle Owners").child(servid);

                    if (!servid.equals(logid)) {
                        service.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    VehicleOwnerInformation veh = dataSnapshot.getValue(VehicleOwnerInformation.class);

                                    SSname = veh.getFullname();
                                    SScont = veh.getMobile();
                                    System.out.println("NAMMEEEEEE" + SSname);
                                    servicenametext.setText("Customer Name: " + SSname);
                                    contacttext.setText("Contact Number: " + SScont);

                                    trouble.setText(Trouble);
                                    facility.setText(Facility);
                                    date.setText("Date: " + time);


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }




                    DatabaseReference serurl=firebaseDatabase.getReference().child("Profile Images").child(servid);


                    if (!servid.equals(logid)) {
                        serurl.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                    if (map.get("profileImageUrl") != null) {
                                        agentImageUrl = map.get("profileImageUrl").toString();
                                        Glide.with(getApplication()).load(agentImageUrl).into(userim);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        getHistoryLocation();


                    }
                }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });












    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildGoogleApiClient();
        mMap=googleMap;
        mMap.setMyLocationEnabled(true);




        // init();

    }

    protected synchronized void buildGoogleApiClient(){

        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }




    public void getHistoryLocation(){

        DatabaseReference historylocationref=firebaseDatabase.getReference().child("History").child(rideId).child("location");



        GeoFire customergeo=new GeoFire(historylocationref);

        customergeo.getLocation(rideId, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location!=null){

                    histlatlng=new LatLng(location.latitude,location.longitude);

                    System.out.println("Retrieved location "+histlatlng);

                    System.out.println("HISTORYLOCATIONNNNN"+histlatlng);
                    historyMarker=mMap.addMarker(new MarkerOptions().position(histlatlng).title("Breakdown Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.cbfinal)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(histlatlng,19));


                }

                else{
                    System.out.println("Can't get the service location");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        /*historylocation.addValueEventListener(new ValueEventListener() {
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


                    histlatlng=new LatLng(locationLat,locationLong);

                    historyloc=new Location("");
                    historyloc.setLatitude(locationLat);
                    historyloc.setLongitude(locationLong);


                    System.out.println("LOCATION OF THE HISTORY: "+historyloc);



                    if (historyMarker!=null){
                        historyMarker.remove();
                    }

                    System.out.println("HISTORYLOCATIONNNNN"+historyloc);
                    historyMarker=mMap.addMarker(new MarkerOptions().position(histlatlng).title("Breakdown Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.cbfinal)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(histlatlng, 16));*/


                 /*   //drawing route
                    try{
                   getRouteToMarker(towlatlng);}

                   catch(NullPointerException e){

                       System.out.println("ROUTEEEE null");
                   }*/

                    //distancetext.startAnimation(FabOpen);

             /*   }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
