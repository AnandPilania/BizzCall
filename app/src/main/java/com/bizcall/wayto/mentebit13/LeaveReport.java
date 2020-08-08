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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaveReport extends AppCompatActivity {

    RecyclerView recyclerPendingApproval,recyclerApprovedLeaves;
    AdapterPendingApproval adapterPendingApproval,adapterApproved;
    ArrayList<DataPendingApproval> arrayListPendingApproval,arrayListApproval;
    RequestQueue requestQueue;
    SharedPreferences sp;
    String counselorid,clientid,clienturl,counselorname;
    long timeout;
    Thread thread;
    ImageView imgBack,imgRefresh;
    FloatingActionButton fab;
    ProgressDialog dialog;
    LinearLayout linearApprovedLeaves,linearPendingApproval;
    TextView txtNoAppliedLeaves,txtNoApprovedLeaves;
    TextView txtPending,txtApproved,txtRejected,txtHold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_leave_report);
            initialize();
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LeaveReport.this, LeaveReport.class);
                    startActivity(intent);
                    finish();
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LeaveReport.this, LeaveApplication.class);
                    startActivity(intent);
                }
            });
            dialog = ProgressDialog.show(LeaveReport.this, "", "Loading pending approval", true);
            newThreadInitilization(dialog);
            //when first activity will be loaded all pending approvals for leave will be displayed here
            getPendingApproval("0");

            txtApproved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = ProgressDialog.show(LeaveReport.this, "", "Loading approved leaves", true);
                    newThreadInitilization(dialog);
                    //when first activity will be loaded all pending approvals for leave will be displayed here
                     getPendingApproval("1");
                    txtApproved.setBackgroundColor(getResources().getColor(R.color.toolbar_background));
                    txtPending.setBackgroundColor(getResources().getColor(R.color.white));
                    txtRejected.setBackgroundColor(getResources().getColor(R.color.white));
                    txtHold.setBackgroundColor(getResources().getColor(R.color.white));
                }
            });

            txtPending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = ProgressDialog.show(LeaveReport.this, "", "Loading pending leaves", true);
                    newThreadInitilization(dialog);
                    getPendingApproval("0");
                    txtPending.setBackgroundColor(getResources().getColor(R.color.toolbar_background));
                    txtApproved.setBackgroundColor(getResources().getColor(R.color.white));
                    txtRejected.setBackgroundColor(getResources().getColor(R.color.white));
                    txtHold.setBackgroundColor(getResources().getColor(R.color.white));
                }
            });

            txtRejected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = ProgressDialog.show(LeaveReport.this, "", "Loading rejected leaves", true);
                    newThreadInitilization(dialog);
                    getPendingApproval("2");
                    txtRejected.setBackgroundColor(getResources().getColor(R.color.toolbar_background));
                    txtPending.setBackgroundColor(getResources().getColor(R.color.white));
                    txtApproved.setBackgroundColor(getResources().getColor(R.color.white));
                    txtHold.setBackgroundColor(getResources().getColor(R.color.white));
                }
            });

            txtHold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = ProgressDialog.show(LeaveReport.this, "", "Loading holded leaves", true);
                    newThreadInitilization(dialog);
                    getPendingApproval("3");
                    txtHold.setBackgroundColor(getResources().getColor(R.color.toolbar_background));
                    txtPending.setBackgroundColor(getResources().getColor(R.color.white));
                    txtRejected.setBackgroundColor(getResources().getColor(R.color.white));
                    txtApproved.setBackgroundColor(getResources().getColor(R.color.white));
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(LeaveReport.this,"Errorcode-532 LeaveReport onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize()
    {

        recyclerPendingApproval = findViewById(R.id.recyclerPendingApproval);
        linearPendingApproval = findViewById(R.id.linearPendingApproval);
        txtNoAppliedLeaves = findViewById(R.id.txtNoAppliedLeaves);

        txtPending=findViewById(R.id.txtPending);
        txtApproved=findViewById(R.id.txtApproved);
        txtRejected=findViewById(R.id.txtRejected);
        txtHold=findViewById(R.id.txtHold);

        fab = findViewById(R.id.fab);
        imgBack = findViewById(R.id.img_back);
        imgRefresh = findViewById(R.id.imgRefresh);
        requestQueue = Volley.newRequestQueue(LeaveReport.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replaceAll(" ", "");
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        counselorname = sp.getString("Name", null);
        timeout = sp.getLong("TimeOut", 0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(LeaveReport.this,Home.class);
        intent.putExtra("Activity","TotalCallMade");
        startActivity(intent);
        finish();
    }

    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    // dialog1.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(LeaveReport.this, "Connection Aborted.", Toast.LENGTH_SHORT).show();
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


    //here isapproved=0 for pending approval
    //     isapproved=1 for approved approval
    //     isapproved=2 for rejected approval
    //     isapproved=3 for holded approval

    public void getPendingApproval(final String isapproved)
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid=146&Step=1&CounselorID="+counselorid+"&IsApproved="+isapproved;
            Log.d("PendingUrl", url);
            arrayListPendingApproval=new ArrayList<>();
            if(CheckInternet.checkInternet(LeaveReport.this))
            {
                if(CheckServer.isServerReachable(LeaveReport.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();

                                    Log.d("PendingResponse", response);
                                    try {
                                     //   getApprovedLeaves();
                                        if(response.contains("[]"))
                                        {
                                            if(isapproved.equals("0"))
                                            {
                                                txtNoAppliedLeaves.setText("No pending approvals");
                                            }
                                            else if(isapproved.equals("1"))
                                            {
                                                txtNoAppliedLeaves.setText("No approved leaves");
                                            }else if(isapproved.equals("2"))
                                            {
                                                txtNoAppliedLeaves.setText("No rejected leaves");
                                            }else if(isapproved.equals("3"))
                                            {
                                                txtNoAppliedLeaves.setText("No holded leaves");
                                            }
                                            linearPendingApproval.setVisibility(View.GONE);
                                            txtNoAppliedLeaves.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            linearPendingApproval.setVisibility(View.VISIBLE);
                                            txtNoAppliedLeaves.setVisibility(View.GONE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String leaveID = jsonObject1.getString("nLeaveID");
                                                String dtDateFrom = jsonObject1.getString("dtDateFrom");
                                                dtDateFrom = dtDateFrom.substring(9, dtDateFrom.lastIndexOf(" "));
                                                String dtDateTo = jsonObject1.getString("dtDateTo");
                                                dtDateTo = dtDateTo.substring(9, dtDateTo.lastIndexOf(" "));
                                                String nTotalDays = jsonObject1.getString("nTotalDays");
                                                String remarks = jsonObject1.getString("cRemarks");
                                                String dtApplicationDate = jsonObject1.getString("dtApplicationDate");
                                                dtApplicationDate = dtApplicationDate.substring(9, dtApplicationDate.lastIndexOf(" "));

                                                DataPendingApproval dataPendingApproval = new DataPendingApproval(leaveID, dtDateFrom, dtDateTo, nTotalDays, remarks, dtApplicationDate);
                                                arrayListPendingApproval.add(dataPendingApproval);
                                            }
                                            adapterPendingApproval = new AdapterPendingApproval(LeaveReport.this, arrayListPendingApproval);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(LeaveReport.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerPendingApproval.setLayoutManager(layoutManager);
                                            recyclerPendingApproval.setAdapter(adapterPendingApproval);
                                            adapterPendingApproval.notifyDataSetChanged();

                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(LeaveReport.this,"Errorcode-536 LeaveReport PendingApprovalResponse"+e.toString(),Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Log.d("CounselorDetailExceptio", String.valueOf(e));
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LeaveReport.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(LeaveReport.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LeaveReport.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LeaveReport.this);
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
            Toast.makeText(LeaveReport.this,"Errorcode-535 LeaveReport getPendingApproval"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }//getPendingApproval
}
