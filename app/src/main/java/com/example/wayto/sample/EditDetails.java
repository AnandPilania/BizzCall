package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class EditDetails extends AppCompatActivity
{
    TextView txtSrno1,txtCourse1,txtMobile1;
    EditText edtName1,edtCity1,edtAddress1,edtEmail1,edtParent1,edtPincode1;
    Button btnUpdate,btnCancel;
    Spinner spinnerState1;
    int temp = 0;
    String name11,address1,city1,state11,email1,parentno1,pincode1;
    String mbl,parentno,name,course,sr_no,email,allocatedDate,adrs,city,state1,pincode,statusid,remark,status11;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    ImageView imgBack;
    int updateDetails;
    String clienturl,clientid,counsellorid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_editdetails);

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor=sp.edit();
        clientid=sp.getString("ClientId",null);
        counsellorid=sp.getString("Id",null);
        counsellorid=counsellorid.replace(" ","");
        clienturl=sp.getString("ClientUrl",null);
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

        updateDetails=sp.getInt("UpdateDetails",0);
        txtSrno1 = findViewById(R.id.txtSrno12);
        txtCourse1 =findViewById(R.id.txtCourse12);
        txtMobile1 = findViewById(R.id.txtPhone12);
        edtName1 = findViewById(R.id.edtName11);
        edtAddress1 =findViewById(R.id.edtAddress11);
        edtCity1 = findViewById(R.id.edtCity11);
        spinnerState1 = findViewById(R.id.spinnerState11);
        edtEmail1 = findViewById(R.id.edtEmail11);
        edtParent1 =findViewById(R.id.edtParentNo11);
        edtPincode1 =findViewById(R.id.edtPinCode11);
        btnUpdate = findViewById(R.id.btnUpdtDetails);
        btnCancel = findViewById(R.id.btnCancelUpdate);
        imgBack=findViewById(R.id.img_back);

        txtSrno1.setText(sr_no);
        txtCourse1.setText(course);
        txtMobile1.setText(mbl);
        edtName1.setText(name);
        if(edtName1.getText().toString().length()==0)
        {
            edtName1.setText("NA");
        }
        edtAddress1.setText(adrs);
        if(edtAddress1.getText().toString().length()==0)
        {
            edtAddress1.setText("NA");
        }
        edtCity1.setText(city);
        if(edtCity1.getText().toString().length()==0)
        {
            edtCity1.setText("NA");
        }
        edtParent1.setText(parentno);
        if(edtParent1.getText().toString().length()==0)
        {
            edtParent1.setText("NA");
        }
        edtEmail1.setText(email);
        if(edtEmail1.getText().toString().length()==0)
        {
            edtEmail1.setText("NA");
        }
        edtPincode1.setText(pincode);
        if(edtPincode1.getText().toString().length()==0)
        {
            edtPincode1.setText("NA");
        }
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(EditDetails.this,
                R.array.States, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerState1.setAdapter(adapter1);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name11 = edtName1.getText().toString();
                if (name11.length() == 0) {
                    edtName1.setError("Please enter name");
                    temp = 1;
                }
                address1 = edtAddress1.getText().toString();
                if (address1.length() == 0) {
                    edtAddress1.setError("Please enter address");
                    temp = 1;
                }
                city1 = edtCity1.getText().toString();
                if (city1.length() == 0) {
                    edtCity1.setError("Please enter city");
                    temp = 1;
                }
                state11 = spinnerState1.getSelectedItem().toString();
                email1 = edtEmail1.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!email1.matches(emailPattern)) {
                    edtEmail1.setError("Please enter email");
                    temp = 1;
                }
                parentno1 = edtParent1.getText().toString();
                if (parentno1.length() < 10 || parentno1.length() == 0) {
                    edtParent1.setError("Invalid number");
                    temp = 1;
                }
                pincode1 = edtPincode1.getText().toString();
                if (pincode1.length() < 6 || pincode1.length() == 0) {
                    edtPincode1.setError("Invalid pin");
                    temp = 1;
                }

                //name = name.replaceAll(" ", "");

               /* email1 = URLEncoder.encode(email1, "UTF-8");}
                     catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
                  //  Toast.makeText(EditDetails.this,"Runnig2",Toast.LENGTH_SHORT).show();
                    if (temp == 0)
                    {

                      //  Toast.makeText(EditDetails.this,"Runnig",Toast.LENGTH_SHORT).show();
                         insertCounselorDetails();
                        updateCounselorDetails();
                    }
                temp=0;

                    }

                // insertCounselorDetails();
               // updateCounselorDetails();

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //alertDialog.dismiss();
            }
        });
    }

    public void updateCounselorDetails() {
        String url = "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=AnDe828500&caseid=18&Srno=" + sr_no + "&CounselorName=" + name11 + "&Address=" +
                address1 + "&City=" + city1 + "&State=" + state11 + "&Pincode=" + pincode1 + "&Email=" + email1 + "&ParentNo=" + parentno1;
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(url);
        Log.d("UpdateDetailsUrl", url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException
            {
                //dialog.dismiss();
                Log.d("updateDetailsResponse", response);

                if (response.contains("Row updated successfully")) {
                    int eventid=5;
                    insertPointCollection(eventid);
                    editor.putInt("UpdateDetails",updateDetails);
                    editor.commit();
                    Toast.makeText(EditDetails.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditDetails.this, "Data not updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

    public void insertPointCollection(int eid)
    {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=36&nCounsellorId=" + counsellorid + "&nEventId="+eid);
        Log.d("PointCollectionResponse", clienturl+"?clientid=" + clientid + "&caseid=36&nCounsellorId=" + counsellorid + "&nEventId="+eid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("PointCollectionResponse", response);
                if (response.contains("Data inserted successfully")) {
                    Toast.makeText(EditDetails.this, "Added point for update details",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditDetails.this, "Point not added for update details", Toast.LENGTH_SHORT).show();
                }
                //   Log.d("Size**", String.valueOf(arrayList.size()));
            }
        });
    }

    public void insertCounselorDetails() {
        String url = "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=AnDe828500&caseid=31&nSrno=" + sr_no;
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(url);
        Log.d("InsertDetailsUrl", url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                //dialog.dismiss();
                Log.d("InsertDetailsResponse", response);
                if (response.contains("Data inserted successfully"))
                {
                    Toast.makeText(EditDetails.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditDetails.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
