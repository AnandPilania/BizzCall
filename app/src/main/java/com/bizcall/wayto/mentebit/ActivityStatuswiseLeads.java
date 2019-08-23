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

public class ActivityStatuswiseLeads extends AppCompatActivity /*implements OnChartValueSelectedListener*/ {
    DataRefwiseLeads dataRefwiseLeads;
    ArrayList<DataRefwiseLeads> arrayListCounselorwise;
    AdapterStatuswiseLeads adapterStatuswiseLeads;
    RecyclerView recyclerView;
    String url,clientUrl,clientId,mainActivityName,leadDate, TtlLeadsDate;
    RequestQueue requestQueue;
    String counsname,counsleads,counsid,totalLeadCount,RefId, RefName, RefLeads, refNm;
    TextView txtTotalLeadcount,txtRefname,txtRefnameval,txtCounsName,txtCounsNameval;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TableLayout tableLayout;
    ImageView imgBack;
    Vibrator vibrator;
    private ProgressDialog progressBar;

    String chartcounsNm, chartcounsid, chartcounsttllead;
    BarChart statusBarChart;
    ArrayList<BarEntry> x;
    ArrayList<String> y;
    ArrayList<String> z;
    MarkerView mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statuswise_leads);

        requestQueue= Volley.newRequestQueue(ActivityStatuswiseLeads.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        recyclerView=findViewById(R.id.recycler_statuswisetotallead);
        txtTotalLeadcount=findViewById(R.id.txt_totalleadcount);
        txtRefname=findViewById(R.id.txtRefname);
        txtRefnameval=findViewById(R.id.txtRefnameval);
        txtCounsName=findViewById(R.id.txtCounsname);
        txtCounsNameval=findViewById(R.id.txtCounsnameval);
        tableLayout=findViewById(R.id.tableCounselorwiseLeads);
        imgBack = findViewById(R.id.img_back);

        sp=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientUrl = sp.getString("ClientUrl", null);
        clientId = sp.getString("ClientId", null);
        totalLeadCount=sp.getString("TotalLeadsCount",null);
        mainActivityName = sp.getString("ReportActivity", null);
        leadDate = sp.getString("LeadDate", null);
        TtlLeadsDate = sp.getString("TtlDateLead", null);

        //Log.d("TtlLeadsDate",TtlLeadsDate);

        RefId=sp.getString("refid",null);
        RefName=sp.getString("referencename",null);
        RefLeads=sp.getString("referencetotalleads",null);

        counsid=getIntent().getStringExtra("CounId");
        counsname=getIntent().getStringExtra("CounName");
        counsleads=getIntent().getStringExtra("TotalLeads");

        editor = sp.edit();
        editor.putString("counid", counsid);
        editor.putString("counname", counsname);
        editor.putString("countotalleads", counsleads);
        editor.commit();

        txtTotalLeadcount.setText(totalLeadCount);
        txtCounsName.setText(counsname);
        txtCounsNameval.setText(counsleads);

        //==============chart
        chartcounsid = getIntent().getStringExtra("chartcounsid");
        chartcounsNm = getIntent().getStringExtra("chartcounsname");
        chartcounsttllead = getIntent().getStringExtra("chartcounslead");
        Log.d("refnameaaa", chartcounsid + " " + chartcounsNm + " " + chartcounsttllead);

        editor = sp.edit();
        editor.putString("chartcounsid", chartcounsid);
        editor.putString("chartcounsname", chartcounsNm);
        editor.putString("chartcounslead", chartcounsttllead);
        editor.commit();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
                Animatoo.animateSlideRight(ActivityStatuswiseLeads.this);
            }
        });

        try {
            if (CheckInternetSpeed.checkInternet(ActivityStatuswiseLeads.this).contains("0")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeads.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(ActivityStatuswiseLeads.this).contains("1")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeads.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        }).show();
            } else {
                try {
                    if (mainActivityName.contains("Refwise Report")){
                        txtRefname.setText(RefName);
                        txtRefnameval.setText(RefLeads);
                        getStatuswiseLeads();
                    } else if (mainActivityName.contains("Statuswise Report")){
                        txtRefname.setText(RefName);
                        txtRefnameval.setText(RefLeads);
                        getDataRefwiseLeads();
                    } else if (mainActivityName.contains("Datewise Report")){
                        txtRefname.setText(leadDate);
                        txtRefnameval.setText(TtlLeadsDate);
                        getDatewiseLeads();
                    } else if (mainActivityName.contains("Counselorwise Report")) {
                        txtRefname.setText(RefName);
                        txtRefnameval.setText(RefLeads);
                        getCounselorwiseLeads();
                    }

                }catch (Exception e){
                    progressBar.dismiss();
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            progressBar.dismiss();
            e.printStackTrace();
            Toast.makeText(ActivityStatuswiseLeads.this, "ErrorCode-1000: Login Website Button", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            ActivityStatuswiseLeads.this.overridePendingTransition(0,
                    R.anim.play_panel_close_background);
            /*Intent intent = new Intent(ActivityStatuswiseLeads.this, ActivityCounselorwiseLeads.class);
            intent.putExtra("Activity", "GraphReport");
            startActivity(intent);*/
            finish();
            //Animatoo.animateSlideRight(ActivityStatuswiseLeads.this);
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
    }

    public void getCounselorwiseLeads(){
        try {
            progressBar = ProgressDialog.show(ActivityStatuswiseLeads.this, "", "Loading Total Leads", true);

            /*if (RefId == null){
                url = clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=4&Statusid="+sp.getString("chartrefid",null)+"&CounselorID="+chartcounsid;
                Log.d("UrlReportNo", url);
                txtRefname.setText(sp.getString("chartrefname",null) + " => " + sp.getString("chartreflead",null));
                txtCounsName.setText(sp.getString("chartcounsname",null) + " => " + sp.getString("chartcounslead",null));
            } else*/ {
                url = clientUrl + "?clientid=" + clientId + "&caseid=A8&Step=4&CounselorID="+RefId+"&cdatafrom="+counsid;
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
                                arrayListCounselorwise = new ArrayList<>();
                                adapterStatuswiseLeads = new AdapterStatuswiseLeads(arrayListCounselorwise, getApplicationContext());
                                LinearLayoutManager manager = new LinearLayoutManager(ActivityStatuswiseLeads.this);
                                manager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(manager);
                                recyclerView.setAdapter(adapterStatuswiseLeads);

                                tableLayout.setVisibility(View.VISIBLE);
                                arrayListCounselorwise.clear();

                                //==============================Bar chart
                                statusBarChart = (BarChart) findViewById(R.id.ttllead_statusbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                statusBarChart.setTouchEnabled(true);
                                statusBarChart.setDragEnabled(true);
                                statusBarChart.setScaleEnabled(true);
                                statusBarChart.animateY(3000);
                                //statusBarChart.setOnChartValueSelectedListener(ActivityStatuswiseLeads.this);
                                statusBarChart.setMarkerView(mv);
                                //==================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String statusid = jsonObject1.getString("nStatusID");
                                    String statusname=jsonObject1.getString("cstatus");
                                    String totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads  = new DataRefwiseLeads(statusid, statusname, totalleads);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterStatuswiseLeads.notifyDataSetChanged();

                                    //=======================bar chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(statusname);
                                    z.add(statusid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    statusBarChart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityStatuswiseLeads.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityStatuswiseLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityStatuswiseLeads.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDatewiseLeads(){
        try {
            progressBar = ProgressDialog.show(ActivityStatuswiseLeads.this, "", "Loading Total Leads", true);

            url=clientUrl + "?clientid=" + clientId + "&caseid=A7&Step=4&SelectedDate="+leadDate+"&cdatafrom="+counsid;
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
                                arrayListCounselorwise = new ArrayList<>();
                                adapterStatuswiseLeads = new AdapterStatuswiseLeads(arrayListCounselorwise, getApplicationContext());
                                LinearLayoutManager manager = new LinearLayoutManager(ActivityStatuswiseLeads.this);
                                manager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(manager);
                                recyclerView.setAdapter(adapterStatuswiseLeads);

                                tableLayout.setVisibility(View.VISIBLE);
                                arrayListCounselorwise.clear();

                                //==============================Bar chart
                                statusBarChart = (BarChart) findViewById(R.id.ttllead_statusbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                statusBarChart.setTouchEnabled(true);
                                statusBarChart.setDragEnabled(true);
                                statusBarChart.setScaleEnabled(true);
                                statusBarChart.animateY(3000);
                                //statusBarChart.setOnChartValueSelectedListener(ActivityStatuswiseLeads.this);
                                statusBarChart.setMarkerView(mv);
                                //==================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String statusid = jsonObject1.getString("ccounselorID");
                                    String statusname=jsonObject1.getString("ccounselorName");
                                    String totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads  = new DataRefwiseLeads(statusid, statusname, totalleads);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterStatuswiseLeads.notifyDataSetChanged();

                                    //=======================bar chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(statusname);
                                    z.add(statusid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    statusBarChart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityStatuswiseLeads.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityStatuswiseLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityStatuswiseLeads.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataRefwiseLeads(){
        try {
            progressBar = ProgressDialog.show(ActivityStatuswiseLeads.this, "", "Loading Total Leads", true);

            /*if (RefId == null){
                url = clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=4&Statusid="+sp.getString("chartrefid",null)+"&CounselorID="+chartcounsid;
                Log.d("UrlReportNo", url);
                txtRefname.setText(sp.getString("chartrefname",null) + " => " + sp.getString("chartreflead",null));
                txtCounsName.setText(sp.getString("chartcounsname",null) + " => " + sp.getString("chartcounslead",null));
            } else*/ {
                url = clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=4&Statusid="+RefId+"&CounselorID="+counsid;
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
                                arrayListCounselorwise = new ArrayList<>();
                                adapterStatuswiseLeads = new AdapterStatuswiseLeads(arrayListCounselorwise, getApplicationContext());
                                LinearLayoutManager manager = new LinearLayoutManager(ActivityStatuswiseLeads.this);
                                manager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(manager);
                                recyclerView.setAdapter(adapterStatuswiseLeads);

                                tableLayout.setVisibility(View.VISIBLE);
                                arrayListCounselorwise.clear();

                                //==============================Bar chart
                                statusBarChart = (BarChart) findViewById(R.id.ttllead_statusbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                statusBarChart.setTouchEnabled(true);
                                statusBarChart.setDragEnabled(true);
                                statusBarChart.setScaleEnabled(true);
                                statusBarChart.animateY(3000);
                                //statusBarChart.setOnChartValueSelectedListener(ActivityStatuswiseLeads.this);
                                statusBarChart.setMarkerView(mv);
                                //==================================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String statusid = jsonObject1.getString("DataRefID");
                                    String statusname=jsonObject1.getString("DataRefName");
                                    String totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads  = new DataRefwiseLeads(statusid, statusname, totalleads);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterStatuswiseLeads.notifyDataSetChanged();

                                    //=======================bar chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(statusname);
                                    z.add(statusid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    statusBarChart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityStatuswiseLeads.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityStatuswiseLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityStatuswiseLeads.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getStatuswiseLeads() {
        try {
            progressBar = ProgressDialog.show(ActivityStatuswiseLeads.this, "", "Loading Total Leads", true);

            /*if (RefId == null){
                url = clientUrl + "?clientid=" + clientId + "&caseid=A2&Step=4&RefId="+sp.getString("chartrefid",null)+"&CounselorID="+chartcounsid;
                Log.d("UrlReportNo", url);
                txtRefname.setText(sp.getString("chartrefname",null) + " => " + sp.getString("chartreflead",null));
                txtCounsName.setText(sp.getString("chartcounsname",null) + " => " + sp.getString("chartcounslead",null));
            } else*/ {
                url=clientUrl + "?clientid=" + clientId + "&caseid=A2&Step=4&RefId="+RefId+"&CounselorID="+counsid;
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
                                arrayListCounselorwise = new ArrayList<>();
                                adapterStatuswiseLeads = new AdapterStatuswiseLeads(arrayListCounselorwise, getApplicationContext());
                                LinearLayoutManager manager = new LinearLayoutManager(ActivityStatuswiseLeads.this);
                                manager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(manager);
                                recyclerView.setAdapter(adapterStatuswiseLeads);

                                tableLayout.setVisibility(View.VISIBLE);
                                arrayListCounselorwise.clear();
                                //==============================bar chart
                                statusBarChart = (BarChart) findViewById(R.id.ttllead_statusbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                statusBarChart.setTouchEnabled(true);
                                statusBarChart.setDragEnabled(true);
                                statusBarChart.setScaleEnabled(true);
                                statusBarChart.animateY(3000);
                                //statusBarChart.setOnChartValueSelectedListener(ActivityStatuswiseLeads.this);
                                statusBarChart.setMarkerView(mv);
                                //============================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String statusid = jsonObject1.getString("nStatusID");
                                    String statusname=jsonObject1.getString("cstatus");
                                    String totalleads = jsonObject1.getString("total leads");
                                    dataRefwiseLeads  = new DataRefwiseLeads(statusid, statusname, totalleads);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterStatuswiseLeads.notifyDataSetChanged();

                                    //=======================bar chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(statusname);
                                    z.add(statusid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    statusBarChart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityStatuswiseLeads.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityStatuswiseLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityStatuswiseLeads.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.d("indexqq", String.valueOf(e.getXIndex()));
        int index = e.getXIndex();

        Intent intent = new Intent(ActivityStatuswiseLeads.this, ActivityStatuswiseLeadDetailRecords.class);
        intent.putExtra("chartstatussid", z.get(index));
        intent.putExtra("chartstatusname", y.get(index));
        intent.putExtra("chartstatuslead", arrayListCounselorwise.get(index).getTotalleads());
        Log.d("indexss", z.get(index)+" "+y.get(index)+" "+arrayListCounselorwise.get(index).getTotalleads());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Animatoo.animateSlideLeft(ActivityStatuswiseLeads.this);
    }

    @Override
    public void onNothingSelected() {

    }*/
}
