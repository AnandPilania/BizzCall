package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPaymentHistory extends RecyclerView.Adapter<AdapterPaymentHistory.MyHolder> {
   Context context;
   ArrayList<DataPaymentHistory> arrayList;

    public AdapterPaymentHistory(Context context, ArrayList<DataPaymentHistory> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_payment_history,viewGroup,false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        final DataPaymentHistory dataPaymentHistory=arrayList.get(i);
        myHolder.txtPaymentId.setText(dataPaymentHistory.getPaymentid());
        myHolder.txtAmountUSD.setText(dataPaymentHistory.getAmountUSD());
        myHolder.txtAmountINR.setText(dataPaymentHistory.getAmountINR());
        myHolder.txtDate1.setText(dataPaymentHistory.getDate1());
        myHolder.txtApproved.setText(dataPaymentHistory.getApproved());
        myHolder.txtPaymentId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PaymentHistoryDetails.class);
                intent.putExtra("FileNo1",arrayList.get(i).getFileID());
                intent.putExtra("PaymentId",myHolder.txtPaymentId.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtPaymentId,txtAmountUSD,txtAmountINR,txtDate1,txtApproved;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtPaymentId=itemView.findViewById(R.id.txtPaymentID);
            txtAmountINR=itemView.findViewById(R.id.txtAmountINR);
            txtAmountUSD=itemView.findViewById(R.id.txtAmountUSD);
            txtDate1=itemView.findViewById(R.id.txtPaymentDate);
            txtApproved=itemView.findViewById(R.id.txtApproved);
        }
    }
}
