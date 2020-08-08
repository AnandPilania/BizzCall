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
import org.json.JSONObject;

import java.util.ArrayList;

public class StatusReport extends AppCompatActivity {

    UrlRequest urlRequest;
    String clientUrl, clientId, id1, actname, urlReportNo;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    TextView txtNoStatus, txtActivityName, txtDataRef;
    ProgressDialog dialog;
    ArrayList<DataStatusReport> arrayListStatus;
    ImageView imgBack,imgRefresh;
    RequestQueue requestQueue;
    Thread thread;
    long timeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_status_report);
            requestQueue = Volley.newRequestQueue(StatusReport.this);
            txtNoStatus = findViewById(R.id.txtNoStatus);
            recyclerView = findViewById(R.id.recyclerStatus);
            imgBack = findViewById(R.id.img_back);
            txtActivityName = findViewById(R.id.txtActivityName);
            txtDataRef = findViewById(R.id.txtRefName);
            imgRefresh=findViewById(R.id.imgRefresh);


            arrayListStatus = new ArrayList<>();
            actname = getIntent().getStringExtra("ActivityName");
            Log.d("StatusActivity??", actname);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            timeout=sp.getLong("TimeOut",0);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            if (actname.equals("Status Report")) {
                editor.putString("Act", "StatusReport");
                txtDataRef.setVisibility(View.GONE);
            } else {
                editor.putString("Act", "StatusReportDataRef");
                txtActivityName.setText("Status Report DataRefwise");
            }

            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(StatusReport.this,StatusReport.class);
                    intent.putExtra("ActivityName",actname);
                    startActivity(intent);
                }
            });
            editor.commit();
            clientUrl = sp.getString("ClientUrl", null);
            clientId = sp.getString("ClientId", null);
            id1 = sp.getString("Id", null);
            id1 = id1.replaceAll(" ", "");
            if (actname.equals("Status Report")) {
                urlReportNo = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=306";
            } else {
                urlReportNo = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=307";
            }
            if(CheckInternetSpeed.checkInternet(StatusReport.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(StatusReport.this);
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
            else if(CheckInternetSpeed.checkInternet(StatusReport.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(StatusReport.this);
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
                dialog = ProgressDialog.show(StatusReport.this, "", "Loading status report", true);
                newThreadInitilization(dialog);
                getStatusReport();
            }
            // refreshWhenLoading();
        } catch (Exception e) {
            Toast.makeText(StatusReport.this,"Errorcode-378 StatusReport onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("StatusReportException", String.valueOf(e));
        }
    }
    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(StatusReport.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {

            Intent intent = new Intent(StatusReport.this, Home.class);
            intent.putExtra("Activity", "StatusReport");
            startActivity(intent);
        finish();

    }

    public void getStatusReport() {
        try {
            if(CheckServer.isServerReachable(StatusReport.this)) {
                Log.d("StatusReportUrl", urlReportNo);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlReportNo,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    dialog.dismiss();
                                    String res = String.valueOf(response);
                                    if (res.contains("[]")) {
                                        txtNoStatus.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    } else {
                                        txtNoStatus.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }

                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("StatusReportResponse", String.valueOf(response));
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        int total_no = jsonObject1.getInt("Total No");
                                        if (!actname.equals("Status Report")) {
                                            String refname = jsonObject1.getString("DataRefName");
                                            String status = jsonObject1.getString("cStatus");
                                            DataStatusReport dataStatusReport = new DataStatusReport(refname, status, total_no);
                                            arrayListStatus.add(dataStatusReport);
                                        }
                                        // Log.d("data fetch table", CallDate + " " + TotalCall + " ");
                                        else {
                                            String status = jsonObject1.getString("Status");
                                            DataStatusReport sampleData = new DataStatusReport(status, total_no);
                                            arrayListStatus.add(sampleData);
                                        }
                                    }
                                    AdapterStatusReport adapterStatusReport = new AdapterStatusReport(arrayListStatus, getApplicationContext());
                                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(manager);
                                    recyclerView.setAdapter(adapterStatusReport);
                                    adapterStatusReport.notifyDataSetChanged();
                                } catch (Exception e) {
                                    Toast.makeText(StatusReport.this, "Errorcode-380 StatusReport StatusReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(StatusReport.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(StatusReport.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(StatusReport.this);
                alertDialogBuilder.setTitle("Network issue!!!!")
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
            Toast.makeText(StatusReport.this,"Errorcode-379 StatusReport getStatusReport "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
}