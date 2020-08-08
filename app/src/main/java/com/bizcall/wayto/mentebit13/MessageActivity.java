package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;

import android.text.util.Linkify;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

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

public class MessageActivity extends AppCompatActivity {

   TextView txtCloseWindow,txtName;
   ImageView imgCloseWindow,imgClearText,imgBack;
   EditText edtMobile,edtMessage,edtName;
   Button btnSms,btnWhatsapp,btnWeb,btnEmail,btnNotify;
   Spinner spinnerTemplate;
   SharedPreferences sp;
   ProgressDialog dialog;
   UrlRequest urlRequest;
   String clienturl="",clientid="",selectedmbl="",parentno="",strCandidateName="",smbl="",countrycode="",sms="",status="",fileno="";
   ArrayList<String> arraySms;
    RequestQueue requestQueue;
    int eventid;
    boolean isClicked;
    String msg,sr_no,id1,totalcount,smbl1,cid,activity,mobile,email;
    Vibrator vibrator;
    Thread thread;
    long timeout;
    int flag=0;
    Spinner spinnerMobile;
    ArrayList<String> arrayListPhone;
    AppCompatSpinner spinner_countrycode;
    private String[] textArray = {"IN +91", "Afghan +93", "Africa +2", "Argentina +54", "Armenia +374", "Australia +61", "Banlga +880", "Belarus +375",
            "Belgium +32", "Bhutan +975", "Bulgaria +673", "Canada +1", "China +86", "Europe +3", "Georgia +995", "Germany +49", "Iraq +964", "Iran +98",
            "Japan +81", "Kazakhstan +7", "Kyrgyzstan +996", "Nepal +977", "NewZealand +64", "Nigeria +234","Pak +92" ,"Poland +48", "Qatar +974", "Russia +7", "Singapore +65",
            "SriLanka +94", "Sweden +46", "Switzerland +41", "Ukraine +380", "UAE +971", "UK +44", "US +1", "Zimbabwe +263"};
    private Integer[] imageArray = {R.drawable.india, R.drawable.afghan, R.drawable.africa, R.drawable.argentina, R.drawable.armenia, R.drawable.australia,
            R.drawable.bangla, R.drawable.belarus, R.drawable.belgium, R.drawable.bhutan, R.drawable.bulgaria, R.drawable.canada, R.drawable.china, R.drawable.europe,
            R.drawable.georgia, R.drawable.germany, R.drawable.iraq, R.drawable.iran, R.drawable.japan, R.drawable.kazakhstan, R.drawable.kyrgyzstan, R.drawable.nepal,
            R.drawable.newzealand, R.drawable.nigeria,R.drawable.pakistan, R.drawable.poland, R.drawable.qatar, R.drawable.russia, R.drawable.singapore, R.drawable.srilanka, R.drawable.sweden,
            R.drawable.switzerland, R.drawable.ukraine, R.drawable.uae, R.drawable.uk, R.drawable.us, R.drawable.zimbabwe};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        try{
            initialize();
            if(activity.contains("Summary"))
            {
                btnEmail.setVisibility(View.VISIBLE);
                btnNotify.setVisibility(View.VISIBLE);
               // fileno=sp.getString("FileNo",null);

                msg="Dear "+strCandidateName+", \n" +
                        "Thanking you for choosing Select Your University as your preferred study abroad partner.\n" +
                        "Your File No:"+sr_no+" is generated with us.Your login details are:"+"\n"+"Client ID:WA11"+"\n"+"File No:"+sr_no+"\n"+"Mobile No:"+mobile+"\n"+"Kindley download Android Application to proceed further.   "+"\n"+"https://play.google.com/store/apps/details?id=com.bizcalldocument.mentebit.bizcalldocumentmodule";
                edtMessage.setText(msg);
                Linkify.addLinks(edtMessage , Linkify.WEB_URLS);
            }
      //  edtMobile.setText(selectedmbl);
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
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNotifyClicked();
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
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMailClicked();
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
                                Toast.makeText(MessageActivity.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
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
    public void initialize()
    {
        requestQueue=Volley.newRequestQueue(MessageActivity.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sp=getSharedPreferences("Settings",Context.MODE_PRIVATE);
        mobile=sp.getString("SelectedMobile",null);
        parentno=sp.getString("ParentNo","");
        if(mobile.length()==0)
        {
            mobile = "NA";
        }
        selectedmbl=mobile;
        if(parentno.length()==0)
        {
            parentno="NA";
        }
        arrayListPhone=new ArrayList<>();
        clientid=sp.getString("ClientId",null);
        clienturl=sp.getString("ClientUrl",null);
        sr_no = sp.getString("SelectedSrNo", null);
        email=sp.getString("SelectedEmail",null);
        timeout=sp.getLong("TimeOut",0);

        id1=sp.getString("Id",null);
        id1=id1.replaceAll(" ","");
        strCandidateName=sp.getString("CName",null);
        activity=getIntent().getStringExtra("Activity");
        Log.d("Cname",strCandidateName);
        //  txtCloseWindow = findViewById(R.id.txtCloseWindow);
        //imgCloseWindow =findViewById(R.id.imgCloseWindow);
        btnNotify=findViewById(R.id.btnNotify);
        edtMobile = findViewById(R.id.edtMobileNo);
        edtName = findViewById(R.id.edtUserName1);
        edtMessage = findViewById(R.id.edtMessage1);
        spinnerTemplate =findViewById(R.id.spinnerMsgTemplate);
        btnSms =findViewById(R.id.btnSMS);
        imgBack=findViewById(R.id.img_back);
        imgClearText =findViewById(R.id.imgClearMsg);
        btnWhatsapp = findViewById(R.id.btnWhatsapp);
        btnWeb=findViewById(R.id.btnWeb);
        btnEmail=findViewById(R.id.btnEmail);
        spinnerMobile=findViewById(R.id.spinnerMobile);
        spinner_countrycode=findViewById(R.id.spinner_countrycode);

        cid = sp.getString("Id", null);
        Log.d("CID", cid);
        cid = cid.replace(" ", "");
        arrayListPhone.add(selectedmbl);
        arrayListPhone.add(parentno);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MessageActivity.this, R.layout.spinner_item1, arrayListPhone);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMobile.setAdapter(arrayAdapter);
        spinnerMobile.setSelection(0);

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.lay_spinner_value, textArray, imageArray);
        spinner_countrycode.setAdapter(adapter);
        spinner_countrycode.setSelection(0);

        smbl=spinnerMobile.getSelectedItem().toString();
        countrycode = spinner_countrycode.getSelectedItem().toString();
        smbl1 = countrycode + smbl;
        edtMobile.setText(smbl1);
        spinnerMobile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                smbl=spinnerMobile.getSelectedItem().toString();
                countrycode = spinner_countrycode.getSelectedItem().toString();
                smbl1 = countrycode + smbl;
                edtMobile.setText(smbl1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_countrycode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                smbl=spinnerMobile.getSelectedItem().toString();
                countrycode = spinner_countrycode.getSelectedItem().toString();
                smbl1 = countrycode + smbl;
                edtMobile.setText(smbl1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void setNotification(String strNotification,String strSrNo,String strCounselorID)
    {
        try{
           String url=clienturl+"?clientid=" + clientid + "&caseid=62&SrNo=" + strSrNo +  "&CounselorId=" + strCounselorID +"&Notification=" + strNotification;
            Log.d("NotificationUrl", url);
            if(CheckInternet.checkInternet(MessageActivity.this))
            {
                if(CheckServer.isServerReachable(MessageActivity.this))
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("*******", response.toString());
                                    try {
                                        dialog.dismiss();
                                        Log.d("NotificationResponse", response);
                                        if (response.contains("Data inserted successfully"))
                                        {

                                            Toast.makeText(MessageActivity.this, "Notification set", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(MessageActivity.this, "Noification not set", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(MessageActivity.this,"Errorcode-217 CounselorContact setNotification "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(MessageActivity.this,"Network issue",Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                }else {
                    dialog.dismiss();
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
                } }else {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MessageActivity.this);
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
            Toast.makeText(MessageActivity.this,"Errorcode-216 CounselorContact setNotification "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcSetNotifjcation", String.valueOf(e));
        }

    }
    public void btnMailClicked()
    {
        String subject="Thank you for choosing Select Your University";
        String text="Dear "+strCandidateName+", \n" +
                "\n" +
                "Thanking you for choosing Select Your University as your preferred study abroad partner. \n" +
                "\n" +
                "Your File No:"+sr_no+" is generated with us.Your login details are:"+"\n"+"Client ID:WA11"+"\n"+"File No:"+sr_no+"\n"+"Mobile No:"+mobile+"\n"+"Kindley download Android Application to proceed further."+"\n"+"https://play.google.com/store/apps/details?id=com.bizcalldocument.mentebit.bizcalldocumentmodule\n" +
                "\n\n" +
                "Thanks & Regards,\n" +
                "\n" +
                "Management \n" +
                "\n" +
                "Select Your University.";

        String emailAddressList[] = {email};

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddressList);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setType("plain/text");
        startActivity(Intent.createChooser(intent, "Send mail"));
    }

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
               newThreadInitilization(dialog);
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

                       smbl1 = edtMobile.getText().toString();

                        flag=0;
                        if(smbl1.contains("NA"))
                        {
                            flag=1;
                            Toast.makeText(MessageActivity.this,"Invalid mobile number",Toast.LENGTH_SHORT).show();
                        }
                        if(flag==0)
                        {
                            dialog = ProgressDialog.show(MessageActivity.this, "", "Checking number in database...", true);
                           // smbl1=edtMobile.getText().toString();
                            //smbl1 = smbl.substring(smbl.length() - 10);
                            newThreadInitilization(dialog);
                            checkPhoneNumber("Whatsapp", smbl1);
                        }
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
    public void btnNotifyClicked()
    {
       // msg="Your File No "+sr_no+"is generated with us.";
        setNotification(msg,sr_no,id1);
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
                  //  dialog = ProgressDialog.show(MessageActivity.this, "", "Checking number in databse...", true);
                    //smbl = edtMobile.getText().toString();
                    //smbl1 = smbl.substring(smbl.length() - 10);

                    smbl1=edtMobile.getText().toString();
                    flag=0;
                    if(smbl1.contains("NA"))
                    {
                        flag=1;
                        Toast.makeText(MessageActivity.this,"Invalid mobile number",Toast.LENGTH_SHORT).show();
                    }
                    if(flag==0) {
                        dialog = ProgressDialog.show(MessageActivity.this, "", "Checking number in database...", true);
                       // countrycode = spinner_countrycode.getSelectedItem().toString();
                        //smbl1 = countrycode + smbl;

                        //smbl1 = smbl.substring(smbl.length() - 10);
                        newThreadInitilization(dialog);
                        checkPhoneNumber("SMS", smbl1);
                    }
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
                                    if (status.contains("SENT")||status.contains("AWAITED-DLR")) {
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
                            alertDialogBuilder.setTitle("Network issue!!!")


                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).show();*/

                                // Toast.makeText(context,"Network issue",Toast.LENGTH_SHORT).show();
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
    public void checkPhoneNumber(final String msgsource, final String number) {
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
                                            setSmsEntry(strCandidateName, number, sr_no, id1, msg);
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + number + "&text=" + msg));
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        } else {
                                            setSmsEntry(strCandidateName, number, sr_no, id1, msg);

                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
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
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(MessageActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
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

