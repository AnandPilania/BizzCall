package com.bizcall.wayto.mentebit13;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityPending extends AppCompatActivity {

    public static String strLayout, clientid, clienturl, counselorId;
    public static TextView txtActivityTitle, txtNotFound;
    SharedPreferences sp;
    RequestQueue requestQueue;
    public static ProgressDialog progressDialog;
    ImageView imgBack,imgRefresh;
    public static ArrayList<DataPendingDetails> arrayListPendingDetails;
    ArrayList<DataPendingRejectDetails> arrayListPendingRejectedDetails;
    public static RecyclerView recyclerView;
    public static AdapterPendingDetails adapterPendingDetails;
    AdapterPendingRejectDetails adapterPendingRejectDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

       initialize();
        //Log.d("qaz", strLayout);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switch (strLayout) {
            case "Pending On Me":
                //to get transaction pending amount on user
                getPendingOnMe();
                imgRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPendingOnMe();
                    }
                });
                break;
            case "Pending On Others":
                //to get transaction pending amount on others
                getPendingOnOthers();
                imgRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPendingOnOthers();
                    }
                });
                break;
            case "Rejected Flow":
                //to get transaction rejected amount
                getRejectedFlow();
                imgRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRejectedFlow();
                    }
                });
                break;
        }
    }

    private void initialize() {
        requestQueue = Volley.newRequestQueue(ActivityPending.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        counselorId = sp.getString("Id", "");
        counselorId = counselorId.replace(" ", "");

        txtActivityTitle = findViewById(R.id.txtActivitywithStatus);
        txtNotFound = findViewById(R.id.txt_notfound);
        imgBack = findViewById(R.id.img_back);
        imgRefresh = findViewById(R.id.imgRefresh1);
        recyclerView=findViewById(R.id.recyclerpendingdetails);

        strLayout = getIntent().getStringExtra("pendingdetails");
        txtActivityTitle.setText(strLayout);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityPending.this, AccountsActivity.class);
        startActivity(intent);
        finish();
    }

    public void getPendingOnMe() {
        arrayListPendingDetails = new ArrayList<>();
        progressDialog = ProgressDialog.show(ActivityPending.this, "","Loading...",true);
        String urlPending = clienturl + "?clientid=" + clientid + "&caseid=128&CounselorID="+counselorId+"&Step=1";
        Log.d("urlPending", urlPending);
        StringRequest jsonEducationInfo = new StringRequest(Request.Method.GET, urlPending, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    Log.d("clientresponce", response);
                    if (response.contains("[]")){
                        txtNotFound.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    } else {
                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String strCashOutId = jsonObject.getString("nCashOutId");
                            String strExpenceDate = jsonObject.getString("Expense Date");
                            String strAmountType = jsonObject.getString("cAmountType");
                            String strAmount = jsonObject.getString("cAmount");
                            String strCategoryType = jsonObject.getString("CategoryType");
                            String strMemo = jsonObject.getString("cmemo");
                            String strRemark = jsonObject.getString("cRemarks");
                            String strCategoryName = jsonObject.getString("CategoryName");
                            String strApprovedBy = jsonObject.getString("ApprovedBy");
                            String strReceiptFileName = jsonObject.getString("ReceiptFileName");
                            String strFrom = jsonObject.getString("From");
                            String strYou = jsonObject.getString("You");

                            DataPendingDetails dataPendingDetails = new DataPendingDetails(strCashOutId, strExpenceDate,strAmountType,
                                    strAmount, strCategoryType, strMemo, strRemark, strCategoryName, strApprovedBy, strReceiptFileName,
                                    strFrom, strYou);
                            arrayListPendingDetails.add(dataPendingDetails);

                            adapterPendingDetails = new AdapterPendingDetails(arrayListPendingDetails, ActivityPending.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityPending.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapterPendingDetails);
                            adapterPendingDetails.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
                        //Log.d("educationarray",  + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });

        jsonEducationInfo.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonEducationInfo);
    }

    public void getPendingOnOthers() {
        arrayListPendingRejectedDetails = new ArrayList<>();
        progressDialog = ProgressDialog.show(ActivityPending.this, "","Loading...",true);
        String urlPending = clienturl + "?clientid=" + clientid + "&caseid=128&CounselorID="+counselorId+"&Step=2";
        Log.d("urlPending", urlPending);
        StringRequest jsonEducationInfo = new StringRequest(Request.Method.GET, urlPending, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    Log.d("clientresponce", response);

                    if (response.contains("[]")){
                        txtNotFound.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    } else {
                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String strExpenceDate = jsonObject.getString("Expense Date");
                            String strAmountType = jsonObject.getString("cAmountType");
                            String strAmount = jsonObject.getString("cAmount");
                            String strCategoryType = jsonObject.getString("CategoryType");
                            String strMemo = jsonObject.getString("cmemo");
                            String strRemark = jsonObject.getString("cRemarks");
                            String strCategoryName = jsonObject.getString("CategoryName");
                            String strApprovedBy = jsonObject.getString("ApprovedBy");
                            String strReceiptFileName = jsonObject.getString("ReceiptFileName");
                            String strFrom = jsonObject.getString("From");
                            String strYou = jsonObject.getString("You");
                            String strRejectedDate = "";
                            String strApprovedRemark = "";

                            DataPendingRejectDetails dataPendingDetails = new DataPendingRejectDetails(strExpenceDate, strAmountType, strAmount,
                                    strCategoryType, strMemo, strRemark, strCategoryName, strApprovedBy, strReceiptFileName, strFrom,
                                    strYou, strRejectedDate, strApprovedRemark);
                            arrayListPendingRejectedDetails.add(dataPendingDetails);

                            recyclerView = findViewById(R.id.recyclerpendingdetails);
                            adapterPendingRejectDetails = new AdapterPendingRejectDetails(arrayListPendingRejectedDetails, ActivityPending.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityPending.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapterPendingRejectDetails);
                            adapterPendingRejectDetails.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
                        //Log.d("educationarray",  + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });

        jsonEducationInfo.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonEducationInfo);
    }

    public void getRejectedFlow() {
        arrayListPendingRejectedDetails = new ArrayList<>();
        progressDialog = ProgressDialog.show(ActivityPending.this, "","Loading...",true);
        String urlPending = clienturl + "?clientid=" + clientid + "&caseid=128&CounselorID="+counselorId+"&Step=3";
        Log.d("urlPending", urlPending);
        StringRequest jsonEducationInfo = new StringRequest(Request.Method.GET, urlPending, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    Log.d("clientresponce", response);

                    if (response.contains("[]")){
                        txtNotFound.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    } else {
                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String strExpenceDate = jsonObject.getString("Expense Date");
                            String strAmountType = jsonObject.getString("cAmountType");
                            String strAmount = jsonObject.getString("cAmount");
                            String strCategoryType = jsonObject.getString("CategoryType");
                            String strMemo = jsonObject.getString("cmemo");
                            String strRemark = jsonObject.getString("cRemarks");
                            String strCategoryName = jsonObject.getString("CategoryName");
                            String strApprovedBy = jsonObject.getString("ApprovedBy");
                            String strReceiptFileName = jsonObject.getString("ReceiptFileName");
                            String strFrom = jsonObject.getString("From");
                            String strYou = jsonObject.getString("You");
                            String strRejectedDate = jsonObject.getString("rejected date");
                            String strApprovedRemark = jsonObject.getString("ApprovedRemarks");

                            DataPendingRejectDetails dataPendingDetails = new DataPendingRejectDetails(strExpenceDate, strAmountType, strAmount,
                                    strCategoryType, strMemo, strRemark, strCategoryName, strApprovedBy, strReceiptFileName, strFrom,
                                    strYou, strRejectedDate, strApprovedRemark);
                            arrayListPendingRejectedDetails.add(dataPendingDetails);

                            recyclerView = findViewById(R.id.recyclerpendingdetails);
                            adapterPendingRejectDetails = new AdapterPendingRejectDetails(arrayListPendingRejectedDetails, ActivityPending.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityPending.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapterPendingRejectDetails);
                            adapterPendingRejectDetails.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
                        //Log.d("educationarray",  + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });

        jsonEducationInfo.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonEducationInfo);
    }

}
