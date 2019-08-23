package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
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

import java.util.List;

public class PhoneStateReceiver extends BroadcastReceiver {

    static final String TAG = "State";
    static final String TAG1 = " Inside State";
    static final String TAGS = " Inside Service";
    public static String phoneNumber;
    public static String name;
    String mbl;
    static Boolean recordStarted;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    MediaRecorder recorder;
    AudioManager audioManager;
    MediaMetadataRetriever mmr;
    RequestQueue requestQueue;
    String clientid, sr_no,strSrNo,strRefNo;
    UrlRequest urlRequest;
    int ch=0;
    String duration1, counselorid, date,clienturl;

    private long startHTime = 0L;
    private Handler customHandler = new Handler();
        ProgressDialog dialog;
        RecordService recordService;
        String called="";



    @Override
    public void onReceive(final Context context, final Intent intent)
    {// context=context1;
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        requestQueue= Volley.newRequestQueue(context);
        editor=sp.edit();
        clientid=sp.getString("ClientId",null);
        clienturl=sp.getString("ClientUrl",null);
        counselorid=sp.getString("Id",null);
        counselorid=counselorid.replace(" ","");
        editor.putString("RecordedFile",null);
        editor.commit();
        recordService=new RecordService(context);
        recorder = new MediaRecorder();
        recorder.reset();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mmr = new MediaMetadataRetriever();
        try {
            System.out.println("Receiver Start");
            final TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            //telephony.EXTRA_STATE;
            telephony.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber)
                {
                    super.onCallStateChanged(state,incomingNumber);
                    ch++;
                    phoneNumber=incomingNumber;
                    mbl=incomingNumber;
                    editor.putString("StateNumber",incomingNumber);
                    editor.commit();
                    Log.d("PieincomingNumber : ",incomingNumber);
                    if (state==1)
                    {
                        String time = new CommonMethods().getTIme();
                        time = time.replaceAll(" ", "");
                        date = new CommonMethods().getDate();
                        phoneNumber = phoneNumber.substring((phoneNumber.length()) - 10);
                        phoneNumber = phoneNumber.replaceAll(" ", "");
                        String path = new CommonMethods().getPath();
                        String rec = path + "/" +phoneNumber + "_" + date + "_" + time + ".mp3";
                        editor.putString("RecordedFile",rec);
                        editor.commit();
                        try {
                            recordService.startRecord();
                        }catch (Exception e)
                        {
                            Toast.makeText(context,"Exception in start recording"+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                       /* if(Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())){
                            recordService.startRecord();
                        }*/
                        //else {
                       /* called="Incoming";
                            phoneNumber = phoneNumber.substring((phoneNumber.length()) - 10);
                            phoneNumber = phoneNumber.replaceAll(" ", "");
                            // dialog=ProgressDialog.show(context,"","Checking number",true);
                            checkIncomingNumber(phoneNumber, context);*/
                       // }
                        Log.d(TAG1, " Inside " + state);
                  /*  int j=pref.getInt("numOfCalls",0);
                    pref.edit().putInt("numOfCalls",++j).apply();
                    Log.d(TAG, "onReceive: num of calls "+ pref.getInt("numOfCalls",0));*/
                    }
                    else if (state==2)
                    {
                        /* editor.putString("StateNumber1",incomingNumber);
                        editor.commit();
                        Log.d("OMG","This is mona"+incomingNumber);*/
                       // recordService.startRecord();
                        int j = pref.getInt("numOfCalls", 0);
                        pref.edit().putInt("numOfCalls", ++j).apply();
                        Log.d(TAG, "onReceive: num of calls " + pref.getInt("numOfCalls", 0));
                        Log.d(TAG1, " recordStarted in offhook: " + recordStarted);
                        Log.d(TAG1, " Inside " + state);
                      /*  if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O)
                        {
                            if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction()))
                            {
                                Log.d("OMG","This is action call");
                                phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                                Log.d("PHONE1111", "outgoing,ringing:" + phoneNumber);
                                editor.putString("StateNumber",phoneNumber);
                                editor.commit();
                            } else {
                                phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                                Log.d(TAG1, " Phone Number in receiver " + phoneNumber);
                                editor.putString("StateNumber",phoneNumber);
                                editor.commit();
                            }
                        }*/
                     /* if (pref.getInt("numOfCalls", 1) == 1)
                        {
                            Intent reivToServ = new Intent(context, RecordService.class);
                            if(TextUtils.isEmpty(phoneNumber)){
                                Log.d("INNumber",incomingNumber);
                                reivToServ.putExtra("CNumber",incomingNumber);
                                Log.d("OMG","This is action call on pie"+incomingNumber);
                            }
                            else
                            {
                                reivToServ.putExtra("CNumber",phoneNumber);
                            }*/

                         // recordService.startRecord();
                               // context.startService(reivToServ);

                        if(Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())){
                            String time = new CommonMethods().getTIme();
                            time = time.replaceAll(" ", "");
                            date = new CommonMethods().getDate();
                            if(phoneNumber.startsWith("+")) {
                                phoneNumber = phoneNumber.substring((phoneNumber.length()) - 10);
                            }
                            phoneNumber = phoneNumber.replaceAll(" ", "");
                            String path = new CommonMethods().getPath();
                            String rec = path + "/" +phoneNumber + "_" + date + "_" + time + ".mp3";
                            editor=sp.edit();
                            editor.putString("RecordedFile",rec);
                            editor.commit();
                           /* called="Outgoing";
                            phoneNumber = phoneNumber.substring((phoneNumber.length()) - 10);
                            phoneNumber = phoneNumber.replaceAll(" ", "");
                            checkIncomingNumber(phoneNumber,context);*/
                            try {
                                recordService.startRecord();
                            }catch (Exception e)
                            {
                                Toast.makeText(context,"Exception in start recording"+e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                            int serialNumber = pref.getInt("serialNumData", 1);
                            new DatabaseManager(context).addCallDetails(new CallDetails(serialNumber, phoneNumber, new CommonMethods().getTIme(), new CommonMethods().getDate()));

                            List<CallDetails> list = new DatabaseManager(context).getAllDetails();
                            for (CallDetails cd : list) {
                                String log = "Serial Number : " + cd.getSerial() + " | Phone num : " + cd.getNum() + " | Time : " + cd.getTime1() + " | Date : " + cd.getDate1();
                                Log.d("Database ", log);
                            }
                            //recordStarted=true;
                            pref.edit().putInt("serialNumData", ++serialNumber).apply();
                            pref.edit().putBoolean("recordStarted", true).apply();

                    } else if (state==0) {
                        int k = pref.getInt("numOfCalls", 1);
                        pref.edit().putInt("numOfCalls", --k).apply();
                        int l = pref.getInt("numOfCalls", 0);
                        Log.d(TAG1, " Inside " + state);
                        recordStarted = pref.getBoolean("recordStarted", false);
                        Log.d(TAG1, " recordStarted in idle :" + recordStarted);
                        if (recordStarted && l == 0) {
                            Log.d(TAG1, " Inside to stop recorder " + state);
                           recordService.onStop();
                            // context.stopService(new Intent(context, RecordService.class));
                            pref.edit().putBoolean("recordStarted", false).apply();
                                // new RecordService().stopSelf();
                            Toast.makeText(context, "Call detected(Incoming/Outgoing) " + state, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            },PhoneStateListener.LISTEN_CALL_STATE);
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
    }

    public void checkIncomingNumber(String number, final Context context1)
    {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=91&CounselorID=" + counselorid+"&PhoneNumber="+number;
            if (CheckInternet.checkInternet(context1)) {
                if(CheckServer.isServerReachable(context1)) {
                    Log.d("AllDetailsUrl", url);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Called",called);
                                    Log.d("AllDetailsResponse", response);
                                    try {
                                       // dialog.dismiss();
                                        if (response.contains("[]"))
                                        {
                                            /* recordService.startRecord();
                                            editor.putString("SelectedSrNo", phoneNumber);
                                            editor.commit();
                                             Intent intent=new Intent(context1,SplashNumberNotPresent.class);
                                            context1.startActivity(intent);*/

                                        } else {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                 strSrNo = jsonObject1.getString("nSrNo");
                                                 strRefNo = jsonObject1.getString("refNo");

                                            }
                                            editor.putString("SelectedSrNo", strSrNo);
                                            editor.commit();
                                            try {
                                                recordService.startRecord();

                                            }catch (Exception e)
                                            {

                                                Toast.makeText(context1,"Exception in call recorder",Toast.LENGTH_SHORT).show();
                                            }
                                            Intent intent=new Intent(context1,CounselorContactActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("ActivityName","PhoneStateReceiver");
                                            context1.startActivity(intent);
                                        }
                                    } catch (JSONException e)
                                    {
                                        Toast.makeText(context1, "Errorcode-310 MasterEntry AllDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context1);
                                        alertDialogBuilder.setTitle("Server Error!!!")



                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        //   Toast.makeText(context1, "Server Error", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }
                else {
                   /* if (dialog.isShowing()) {
                        dialog.dismiss();
                    }*/
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context1);
                    alertDialogBuilder.setTitle("Server Down!!!!")
                            .setMessage("Try after some time!")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //insertIMEI();

                                    dialog.dismiss();

                                }
                            }).show();
                }
            } else {
                //dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context1);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();

                                dialog.dismiss();

                            }
                        }).show();
            }
        }catch (Exception e)
        {
            Toast.makeText(context1,"Errorcode-309 MasterEntry getAllDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }


}
