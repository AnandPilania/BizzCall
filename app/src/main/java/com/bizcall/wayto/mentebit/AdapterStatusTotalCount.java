package com.bizcall.wayto.mentebit;

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
    ArrayList<String> arrayListRefId;

    public AdapterStatusTotalCount(ArrayList<DataStatusTotal> arrayList,  Context context) {
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
      //  DataDtaFrom datafrom=arrayListRefId.get(i);
        final DataStatusTotal dataStatusTotal = arrayList.get(i);
        myHolder.txtTotal.setText(dataStatusTotal.getTotal());
        myHolder.txtStatus.setText(dataStatusTotal.getStatus());

      /*  for(int i=0;i<arrayList.size();i++) {*/
        if(i==0)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.wrongnumber_icon);
        }
        if(i==1)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_set_icon);
        }
        if(i==2)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_eligible_icon);
        }
        if(i==3)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_picked_icon);
        }
        if(i==4)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_reachable_icon);
        }
        if(i==5) {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_eligible_icon);
        }
        if(i==6)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_picked_icon);
        }
        if(i==7)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_eligible_icon);
        }
        if(i==8)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_set_icon);
        }
        if(i==9)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.clients_done_icon);
        }
        if(i==10)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_set_icon);
        }
        if(i==11)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.clients_heart_intrested_icon);
        }
        if(i==12)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.call_icon);
        }
        if(i==13)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_reachable_icon);
        }
        if(i==14)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_picked_icon);
        }
        if(i==15)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_set_icon);
        }
        if(i==15)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.wrongnumber_icon);
        }
        if(i==16)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.call_icon);
        }
        if(i==17)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.clients_done_icon);
        }
        if(i==18)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.clients_heart_intrested_icon);
        }
        if(i==19)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_eligible_icon);
        }
        if(i==20)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.call_icon);
        }
        if(i==21)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.wrongnumber_icon);
        }
        if(i==22)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_set_icon);
        }
        if(i==23)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.not_picked_icon);
        }
        if(i==24)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.clients_heart_intrested_icon);
        }
        if(i==25)
        {
            myHolder.imgStatus.setBackgroundResource(R.drawable.call_icon);
        }
          /*  if (dataStatusTotal.getStatus().contains("WRONG NUMBER")) {
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
            if (dataStatusTotal.getStatus().contains("Block Number")) {
                myHolder.imgStatus.setBackgroundResource(R.drawable.switchoff);
            }
            if (dataStatusTotal.getStatus().contains("BUSY CALL BACK")) {
                myHolder.imgStatus.setBackgroundResource(R.drawable.busy_callback);
            }
            if (dataStatusTotal.getStatus().contains("ADMISSION ALREADY DONE")) {
                myHolder.imgStatus.setBackgroundResource(R.drawable.admission_already_done);
            }
            if (dataStatusTotal.getStatus().contains("Not Interested")) {
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
            }*/
      //  }
        myHolder.linearStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("SStatusId", dataStatusTotal.getCurrentstatus());
                editor.putString("SCStatus", dataStatusTotal.getStatus());
                editor.putString("Count", dataStatusTotal.getTotal());

                editor.commit();
                Intent intent = new Intent(context, CounsellorData.class);
                intent.putExtra("Activity","Home");
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
