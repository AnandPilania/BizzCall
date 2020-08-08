package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.GridLayoutManager;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitySendNotify extends AppCompatActivity {
    AdapterSendNotification adapterSendNotification;
    public static ArrayList<DataListCounselor> arrayListCounselorName, arrayListNew;
    DataListCounselor dataListCounselor;
    public static int selectCheckbox = 0;
    String clienturl, clientid;
    ProgressDialog dialog;
    RequestQueue requestQueue;
    SharedPreferences sp;
    Button btnSend;
    AppCompatCheckBox checkbox;
    EditText edtMessage;
    TextView txtAllcounselors;
    ImageView imgBack, imgRefresh;
    Thread thread;
    long timeout;
    int cnt = 0;
    RecyclerView recyclerCounselors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notify);
        requestQueue = Volley.newRequestQueue(ActivitySendNotify.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        timeout = sp.getLong("TimeOut", 0);
        btnSend = findViewById(R.id.btnSend);
        edtMessage = findViewById(R.id.edtMessage);
        txtAllcounselors = findViewById(R.id.txt_selectallcouns);
        imgBack = findViewById(R.id.img_back);
        imgRefresh = findViewById(R.id.imgRefresh);
        checkbox = findViewById(R.id.chk_selectall);
        recyclerCounselors = findViewById(R.id.recycler_counselors);

        arrayListNew = new ArrayList<>();

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySendNotify.this, ActivitySendNotify.class);
                // intent.putExtra("Activity", "CounsellorContact");
                startActivity(intent);
            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkbox.isChecked()) {
                    selectCheckbox = 1;
                    txtAllcounselors.setVisibility(View.VISIBLE);
                    //recyclerCounselors.setAdapter(adapterSendNotification);
                   // adapterSendNotification.notifyDataSetChanged();
                } else {
                    selectCheckbox = 0;
                    txtAllcounselors.setVisibility(View.INVISIBLE);
                  //  recyclerCounselors.setAdapter(adapterSendNotification);
                   // adapterSendNotification.notifyDataSetChanged();
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dialog = ProgressDialog.show(ActivitySendNotify.this, "", "Getting counselor list", true);
        newThreadInitilization(dialog);
        getCounselorDetails();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectCheckbox == 1) {
                    Log.d("checkbox1", "" + arrayListCounselorName.size());
                    for (int i = 0; i < arrayListCounselorName.size(); i++) {
                        String msg = edtMessage.getText().toString();
                        if (msg.length() == 0) {
                            edtMessage.setError("Invalid message");
                        } else {
                            setNotification(msg, "1", arrayListCounselorName.get(i).getCounselorid());
                        }
                    }
                    arrayListNew.clear();
                    recyclerCounselors.setAdapter(adapterSendNotification);
                } else {
                    /*for (int i = 0; i < listVOs.size(); i++) {
                        if (listVOs.get(i).isSelected()) {
                            arrayListCounselorN.add(arrayListCounselorName.get(i));
                        }
                    }*/
                    if (arrayListNew.size() != 0) {
                        for (int i = 0; i < arrayListNew.size(); i++) {
                            String msg = edtMessage.getText().toString();
                            if (msg.length() == 0) {
                                edtMessage.setError("Invalid message");
                            } else {
                                setNotification(msg, "1", arrayListNew.get(i).getCounselorid());
                            }
                        }
                        arrayListNew.clear();
                        recyclerCounselors.setAdapter(adapterSendNotification);
                    } else {
                        Toast.makeText(ActivitySendNotify.this, "Select counselor first", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void newThreadInitilization(final ProgressDialog dialog1) {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    // dialog1.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(ActivitySendNotify.this, "Connection aborted", Toast.LENGTH_SHORT).show();
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
        super.onBackPressed();
        Intent intent = new Intent(ActivitySendNotify.this, Home.class);
        intent.putExtra("Activity", "CounselorNotification");
        startActivity(intent);
    }

    public void setNotification(String strNotification, String strSrNo, String strCounselorID) {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=62&SrNo=" + strSrNo + "&CounselorId=" + strCounselorID + "&Notification=" + "From Admin - " + strNotification;
            Log.d("NotificationUrl", url);
            if (CheckInternet.checkInternet(ActivitySendNotify.this)) {
                if (CheckServer.isServerReachable(ActivitySendNotify.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("*******", response.toString());
                                    try {
                                        dialog.dismiss();
                                        Log.d("NotificationResponse", response + " " + cnt++);
                                        if (response.contains("Data inserted successfully")) {
                                            edtMessage.setText(null);
                                            //Toast.makeText(ActivitySendNotify.this, "Message sent", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(ActivitySendNotify.this, "Noification not sent", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(ActivitySendNotify.this, "Errorcode-217 CounselorContact setNotification " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivitySendNotify.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ActivitySendNotify.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivitySendNotify.this);
                    alertDialogBuilder.setTitle("No Internet connection!!!")
                            .setMessage("Can't do further process")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivitySendNotify.this);
                alertDialogBuilder.setTitle("Network issue!!!!")
                        .setMessage("Try after some time!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        } catch (Exception e) {
            Toast.makeText(ActivitySendNotify.this, "Errorcode-216 CounselorContact setNotification " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcSetNotifjcation", String.valueOf(e));
        }
    }

    public void getCounselorDetails() {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=30";
            Log.d("CounselorUrl", url);
            arrayListCounselorName = new ArrayList<>();
            adapterSendNotification = new AdapterSendNotification(arrayListCounselorName, ActivitySendNotify.this);
            recyclerCounselors.setLayoutManager(new GridLayoutManager(ActivitySendNotify.this, 2));

            if (CheckInternet.checkInternet(ActivitySendNotify.this)) {
                if (CheckServer.isServerReachable(ActivitySendNotify.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    Log.d("CounselorResponse1", response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String id = String.valueOf(jsonObject1.getInt("cCounselorID"));
                                            String name = jsonObject1.getString("cCounselorName");

                                            dataListCounselor = new DataListCounselor(id, name);
                                            arrayListCounselorName.add(dataListCounselor);
                                            recyclerCounselors.setAdapter(adapterSendNotification);
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(ActivitySendNotify.this, "Errorcode-189 CounselorContact counselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivitySendNotify.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ActivitySendNotify.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivitySendNotify.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivitySendNotify.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }).show();

            }
        } catch (Exception e) {
            Toast.makeText(ActivitySendNotify.this, "Errorcode-188 CounselorContact getCounselorDetails " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }
}
