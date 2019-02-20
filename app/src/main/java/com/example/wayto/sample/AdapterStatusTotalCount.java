package com.example.wayto.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterStatusTotalCount extends RecyclerView.Adapter<AdapterStatusTotalCount.MyHolder> {
    ArrayList<DataStatusTotal> arrayList;
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public AdapterStatusTotalCount(ArrayList<DataStatusTotal> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterStatusTotalCount.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_statustotalcount, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterStatusTotalCount.MyHolder myHolder, int i) {
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        final DataStatusTotal dataStatusTotal = arrayList.get(i);
        myHolder.txtTotal.setText(dataStatusTotal.getTotal());
        myHolder.txtStatus.setText(dataStatusTotal.getStatus());
        if (dataStatusTotal.getStatus().contains("WRONG NUMBER")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.wrong_no);
        }
        if (dataStatusTotal.getStatus().contains("NOT YET SET")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.status1);
        }
        if (dataStatusTotal.getStatus().contains("NOT ELIGIBLE")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_eligible);
        }
        if (dataStatusTotal.getStatus().contains("NOT PICKED")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_picked);
        }
        if (dataStatusTotal.getStatus().contains("NOT REACHABLE")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_reachable);
        }
        if (dataStatusTotal.getStatus().contains("SWITCH OFF")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.switchoff);
        }
        if (dataStatusTotal.getStatus().contains("BUSY CALL BACK")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.busy_callback);
        }
        if (dataStatusTotal.getStatus().contains("ADMISSION ALREADY DONE")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.admission_already_done);
        }
        if (dataStatusTotal.getStatus().contains("NOT INTERESTED")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.status1);
        }
        if (dataStatusTotal.getStatus().contains("CONFIRM SUCCESS")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.status1);
        }
        if (dataStatusTotal.getStatus().contains("IN PROCESS")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.in_process);
        }
        if (dataStatusTotal.getStatus().contains("INTERESTED")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.status1);
        }
        if (dataStatusTotal.getStatus().contains("COLD")) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.status1);
        }
        myHolder.linearStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("SStatusId", dataStatusTotal.getCurrentstatus());
                editor.putString("SCStatus", dataStatusTotal.getStatus());
                editor.putString("Count", dataStatusTotal.getTotal());
                editor.commit();
                Intent intent = new Intent(context, CounsellorData.class);
                /*intent.putExtra("CStatusId",dataStatusTotal.getCurrentstatus());
                intent.putExtra("Count",dataStatusTotal.getTotal());
                intent.putExtra("Status",dataStatusTotal.getStatus());*/
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtTotal, txtStatus;
        ImageView imgStatus;
        LinearLayout linearStatus;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtTotal = itemView.findViewById(R.id.txtStatusCount);
            imgStatus = itemView.findViewById(R.id.btnStatus);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            linearStatus = itemView.findViewById(R.id.linearStatus);
        }
    }
}
