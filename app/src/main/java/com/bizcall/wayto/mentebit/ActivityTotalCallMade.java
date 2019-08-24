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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityTotalCallMade extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterTotalCallMade adapterTotalCallMade;
    ArrayList<DataTotalCallMade> arrayList;
    ProgressDialog dialog;
    TextView txtTotalCallNo;
    ImageView imgBack, imgCoin, imgRefresh;
    SharedPreferences sp;
    String counselorid, clientid, clienturl, totalcoins;
    TextView txtMsg;
    LinearLayout linearLayout;
    TextView txtCoin, txtDiamond;
    Vibrator vibrator;
    String url;
    RequestQueue requestQueue;
    Spinner spinnerCounselor;
    LinearLayout linearUnderCounselorName;
    ArrayList<String> arrayListCounselorId, arrayListCounselorName;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_call_made);
        try {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            requestQueue = Volley.newRequestQueue(ActivityTotalCallMade.this);
            txtCoin = findViewById(R.id.txtCoin);
            //  txtDiamond=findViewById(R.id.txtDiamond);
            txtTotalCallNo = findViewById(R.id.txtTotalCallNo);
            imgBack = findViewById(R.id.img_back);
            imgRefresh = findViewById(R.id.imgRefresh);
            txtMsg = findViewById(R.id.txtNoCallMadeMsg);
            linearLayout = findViewById(R.id.linearCallColumns);
            spinnerCounselor = findViewById(R.id.spinner_counselor);
            linearUnderCounselorName = findViewById(R.id.linearUnderCounselor);
            imgCoin = findViewById(R.id.imgCoin);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                    Animatoo.animateSlideRight(ActivityTotalCallMade.this);
                }
            });
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            // counselorid = "3"/*sp.getString("Id", null)*/;
            //counselorid = counselorid.replaceAll(" ", "");
            clientid = sp.getString("ClientId", null);
            clienturl = sp.getString("ClientUrl", null);
            // totalcoins = sp.getString("TotalCoin", null);
            Log.d("CID", clientid);

            if (CheckInternetSpeed.checkInternet(ActivityTotalCallMade.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
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
            } else if (CheckInternetSpeed.checkInternet(ActivityTotalCallMade.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
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
                dialog = ProgressDialog.show(ActivityTotalCallMade.this, "", "Loading Counselor List", false, true);
                getCounselorList();
                //getTotalCallNo();
            }
            spinnerCounselor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    counselorid = arrayListCounselorId.get(position);
                    editor = sp.edit();
                    editor.putString("Id", counselorid);
                    editor.commit();

                    if (counselorid.equals("0")) {
                        linearUnderCounselorName.setVisibility(View.GONE);
                        Toast.makeText(ActivityTotalCallMade.this, "Select counselor", Toast.LENGTH_SHORT).show();
                    } else {
                        linearUnderCounselorName.setVisibility(View.VISIBLE);
                        dialog = ProgressDialog.show(ActivityTotalCallMade.this, "", "Loading Total Call No", true);
                        getTotalCallNo();
                    }
                    //loadAllReport();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // refreshWhenLoading();
        /*txtCoin.setText(totalcoins);
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(TotalCallMade.this,TotalCallMade.class);
                    //intent.putExtra("Activity",activityName);
                    startActivity(intent);
                }
            });
        imgCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                startActivity(new Intent(TotalCallMade.this,PointCollectionDetails.class));
            }
        });*/
        } catch (Exception e) {
            Toast.makeText(ActivityTotalCallMade.this, "Errorcode-350 TotalCallMade onCreate " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("TotalCallsException", String.valueOf(e));
        }
    }

    public void getCounselorList() {
        try {
            String CounselorUrl = clienturl + "?clientid=" + clientid + "&caseid=30";
            Log.d("CounselorUrl", CounselorUrl);

            if (CheckInternet.checkInternet(ActivityTotalCallMade.this)) {
                if (CheckServer.isServerReachable(ActivityTotalCallMade.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, CounselorUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    arrayListCounselorId = new ArrayList<>();
                                    arrayListCounselorName = new ArrayList<>();

                                    arrayListCounselorName.add(0, "Select Counselor");
                                    arrayListCounselorId.add(0, "0");
                                    Log.d("CounselorResponse1", response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String id = String.valueOf(jsonObject1.getInt("cCounselorID"));
                                            String name = jsonObject1.getString("cCounselorName");
                                            arrayListCounselorName.add(name);
                                            arrayListCounselorId.add(id);
                                        }
                                        ArrayAdapter<String> dataAdapterState = new ArrayAdapter(ActivityTotalCallMade.this, R.layout.spinner_item1, arrayListCounselorName);
                                        spinnerCounselor.setAdapter(dataAdapterState);
                                        dataAdapterState.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        Toast.makeText(ActivityTotalCallMade.this, "Errorcode-189 CounselorContact counselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ActivityTotalCallMade.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
                    alertDialogBuilder.setTitle("Server Down!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    CheckServer.temp = 0;

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
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
        } catch (Exception e) {
            Toast.makeText(ActivityTotalCallMade.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshWhenLoading() {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if (dialog.isShowing()) {
                    Intent intent = new Intent(ActivityTotalCallMade.this, ActivityTotalCallMade.class);
                    // intent.putExtra("Activity",strActivity);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(ActivityTotalCallMade.this, ActivityHome.class);
            intent.putExtra("Activity", "TotalCallMade");
            startActivity(intent);
            finish();
            super.onBackPressed();
            Animatoo.animateSlideRight(ActivityTotalCallMade.this);
        } catch (Exception e) {
            Toast.makeText(ActivityTotalCallMade.this, "Errorcode-351 TotalCallMade onBackpressed " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }

    public void getTotalCallMade() {
        try {
            arrayList = new ArrayList<>();
            url = clienturl + "?clientid=" + clientid + "&caseid=15&CounsellorId=" + counselorid;
            Log.d("TotalCallUrl", url);
            if (CheckInternet.checkInternet(ActivityTotalCallMade.this)) {
                if (CheckServer.isServerReachable(ActivityTotalCallMade.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Log.d("TotalCallResponse", response);

                                    if (response.contains("[]")) {
                                        linearLayout.setVisibility(View.GONE);
                                        txtMsg.setVisibility(View.VISIBLE);
                                    } else {
                                        linearLayout.setVisibility(View.VISIBLE);
                                        txtMsg.setVisibility(View.GONE);
                                        try {

                                            JSONObject jsonObject = new JSONObject(response);
                                            // Log.d("Json",jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String srno = jsonObject1.getString("nSrNo");
                                                String duration = jsonObject1.getString("cCallDuration");
                                                String cdate = jsonObject1.getString("Call date");
                                                String filename = jsonObject1.getString("cFileName");
                                                DataTotalCallMade dataTotalCallMade = new DataTotalCallMade(srno, duration, cdate, filename);
                                                arrayList.add(dataTotalCallMade);
                                            }
                                            adapterTotalCallMade = new AdapterTotalCallMade(ActivityTotalCallMade.this, arrayList);
                                            recyclerView = findViewById(R.id.recycleTotalCallMade);
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                                            recyclerView.setLayoutManager(linearLayoutManager);
                                            recyclerView.setAdapter(adapterTotalCallMade);
                                            adapterTotalCallMade.notifyDataSetChanged();

                                            Log.d("Size**", String.valueOf(arrayList.size()));
                                        } catch (Exception e) {
                                            Toast.makeText(ActivityTotalCallMade.this, "Errorcode-353 TotalCallMade TotalCallMadeDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                            Log.d("Exception", String.valueOf(e));
                                        }
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ActivityTotalCallMade.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
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
        } catch (Exception e) {
            Toast.makeText(ActivityTotalCallMade.this, "Errorcode-352 TotalCallMade getTotalCallMadeDetails " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void getTotalCallNo() {
        // dialog = ProgressDialog.show(TotalCallMade.this, "Loading", "Please wait.....", false, true);
        try {
            arrayList = new ArrayList<>();
            url = clienturl + "?clientid=" + clientid + "&caseid=17&CounsellorId=" + counselorid;
            Log.d("CallNoUrl", url);
            if (CheckInternet.checkInternet(ActivityTotalCallMade.this)) {
                if (CheckServer.isServerReachable(ActivityTotalCallMade.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("*******", response.toString());
                                    try {
                           /* if (dialog.isShowing()) {
                                dialog.dismiss();
                            }*/
                                        Log.d("CallNoResponse", response);

                                        if (response.contains("[]")) {
                                            txtTotalCallNo.setText("0");
                                        } else {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                Log.d("Json", jsonObject.toString());
                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    String cnt = object.getString("");
                                                    txtTotalCallNo.setText(cnt);
                                                }
                                                getTotalCallMade();

                                            } catch (Exception e) {
                                                Log.d("Exception", String.valueOf(e));
                                            }
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(ActivityTotalCallMade.this, "Errorcode-355 TotalCallMade TotalCalledNoResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.d("Exception", String.valueOf(e));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;

                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ActivityTotalCallMade.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
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
                                    CheckServer.temp = 0;

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityTotalCallMade.this);
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
        } catch (Exception e) {
            Toast.makeText(ActivityTotalCallMade.this, "Errorcode-354 TotalCallMade getTotalCalledNo " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
