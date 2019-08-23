package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterStatusReport extends RecyclerView.Adapter<AdapterStatusReport.MyHolder> {
    ArrayList<DataStatusReport> arrayList;
    Context context;
    SharedPreferences sp;

    public AdapterStatusReport(ArrayList<DataStatusReport> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View view=LayoutInflater.from(context).inflate(R.layout.adapter_statusreport,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        DataStatusReport dataStatusReport=arrayList.get(i);
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        String act=sp.getString("Act",null);
        Log.d("AdapterAct",act);
        if(act.equals("StatusReport"))
        {
            myHolder.txtDataRef.setVisibility(View.GONE);
            myHolder.txtStatus.setText(dataStatusReport.getStatus());
            myHolder.txtTotal.setText(Integer.toString(dataStatusReport.getTotalcount()));
        }
        else {
            myHolder.txtDataRef.setText(dataStatusReport.getRefname());
            myHolder.txtStatus.setText(dataStatusReport.getStatus());
            myHolder.txtTotal.setText(Integer.toString(dataStatusReport.getTotalcount()));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtDataRef,txtStatus,txtTotal;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtDataRef=itemView.findViewById(R.id.txtRefName);
            txtStatus=itemView.findViewById(R.id.txtStatus);
            txtTotal=itemView.findViewById(R.id.txtTotalNo);
        }
    }
}
