package com.bizcall.wayto.mentebit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
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
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityCounselorwiseLeads extends AppCompatActivity /*implements OnChartValueSelectedListener*/ {
    DataRefwiseLeads dataRefwiseLeads;
    ArrayList<DataRefwiseLeads> arrayListCounselorwise;
    AdapterCounselorwiseLeads adapterCounselorwiseLeads;
    RecyclerView recyclerView;
    String url, clientUrl, clientId;
    RequestQueue requestQueue;
    String refid, refname, totalleads, LeadDate, TtlDateLead, totalLeadCount, mainActivityName;
    TextView txtTotalLeadcount, txtRefname, txtRefnameval;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TableLayout tableLayout;
    ImageView imgBack;
    Vibrator vibrator;
    String chartrefNm, chartrefid, chartrefttllead;
    private ProgressDialog progressBar;

    BarChart counsBarChart;
    ArrayList<String> y;
    ArrayList<BarEntry> x;
    ArrayList<String> z;
    MarkerView mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselorwise_leads);

        requestQueue = Volley.newRequestQueue(ActivityCounselorwiseLeads.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        recyclerView = findViewById(R.id.recycler_counselorwisetotallead);
        txtTotalLeadcount = findViewById(R.id.txt_totalleadcount);
        txtRefname = findViewById(R.id.txtRefname);
        txtRefnameval = findViewById(R.id.txtRefnameval);
        tableLayout = findViewById(R.id.tableCounselorwiseLeads);
        imgBack = findViewById(R.id.img_back);

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientUrl = sp.getString("ClientUrl", null);
        clientId = sp.getString("ClientId", null);
        totalLeadCount = sp.getString("TotalLeadsCount", null);
        mainActivityName = sp.getString("ReportActivity", null);

        refid = getIntent().getStringExtra("ReffId");
        refname = getIntent().getStringExtra("ReffName");
        totalleads = getIntent().getStringExtra("fTotalLeads");

        LeadDate = getIntent().getStringExtra("DateLead");
        TtlDateLead = getIntent().getStringExtra("TtlDateLead");

        Log.d("reportsp", mainActivityName);

        txtTotalLeadcount.setText(totalLeadCount);

        editor = sp.edit();
        editor.putString("refid", refid);
        editor.putString("referencename", refname);
        editor.putString("referencetotalleads", totalleads);
        editor.putString("LeadDate", LeadDate);
        editor.putString("TtlDateLead", TtlDateLead);
        editor.commit();

        arrayListCounselorwise = new ArrayList<>();
        adapterCounselorwiseLeads = new AdapterCounselorwiseLeads(arrayListCounselorwise, ActivityCounselorwiseLeads.this);
        LinearLayoutManager manager = new LinearLayoutManager(ActivityCounselorwiseLeads.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterCounselorwiseLeads);

        //==============chart
        chartrefid = getIntent().getStringExtra("chartrefid");
        chartrefNm = getIntent().getStringExtra("chartrefname");
        chartrefttllead = getIntent().getStringExtra("chartreflead");
        Log.d("refnameaaa", chartrefid + " " + chartrefNm + " " + chartrefttllead);

        editor = sp.edit();
        editor.putString("chartrefid", chartrefid);
        editor.putString("chartrefname", chartrefNm);
        editor.putString("chartreflead", chartrefttllead);
        editor.commit();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
                Animatoo.animateSlideRight(ActivityCounselorwiseLeads.this);
            }
        });

        try {
            if (CheckInternetSpeed.checkInternet(ActivityCounselorwiseLeads.this).contains("0")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityCounselorwiseLeads.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(ActivityCounselorwiseLeads.this).contains("1")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityCounselorwiseLeads.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        }).show();
            } else {
                if (mainActivityName.contains("Refwise Report")) {
                    txtRefname.setText(refname);
                    txtRefnameval.setText(totalleads);
                    getRefwiseLeads();
                } else if (mainActivityName.contains("Statuswise Report")) {
                    txtRefname.setText(refname);
                    txtRefnameval.setText(totalleads);
                    getStatuswiseLeads();
                } else if (mainActivityName.contains("Datewise Report")) {
                    txtRefname.setText(LeadDate);
                    txtRefnameval.setText(TtlDateLead);
                    getDatewiseLeads();
                } else if (mainActivityName.contains("Counselorwise Report")) {
                    txtRefname.setText(refname);
                    txtRefnameval.setText(totalleads);
                    getCounselorwiseLeads();
                }
            }
        } catch (Exception e) {
            //progressBar.dismiss();
            e.printStackTrace();
            Toast.makeText(ActivityCounselorwiseLeads.this, "ErrorCode-1000: Login Website Button", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            ActivityCounselorwiseLeads.this.overridePendingTransition(0,
                    R.anim.play_panel_close_background);
            /*Intent intent = new Intent(ActivityCounselorwiseLeads.this, ActivityTotalLeadReport.class);
            intent.putExtra("Activity", "GraphReport");
            startActivity(intent);*/
            finish();
            //Animatoo.animateSlideRight(ActivityCounselorwiseLeads.this);
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
    }

    public void getCounselorwiseLeads() {
        try {
            progressBar = ProgressDialog.show(ActivityCounselorwiseLeads.this, "", "Loading Total Leads", true);

            /*if (refid == null){
                url = clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=3&Statusid=" + chartrefid;
                Log.d("UrlReportNo", url);
                txtRefname.setText(chartrefNm + " => " + chartrefttllead);
            } else*/ {
                url = clientUrl + "?clientid=" + clientId + "&caseid=A8&Step=3&CounselorID=" + refid;
                Log.d("UrlReportNo", url);
            }

            //if (CheckServer.isServerReachable(GraphReport.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // dialog.dismiss();
                                String res = String.valueOf(response);
                               /* if (res.contains("[]")) {
                                    txtNotFound.setVisibility(View.VISIBLE);
                                    linearLayoutAll.setVisibility(View.GONE);
                                } else {
                                    txtNotFound.setVisibility(View.GONE);
                                    linearLayoutAll.setVisibility(View.VISIBLE);

                                }*/
                                tableLayout.setVisibility(View.VISIBLE);
                                arrayListCounselorwise.clear();

                                //==============================Bar chart
                                counsBarChart = (BarChart) findViewById(R.id.ttllead_counsbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                counsBarChart.setTouchEnabled(true);
                                counsBarChart.setDragEnabled(true);
                                counsBarChart.setScaleEnabled(true);
                                counsBarChart.animateY(3000);
                                //counsBarChart.setOnChartValueSelectedListener(ActivityCounselorwiseLeads.this);
                                counsBarChart.setMarkerView(mv);
                                //==================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String refid = jsonObject1.getString("DataRefID");
                                    String refname = jsonObject1.getString("DataRefName");
                                    String totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads = new DataRefwiseLeads(refid, refname, totalleads);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterCounselorwiseLeads.notifyDataSetChanged();

                                    //=======================bar chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(refname);
                                    z.add(refid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    counsBarChart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityCounselorwiseLeads.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityCounselorwiseLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityCounselorwiseLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
            /*} else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GraphReport.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
                        .setMessage("Try after some time!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        *//*edtName.setText("");
                                        edtPassword.setText("");*//*
                                dialog.dismiss();

                            }
                        }).show();
            }*/
        } catch (Exception e) {
            progressBar.dismiss();
            Toast.makeText(ActivityCounselorwiseLeads.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDatewiseLeads() {
        try {
            progressBar = ProgressDialog.show(ActivityCounselorwiseLeads.this, "", "Loading Total Leads", true);

            url = clientUrl + "?clientid=" + clientId + "&caseid=A7&Step=3&SelectedDate=" + LeadDate;
            Log.d("UrlReportNo", url);
            //if (CheckServer.isServerReachable(GraphReport.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // dialog.dismiss();
                                String res = String.valueOf(response);
                               /* if (res.contains("[]")) {
                                    txtNotFound.setVisibility(View.VISIBLE);
                                    linearLayoutAll.setVisibility(View.GONE);
                                } else {
                                    txtNotFound.setVisibility(View.GONE);
                                    linearLayoutAll.setVisibility(View.VISIBLE);

                                }*/
                                tableLayout.setVisibility(View.VISIBLE);
                                arrayListCounselorwise.clear();

                                //==============================Bar chart
                                counsBarChart = (BarChart) findViewById(R.id.ttllead_counsbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                counsBarChart.setTouchEnabled(true);
                                counsBarChart.setDragEnabled(true);
                                counsBarChart.setScaleEnabled(true);
                                counsBarChart.animateY(3000);
                                //counsBarChart.setOnChartValueSelectedListener(ActivityCounselorwiseLeads.this);
                                counsBarChart.setMarkerView(mv);
                                //==================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String refid = jsonObject1.getString("DataRefID");
                                    String refname = jsonObject1.getString("DataRefName");
                                    String totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads = new DataRefwiseLeads(refid, refname, totalleads);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterCounselorwiseLeads.notifyDataSetChanged();

                                    //=======================bar chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(refname);
                                    z.add(refid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    counsBarChart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityCounselorwiseLeads.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityCounselorwiseLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityCounselorwiseLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
            /*} else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GraphReport.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
                        .setMessage("Try after some time!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        *//*edtName.setText("");
                                        edtPassword.setText("");*//*
                                dialog.dismiss();

                            }
                        }).show();
            }*/
        } catch (Exception e) {
            progressBar.dismiss();
            Toast.makeText(ActivityCounselorwiseLeads.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getStatuswiseLeads() {
        try {
            progressBar = ProgressDialog.show(ActivityCounselorwiseLeads.this, "", "Loading Total Leads", true);

            /*if (refid == null){
                url = clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=3&Statusid=" + chartrefid;
                Log.d("UrlReportNo", url);
                txtRefname.setText(chartrefNm + " => " + chartrefttllead);
            } else*/ {
                url = clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=3&Statusid=" + refid;
                Log.d("UrlReportNo", url);
            }

            //if (CheckServer.isServerReachable(GraphReport.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // dialog.dismiss();
                                String res = String.valueOf(response);
                               /* if (res.contains("[]")) {
                                    txtNotFound.setVisibility(View.VISIBLE);
                                    linearLayoutAll.setVisibility(View.GONE);
                                } else {
                                    txtNotFound.setVisibility(View.GONE);
                                    linearLayoutAll.setVisibility(View.VISIBLE);

                                }*/
                                tableLayout.setVisibility(View.VISIBLE);
                                arrayListCounselorwise.clear();

                                //==============================Bar chart
                                counsBarChart = (BarChart) findViewById(R.id.ttllead_counsbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                counsBarChart.setTouchEnabled(true);
                                counsBarChart.setDragEnabled(true);
                                counsBarChart.setScaleEnabled(true);
                                counsBarChart.animateY(3000);
                                //counsBarChart.setOnChartValueSelectedListener(ActivityCounselorwiseLeads.this);
                                counsBarChart.setMarkerView(mv);
                                //==================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String refid = jsonObject1.getString("ccounselorID");
                                    String refname = jsonObject1.getString("ccounselorName");
                                    String totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads = new DataRefwiseLeads(refid, refname, totalleads);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterCounselorwiseLeads.notifyDataSetChanged();

                                    //=======================bar chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(refname);
                                    z.add(refid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    counsBarChart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityCounselorwiseLeads.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityCounselorwiseLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityCounselorwiseLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
            /*} else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GraphReport.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
                        .setMessage("Try after some time!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        *//*edtName.setText("");
                                        edtPassword.setText("");*//*
                                dialog.dismiss();

                            }
                        }).show();
            }*/
        } catch (Exception e) {
            progressBar.dismiss();
            Toast.makeText(ActivityCounselorwiseLeads.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getRefwiseLeads() {
        try {
            progressBar = ProgressDialog.show(ActivityCounselorwiseLeads.this, "", "Loading Total Leads", true);

            /*if (refid == null){
                url = clientUrl + "?clientid=" + clientId + "&caseid=A2&Step=3&RefId=" + chartrefid;
                Log.d("UrlReportNo", url);
                txtRefname.setText(chartrefNm + " => " + chartrefttllead);
            } else*/ {
                url = clientUrl + "?clientid=" + clientId + "&caseid=A2&Step=3&RefId=" + refid;
                Log.d("UrlReportNo", url);
            }

            //if (CheckServer.isServerReachable(GraphReport.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // dialog.dismiss();
                                String res = String.valueOf(response);
                               /* if (res.contains("[]")) {
                                    txtNotFound.setVisibility(View.VISIBLE);
                                    linearLayoutAll.setVisibility(View.GONE);
                                } else {
                                    txtNotFound.setVisibility(View.GONE);
                                    linearLayoutAll.setVisibility(View.VISIBLE);

                                }*/
                                tableLayout.setVisibility(View.VISIBLE);
                                arrayListCounselorwise.clear();

                                //==============================bar chart
                                counsBarChart = (BarChart) findViewById(R.id.ttllead_counsbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                counsBarChart.setTouchEnabled(true);
                                counsBarChart.setDragEnabled(true);
                                counsBarChart.setScaleEnabled(true);
                                counsBarChart.animateY(3000);
                                //counsBarChart.setOnChartValueSelectedListener(ActivityCounselorwiseLeads.this);
                                counsBarChart.setMarkerView(mv);
                                //============================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String refid = jsonObject1.getString("ccounselorID");
                                    String refname = jsonObject1.getString("ccounselorName");
                                    String totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads = new DataRefwiseLeads(refid, refname, totalleads);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterCounselorwiseLeads.notifyDataSetChanged();

                                    //=======================bar chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(refname);
                                    z.add(refid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    counsBarChart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityCounselorwiseLeads.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityCounselorwiseLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityCounselorwiseLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
            /*} else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GraphReport.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
                        .setMessage("Try after some time!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        *//*edtName.setText("");
                                        edtPassword.setText("");*//*
                                dialog.dismiss();

                            }
                        }).show();
            }*/
        } catch (Exception e) {
            //progressBar.dismiss();
            Toast.makeText(ActivityCounselorwiseLeads.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.d("indexqq", String.valueOf(e.getXIndex()));
        int index = e.getXIndex();

        Intent intent = new Intent(ActivityCounselorwiseLeads.this, ActivityStatuswiseLeads.class);
        intent.putExtra("chartcounsid", z.get(index));
        intent.putExtra("chartcounsname", y.get(index));
        intent.putExtra("chartcounslead", arrayListCounselorwise.get(index).getTotalleads());
        Log.d("indexss", z.get(index)+" "+y.get(index)+" "+arrayListCounselorwise.get(index).getTotalleads());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Animatoo.animateSlideLeft(ActivityCounselorwiseLeads.this);
    }

    @Override
    public void onNothingSelected() {

    }*/
}
