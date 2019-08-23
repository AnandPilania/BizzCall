package com.bizcall.wayto.mentebit;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityStatuswiseLeadDetailRecords extends AppCompatActivity /*implements OnChartValueSelectedListener*/ {
    DetailStatuswiseLeadReportData dataRefwiseLeads;
    ArrayList<DetailStatuswiseLeadReportData> arrayListCounselorwise;
    AdapterStatuswiseDetailLeadReport adapterStatuswiseDetailLeadReport;
    RecyclerView recyclerView;
    String url,clientUrl,clientId,mainActivityName,leadDate,TtlLeadsDate, dateaa, datebb;
    RequestQueue requestQueue;
    String statusname,statusleads,dateid,datename,dtttlleads,statusid,totalLeadCount, refNm, RefId, RefName, RefLeads,Counid,Counname,Countotalleads;
    TextView txtTotalLeadval,txtRefname,txtRefnameval, txtCounsName,txtCounsNameval, txtStatusName,txtStatusNameval, txtDatewise,txtDatewiseval;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TableLayout tableLayout;
    ImageView imgBack;
    Vibrator vibrator;
    LinearLayout layoutDatewise, layoutChartgone;
    private ProgressDialog progressBar;

    /*String chartstatusNm, chartstatusid, chartstatusttllead;
    BarChart statusdetailBarChart;
    ArrayList<BarEntry> x;
    ArrayList<String> y;
    ArrayList<String> z;
    MarkerView mv;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statuswise_lead_detail_records);

        Intent intent=getIntent();
        refNm=intent.getStringExtra("chartstatusname");
        //Log.d("statusnameaaa",refNm);

        requestQueue= Volley.newRequestQueue(ActivityStatuswiseLeadDetailRecords.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        recyclerView=findViewById(R.id.recycler_detailstatuswisetotallead);
        txtTotalLeadval=findViewById(R.id.txt_totalleadcount);
        txtRefname=findViewById(R.id.txtRefname);
        txtRefnameval=findViewById(R.id.txtRefnameval);
        txtCounsName=findViewById(R.id.txtCounsname);
        txtCounsNameval=findViewById(R.id.txtCounsnameval);
        txtStatusName=findViewById(R.id.txtStatusname);
        txtStatusNameval=findViewById(R.id.txtStatusnameval);
        tableLayout=findViewById(R.id.tableCounselorwiseLeads);
        imgBack = findViewById(R.id.img_back);
        txtDatewise = findViewById(R.id.txtdatewisename);
        txtDatewiseval = findViewById(R.id.txtdatewisenameval);
        layoutDatewise = findViewById(R.id.layoutDatewise);
       // layoutChartgone = findViewById(R.id.layout_chartgone);

        sp=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientUrl = sp.getString("ClientUrl", null);
        clientId = sp.getString("ClientId", null);
        totalLeadCount=sp.getString("TotalLeadsCount",null);
        RefId=sp.getString("refid",null);
        RefName=sp.getString("referencename",null);
        RefLeads=sp.getString("referencetotalleads",null);
        Counid = sp.getString("counid",null);
        Counname = sp.getString("counname",null);
        Countotalleads = sp.getString("countotalleads",null);
        mainActivityName = sp.getString("ReportActivity", null);
        leadDate = sp.getString("LeadDate", null);
        TtlLeadsDate = sp.getString("TtlDateLead", null);
        dateaa = sp.getString("datecounsname", null);
        datebb = sp.getString("datecounsttlleads", null);

        statusid=getIntent().getStringExtra("StatusId");
        statusname=getIntent().getStringExtra("StatusName");
        statusleads=getIntent().getStringExtra("TotalLeads");
        Log.d("asdf", ""+statusleads+""+Countotalleads+" "+RefLeads);

        dateid=getIntent().getStringExtra("dateId");
        datename=getIntent().getStringExtra("dateName");
        dtttlleads=getIntent().getStringExtra("dttotalLeads");

        arrayListCounselorwise = new ArrayList<>();
        adapterStatuswiseDetailLeadReport = new AdapterStatuswiseDetailLeadReport(arrayListCounselorwise, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(ActivityStatuswiseLeadDetailRecords.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterStatuswiseDetailLeadReport);

        txtTotalLeadval.setText(totalLeadCount);
        txtRefname.setText(RefName);
        txtRefnameval.setText(RefLeads);
        txtCounsName.setText(Counname);
        txtCounsNameval.setText(Countotalleads);
        txtStatusName.setText(statusname);
        txtStatusNameval.setText(statusleads);

        //==============chart
        /*chartstatusid = getIntent().getStringExtra("chartstatussid");
        chartstatusNm = getIntent().getStringExtra("chartstatusname");
        chartstatusttllead = getIntent().getStringExtra("chartstatuslead");
        Log.d("refnameaaa", chartstatusid + " " + chartstatusNm + " " + chartstatusttllead);

        editor = sp.edit();
        editor.putString("chartstatusid", chartstatusid);
        editor.putString("chartstatusNm", chartstatusNm);
        editor.putString("chartstatusttllead", chartstatusttllead);
        editor.commit();*/

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
                Animatoo.animateSlideRight(ActivityStatuswiseLeadDetailRecords.this);
            }
        });

        try {
            if (CheckInternetSpeed.checkInternet(ActivityStatuswiseLeadDetailRecords.this).contains("0")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeadDetailRecords.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(ActivityStatuswiseLeadDetailRecords.this).contains("1")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeadDetailRecords.this);
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
                        //layoutChartgone.setVisibility(View.GONE);
                        getStatuswiseLeadDetails();
                    } else if (mainActivityName.contains("Statuswise Report")){
                        //layoutChartgone.setVisibility(View.GONE);
                        getDataRefwiseLeads();
                    } else if (mainActivityName.contains("Datewise Report")){
                        txtRefname.setText(leadDate);
                        txtRefnameval.setText(TtlLeadsDate);
                        txtStatusName.setText(dateaa);
                        txtStatusNameval.setText(datebb);
                        layoutDatewise.setVisibility(View.VISIBLE);
                        //layoutChartgone.setVisibility(View.GONE);
                        txtDatewise.setText(datename);
                        txtDatewiseval.setText(dtttlleads);
                        getDatewiseLeads();
                    } else if (mainActivityName.contains("Counselorwise Report")) {
                        //layoutChartgone.setVisibility(View.GONE);
                        getCounselorwiseLeads();
                    }
                }catch (Exception e){
                    //progressBar.dismiss();
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            //progressBar.dismiss();
            e.printStackTrace();
            Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "ErrorCode-1000: Login Website Button", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            ActivityStatuswiseLeadDetailRecords.this.overridePendingTransition(0,
                    R.anim.play_panel_close_background);
            /*Intent intent = new Intent(ActivityStatuswiseLeadDetailRecords.this, ActivityStatuswiseLeads.class);
            intent.putExtra("Activity", "GraphReport");
            startActivity(intent);*/
            finish();
            //Animatoo.animateSlideRight(ActivityStatuswiseLeadDetailRecords.this);
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
    }

    public void getCounselorwiseLeads(){
        try {
            progressBar = ProgressDialog.show(ActivityStatuswiseLeadDetailRecords.this, "", "Loading Total Leads", true);

            /*if (RefId == null){
                url=clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=5&Statusid="+sp.getString("chartrefid",null)+"&CounselorID="+sp.getString("chartcounsid",null)+"&DataRefID="+chartstatusid;
                Log.d("UrlReportNo", url);
                txtRefname.setText(sp.getString("chartrefname",null) + " => " + sp.getString("chartreflead",null));
                txtCounsName.setText(sp.getString("chartcounsname",null) + " => " + sp.getString("chartcounslead",null));
                txtStatusName.setText(chartstatusNm+" => "+chartstatusttllead);
            } else*/ {
                url=clientUrl + "?clientid=" + clientId + "&caseid=A8&Step=5&CounselorID="+RefId+"&cdatafrom="+Counid+"&Statusid="+statusid;
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
                                /*statusdetailBarChart = (BarChart) findViewById(R.id.ttllead_statusdetailbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                statusdetailBarChart.setTouchEnabled(true);
                                statusdetailBarChart.setDragEnabled(true);
                                statusdetailBarChart.setScaleEnabled(true);
                                statusdetailBarChart.animateY(3000);
                                //statusdetailBarChart.setOnChartValueSelectedListener(ActivityStatuswiseLeadDetailRecords.this);
                                statusdetailBarChart.setMarkerView(mv);*/
                                //============================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String mSrNo = jsonObject1.getString("nSrNo");
                                    String mRefNo = jsonObject1.getString("refNo");
                                    String mCandName=jsonObject1.getString("cCandidateName");
                                    String mCourse = jsonObject1.getString("cCourse");
                                    String mMobile = jsonObject1.getString("cMobile");
                                    String mAddress = jsonObject1.getString("cAddressLine");
                                    String mCity = jsonObject1.getString("cCity");
                                    String mState = jsonObject1.getString("cState");
                                    String mPinCode = jsonObject1.getString("cPinCode");
                                    String mParentNo = jsonObject1.getString("cParantNo");
                                    String mEmail = jsonObject1.getString("cEmail");
                                    String mDataFrom = jsonObject1.getString("cDataFrom");
                                    String mAllocatedTo = jsonObject1.getString("AllocatedTo");

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("AllocationDate");
                                    String mAllocatedDate = jsonObject2.getString("date").substring(0,19);

                                    String mStatus = jsonObject1.getString("CurrentStatus");
                                    String mRemark = jsonObject1.getString("cRemarks");

                                    JSONObject jsonObject3 = jsonObject1.getJSONObject("dtCreatedDate");
                                    String mCreatedDate = jsonObject3.getString("date").substring(0,19);

                                    dataRefwiseLeads  = new DetailStatuswiseLeadReportData(mSrNo, mRefNo, mCandName, mCourse, mMobile, mAddress,
                                            mCity, mState, mPinCode, mParentNo, mEmail, mDataFrom, mAllocatedTo, mAllocatedDate, mStatus, mRemark, mCreatedDate);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterStatuswiseDetailLeadReport.notifyDataSetChanged();

                                    //=======================bar chart
                                    /*x.add(new BarEntry(Float.parseFloat(mDataFrom), i));
                                    y.add(mCandName);
                                    z.add(mSrNo);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    //statusdetailBarChart.setData(data);*/
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                //progressBar.dismiss();
                                Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeadDetailRecords.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDatewiseLeads(){
        try {
            progressBar = ProgressDialog.show(ActivityStatuswiseLeadDetailRecords.this, "", "Loading Total Leads", true);

            String datecounsid = sp.getString("datecounsid",null);
            url=clientUrl + "?clientid=" + clientId + "&caseid=A7&Step=6&SelectedDate="+leadDate+"&CounselorID="+datecounsid+"&cdatafrom="+Counid;
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

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String mSrNo = jsonObject1.getString("nSrNo");
                                    String mRefNo = jsonObject1.getString("refNo");
                                    String mCandName=jsonObject1.getString("cCandidateName");
                                    String mCourse = jsonObject1.getString("cCourse");
                                    String mMobile = jsonObject1.getString("cMobile");
                                    String mAddress = jsonObject1.getString("cAddressLine");
                                    String mCity = jsonObject1.getString("cCity");
                                    String mState = jsonObject1.getString("cState");
                                    String mPinCode = jsonObject1.getString("cPinCode");
                                    String mParentNo = jsonObject1.getString("cParantNo");
                                    String mEmail = jsonObject1.getString("cEmail");
                                    String mDataFrom = jsonObject1.getString("cDataFrom");
                                    String mAllocatedTo = jsonObject1.getString("AllocatedTo");

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("AllocationDate");
                                    String mAllocatedDate = jsonObject2.getString("date").substring(0,19);

                                    String mStatus = jsonObject1.getString("CurrentStatus");
                                    String mRemark = jsonObject1.getString("cRemarks");

                                    JSONObject jsonObject3 = jsonObject1.getJSONObject("dtCreatedDate");
                                    String mCreatedDate = jsonObject3.getString("date").substring(0,19);

                                    dataRefwiseLeads  = new DetailStatuswiseLeadReportData(mSrNo, mRefNo, mCandName, mCourse, mMobile, mAddress,
                                            mCity, mState, mPinCode, mParentNo, mEmail, mDataFrom, mAllocatedTo, mAllocatedDate, mStatus, mRemark, mCreatedDate);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterStatuswiseDetailLeadReport.notifyDataSetChanged();
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                //progressBar.dismiss();
                                Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeadDetailRecords.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataRefwiseLeads(){
        try {
            progressBar = ProgressDialog.show(ActivityStatuswiseLeadDetailRecords.this, "", "Loading Total Leads", true);

            /*if (RefId == null){
                url=clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=5&Statusid="+sp.getString("chartrefid",null)+"&CounselorID="+sp.getString("chartcounsid",null)+"&DataRefID="+chartstatusid;
                Log.d("UrlReportNo", url);
                txtRefname.setText(sp.getString("chartrefname",null) + " => " + sp.getString("chartreflead",null));
                txtCounsName.setText(sp.getString("chartcounsname",null) + " => " + sp.getString("chartcounslead",null));
                txtStatusName.setText(chartstatusNm+" => "+chartstatusttllead);
            } else*/ {
                url=clientUrl + "?clientid=" + clientId + "&caseid=A6&Step=5&Statusid="+RefId+"&CounselorID="+Counid+"&DataRefID="+statusid;
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
                                /*statusdetailBarChart = (BarChart) findViewById(R.id.ttllead_statusdetailbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                statusdetailBarChart.setTouchEnabled(true);
                                statusdetailBarChart.setDragEnabled(true);
                                statusdetailBarChart.setScaleEnabled(true);
                                statusdetailBarChart.animateY(3000);
                                //statusdetailBarChart.setOnChartValueSelectedListener(ActivityStatuswiseLeadDetailRecords.this);
                                statusdetailBarChart.setMarkerView(mv);*/
                                //============================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String mSrNo = jsonObject1.getString("nSrNo");
                                    String mRefNo = jsonObject1.getString("refNo");
                                    String mCandName=jsonObject1.getString("cCandidateName");
                                    String mCourse = jsonObject1.getString("cCourse");
                                    String mMobile = jsonObject1.getString("cMobile");
                                    String mAddress = jsonObject1.getString("cAddressLine");
                                    String mCity = jsonObject1.getString("cCity");
                                    String mState = jsonObject1.getString("cState");
                                    String mPinCode = jsonObject1.getString("cPinCode");
                                    String mParentNo = jsonObject1.getString("cParantNo");
                                    String mEmail = jsonObject1.getString("cEmail");
                                    String mDataFrom = jsonObject1.getString("cDataFrom");
                                    String mAllocatedTo = jsonObject1.getString("AllocatedTo");

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("AllocationDate");
                                    String mAllocatedDate = jsonObject2.getString("date").substring(0,19);

                                    String mStatus = jsonObject1.getString("CurrentStatus");
                                    String mRemark = jsonObject1.getString("cRemarks");

                                    JSONObject jsonObject3 = jsonObject1.getJSONObject("dtCreatedDate");
                                    String mCreatedDate = jsonObject3.getString("date").substring(0,19);

                                    dataRefwiseLeads  = new DetailStatuswiseLeadReportData(mSrNo, mRefNo, mCandName, mCourse, mMobile, mAddress,
                                            mCity, mState, mPinCode, mParentNo, mEmail, mDataFrom, mAllocatedTo, mAllocatedDate, mStatus, mRemark, mCreatedDate);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterStatuswiseDetailLeadReport.notifyDataSetChanged();

                                    //=======================bar chart
                                    /*x.add(new BarEntry(Float.parseFloat(mDataFrom), i));
                                    y.add(mCandName);
                                    z.add(mSrNo);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    //statusdetailBarChart.setData(data);*/
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                //progressBar.dismiss();
                                Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeadDetailRecords.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getStatuswiseLeadDetails() {
        try {
            progressBar = ProgressDialog.show(ActivityStatuswiseLeadDetailRecords.this, "", "Loading Total Leads", true);

            /*if (RefId == null){
                url=clientUrl + "?clientid=" + clientId + "&caseid=A2&Step=5&RefId="+sp.getString("chartrefid",null)+"&CounselorID="+sp.getString("chartcounsid",null)+"&Status="+chartstatusid;
                Log.d("UrlReportNo", url);
                txtRefname.setText(sp.getString("chartrefname",null) + " => " + sp.getString("chartreflead",null));
                txtCounsName.setText(sp.getString("chartcounsname",null) + " => " + sp.getString("chartcounslead",null));
                txtStatusName.setText(chartstatusNm+" => "+chartstatusttllead);
            } else*/ {
                url=clientUrl + "?clientid=" + clientId + "&caseid=A2&Step=5&RefId="+RefId+"&CounselorID="+Counid+"&Status="+statusid;
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
                                /*statusdetailBarChart = (BarChart) findViewById(R.id.ttllead_statusdetailbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                statusdetailBarChart.setTouchEnabled(true);
                                statusdetailBarChart.setDragEnabled(true);
                                statusdetailBarChart.setScaleEnabled(true);
                                statusdetailBarChart.animateY(3000);
                                //statusdetailBarChart.setOnChartValueSelectedListener(ActivityStatuswiseLeadDetailRecords.this);
                                statusdetailBarChart.setMarkerView(mv);*/
                                //============================================

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String mSrNo = jsonObject1.getString("nSrNo");
                                    String mRefNo = jsonObject1.getString("refNo");
                                    String mCandName=jsonObject1.getString("cCandidateName");
                                    String mCourse = jsonObject1.getString("cCourse");
                                    String mMobile = jsonObject1.getString("cMobile");
                                    String mAddress = jsonObject1.getString("cAddressLine");
                                    String mCity = jsonObject1.getString("cCity");
                                    String mState = jsonObject1.getString("cState");
                                    String mPinCode = jsonObject1.getString("cPinCode");
                                    String mParentNo = jsonObject1.getString("cParantNo");
                                    String mEmail = jsonObject1.getString("cEmail");
                                    String mDataFrom = jsonObject1.getString("cDataFrom");
                                    String mAllocatedTo = jsonObject1.getString("AllocatedTo");

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("AllocationDate");
                                    String mAllocatedDate = jsonObject2.getString("date").substring(0,19);

                                    String mStatus = jsonObject1.getString("CurrentStatus");
                                    String mRemark = jsonObject1.getString("cRemarks");

                                    JSONObject jsonObject3 = jsonObject1.getJSONObject("dtCreatedDate");
                                    String mCreatedDate = jsonObject3.getString("date").substring(0,19);

                                    dataRefwiseLeads  = new DetailStatuswiseLeadReportData(mSrNo, mRefNo, mCandName, mCourse, mMobile, mAddress,
                                            mCity, mState, mPinCode, mParentNo, mEmail, mDataFrom, mAllocatedTo, mAllocatedDate, mStatus, mRemark, mCreatedDate);
                                    arrayListCounselorwise.add(dataRefwiseLeads);
                                    adapterStatuswiseDetailLeadReport.notifyDataSetChanged();

                                    //=======================bar chart
                                    /*x.add(new BarEntry(Float.parseFloat(mDataFrom), i));
                                    y.add(mCandName);
                                    z.add(mSrNo);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    //statusdetailBarChart.setData(data);*/
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityStatuswiseLeadDetailRecords.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityStatuswiseLeadDetailRecords.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.d("indexqq", String.valueOf(e.getXIndex()));
        int index = e.getXIndex();

        Intent intent = new Intent(ActivityStatuswiseLeadDetailRecords.this, ActivityLeadsDetailsReport.class);
        intent.putExtra("mSrNo",arrayListCounselorwise.get(index).getmSrNo());
        intent.putExtra("mRefNo",arrayListCounselorwise.get(index).getmRefNo());
        intent.putExtra("mCandName",arrayListCounselorwise.get(index).getmCandName());
        intent.putExtra("mCourse",arrayListCounselorwise.get(index).getmCourse());
        intent.putExtra("mMobile",arrayListCounselorwise.get(index).getmMobile());
        intent.putExtra("mAddress",arrayListCounselorwise.get(index).getmAddress());
        intent.putExtra("mCity",arrayListCounselorwise.get(index).getmCity());
        intent.putExtra("mState",arrayListCounselorwise.get(index).getmState());
        intent.putExtra("mPinCode",arrayListCounselorwise.get(index).getmPinCode());
        intent.putExtra("mParentNo",arrayListCounselorwise.get(index).getmParentNo());
        intent.putExtra("mEmail",arrayListCounselorwise.get(index).getmEmail());
        intent.putExtra("mDataFrom",arrayListCounselorwise.get(index).getmDataFrom());
        intent.putExtra("mAllocatedTo",arrayListCounselorwise.get(index).getmAllocatedTo());
        intent.putExtra("mAllocatedDate",arrayListCounselorwise.get(index).getmAllocatedDate());
        intent.putExtra("mStatus",arrayListCounselorwise.get(index).getmStatus());
        intent.putExtra("mRemark",arrayListCounselorwise.get(index).getmRemark());
        intent.putExtra("mCreatedDate",arrayListCounselorwise.get(index).getmCreatedDate());
        Log.d("indexss", z.get(index)+" "+y.get(index)+" "+x.get(index));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Animatoo.animateSlideLeft(ActivityStatuswiseLeadDetailRecords.this);
    }

    @Override
    public void onNothingSelected() {

    }*/
}
