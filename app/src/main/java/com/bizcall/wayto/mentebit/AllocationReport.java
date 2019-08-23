package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllocationReport extends AppCompatActivity {

    TextView txtAllocatedTo,txtAllocatedBy,txtMin,txtMax,txtDisplayInfo,txtNotFound;
    RecyclerView recyclerAllocation;
    Button btnPrevious,btnNext;
    AdapterReallocation  adapterReallocation;
    ArrayList<DataReallocation> arrayListReallocation;
    UrlRequest urlRequest;
    String clienturl,clientid,counselorid,urlCounselor,strMin,strMax;
    SharedPreferences sp;
    ProgressDialog dialog;
    ImageView imgBack,imgRefresh;
    AlertDialog alertDialog;
    long timeout;
    Vibrator vibrator;
    SharedPreferences.Editor editor;
    String strToBy;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_allocation_report);
            requestQueue = Volley.newRequestQueue(AllocationReport.this);
            btnPrevious = findViewById(R.id.btnLoadPrevious);
            btnNext = findViewById(R.id.btnLoadMore);
            txtAllocatedBy = findViewById(R.id.txtAllocatedBy);
            txtAllocatedTo = findViewById(R.id.txtAllocatedTo);
            txtMin = findViewById(R.id.txtMin);
            txtMax = findViewById(R.id.txtMax);
            txtDisplayInfo = findViewById(R.id.txtDisplayInfo);
            imgBack = findViewById(R.id.img_back);
            txtNotFound = findViewById(R.id.txtNotFound);
            imgRefresh=findViewById(R.id.imgRefresh);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            clienturl = sp.getString("ClientUrl", null);
            clientid = sp.getString("ClientId", null);
            counselorid = sp.getString("Id", null);
            timeout = sp.getLong("TimeOut", 0);
            counselorid = counselorid.replace(" ", "");

            strMin = "1";
            strMax = "25";
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());

            txtAllocatedBy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtMin.setText("1");
                    txtMax.setText("25");
                    txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
                    editor.putString("ToorBy", "By");
                    editor.commit();
                    urlCounselor = clienturl + "?clientid=" + clientid + "&caseid=303&nCounsellorId=" + counselorid + "&ToOrBy=By&MinVal=" + txtMin.getText().toString() + "&MaxVal=" + txtMax.getText().toString();
                    if(CheckInternetSpeed.checkInternet(AllocationReport.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
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
                    else if(CheckInternetSpeed.checkInternet(AllocationReport.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
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
                    }
                    else {

                        dialog = ProgressDialog.show(AllocationReport.this, "", "Loading", true);
                        getRellocation();
                    }
                }
            });
            txtAllocatedTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtMin.setText("1");
                    txtMax.setText("25");
                    txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
                    editor.putString("ToorBy", "To");
                    editor.commit();
                    urlCounselor = clienturl + "?clientid=" + clientid + "&caseid=303&nCounsellorId=" + counselorid + "&ToOrBy=To&MinVal=" + txtMin.getText().toString() + "&MaxVal=" + txtMax.getText().toString();
                    if(CheckInternetSpeed.checkInternet(AllocationReport.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
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
                    else if(CheckInternetSpeed.checkInternet(AllocationReport.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
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
                    }
                    else {
                        dialog = ProgressDialog.show(AllocationReport.this, "", "Loading Clients Allocated By You To Other Counsellor", true);
                        //  countdown();
                        getRellocation();
                    }

                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(AllocationReport.this,AllocationReport.class);
                   // intent.putExtra("Activity",activityName);
                    startActivity(intent);
                }
            });
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
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(AllocationReport.this,"Errorcode-356 ReallocationReport onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ReminderException", String.valueOf(e));
        }

    }//onCreate
    public void btnNextClicked(){
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) + 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) + 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
            strToBy = sp.getString("ToorBy", null);
            if (strToBy.contains("By")) {
                urlCounselor = clienturl + "?clientid=" + clientid + "&caseid=303&nCounsellorId=" + counselorid + "&ToOrBy=By&MinVal=" + txtMin.getText().toString() + "&MaxVal=" + txtMax.getText().toString();
            } else {
                urlCounselor = clienturl + "?clientid=" + clientid + "&caseid=303&nCounsellorId=" + counselorid + "&ToOrBy=To&MinVal=" + txtMin.getText().toString() + "&MaxVal=" + txtMax.getText().toString();
            }
            if (CheckInternetSpeed.checkInternet(AllocationReport.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
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
            } else if (CheckInternetSpeed.checkInternet(AllocationReport.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
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
                dialog = ProgressDialog.show(AllocationReport.this, "", "Loading", true);
                // countdown();
                getRellocation();
            }
        }catch (Exception e)
        {
            Toast.makeText(AllocationReport.this,"Errorcode-357 ReallocationReport btnNextClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnPreviousClicked(){
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) - 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) - 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
            if (strToBy.contains("By")) {
                urlCounselor = clienturl + "?clientid=" + clientid + "&caseid=303&nCounsellorId=" + counselorid + "&ToOrBy=By&MinVal=" + txtMin.getText().toString() + "&MaxVal=" + txtMax.getText().toString();
            } else {
                urlCounselor = clienturl + "?clientid=" + clientid + "&caseid=303&nCounsellorId=" + counselorid + "&ToOrBy=To&MinVal=" + txtMin.getText().toString() + "&MaxVal=" + txtMax.getText().toString();
            }
            if (CheckInternetSpeed.checkInternet(AllocationReport.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
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
            } else if (CheckInternetSpeed.checkInternet(AllocationReport.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
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
                dialog = ProgressDialog.show(AllocationReport.this, "", "Loading", true);
                //countdown();
                getRellocation();
            }
        }catch (Exception e)
        {
            Toast.makeText(AllocationReport.this,"Errorcode-358 ReallocationReport btnPreviousClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void getRellocation()
    {
        try {
            arrayListReallocation = new ArrayList<>();
            Log.d("ReallocationUrl", urlCounselor);
            if (CheckInternet.checkInternet(AllocationReport.this)) {
                if(CheckServer.isServerReachable(AllocationReport.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCounselor,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    int mval = Integer.parseInt(txtMax.getText().toString());
                                    Log.d("MaxVal", String.valueOf(mval));
                                    if (response.contains("[]")) {
                                        txtNotFound.setVisibility(View.VISIBLE);
                                    } else {
                                        txtNotFound.setVisibility(View.GONE);
                                    }

                                    Log.d("CounselorResponse", response);
                                    try {
                                        //arraylistCounselor.clear();
                                        JSONObject jsonObject = new JSONObject(response);
                                        // Log.d("Json",jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String serial = jsonObject1.getString("SNO");
                                            String srno = jsonObject1.getString("SR NO");
                                            String cname = jsonObject1.getString("Candidate Name");
                                            String course = jsonObject1.getString("Course");
                                            String counselorname = jsonObject1.getString("New Counselor");
                                            String statusStr = jsonObject1.getString("Current Status");
                                            Log.d("Status11", srno);
                                            DataReallocation dataReallocation = new DataReallocation(serial, srno, cname, course, statusStr, counselorname);
                                            arrayListReallocation.add(dataReallocation);
                                        }

                                        recyclerAllocation = findViewById(R.id.recyclerReallocationReport);
                                        adapterReallocation = new AdapterReallocation(AllocationReport.this, arrayListReallocation);
                                        // recyclerAllocation = findViewById(R.id.recyclerCounselorData);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(AllocationReport.this);
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerAllocation.setLayoutManager(layoutManager);
                                        recyclerAllocation.setAdapter(adapterReallocation);
                                        adapterReallocation.notifyDataSetChanged();
                                        Log.d("RealloationArraySize**", String.valueOf(arrayListReallocation.size()));
                                        if (txtMin.getText().toString().equals("1")) {
                                            btnNext.setVisibility(View.GONE);
                                            btnPrevious.setVisibility(View.GONE);
                                            if (Integer.parseInt(txtMax.getText().toString()) <= arrayListReallocation.size()) {
                                                btnNext.setVisibility(View.VISIBLE);
                                            } else {
                                                btnNext.setVisibility(View.GONE);

                                            }
                                        } else if (25 > arrayListReallocation.size()) {
                                            btnPrevious.setVisibility(View.VISIBLE);
                                            btnNext.setVisibility(View.GONE);
                                        } else {
                                            btnNext.setVisibility(View.VISIBLE);
                                            btnPrevious.setVisibility(View.VISIBLE);
                                        }

                                        // btnShow();

                                    } catch (JSONException e) {
                                        Toast.makeText(AllocationReport.this, "Errorcode-360 ReallocationReport getReallocationResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(AllocationReport.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
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
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllocationReport.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        }).show();
            }
        }catch (Exception e)
        {
            Toast.makeText(AllocationReport.this,"Errorcode-359 ReallocationReport getReallocation "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnShow()
    {
        String txtMinVal=txtMin.getText().toString();
        //String txtMaxVal=txtMax.getText().toString();
        Log.d("MinVal",txtMinVal);
        if(txtMinVal.equals("1"))
        {
            Log.d("Entered","MinVal1");
            btnPrevious.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
           // txtMin.setText((Integer.parseInt(txtMinVal)+25));
        }
        }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AllocationReport.this,Home.class);
        intent.putExtra("Activity","ReallocationReport");
        startActivity(intent);
    }
}

