package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.loginapp.historyRecyclerView.HistoryAdapter;
import com.example.loginapp.historyRecyclerView.HistoryObject;
import com.example.loginapp.historyRecyclerView.HistoryViewHolders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.text.format.DateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;



public class HistoryActivity extends AppCompatActivity {


    private RecyclerView historyrecyclerView;
    private RecyclerView.Adapter historyAdapter;
    private RecyclerView.LayoutManager historyLayoutManager;

    private String VehicleOwnerOrService;
    private String userId;
    private long counter=0, datasnapshotsize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        historyrecyclerView=(RecyclerView)findViewById(R.id.historyrecyclerView);

        historyrecyclerView.setNestedScrollingEnabled(false);
        historyrecyclerView.setHasFixedSize(true);
        historyLayoutManager=new LinearLayoutManager(HistoryActivity.this);

        historyrecyclerView.setLayoutManager(historyLayoutManager);

        historyAdapter=new HistoryAdapter(getDataSetHistory(),HistoryActivity.this);

        historyrecyclerView.setAdapter(historyAdapter);



        VehicleOwnerOrService=getIntent().getExtras().getString("VehicleOwnerOrService");

        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
       getUserHistoryIds();



/*
        for( int i=0;i<100;i++){

HistoryObject obj=new HistoryObject(Integer.toString(i));
resultHistory.add(obj);
        }*/



    }

    private synchronized void getUserHistoryIds() {

        DatabaseReference userHistoryDatabase= FirebaseDatabase.getInstance().getReference().child("UserInformation").child(VehicleOwnerOrService).child(userId).child("History");
        userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    datasnapshotsize=dataSnapshot.getChildrenCount();

                    for (DataSnapshot history:dataSnapshot.getChildren()){

                        FetchHelpInformation (history.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private synchronized void FetchHelpInformation(String key) {

        DatabaseReference historydatabase= FirebaseDatabase.getInstance().getReference().child("History").child(key);
        historydatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){


                    String rideId=dataSnapshot.getKey();

                    Long timestamp=0L;


                    for (DataSnapshot child:dataSnapshot.getChildren()){


                        if (child.getKey().equals("timestamp")){

                            timestamp=Long.valueOf(child.getValue().toString());
                        }

                    }
                    HistoryObject obj=new HistoryObject(rideId ,getDate(timestamp));

                    resultHistory.add(obj);

                    counter++;

                    if(counter==datasnapshotsize){

                        counter=0;
                        datasnapshotsize=0;
                        historyAdapter.notifyDataSetChanged();

                    }}


                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String getDate(Long timestamp) {

        Calendar cal=Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);

        String date= DateFormat.format("yyyy-MM-dd hh:mm",cal).toString();

        return date;
    }


    private ArrayList resultHistory=new ArrayList<HistoryObject>();

    private ArrayList<HistoryObject> getDataSetHistory() {

        return resultHistory;
    }

}
