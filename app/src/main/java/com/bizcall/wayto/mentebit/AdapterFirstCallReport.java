package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterFirstCallReport extends RecyclerView.Adapter<AdapterFirstCallReport.MyHolder> {
    Context context;
    ArrayList<DataFirstCallReport> arrayList;

    public AdapterFirstCallReport(Context context, ArrayList<DataFirstCallReport> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdapterFirstCallReport.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(context).inflate(R.layout.adapter_first_call,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFirstCallReport.MyHolder myHolder, int i) {
        DataFirstCallReport dataFirstCallReport=arrayList.get(i);
        myHolder.txtCallId.setText(dataFirstCallReport.getStrCallid());
        myHolder.txtCName.setText(dataFirstCallReport.getStrCounselorName());
        myHolder.txtCallDate.setText(dataFirstCallReport.getStrCallDate());
        myHolder.txtCallDuration.setText(dataFirstCallReport.getStrCallDuration());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtCallId,txtCName,txtCallDate,txtCallDuration;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtCallId=itemView.findViewById(R.id.txtCallId);
            txtCName=itemView.findViewById(R.id.txtCName);
            txtCallDate=itemView.findViewById(R.id.txtCallDate);
            txtCallDuration=itemView.findViewById(R.id.txtDuration);
        }
    }
}
