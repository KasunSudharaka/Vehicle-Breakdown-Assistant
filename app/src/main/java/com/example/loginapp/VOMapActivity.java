package com.example.loginapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import pl.droidsonroids.gif.GifImageView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
import com.google.android.gms.maps.model.MarkerOptions;
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


public class VOMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    private LatLng helpReqLocation;
    private CardView helpCardView;
    private TextView helpText,firstssname,secondssname,thirdssname,cnfrmtext;

    //new cardViews
    CardView mainbtn,firstss,secondss,thirdss;
    FloatingActionButton floatbtn,closebtn;
    private Button viewbtn1,viewbtn2,viewbtn3,slctbtn1,slctbtn2,slctbtn3;
   // TextView floattext;
    Animation FabOpen,FabClose,FabClockwise,FabAnticlockwise,FabOpenNew,FabCloseNew;
    boolean isOpen=false;
    boolean cardOpen=false;
    boolean cancelOpen=false;
    boolean confirmOpen=false;
    boolean serviceinfoopen=false;
    boolean timeopen=false;

    //searchbareditText

    private EditText searchbar;

    private static int count=0;
    private static int namecount=0;
    private static int arraycount=0;
    //array for ssnames
    private String [] ssname;

    //selected service station
    private String selectedss;

    //ssnames
    private String fname="Kasun";

    //ssinfosnippet
    private TextView ssnamesnipet,ssinfosnippet;
    private CardView ssinfosnippetcardview;


    //helprequirelocation marker
    private Marker helpReqMarker;

    public GoogleMap nMap;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient mfusedLocationProviderClient;


    //nav
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    //testbtn
    // private Button testbtn;
    //cancelbtn
    private Button cancelbtn;
    //gif
    private GifImageView loadinggif;
    private CardView confrim;

    private String imageUrl;

    //serviceinfo
    private LinearLayout serviceinfo;
    private ImageView serviceImage,clbtn3;
    private TextView agentname,agentcontact;
    private Button vwbtn3;
    private String agentmobile;
    private String agentImageUrl;
    private TextView arrival;

    private TextView timetext;
    private long timeleft =120000; //2mins
    private boolean timerrunning;
    private CountDownTimer countDownTimer;


    //rating elements

    private LinearLayout ratingPanel;
    private Animation animfadein;
    private Animation animfadeout;
    private boolean isfaded=false;

    private Button laterbtn;

    private RatingBar ratingBar;
    private String histId;

    private DatabaseReference historyRideInforef;

    //inforatingbra
    private RatingBar infoRating;

    //new helpbtn
    private ImageView helpButton;

    private float distance;

    private static final int REQUEST_CALL=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vomap);

        //helpCardView=(CardView)findViewById(R.id.reqCard);

       // helpText=(TextView)findViewById(R.id.helpText);

        helpButton=(ImageView)findViewById(R.id.helpimg);
        cnfrmtext=(TextView)findViewById(R.id.cnfrmtext);

        //initializing array
        ssname=new String[5];

        //new cardviews

        //mainbtn=(CardView)findViewById(R.id.reqCardnew);
        floatbtn=(FloatingActionButton)findViewById(R.id.floating);
        closebtn=(FloatingActionButton)findViewById(R.id.closebtn);
        //floattext=(TextView)findViewById(R.id.floattext);
        firstss=(CardView)findViewById(R.id.firstsscard);
        secondss=(CardView)findViewById(R.id.secondsscard);
        thirdss=(CardView)findViewById(R.id.thirdsscard);
        viewbtn1=(Button)findViewById(R.id.viewbtn1);
        viewbtn2=(Button)findViewById(R.id.viewbtn2);
        viewbtn3=(Button)findViewById(R.id.viewbtn3);
        slctbtn1=(Button)findViewById(R.id.slctbtn1);
        slctbtn2=(Button)findViewById(R.id.slctbtn2);
        slctbtn3=(Button)findViewById(R.id.slcbtn3);

        //gif
        loadinggif=(GifImageView)findViewById(R.id.loadinggif);

        FabOpen= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabClockwise=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabOpenNew=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open_new);
        FabCloseNew=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close_new);
        FabAnticlockwise=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        //searchbar

        searchbar=(EditText)findViewById(R.id.inputSearch);

        //ssnametextviews
        firstssname=(TextView)findViewById(R.id.helpTextnew);
        secondssname=(TextView)findViewById(R.id.helpTextnewww);
        thirdssname=(TextView)findViewById(R.id.helpTextnewwww);

        //testbtn
       // testbtn=(Button)findViewById(R.id.testbtn);

        //ssinfo snippet

        ssinfosnippetcardview=(CardView)findViewById(R.id.ssinfocard);
        ssnamesnipet=(TextView)findViewById(R.id.ssnamesnippet);
        ssinfosnippet=(TextView)findViewById(R.id.ssinfosnippet);

        cancelbtn=(Button)findViewById(R.id.cancelbtn);

        confrim=(CardView)findViewById(R.id.confirmlayout);

        //serviceinfo
        serviceinfo=(LinearLayout)findViewById(R.id.serviceinfo);
        serviceImage=(ImageView)findViewById(R.id.serviceimage);
        clbtn3=(ImageView)findViewById(R.id.callbtn3);
        vwbtn3=(Button)findViewById(R.id.vwbtn3);
        agentname=(TextView)findViewById(R.id.agentname);
        agentcontact=(TextView)findViewById(R.id.agentcontact);
        arrival=(TextView)findViewById(R.id.arrival);

        timetext=(TextView)findViewById(R.id.timetext);

        //ratingbar

        ratingPanel=(LinearLayout)findViewById(R.id.ratingpanel);
        laterbtn=(Button)findViewById(R.id.laterbtn);
        ratingBar=(RatingBar)findViewById(R.id.ratingbar);

        animfadein= AnimationUtils.loadAnimation(this,R.anim.fade);
        animfadeout= AnimationUtils.loadAnimation(this,R.anim.fadeout);

//inforating
        infoRating=(RatingBar)findViewById(R.id.inforating);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);



        //closebtn in info Cardview
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ssinfosnippetcardview.startAnimation(FabClose);
                firstss.startAnimation(FabOpen);
                secondss.startAnimation(FabOpen);
                thirdss.startAnimation(FabOpen);
                isOpen=true;
                cardOpen=false;

            }
        });

        //new cardView

        floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen){

                    floatbtn.startAnimation(FabAnticlockwise);
                    firstss.startAnimation(FabClose);
                    secondss.startAnimation(FabClose);
                    thirdss.startAnimation(FabClose);
                    firstss.setClickable(false);
                    secondss.setClickable(false);
                    thirdss.setClickable(false);
                    isOpen=false;


                    if(cardOpen){
                    ssinfosnippetcardview.startAnimation(FabClose);
                    cardOpen=false;
                    }

                }

                else {

                    floatbtn.startAnimation(FabClockwise);
                    firstss.startAnimation(FabOpen);
                    secondss.startAnimation(FabOpen);
                    thirdss.startAnimation(FabOpen);
                    firstss.setClickable(true);
                    secondss.setClickable(true);
                    thirdss.setClickable(true);
                    isOpen=true;

                    if(cardOpen){
                        ssinfosnippetcardview.startAnimation(FabClose);
                        cardOpen=false;
                    }


                    nMap.animateCamera(CameraUpdateFactory.zoomTo(13));


                }




            }
        });


        viewbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userid=ssname[2];

                viewbtn(userid);
                ssInfoSnippet(userid);
                ssinfosnippetcardview.startAnimation(FabOpen);
                firstss.startAnimation(FabClose);
                secondss.startAnimation(FabClose);
                thirdss.startAnimation(FabClose);
                isOpen=false;
                cardOpen=true;



            }
        });

        viewbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userid=ssname[1];

                viewbtn(userid);
                ssInfoSnippet(userid);
                ssinfosnippetcardview.startAnimation(FabOpen);
                firstss.startAnimation(FabClose);
                secondss.startAnimation(FabClose);
                thirdss.startAnimation(FabClose);
                isOpen=false;
                cardOpen=true;




            }
        });

        viewbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userid=ssname[0];

                viewbtn(userid);
                ssInfoSnippet(userid);
                ssinfosnippetcardview.startAnimation(FabOpen);
                firstss.startAnimation(FabClose);
                secondss.startAnimation(FabClose);
                thirdss.startAnimation(FabClose);
                isOpen=false;
                cardOpen=true;




            }
        });

        //selectbtns

        slctbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userid=ssname[2];
                selectedss=ssname[2];
                Intent gotoTow=new Intent(VOMapActivity.this,TowTruckActivity.class);
                gotoTow.putExtra("serviceid",userid);
                startActivity(gotoTow);
                markTow();
                dissapearButtons();
                cnfrmtext.setText("Request is pending...");
                openConfirmation();
                checkConfirmation();
                checkRejection();
                checkFinish();



            }
        });

        slctbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userid=ssname[1];
                selectedss=ssname[1];
                Intent gotoTow=new Intent(VOMapActivity.this,TowTruckActivity.class);
                gotoTow.putExtra("serviceid",userid);
                startActivity(gotoTow);
                markTow();
                dissapearButtons();
                cnfrmtext.setText("Request is pending...");
                openConfirmation();
                checkConfirmation();
                checkRejection();
                checkFinish();



            }
        });

        slctbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userid=ssname[0];
                selectedss=ssname[0];
                Intent gotoTow=new Intent(VOMapActivity.this,TowTruckActivity.class);
                gotoTow.putExtra("serviceid",userid);
                startActivity(gotoTow);
                markTow();
                dissapearButtons();
                cnfrmtext.setText("Request is pending...");
                openConfirmation();
                checkConfirmation();
                checkRejection();
                checkFinish();



            }
        });



        //cancel the request

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(VOMapActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure you want to cancel the request?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference usersref=FirebaseDatabase.getInstance().getReference("Users").child("Service").child(selectedss);
                                usersref.removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError!=null){

                                            System.out.println("Help request deleted successfully");
                                            Toast.makeText(VOMapActivity.this,"Request Cancelled",Toast.LENGTH_LONG);

                                        }

                                        else {
                                            System.out.println("Couldn't delete help request");
                                        }
                                    }
                                });


                                nMap.clear();

                                if (cancelOpen){

                                    cancelbtn.startAnimation(FabClose);
                                    cancelbtn.setClickable(false);
                                    cancelOpen=false;
                                }

                                if (timeopen){

                                    timetext.startAnimation(FabCloseNew);
                                    timeopen=false;

                                }

                                if (serviceinfoopen){

                                    serviceinfo.startAnimation(FabCloseNew);
                                    serviceinfoopen=false;
                                }
                                helpButton.setEnabled(true);
                                closeConfirmation();


                                LatLng back=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                                nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(back,16));




                                //creating cancel node and deleteing

                                String ussid=firebaseAuth.getCurrentUser().getUid();
                                final DatabaseReference cancelnode=firebaseDatabase.getReference().child("CancelRequest").child(ussid).child(selectedss);

                                HashMap cancl=new HashMap();
                                cancl.put("ServiceId",selectedss);
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

                                },10000);




                                final DatabaseReference helpinfonode=firebaseDatabase.getReference().child("HelpInfo").child(selectedss).child(ussid);

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


                                arrival.setText("");

                                if (timerrunning){
                                    resetSTimer();
                                }
                            }
                        });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();




            }

        });





        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(VOMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQ_CODE );
        }
        else {
            supportMapFragment.getMapAsync(this);
        }


        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference rr=FirebaseDatabase.getInstance().getReference("RequestHelp");
                GeoFire geo=new GeoFire(rr);
                geo.setLocation(uid,new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {

                            Toast.makeText(VOMapActivity.this,"Locating..",Toast.LENGTH_LONG).show();
                            System.out.println("Location saved on server successfully!");

                        }
                    }
                });


                if (helpReqMarker!=null){

                    helpReqMarker.remove();
                }

                try {
                    helpReqLocation= new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                    helpReqMarker= nMap.addMarker(new MarkerOptions().position(helpReqLocation).title("I'm here").icon(BitmapDescriptorFactory.fromResource(R.drawable.cbfinal)));
                    Toast.makeText(VOMapActivity.this,"Getting help.. Please wait..",Toast.LENGTH_LONG).show();
                    System.out.println("Marking successful");
                }

                catch (NullPointerException e){

                    System.out.println("ERORRRRRR"+e);
                }




                getClosestLocation();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        floatbtn.startAnimation(FabOpen);
                       // floattext.startAnimation(FabOpen);
                    }


                },3500);


            }
        });





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

                     Intent gotoVOProfile= new Intent(VOMapActivity.this,VOProfileActivity.class);

                     startActivity(gotoVOProfile);
                        break;

                    case R.id.dashboard:



                        Intent gotoDash1= new Intent(VOMapActivity.this,Dash_Board_1.class);

                        startActivity(gotoDash1);
                        break;

                    case R.id.about:


                        startActivity(new Intent(VOMapActivity.this,AboutUs
                                .class));

                        break;
                }


                return false;
            }
        });


        //testing choosemap

     /*   testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoCMap=new Intent(VOMapActivity.this,ChooseLocation.class);
                startActivity(gotoCMap);
            }
        });*/


     //rate later btn

        laterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fadeoutAnim();
            }
        });




     updateHeader();

     updateTimer();
    }


    //rating process

    public void rateService(){

        historyRideInforef=FirebaseDatabase.getInstance().getReference("History").child(histId);
        final DatabaseReference ssratingref=FirebaseDatabase.getInstance().getReference("UserInformation").child("Service Stations").child(selectedss).child("rating");


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {

historyRideInforef.child("rating").setValue(rating);

                ssratingref.child(histId).setValue(rating);

                fadeoutAnim();

            }
        });
    }



    //rating animations

    public void fadeinAnim(){

        if (!isfaded){

            ratingPanel.startAnimation(animfadein);
            isfaded=true;

            rateService();
        }}

    public void fadeoutAnim(){
        if (isfaded){

            ratingPanel.startAnimation(animfadeout);
            isfaded=false;
        }}




    //timer start

    public void startTimer(){

        countDownTimer=new CountDownTimer(timeleft,1000) {
            @Override
            public void onTick(long l) {

                timeleft=l;
                updateTimer();
            }

            @Override
            public void onFinish() {

                cancelbtn.setEnabled(false);
                cancelbtn.setAlpha(0.75f);
                timetext.setText("00:00");
                timerrunning=false;

                if (timeopen){
                    timetext.startAnimation(FabCloseNew);
                    timeopen=false;
                }

                resetSTimer();
            }
        }.start();

        timerrunning=true;
    }

    public void updateTimer(){

        int minutes=(int) timeleft/60000;
        int seconds=(int) timeleft%60000/1000;

        String timeleftText;

        timeleftText=""+"Cancelling expires: "+minutes;
        timeleftText+=":";

        if (seconds<10) timeleftText+="0";

        timeleftText+=seconds;

        timetext.setText(timeleftText);
    }

    public void resetSTimer(){

        countDownTimer.cancel();
        timeleft=120000;
        updateTimer();
    }




    //method to get distance between two points
    public void getDistance(Location l1,Location l2){

        Location loc1=new Location("");
        loc1.setLatitude(l1.getLatitude());
        loc1.setLongitude(l1.getLongitude());

        Location loc2=new Location("");
        loc2.setLatitude(l2.getLatitude());
        loc2.setLongitude(l2.getLongitude());

       distance=loc1.distanceTo(loc2);

       // distancetext.setText("Distance: "+distance);


    }


    //open customer info

    public void openServiceInfo(){


        DatabaseReference ser=firebaseDatabase.getReference().child("UserInformation").child("Service Stations").child(selectedss);
        DatabaseReference serurl=firebaseDatabase.getReference().child("Profile Images").child(selectedss);

        ser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    UserInformation uu=dataSnapshot.getValue(UserInformation.class);

                    agentname.setText("Agent Name: "+uu.getFullname());
                    agentcontact.setText("Contact No: "+uu.getMobile());
                    agentmobile=uu.getMobile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        serurl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                    if (map.get("profileImageUrl")!=null){
                        agentImageUrl=map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(agentImageUrl).into(serviceImage);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (!serviceinfoopen){

            serviceinfo.startAnimation(FabOpenNew);
            serviceinfoopen=true;

        }


        clbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });


    }


    public void makePhoneCall(){

        if (agentmobile.trim().length()>0){

            if (ContextCompat.checkSelfPermission(VOMapActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(VOMapActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }

            else {
                String dial="tel:" +agentmobile;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }

        else {
            Toast.makeText(VOMapActivity.this,"No phone number found",Toast.LENGTH_LONG).show();
        }


    }



    public void updateHeader(){

        View headerview=navigationView.getHeaderView(0);

final TextView usersname=headerview.findViewById(R.id.usersname);
      final  ImageView profileimage=headerview.findViewById(R.id.profileimage);

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
                    usersname.setText("Welcome "+use.getFullname());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //open confirmation
    public void openConfirmation(){

        if (!confirmOpen){
            confrim.startAnimation(FabOpen);
loadinggif.setVisibility(loadinggif.VISIBLE);
cnfrmtext.startAnimation(FabOpen);
            confirmOpen=true;
        }
    }

    //close confirmation

    public void closeConfirmation(){

        if (confirmOpen){
            confrim.startAnimation(FabClose);
            loadinggif.setVisibility(loadinggif.INVISIBLE);
            cnfrmtext.startAnimation(FabClose);
            confirmOpen=false;
        }
    }
    //check rejection

    public void checkRejection() {

        try{
        String newuid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference reject = firebaseDatabase.getReference().child("RejectRequest").child(selectedss).child(newuid).child("CustomerId");

        reject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    cnfrmtext.setText("Request Rejected!");

                    Toast.makeText(VOMapActivity.this, "Request rejected by the service station", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            cnfrmtext.setText("");
                            nMap.clear();

                            if (cancelOpen) {

                                cancelbtn.startAnimation(FabClose);
                                cancelbtn.setClickable(false);
                                cancelOpen = false;
                            }
                            helpButton.setEnabled(true);
                            closeConfirmation();


                        System.out.println("METHOD CALLED TWICE");
                            Intent gotovoreject = new Intent(VOMapActivity.this, VoRejectSplash.class);
                            startActivity(gotovoreject);




                        }

                    }, 3000);



                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            LatLng back = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                            nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(back, 16));

                        }


                    }, 4500);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    catch (NullPointerException e){

        System.out.println("Rejection method called early");
    }
    }


    //check finish

    public void checkFinish(){


        try{
            String finid = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference reject = firebaseDatabase.getReference().child("FinishRequest").child(selectedss).child(finid).child("FinishedCustomerId");

            //finishride id ref

            final DatabaseReference finishIdref= firebaseDatabase.getReference().child("FinishRequest");


            //getting history id

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    finishIdref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){

                                Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                                if (map.get("historyId")!=null){
                                    histId=map.get("historyId").toString();

                                    System.out.println("FINISHEDRIDEIDDDD"+histId);

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            },4000);



            //finishing
            reject.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                cnfrmtext.setText("");
                                nMap.clear();

                                if (cancelOpen) {

                                    cancelbtn.startAnimation(FabClose);
                                    cancelbtn.setClickable(false);
                                    cancelOpen = false;
                                }
                                helpButton.setEnabled(true);
                                closeConfirmation();


                                if (serviceinfoopen){

                                    serviceinfo.startAnimation(FabCloseNew);
                                    serviceinfoopen=false;
                                }

                                if (timeopen){

                                    timetext.startAnimation(FabCloseNew);
                                    timeopen=false;

                                }
                                System.out.println("METHOD CALLED TWICE");

                                if (timerrunning){
                                    resetSTimer();
                                }


                            }

                        }, 3000);


                        //opening rating bar


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                fadeinAnim();

                            }

                        },4500);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                LatLng back = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                                nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(back, 16));

                            }


                        }, 4500);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }

        catch (NullPointerException e){

            System.out.println("Finish method called early");
        }

    }
    //check confirmation

    public void checkConfirmation(){

        try{
        String usid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference check=firebaseDatabase.getReference().child("ConfirmRequest").child(selectedss).child(usid).child("CustomerId");

        check.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    cnfrmtext.setText("Request Confirmed!");
                    Toast.makeText(VOMapActivity.this,"Request confirmed by the service station",Toast.LENGTH_LONG).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            closeConfirmation();
                            cnfrmtext.setText("");

                            openServiceInfo();

                            if (!timeopen) {
                                timetext.startAnimation(FabOpenNew);
                                timeopen=true;
                                startTimer();
                            }

                        }

                    },3000);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }

        catch(NullPointerException e){

            System.out.println("Confirmation method called early");
        }
    }

    //invisible helpbuttons

    public void dissapearButtons(){

        if (isOpen){

            helpButton.setEnabled(false);
            floatbtn.startAnimation(FabAnticlockwise);
            floatbtn.startAnimation(FabClose);
            firstss.startAnimation(FabClose);
            secondss.startAnimation(FabClose);
            thirdss.startAnimation(FabClose);
            firstss.setClickable(false);
            secondss.setClickable(false);
            thirdss.setClickable(false);
            isOpen=false;

            if (!cancelOpen){

                cancelbtn.startAnimation(FabOpen);
                cancelbtn.setClickable(true);
                cancelOpen=true;
            }


        }

        else {
            helpButton.setEnabled(true);
            floatbtn.startAnimation(FabClockwise);
            floatbtn.startAnimation(FabOpen);
            firstss.startAnimation(FabOpen);
            secondss.startAnimation(FabOpen);
            thirdss.startAnimation(FabOpen);
            firstss.setClickable(true);
            secondss.setClickable(true);
            thirdss.setClickable(true);
            isOpen=true;


            if (cancelOpen){

                cancelbtn.startAnimation(FabClose);
                cancelbtn.setClickable(false);
                cancelOpen=false;
            }
        }


    }

    public void markTow(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getTowLocation();


            }



        },4000);

    }



    //marking towtruck location

    private Marker towMarker;

    public void getTowLocation(){



        DatabaseReference servicelocationref=firebaseDatabase.getReference().child("CurrentlyServing").child(selectedss).child("l");
        servicelocationref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    List<Object> hMap=(List<Object>)dataSnapshot.getValue();
                    double locationLat=0;
                    double locationLong=0;

                    if (hMap.get(0)!=null){

                        locationLat=Double.parseDouble(hMap.get(0).toString());
                    }

                    if (hMap.get(1)!=null){

                        locationLong=Double.parseDouble(hMap.get(1).toString());
                    }

                    final LatLng towlatlng=new LatLng(locationLat,locationLong);
                    if (towMarker!=null){

                        towMarker.remove();
                    }

                    try {
                        towMarker = nMap.addMarker(new MarkerOptions().position(towlatlng).title("Service on the way").icon(BitmapDescriptorFactory.fromResource(R.drawable.twfinal)));
                        System.out.println("Marking successful");

                        Location temp=new Location(LocationManager.GPS_PROVIDER);
                        temp.setLatitude(locationLat);
                        temp.setLongitude(locationLong);

                        getDistance(lastLocation,temp);

                        if (distance<100){


                            arrival.setText("THE HELP HAS ARRIVED!");
                            arrival.setTextSize(19);
                            agentname.setText("");
                            agentcontact.setText("");

                        }

                        vwbtn3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(towlatlng,19));

                            }
                        });
                    }

                    catch (NullPointerException e){

                        System.out.println("ERORRRRRR"+e);
                    }


                }



              /*  else{
                    nMap.clear();
                    cancelbtn.startAnimation(FabClose);
                    cancelbtn.setClickable(false);
                    cancelOpen=false;

                    if (cancelOpen){

                        cancelbtn.startAnimation(FabClose);
                        cancelbtn.setClickable(false);
                        cancelOpen=false;
                    }
                    helpCardView.setEnabled(true);
                    closeConfirmation();


                    LatLng back=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                    nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(back,16));
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public void getSsName(String ssid){

        final DatabaseReference ssref=FirebaseDatabase.getInstance().getReference().child("UserInformation").child("Service Stations").child(ssid);


        ssref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInformation userInformationss=dataSnapshot.getValue(UserInformation.class);

                fname=userInformationss.getSsname();



                if (namecount==0){

                    firstssname.setText(fname);
                    System.out.println("ssssssssssssssss"+fname);
                }

                if (namecount==1){

                    secondssname.setText(fname);
                    System.out.println("ssssssssssssssss"+fname);
                }

                if (namecount==2){

                    thirdssname.setText(fname);
                    System.out.println("ssssssssssssssss"+fname);
                }
                namecount++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //getting closest service station

    private  int radius=1;
    private boolean serviceFound=false;
    private String serviceFoundId;

    public void getClosestLocation(){

        DatabaseReference serviceLocation=FirebaseDatabase.getInstance().getReference().child("ServiceStationLocations");
        GeoFire geo=new GeoFire(serviceLocation);



        final GeoQuery geoQuery=geo.queryAtLocation(new GeoLocation(helpReqLocation.latitude,helpReqLocation.longitude),radius);
       geoQuery.removeAllListeners();


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                System.out.println("Retrieved key:"+key);


    if (!serviceFound ) {

        serviceFoundId = key;
       // Toast.makeText(VOMapActivity.this, "Service Station is " + serviceFoundId, Toast.LENGTH_LONG).show();
        System.out.println("Service Station is " + serviceFoundId);
        System.out.println(radius);







        ssname[arraycount] = key;
        System.out.println("array " + ssname[arraycount]);
        System.out.println("array index"+arraycount);
        arraycount++;

        System.out.println("Finisheddddddddddddddddddddddddddddddd");




        }

                if (arraycount>=3){

                    System.out.println("stoppppppppppppppppppppa");

                    serviceFound = true;
                    for (int i=0;i<=2;i++){

                        getSsName(ssname[i]);

                        // firstssname.setText(fname);
                        System.out.println("mmmmmmmmmmm" + fname);
                        markServiceStations(ssname[i]);



                    }




      /*    if (count == 0) {
                getSsName(ssname[count]);

                // firstssname.setText(fname);
                System.out.println("mmmmmmmmmmm" + fname);
                markServiceStations(ssname[count]);





            }

            if (count == 1) {


                getSsName(ssname[count]);

                // firstssname.setText(fname);
                System.out.println("kkkkkkkkkk" + fname);
                markServiceStations(ssname[count]);

               if(ssname.length==3){

                    for (int i=0;i<=3;i++){

                        getSsName(ssname[i]);

                        // firstssname.setText(fname);
                        System.out.println("kkkkkkkk" + fname);
                        markServiceStations(ssname[i]);}
                }


            }

            if (count == 2) {


                getSsName(ssname[count]);

                // firstssname.setText(fname);
                System.out.println("hhhhhhh" + fname);
                markServiceStations(ssname[count]);

                if(ssname.length==3){

                    for (int i=0;i<=3;i++){

                        getSsName(ssname[i]);

                        // firstssname.setText(fname);
                        System.out.println("hhhhhhh" + fname);
                        markServiceStations(ssname[i]);}
                }

            }*/

          //  count++;


         //   if (count == 3) {


         //   }



        }

    //}


            }


            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (!serviceFound){

                    radius++;
                   arraycount=0;
                    getClosestLocation();

                    //new
                   // if (radius==5){
                    //    serviceFound=true;
                   // }

                    System.out.println("NOT WORKINGGGGGGGGGG");
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }



    //marking nearest service stations

    public void markServiceStations(String ssidnew){

        DatabaseReference serviceLocationnew=FirebaseDatabase.getInstance().getReference().child("ServiceStationLocations");
        GeoFire ssgeo=new GeoFire(serviceLocationnew);

        ssgeo.getLocation(ssidnew, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location!=null){

                    LatLng sslatLng=new LatLng(location.latitude,location.longitude);

                    nMap.addMarker(new MarkerOptions().position(sslatLng).title("Service Station").icon(BitmapDescriptorFactory.fromResource(R.drawable.serviceic)));

                    System.out.println("Retrieved location "+sslatLng);


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






    @Override
    public void onLocationChanged(Location location) {

        if (getApplicationContext() != null) {


            lastLocation = location;

            LatLng latLng=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());


          //  nMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));



//Geofire


     /*      String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference r=FirebaseDatabase.getInstance().getReference("Vehicles Available");

            GeoFire geoFire=new GeoFire(r);
            geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {

                        Toast.makeText(VOMapActivity.this,"Locating..",Toast.LENGTH_LONG).show();
                        System.out.println("Location saved!");
                    }
                }
            });*/

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

            ActivityCompat.requestPermissions(VOMapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQ_CODE );
        }


        mfusedLocationProviderClient.requestLocationUpdates(locationRequest,new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult.getLastLocation()!=null){


                  //  Toast.makeText(VOMapActivity.this,"Lati: "+locationResult.getLastLocation().getLatitude()+"Long: "+locationResult.getLastLocation().getLongitude(),Toast.LENGTH_LONG).show();
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

                else {Toast.makeText(VOMapActivity.this,"task failed",Toast.LENGTH_LONG).show();}
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

                break;}


            case REQUEST_CALL:{

                if (grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    makePhoneCall();
                }

                else {Toast.makeText(VOMapActivity.this,"Permission denied", Toast.LENGTH_LONG).show();}

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

                firebaseAuth.signOut();
                finish();
                Intent gotologinactivity=new Intent(VOMapActivity.this,LoginActivity.class);
                startActivity(gotologinactivity);
                break;




            }


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();


      /*  mfusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);

        mfusedLocationProviderClient.removeLocationUpdates(locationCallback);

        String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference r=FirebaseDatabase.getInstance().getReference("Vehicles Available");

        GeoFire geoFire=new GeoFire(r);
            geoFire.removeLocation(userId);*/


      //disconnectLocationUpdates();
      //  disconnectApi();



    }




    public void disconnectLocationUpdates(){

        mfusedLocationProviderClient.removeLocationUpdates(new LocationCallback()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(VOMapActivity.this,"Disconnected",Toast.LENGTH_LONG).show();
                }

                else{Toast.makeText(VOMapActivity.this,"Task failed",Toast.LENGTH_LONG).show();}
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
        searchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        String searchString =searchbar.getText().toString();

        Geocoder geocoder=new Geocoder(VOMapActivity.this);

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

        nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));

        MarkerOptions markerOptions=new MarkerOptions()
                .position(latlng)
                .title(title);

        nMap.addMarker(markerOptions);

        hideSoftKeyboard();
    }

    public void hideSoftKeyboard(){

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void viewbtn(String ssidview){

        DatabaseReference serviceLocationview=FirebaseDatabase.getInstance().getReference().child("ServiceStationLocations");
        GeoFire ssgeo=new GeoFire(serviceLocationview);

        ssgeo.getLocation(ssidview, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location!=null){

                    LatLng sslatLngview=new LatLng(location.latitude,location.longitude);

                    System.out.println("Retrieved location "+sslatLngview);
                    nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sslatLngview,16));


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


    //getting ssinfo to the snippet

    public void ssInfoSnippet(String ssid){

        final DatabaseReference ssdataref=firebaseDatabase.getReference().child("UserInformation").child("Service Stations").child(ssid);

        ssdataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInformation us=dataSnapshot.getValue(UserInformation.class);

                String SSname=us.getSsname();
                String Address=us.getAddress();
                String Contact=us.getContactnumber();
                String Openhrs=us.getOpenhours();



                int ratingSum=0;
                int ratingTotal=0;
                int ratingAvg=0;

                for (DataSnapshot child:dataSnapshot.child("rating").getChildren()){

                    ratingSum=ratingSum+Integer.valueOf(child.getValue().toString());
                    ratingTotal++;
                }

                if (ratingTotal!=0){

                    ratingAvg=ratingSum/ratingTotal;
                    infoRating.setRating(ratingAvg);
                }

                ssnamesnipet.setText(SSname);
                ssinfosnippet.setText("Address: "+Address+ "\n"+"Contact No: "+Contact+"\n"+"Open Hrs: "+Openhrs+"\n"+"Rating: "+ratingAvg+"\n");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
