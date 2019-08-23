package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.Intent;
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

public class AdapterStatuswiseDetailLeadReport extends RecyclerView.Adapter<AdapterStatuswiseDetailLeadReport.Holder> {
    private ArrayList<DetailStatuswiseLeadReportData> arrayListRefwise;
    private Context mContext;
    String strspannable;
    View view;

    public AdapterStatuswiseDetailLeadReport(ArrayList<DetailStatuswiseLeadReportData> arrayListRefwise, Context mContext) {
        this.arrayListRefwise = arrayListRefwise;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_refwiseleads, viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        DetailStatuswiseLeadReportData tableDetails = arrayListRefwise.get(i);
        holder.txtstatusid.setText(tableDetails.getmSrNo());
        holder.txtstatusName.setText(tableDetails.getmCandName());
        holder.txtTotalLeads.setText(tableDetails.getmMobile());

        strspannable = holder.txtstatusName.getText().toString();
        SpannableString spannableString = new SpannableString(strspannable);
        spannableString.setSpan(new UnderlineSpan(),0, spannableString.length(), 0);
        holder.txtstatusName.setText(spannableString);

        holder.txtstatusName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ActivityLeadsDetailsReport.class);
                intent.putExtra("mSrNo",arrayListRefwise.get(i).getmSrNo());
                intent.putExtra("mRefNo",arrayListRefwise.get(i).getmRefNo());
                intent.putExtra("mCandName",arrayListRefwise.get(i).getmCandName());
                intent.putExtra("mCourse",arrayListRefwise.get(i).getmCourse());
                intent.putExtra("mMobile",arrayListRefwise.get(i).getmMobile());
                intent.putExtra("mAddress",arrayListRefwise.get(i).getmAddress());
                intent.putExtra("mCity",arrayListRefwise.get(i).getmCity());
                intent.putExtra("mState",arrayListRefwise.get(i).getmState());
                intent.putExtra("mPinCode",arrayListRefwise.get(i).getmPinCode());
                intent.putExtra("mParentNo",arrayListRefwise.get(i).getmParentNo());
                intent.putExtra("mEmail",arrayListRefwise.get(i).getmEmail());
                intent.putExtra("mDataFrom",arrayListRefwise.get(i).getmDataFrom());
                intent.putExtra("mAllocatedTo",arrayListRefwise.get(i).getmAllocatedTo());
                intent.putExtra("mAllocatedDate",arrayListRefwise.get(i).getmAllocatedDate());
                intent.putExtra("mStatus",arrayListRefwise.get(i).getmStatus());
                intent.putExtra("mRemark",arrayListRefwise.get(i).getmRemark());
                intent.putExtra("mCreatedDate",arrayListRefwise.get(i).getmCreatedDate());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Animatoo.animateSlideLeft(view.getContext());
            }
        });
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
