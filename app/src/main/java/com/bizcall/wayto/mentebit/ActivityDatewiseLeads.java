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

public class ActivityDatewiseLeads extends AppCompatActivity {
    DataRefwiseLeads dataRefwiseLeads;
    ArrayList<DataRefwiseLeads> arrayListCounselorwise;
    AdapterRefwiseLeads adapterRefwiseLeads;
    RecyclerView recyclerView;
    String url,clientUrl,clientId,mainActivityName,leadDate,TtlLeadsDate;
    RequestQueue requestQueue;
    String statusname,statusleads,statusid,totalLeadCount,RefId, RefName, RefLeads,Counid,Counname,Countotalleads;
    TextView txtTotalLeadval,txtRefname,txtRefnameval, txtCounsName,txtCounsNameval, txtStatusName,txtStatusNameval;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TableLayout tableLayout;
    ImageView imgBack;
    Vibrator vibrator;
    private ProgressDialog progressBar;

    String chartdateNm, chartdateid, chartdatettllead;
    BarChart dateBarChart;
    ArrayList<BarEntry> x;
    ArrayList<String> y;
    ArrayList<String> z;
    MarkerView mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datewise_leads);

        requestQueue= Volley.newRequestQueue(ActivityDatewiseLeads.this);
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
        Log.d("adapclick",totalLeadCount);

        statusid=getIntent().getStringExtra("StatusId");
        statusname=getIntent().getStringExtra("StatusName");
        statusleads=getIntent().getStringExtra("TotalLeads");

        editor = sp.edit();
        editor.putString("datecounsid",statusid);
        editor.putString("datecounsname",statusname);
        editor.putString("datecounsttlleads",statusleads);
        editor.commit();

        txtTotalLeadval.setText(totalLeadCount);
        txtCounsName.setText(Counname);
        txtCounsNameval.setText(Countotalleads);
        txtStatusName.setText(statusname);
        txtStatusNameval.setText(statusleads);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
                Animatoo.animateSlideRight(ActivityDatewiseLeads.this);
            }
        });

        try {
            if (CheckInternetSpeed.checkInternet(ActivityDatewiseLeads.this).contains("0")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDatewiseLeads.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(ActivityDatewiseLeads.this).contains("1")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDatewiseLeads.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        }).show();
            } else {
                try {
                     if (mainActivityName.contains("Datewise Report")){
                        txtRefname.setText(leadDate);
                         txtRefnameval.setText(TtlLeadsDate);
                        getDatewiseLeads();
                    }
                }catch (Exception e){
                    progressBar.dismiss();
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            progressBar.dismiss();
            e.printStackTrace();
            Toast.makeText(ActivityDatewiseLeads.this, "ErrorCode-1000: Login Website Button", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDatewiseLeads(){
        try {
            progressBar = ProgressDialog.show(ActivityDatewiseLeads.this, "", "Loading Total Leads", true);

            url=clientUrl + "?clientid=" + clientId + "&caseid=A7&Step=5&SelectedDate="+leadDate+"&cdatafrom="+Counid+"&CounselorID="+statusid;
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
                                adapterRefwiseLeads = new AdapterRefwiseLeads(arrayListCounselorwise, getApplicationContext());
                                LinearLayoutManager manager = new LinearLayoutManager(ActivityDatewiseLeads.this);
                                manager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(manager);
                                recyclerView.setAdapter(adapterRefwiseLeads);

                                tableLayout.setVisibility(View.VISIBLE);
                                arrayListCounselorwise.clear();

                                //==============================Bar chart
                                dateBarChart = (BarChart) findViewById(R.id.ttllead_statusdatedetailbarchart);
                                y = new ArrayList<String>();
                                x = new ArrayList<BarEntry>();
                                z = new ArrayList<>();

                                dateBarChart.setTouchEnabled(true);
                                dateBarChart.setDragEnabled(true);
                                dateBarChart.setScaleEnabled(true);
                                dateBarChart.animateY(3000);
                                //statusBarChart.setOnChartValueSelectedListener(ActivityStatuswiseLeads.this);
                                dateBarChart.setMarkerView(mv);
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
                                    adapterRefwiseLeads.notifyDataSetChanged();

                                    //=======================bar chart
                                    x.add(new BarEntry(Float.parseFloat(totalleads), i));
                                    y.add(statusname);
                                    z.add(statusid);

                                    BarDataSet set1 = new BarDataSet(x, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(y, set1);
                                    dateBarChart.setData(data);
                                }
                                progressBar.dismiss();
                            } catch (Exception e) {
                                progressBar.dismiss();
                                Toast.makeText(ActivityDatewiseLeads.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDatewiseLeads.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                progressBar.dismiss();
                                            }
                                        }).show();
                                // dialog.dismiss();
                                Toast.makeText(ActivityDatewiseLeads.this, "Server Error", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ActivityDatewiseLeads.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityDatewiseLeads.this.overridePendingTransition(0,
                R.anim.play_panel_close_background);
            /*Intent intent = new Intent(ActivityCounselorwiseLeads.this, ActivityTotalLeadReport.class);
            intent.putExtra("Activity", "GraphReport");
            startActivity(intent);*/
        finish();
        //Animatoo.animateSlideRight(ActivityCounselorwiseLeads.this);
    }
}
