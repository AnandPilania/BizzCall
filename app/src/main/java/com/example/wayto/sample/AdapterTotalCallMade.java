package com.example.wayto.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterTotalCallMade extends RecyclerView.Adapter<AdapterTotalCallMade.MyHolder> {
    ArrayList<DataTotalCallMade> arrayList;
    Context context;

    public AdapterTotalCallMade(Context context, ArrayList<DataTotalCallMade> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterTotalCallMade.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.lyout_adapter_total_calls_made, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTotalCallMade.MyHolder myHolder, int i) {
        DataTotalCallMade dataTotalCallMade = arrayList.get(i);
        myHolder.txtSr.setText(dataTotalCallMade.getSrno());
        myHolder.txtDuration.setText(dataTotalCallMade.getDuration());
        myHolder.txtDate.setText(dataTotalCallMade.getDate1());
        myHolder.txtFile.setText(dataTotalCallMade.getFilename());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView txtSr, txtDuration, txtDate, txtFile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtSr = itemView.findViewById(R.id.txtSrNo);
            txtDuration = itemView.findViewById(R.id.txtCallDuration);
            txtDate = itemView.findViewById(R.id.txtCallDate);
            txtFile = itemView.findViewById(R.id.txtFile);
        }
    }
}
