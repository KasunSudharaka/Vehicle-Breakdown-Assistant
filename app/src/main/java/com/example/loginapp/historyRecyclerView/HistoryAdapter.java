package com.example.loginapp.historyRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginapp.R;
import com.example.loginapp.UserInformation;
import com.example.loginapp.VehicleOwnerInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolders> {
    private List<HistoryObject> itemList;
    private Context context;
    private String serid;
    private String SSname;
    String historyid;
    String logid;




    public HistoryAdapter(List<HistoryObject> itemList, Context context){

        this.itemList=itemList;
        this.context=context;
    }


    @NonNull
    @Override
    public HistoryViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,null,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        HistoryViewHolders rev=new HistoryViewHolders(layoutView);

        return rev;
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolders holder, int position) {


        historyid=itemList.get(position).getRideId();

        logid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference hisref= FirebaseDatabase.getInstance().getReference().child("History").child(historyid);



        hisref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                    if (map.get("driver")!=null){
                        serid=map.get("driver").toString();
                        System.out.println("IDDDDDDDD"+serid);


                    }

                    final DatabaseReference service= FirebaseDatabase.getInstance().getReference().child("UserInformation").child("Service Stations").child(serid);


                    if (!serid.equals(logid)) {
                        service.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    UserInformation us = dataSnapshot.getValue(UserInformation.class);

                                    SSname = us.getSsname();
                                    holder.rideId.setText(SSname);


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //service station owner

        hisref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                    if (map.get("vehowner")!=null){
                        serid=map.get("vehowner").toString();
                        System.out.println("IDDDDDDDD"+serid);


                    }

                    final DatabaseReference service= FirebaseDatabase.getInstance().getReference().child("UserInformation").child("Vehicle Owners").child(serid);


                    if (!serid.equals(logid)) {
                        service.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    VehicleOwnerInformation veh = dataSnapshot.getValue(VehicleOwnerInformation.class);

                                    SSname = veh.getFullname();
                                    holder.rideId.setText(SSname);


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        System.out.println("NAMEEEEE"+SSname);

        holder.time.setText(itemList.get(position).getTime());
        holder.idd.setText(itemList.get(position).getRideId());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
