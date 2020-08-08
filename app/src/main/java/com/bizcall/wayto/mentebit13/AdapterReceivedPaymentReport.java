package com.bizcall.wayto.mentebit13;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterReceivedPaymentReport extends RecyclerView.Adapter<AdapterReceivedPaymentReport.MyHolder> {
    Context context;
    ArrayList<DataReceivedPaymentReport> arrayListReceivedPayment;

    public AdapterReceivedPaymentReport(Context context, ArrayList<DataReceivedPaymentReport> arrayListReceivedPayment) {
        this.context = context;
        this.arrayListReceivedPayment = arrayListReceivedPayment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_received_payment_report,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        DataReceivedPaymentReport dataReceivedPaymentReport=arrayListReceivedPayment.get(i);
        myHolder.txtFileNo.setText(dataReceivedPaymentReport.getFileno());
        myHolder.txtCname.setText(dataReceivedPaymentReport.getFname()+" "+dataReceivedPaymentReport.getLname());
        myHolder.txtPaymentDate.setText(dataReceivedPaymentReport.getPaymentdate());
        myHolder.txtPaymentMode.setText(dataReceivedPaymentReport.getPaymentMode());
        myHolder.txtPaymentFrom.setText(dataReceivedPaymentReport.getPaymentFrom());

        myHolder.txtRefNo.setText(dataReceivedPaymentReport.getRefno());
        myHolder.txtPaymentRemarks.setText(dataReceivedPaymentReport.getPaymentremarks());
        myHolder.txtApprovedRemarks.setText(dataReceivedPaymentReport.getApprovalremarks());
        myHolder.txtCurrentLocalRate.setText(dataReceivedPaymentReport.getCurrentLocalRate());
        myHolder.txtReceiptName.setText(dataReceivedPaymentReport.getReceiptname());
        myHolder.txtAmountINR.setText(dataReceivedPaymentReport.getAmountINR());

    }

    @Override
    public int getItemCount() {
        return arrayListReceivedPayment.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView txtFileNo,txtCname,txtPaymentDate,txtPaymentMode,txtPaymentFrom,txtRefNo,txtPaymentRemarks,txtApprovedRemarks,
        txtCurrentLocalRate,txtReceiptName,txtAmountINR;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtFileNo=itemView.findViewById(R.id.txtFileID);
            txtCname=itemView.findViewById(R.id.txtCandidateName);
            txtPaymentDate=itemView.findViewById(R.id.txtPaymentDate);
            txtPaymentMode=itemView.findViewById(R.id.txtPaymentMode);
            txtPaymentFrom=itemView.findViewById(R.id.txtPaymentFrom);
            txtRefNo=itemView.findViewById(R.id.txtRefNo);
            txtPaymentRemarks=itemView.findViewById(R.id.txtPaymentRemarks);
            txtApprovedRemarks=itemView.findViewById(R.id.txtApprovalRemarks);
            txtCurrentLocalRate=itemView.findViewById(R.id.txtCurrentLocalRate);
            txtReceiptName=itemView.findViewById(R.id.txtReceiptName);
            txtAmountINR=itemView.findViewById(R.id.txtAmountINR);
        }
    }
}
