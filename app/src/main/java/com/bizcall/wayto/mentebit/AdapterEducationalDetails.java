package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterEducationalDetails extends RecyclerView.Adapter<AdapterEducationalDetails.UserHolder> {
    private ArrayList<DataStudEducation> mStudEducationDetails;
    private Context mContext;

    public AdapterEducationalDetails(ArrayList<DataStudEducation> mStudEducationDetails, Context mContext) {
        this.mStudEducationDetails = mStudEducationDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_educationaldetails, viewGroup,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int i) {
        DataStudEducation details = mStudEducationDetails.get(i);
        userHolder.mqualification.setText(details.getStrQualification());
        userHolder.mcoursename.setText(details.getStrCourseName());
        userHolder.mcollegename.setText(details.getStrCollegeName());
        userHolder.mpassyear.setText(details.getPassyear());
        userHolder.mmarkobt.setText(details.getMarkobt());
        userHolder.mmarkoutof.setText(details.getMarkoutof());
        userHolder.mpercentage.setText(details.getPercentage());
    }

    @Override
    public int getItemCount() {
        return mStudEducationDetails.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView mqualification, mcoursename, mcollegename, mpassyear, mmarkobt, mmarkoutof, mpercentage;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            mqualification = itemView.findViewById(R.id.edtqualification);
            mcoursename = itemView.findViewById(R.id.edtCoursename);
            mcollegename = itemView.findViewById(R.id.edtCollegeName);
            mpassyear = itemView.findViewById(R.id.edtPassingYear);
            mmarkobt = itemView.findViewById(R.id.edtMarks);
            mmarkoutof = itemView.findViewById(R.id.edtOutof);
            mpercentage = itemView.findViewById(R.id.edtPercentage);
        }
    }
}