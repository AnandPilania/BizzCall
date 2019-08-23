package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;

public class AdapterStatuswiseLeads extends RecyclerView.Adapter<AdapterStatuswiseLeads.Holder> {
    private ArrayList<DataRefwiseLeads> arrayListRefwise;
    private Context mContext;
    String strspannable, mainActivityName;
    View view;
    SharedPreferences sp;

    public AdapterStatuswiseLeads(ArrayList<DataRefwiseLeads> arrayListRefwise, Context mContext) {
        this.arrayListRefwise = arrayListRefwise;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_refwiseleads, viewGroup,false);
        // AdapterRefwiseLeads.UserHolder userHolder = new AdapterRefwiseLeads.UserHolder(view);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        DataRefwiseLeads tableDetails = arrayListRefwise.get(i);
        holder.txtstatusid.setText(tableDetails.getRefid());
        holder.txtstatusName.setText(tableDetails.getRefnames());
        holder.txtTotalLeads.setText(tableDetails.getTotalleads());

        strspannable = holder.txtstatusName.getText().toString();
        SpannableString spannableString = new SpannableString(strspannable);
        spannableString.setSpan(new UnderlineSpan(),0, spannableString.length(), 0);
        holder.txtstatusName.setText(spannableString);

        sp=mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        mainActivityName = sp.getString("ReportActivity", null);
        Log.d("mainActivityNameqq",mainActivityName);

        if (mainActivityName.contains("Refwise Report") || mainActivityName.contains("Statuswise Report") || mainActivityName.contains("Counselorwise Report")) {
            holder.txtstatusName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityStatuswiseLeadDetailRecords.class);
                    intent.putExtra("StatusId", holder.txtstatusid.getText().toString());
                    intent.putExtra("StatusName", holder.txtstatusName.getText().toString());
                    intent.putExtra("TotalLeads", holder.txtTotalLeads.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    Animatoo.animateSlideLeft(view.getContext());
                }
            });
        } else if (mainActivityName.contains("Datewise Report")){
            holder.txtstatusName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityDatewiseLeads.class);
                    intent.putExtra("StatusId", holder.txtstatusid.getText().toString());
                    intent.putExtra("StatusName", holder.txtstatusName.getText().toString());
                    intent.putExtra("TotalLeads", holder.txtTotalLeads.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    Animatoo.animateSlideLeft(view.getContext());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayListRefwise.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtstatusid, txtstatusName, txtTotalLeads;
        public Holder(@NonNull View itemView) {
            super(itemView);

            txtstatusid = itemView.findViewById(R.id.txtRefId);
            txtstatusName = itemView.findViewById(R.id.txtRefName);
            txtTotalLeads = itemView.findViewById(R.id.txtTotalLeads);
        }
    }
}
