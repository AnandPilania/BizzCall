package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmpDetails extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sp;
    String counselorid="",clientid="",clienturl="",counselorname="";
    long timeout;
    Thread thread;
    ProgressDialog dialog;
    ImageView imgBack,imgRefresh;
    TextView txtEmpNo,txtEmpID,txtCounselorID,txtEmpMobile,txtEmpName,txtEmpEmail,txtEmpDOB,txtEmpGender,txtEmpAddress,txtEmpCity,
            txtEmpState,txtEmpPin,txtEmpIsactive,txtEmpJoindate,txtEmpDepartment,txtEmpLeavebal,txtEmpDesignation,
            txtEmpPanno,txtEmpAdharno,txtEmpAccName,txtEmpAccNo,txtEmpBankName,txtEmpIFSCNo,txtEmpBranchName;

    String empno="",empid="",empname="",empemail="",empdob="",empgender="",empaddress="",empcity="",empstate="",emppin="",empisactive="",empjoindate="",empdepartment="",
    empleavebal="",empdesignation="",emppan="",empadhar="",empaccname="",empaccno="",empbankname="",empifscno="",empbranchname="",empmobile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_emp_details);

            initialize();
            dialog = ProgressDialog.show(EmpDetails.this, "", "Loading employee details", true);
            newThreadInitilization(dialog);
            //to get all details of user who is logged in
            getEmpDetails();

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EmpDetails.this, EmpDetails.class);
                    startActivity(intent);
                    finish();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(EmpDetails.this,"Errorcode-553 EmpDetails onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize() {
        requestQueue = Volley.newRequestQueue(EmpDetails.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replaceAll(" ", "");
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        counselorname = sp.getString("Name", null);
        timeout = sp.getLong("TimeOut", 0);
        txtEmpNo = findViewById(R.id.txtEmpNo);
        txtEmpID = findViewById(R.id.txtEmpID);
        txtCounselorID = findViewById(R.id.txtCounselorID);
        txtEmpName = findViewById(R.id.txtEmpName);
        txtEmpEmail = findViewById(R.id.txtEmpEmail);
        txtEmpDOB = findViewById(R.id.txtEmpDOB);
        txtEmpGender = findViewById(R.id.txtEmpGender);
        txtEmpAddress = findViewById(R.id.txtEmpAddress);
        txtEmpCity = findViewById(R.id.txtEmpCity);
        txtEmpState = findViewById(R.id.txtEmpState);
        txtEmpPin = findViewById(R.id.txtEmpPin);
        txtEmpIsactive = findViewById(R.id.txtEmpIsactive);
        txtEmpJoindate = findViewById(R.id.txtEmpJoindate);
        txtEmpDepartment = findViewById(R.id.txtEmpDepartment);
        txtEmpLeavebal = findViewById(R.id.txtEmpLeavebal);
        txtEmpDesignation = findViewById(R.id.txtEmpDesignation);
        txtEmpPanno = findViewById(R.id.txtEmpPanno);
        txtEmpAdharno = findViewById(R.id.txtEmpAdharno);
        txtEmpAccName = findViewById(R.id.txtEmpAccName);
        txtEmpAccNo = findViewById(R.id.txtEmpAccNo);
        txtEmpBankName = findViewById(R.id.txtEmpBankName);
        txtEmpIFSCNo = findViewById(R.id.txtEmpIFSCNo);
        txtEmpBranchName = findViewById(R.id.txtEmpBranchName);
        txtEmpMobile = findViewById(R.id.txtEmpMobile);
        imgBack = findViewById(R.id.img_back);
        imgRefresh = findViewById(R.id.imgRefresh);

    }//initialize

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EmpDetails.this, Home.class);
        intent.putExtra("Activity", "EmpDetails");
        startActivity(intent);
        finish();
    }

    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    // dialog1.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(EmpDetails.this, "Connection Aborted.", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(Home.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Log.d("TimeThread","cdvmklmv");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    public void getEmpDetails()
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid=157&CounselorID="+counselorid;
            Log.d("EmpDetailsUrl", url);
            if(CheckInternet.checkInternet(EmpDetails.this))
            {
                if(CheckServer.isServerReachable(EmpDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();

                                    Log.d("EmpDetailsResponse", response);
                                    try {
                                        if(response.contains("[]"))
                                        {
                                            empno="NA";
                                            empid="NA";
                                           empname="NA";
                                           empmobile="NA";
                                          empemail="NA";
                                           empdob="NA";
                                              empaddress="NA";
                                            empcity="NA";
                                            empstate="NA";
                                            emppin="NA";
                                            empgender="NA";
                                            emppan="NA";
                                          empadhar="NA";
                                           empaccno="NA";
                                           empaccname="NA";
                                           empbranchname="NA";
                                           empbankname="NA";
                                          empifscno="NA";
                                          empisactive="NA";
                                          empjoindate="NA";
                                           empdesignation="NA";
                                           empdepartment="NA";
                                           empleavebal="NA";
                                            txtEmpNo.setText(empno);
                                            txtEmpID.setText(empid);
                                            txtEmpName.setText(empname);
                                            txtEmpJoindate.setText(empjoindate);
                                            txtEmpMobile.setText(empmobile);
                                            txtEmpEmail.setText(empemail);
                                            txtEmpDOB.setText(empdob);
                                            txtEmpAddress.setText(empaddress);
                                            txtEmpCity.setText(empcity);
                                            txtEmpState.setText(empstate);
                                            txtEmpPin.setText(emppin);
                                            txtEmpGender.setText(empgender);
                                            txtEmpPanno.setText(emppan);
                                            txtEmpAdharno.setText(empadhar);
                                            txtEmpAccName.setText(empaccname);
                                            txtEmpAccNo.setText(empaccno);
                                            txtEmpBankName.setText(empbankname);
                                            txtEmpBranchName.setText(empbranchname);
                                            txtEmpIFSCNo.setText(empifscno);
                                            txtEmpIsactive.setText(empisactive);
                                            txtEmpDesignation.setText(empdesignation);
                                            txtEmpDepartment.setText(empdepartment);
                                            txtEmpLeavebal.setText(empleavebal);
                                            txtCounselorID.setText(counselorid);

                                        }else {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                empno = jsonObject1.getString("EmpNo");
                                                empid = jsonObject1.getString("EmpIdNo");
                                                empname = jsonObject1.getString("EmpFirstName") + " " + jsonObject1.getString("EmpLastName");
                                                empjoindate = jsonObject1.getString("EmpJoinDate");
                                                empjoindate = empjoindate.substring(9, empjoindate.lastIndexOf(" "));
                                                empmobile = jsonObject1.getString("EmpMobileNo");
                                                empemail = jsonObject1.getString("EmpEmailId");
                                                empdob = jsonObject1.getString("EmpDOB");
                                                empdob = empdob.substring(9, empdob.lastIndexOf(" "));
                                                empaddress = jsonObject1.getString("EmpAddress");
                                                empcity = jsonObject1.getString("EmpCity");
                                                empstate = jsonObject1.getString("EmpState");
                                                emppin = jsonObject1.getString("EmpPinCode");
                                                empgender = jsonObject1.getString("EmpGender");
                                                emppan = jsonObject1.getString("EmpPanCard");
                                                empadhar = jsonObject1.getString("EmpAadhaarCard");
                                                empaccname = jsonObject1.getString("EmpAccountName");
                                                empaccno = jsonObject1.getString("EmpAccountNo");
                                                empifscno = jsonObject1.getString("EmpIfscCode");
                                                empbankname = jsonObject1.getString("EmpBankName");
                                                empbranchname = jsonObject1.getString("EmpBranchName");
                                                empisactive = jsonObject1.getString("IsActive");
                                                counselorid = jsonObject1.getString("cCounselorID");
                                                empdesignation = jsonObject1.getString("EmpDesignation");
                                                empdepartment = jsonObject1.getString("EmpDepartment");
                                                empleavebal = jsonObject1.getString("Leavebal");
                                            }
                                          /*  if(empno.equals(""))
                                            {
                                                empno="NA";
                                            }
                                        if(empid.equals(""))
                                        {
                                            empid="NA";
                                        }
                                        if(empname.equals(""))
                                        {
                                            empname="NA";
                                        }
                                        if(empjoindate.equals(""))
                                        {
                                            empjoindate="NA";
                                        }
                                        if(empmobile.equals(""))
                                        {
                                            empmobile="NA";
                                        }
                                        if(empemail.equals(""))
                                        {
                                            empemail="NA";
                                        }
                                        if(empdob.equals(""))
                                        {
                                            empdob="NA";
                                        }
                                        if(empaddress.equals(""))
                                        {
                                            empaddress="NA";
                                        }
                                        if(empcity.equals(""))
                                        {
                                            empcity="NA";
                                        }
                                        if(empstate.equals(""))
                                        {
                                            empstate="NA";
                                        }
                                        if(emppin.equals(""))
                                        {
                                            emppin="NA";
                                        }
                                        if(empgender.equals(""))
                                        {
                                            empgender="NA";
                                        }
                                        if(emppan.equals(""))
                                        {
                                            emppan="NA";
                                        }
                                        if(empadhar.equals(""))
                                        {
                                            empadhar="NA";
                                        }
                                        if(empaccno.equals(""))
                                        {
                                            empaccno="NA";
                                        }
                                        if(empbranchname.equals(""))
                                        {
                                            empbranchname="NA";
                                        }
                                        if(empbankname.equals(""))
                                        {
                                            empbankname="NA";
                                        }
                                        if(empifscno.equals(""))
                                        {
                                            empifscno="NA";
                                        }
                                        if(empisactive.equals(""))
                                        {
                                            empisactive="NA";
                                        }
                                        if(empdesignation.equals(""))
                                        {
                                            empdesignation="NA";
                                        }
                                        if(empdepartment.equals(""))
                                        {
                                            empdepartment="NA";
                                        }
                                        if(empleavebal.equals(""))
                                        {
                                            empleavebal="NA";
                                        }*/

                                            txtEmpNo.setText(empno);
                                            txtEmpID.setText(empid);
                                            txtEmpName.setText(empname);
                                            txtEmpJoindate.setText(empjoindate);
                                            txtEmpMobile.setText(empmobile);
                                            txtEmpEmail.setText(empemail);
                                            txtEmpDOB.setText(empdob);
                                            txtEmpAddress.setText(empaddress);
                                            txtEmpCity.setText(empcity);
                                            txtEmpState.setText(empstate);
                                            txtEmpPin.setText(emppin);
                                            txtEmpGender.setText(empgender);
                                            txtEmpPanno.setText(emppan);
                                            txtEmpAdharno.setText(empadhar);
                                            txtEmpAccName.setText(empaccname);
                                            txtEmpAccNo.setText(empaccno);
                                            txtEmpBankName.setText(empbankname);
                                            txtEmpBranchName.setText(empbranchname);
                                            txtEmpIFSCNo.setText(empifscno);
                                            txtEmpIsactive.setText(empisactive);
                                            txtEmpDesignation.setText(empdesignation);
                                            txtEmpDepartment.setText(empdepartment);
                                            txtEmpLeavebal.setText(empleavebal);
                                            txtCounselorID.setText(counselorid);
                                        }

                                        //getPendingApproval();
                                    } catch (JSONException e) {
                                        Toast.makeText(EmpDetails.this,"Errorcode-555 EmpDetails getEmpDetailsResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Log.d("CounselorDetailExceptio", String.valueOf(e));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;
                                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EmpDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(EmpDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EmpDetails.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EmpDetails.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                dialog.dismiss();

                            }
                        }).show();

            }
        }catch (Exception e)
        {
            Toast.makeText(EmpDetails.this,"Errorcode-554 EmpDetails getEmpDetails "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcApprovedLeaves", String.valueOf(e));
        }
    }//getEmpDetails
}
