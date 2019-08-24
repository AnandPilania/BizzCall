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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityLastLoginDetails extends AppCompatActivity {

    RecyclerView recyclerLastLogin;
    AdapterLastLogin adapterLastLogin;
    ArrayList<DataLastLogin> arrayListLastLogin;
    ProgressDialog dialog;
    String url,clientUrl,clientId,counselorid;
    SharedPreferences sp;
    LinearLayout linearLastLogin;
    TextView txtNotFound;
    ImageView imgBack,imgRefresh;
    TableLayout tableLayoutLastLogin;
    Vibrator vibrator;
    RequestQueue requestQueue;
    ArrayList<String> arrayListCounselorId,arrayListCounselorName;
    Spinner spinnerCounselor;
    LinearLayout linearUnderCounselorName;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_last_login_details);
            requestQueue= Volley.newRequestQueue(ActivityLastLoginDetails.this);
            vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clientUrl = sp.getString("ClientUrl", null);
            clientId = sp.getString("ClientId", null);
            counselorid = "3"/*sp.getString("Id", null)*/;
            txtNotFound = findViewById(R.id.txtNotFound);
            linearLastLogin = findViewById(R.id.linearLastLogin);
            spinnerCounselor = findViewById(R.id.spinner_counselor);
            linearUnderCounselorName=findViewById(R.id.linearUnderCounselor);
            imgBack = findViewById(R.id.img_back);
            imgRefresh=findViewById(R.id.imgRefresh);
            tableLayoutLastLogin = findViewById(R.id.table_lastlogin);
            if(CheckInternetSpeed.checkInternet(ActivityLastLoginDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityLastLoginDetails.this);
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
            else if(CheckInternetSpeed.checkInternet(ActivityLastLoginDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityLastLoginDetails.this);
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

                dialog = ProgressDialog.show(ActivityLastLoginDetails.this, "", "Loading Counselor list", true);
                getCounselorList();

            }
            spinnerCounselor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    counselorid = arrayListCounselorId.get(position);
                    editor = sp.edit();
                    editor.putString("Id", counselorid);
                    editor.commit();

                    if(counselorid.equals("0")) {
                        linearUnderCounselorName.setVisibility(View.GONE);
                        Toast.makeText(ActivityLastLoginDetails.this,"Select counselor",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        linearUnderCounselorName.setVisibility(View.VISIBLE);
                        url = clientUrl + "?clientid=" + clientId + "&caseid=304&CounselorID=" + counselorid;
                        getLastLoginReport();
                    }
                    //loadAllReport();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
           // refreshWhenLoading();
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                    Animatoo.animateSlideRight(ActivityLastLoginDetails.this);
                }
            });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ActivityLastLoginDetails.this, ActivityLastLoginDetails.class);
                  //  intent.putExtra("ActivityName",activityName);
                    startActivity(intent);
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(ActivityLastLoginDetails.this, "Errorcode-396 LastLoginReport onCreate " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExceptionLastLogin", String.valueOf(e));
        }
    }

    public void getCounselorList() {
        try {
            String CounselorUrl = clientUrl + "?clientid=" + clientId + "&caseid=30";
            Log.d("CounselorUrl", CounselorUrl);

            if (CheckInternet.checkInternet(ActivityLastLoginDetails.this)) {
                if (CheckServer.isServerReachable(ActivityLastLoginDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, CounselorUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    arrayListCounselorId = new ArrayList<>();
                                    arrayListCounselorName = new ArrayList<>();

                                    arrayListCounselorName.add(0, "Select Counselor");
                                    arrayListCounselorId.add(0,"0");
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
                                        ArrayAdapter<String> dataAdapterState = new ArrayAdapter(ActivityLastLoginDetails.this, R.layout.spinner_item1, arrayListCounselorName);
                                        spinnerCounselor.setAdapter(dataAdapterState);
                                        dataAdapterState.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        Toast.makeText(ActivityLastLoginDetails.this, "Errorcode-189 CounselorContact counselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityLastLoginDetails.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ActivityLastLoginDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityLastLoginDetails.this);
                    alertDialogBuilder.setTitle("Server Down!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    CheckServer.temp=0;

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityLastLoginDetails.this);
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
            Toast.makeText(ActivityLastLoginDetails.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshWhenLoading()
    {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if(dialog.isShowing()) {
                    Intent intent = new Intent(ActivityLastLoginDetails.this, ActivityLastLoginDetails.class);
                   // intent.putExtra("ActivityName",actname);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ActivityLastLoginDetails.this,ActivityHome.class);
        intent.putExtra("Activity","LastLoginReport");
        startActivity(intent);
        finish();
        super.onBackPressed();
        Animatoo.animateSlideRight(ActivityLastLoginDetails.this);
    }

    private void getLastLoginReport() {
        try {
            if (CheckServer.isServerReachable(ActivityLastLoginDetails.this)) {
                Log.d("LastLoginUrl", url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    dialog.dismiss();
                                    arrayListLastLogin = new ArrayList<>();
                                    Log.d("ResponseLastLogin", response);
                                    //  String res= String.valueOf(response);
                                    if (response.contains("[]")) {
                                        txtNotFound.setVisibility(View.VISIBLE);
                                        linearLastLogin.setVisibility(View.GONE);
                                    } else {
                                        txtNotFound.setVisibility(View.GONE);
                                        linearLastLogin.setVisibility(View.VISIBLE);

                                    }
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("table data success", String.valueOf(response));
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String loginid = jsonObject1.getString("LoginId");

                                        String counselorName1 = jsonObject1.getString("CounselorName");
                                        String logindate = jsonObject1.getString("Login date");
                                        String ipadrs = jsonObject1.getString("Ip Address");

                                        // Log.d("data fetch table", CallDate + " " + TotalCall + " ");
                                        DataLastLogin dataLastLogin = new DataLastLogin(loginid, counselorName1, logindate, ipadrs);
                                        arrayListLastLogin.add(dataLastLogin);
                                    }
                                    recyclerLastLogin = findViewById(R.id.recyclerLastLogin);
                                    adapterLastLogin = new AdapterLastLogin(ActivityLastLoginDetails.this, arrayListLastLogin);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityLastLoginDetails.this);
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerLastLogin.setLayoutManager(linearLayoutManager);
                                    recyclerLastLogin.setAdapter(adapterLastLogin);
                                    adapterLastLogin.notifyDataSetChanged();

                                } catch (Exception e) {
                                    Toast.makeText(ActivityLastLoginDetails.this, "Errorcode-398 LastLoginReport LastLoginResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;
                                //    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityLastLoginDetails.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ActivityLastLoginDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityLastLoginDetails.this);
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
        }catch (Exception e)
        {
            Toast.makeText(ActivityLastLoginDetails.this, "Errorcode-397 LastLoginReport getLastLogindetails " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
