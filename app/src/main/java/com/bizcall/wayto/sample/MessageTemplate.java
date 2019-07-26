package com.bizcall.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Timer;
import java.util.TimerTask;

public class MessageTemplate extends AppCompatActivity {

    EditText edtMeggase,edtMailSubject;
    Button btnSubmit;
    SharedPreferences sp;
    String counselorid, msg, clientid,clienturl;
    long timeout;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    int flag = 0;
    ImageView imageBack,imgRefresh;
    Vibrator vibrator;
    String url,activity;
    RequestQueue requestQueue;
    TextView txtActivity,txtTitle,txtSubjectTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_template);
        try{
            requestQueue=Volley.newRequestQueue(MessageTemplate.this);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            edtMeggase = findViewById(R.id.edtMessage1);
            edtMailSubject=findViewById(R.id.edtSubject);
            btnSubmit = findViewById(R.id.btnSubmitMsg);
            imageBack = findViewById(R.id.img_back);
            imgRefresh=findViewById(R.id.imgRefresh);
            txtActivity=findViewById(R.id.txtActivityName);
            txtTitle=findViewById(R.id.txtTitle);
            txtSubjectTitle=findViewById(R.id.txtTitleSubject);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            counselorid = sp.getString("Id", null);
            counselorid=counselorid.replaceAll(" ","");
            clientid = sp.getString("ClientId", null);
            clienturl=sp.getString("ClientUrl",clienturl);
            timeout=sp.getLong("TimeOut",0);

            activity=getIntent().getStringExtra("Activity");
            txtActivity.setText(activity+" "+"Template");
            txtTitle.setText(activity);
            if(activity.contains("Mail")) {
                txtSubjectTitle.setVisibility(View.VISIBLE);
                edtMeggase.setHint("Enter mail body here");
                edtMailSubject.setVisibility(View.VISIBLE);
                edtMailSubject.setHint("Enter mail subject here");
                txtTitle.setText(activity+" "+"Body");
            }
       /* SpannableString content = new SpannableString(mbl);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        viewHolder.txtMobile.setText(content);*/
            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
                imgRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MessageTemplate.this,MessageTemplate.class);
                        //intent.putExtra("Activity",activityName);
                        startActivity(intent);
                    }
                });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 0;
                    msg = edtMeggase.getText().toString();
                    msg=msg.replaceAll("'","");
                    if (msg.length() == 0) {
                        edtMeggase.setError("Please enter message");
                        flag = 1;
                    }
                    if (flag == 0) {
                        if(activity.contains("Message")) {
                            if(CheckInternetSpeed.checkInternet(MessageTemplate.this).contains("0")) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
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
                            else if(CheckInternetSpeed.checkInternet(MessageTemplate.this).contains("1")) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
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
                                dialog = ProgressDialog.show(MessageTemplate.this, "", "Inserting message in template", true);
                                insertMsgTemplate();
                            }
                        }
                      //  refreshWhenLoading();
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(MessageTemplate.this,"Got exception, can't load",Toast.LENGTH_SHORT).show();
            Log.d("MsgTemplateException", String.valueOf(e));
        }
    }
    public void refreshWhenLoading()
    {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if(dialog.isShowing()) {
                    Intent intent = new Intent(MessageTemplate.this, MessageTemplate.class);
                   // intent.putExtra("Activity",strActivity);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }

    public void insertMsgTemplate() {
        url=clienturl+"?clientid=" + clientid + "&caseid=8&cSmsText=" + msg + "&IsActive=1&nCounselorID=" + counselorid;
        Log.d("MsgTemplateUrl",url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("*******", response.toString());
                        try {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Log.d("TemplateMsgResponse", response);
                            if (response.contains("Data inserted successfully")) {
                                edtMeggase.setText("");
                                Toast.makeText(MessageTemplate.this, "Template saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MessageTemplate.this, "Template not saved", Toast.LENGTH_SHORT).show();
                            }

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
                        if (error.networkResponse != null||error instanceof TimeoutError ||error instanceof NoConnectionError ||error instanceof AuthFailureError ||error instanceof ServerError ||error instanceof NetworkError ||error instanceof ParseError) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageTemplate.this);
                            alertDialogBuilder.setTitle("Server Error!!!")


                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).show();
                            dialog.dismiss();
                            Toast.makeText(MessageTemplate.this,"Server Error",Toast.LENGTH_SHORT).show();
                            // showCustomPopupMenu();
                            Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                        }
                    }
                });
        requestQueue.add(stringRequest);
        }

    @Override
    public void onBackPressed() {
        try{
        Intent intent=new Intent(MessageTemplate.this,Home.class);
        intent.putExtra("Activity","MessageTemplate");
        startActivity(intent);
        super.onBackPressed();
        }
        catch (Exception e)
        {
            Log.d("Exception", String.valueOf(e));
        }
       /* Intent intent=new Intent(MessageTemplate.this,Home.class);
        startActivity(intent);*/
    }
}
