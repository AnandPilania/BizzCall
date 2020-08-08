package com.bizcall.wayto.mentebit13;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterOnlineFormDetails extends RecyclerView.Adapter<AdapterOnlineFormDetails.ViewHolder> {
    Context context;
    ArrayList<DataOnlineFormDetails> arrayList;

    public AdapterOnlineFormDetails(Context context, ArrayList<DataOnlineFormDetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdapterOnlineFormDetails.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_onlineformdetails,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOnlineFormDetails.ViewHolder viewHolder, int i) {
            DataOnlineFormDetails dataOnlineFormDetails=arrayList.get(i);
            viewHolder.txtId.setText(dataOnlineFormDetails.getId());
            viewHolder.txtFormno.setText(dataOnlineFormDetails.getFormno());
            viewHolder.txtFname.setText(dataOnlineFormDetails.getFname());
        viewHolder.txtLname.setText(dataOnlineFormDetails.getLname());
        viewHolder.txtFathername.setText(dataOnlineFormDetails.getFathername());
        viewHolder.txtMothername.setText(dataOnlineFormDetails.getMothername());
        viewHolder.txtDob.setText(dataOnlineFormDetails.getDob());
        viewHolder.txtPassport.setText(dataOnlineFormDetails.getPassport());
        viewHolder.txtAdrs.setText(dataOnlineFormDetails.getAddress());
        viewHolder.txtMobile.setText(dataOnlineFormDetails.getMobile());
        viewHolder.txtCity.setText(dataOnlineFormDetails.getCity());
        viewHolder.txtState.setText(dataOnlineFormDetails.getState());
        viewHolder.txtPin.setText(dataOnlineFormDetails.getPincode());
        viewHolder.txtPAdrs.setText(dataOnlineFormDetails.getPaddress());
        viewHolder.txtPCity.setText(dataOnlineFormDetails.getPcity());
        viewHolder.txtPState.setText(dataOnlineFormDetails.getPstate());
        viewHolder.txtPPin.setText(dataOnlineFormDetails.getPpin());
        viewHolder.txtParentmbl.setText(dataOnlineFormDetails.getParentmbl());
        viewHolder.txtEmail.setText(dataOnlineFormDetails.getEmail());
        viewHolder.txtCollege.setText(dataOnlineFormDetails.getCollege());
        viewHolder.txtSchoolBoard.setText(dataOnlineFormDetails.getSchoolboard());
        viewHolder.txtTenthPer.setText(dataOnlineFormDetails.getTenthper());
        viewHolder.txtTenthYear.setText(dataOnlineFormDetails.getTenthyear());
        viewHolder.txtTenthMarks.setText(dataOnlineFormDetails.getTenthmarks());
        viewHolder.txtTenthOutof.setText(dataOnlineFormDetails.getTenthoutof());
        viewHolder.txtTwelthPer.setText(dataOnlineFormDetails.getTwelthper());
        viewHolder.txtTwelthYear.setText(dataOnlineFormDetails.getTwelthyear());
        viewHolder.txtTwelthMarks.setText(dataOnlineFormDetails.getTwelthmarks());
        viewHolder.txtTwelthOutof.setText(dataOnlineFormDetails.getTwelthoutof());
        viewHolder.txtNeetMarks.setText(dataOnlineFormDetails.getNeetmarks());
        viewHolder.txtNeetYear.setText(dataOnlineFormDetails.getNeetyear());
        viewHolder.txtPhysics.setText(dataOnlineFormDetails.getPhysics());
        viewHolder.txtChemistry.setText(dataOnlineFormDetails.getChemistry());
        viewHolder.txtBiology.setText(dataOnlineFormDetails.getBiology());
        viewHolder.txtPCB.setText(dataOnlineFormDetails.getPcb());
        viewHolder.txtAggregate.setText(dataOnlineFormDetails.getAggregate());
        viewHolder.txtDate.setText(dataOnlineFormDetails.getCreateddate());
        viewHolder.txtWebsite.setText(dataOnlineFormDetails.getWebsite());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId,txtFormno,txtFname,txtLname,txtFathername,txtMothername,txtDob,txtPassport,txtAdrs,txtCity,txtState,txtPin,txtMobile,
        txtPAdrs,txtPCity,txtPState,txtPPin,txtParentmbl,txtEmail,txtCollege,txtSchoolBoard,txtTenthPer,txtTenthYear,txtTenthMarks,txtTenthOutof,txtTwelthPer,txtTwelthYear,
        txtTwelthMarks,txtTwelthOutof,txtNeetYear,txtNeetMarks,txtPhysics,txtChemistry,txtBiology,txtPCB,txtAggregate,txtDate,txtWebsite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId=itemView.findViewById(R.id.txtId);
            txtFormno=itemView.findViewById(R.id.txtFormNo);
            txtFname=itemView.findViewById(R.id.txtFname);
            txtLname=itemView.findViewById(R.id.txtLname);
            txtFathername=itemView.findViewById(R.id.txtFatherName);
            txtMothername=itemView.findViewById(R.id.txtMothername);
            txtDob=itemView.findViewById(R.id.txtDOB);
            txtMobile=itemView.findViewById(R.id.txtMobile);
            txtPassport=itemView.findViewById(R.id.txtPassport);
            txtAdrs=itemView.findViewById(R.id.txtAddress);
            txtCity=itemView.findViewById(R.id.txtCity);
            txtState=itemView.findViewById(R.id.txtState);
            txtPin=itemView.findViewById(R.id.txtPincode);
            txtPAdrs=itemView.findViewById(R.id.txtPAddress);
            txtPCity=itemView.findViewById(R.id.txtPCity);
            txtPState=itemView.findViewById(R.id.txtPState);
            txtPPin=itemView.findViewById(R.id.txtPPincode);
            txtParentmbl=itemView.findViewById(R.id.txtParentMobile);
            txtEmail=itemView.findViewById(R.id.txtEmail);
            txtCollege=itemView.findViewById(R.id.txtCollege);
            txtSchoolBoard=itemView.findViewById(R.id.txtSchoolBoard);
            txtTenthPer=itemView.findViewById(R.id.txtTenthPer);
            txtTenthYear=itemView.findViewById(R.id.txtTenthYear);
            txtTenthMarks=itemView.findViewById(R.id.txtTenthMarks);
            txtTenthOutof=itemView.findViewById(R.id.txtTenthOutof);
            txtTwelthPer=itemView.findViewById(R.id.txtTwelthPer);
            txtTwelthYear=itemView.findViewById(R.id.txtTwelthYear);
            txtTwelthMarks=itemView.findViewById(R.id.txtTwelthMarks);
            txtTwelthOutof=itemView.findViewById(R.id.txtTwelthOutof);
            txtNeetYear=itemView.findViewById(R.id.txtNeetYear);
            txtNeetMarks=itemView.findViewById(R.id.txtNeetMarks);
            txtPhysics=itemView.findViewById(R.id.txtPhysics);
            txtChemistry=itemView.findViewById(R.id.txtChemistry);
            txtBiology=itemView.findViewById(R.id.txtBiology);
            txtPCB=itemView.findViewById(R.id.txtPCB);
            txtAggregate=itemView.findViewById(R.id.txtAggregate);
            txtDate=itemView.findViewById(R.id.txtCreatedDate);
            txtWebsite=itemView.findViewById(R.id.txtWebsite);

        }
    }
}
