package com.bizcall.wayto.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterUserInfo extends RecyclerView.Adapter<AdapterUserInfo.ViewHolder> {
    Context context;
    ArrayList<DataUser> arrayList;

    public AdapterUserInfo(Context context, ArrayList<DataUser> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_userinfo, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DataUser dataUser = arrayList.get(i);
       /* viewHolder.txtId.setText(dataUser.getId());
        viewHolder.txtName.setText(dataUser.getName());
        viewHolder.txtEmail.setText(dataUser.getEmail());*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtName, txtEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtId);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmailId);
        }
    }
}
