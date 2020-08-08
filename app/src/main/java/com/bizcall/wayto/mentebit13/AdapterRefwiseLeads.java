package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRefwiseLeads extends RecyclerView.Adapter<AdapterRefwiseLeads.UserHolder> {
    private ArrayList<DataRefwiseLeads> arrayListRefwise;
    private Context mContext;
    String strspannable, mainActivityName,RefLeads, RefName;
    View view;
    SharedPreferences sp;

    public AdapterRefwiseLeads(ArrayList<DataRefwiseLeads> mTableDetailsArrayList, Context mContext) {
        this.arrayListRefwise = mTableDetailsArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_refwiseleads, viewGroup,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder userHolder, int i) {
        DataRefwiseLeads tableDetails = arrayListRefwise.get(i);
        userHolder.txtRefid.setText(tableDetails.getRefid());
        userHolder.txtRefName.setText(tableDetails.getRefnames());
        userHolder.txtTotalLeads.setText(tableDetails.getTotalleads());

        strspannable = userHolder.txtRefName.getText().toString();
        SpannableString spannableString = new SpannableString(strspannable);
        spannableString.setSpan(new UnderlineSpan(),0, spannableString.length(), 0);
        userHolder.txtRefName.setText(spannableString);

        sp = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        mainActivityName = sp.getString("ReportActivity", null);
        RefName=sp.getString("referencename",null);
        RefLeads=sp.getString("referencetotalleads",null);

        if (mainActivityName.contains("Refwise Report") || mainActivityName.contains("Statuswise Report") || mainActivityName.contains("Counselorwise Report")) {
            userHolder.txtRefName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   Intent intent = new Intent(mContext, ActivityCounselorwiseLeads.class);
                    intent.putExtra("ReffId", userHolder.txtRefid.getText().toString());
                    intent.putExtra("ReffName", userHolder.txtRefName.getText().toString());
                    intent.putExtra("fTotalLeads", userHolder.txtTotalLeads.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    Animatoo.animateSlideLeft(view.getContext());*/
                }
            });
        } else if (mainActivityName.contains("Datewise Report")) {
            userHolder.txtRefName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             /*       Intent intent = new Intent(mContext, ActivityStatuswiseLeadDetailRecords.class);
                    intent.putExtra("dateId", userHolder.txtRefid.getText().toString());
                    intent.putExtra("dateName", userHolder.txtRefName.getText().toString());
                    intent.putExtra("dttotalLeads", userHolder.txtTotalLeads.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    Animatoo.animateSlideLeft(view.getContext());*/
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayListRefwise.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView txtRefid, txtRefName, txtTotalLeads;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            txtRefid = itemView.findViewById(R.id.txtRefId);
            txtRefName = itemView.findViewById(R.id.txtRefName);
            txtTotalLeads = itemView.findViewById(R.id.txtTotalLeads);
        }
    }
}
