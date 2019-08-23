package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Timer;
import java.util.TimerTask;

public class MessageActivity extends AppCompatActivity {

   TextView txtCloseWindow,txtName;
   ImageView imgCloseWindow,imgClearText,imgBack;
   EditText edtMobile,edtMessage,edtName;
   Button btnSms,btnWhatsapp,btnWeb;
   Spinner spinnerTemplate;
   SharedPreferences sp;
   ProgressDialog dialog;
   UrlRequest urlRequest;
   String clienturl,clientid,selectedmbl,strCandidateName,smbl,sms,status,fileno;
   ArrayList<String> arraySms;
    RequestQueue requestQueue;
    int eventid;
    boolean isClicked;
    String msg,sr_no,id1,totalcount,smbl1,cid,activity,mobile;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        try{
            requestQueue=Volley.newRequestQueue(MessageActivity.this);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sp=getSharedPreferences("Settings",Context.MODE_PRIVATE);
        mobile=sp.getString("SelectedMobile",null);
        selectedmbl="+91"+mobile;
        clientid=sp.getString("ClientId",null);
        clienturl=sp.getString("ClientUrl",null);
        sr_no = sp.getString("SelectedSrNo", null);

        id1=sp.getString("Id",null);
        id1=id1.replaceAll(" ","");
        strCandidateName=sp.getString("CName",null);
        activity=getIntent().getStringExtra("Activity");
        Log.d("Cname",strCandidateName);
      //  txtCloseWindow = findViewById(R.id.txtCloseWindow);
        //imgCloseWindow =findViewById(R.id.imgCloseWindow);
        edtMobile = findViewById(R.id.edtMobileNo);
        edtName = findViewById(R.id.edtUserName1);
        edtMessage = findViewById(R.id.edtMessage1);
        spinnerTemplate =findViewById(R.id.spinnerMsgTemplate);
        btnSms =findViewById(R.id.btnSMS);
        imgBack=findViewById(R.id.img_back);
        imgClearText =findViewById(R.id.imgClearMsg);
        btnWhatsapp = findViewById(R.id.btnWhatsapp);
        btnWeb=findViewById(R.id.btnWeb);

        //int pos = viewHolder.getAdapterPosition();
        // DataCounselor dataCounselo = arrayList.get(pos);
        //String mobile,cname;
        // mobile=dataCounselo.getMobile();
        //  Log.d("Mbl",mobile);
        // cname=dataCounselo.getCname();
        //  Log.d("cname",cname);
         cid = sp.getString("Id", null);
        Log.d("CID", cid);
        cid = cid.replace(" ", "");
            if(activity.contains("Summary"))
            {
               // fileno=sp.getString("FileNo",null);
                msg="Your File No:"+sr_no+" is generated with us.Kindley download Android Application to proceed further."+"\n"
                        +"Your login details are"+"\n"+"Client ID:WA11"+"\n"+"File No:"+sr_no+"\n"+"Mobile No:"+mobile;
                edtMessage.setText(msg);
            }
        edtMobile.setText(selectedmbl);
        edtName.setText(strCandidateName);
        smbl = edtMobile.getText().toString();
        templateMessage();
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               btnWebClicked();
            }
        });
        imgClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtMessage.getText().clear();
            }
        });

        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String text = spinnerTemplate.getSelectedItem().toString();
                    edtMessage.setText(text);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isClicked = false;
            }
        });
        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              btnWhatsappClicked();
            }

        });
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSmsClicked();
               // smbl = edtMobile.getText().toString();

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
            }
        });
        }catch (Exception e)
        {
            Toast.makeText(MessageActivity.this,"Errorcode-275 MessageActivity onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("MessageActException", String.valueOf(e));
        }
    }//onCreate


    public void templateMessage()
    {
        try {
            if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
            } else if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
                dialog = ProgressDialog.show(MessageActivity.this, "", "Loading Message Template", true);
                getTemplateMessage();
            }
        }catch (Exception e)
        {
            Toast.makeText(MessageActivity.this,"Errorcode-276 MessageActivity loadTemplateMsg "+e.toString(),Toast.LENGTH_SHORT).show();            Log.d("MessageActException", String.valueOf(e));
        }
    }
    public void btnWebClicked(){
        try {
            msg = edtMessage.getText().toString();
            //  smbl = edtMobile.getText().toString();
            strCandidateName = edtName.getText().toString();
            if (msg.length() != 0) {
                try {
                    if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
                    } else if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
                        //  dialog = ProgressDialog.show(MessageActivity.this, "", "Loading Message...", true);
                        sendWebSms();
                    }
                       /* String urlWeb=clienturl+"?clientid="+ clientid+"&caseid=301&Sms="+msg+"&Mobile="+smbl;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urlWeb));//+ smbl + "&text=" + msg));
                        startActivity(intent);
                        finish();*/
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                edtMessage.setError("Please enter message");
            }
        }catch (Exception e)
        {
            Toast.makeText(MessageActivity.this,"Errorcode-277 MessageActivity btnWebClicked "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("MessageActException", String.valueOf(e));
        }
    }
    public void btnWhatsappClicked(){
        try {
            msg = edtMessage.getText().toString();
            //  smbl = edtMobile.getText().toString();
            strCandidateName = edtName.getText().toString();
            //int pos = viewHolder.getAdapterPosition();
            //  DataCounselor dataCounselor1 = arrayList.get(pos);
            if (msg.length() != 0) {
                try {
                    if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
                    } else if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
                        dialog = ProgressDialog.show(MessageActivity.this, "", "Checking number in database...", true);
                        smbl = edtMobile.getText().toString();
                        smbl1 = smbl.substring(smbl.length() - 10);
                        checkPhoneNumber("Whatsapp", smbl1, smbl);
                    }
                    //refreshWhenLoading();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                edtMessage.setError("Please enter message");
            }
        }catch (Exception e)
        {
            Toast.makeText(MessageActivity.this,"Errorcode-278 MessageActivity btnWhatsappClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void btnSmsClicked(){
        try {
            msg = edtMessage.getText().toString();
            strCandidateName = edtName.getText().toString();

            if (msg.length() != 0) {
                if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
                } else if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
                    dialog = ProgressDialog.show(MessageActivity.this, "", "Checking number in databse...", true);
                    smbl = edtMobile.getText().toString();
                    smbl1 = smbl.substring(smbl.length() - 10);
                    checkPhoneNumber("SMS", smbl1, smbl);
                }
            } else {
                edtMessage.setError("Please enter message");
            }
        }catch (Exception e)
        {
            Toast.makeText(MessageActivity.this,"Errorcode-279 MessageActivity btnSMSClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void sendSms() {
        try {
            // requestQueue= Volley.newRequestQueue(context);
            String url = clienturl + "?clientid=" + clientid + "&caseid=86&CandidateName=" + msg + "&Mobile=" + mobile + "&SmsNo=2&SrNo=" + sr_no + "&CounselorID=" + cid;
            Log.d("SendSmsUrl", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // dialog.dismiss();
                            Log.d("*******", response.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("description");
                                JSONArray jsonArray = jsonObject1.getJSONArray("batch_dtl");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    status = jsonObject2.getString("status");
                                    Log.d("StatusMsg", status);
                                    if (status.contains("SENT")) {
                                        Toast.makeText(MessageActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                        edtMessage.setText("");
                                    } else {
                                        Toast.makeText(MessageActivity.this, "Message not sent", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                Log.d("SendSMSResponse", response);


                            } catch (Exception e) {
                                Toast.makeText(MessageActivity.this,"Errorcode-281 MessageActivity sendSMSRessponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                           /* android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("Server Error!!!")


                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).show();*/

                                // Toast.makeText(context,"Server Error",Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }
                        }
                    });
            requestQueue.add(stringRequest);
        }catch (Exception e)
        {
            Toast.makeText(MessageActivity.this,"Errorcode-280 MessageActivity sendSMS "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void sendWebSms()
    {
        try {
            if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
            } else if (CheckInternetSpeed.checkInternet(MessageActivity.this).contains("1"))
            {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
                //dialog = ProgressDialog.show(MessageActivity.this, "", "Loading counselor information...", true);
               sendSms();
            }
        }catch (Exception e)
        {
            Toast.makeText(MessageActivity.this,"Errorcode-282 MessageActivity sendWebSMS "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    public void getTemplateMessage()
    {
        try {
            arraySms = new ArrayList<>();
            urlRequest = UrlRequest.getObject();
            urlRequest.setContext(MessageActivity.this);
            urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=7&nCounselorID=" + cid + "&IsActive=1");
            Log.d("TemplatUrl", clienturl + "?clientid=" + clientid + "&caseid=7&nCounselorID=" + cid + "&IsActive=1");
            urlRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) throws JSONException {

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.d("TemplateResponse", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("Json", jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            sms = jsonObject1.getString("cSmsText");
                            arraySms.add(sms);
                        }
                        arraySms.add(0, "Select Message From Template");
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MessageActivity.this, R.layout.spinner_item1, arraySms);

                        // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTemplate.setAdapter(arrayAdapter);
                        Log.d("Size**", String.valueOf(arraySms.size()));
                    } catch (JSONException e) {
                        Toast.makeText(MessageActivity.this,"Errorcode-284 MessageActivity getTemplateMsgResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    // String msg=response.substring(response.indexOf( "( [cSmsText] =>"+15),response.indexOf(")"));
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(MessageActivity.this,"Errorcode-283 MessageActivity getTemplateMsg "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void refreshWhenLoading()
    {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if(dialog.isShowing()) {
                    Intent intent = new Intent(MessageActivity.this, MessageActivity.class);
                    //intent.putExtra("ActivityName",actname);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }

    public void setSmsEntry(String strName,String strMobile,String strSrNo,String strCounsellorId,String strMessage)
    {
        try{
            String fnlMessage;
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        fnlMessage = "Dear " + strName + "," + strMessage;
        String urlSms=clienturl+"?clientid="+ clientid+"&caseid=27&nSrNo=" + strSrNo + "&cMobileNo=" + strMobile + "&cCounselorID=" + strCounsellorId + "&cSms=" + fnlMessage;
        urlRequest.setUrl(urlSms);
        Log.d("SMSUrl",urlSms);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("SMSResponse", response);
                if (response.contains("Data inserted successfully")) {
                    eventid=3;
                    insertPointCollection(eventid);
                    Toast.makeText(MessageActivity.this, "message inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MessageActivity.this, "message not inserted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }catch (Exception e)
        {
            Toast.makeText(MessageActivity.this,"Errorcode-285 MessageActivity setSmsEntry "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void insertPointCollection(int eid)
    {
        try{
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=36&nCounsellorId=" + id1 + "&nEventId="+eid);
        Log.d("PointCollectionResponse", clienturl+"?clientid=" + clientid + "&caseid=36&nCounsellorId=" + id1 + "&nEventId="+eid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("PointCollectionResponse", response);
                if (response.contains("Data inserted successfully")) {

                //    dialog = ProgressDialog.show(MessageActivity.this, "", "Loading...", true);
                    Toast.makeText(MessageActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                   // getPointCollection();
                } else {
                    Toast.makeText(MessageActivity.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                }
                //   Log.d("Size**", String.valueOf(arrayList.size()));
            }
        });
    }catch (Exception e)
    {
        Log.d("Exception", String.valueOf(e));
        Toast.makeText(MessageActivity.this,"Errorcode-286 MessageActivity insertPoint "+e.toString(),Toast.LENGTH_SHORT).show();
    }
    }
    public void checkPhoneNumber(final String msgsource, final String number, final String number2) {
        try {

            String url = clienturl + "?clientid=" + clientid + "&caseid=68&CounselorID=" + id1 + "&PhoneNumber=" + number;

            Log.d("CheckNumberUrl", url);
            if (CheckInternet.checkInternet(MessageActivity.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("CheckNumberResponse", response);
                                try {
                                    // arrayListTotal.clear();
                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log.d("Json",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        totalcount = jsonObject1.getString("mobilecount");
                                    }
                                    if (totalcount.equals("0")) {
                                        if (msgsource.contains("Whatsapp")) {
                                            setSmsEntry(strCandidateName, number2, sr_no, id1, msg);
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + number2 + "&text=" + msg));
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        } else {
                                            setSmsEntry(strCandidateName, number2, sr_no, id1, msg);

                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number2));
                                            intent.putExtra("sms_body", msg);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }
                                    } else {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
                                        alertDialogBuilder.setTitle("This number is allocated to someone else")
                                                .setMessage("You cannot send message!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })

                                                .show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(MessageActivity.this, "Errorcode-288 MessageActivity checkPhoneNumberResponse " + e.toString(), Toast.LENGTH_SHORT).show();                                    dialog.dismiss();
                                    Log.d("CheckPhoneException", String.valueOf(e));
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(MessageActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
            Toast.makeText(MessageActivity.this, "Errorcode-287 MessageActivity checkPhoneNumber " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}

