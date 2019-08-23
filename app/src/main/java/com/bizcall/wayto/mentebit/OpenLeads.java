package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class OpenLeads extends AppCompatActivity {

   Spinner spinnerFilter;
   ArrayList<String> arrayListOpenLeadRefName,arrayListOpenLeadRefId;
   RecyclerView recyclerOpenLead;
   AdapterOnlineLead adapterOnlineLead;
   String url,strMin,strMax,caseid,strRefId,strRefName;
   UrlRequest urlRequest;
   ArrayList<DataCounselor> arrayListOnlineLead;
   TextView txtMax,txtMin,txtNotFound,txtDisplayInfo;
   Button btnPrevious,btnNext;
   ProgressDialog dialog;
   String activity,clienturl,clientid,counselorid,name,course,mbl,adrs,city,state1,pincode,parentno,email,fetchedDataFrom,fetchedAllocatedTo,allocatedDate,statusid,remark,fetchedCreatedDate,status11;
   SharedPreferences sp;
   SharedPreferences.Editor editor;
   ImageView imgBack,imgRefresh;
   Vibrator vibrator;
   int spinnerPos,check=0;
   RequestQueue requestQueue;
   String dataRefName,dataFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_open_leads);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            requestQueue = Volley.newRequestQueue(OpenLeads.this);
            //  recyclerOpenLead=findViewById(R.id.recyclerOpenLeads);
            spinnerFilter = findViewById(R.id.spinnerFilter);
            txtNotFound = findViewById(R.id.txtNotFound1);
            txtMin = findViewById(R.id.txtMin);
            txtMax = findViewById(R.id.txtMax);
            btnNext = findViewById(R.id.btnLoadMore);
            btnPrevious = findViewById(R.id.btnLoadPrevious);
            txtDisplayInfo = findViewById(R.id.txtDisplayInfo);
            imgBack = findViewById(R.id.img_back);
            imgRefresh=findViewById(R.id.imgRefresh);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            clientid = sp.getString("ClientId", null);
            clienturl = sp.getString("ClientUrl", null);
            counselorid = sp.getString("Id", null);
            activity=getIntent().getStringExtra("Activity");
            // imgRefresh = findViewById(R.id.imgRefresh);
            arrayListOpenLeadRefName = new ArrayList<>();
            arrayListOpenLeadRefId = new ArrayList<>();
            Log.d("Act",activity);
            editor=sp.edit();


           /* strMin = "1";
            strMax = "25";
            txtMin.setText(strMin);
            txtMax.setText(strMax);*/
            // arrayListOpenLeadRefName.add("--Select Leads--");
            //  arrayListSpinnerValues.add("Online Leads");
            editor.putString("ActOnlineLead","OpenLeads");
            editor.commit();
            caseid = "75";
            loadRefnames();

            spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 try {

                     if (++check > 1) {
                         strMin = "1";
                         strMax = "25";
                         txtMin.setText(strMin);
                         txtMax.setText(strMax);
                         txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
                         //}
                         strRefName = spinnerFilter.getSelectedItem().toString();
                         spinnerPos = spinnerFilter.getSelectedItemPosition();
                         editor = sp.edit();
                         editor.putInt("Pos", spinnerPos);
                         editor.commit();

                         strRefId = arrayListOpenLeadRefId.get(position);
                         Log.d("RefId", strRefId);
                         strRefName = arrayListOpenLeadRefName.get(position);
                         editor.putString("DtaFrom", strRefId);
                         editor.putString("DataRefName", strRefName);
                         editor.commit();
                         if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("0")) {
                             android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
                         } else if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("1")) {
                             android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
                             editor.putString("Min", txtMin.getText().toString());
                             editor.putString("Max", txtMax.getText().toString());
                             editor.commit();
                             dialog = ProgressDialog.show(OpenLeads.this, "", "Loading open leads", true);
                             getOpenLead(strRefId, caseid, strMin, strMax);
                         }
                     }
                 }catch (Exception e)
                 {
                     Toast.makeText(OpenLeads.this,"Errorcode-235 OpenLeads spinnerItemSelected "+e.toString(),Toast.LENGTH_SHORT).show();
                 }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(OpenLeads.this,OpenLeads.class);
                    intent.putExtra("Activity","RefreshedOpenLead");
                    startActivity(intent);
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnNextClicked();
                // refreshWhenLoading();
                }
            });
            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnPreviousClicked();
                    // refreshWhenLoading();

                }
            });


        } catch (Exception e) {
            Toast.makeText(OpenLeads.this,"Errorcode-234 OpenLeads onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }//onCreate

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(OpenLeads.this, Home.class);
            intent.putExtra("Activity", "OpenLead");
            //intent.putExtra("MainMenu",MainMenuselected);
            startActivity(intent);
        }catch (Exception e)
        {
            Toast.makeText(OpenLeads.this,"Errorcode-236 OpenLeads onBackPressed "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnNextClicked(){
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) + 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) + 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
            } else if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
                editor.putString("Min", txtMin.getText().toString());
                editor.putString("Max", txtMax.getText().toString());
                editor.commit();
                dialog = ProgressDialog.show(OpenLeads.this, "", "Loading leads", true);
                getOpenLead(strRefId, caseid, strMin, strMax);
            }
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
        }catch (Exception e)
        {
            Toast.makeText(OpenLeads.this,"Errorcode-237 OpenLeads onNextClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnPreviousClicked()
    {
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) - 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) - 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
            if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
            } else if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
                editor.putString("Min", txtMin.getText().toString());
                editor.putString("Max", txtMax.getText().toString());
                editor.commit();
                dialog = ProgressDialog.show(OpenLeads.this, "", "Loading leads", true);
                getOpenLead(strRefId, caseid, strMin, strMax);
            }
        }catch (Exception e)
        {
            Toast.makeText(OpenLeads.this,"Errorcode-238 OpenLeads btnPreviousClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void loadRefnames() {
        try {
            if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
            } else if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
                dialog = ProgressDialog.show(OpenLeads.this, "", "Loading Open leads with RefName", true);
                getOpenLeadRef();
            }
        }catch (Exception e)
        {
            Toast.makeText(OpenLeads.this,"Errorcode-239 OpenLeads loadRefNames "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    public void getOpenLead(String refId,String caseid1, String strMin1, String strMax1)
    {
        try{
        url=clienturl+"?clientid=" + clientid + "&caseid="+caseid1+"&MinVal="+strMin1+"&MaxVal="+strMax1+"&DataRefID="+refId;
        Log.d("OpenLeadUrl",url);
        arrayListOnlineLead=new ArrayList<>();
        if(CheckServer.isServerReachable(OpenLeads.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(dialog.isShowing()){
                                dialog.dismiss();}
                            Log.d("OpeneLeadResponse", response);
                            int mval = Integer.parseInt(txtMax.getText().toString());
                            try {
                                if (response.contains("[]") || response.contains("You are not authorized")) {
                                    txtNotFound.setVisibility(View.VISIBLE);
                                } else {
                                    txtNotFound.setVisibility(View.GONE);
                                }
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d("Json", jsonObject.toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("Length", String.valueOf(jsonArray.length()));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String sr_no = String.valueOf(jsonObject1.getInt("nSrNo"));
                                    name = jsonObject1.getString("cCandidateName");
                                    course = jsonObject1.getString("cCourse");
                                    mbl = jsonObject1.getString("cMobile");
                                    adrs = jsonObject1.getString("cAddressLine");
                                    city = jsonObject1.getString("cCity");
                                    state1 = jsonObject1.getString("cState");
                                    pincode = jsonObject1.getString("cPinCode");
                                    parentno = jsonObject1.getString("cParantNo");
                                    email = jsonObject1.getString("cEmail");
                                    fetchedDataFrom = jsonObject1.getString("cDataFrom");
                                    fetchedAllocatedTo = jsonObject1.getString("AllocatedTo");
                                    allocatedDate = jsonObject1.getString("AllocationDate");
                                    statusid = jsonObject1.getString("CurrentStatus");
                                    remark = jsonObject1.getString("cRemarks");
                                    fetchedCreatedDate = jsonObject1.getString("dtCreatedDate");
                                    status11 = jsonObject1.getString("cStatus");
                                    // displayno=jsonObject1.getString("displayno");
                                    DataCounselor dataCounselor = new DataCounselor("1", sr_no, name, course, mbl, parentno, email, allocatedDate, adrs,
                                            city, state1, pincode, statusid, status11, remark);
                                    arrayListOnlineLead.add(dataCounselor);

                                }
                                //  recyclerOnlineLead=findViewById(R.id.recyclerNotification);
                                adapterOnlineLead = new AdapterOnlineLead(OpenLeads.this, arrayListOnlineLead);

                                recyclerOpenLead = findViewById(R.id.recyclerOpenLeads);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(OpenLeads.this);
                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerOpenLead.setLayoutManager(layoutManager);
                                recyclerOpenLead.setAdapter(adapterOnlineLead);
                                adapterOnlineLead.notifyDataSetChanged();
                                Log.d("OnlineArraysize", String.valueOf(arrayListOnlineLead.size()));


                                if (txtMin.getText().toString().equals("1")) {
                                    btnPrevious.setVisibility(View.GONE);
                                    btnNext.setVisibility(View.GONE);
                                    if (25 <= arrayListOnlineLead.size()) {
                                        btnNext.setVisibility(View.VISIBLE);
                                    } else {
                                        btnNext.setVisibility(View.GONE);
                                    }
                                } else if (25 > arrayListOnlineLead.size()) {
                                    btnPrevious.setVisibility(View.VISIBLE);
                                    btnNext.setVisibility(View.GONE);
                                } else {
                                    btnNext.setVisibility(View.VISIBLE);
                                    btnPrevious.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(OpenLeads.this, "Errorcode-241 OpenLeads getOpenLeadResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("OnlineLeadException", String.valueOf(e));
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
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")
                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(OpenLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }
                        }
                    });
            requestQueue.add(stringRequest);
        }else {
            if(dialog.isShowing()){
                dialog.dismiss();}
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
            alertDialogBuilder.setTitle("Server Down!!!!")
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
            Log.d("ExcOpenLead",e.toString());
            Toast.makeText(OpenLeads.this,"Errorcode-240 OpenLeads getOpenLead "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
    public void getOpenLeadRef() {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=74";
            arrayListOnlineLead = new ArrayList<>();
            Log.d("OpenLeadRefUrl", url);
            if(CheckServer.isServerReachable(OpenLeads.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(dialog.isShowing()){
                                        dialog.dismiss();}
                                    Log.d("OpenLeadResponse", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("Length", String.valueOf(jsonArray.length()));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        dataFrom = String.valueOf(jsonObject1.getInt("DataRefID"));
                                        dataRefName = jsonObject1.getString("DataRefName");
                                        arrayListOpenLeadRefId.add(dataFrom);
                                        arrayListOpenLeadRefName.add(dataRefName);
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter(OpenLeads.this, R.layout.spinner_item1, arrayListOpenLeadRefName);
                                    // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerFilter.setAdapter(arrayAdapter);
                                    if (activity.equals("RefreshedOpenLead") || activity.equals("ContactOpenLeads")) {
                                        spinnerPos = sp.getInt("Pos", 0);
                                        Log.d("PositionSelected", spinnerPos + "");
                                        spinnerFilter.setSelection(spinnerPos);
                                        strRefId = arrayListOpenLeadRefId.get(spinnerPos);
                                        strRefName = arrayListOpenLeadRefName.get(spinnerPos);
                                        editor.putString("DtaFrom", strRefId);
                                        editor.putString("DataRefName", strRefName);
                                        editor.commit();
                                        strMin = sp.getString("Min", null);
                                        strMax = sp.getString("Max", null);
                                        txtMin.setText(strMin);
                                        txtMax.setText(strMax);
                                        txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
                                    } else {
                                        strRefId = arrayListOpenLeadRefId.get(0);
                                        strRefName = arrayListOpenLeadRefName.get(0);
                                        spinnerFilter.setSelection(0);
                                        editor = sp.edit();
                                        editor.putInt("Pos", 0);
                                        editor.commit();
                                        editor.putString("DtaFrom", strRefId);
                                        editor.putString("DataRefName", strRefName);
                                        editor.commit();
                                        strMin = "1";
                                        strMax = "25";
                                        txtMin.setText(strMin);
                                        txtMax.setText(strMax);
                                        txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
                                    }
                                    if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("0")) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
                                    } else if (CheckInternetSpeed.checkInternet(OpenLeads.this).contains("1")) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
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
                                        editor.putString("Min", txtMin.getText().toString());
                                        editor.putString("Max", txtMax.getText().toString());
                                        editor.commit();
                                        dialog = ProgressDialog.show(OpenLeads.this, "", "Loading open leads", true);
                                        getOpenLead(strRefId, caseid, strMin, strMax);
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(OpenLeads.this, "Errorcode-243 OpenLeads OpenLeadRefResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("OpenLeadException", String.valueOf(e));
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")
                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                    if(dialog.isShowing()){
                                        dialog.dismiss();}
                                    Toast.makeText(OpenLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }
                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if(dialog.isShowing()){
                    dialog.dismiss();}
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OpenLeads.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Log.d("ExcOpenLeadRef",e.toString());
            Toast.makeText(OpenLeads.this,"Errorcode-242 OpenLeads getOpenLeadRef "+e.toString(),Toast.LENGTH_SHORT).show();        }
    }
}
