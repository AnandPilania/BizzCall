package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CounselorNotification extends AppCompatActivity {

   AdapterCounselorNotification adapterCounselorNotification;
   public  static  ArrayList<DataListCounselor> arrayListCounselorName;
   public static  Spinner spinnerCounselorlist;
   DataListCounselor dataListCounselor;
   String clienturl,clientid;
   ProgressDialog dialog;
   RequestQueue requestQueue;
   SharedPreferences sp;
   Button btnSend;
   public static ArrayList<StateVO> listVOs;
    ArrayList<DataListCounselor> arrayListCounselorN;
    EditText edtMessage;
    ImageView imgBack,imgRefresh;
    TextView txtSelectedName;
    Thread thread;
    long timeout;
    int cnt=0;
    StateVO stateVO;
    String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_counselor_notification);
            requestQueue = Volley.newRequestQueue(CounselorNotification.this);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clientid = sp.getString("ClientId", "");
            clienturl = sp.getString("ClientUrl", "");
            timeout = sp.getLong("TimeOut", 0);
            spinnerCounselorlist = findViewById(R.id.spinnerCounselorList);
            btnSend = findViewById(R.id.btnSend);
            edtMessage = findViewById(R.id.edtMessage);
            imgBack = findViewById(R.id.img_back);
            imgRefresh = findViewById(R.id.imgRefresh);
            txtSelectedName=findViewById(R.id.txtSelectedNames);

            arrayListCounselorN = new ArrayList<>();
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CounselorNotification.this, CounselorNotification.class);
                    // intent.putExtra("Activity", "CounsellorContact");
                    startActivity(intent);
                }
            });

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            dialog = ProgressDialog.show(CounselorNotification.this, "", "Getting counselor list", true);
            newThreadInitilization(dialog);
            getCounselorDetails();
            spinnerCounselorlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    for (int i1 = 0; i1 < listVOs.size(); i1++) {
                        if (listVOs.get(i1).isSelected()) {
                            arrayListCounselorN.add(arrayListCounselorName.get(i1));
                        }
                    }
                    for(int i1=0;i1<arrayListCounselorN.size();i1++)
                    {
                        name=name+","+arrayListCounselorN.get(i1).getCounselorname();
                        txtSelectedName.setText(name);
                        Log.d("Entered","");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < listVOs.size(); i++) {
                        if (listVOs.get(i).isSelected()) {
                            arrayListCounselorN.add(arrayListCounselorName.get(i));
                        }
                    }
                    for (int i = 0; i < arrayListCounselorN.size(); i++) {
                        String msg = edtMessage.getText().toString();
                        if (msg.length() == 0) {
                            edtMessage.setError("Invalid message");
                        } else {
                            setNotification(msg, "1", arrayListCounselorN.get(i).getCounselorid());
                        }
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(CounselorNotification.this,"Errorcode-546 CounselorNotification oncreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }
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
                                Toast.makeText(CounselorNotification.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
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
        //super.onBackPressed();
        Intent intent = new Intent(CounselorNotification.this, Home.class);
        intent.putExtra("Activity", "CounselorNotification");
        startActivity(intent);
        finish();
    }

    public void setNotification(String strNotification, String strSrNo, String strCounselorID)
    {
        try{
           String url=clienturl+"?clientid=" + clientid + "&caseid=62&SrNo=" + strSrNo +  "&CounselorId=" + strCounselorID +"&Notification=" + strNotification;
            Log.d("NotificationUrl", url);
            if(CheckInternet.checkInternet(CounselorNotification.this))
            {
                if(CheckServer.isServerReachable(CounselorNotification.this))
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("*******", response.toString());
                                    try {
                                        dialog.dismiss();
                                        Log.d("NotificationResponse", response+" "+cnt++);

                                        if (response.contains("Data inserted successfully"))
                                        {

                                            Toast.makeText(CounselorNotification.this, "Message sent", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(CounselorNotification.this, "Noification not sent", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(CounselorNotification.this,"Errorcode-548 CounselorNotification NotificationResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    if (error.networkResponse != null||error instanceof TimeoutError ||error instanceof NoConnectionError ||error instanceof AuthFailureError ||error instanceof ServerError ||error instanceof NetworkError ||error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounselorNotification.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(CounselorNotification.this,"Network issue",Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                }else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounselorNotification.this);
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
                } }else {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounselorNotification.this);
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
            Toast.makeText(CounselorNotification.this,"Errorcode-547 CounselorNotification setNotification "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcSetNotifjcation", String.valueOf(e));
        }

    }
    public void getCounselorDetails()
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid=30";
            Log.d("CounselorUrl", url);
            arrayListCounselorName=new ArrayList<>();
            if(CheckInternet.checkInternet(CounselorNotification.this))
            {
                if(CheckServer.isServerReachable(CounselorNotification.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    dataListCounselor=new DataListCounselor();
                                    dataListCounselor.setCounselorname("Select All");
                                    arrayListCounselorName.add(0,dataListCounselor);
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

                                           // arrayListCounselorName.set(0,dataListCounselor).setCounselorname("Select All");
                                            arrayListCounselorName.add(dataListCounselor);
                                        }
                                        listVOs = new ArrayList<>();

                                       // listVOs.set(0,stateVO).setTitle("Select All");

                                       // stateVO.setTitle(arrayListCounselorName.get(0).getCounselorname());
                                        for (int i = 0; i < arrayListCounselorName.size(); i++) {
                                            StateVO stateVO = new StateVO();

                                            stateVO.setTitle(arrayListCounselorName.get(i).getCounselorname());
                                            stateVO.setSelected(false);
                                            listVOs.add(stateVO);
                                        }


                                         adapterCounselorNotification = new AdapterCounselorNotification(CounselorNotification.this, 0,
                                                listVOs);
                                        spinnerCounselorlist.setAdapter(adapterCounselorNotification);

                                    } catch (JSONException e) {
                                        Toast.makeText(CounselorNotification.this, "Errorcode-189 CounselorContact counselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounselorNotification.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(CounselorNotification.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounselorNotification.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounselorNotification.this);
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
            Toast.makeText(CounselorNotification.this,"Errorcode-549 CounselorNotification getCounselorList "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }
}
