package com.bizcall.wayto.mentebit13;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRemarks extends RecyclerView.Adapter<AdapterRemarks.MyHolder> {
   Context context;
   ArrayList<DataRemark> arrayList;

    public AdapterRemarks(Context context, ArrayList<DataRemark> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(context).inflate(R.layout.adapter_remarks,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i)
    {
        DataRemark dataRemark=arrayList.get(i);
        myHolder.txtRemark.setText(dataRemark.getRemarks());
        myHolder.txtDateTime.setText(dataRemark.getRemarkDateTime());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       TextView txtRemark,txtDateTime;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtRemark=itemView.findViewById(R.id.txtRemarkBackup);
            txtDateTime=itemView.findViewById(R.id.txtDateTime);
        }
    }
}
