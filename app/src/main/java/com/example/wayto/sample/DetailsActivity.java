package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    TextView txtSrno1, txtCourse1, txtMobile1, txtName1, txtCity1, txtAddress1, txtEmail1, txtParent, txedtPincode1;
    Button btnCancel;
    Spinner spinnerState1;
    ImageView imgBack;
    int temp = 0;
    String name11, address1, city1, state11, email1, parentno1, pincode1;
    String mbl, parentno, name, course, sr_no, email, allocatedDate, adrs, city, state1, pincode, statusid, remark, status11;
    SharedPreferences sp;
    UrlRequest urlRequest;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detalis);
        imgBack=findViewById(R.id.img_back);
        TextView txtSrno = findViewById(R.id.txtSrno11);
        TextView txtCourse = findViewById(R.id.txtCourse11);
        TextView txtMobile = findViewById(R.id.txtPhone11);
        TextView txtName = findViewById(R.id.txtName11);
        TextView txtAddress = findViewById(R.id.txtAddress11);
        TextView txtCity = findViewById(R.id.txtCity11);
        TextView txtState = findViewById(R.id.txtState11);
        TextView txtPincode = findViewById(R.id.txtPinCode11);
        TextView txtEmail = findViewById(R.id.txtEmail11);
        TextView txtParent = findViewById(R.id.txtParentNo11);
        Button btnEdit = findViewById(R.id.btnEditDetails);
        Button btnOk = findViewById(R.id.btnOk11);

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        mbl = sp.getString("SelectedMobile", null);
        parentno = sp.getString("SelectedParentNo", null);
        name = sp.getString("SelectedName", null);
        course = sp.getString("SelectedCourse", null);
        sr_no = sp.getString("SelectedSrNo", null);
        email = sp.getString("SelectedEmail", null);
        allocatedDate = sp.getString("AllocatedDate", null);
        adrs = sp.getString("SelectedAddress", null);
        city = sp.getString("SelectedCity", null);
        state1 = sp.getString("SelectedState", null);
        pincode = sp.getString("SelectedPinCode", null);
        status11=sp.getString("SelectedStatus",null);
        statusid=sp.getString("SelectedStatusId",null);
        remark=sp.getString("SelectedRemark",null);
        txtSrno.setText(sr_no);
        txtCourse.setText(course);
        txtMobile.setText(mbl);
        txtName.setText(name);
        txtAddress.setText(adrs);
        if (txtAddress.getText().toString().length() == 0) {
            txtAddress.setText("NA");
        }
        txtCity.setText(city);
        if (txtCity.getText().toString().length() == 0) {
            txtCity.setText("NA");
        }
        txtState.setText(state1);
        if (txtState.getText().toString().contains("-Select State-")) {
            txtState.setText("NA");
        }
        txtPincode.setText(pincode);
        if (txtPincode.getText().toString().length() == 0) {
            txtPincode.setText("NA");
        }
        txtEmail.setText(email);
        if (txtEmail.getText().toString().length() == 0) {
            txtEmail.setText("NA");
        }
        txtParent.setText(parentno);
        if (txtParent.getText().toString().length() == 0) {
            txtParent.setText("NA");
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsActivity.this, EditDetails.class));
                }
        });
    }
}
