package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterMasterEntry extends RecyclerView.Adapter<AdapterMasterEntry.UserHolder> {
    private ArrayList<DataMasterEntry> arrayListMasterEntry;
    private Context mContext;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public AdapterMasterEntry(ArrayList<DataMasterEntry> arrayListMasterEntry, Context mContext) {
        this.arrayListMasterEntry = arrayListMasterEntry;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_master_entry, null);
        UserHolder userHolder = new UserHolder(view);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder userHolder, int i) {
        DataMasterEntry dataMasterEntry = arrayListMasterEntry.get(i);
        userHolder.txtFileno.setText(dataMasterEntry.getmFileno());
        userHolder.txtSrno.setText(dataMasterEntry.getmSrno());
        userHolder.txtFirstname.setText(dataMasterEntry.getmFirstname());
        userHolder.txtLastname.setText(dataMasterEntry.getmLastname());
        userHolder.txtDob.setText(dataMasterEntry.getmDob());
        userHolder.txtSex.setText(dataMasterEntry.getmSex());
        userHolder.txtParentname.setText(dataMasterEntry.getmParentname());
        userHolder.txtAddress.setText(dataMasterEntry.getmAddress());
        userHolder.txtCity.setText(dataMasterEntry.getmCity());
        userHolder.txtState.setText(dataMasterEntry.getmState());
        userHolder.txtPincode.setText(dataMasterEntry.getmPincode());
        userHolder.txtMobile.setText(dataMasterEntry.getMobile());
        userHolder.txtParentNo.setText(dataMasterEntry.getParentno());
        userHolder.txtEmail.setText(dataMasterEntry.getEmail());
        userHolder.txtRemarks.setText(dataMasterEntry.getRemarks());
        userHolder.txtStatus.setText(dataMasterEntry.getStatus());
        sp=mContext.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        editor=sp.edit();


        userHolder.txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileno=userHolder.txtFileno.getText().toString();
                editor.putString("FileNo",userHolder.txtFileno.getText().toString());
                editor.putString("MobileNo",userHolder.txtMobile.getText().toString());
                editor.putString("FullName",userHolder.txtFirstname.getText().toString()+" "+userHolder.txtLastname.getText().toString());
                editor.commit();

               // Log.d("FileNumber",fileno);
                Intent intent=new Intent(mContext,SummaryDetails.class);
                intent.putExtra("FileNo",fileno);
                intent.putExtra("MobileNo",userHolder.txtMobile.getText().toString());
                mContext.startActivity(intent);
            }
        });
        userHolder.txtDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileno=userHolder.txtFileno.getText().toString();
              //  Log.d("FileNumber",fileno);
                Intent intent=new Intent(mContext,ActivityUploadDocs.class);
                intent.putExtra("FileNo",fileno);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListMasterEntry.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView txtSMSMail,txtDocuments,txtDetails,txtFileno, txtSrno, txtFirstname, txtLastname, txtDob, txtSex, txtParentname, txtAddress, txtCity, txtState,
                txtPincode,txtMobile,txtParentNo,txtEmail,txtRemarks,txtStatus;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            txtSMSMail=itemView.findViewById(R.id.txtSmsMail);
            txtDocuments=itemView.findViewById(R.id.txtDocument);
            txtDetails=itemView.findViewById(R.id.txtDetails);
            txtFileno = itemView.findViewById(R.id.txtUser_fileno);
            txtSrno = itemView.findViewById(R.id.txtUser_srno);
            txtFirstname = itemView.findViewById(R.id.txtUser_firstname);
            txtLastname = itemView.findViewById(R.id.txtUser_lastname);
            txtDob = itemView.findViewById(R.id.txtUser_dob);
            txtSex = itemView.findViewById(R.id.txtUser_sex);
            txtParentname = itemView.findViewById(R.id.txtUser_parentname);
            txtAddress = itemView.findViewById(R.id.txtUser_address);
            txtCity = itemView.findViewById(R.id.txtUser_city);
            txtState = itemView.findViewById(R.id.txtUser_state);
            txtPincode = itemView.findViewById(R.id.txtUser_pincode);
            txtMobile=itemView.findViewById(R.id.txtUser_mobile);
            txtParentNo=itemView.findViewById(R.id.txtUser_parentno);
            txtEmail=itemView.findViewById(R.id.txtUser_email);
            txtRemarks=itemView.findViewById(R.id.txtUser_remarks);
            txtStatus=itemView.findViewById(R.id.txtUser_CStatus);
        }
    }

}
