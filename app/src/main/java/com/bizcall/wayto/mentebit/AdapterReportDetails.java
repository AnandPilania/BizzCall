package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterReportDetails extends RecyclerView.Adapter<AdapterReportDetails.MyHolder> {
    Context context;
    ArrayList<DataReportDetails> arrayList;
    SharedPreferences.Editor editor;
    SharedPreferences sp;
    public AdapterReportDetails(Context context, ArrayList<DataReportDetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(context).inflate(R.layout.adapter_reportdetails,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
            final DataReportDetails dataReportDetails=arrayList.get(i);
            myHolder.txtDate.setText(dataReportDetails.getDate1());
            myHolder.txtCName.setText(dataReportDetails.getCname());
            myHolder.txtSrno.setText(dataReportDetails.getSrno());
            myHolder.txtReport.setText(dataReportDetails.getReport());
            sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
            editor=sp.edit();

        myHolder.txtSrno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("SelectedSrNo", dataReportDetails.getSrno());
                editor.putString("ActivityContact","ReportDetails");
                editor.commit();
                if(!sp.getString("ReportName",null).equals("CallLog")) {
                    Log.d("SrNo&&&", dataReportDetails.getSrno());
                    Intent intent = new Intent(context, CounselorContactActivity.class);
                    intent.putExtra("ActivityName", "ReportDetails");
                   /* intent.putExtra("Name",name);
                    intent.putExtra("Course",course);
                    intent.putExtra("SrNo",sr_no);
                    intent.putExtra("Email",email);*/
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtSrno,txtDate,txtCName,txtReport;
        public MyHolder(@NonNull View itemView) {

            super(itemView);
            txtSrno=itemView.findViewById(R.id.txtSrno);
            txtCName=itemView.findViewById(R.id.txtCandidateN);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtReport=itemView.findViewById(R.id.txtreport);
        }
    }
}
