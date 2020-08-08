package com.bizcall.wayto.mentebit13;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterEmpDetails extends RecyclerView.Adapter<AdapterEmpDetails.MyHolder>
{
   Context context;
   ArrayList<DataEmpDetails> arrayListEmpDetails;

    public AdapterEmpDetails(Context context, ArrayList<DataEmpDetails> arrayListEmpDetails) {
        this.context = context;
        this.arrayListEmpDetails = arrayListEmpDetails;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_emp_details,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        DataEmpDetails dataEmpDetails=arrayListEmpDetails.get(i);
        myHolder.txtEmpID.setText(dataEmpDetails.getEmpid());
        myHolder.txtEmpName.setText(dataEmpDetails.getName());
        myHolder.txtEmpDob.setText(dataEmpDetails.getDob());
    }

    @Override
    public int getItemCount() {
        return arrayListEmpDetails.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtEmpID,txtEmpName,txtEmpDob;
        ImageView imgProfile;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtEmpID=itemView.findViewById(R.id.txtEmpID);
            txtEmpName=itemView.findViewById(R.id.txtEmpName);
            txtEmpDob=itemView.findViewById(R.id.txtEmpDOB);
            imgProfile=itemView.findViewById(R.id.imgProfile);
        }
    }
}
