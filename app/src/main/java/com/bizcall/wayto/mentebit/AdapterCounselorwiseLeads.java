package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;

public class AdapterCounselorwiseLeads extends RecyclerView.Adapter<AdapterCounselorwiseLeads.Holder> {
    private ArrayList<DataRefwiseLeads> arrayListRefwise;
    private Context mContext;
    String strspannable, mainActivityName;
    View view;
    SharedPreferences sp;

    public AdapterCounselorwiseLeads(ArrayList<DataRefwiseLeads> arrayListRefwise, Context mContext) {
        this.arrayListRefwise = arrayListRefwise;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_refwiseleads, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        DataRefwiseLeads tableDetails = arrayListRefwise.get(i);
        holder.txtCounid.setText(tableDetails.getRefid());
        holder.txtCounName.setText(tableDetails.getRefnames());
        holder.txtTotalLeads.setText(tableDetails.getTotalleads());

        strspannable = holder.txtCounName.getText().toString();
        SpannableString spannableString = new SpannableString(strspannable);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        holder.txtCounName.setText(spannableString);

        sp = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        mainActivityName = sp.getString("ReportActivity", null);

        if (mainActivityName.contains("Refwise Report") || mainActivityName.contains("Statuswise Report") || mainActivityName.contains("Counselorwise Report")) {
            holder.txtCounName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityStatuswiseLeads.class);
                    intent.putExtra("CounId", holder.txtCounid.getText().toString());
                    intent.putExtra("CounName", holder.txtCounName.getText().toString());
                    intent.putExtra("TotalLeads", holder.txtTotalLeads.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    Animatoo.animateSlideLeft(view.getContext());
                }
            });
        } else if (mainActivityName.contains("Datewise Report")) {
            holder.txtCounName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityStatuswiseLeads.class);
                    intent.putExtra("CounId", holder.txtCounid.getText().toString());
                    intent.putExtra("CounName", holder.txtCounName.getText().toString());
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
        TextView txtCounid, txtCounName, txtTotalLeads;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtCounid = itemView.findViewById(R.id.txtRefId);
            txtCounName = itemView.findViewById(R.id.txtRefName);
            txtTotalLeads = itemView.findViewById(R.id.txtTotalLeads);
        }
    }
}
