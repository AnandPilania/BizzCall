package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.UserHolder> {

    private ArrayList<DataChat> mDetailsArrayList;
    private Context mContext;

    public AdapterChat(ArrayList<DataChat> mDetailsArrayList, Context mContext) {
        this.mDetailsArrayList = mDetailsArrayList;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_chat,viewGroup,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int i) {

        DataChat details = mDetailsArrayList.get(i);
        userHolder.txtUserName.setText(details.getmUser()+" : "+details.getmDate());
        userHolder.txtChat.setText(details.getmChat());
    }

    @Override
    public int getItemCount() {
        return mDetailsArrayList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView txtUserName,txtChat;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);

            Random randomcolor = new Random();
            int color = Color.argb(255, randomcolor.nextInt(256),
                    randomcolor.nextInt(256), randomcolor.nextInt(256));
            txtUserName.setTextColor(color);

            txtChat = itemView.findViewById(R.id.txtChat);

        }
    }
}
