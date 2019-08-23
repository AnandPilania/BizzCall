package com.bizcall.wayto.mentebit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AdapterAllRecords extends RecyclerView.Adapter<AdapterAllRecords.MyHolder> {
    @NonNull
    @Override
    public AdapterAllRecords.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAllRecords.MyHolder myHolder, int i) {}

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
      TextView txtFileno,txtSrNo,txtFirstName,txtLastName,txtDob,txtSex,txtParentName,txtAddress,txtCity,txtState,txtPincode;
      Button btnSendMail,btnSMS,btnDetails;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtFileno=itemView.findViewById(R.id.txtUserfileno);
            txtSrNo=itemView.findViewById(R.id.txtUser_srno);
            txtFirstName=itemView.findViewById(R.id.txtUser_firstname);
            txtLastName=itemView.findViewById(R.id.txtUser_lastname);
            txtDob=itemView.findViewById(R.id.txtUser_dob);
            txtSex=itemView.findViewById(R.id.txtUser_sex);
            txtParentName=itemView.findViewById(R.id.txtUser_parentname);
            txtAddress=itemView.findViewById(R.id.txtUser_sex);

        }
    }
}
