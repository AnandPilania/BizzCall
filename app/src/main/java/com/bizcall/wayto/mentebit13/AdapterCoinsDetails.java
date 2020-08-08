package com.bizcall.wayto.mentebit13;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCoinsDetails extends RecyclerView.Adapter<AdapterCoinsDetails.MyHolder> {
   Context context;
   ArrayList<DataCoin> arrayList;

    public AdapterCoinsDetails(Context context, ArrayList<DataCoin> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.adapter_coin_details,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
            DataCoin dataCoin=arrayList.get(i);
            myHolder.txtPointId.setText(dataCoin.getPointid());
            myHolder.txtEvent.setText(dataCoin.getEvent());
            myHolder.txtPoint.setText(dataCoin.getPoint());
            myHolder.txtValidFrom.setText(dataCoin.getValidfrom());
            myHolder.txtValidUpto.setText(dataCoin.getValidupto());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

      TextView txtPointId,txtEvent,txtPoint,txtValidFrom,txtValidUpto;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtPointId=itemView.findViewById(R.id.txtPointId);
            txtEvent=itemView.findViewById(R.id.txtEvent);
            txtPoint=itemView.findViewById(R.id.txtPoint);
            txtValidFrom=itemView.findViewById(R.id.txtValidFrom);
            txtValidUpto=itemView.findViewById(R.id.txtValidUpto);
        }
    }
}
