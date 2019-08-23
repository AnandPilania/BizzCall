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
import android.widget.HorizontalScrollView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FirstCallReport extends AppCompatActivity {

    RecyclerView recyclerViewFisrtCall;
    ArrayList<DataFirstCallReport> arrayListFirstCall;
    AdapterFirstCallReport adapterFirstCallReport;
    UrlRequest urlRequest;
    String clienturl,clientid;
    TextView edtMobileno;
    Button btnSubmit;
    String mobile;
    SharedPreferences sp;
    TableLayout tableLayout;
    ProgressDialog dialog;
    ImageView imgBack,imgRefresh;
    Vibrator vibrator;
    String url;
    RequestQueue requestQueue;
    TextView txtNoCallMade;
    HorizontalScrollView horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_first_call_report);
            requestQueue=Volley.newRequestQueue(FirstCallReport.this);
            vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
            horizontalScrollView=findViewById(R.id.horizontalScrollview);
            txtNoCallMade=findViewById(R.id.txtNoCallMadeMsg);
            edtMobileno = findViewById(R.id.edtMobileNo);
            btnSubmit = findViewById(R.id.btnSubmit);
            tableLayout = findViewById(R.id.table_firstcalltitle);
            imgBack = findViewById(R.id.img_back);
            imgRefresh=findViewById(R.id.imgRefresh);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clienturl = sp.getString("ClientUrl", null);
            clientid = sp.getString("ClientId", null);
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
                    Intent intent=new Intent(FirstCallReport.this,FirstCallReport.class);
                   // intent.putExtra("Activity",activityName);
                    startActivity(intent);
                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobile = edtMobileno.getText().toString();
                    if (mobile.length() < 9) {
                        edtMobileno.setError("Invalid number");
                    } else {
                        if(CheckInternetSpeed.checkInternet(FirstCallReport.this).contains("0")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FirstCallReport.this);
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
                        else if(CheckInternetSpeed.checkInternet(FirstCallReport.this).contains("1")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FirstCallReport.this);
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

                            dialog = ProgressDialog.show(FirstCallReport.this, "", "Getting call report ", true);
                            getFirstCall(mobile);
                        }
                      //  refreshWhenLoading();
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(FirstCallReport.this,"Errorcode-361 FirstCallReport onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("FirstCallException", String.valueOf(e));
        }

    }
    public void refreshWhenLoading()
    {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if(dialog.isShowing()) {
                    Intent intent = new Intent(FirstCallReport.this, FirstCallReport.class);
                   // intent.putExtra("Activity",strActivity);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(FirstCallReport.this,Home.class);
        intent.putExtra("Activity","First call");
        startActivity(intent);
       // super.onBackPressed();
    }

    public void getFirstCall(String mbl) {
        try {
            arrayListFirstCall = new ArrayList<>();
            url = clienturl + "?clientid=" + clientid + "&caseid=71&PhoneNumber=" + mbl;
            Log.d("FirstCallUrl", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            tableLayout.setVisibility(View.VISIBLE);
                            Log.d("FirsrCallResponse1", response);
                            try {
                                if(response.contains("[]"))
                                {
                                    horizontalScrollView.setVisibility(View.GONE);
                                    txtNoCallMade.setVisibility(View.VISIBLE);
                                }
                                else {
                                    horizontalScrollView.setVisibility(View.VISIBLE);
                                    txtNoCallMade.setVisibility(View.GONE);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String cname = jsonObject1.getString("cCOunselorName");
                                        String callid = jsonObject1.getString("nCallid");
                                        String calldate = jsonObject1.getString("Call date");
                                        String duration = jsonObject1.getString("cCallDuration");
                                        DataFirstCallReport dataFirstCallReport = new DataFirstCallReport(callid, cname, calldate, duration);
                                        arrayListFirstCall.add(dataFirstCallReport);
                                        // Log.d("Json11111",arrayList1.toString());
                                    }
                                    recyclerViewFisrtCall = findViewById(R.id.recyclerFirstCall);
                                    adapterFirstCallReport = new AdapterFirstCallReport(FirstCallReport.this, arrayListFirstCall);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(FirstCallReport.this);
                                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerViewFisrtCall.setLayoutManager(layoutManager);
                                    recyclerViewFisrtCall.setAdapter(adapterFirstCallReport);
                                    adapterFirstCallReport.notifyDataSetChanged();
                                }

                            } catch (JSONException e) {
                                Toast.makeText(FirstCallReport.this,"Errorcode-363 FirstCallReport getFirstCallResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FirstCallReport.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(FirstCallReport.this, "Server Error", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }
                        }
                    });
            requestQueue.add(stringRequest);
        }catch (Exception e)
        {
            Toast.makeText(FirstCallReport.this,"Errorcode-362 FirstCallReport getFirstCall "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
}
