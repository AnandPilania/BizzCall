package com.bizcall.wayto.mentebit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityTotalLeadReport extends AppCompatActivity /*implements OnChartValueSelectedListener*/ {
    private ProgressDialog progressBar;
    RequestQueue requestQueue;
    TextView txtTotalLeadReport, txtCondition;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String clientUrl, clientId, strTotalleadno, url, strActivityName, strTotalRecordsUrl;
    LinearLayout layoutRefwisetotallead;
    ImageView imgBack;
    Vibrator vibrator;
    AdapterDatewiseLeads adapterDatewiseLeads;
    AdapterRefwiseLeads adapterRefwiseLeads;
    DataRefwiseLeads dataRefwiseLeads;
    DatewiseLeads datewiseLeads;
    ArrayList<DatewiseLeads> arrayListDatewise;
    ArrayList<DataRefwiseLeads> arrayListRefwise;
    RecyclerView recyclerRefwise;
    TableLayout tableRefwiseleads, tableDatewiseleads;
    String refname, totalleads, refid;

    BarChart ttllead_barchart;
    ArrayList<BarEntry> x;
    ArrayList<String> y;
    ArrayList<String> z;
    MarkerView mv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalleadreport);

        txtTotalLeadReport = findViewById(R.id.txt_totalleadcount);
        txtCondition = findViewById(R.id.txt_tabcondition);
        layoutRefwisetotallead = findViewById(R.id.layout_refwisetotalleads);
        imgBack = findViewById(R.id.img_back);
        recyclerRefwise = findViewById(R.id.recycler_refwisetotallead);
        tableRefwiseleads = findViewById(R.id.tableRefwiseLeads);
        tableDatewiseleads = findViewById(R.id.tableDatewiseLeads);

        requestQueue = Volley.newRequestQueue(ActivityTotalLeadReport.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        clientUrl = sp.getString("ClientUrl", null);
        clientId = sp.getString("ClientId", null);

        strActivityName = getIntent().getStringExtra("ActivityName");
        editor.putString("ReportActivity", strActivityName);
        editor.commit();
        Log.d("activityname", strActivityName);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
            }
        });

        try {
            if (CheckInternetSpeed.checkInternet(ActivityTotalLeadReport.this).contains("0")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityTotalLeadReport.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBar.dismiss();
                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(ActivityTotalLeadReport.this).contains("1")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityTotalLeadReport.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressBar.dismiss();
                            }
                        }).show();
            } else {
                progressBar = ProgressDialog.show(ActivityTotalLeadReport.this, "", "Loading Total Leads", true);

                if (strActivityName.contains("Refwise Report")) {
                    strTotalRecordsUrl = clientUrl + "?clientid=" + clientId + "&caseid=A2&Step=1";
                    Log.d("strrefwiseUrl", strTotalRecordsUrl);
                } else if (strActivityName.contains("Statuswise Report")) {
                    strTotalRecordsUrl = clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=1";
                    Log.d("strstatuswiseUrl", strTotalRecordsUrl);
                } else if (strActivityName.contains("Datewise Report")) {
                    strTotalRecordsUrl = clientUrl + "?clientid=" + clientId + "&caseid=A7&Step=1";
                    Log.d("strdatewiseUrl", strTotalRecordsUrl);
                } else if (strActivityName.contains("Counselorwise Report")) {
                    strTotalRecordsUrl = clientUrl + "?clientid=" + clientId + "&caseid=A8&Step=1";
                    Log.d("strdatewiseUrl", strTotalRecordsUrl);
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strTotalRecordsUrl,
                        null, new success(), new fail());

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);

                txtTotalLeadReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtCondition.setVisibility(View.GONE);
                        if (strActivityName.contains("Refwise Report")) {
                            getRefwiseLeads();
                        } else if (strActivityName.contains("Statuswise Report")) {
                            getStatuswiseLeads();
                        } else if (strActivityName.contains("Datewise Report")) {
                            getDatewiseLeads();
                        } else if (strActivityName.contains("Counselorwise Report")) {
                            getCounselorwiseLeads();
                        }
                    }
                });
            }
        } catch (Exception e) {
            progressBar.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(ActivityTotalLeadReport.this, ActivityHome.class);
            intent.putExtra("Activity", "GraphReport");
            startActivity(intent);
            finish();
            Animatoo.animateSlideRight(ActivityTotalLeadReport.this);
            //super.onBackPressed();
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
    }

    public void getCounselorwiseLeads() {
        try {
            progressBar = ProgressDialog.show(ActivityTotalLeadReport.this, "", "Loading Referencewise Report", true);

            url = clientUrl + "?clientid=" + clientId + "&caseid=A8&Step=2";
            Log.d("UrlReportNo", url);
            //if (CheckServer.isServerReachable(GraphReport.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                arrayListRefwise = new ArrayList<>();
                                adapterRefwiseLeads = new AdapterRefwiseLeads(arrayListRefwise, ActivityTotalLeadReport.this);
                                LinearLayoutManager manager = new LinearLayoutManager(ActivityTotalLeadReport.this);
                                manager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerRefwise.setLayoutManager(manager);
                                recyclerRefwise.setAdapter(adapterRefwiseLeads);

                                layoutRefwisetotallead.setVisibility(View.VISIBLE);
                                tableRefwiseleads.setVisibility(View.VISIBLE);
                                arrayListRefwise.clear();

                                //================================Chart
                                x = new ArrayList<BarEntry>();
                                y = new ArrayList<String>();
                                z = new ArrayList<String>();

                                ttllead_barchart = findViewById(R.id.ttllead_barchart);

                                ttllead_barchart.setTouchEnabled(true);
                                ttllead_barchart.setDragEnabled(true);
                                ttllead_barchart.setScaleEnabled(true);
                                ttllead_barchart.setClickable(false);
                                //ttllead_barchart.setOnChartValueSelectedListener(ActivityTotalLeadReport.this);
                                ttllead_barchart.setMarkerView(mv);
                                //===================================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    refid = jsonObject1.getString("ccounselorID");
                                    refname = jsonObject1.getString("ccounselorName");
                                    totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads = new DataRefwiseLeads(refid, refname, totalleads);
                                    arrayListRefwise.add(dataRefwiseLeads);
                                    adapterRefwiseLeads.notifyDataSetChanged();

                                    //=========================chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(refname);
                                    z.add(refid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);
                                    ttllead_barchart.animateY(3000);
                                    BarData data = new BarData(y, set1);
                                    ttllead_barchart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityTotalLeadReport.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityTotalLeadReport.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityTotalLeadReport.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityTotalLeadReport.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDatewiseLeads() {
        try {
            progressBar = ProgressDialog.show(ActivityTotalLeadReport.this, "", "Loading Referencewise Report", true);

            url = clientUrl + "?clientid=" + clientId + "&caseid=A7&Step=2";
            Log.d("UrlReportNo", url);
            //if (CheckServer.isServerReachable(GraphReport.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                arrayListDatewise = new ArrayList<>();
                                adapterDatewiseLeads = new AdapterDatewiseLeads(arrayListDatewise, getApplicationContext());
                                LinearLayoutManager manager1 = new LinearLayoutManager(ActivityTotalLeadReport.this);
                                manager1.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerRefwise.setLayoutManager(manager1);
                                recyclerRefwise.setAdapter(adapterDatewiseLeads);

                                layoutRefwisetotallead.setVisibility(View.VISIBLE);
                                tableDatewiseleads.setVisibility(View.VISIBLE);
                                arrayListDatewise.clear();

                                //================================Chart
                                x = new ArrayList<BarEntry>();
                                y = new ArrayList<String>();
                                z = new ArrayList<String>();

                                ttllead_barchart = findViewById(R.id.ttllead_barchart);

                                ttllead_barchart.setTouchEnabled(true);
                                ttllead_barchart.setDragEnabled(true);
                                ttllead_barchart.setScaleEnabled(true);
                                //ttllead_barchart.setOnChartValueSelectedListener(ActivityTotalLeadReport.this);
                                ttllead_barchart.setMarkerView(mv);
                                //===================================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String refid = jsonObject1.getString("Date");
                                    String totalleads = jsonObject1.getString("total lead");

                                    datewiseLeads = new DatewiseLeads(refid, totalleads);
                                    arrayListDatewise.add(datewiseLeads);
                                    adapterDatewiseLeads.notifyDataSetChanged();

                                    //=========================chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(refid);
                                    //z.add(refid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);
                                    ttllead_barchart.animateY(3000);
                                    BarData data = new BarData(y, set1);
                                    ttllead_barchart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityTotalLeadReport.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityTotalLeadReport.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityTotalLeadReport.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityTotalLeadReport.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getRefwiseLeads() {
        try {
            progressBar = ProgressDialog.show(ActivityTotalLeadReport.this, "", "Loading Referencewise Report", true);

            url = clientUrl + "?clientid=" + clientId + "&caseid=A2&Step=2";
            Log.d("UrlReportNo", url);
            //if (CheckServer.isServerReachable(GraphReport.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                arrayListRefwise = new ArrayList<>();
                                adapterRefwiseLeads = new AdapterRefwiseLeads(arrayListRefwise, getApplicationContext());
                                LinearLayoutManager manager = new LinearLayoutManager(ActivityTotalLeadReport.this);
                                manager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerRefwise.setLayoutManager(manager);
                                recyclerRefwise.setAdapter(adapterRefwiseLeads);

                                layoutRefwisetotallead.setVisibility(View.VISIBLE);
                                tableRefwiseleads.setVisibility(View.VISIBLE);
                                arrayListRefwise.clear();

                                //================================Chart
                                x = new ArrayList<BarEntry>();
                                y = new ArrayList<String>();
                                z = new ArrayList<String>();

                                ttllead_barchart = findViewById(R.id.ttllead_barchart);

                                ttllead_barchart.setTouchEnabled(true);
                                ttllead_barchart.setDragEnabled(true);
                                ttllead_barchart.setScaleEnabled(true);
                                ttllead_barchart.setClickable(false);
                                //ttllead_barchart.setOnChartValueSelectedListener(ActivityTotalLeadReport.this);
                                ttllead_barchart.setMarkerView(mv);
                                //===================================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    refid = jsonObject1.getString("DataRefID");
                                    refname = jsonObject1.getString("DataRefName");
                                    totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads = new DataRefwiseLeads(refid, refname, totalleads);
                                    arrayListRefwise.add(dataRefwiseLeads);
                                    adapterRefwiseLeads.notifyDataSetChanged();

                                    //=========================chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(refname);
                                    z.add(refid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);
                                    ttllead_barchart.animateY(3000);
                                    BarData data = new BarData(y, set1);
                                    ttllead_barchart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityTotalLeadReport.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityTotalLeadReport.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityTotalLeadReport.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityTotalLeadReport.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getStatuswiseLeads() {
        try {
            progressBar = ProgressDialog.show(ActivityTotalLeadReport.this, "", "Loading Statuswise Report", true);

            url = clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=2";
            Log.d("UrlReportNo", url);
            //if (CheckServer.isServerReachable(GraphReport.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                arrayListRefwise = new ArrayList<>();
                                adapterRefwiseLeads = new AdapterRefwiseLeads(arrayListRefwise, getApplicationContext());
                                LinearLayoutManager manager = new LinearLayoutManager(ActivityTotalLeadReport.this);
                                manager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerRefwise.setLayoutManager(manager);
                                recyclerRefwise.setAdapter(adapterRefwiseLeads);

                                layoutRefwisetotallead.setVisibility(View.VISIBLE);
                                tableRefwiseleads.setVisibility(View.VISIBLE);
                                arrayListRefwise.clear();

                                //================================Chart
                                x = new ArrayList<BarEntry>();
                                y = new ArrayList<String>();
                                z = new ArrayList<String>();

                                ttllead_barchart = findViewById(R.id.ttllead_barchart);

                                ttllead_barchart.setTouchEnabled(true);
                                ttllead_barchart.setDragEnabled(true);
                                ttllead_barchart.setScaleEnabled(true);
                                //ttllead_barchart.setOnChartValueSelectedListener(ActivityTotalLeadReport.this);
                                ttllead_barchart.setMarkerView(mv);
                                //===================================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String statusid = jsonObject1.getString("nStatusID");
                                    String statusname = jsonObject1.getString("cstatus");
                                    String totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads = new DataRefwiseLeads(statusid, statusname, totalleads);
                                    arrayListRefwise.add(dataRefwiseLeads);
                                    adapterRefwiseLeads.notifyDataSetChanged();

                                    //=========================chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(statusname);
                                    z.add(statusid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);
                                    ttllead_barchart.animateY(3000);
                                    BarData data = new BarData(y, set1);
                                    ttllead_barchart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityTotalLeadReport.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityTotalLeadReport.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityTotalLeadReport.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityTotalLeadReport.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.d("indexqq", String.valueOf(e.getXIndex()));
        int index = e.getXIndex();

        if (strActivityName.contains("Refwise Report") || strActivityName.contains("Statuswise Report")) {
            Intent intent = new Intent(ActivityTotalLeadReport.this, ActivityCounselorwiseLeads.class);
            intent.putExtra("chartrefid", z.get(index));
            intent.putExtra("chartrefname", y.get(index));
            intent.putExtra("chartreflead", arrayListRefwise.get(index).getTotalleads());
            Log.d("indexss", z.get(index) + " " + y.get(index) + " " + arrayListRefwise.get(index).getTotalleads());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (strActivityName.contains("Datewise Report")) {
            Intent intent = new Intent(ActivityTotalLeadReport.this, ActivityCounselorwiseLeads.class);
            intent.putExtra("chartrefname", y.get(index));
            intent.putExtra("chartreflead", arrayListRefwise.get(index).getTotalleads());
            Log.d("indexss",  y.get(index) + " " + arrayListRefwise.get(index).getTotalleads());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        Animatoo.animateSlideLeft(ActivityTotalLeadReport.this);
    }

    @Override
    public void onNothingSelected() {

    }*/

    private class success implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            Log.e("Success", String.valueOf(response));

            try {
                JSONArray jsonArray = response.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strTotalleadno = jsonObject.getString("total leads");
                    txtTotalLeadReport.setText(strTotalleadno);

                    /*Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(300); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    txtTotalLeadReport.startAnimation(anim);*/

                    editor = sp.edit();
                    editor.putString("TotalLeadsCount", strTotalleadno);
                    editor.commit();
                }
                progressBar.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                progressBar.dismiss();
                Toast.makeText(ActivityTotalLeadReport.this, "ErrorCode-1007:Login Success", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class fail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressBar.dismiss();
            Log.d("fail", "fail to load total leads");
        }
    }
}
