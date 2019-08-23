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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineLead extends AppCompatActivity {

   AdapterOnlineLead adapterOnlineLead;
   RecyclerView recyclerOnlineLead;
   ArrayList<DataCounselor> arrayListOnlineLead;
   UrlRequest urlRequest;
   String clienturl,clientid,name,course,mbl,adrs,city,state1,pincode,parentno,email,fetchedDataFrom,fetchedAllocatedTo,allocatedDate,statusid,remark,fetchedCreatedDate,status11,sr_no,selectedmbl;
   ProgressDialog dialog;
   SharedPreferences sp;
   ImageView imageBack,imgRefresh;
   TextView txtDataRef,txtSelectedStatus,txtDisplayInfo;
   LinearLayout linearLayoutSearch;
   RequestQueue requestQueue;

   ArrayList<String> arrayListSearchAs;
   Spinner spinnerFilter;
   TextView txtAsc,txtDesc,txtNotFound,txtMin,txtMax;
   EditText edtSearchText;
   String strMin,strMax;
   Button btnNext,btnPrevious;
   String counselorid;
   Vibrator vibrator;
   String actname,url,caseid,displayno;
   SharedPreferences.Editor editor;
   TextView txtActivityName;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_online_lead);
            requestQueue=Volley.newRequestQueue(OnlineLead.this);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor=sp.edit();
            clienturl = sp.getString("ClientUrl", null);
            clientid = sp.getString("ClientId", null);
            counselorid = sp.getString("Id", null);
            counselorid=counselorid.replaceAll(" ","");
            imageBack = findViewById(R.id.img_back);
            txtDataRef = findViewById(R.id.txtDataRef);
            txtSelectedStatus = findViewById(R.id.txtSlectedStatus);
            txtDisplayInfo = findViewById(R.id.txtDisplayInfo);
            linearLayoutSearch = findViewById(R.id.linearSearch);
            arrayListSearchAs = new ArrayList<>();
            spinnerFilter = findViewById(R.id.spinnerFilter1);
            txtAsc = findViewById(R.id.txtAsc);
            txtDesc = findViewById(R.id.txtDesc);
            edtSearchText = findViewById(R.id.edtSearchtext);
            // imgSearch = findViewById(R.id.img_search);
            txtNotFound = findViewById(R.id.txtNotFound1);
            txtMin = findViewById(R.id.txtMin);
            txtMax = findViewById(R.id.txtMax);
            btnNext = findViewById(R.id.btnLoadMore);
            btnPrevious = findViewById(R.id.btnLoadPrevious);
            imgRefresh = findViewById(R.id.imgRefresh);
            txtActivityName=findViewById(R.id.txtActivityName);

            // txtSelectedStatus.setVisibility(View.V);
            txtDataRef.setVisibility(View.GONE);
            txtDisplayInfo.setVisibility(View.VISIBLE);
            linearLayoutSearch.setVisibility(View.GONE);
            actname=getIntent().getStringExtra("ActivityLeads");
            Log.d("ActivityLeads",actname);

            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });

            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshClicked();
                }
            });
           loadOnlineLead();
          btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   btnNextClicked();
                }
            });
            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnPreviousClicked();
                   // refreshWhenLoading();

                }
            });
        }catch (Exception e)
        {
            Toast.makeText(OnlineLead.this,"Errorcode-226 OnlineLead onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExceptionOnlineLead", String.valueOf(e));
        }
   }//onCreate

        @Override
    public void onBackPressed() {
       try{
       Intent intent = new Intent(OnlineLead.this, Home.class);
        intent.putExtra("Activity", "OnlineLead");
        //intent.putExtra("MainMenu",MainMenuselected);
        startActivity(intent);
       }catch (Exception e)
       {
           Toast.makeText(OnlineLead.this,"Errorcode-233 OnlineLead onBackPressed "+e.toString(),Toast.LENGTH_SHORT).show();
       }
    }
    public void loadOnlineLead()
    {
        try {
            if (CheckInternetSpeed.checkInternet(OnlineLead.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OnlineLead.this);
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
            } else if (CheckInternetSpeed.checkInternet(OnlineLead.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OnlineLead.this);
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
                dialog = ProgressDialog.show(OnlineLead.this, "", "Loading online leads", true);
                if (actname.equals("OnlineLead") || actname.contains("RefreshOnlineLead") || actname.equals("ContactOnlineLead")) {
                    caseid = "59";
                    editor.putString("ActOnlineLead", "OnlineLead");
                    editor.commit();
                } else {
                    caseid = "72";
                    txtActivityName.setText("ConvertedOnlineLead");
                    editor.putString("ActOnlineLead", "ConvertedOnlineLead");
                    editor.commit();
                }

                if (actname.contains("RefreshOnlineLead") || actname.contains("RefreshConvertedOnlineLead") || actname.contains("ContactConvertedOnlineLead") || actname.contains("ContactOnlineLead")) {
                    strMin = sp.getString("MinVal", null);
                    strMax = sp.getString("MaxVal", null);
                } else {
                    strMin = "1";
                    strMax = "25";

                }
                txtMin.setText(strMin);
                txtMax.setText(strMax);
                editor.putString("MinVal", txtMin.getText().toString());
                editor.putString("MaxVal", txtMax.getText().toString());
                editor.commit();
                txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
                getOnlineLead(caseid, strMin, strMax);
            }
        }catch (Exception e)
        {
            Toast.makeText(OnlineLead.this,"Errorcode-227 OnlineLead loadOnlineLead "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnNextClicked()
    {
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) + 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) + 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            if (CheckInternetSpeed.checkInternet(OnlineLead.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OnlineLead.this);
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
            } else if (CheckInternetSpeed.checkInternet(OnlineLead.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OnlineLead.this);
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
                editor.putString("MinVal", txtMin.getText().toString());
                editor.putString("MaxVal", txtMax.getText().toString());
                editor.commit();
                dialog = ProgressDialog.show(OnlineLead.this, "", "Loading online leads", true);
                getOnlineLead(caseid, strMin, strMax);
            }
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
        }catch (Exception e)
        {
            Toast.makeText(OnlineLead.this,"Errorcode-228 OnlineLead btnNextClicked "+e.toString(),Toast.LENGTH_SHORT).show();
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
            if (CheckInternetSpeed.checkInternet(OnlineLead.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OnlineLead.this);
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
            } else if (CheckInternetSpeed.checkInternet(OnlineLead.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OnlineLead.this);
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
                editor.putString("MinVal", txtMin.getText().toString());
                editor.putString("MaxVal", txtMax.getText().toString());
                editor.commit();
                dialog = ProgressDialog.show(OnlineLead.this, "", "Loading online leads", true);
                getOnlineLead(caseid, strMin, strMax);
            }
        }catch (Exception e)
        {
            Toast.makeText(OnlineLead.this,"Errorcode-229 OnlineLead btnPreviousClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void refreshClicked()
    {
        try {
            vibrator.vibrate(100);
            editor.putString("MinVal", txtMin.getText().toString());
            editor.putString("MaxVal", txtMax.getText().toString());
            editor.commit();
            Intent intent = new Intent(OnlineLead.this, OnlineLead.class);
            if (actname.equals("OnlineLead") || actname.equals("ConvertedOnlineLead")) {
                intent.putExtra("ActivityLeads", "Refresh" + actname);
            } else {
                intent.putExtra("ActivityLeads", actname);
            }
            startActivity(intent);
        }catch (Exception e)
        {
            Toast.makeText(OnlineLead.this,"Errorcode-230 OnlineLead btnPreviousClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void refreshWhenLoading() {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if (dialog.isShowing()) {
                    Intent intent = new Intent(OnlineLead.this, OnlineLead.class);
                     intent.putExtra("Activity",actname);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 5000); // after 12 second (or 2000 miliseconds), the task will be active.
    }
    public void getOnlineLead(String caseid1,String strMin1,String strMax1)
        {
            try{
                if(CheckServer.isServerReachable(OnlineLead.this)) {
                    arrayListOnlineLead = new ArrayList<>();
                    url = clienturl + "?clientid=" + clientid + "&caseid=" + caseid1 + "&MinVal=" + strMin1 + "&MaxVal=" + strMax1 + "&CounselorID=" + counselorid;
                    Log.d("OnlineLeadUrl", url);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        dialog.dismiss();
                                        Log.d("OnlineLeadResponse", response);
                                        //int mval=Integer.parseInt(txtMax.getText().toString());
                                        if (response.contains("[]") || response.contains("You are not authorized")) {
                                            txtNotFound.setVisibility(View.VISIBLE);
                                        } else {
                                            txtNotFound.setVisibility(View.GONE);

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
                                        }

                                        //  recyclerOnlineLead=findViewById(R.id.recyclerNotification);
                                        adapterOnlineLead = new AdapterOnlineLead(OnlineLead.this, arrayListOnlineLead);
                                        recyclerOnlineLead = findViewById(R.id.recyclerOnlineLead);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerOnlineLead.setLayoutManager(layoutManager);
                                        recyclerOnlineLead.setAdapter(adapterOnlineLead);
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
                                        Toast.makeText(OnlineLead.this, "Errorcode-232 OnlineLead OnlineLeadResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    if (error.networkResponse != null || error.networkResponse.equals("TimeoutError") || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OnlineLead.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")
                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(OnlineLead.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                }else {
                    if(dialog.isShowing()){
                        dialog.dismiss();}
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OnlineLead.this);
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
                Toast.makeText(OnlineLead.this,"Errorcode-231 OnlineLead getOnlineLead "+e.toString(),Toast.LENGTH_SHORT).show();            }
        }
}
