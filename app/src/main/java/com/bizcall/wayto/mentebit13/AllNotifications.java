package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AllNotifications extends AppCompatActivity {

   RecyclerView recyclerNotifications;
   AdapterNotification adapterNotification;
   ArrayList<DataNotification> arrayListNotification;
   DataNotification dataNotification;
   ProgressDialog dialog;
   UrlRequest urlRequest;
   String clienturl,clientid,counselorid,strMin,strMax;
   SharedPreferences sp;
   TextView txtNoNotifications,txtMin,txtMax,txtDisplayInfo;
   ImageView imgBack,imgRefresh;
   Button btnLoadMore,btnLoadPrevious;
   Vibrator vibrator;
   String url;
   RequestQueue requestQueue;
   long timeout;
   Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_all_notifications);
            requestQueue=Volley.newRequestQueue(AllNotifications.this);
            txtNoNotifications = findViewById(R.id.txtNoNotifications);
            txtMin = findViewById(R.id.txtMin);
            txtMax = findViewById(R.id.txtMax);
            txtDisplayInfo = findViewById(R.id.txtDisplayInfo);
            btnLoadMore = findViewById(R.id.btnNext);
            btnLoadPrevious = findViewById(R.id.btnPrevious);
            imgBack = findViewById(R.id.img_back);
            imgRefresh=findViewById(R.id.imgRefresh);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clientid = sp.getString("ClientId", null);
            clienturl = sp.getString("ClientUrl", null);
            counselorid = sp.getString("Id", null);
            timeout = sp.getLong("TimeOut", 0);
            counselorid = counselorid.replaceAll(" ", "");
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(AllNotifications.this,AllNotifications.class);
                    //intent.putExtra("ActivityName",activityName);
                    startActivity(intent);
                }
            });

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
            strMin = "1";
            strMax = "25";
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
            if(CheckInternetSpeed.checkInternet(AllNotifications.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllNotifications.this);
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
            else if(CheckInternetSpeed.checkInternet(AllNotifications.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllNotifications.this);
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
                dialog = ProgressDialog.show(AllNotifications.this, "", "Loading Notifications", true);
               newThreadInitilization(dialog);
                getNotification(counselorid, txtMin.getText().toString(), txtMax.getText().toString());
            }
           // refreshWhenLoading();
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnNextClicked();
                }
            });
            btnLoadPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                btnPreviousClicked();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(AllNotifications.this,"Errorcode-381 AllNotifications onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("NotificationException", String.valueOf(e));
        }
    }//onCreate
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
                                Toast.makeText(AllNotifications.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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
    public void btnNextClicked(){
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) + 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) + 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
            if (CheckInternetSpeed.checkInternet(AllNotifications.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllNotifications.this);
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
            } else if (CheckInternetSpeed.checkInternet(AllNotifications.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllNotifications.this);
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
                dialog = ProgressDialog.show(AllNotifications.this, "", "Loading Notifications", true);
               newThreadInitilization(dialog);
                getNotification(counselorid, txtMin.getText().toString(), txtMax.getText().toString());
            }
        }catch (Exception e)
        {
            Toast.makeText(AllNotifications.this,"Errorcode-382 AllNotifications btnNextClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnPreviousClicked() {
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) - 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) - 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            if (CheckInternetSpeed.checkInternet(AllNotifications.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllNotifications.this);
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
            } else if (CheckInternetSpeed.checkInternet(AllNotifications.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllNotifications.this);
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

                dialog = ProgressDialog.show(AllNotifications.this, "", "Loading Notifications", true);
                newThreadInitilization(dialog);
                getNotification(counselorid, txtMin.getText().toString(), txtMax.getText().toString());
            }
            //refreshWhenLoading();
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
        }catch (Exception e)
        {
            Toast.makeText(AllNotifications.this,"Errorcode-383 AllNotifications btnPreviousClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void getNotification(String strCid,String minVal,String maxVal) {
        try {
            if(CheckServer.isServerReachable(AllNotifications.this)) {
                //dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);
                arrayListNotification = new ArrayList<>();
                url = clienturl + "?clientid=" + clientid + "&caseid=64&CounselorID=" + strCid + "&MinVal=" + minVal + "&MaxVal=" + maxVal;
                Log.d("NotificationUrl", url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("*******", response.toString());
                                try {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Log.d("getNotificationRes", response);
                                    int mval = Integer.parseInt(txtMax.getText().toString());

                                    if (response.contains("[]")) {
                                        txtNoNotifications.setVisibility(View.VISIBLE);

                                    } else {
                                        txtNoNotifications.setVisibility(View.GONE);
                                    }

                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log.d("Json",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String notification = jsonObject1.getString("cNotification");
                                        String srno = jsonObject1.getString("nSrNo");
                                        String nid = jsonObject1.getString("nNotificationID");
                                        DataNotification dataNotification = new DataNotification(notification, srno, nid);
                                        arrayListNotification.add(dataNotification);
                                    }
                                    recyclerNotifications = findViewById(R.id.recyclerNotification);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(AllNotifications.this);
                                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerNotifications.setLayoutManager(layoutManager);
                                    adapterNotification = new AdapterNotification(AllNotifications.this, arrayListNotification);
                                    recyclerNotifications.setAdapter(adapterNotification);
                                    adapterNotification.notifyDataSetChanged();
                                    Log.d("ArraylistNotification", String.valueOf(arrayListNotification.size()));
                                  /*  if (txtMin.getText().toString().equals("1")) {
                                        btnLoadPrevious.setVisibility(View.GONE);
                                        btnLoadMore.setVisibility(View.GONE);
                                        if (25 >arrayListNotification.size()) {
                                            btnLoadMore.setVisibility(View.VISIBLE);
                                        } else {
                                            btnLoadMore.setVisibility(View.GONE);
                                        }
                                    } else if (25 > arrayListNotification.size()) {
                                        btnLoadPrevious.setVisibility(View.VISIBLE);
                                        btnLoadMore.setVisibility(View.GONE);
                                    } else {
                                        btnLoadMore.setVisibility(View.VISIBLE);
                                        btnLoadPrevious.setVisibility(View.VISIBLE);
                                    }*/
                                    if (txtMin.getText().toString().equals("1")) {
                                        btnLoadMore.setVisibility(View.GONE);
                                        btnLoadPrevious.setVisibility(View.GONE);
                                        if (Integer.parseInt(txtMax.getText().toString()) <= arrayListNotification.size()) {
                                            btnLoadMore.setVisibility(View.VISIBLE);
                                        } else {
                                            btnLoadMore.setVisibility(View.GONE);

                                        }
                                    } else if (25 > arrayListNotification.size()) {
                                        btnLoadPrevious.setVisibility(View.VISIBLE);
                                        btnLoadMore.setVisibility(View.GONE);
                                    } else {
                                        btnLoadMore.setVisibility(View.VISIBLE);
                                        btnLoadPrevious.setVisibility(View.VISIBLE);
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(AllNotifications.this, "Errorcode-385 AllNotifications NotificationsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllNotifications.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")
                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(AllNotifications.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AllNotifications.this);
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
            Toast.makeText(AllNotifications.this,"Errorcode-384 AllNotifications getNotifications "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {

            Intent intent = new Intent(AllNotifications.this, Home.class);
            intent.putExtra("Activity", "AllNotifications");
            //intent.putExtra("MainMenu",MainMenuselected);
            startActivity(intent);
        finish();

    }
}
