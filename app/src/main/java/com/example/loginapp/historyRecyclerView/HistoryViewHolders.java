package com.example.loginapp.historyRecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.loginapp.HistorySingleActivity;
import com.example.loginapp.R;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView rideId;
    public TextView time;
    public TextView idd;

    public HistoryViewHolders(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        rideId=(TextView) itemView.findViewById(R.id.rideId);
        time=(TextView) itemView.findViewById(R.id.time);
        idd=(TextView)itemView.findViewById(R.id.idd);

    }

    @Override
    public void onClick(View view) {

         Intent inten=new Intent(view.getContext(), HistorySingleActivity.class);
        Bundle b=new Bundle();
        b.putString("rideId",idd.getText().toString());
        b.putString("date",time.getText().toString());
        inten.putExtras(b);
        view.getContext().startActivity(inten);
    }
}
