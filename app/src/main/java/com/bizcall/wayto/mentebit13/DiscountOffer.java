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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class DiscountOffer extends AppCompatActivity {

   TextView txtClgFee,txtClgFinal,txtHostalFee,txtHostalFinal,txtOTPFee,txtOTPFinal,txtAccidentalFee,txtAccidentalFinal,txtMessFee,txtMessFinal,txtTotalFeeD,txtTotalDiscountD,txtTotalFinalD;
   TextView txtAirFareFee,txtAirFareFinal,txtIndiaOTCFee,txtIndiaOTCFinal,txtTotalFeeR,txtTotalFinalR,txtUpdateDiscount;
   EditText edtAirFareDiscount,edtIndiaOTCDiscount;
   EditText edtClgDiscount,edtHostalDiscount,edtOTPDiscount,edtAccidentalDiscount,edtMessDiscount;
    private String counselorid,clienturl,clientid,url,fileID;
    String collegeFee1YearD,hostalFee1YearD,OTPFee,mess,accidental,airFareFeeR,indiaOTC,totalFeeYearD,totalFeeYearR;
    String collegeFeeDiscount,hostalFeeDiscount,OTPFeeDiscount,messDiscount,accidentalDiscount,airFareFeeDiscountR,indiaOTCDiscount,totalFeeYearDDiscount,totalFeeYearRDiscount;
   int t11,t12,t13,t14,t15,t16,t17,t1,t2,t3,t4,t5,t6,t7;
   int clgFinal,hostalfinal,OTPFinal,accidentalFinal,messFinal,indiaOTCFinal,airFareFinal,totalUSDFinal,totalINRFinal;
  String actname,strClgFeeDiscount,strClgFeeFinal,strHostalFeeDiscount,strhostalFeeFinal,strOPTDiscount,strOTPFinal,strAccidentalDiscount,strAccidentalFinal,
    strMessDiscount,strMessFinal,strAirFareDiscount,strAirFareFinal,strIndiaOTCDiscount,strIndiaOTCFinal,strTotalFeeD,strTotalFeeR;
    SharedPreferences sp;
    ProgressDialog dialog;
    RequestQueue requestQueue;
    ImageView imgBack;
    Thread thread;
    long timeout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_discountdetails);
            initialize();

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            if (CheckInternetSpeed.checkInternet(DiscountOffer.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DiscountOffer.this);
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
            } else if (CheckInternetSpeed.checkInternet(DiscountOffer.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DiscountOffer.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                dialog = ProgressDialog.show(DiscountOffer.this, "", "Loading discount", true);
                newThreadInitilization(dialog);
                //to get details of  package for allocated college
                getPackageDetails();
            }
            txtUpdateDiscount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strClgFeeDiscount = String.valueOf(t11);
                    strHostalFeeDiscount = String.valueOf(t12);
                    strOPTDiscount = String.valueOf(t13);
                    strAccidentalDiscount = String.valueOf(t14);
                    strMessDiscount = String.valueOf(t15);
                    strAirFareDiscount = String.valueOf(t16);
                    strIndiaOTCDiscount = String.valueOf(t17);

                    strClgFeeFinal = String.valueOf(clgFinal);
                    strhostalFeeFinal = String.valueOf(hostalfinal);
                    strOTPFinal = String.valueOf(OTPFinal);
                    strAccidentalFinal = String.valueOf(accidentalFinal);
                    strMessFinal = String.valueOf(messFinal);
                    strAirFareFinal = String.valueOf(airFareFinal);
                    strIndiaOTCFinal = String.valueOf(indiaOTCFinal);

                    strTotalFeeD = String.valueOf(totalUSDFinal);
                    strTotalFeeR = String.valueOf(totalINRFinal);


                    if (CheckInternetSpeed.checkInternet(DiscountOffer.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DiscountOffer.this);
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
                    } else if (CheckInternetSpeed.checkInternet(DiscountOffer.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DiscountOffer.this);
                        alertDialogBuilder.setTitle("Slow Internet speed!!!")
                                .setMessage("Can't do further process")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //insertIMEI();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        dialog = ProgressDialog.show(DiscountOffer.this, "", "Updating discount", true);
                       newThreadInitilization(dialog);
                       //to update discount details
                        updateDiscount();
                    }
                }
            });
            edtClgDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if college discount field is changed in USD set updated value to edtClgDiscount
                    getAmountUSD("ClgFeeD");

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtHostalDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if hostal discount field is changed in USD set updated value to edtHostalDiscount
                    getAmountUSD("HostalFeeD");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtOTPDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if OTP discount field is changed in USD set updated value to edtOTPDiscount
                    getAmountUSD("OTPFee");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtAccidentalDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if accidental discount field is changed in USD set updated value to edtAccidentalDiscount
                    getAmountUSD("AccidentalFee");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtMessDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if mess discount field is changed in USD set updated value to edtMessDiscount
                    getAmountUSD("MessFee");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtAirFareDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if aiefare discount field is changed in INR set updated value to edtAirFareDiscount
                    getAmountINR("AirFee");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtIndiaOTCDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if IndiaOTP discount field is changed in INR set updated value to edtIndaiOTCDiscount
                    getAmountINR("IndiaOTC");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }catch (Exception e)
        {
            Toast.makeText(DiscountOffer.this,"Errorcode-472 DiscountOffer onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }//onCreate

    private void initialize() {
        requestQueue = Volley.newRequestQueue(DiscountOffer.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        counselorid = sp.getString("Id", "");
        timeout = sp.getLong("TimeOut", 0);
        fileID = getIntent().getStringExtra("FineNo1");
        txtClgFee = findViewById(R.id.txtCollegeFees);
        txtClgFinal = findViewById(R.id.txtClgFinal);
        txtHostalFee = findViewById(R.id.txtHostalFees);
        txtHostalFinal = findViewById(R.id.txtHostalFinal);
        txtOTPFee = findViewById(R.id.txtOTProcessingFee);
        txtOTPFinal = findViewById(R.id.txtOTPFinal);
        txtAccidentalFee = findViewById(R.id.txtAccidentalFee);
        txtAccidentalFinal = findViewById(R.id.txtAccidentalFinal);
        txtMessFee = findViewById(R.id.txtMessFee);
        txtMessFinal = findViewById(R.id.txtMessFinal);
        txtTotalFeeD = findViewById(R.id.txtTotalFeeD);
        txtTotalDiscountD = findViewById(R.id.txtTotalDiscountD);
        txtTotalFinalD = findViewById(R.id.txtTotalFinalD);
        edtClgDiscount = findViewById(R.id.edtClgDiscount);
        edtHostalDiscount = findViewById(R.id.edtHostalDiscount);
        edtOTPDiscount = findViewById(R.id.edtOTPDiscount);
        edtAccidentalDiscount = findViewById(R.id.edtAccidentalDiscount);
        edtMessDiscount = findViewById(R.id.edtMessDiscount);
        txtAirFareFee = findViewById(R.id.txtAirfareFees);
        txtAirFareFinal = findViewById(R.id.txtAirFareFinal);
        txtIndiaOTCFee = findViewById(R.id.txtIndiaOTCFee);
        txtIndiaOTCFinal = findViewById(R.id.txtIndiaOTCFinal);
        edtAirFareDiscount = findViewById(R.id.edtAirFareFeeR);
        edtIndiaOTCDiscount = findViewById(R.id.edtIndiaOTC);
        txtTotalFeeR = findViewById(R.id.txtTotalFeeR);
        txtTotalFinalR = findViewById(R.id.txtTotalFinalR);
        txtUpdateDiscount = findViewById(R.id.txtUpdateDiscount);

        actname=getIntent().getStringExtra("ActName");

        imgBack = findViewById(R.id.img_back);
    }//initialize

    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(DiscountOffer.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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

    public void getAmountUSD(String edtname)
    {
        try {
            if (edtname.contains("ClgFeeD")) {
                if (edtClgDiscount.getText().toString().isEmpty()) {
                    t11 = 0;
                } else {
                    t11 = Integer.parseInt(edtClgDiscount.getText().toString());
                }
                if (txtClgFee.getText().toString().isEmpty()) {
                    t1 = 0;
                } else {
                    t1 = Integer.parseInt(txtClgFee.getText().toString());
                }
                clgFinal = t1 - t11;
                txtClgFinal.setText(String.valueOf(clgFinal));
            } else if (edtname.contains("HostalFeeD")) {
                if (edtHostalDiscount.getText().toString().isEmpty()) {
                    t12 = 0;
                } else {
                    t12 = Integer.parseInt(edtHostalDiscount.getText().toString());
                }
                if (txtHostalFee.getText().toString().isEmpty()) {
                    t2 = 0;
                } else {
                    t2 = Integer.parseInt(txtHostalFee.getText().toString());
                }
                hostalfinal = t2 - t12;
                txtHostalFinal.setText(String.valueOf(hostalfinal));
            } else if (edtname.contains("OTPFee")) {

                if (edtOTPDiscount.getText().toString().isEmpty()) {
                    t13 = 0;
                } else {
                    t13 = Integer.parseInt(edtOTPDiscount.getText().toString());
                }
                if (txtOTPFee.getText().toString().isEmpty()) {
                    t3 = 0;
                } else {
                    t3 = Integer.parseInt(txtOTPFee.getText().toString());
                }
                OTPFinal = t3 - t13;
                txtOTPFinal.setText(String.valueOf(OTPFinal));
            } else if (edtname.contains("AccidentalFee")) {
                if (edtAccidentalDiscount.getText().toString().isEmpty()) {
                    t14 = 0;
                } else {
                    t14 = Integer.parseInt(edtAccidentalDiscount.getText().toString());
                }
                if (txtAccidentalFee.getText().toString().isEmpty()) {
                    t4 = 0;
                } else {
                    t4 = Integer.parseInt(txtAccidentalFee.getText().toString());
                }
                accidentalFinal = t4 - t14;
                txtAccidentalFinal.setText(String.valueOf(accidentalFinal));
            } else if (edtname.contains("MessFee")) {
                if (edtMessDiscount.getText().toString().isEmpty()) {
                    t15 = 0;
                } else {
                    t15 = Integer.parseInt(edtMessDiscount.getText().toString());
                }
                if (txtMessFee.getText().toString().isEmpty()) {
                    t5 = 0;
                } else {
                    t5 = Integer.parseInt(txtMessFee.getText().toString());
                }
                messFinal = t5 - t15;
                txtMessFinal.setText(String.valueOf(messFinal));
            }

            totalUSDFinal = clgFinal + hostalfinal + accidentalFinal + OTPFinal + messFinal;
            txtTotalFinalD.setText(String.valueOf(totalUSDFinal));
        }catch (Exception e)
        {
            Toast.makeText(DiscountOffer.this,"Errorcode-473 DiscountOffer getAmountUSD "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//getAmountUSD
    public void getAmountINR(String edtname)
    {
        try {
            if (edtname.contains("AirFee")) {
                // String edtAirval=edtAirFareDiscount.getText().toString();
                //String strtxtAirval=txtAirFareFee.getText().toString();
                if (edtAirFareDiscount.getText().toString() == null || edtAirFareDiscount.getText().toString().isEmpty() || edtAirFareDiscount.getText().toString().equals("null")) {
                    t16 = 0;
                } else {
                    t16 = Integer.parseInt(edtAirFareDiscount.getText().toString());
                }

                if (txtAirFareFee.getText().toString() == null || txtAirFareFee.getText().toString().isEmpty() || txtAirFareFee.getText().toString().equals("null")) {
                    t6 = 0;
                } else {
                    t6 = Integer.parseInt(txtAirFareFee.getText().toString());
                }
                airFareFinal = t6 - t16;
                txtAirFareFinal.setText(String.valueOf(airFareFinal));
            } else if (edtname.contains("IndiaOTC")) {
                String edtIndiaval = edtIndiaOTCDiscount.getText().toString();
                if (edtIndiaval == null || edtIndiaval.isEmpty() || edtIndiaval.equals("null")) {
                    t17 = 0;
                } else {
                    t17 = Integer.parseInt(edtIndiaOTCDiscount.getText().toString());
                }
                String txtindiaval = txtIndiaOTCFee.getText().toString();
                if (txtindiaval == null || txtindiaval.isEmpty() || txtindiaval.equals("null")) {
                    t7 = 0;
                } else {
                    t7 = Integer.parseInt(txtIndiaOTCFee.getText().toString());
                }
                indiaOTCFinal = t7 - t17;
                txtIndiaOTCFinal.setText(String.valueOf(indiaOTCFinal));
            }
            totalINRFinal = airFareFinal + indiaOTCFinal;

            txtTotalFinalR.setText(String.valueOf(totalINRFinal));
        }catch (Exception e)
        {
            Toast.makeText(DiscountOffer.this,"Errorcode-474 DiscountOffer getAmountINR "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//getAmountINR
    public void updateDiscount()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=124&CollegeFeeDiscount="+strClgFeeDiscount+"&CollegeFeeFinal="+strClgFeeFinal+"&HostalFeeDiscount="+strHostalFeeDiscount+"&HostalFeeFinal="+strhostalFeeFinal+"&OTPFeeDiscount="+OTPFeeDiscount+
                    "&OTPFeeFinal="+strOTPFinal+"&AccidentalDiscount="+strAccidentalDiscount+"&AccidentalFinal="+strAccidentalFinal+
                    "&MessDiscount="+strMessDiscount+"&MessFinal="+strMessFinal+"&IndiaOTCDiscount="+strIndiaOTCDiscount+"&IndiaOTCFinal="+strIndiaOTCFinal+"&AirFareDiscount="+strAirFareDiscount+"&AirFareFinal="+strAirFareFinal+
                    "&TotalFeeD="+strTotalFeeD+"&TotalFeeR="+strTotalFeeR+"&FileNo="+fileID;
            Log.d("DiscountUpdateUrl", url);
            if (CheckInternet.checkInternet(DiscountOffer.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("DiscountUpdateResponse", response);
                                try {
                                    dialog.dismiss();
                                    if (response.contains("Data updated successfully")) {
                                        Intent intent=new Intent(DiscountOffer.this,ClientAccount.class);
                                        intent.putExtra("FileID", fileID);
                                        intent.putExtra("ActName", "DiscountOffer");
                                        startActivity(intent);

                                        Toast.makeText(DiscountOffer.this, "Payment updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DiscountOffer.this, "Payment updation failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(DiscountOffer.this,"Errorcode-476 DiscountOffer updateDiscountResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;

                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DiscountOffer.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(DiscountOffer.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DiscountOffer.this);
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
            dialog.dismiss();
            Toast.makeText(DiscountOffer.this,"Errorcode-475 DiscountOffer updateDiscount "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//updateDiscount

    public void getPackageDetails()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=123&FileNo="+fileID;
            Log.d("DiscountDetailsUrl", url);
            if (CheckInternet.checkInternet(DiscountOffer.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("DiscountDetailsResponse", response);
                                try {

                                    dialog.dismiss();
                                    if (response.contains("[]")) {

                                        Toast.makeText(DiscountOffer.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            collegeFeeDiscount= jsonObject1.getString("CollegeFee1YearD_Discount");
                                            hostalFeeDiscount = jsonObject1.getString("HostalFee1YearD_Discount");
                                            OTPFeeDiscount = jsonObject1.getString("OneTimeProcessingFeeD_Discount");
                                            accidentalDiscount=jsonObject1.getString("Accidental_Insurance_Discount");
                                            messDiscount=jsonObject1.getString("MessFeeD_Discount");
                                          //  totalFeeYearDDiscount = jsonObject1.getString("TotalFeeYearD_Discount");
                                            airFareFeeDiscountR = jsonObject1.getString("AirFareFeeR_Discount");
                                            indiaOTCDiscount=jsonObject1.getString("IndiaOTC_Discount");
                                           // totalFeeYearRDiscount = jsonObject1.getString("TotalFeeYearR_Discount");

                                            collegeFee1YearD= jsonObject1.getString("CollegeFee1YearD");
                                            hostalFee1YearD = jsonObject1.getString("HostalFee1YearD");
                                            OTPFee = jsonObject1.getString("OneTimeProcessingFeeD");
                                            accidental=jsonObject1.getString("Accidental_Insurance");
                                            mess=jsonObject1.getString("MessFeeD");
                                            totalFeeYearD = jsonObject1.getString("TotalFeeYearD");
                                            airFareFeeR = jsonObject1.getString("AirFareFeeR");
                                            indiaOTC=jsonObject1.getString("IndiaOTC");
                                            totalFeeYearR = jsonObject1.getString("TotalFeeYearR");
                                        }
                                        if (airFareFeeR == null || airFareFeeR.isEmpty() || airFareFeeR.equals("null")||airFareFeeR.contains("NA")) {
                                            txtAirFareFee.setText("0");
                                        }
                                        else {
                                            txtAirFareFee.setText(airFareFeeR);
                                        }
                                        if (indiaOTC == null || indiaOTC.isEmpty() || indiaOTC.equals("null")||indiaOTC.contains("NA")) {
                                            txtIndiaOTCFee.setText("0");
                                        }else {
                                            txtIndiaOTCFee.setText(indiaOTC);
                                        }
                                        if (totalFeeYearR == null || totalFeeYearR.isEmpty() || totalFeeYearR.equals("null")||totalFeeYearR.contains("NA")) {
                                            txtTotalFeeR.setText("0");
                                        }else {
                                            txtTotalFeeR.setText(totalFeeYearR);
                                        }
                                        if (airFareFeeDiscountR == null || airFareFeeDiscountR.isEmpty() || airFareFeeDiscountR.contains("null")||airFareFeeDiscountR.contains("NA"))
                                        {
                                            edtAirFareDiscount.setText("0");
                                        }
                                        else {
                                            edtAirFareDiscount.setText(airFareFeeDiscountR);
                                        }
                                        if (indiaOTCDiscount == null || indiaOTCDiscount.isEmpty() || indiaOTCDiscount.equals("null")||indiaOTCDiscount.contains("NA"))
                                        {
                                            edtIndiaOTCDiscount.setText("0");
                                        }
                                        else {
                                            edtIndiaOTCDiscount.setText(indiaOTCDiscount);
                                        }
                                        if (collegeFee1YearD == null || collegeFee1YearD.isEmpty() || collegeFee1YearD.equals("null")||collegeFee1YearD.contains("NA"))
                                        {
                                            txtClgFee.setText("0");
                                        }else {
                                            txtClgFee.setText(collegeFee1YearD);
                                        }
                                        if (hostalFee1YearD == null || hostalFee1YearD.isEmpty() || hostalFee1YearD.equals("null")||hostalFee1YearD.contains("NA")) {
                                            txtHostalFee.setText("0");
                                        }
                                        else {
                                            txtHostalFee.setText(hostalFee1YearD);
                                        }
                                        if (OTPFee == null || OTPFee.isEmpty() || OTPFee.equals("null")||OTPFee.contains("NA")) {
                                            txtOTPFee.setText("0");
                                        }else {
                                            txtOTPFee.setText(OTPFee);
                                        }
                                        if (accidental == null || accidental.isEmpty() || accidental.equals("null")||accidental.contains("NA")) {
                                            txtAccidentalFee.setText("0");
                                        }
                                        else {
                                            txtAccidentalFee.setText(accidental);
                                        }
                                        if (mess == null || mess.isEmpty() || mess.equals("null")||mess.contains("NA")) {
                                            txtMessFee.setText("0");
                                        }else
                                        {
                                            txtMessFee.setText(mess);
                                        }
                                        if (totalFeeYearD == null || totalFeeYearD.isEmpty() || totalFeeYearD.equals("null")||totalFeeYearD.contains("NA")) {
                                            txtTotalFeeD.setText("0");
                                        }else {
                                            txtTotalFeeD.setText(totalFeeYearD);
                                        }
                                        if (collegeFeeDiscount == null || collegeFeeDiscount.isEmpty() || collegeFeeDiscount.equals("null")||collegeFeeDiscount.contains("NA")) {
                                            edtClgDiscount.setText("0");
                                        }
                                        else {
                                            edtClgDiscount.setText(collegeFeeDiscount);
                                        }
                                        if (hostalFeeDiscount == null || hostalFeeDiscount.isEmpty() || hostalFeeDiscount.equals("null")||hostalFeeDiscount.contains("NA")) {
                                            edtHostalDiscount.setText(hostalFeeDiscount);
                                        }
                                        else {
                                            edtHostalDiscount.setText(hostalFeeDiscount);
                                        }
                                        if (OTPFeeDiscount == null || OTPFeeDiscount.isEmpty() || OTPFeeDiscount.equals("null")||OTPFeeDiscount.contains("NA")) {
                                            edtOTPDiscount.setText("0");
                                        }
                                        else {
                                            edtOTPDiscount.setText(OTPFeeDiscount);
                                        }
                                        if (accidentalDiscount == null || accidentalDiscount.isEmpty() || accidentalDiscount.equals("null")||accidentalDiscount.contains("NA")) {
                                            edtAccidentalDiscount.setText("0");
                                        }
                                        else {
                                            edtAccidentalDiscount.setText(accidentalDiscount);
                                        }
                                        if (messDiscount == null || messDiscount.isEmpty() || messDiscount.equals("null")||messDiscount.contains("NA")) {
                                            edtMessDiscount.setText("0");
                                        }
                                        else {
                                            edtMessDiscount.setText(messDiscount);
                                        }
                                       /* txtAirFareFeeTotal.setText(airFareFeeR);
                                        txtIndiaOTCTotal.setText(otherFee3D);
                                        txtTotalFeeTotalR.setText(totalFeeYearR);*/

                                    }
                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    Toast.makeText(DiscountOffer.this,"Errorcode-478 DiscountOffer gPackageDetailsResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;

                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DiscountOffer.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(DiscountOffer.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DiscountOffer.this);
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
            dialog.dismiss();
            Toast.makeText(DiscountOffer.this,"Errorcode-477 DiscountOffer getPackageDetails "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//getPackageDetails
}
