package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FuelMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private EditText searchbarf;
    GoogleApiClient googleApiClient;
    public GoogleMap fmap;
    private SupportMapFragment supportMapFragmentf;
    private FusedLocationProviderClient mfusedLocationProviderClient;

    Location lastLocationf;
    Location lastLocationclnew;
    LocationRequest locationRequestf;
    LatLng temp;
    private Button fuelbtn;
    Marker marker;
    private FirebaseAuth firebaseAuth;

    private double latitude,longtitude;
   private String hospital="hospital";
    private String fuelstation="gas_station";


    private int proximityRadius=10000;

    private Button selectbtn;

    private boolean isSelectOpen=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_map);


        searchbarf=(EditText)findViewById(R.id.inputSearch_f);
        fuelbtn=(Button)findViewById(R.id.fuelbtn);

        firebaseAuth=FirebaseAuth.getInstance();

        supportMapFragmentf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map_f);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(FuelMap.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQ_CODE );
        }
        else {
            supportMapFragmentf.getMapAsync(this);
        }

        searchNearbyFuel();



    }


    public void searchNearbyFuel(){

final Object transferData[]=new Object[2];
final GetNearbyPlaces getNearbyPlaces=new GetNearbyPlaces();

        fuelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url=getUrl(latitude,longtitude,fuelstation);

                transferData[0]=fmap;
                transferData[1]=url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(FuelMap.this,"Searching For Nearby Fuel Stations",Toast.LENGTH_LONG).show();
                Toast.makeText(FuelMap.this,"Showing Nearby Fuel Stations",Toast.LENGTH_LONG).show();


            }
        });
    }


    private String getUrl(double latitude, double longtitude, String nearbyPlace){

StringBuilder googleURl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
googleURl.append("location="+latitude+","+longtitude);
googleURl.append("&radius="+proximityRadius);

googleURl.append("&type="+ nearbyPlace);
googleURl.append("&sensor=true");
googleURl.append("&key="+"AIzaSyCZ7EYFZw-A_fkFJOwpPsB1-47c1h_HiH8");


        System.out.println("URLLLLLLL"+googleURl.toString());

return googleURl.toString();
    }


    @Override
    public void onLocationChanged(Location location) {
        if (getApplicationContext() != null) {


            latitude=location.getLatitude();
            longtitude=location.getLongitude();
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

        locationRequestf = new LocationRequest();
        locationRequestf.setInterval(1000);
        locationRequestf.setFastestInterval(1000);
        locationRequestf.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(FuelMap.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQ_CODE );
        }


        mfusedLocationProviderClient.requestLocationUpdates(locationRequestf,new LocationCallback(){

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

                    lastLocationf=(Location)task.getResult();

                    LatLng lt=new LatLng(lastLocationf.getLatitude(),lastLocationf.getLongitude());


                    fmap.moveCamera(CameraUpdateFactory.newLatLng(lt));
                    fmap.animateCamera(CameraUpdateFactory.zoomTo(18));
                }

                else {
                    Toast.makeText(FuelMap.this,"task failed",Toast.LENGTH_LONG).show();}
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
       fmap=googleMap;
        fmap.setMyLocationEnabled(true);


        init();
       // confirmLocation();

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

                    supportMapFragmentf.getMapAsync(this);
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
        searchbarf.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        String searchString =searchbarf.getText().toString();

        Geocoder geocoder=new Geocoder(FuelMap.this);

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
            fmap.moveCamera(CameraUpdateFactory.newLatLngZoom(ln,18));

            temp=ln;

            if(marker!=null){
                marker.remove();
            }



            MarkerOptions markerOptionscl=new MarkerOptions()
                    .position(temp)
                    .title("Searched Location").draggable(true);

            marker=fmap.addMarker(markerOptionscl);

            //saveconfirmed();

            fmap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
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

}
