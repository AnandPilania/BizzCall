package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterLastLogin extends RecyclerView.Adapter<AdapterLastLogin.MyHolder>{

   Context context;
   ArrayList<DataLastLogin> arrayListLastLogin;

    public AdapterLastLogin(Context context, ArrayList<DataLastLogin> arrayListLastLogin) {
        this.context = context;
        this.arrayListLastLogin = arrayListLastLogin;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.adapter_lastlogin,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
            DataLastLogin dataLastLogin=arrayListLastLogin.get(i);
            myHolder.txtLoginDate.setText(dataLastLogin.getLogindate());
            myHolder.txtCounselorname.setText(dataLastLogin.getCounselorName());
            myHolder.txtLoginId.setText(dataLastLogin.getLoginId());
            myHolder.txtIpAdrs.setText(dataLastLogin.getIpadrs());
    }

    @Override
    public int getItemCount() {
        return arrayListLastLogin.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
      TextView txtLoginId,txtCounselorname,txtLoginDate,txtIpAdrs;
        public MyHolder(@NonNull View itemView)
        {
            super(itemView);
            txtLoginId=itemView.findViewById(R.id.txtLoginId);
            txtCounselorname=itemView.findViewById(R.id.txtCounselorName);
            txtLoginDate=itemView.findViewById(R.id.txtLoginDate);
            txtIpAdrs=itemView.findViewById(R.id.txtIpAdrs);

        }
    }
}
