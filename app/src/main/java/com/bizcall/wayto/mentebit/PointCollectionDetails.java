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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PointCollectionDetails extends AppCompatActivity {

    ProgressDialog dialog;
    UrlRequest urlRequest;
    String clienturl, clientid, counselorid;
    SharedPreferences sp;
    RecyclerView recyclerCoins;
    AdapterCoinsDetails adapterCoinsDetails;
    ArrayList<DataCoin> coinArrayList;
    LinearLayout linearCoin;
    TextView txtCoin;
    ImageView imgBack, imgCoin;
    String totalcoins, url;
    RequestQueue requestQueue;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_collection_details);
        try {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            requestQueue = Volley.newRequestQueue(PointCollectionDetails.this);
            recyclerCoins = findViewById(R.id.recyclerCoins);
            coinArrayList = new ArrayList<>();
            linearCoin = findViewById(R.id.linearCoinColumns);
            imgBack = findViewById(R.id.img_back);
            imgCoin = findViewById(R.id.imgCoin);
            txtCoin = findViewById(R.id.txtCoin);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clientid = sp.getString("ClientId", null);
            clienturl = sp.getString("ClientUrl", null);
            counselorid = sp.getString("Id", null);
            totalcoins = sp.getString("TotalCoin", null);
            counselorid = counselorid.replace(" ", "");
            if(CheckInternetSpeed.checkInternet(PointCollectionDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PointCollectionDetails.this);
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
            else if(CheckInternetSpeed.checkInternet(PointCollectionDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PointCollectionDetails.this);
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

                dialog = ProgressDialog.show(PointCollectionDetails.this, "", "Loading point collection details...", true);
                getPointCollection();
                txtCoin.setText(totalcoins);
            }
            // refreshWhenLoading();

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
            dialog.dismiss();
            Toast.makeText(PointCollectionDetails.this,"Got Exception in point details",Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshWhenLoading() {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if (dialog.isShowing()) {
                    Intent intent = new Intent(PointCollectionDetails.this, PointCollectionDetails.class);
                    // intent.putExtra("Activity",strActivity);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }

    public void getPointCollection() {
        url = clienturl + "?clientid=" + clientid + "&caseid=38&CounsellorId=" + counselorid;
            if(CheckServer.isServerReachable(PointCollectionDetails.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                if (linearCoin.getVisibility() == View.GONE) {
                                    linearCoin.setVisibility(View.VISIBLE);
                                } else {
                                    linearCoin.setVisibility(View.GONE);
                                }

                                Log.d("CoinResponse", response);
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log.d("Json",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String pointCollectId = jsonObject1.getString("nPointCollectId");
                                        String event = jsonObject1.getString("cEvent");
                                        String point = jsonObject1.getString("cPoint");
                                        String valid_from = jsonObject1.getString("Valid From");
                                        String valid_upto = jsonObject1.getString("Valid Upto");
                                        DataCoin dataCoin = new DataCoin(pointCollectId, event, point, valid_from, valid_upto);
                                        coinArrayList.add(dataCoin);
                                    }
                                    adapterCoinsDetails = new AdapterCoinsDetails(PointCollectionDetails.this, coinArrayList);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(PointCollectionDetails.this);
                                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerCoins.setLayoutManager(layoutManager);
                                    recyclerCoins.setAdapter(adapterCoinsDetails);
                                    adapterCoinsDetails.notifyDataSetChanged();
                                } catch (Exception e) {
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PointCollectionDetails.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(PointCollectionDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }
    }
}


