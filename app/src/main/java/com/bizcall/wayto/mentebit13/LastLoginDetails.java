package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LastLoginDetails extends AppCompatActivity {

    RecyclerView recyclerLastLogin;
    AdapterLastLogin adapterLastLogin;
    ArrayList<DataLastLogin> arrayListLastLogin;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    String url,clientUrl,clientId,counselorid;
    SharedPreferences sp;
    LinearLayout linearLastLogin;
    TextView txtNotFound;
    ImageView imgBack,imgRefresh;
    TableLayout tableLayoutLastLogin;
    Vibrator vibrator;
    RequestQueue requestQueue;
    long timeout;
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_last_login_details);
            requestQueue=Volley.newRequestQueue(LastLoginDetails.this);
            vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clientUrl = sp.getString("ClientUrl", null);
            clientId = sp.getString("ClientId", null);
            counselorid = sp.getString("Id", null);
            timeout = sp.getLong("TimeOut", 0);
            txtNotFound = findViewById(R.id.txtNotFound);
            linearLastLogin = findViewById(R.id.linearLastLogin);
            imgBack = findViewById(R.id.img_back);
            imgRefresh=findViewById(R.id.imgRefresh);

            tableLayoutLastLogin = findViewById(R.id.table_lastlogin);
            if(CheckInternetSpeed.checkInternet(LastLoginDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LastLoginDetails.this);
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
            else if(CheckInternetSpeed.checkInternet(LastLoginDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LastLoginDetails.this);
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

                dialog = ProgressDialog.show(LastLoginDetails.this, "", "Loading Last Login Report", true);
                url = clientUrl + "?clientid=" + clientId + "&caseid=304&CounselorID=" + counselorid;
                newThreadInitilization(dialog);
                getLastLoginReport();
            }
           // refreshWhenLoading();
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
                    Intent intent=new Intent(LastLoginDetails.this,LastLoginDetails.class);
                  //  intent.putExtra("ActivityName",activityName);
                    startActivity(intent);
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(LastLoginDetails.this, "Errorcode-396 LastLoginReport onCreate " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExceptionLastLogin", String.valueOf(e));
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
                                Toast.makeText(LastLoginDetails.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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

            Intent intent = new Intent(LastLoginDetails.this, Home.class);
            intent.putExtra("Activity", "LastLoginReport");
            startActivity(intent);
            finish();

    }

    private void getLastLoginReport() {
        try {
            if (CheckServer.isServerReachable(LastLoginDetails.this)) {
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
                                    adapterLastLogin = new AdapterLastLogin(LastLoginDetails.this, arrayListLastLogin);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LastLoginDetails.this);
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerLastLogin.setLayoutManager(linearLayoutManager);
                                    recyclerLastLogin.setAdapter(adapterLastLogin);
                                    adapterLastLogin.notifyDataSetChanged();

                                } catch (Exception e) {
                                    Toast.makeText(LastLoginDetails.this, "Errorcode-398 LastLoginReport LastLoginResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LastLoginDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(LastLoginDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LastLoginDetails.this);
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
            Toast.makeText(LastLoginDetails.this, "Errorcode-397 LastLoginReport getLastLogindetails " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
