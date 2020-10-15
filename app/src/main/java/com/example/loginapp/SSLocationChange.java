package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SSLocationChange extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private EditText searchbarcl;
    GoogleApiClient googleApiClient;
    public GoogleMap clMap;
    private SupportMapFragment supportMapFragmentcl;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    Location lastLocationcl;
    Location lastLocationclnew;
    LocationRequest locationRequestcl;
    LatLng temp;
    private Button confirmbtn;
    Marker marker;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sslocation_change);
        searchbarcl=(EditText)findViewById(R.id.inputSearch_cl);
        confirmbtn=(Button)findViewById(R.id.confirmbtn);

        firebaseAuth=FirebaseAuth.getInstance();

        supportMapFragmentcl = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map_cl);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SSLocationChange.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQ_CODE );
        }
        else {
            supportMapFragmentcl.getMapAsync(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if (getApplicationContext() != null) {


            lastLocationclnew = location;

            LatLng latLng=new LatLng(lastLocationclnew.getLatitude(),lastLocationclnew.getLongitude());}
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mfusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        locationRequestcl = new LocationRequest();
        locationRequestcl.setInterval(1000);
        locationRequestcl.setFastestInterval(1000);
        locationRequestcl.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SSLocationChange.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQ_CODE );
        }


        mfusedLocationProviderClient.requestLocationUpdates(locationRequestcl,new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult.getLastLocation()!=null){


                    //   Toast.makeText(ChooseLocation.this,"Lati: "+locationResult.getLastLocation().getLatitude()+"Long: "+locationResult.getLastLocation().getLongitude(),Toast.LENGTH_LONG).show();
                    lastLocationclnew=locationResult.getLastLocation();
                }

                onLocationChanged(locationResult.getLastLocation());

            }
        },getMainLooper() );


        mfusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                if (task.isSuccessful()){

                    lastLocationcl=(Location)task.getResult();

                    LatLng lt=new LatLng(lastLocationcl.getLatitude(),lastLocationcl.getLongitude());


                    clMap.moveCamera(CameraUpdateFactory.newLatLng(lt));
                    clMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                }

                else {
                    Toast.makeText(SSLocationChange.this,"task failed",Toast.LENGTH_LONG).show();}
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildGoogleApiClient();
        clMap=googleMap;
        clMap.setMyLocationEnabled(true);


        init();
        confirmLocation();

    }

    protected synchronized void buildGoogleApiClient(){

        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }
    final int LOCATION_REQ_CODE=1;

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){


            //new
            case LOCATION_REQ_CODE:{

                if(grantResults.length>0  &&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    supportMapFragmentcl.getMapAsync(this);
                }

                else{

                    Toast.makeText(getApplicationContext(), "Please give permission", Toast.LENGTH_SHORT).show();
                }

                break;}
            //
        }
    }

    private void init(){

        System.out.println("Initializing");
        searchbarcl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        String searchString =searchbarcl.getText().toString();

        Geocoder geocoder=new Geocoder(SSLocationChange.this);

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

            // moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),18,address.getAddressLine(0));
            LatLng ln=new LatLng(address.getLatitude(),address.getLongitude());
            clMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ln,18));

            temp=ln;

            if(marker!=null){
                marker.remove();
            }



            MarkerOptions markerOptionscl=new MarkerOptions()
                    .position(temp)
                    .title("Is this your service station?").draggable(true);

            marker=clMap.addMarker(markerOptionscl);

            saveconfirmed();

            clMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    temp=marker.getPosition();

                }
            });



        }
    }

    public void moveCamera(LatLng latlng, float zoom, String title){

        System.out.println("Moving camera...."+latlng.latitude+latlng.longitude);
        clMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
        clMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));

        MarkerOptions markerOptions=new MarkerOptions()
                .position(latlng)
                .title(title);

        clMap.addMarker(markerOptions);

        hideSoftKeyboard();
    }

    public void hideSoftKeyboard(){

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public void confirmLocation(){




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String userid = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference sslocationref=FirebaseDatabase.getInstance().getReference().child("ServiceStationLocations");
                GeoFire ssgeo=new GeoFire(sslocationref);


                ssgeo.getLocation(userid, new com.firebase.geofire.LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if (location!=null){

                            LatLng ll=new LatLng(location.latitude,location.longitude);

                            System.out.println("Retrieved location "+ll);

                            MarkerOptions markerOptionscl=new MarkerOptions()
                                    .position(ll)
                                    .title("Is this your service station?").draggable(true);

                            marker=clMap.addMarker(markerOptionscl);
                            clMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,19));

                            temp=ll;

                        }

                        else{
                            System.out.println("Can't get the service location");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                // LatLng ll=new LatLng(lastLocationcl.getLatitude(),lastLocationcl.getLongitude());



            }



        },3000);

        saveconfirmed();

        clMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                temp=marker.getPosition();

            }
        });

    }

    public void saveconfirmed(){


        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uidss= FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference servicelocations= FirebaseDatabase.getInstance().getReference("ServiceStationLocations");
                GeoFire geoss=new GeoFire(servicelocations);

                geoss.setLocation(uidss, new GeoLocation(temp.latitude, temp.longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {

                            Toast.makeText(SSLocationChange.this,"location Updated",Toast.LENGTH_LONG).show();
                            System.out.println("Location saved on server successfully!");


                            finish();

                        }
                    }
                });
            }
        });


    }
}

