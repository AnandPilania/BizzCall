package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterStatus extends RecyclerView.Adapter<AdapterStatus.MyHolder> {
    Context context;
    ArrayList<DataStatus> arrayList;

    public AdapterStatus(Context context, ArrayList<DataStatus> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdapterStatus.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.adapter_remarks,viewGroup,false);
        return new AdapterStatus.MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterStatus.MyHolder myHolder, int i)
    {
        DataStatus dataStatus=arrayList.get(i);
        myHolder.txtRemark.setText(dataStatus.getStatus());
        myHolder.txtStatusDateTime.setText(dataStatus.getStatus_dt_time());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtRemark,txtStatusDateTime;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtRemark=itemView.findViewById(R.id.txtRemarkBackup);
            txtStatusDateTime=itemView.findViewById(R.id.txtDateTime);
        }
    }
}
