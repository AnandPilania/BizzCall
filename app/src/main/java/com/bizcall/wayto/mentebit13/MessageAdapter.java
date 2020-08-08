package com.bizcall.wayto.mentebit13;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder> {
   Context context;
    ArrayList<DataMessage> arrayList;

    public MessageAdapter(Context context, ArrayList<DataMessage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_message, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        DataMessage dataMessage=arrayList.get(i);

        myHolder.txtMessage.setText(dataMessage.getMessageText());
        myHolder.txtMsgTime.setText(dataMessage.getMessageTime());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtMessage,txtMsgTime;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage=itemView.findViewById(R.id.txtMessage);
            txtMsgTime=itemView.findViewById(R.id.txtMsgTime);
        }
    }
}
