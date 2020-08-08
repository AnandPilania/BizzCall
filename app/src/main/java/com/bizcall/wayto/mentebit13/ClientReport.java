package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import org.json.JSONObject;

import java.util.ArrayList;

public class ClientReport extends AppCompatActivity {

    RecyclerView recyclerClientReport;
    AdapterClientReport adapterClientReport;
    ArrayList<DataClientReport> arrayListClientReport;
    RequestQueue requestQueue;
    SharedPreferences sp;
    ProgressDialog dialog;
    Thread thread;
    long timeout;
    ImageView imgBack;
    TextView txtNoDetails;
    EditText edtSearchtext;
    String clientid="", clienturl="", candidatename="", counselorname="", fileid="", totalINR="", totalUSD="", discountINR="", discountUSD="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_client_report);
            initialize();
            edtSearchtext.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                    Log.d("CountTapped", count + "");
                    if (count == 0) {
                        if (edtSearchtext.getText().toString().length() == 0) {
                            getClientReport();
                        /*adapterAllAmountDetails = new AdapterAllAmountDetails(PaymentApprove.this, arrayListAllAmount);
                        recyclerAllAmount.setAdapter(adapterAllAmountDetails);
                        adapterAllAmountDetails.notifyDataSetChanged();*/
                        } else {
                            filter(edtSearchtext.getText().toString());
                        }
                    } else {
                        filter(edtSearchtext.getText().toString());
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {

                    // filter your list from your input

                    //you can use runnable postDelayed like 500 ms to delay search text
                }
            });

            if (CheckInternetSpeed.checkInternet(ClientReport.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientReport.this);
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
            } else if (CheckInternetSpeed.checkInternet(ClientReport.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientReport.this);
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
                dialog = ProgressDialog.show(ClientReport.this, "", "Loading client report", true);
                newThreadInitilization(dialog);
                //to get all detailed client report
                getClientReport();
            }
        } catch (Exception e) {
            Toast.makeText(ClientReport.this, "Errorcode-478 ClientReport onCreate " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//onCreate

    private void initialize() {
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(ClientReport.this);
        clienturl = sp.getString("ClientUrl", null);
        clientid = sp.getString("ClientId", null);
        timeout = sp.getLong("TimeOut", 0);
        recyclerClientReport = findViewById(R.id.recyclerClientReport);
        txtNoDetails = findViewById(R.id.txtNoDetails);
        edtSearchtext = findViewById(R.id.edtSearchtext);
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void updateList(ArrayList<DataClientReport> list){
        try{
        arrayListClientReport = list;
        /*if(arrayListAllAmount.size()==0)
        {
            linearTitle.setVisibility(View.GONE);
            txtNoDetails.setVisibility(View.VISIBLE);
            recyclerAllAmount.setVisibility(View.GONE);
        }else {*/
        adapterClientReport = new AdapterClientReport(ClientReport.this, arrayListClientReport);
        recyclerClientReport.setAdapter(adapterClientReport);
        adapterClientReport.notifyDataSetChanged();
        //}
        } catch (Exception e) {
            Toast.makeText(ClientReport.this, "Errorcode-479 ClientReport updateList " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
//while searching filter result from list
    void filter(String text){
        try{
        ArrayList<DataClientReport> temp = new ArrayList();
        for(DataClientReport d: arrayListClientReport){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getFileID().contains(text)||d.getCounselorname().contains(text)||d.getName().contains(text)||d.getTotalINR().contains(text)||
                    d.getTotalUSD().contains(text)||d.getDiscountINR().contains(text)||d.getDiscountUSD().contains(text)){
                temp.add(d);
            }

        }
        //update recyclerview
        if(temp.size()==0)
        {
            recyclerClientReport.setVisibility(View.GONE);
            txtNoDetails.setVisibility(View.VISIBLE);
        }
        else {
            recyclerClientReport.setVisibility(View.VISIBLE);
            txtNoDetails.setVisibility(View.GONE);
            updateList(temp);
        }
        } catch (Exception e) {
            Toast.makeText(ClientReport.this, "Errorcode-480 ClientReport filter " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        try{
        Intent intent = new Intent(ClientReport.this, Home.class);
        intent.putExtra("Activity", "ClientReport");
        startActivity(intent);
        finish();
        } catch (Exception e) {
            Toast.makeText(ClientReport.this, "Errorcode-481 ClientReport onBackpressed " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getClientReport() {
        try {
            String url=clienturl + "?clientid=" + clientid +"&caseid=133";
            arrayListClientReport=new ArrayList<>();
            if(CheckServer.isServerReachable(ClientReport.this)) {
                Log.d("StatusReportUrl", url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    dialog.dismiss();
                                    String res = String.valueOf(response);
                                    if (res.contains("[]")) {

                                    } else {

                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("StatusReportResponse", String.valueOf(response));
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                             fileid = jsonObject1.getString("nFileNo");
                                             candidatename = jsonObject1.getString("name");
                                            counselorname = jsonObject1.getString("cCounselorName");
                                            totalINR = jsonObject1.getString("TotalFeeYearR");
                                            totalUSD = jsonObject1.getString("TotalFeeYearD");
                                            discountINR = jsonObject1.getString("Discount INR");
                                            discountUSD = jsonObject1.getString("Discount USD");
                                            DataClientReport dataClientReport = new DataClientReport(fileid,candidatename,counselorname,totalINR,totalUSD,discountINR,discountUSD);
                                            arrayListClientReport.add(dataClientReport);

                                        }
                                        adapterClientReport = new AdapterClientReport(ClientReport.this,arrayListClientReport);
                                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                        recyclerClientReport.setLayoutManager(manager);
                                        recyclerClientReport.setAdapter(adapterClientReport);
                                        adapterClientReport.notifyDataSetChanged();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(ClientReport.this, "Errorcode-483 ClientReport getClientReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Log.d("Exception", String.valueOf(e));
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientReport.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ClientReport.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientReport.this);
                alertDialogBuilder.setTitle("Network issue!!!!")
                        .setMessage("Try after some time!")

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
            Toast.makeText(ClientReport.this, "Errorcode-482 ClientReport getClientReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }//getClientReport
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
                                Toast.makeText(ClientReport.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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

}
