package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestActivity extends AppCompatActivity {

    private LinearLayout ratingPanel;
    private Animation animfadein;
    private Animation animfadeout;
    private Animation animslideup;
    private Animation animslidedown;
    Animation FabOpenNew,FabCloseNew;
    private boolean isfaded=false;
    private boolean isslided=false;
    private boolean isRateOpen=false;

    private Button laterbtn;



    //counter

    private TextView testCounter;
    int ratingAvg=0;



    //
    private String userid;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private int serviceCount=0;

    private int fives=0,fours=0,threes=0,twos=0,ones=0;

    private TextView fivet,fourt,threet,twot,onet,averaget;
    private RatingBar rfive,rfour,rthree,rtwo,rone,raverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ratingPanel=(LinearLayout)findViewById(R.id.ratingpanel);
        //laterbtn=(Button)findViewById(R.id.laterbtn);

        animfadein= AnimationUtils.loadAnimation(this,R.anim.fade);
         animfadeout= AnimationUtils.loadAnimation(this,R.anim.fadeout);
        animslideup= AnimationUtils.loadAnimation(this,R.anim.slideup);
        animslidedown= AnimationUtils.loadAnimation(this,R.anim.slidedown);
        FabOpenNew= AnimationUtils.loadAnimation(this,R.anim.fab_open_new);
        FabCloseNew= AnimationUtils.loadAnimation(this,R.anim.fab_close_new);


//counter
        testCounter=(TextView)findViewById(R.id.testcount);

        rfive=(RatingBar) findViewById(R.id.rfive);
        rfour=(RatingBar) findViewById(R.id.rfour);
        rthree=(RatingBar) findViewById(R.id.rthree);
        rtwo=(RatingBar) findViewById(R.id.rtwo);
        rone=(RatingBar) findViewById(R.id.rone);
        raverage=(RatingBar)findViewById(R.id.raverage);

        fivet=(TextView)findViewById(R.id.fivet);
        fourt=(TextView)findViewById(R.id.fourt);
        threet=(TextView)findViewById(R.id.threet);
        twot=(TextView)findViewById(R.id.twot);
        onet=(TextView)findViewById(R.id.onet);
        averaget=(TextView)findViewById(R.id.averaget);




        //getting counts

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        userid=firebaseAuth.getCurrentUser().getUid();


        fadeinAnim();

        getCounts();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                populateCounts();
            }
        },2000);









       /* laterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fadeoutAnim();
            }
        });*/
    }


    //populating the counts

    public void populateCounts(){

        countingapp(-1,fives,fivet);
        countingapp(-1,fours,fourt);
        countingapp(-1,threes,threet);
        countingapp(-1,twos,twot);
        countingapp(-1,ones,onet);
        countingapp(-1,ratingAvg,averaget);

        countingapp(-1,serviceCount,testCounter);


        rfive.setRating(5);
        rfour.setRating(4);
        rthree.setRating(3);
        rtwo.setRating(2);
        rone.setRating(1);
        raverage.setRating(ratingAvg);





    }

    //counter app method

    public void countingapp(int start, int end, TextView textView){

        AnimateCounter animateCounter=new AnimateCounter.Builder(textView)
                .setCount(start,end)
                .setDuration(2000)
                .build();
        animateCounter.execute();
    }



    //getting the counts

    public void getCounts(){

        final DatabaseReference servicecountref=firebaseDatabase.getReference().child("UserInformation").child("Service Stations").child(userid);

        servicecountref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // calculating rating

                int ratingSum=0;
                int ratingTotal=0;

                int value;


                for (DataSnapshot child:dataSnapshot.child("rating").getChildren()){

                    value=Integer.valueOf(child.getValue().toString());
                    ratingSum=ratingSum+value;
                    ratingTotal++;


                    if (value==5){
                        fives++;
                    }


                    if (value==4){
                        fours++;
                    }


                    if (value==3){
                        threes++;
                    }

                    if (value==2){
                        twos++;
                    }

                    if (value==1){
                        ones++;
                    }

                    System.out.println("Fives"+fives+fours+threes+twos+ones);

                }

                if (ratingTotal!=0){

                    ratingAvg=ratingSum/ratingTotal;
                   // infoRating.setRating(ratingAvg);

                    System.out.println("AVERAGE RATINGG"+ratingAvg);
                }


                //calculating service count

                serviceCount=0;

                for (DataSnapshot child:dataSnapshot.child("History").getChildren()){

                    serviceCount++;

                    System.out.println("SERVICE COUNTTT"+serviceCount);

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }



    public void fadeinAnim(){

        if (!isfaded){

        ratingPanel.startAnimation(animfadein);
        isfaded=true;
    }}

    public void fadeoutAnim(){
        if (isfaded){

        ratingPanel.startAnimation(animfadeout);
        isfaded=false;
    }}


    public void slideUp(){

        if (!isslided){

            ratingPanel.startAnimation(animslideup);
            isslided=true;
        }
    }

    public void slideDown(){

        if (isslided){

            ratingPanel.startAnimation(animslidedown);
            isslided=false;
        }

    }

    public void fabopenNew(){

        if (!isRateOpen){

            ratingPanel.startAnimation(FabOpenNew);
            isRateOpen=true;
        }}

    public void fabcloseNew(){

        if (isRateOpen){

            ratingPanel.startAnimation(FabCloseNew);
            isRateOpen=false;
        }}

}
