package com.bizcall.wayto.mentebit13;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        userHolder.txtSub1.setText(details.getSub1name());
        userHolder.txtSub2.setText(details.getSub2name());
        userHolder.txtSub3.setText(details.getSub3name());
        userHolder.txtSub4.setText(details.getSub4name());
        userHolder.txtSub5.setText(details.getSub5name());
        userHolder.txtCol1.setText(details.getCol1name());
        userHolder.txtCol2.setText(details.getCol2name());

        userHolder.edtSub1Value.setText(details.getSub1value());
        userHolder.edtSub2Value.setText(details.getSub2value());
        userHolder.edtSub3Value.setText(details.getSub3value());
        userHolder.edtSub4Value.setText(details.getSub4value());
        userHolder.edtSub5Value.setText(details.getSub5value());
        userHolder.edtCol1Value.setText(details.getCol1value());
        userHolder.edtCol2Value.setText(details.getCol2value());

    }

    @Override
    public int getItemCount() {
        return mStudEducationDetails.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView mqualification, mcoursename, mcollegename, mpassyear, mmarkobt, mmarkoutof, mpercentage;
        TextView txtSub1,txtSub2,txtSub3,txtSub4,txtSub5,txtCol1,txtCol2;
        EditText edtSub1Value,edtSub2Value,edtSub3Value,edtSub4Value,edtSub5Value,edtCol1Value,edtCol2Value;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            mqualification = itemView.findViewById(R.id.edtqualification);
            mcoursename = itemView.findViewById(R.id.edtCoursename);
            mcollegename = itemView.findViewById(R.id.edtCollegeName);
            mpassyear = itemView.findViewById(R.id.edtPassingYear);
            mmarkobt = itemView.findViewById(R.id.edtMarks);
            mmarkoutof = itemView.findViewById(R.id.edtOutof);
            mpercentage = itemView.findViewById(R.id.edtPercentage);
            txtSub1=itemView.findViewById(R.id.txtSub1);
            txtSub2=itemView.findViewById(R.id.txtSub2);
            txtSub3=itemView.findViewById(R.id.txtSub3);
            txtSub4=itemView.findViewById(R.id.txtSub4);
            txtSub5=itemView.findViewById(R.id.txtSub5);
            txtCol1=itemView.findViewById(R.id.txtCol1);
            txtCol2=itemView.findViewById(R.id.txtCol2);

            edtCol1Value=itemView.findViewById(R.id.edtCol1Value);
            edtCol2Value=itemView.findViewById(R.id.edtCol2Value);
            edtSub1Value=itemView.findViewById(R.id.edtSub1Value);
            edtSub2Value=itemView.findViewById(R.id.edtSub2Value);
            edtSub3Value=itemView.findViewById(R.id.edtSub3Value);
            edtSub4Value=itemView.findViewById(R.id.edtSub4Value);
            edtSub5Value=itemView.findViewById(R.id.edtSub5Value);

        }
    }
}