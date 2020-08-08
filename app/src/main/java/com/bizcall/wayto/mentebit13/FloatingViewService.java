package com.bizcall.wayto.mentebit13;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class FloatingViewService extends Service implements View.OnClickListener {
    private WindowManager mWindowManager;
    private View mFloatingView;
    String strMNo, strmobNo, audiofilename, today;
    TextView txtCallerName, txtCallerLead, txtCallerLastLog;
    ImageView imgCallType;
    LinearLayout layDropDown;
    Context context;
    DBHelper myDB;
    RequestQueue requestQueue;
    DataContactList dataContactList;
    ArrayList<DataContactList> arrayListContactUpload;
    String strname, strmobno, stremail, strcompname, struploadedon, strremark, strcreateddate, strcounsid, strimei1, strimei2;
    DataLeadInfo dataLeadInfo;
    ArrayList<DataLeadInfo> arrayListCalllogUpload, arrayListLeadUpload;
    String leadname, leadmobno, leadtype, leaddate, leadtime, leadrecording, leadcounsid, leadimei1, leadimei2;
    String logid, logname, logmobno, logtype, logdate, logtime, logcounsid, logimei1, logimei2;
    String uploadFilePath;
    String uploadFileName;
    String upLoadServerUri;
    int serverResponseCode = 0;

    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }

        requestQueue = Volley.newRequestQueue(FloatingViewService.this);
        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_alertringing, null);
        context = FloatingViewService.this;

        //setting the layout parameters
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                PixelFormat.TRANSLUCENT);

        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //adding click listener to close button and expanded view
        mFloatingView.findViewById(R.id.img_cancle).setOnClickListener(this);
        txtCallerName = mFloatingView.findViewById(R.id.txt_contact_nmno);
        txtCallerLead = mFloatingView.findViewById(R.id.txt_leadstatus);
        txtCallerLastLog = mFloatingView.findViewById(R.id.txt_leadinfo);
        imgCallType = mFloatingView.findViewById(R.id.img_calltype);
        layDropDown = mFloatingView.findViewById(R.id.lay_flowdropdata);
        mFloatingView.findViewById(R.id.txt_mobilecall).setOnClickListener(this);
        mFloatingView.findViewById(R.id.txt_mobilesms).setOnClickListener(this);
        mFloatingView.findViewById(R.id.txt_replywhatsapp).setOnClickListener(this);
        mFloatingView.findViewById(R.id.txt_replyvideo).setOnClickListener(this);
        mFloatingView.findViewById(R.id.txt_leadstatus).setOnClickListener(this);
        mFloatingView.findViewById(R.id.btn_add_lables).setOnClickListener(this);
        mFloatingView.findViewById(R.id.img_drop).setOnClickListener(this);
        mFloatingView.findViewById(R.id.img_up).setOnClickListener(this);
        mFloatingView.findViewById(R.id.view_droplayout);

        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    //function for getting new records to upload, show last calllog record.
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                strMNo = (String) intent.getExtras().get("callnumber");
                strmobNo = (String) intent.getExtras().get("mobno");
                audiofilename = (String) intent.getExtras().get("audioname");
                Log.d("floatdata", strMNo + " / " + strmobNo + " / " + audiofilename);

                Date currentTime = Calendar.getInstance().getTime();
                DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                today = formatter.format(currentTime);

                txtCallerName.setText(strMNo);
              //  getCallDetails(FloatingViewService.this);
                myDB = new DBHelper(FloatingViewService.this);

                Cursor cursor = myDB.getContactLogs(strmobNo);      //--------------------FloatingWindow Last Call Details
                if (cursor.moveToPosition(1)) {
                    if (cursor.getString(0) == null) {
                        imgCallType.setImageResource(R.drawable.blankcall);
                    } else if (cursor.getString(0).equals("MISSED")) {
                        imgCallType.setImageResource(R.drawable.missedcall);
                    } else if (cursor.getString(0).equals("INCOMING")) {
                        imgCallType.setImageResource(R.drawable.receivedcall);
                    } else if (cursor.getString(0).equals("OUTGOING")) {
                        imgCallType.setImageResource(R.drawable.outgoingcall);
                    }

                    txtCallerLead.setText("Lead");
                    txtCallerLastLog.setText(cursor.getString(1) + " / " + cursor.getString(2));
                    Log.d("txtCallerLastLog", txtCallerLastLog.getText().toString());
                } else {
                    txtCallerLastLog.setText("New Lead saved");
                    txtCallerLead.setText("New Lead");
                }

                try {
                    if (CheckInternetSpeed.checkInternet(FloatingViewService.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FloatingViewService.this);
                        alertDialogBuilder.setTitle("No Internet connection!!!")
                                .setMessage("Can't do further process")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    } else if (CheckInternetSpeed.checkInternet(FloatingViewService.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FloatingViewService.this);
                        alertDialogBuilder.setTitle("Slow Internet speed!!!")
                                .setMessage("Can't do further process")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    } else {
                        if (CheckServer.isServerReachable(FloatingViewService.this)) {

                            Cursor cursor1 = myDB.findContactUpload();      //-----------------------Contact upload success with 1
                            arrayListContactUpload = new ArrayList<>();
                            arrayListContactUpload.clear();
                            while (cursor1 != null && cursor1.moveToNext()) {
                                dataContactList = new DataContactList(cursor1.getString(0), cursor1.getString(1), cursor1.getString(2), cursor1.getString(3), cursor1.getString(4), cursor1.getString(5));
                                arrayListContactUpload.add(dataContactList);
                            }
                            Log.d("arrayListContactUpload", arrayListContactUpload.size() + "");

                            strcounsid = "1";
                            //strimei1 = Home.;
                           // strimei2 = ActivityHome.IMEINumber2;

                            //Select * from LocalDB where cMobile like '%mobileno right side 10 digit%'
                            for (int i = 0; i < arrayListContactUpload.size(); i++) {
                                strname = arrayListContactUpload.get(i).getConName();
                                strmobno = arrayListContactUpload.get(i).getConNumber();
                                stremail = arrayListContactUpload.get(i).getConEmail();
                                strcompname = arrayListContactUpload.get(i).getConCompName();
                                struploadedon = arrayListContactUpload.get(i).getConUploaded();
                                strremark = arrayListContactUpload.get(i).getConRemark();

                                uploadContacts(strname, strmobno, stremail, strcompname, struploadedon, strremark, strcreateddate, strcounsid, strimei1, strimei2);
                            }

                            Cursor cursor2 = myDB.findLeadUpload();     //-----------------------Lead upload success with 1
                            arrayListLeadUpload = new ArrayList<>();
                            arrayListLeadUpload.clear();
                            while (cursor2 != null && cursor2.moveToNext()) {
                                dataLeadInfo = new DataLeadInfo(cursor2.getString(0), cursor2.getString(1), cursor2.getString(2), cursor2.getString(3), cursor2.getString(4), cursor2.getString(5), cursor2.getString(6));
                                arrayListLeadUpload.add(dataLeadInfo);
                            }
                            Log.d("arrayListLeadUpload", arrayListLeadUpload.size() + "");

                            leadcounsid = "1";
                            //leadimei1 = ActivityHome.IMEINumber1;
                          //  leadimei2 = ActivityHome.IMEINumber2;

                            if (arrayListLeadUpload.size() != 0) {
                                for (int i = 1; i < arrayListLeadUpload.size(); i++) {
                                    leadname = arrayListLeadUpload.get(i).getLeadName();
                                    leadmobno = arrayListLeadUpload.get(i).getLeadNumber();
                                    leadtype = arrayListLeadUpload.get(i).getLeadType();
                                   // leaddate = arrayListLeadUpload.get(i).get();
                                    leadtime = arrayListLeadUpload.get(i).getLeadTime();
                                   // leadrecording = arrayListLeadUpload.get(i).getLeadRecording();
                                    Log.d("uploadingRecords", leadname + " " + leadmobno + " " + leaddate + " " + leadrecording);

                                    if (!leadrecording.equals(audiofilename)) {
                                        uploadLeads(leadname, leadmobno, leadtype, leaddate, leadtime, leadrecording, leadcounsid, leadimei1, leadimei2);
                                    } else {
                                        Log.d("currentcall", "Upload Done");
                                        //Toast.makeText(context, "Upload Done", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            Cursor cursor3 = myDB.findCallLogUpload();      //---------------------CallLog upload success with 1
                            arrayListCalllogUpload = new ArrayList<>();
                            arrayListCalllogUpload.clear();
                            while (cursor3 != null && cursor3.moveToNext()) {
                                dataLeadInfo = new DataLeadInfo(cursor3.getString(0), cursor3.getString(1), cursor3.getString(2), cursor3.getString(3), cursor3.getString(4), cursor3.getString(5), cursor3.getString(6));
                                arrayListCalllogUpload.add(dataLeadInfo);
                            }
                            Log.d("arrayListCalllogUpload", arrayListCalllogUpload.size() + "");

                            logcounsid = "1";
                           // logimei1 = ActivityHome.IMEINumber1;
                            //logimei2 = ActivityHome.IMEINumber2;

                            for (int i = 0; i < arrayListCalllogUpload.size(); i++) {

                                logid = arrayListCalllogUpload.get(i).getLeadName();
                                logname = arrayListCalllogUpload.get(i).getLeadNumber();
                                logmobno = arrayListCalllogUpload.get(i).getLeadType();
                              //  logtype = arrayListCalllogUpload.get(i).getLeadDate();
                                logdate = arrayListCalllogUpload.get(i).getLeadTime();
                              //  logtime = arrayListCalllogUpload.get(i).getLeadRecording();

                                uploadCallLogs(logid, logname, logmobno, logtype, logdate, logtime, logcounsid, logimei1, logimei2);
                            }
                        } else {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FloatingViewService.this);
                            alertDialogBuilder.setTitle("Server Down!!!!")
                                    .setMessage("Try after some time!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
                    }
                } catch (Exception e) {
                    //progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(FloatingViewService.this, "FloatingViewService : Plugins-001", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    //function for uploading new calllogs on server.
    public void uploadCallLogs(final String logid, String name, String mobno, String type, String date, String time, String counid, String imei1, String imei2) {
        try {
            String strurl = "http://anilsahasrabuddhe.in/CRM/wayto965425/wayto965425_testing_final06052019.php" + "?clientid=wayto965425" +
                    "&caseid=A39&cCallLogID=" + logid + "&cCallLogName=" + name + "&cCallLogMobNo=" + mobno + "&cCallLogType=" + type +
                    "&dtCallLogDate=" + date + "&cCallLogDuration=" + time + "&nCounselorID=" + counid + "&cIMEI1=" + imei1 + "&cIMEI2=" + imei2;

            Log.d("dd39", strurl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("Data inserted successfully")) {
                        myDB.updateCallLog(logid);
                        //Toast.makeText(FloatingViewService.this, "Qualification inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("VolleyError", String.valueOf(error));
                    Toast.makeText(FloatingViewService.this, "Server not responding.", Toast.LENGTH_SHORT).show();
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(FloatingViewService.this, "FloatingViewService : Plugins-002", Toast.LENGTH_SHORT).show();
        }
    }

    //function for uploading new call lead on server.
    public void uploadLeads(String lname, String lmobno, String ltype, String ldate, String ltime, final String lrecording, String lcounid, String limei1, String limei2) {
        try {
            String strurl = "http://anilsahasrabuddhe.in/CRM/wayto965425/wayto965425_testing_final06052019.php" + "?clientid=wayto965425" + "&caseid=A35&ContactName=" + lname +
                    "&MobileNo=" + lmobno + "&CallType=" + ltype + "&CallDate=" + ldate + "&CallTime=" + ltime +
                    "&CallRecording=" + lrecording + "&CounselorID=" + lcounid + "&IMEI1=" + limei1 + "&IMEI2=" + limei2;

            Log.d("dd35", strurl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("Data inserted successfully")) {
                        Log.d("ChatResponse", lrecording);

                        uploadFilePath = new CommonMethods().getPath();
                        uploadFileName = lrecording;
                        upLoadServerUri = "http://anilsahasrabuddhe.in/CRM/wayto965425/wayto965425_testing_final06052019.php" + "?clientid=wayto965425" + "&caseid=A38";
                        uploadFile(uploadFilePath + "/" + uploadFileName);
                        Log.d("AudioPath", uploadFilePath + "/" + uploadFileName);
                        //Toast.makeText(FloatingViewService.this, "Qualification inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("VolleyError", String.valueOf(error));
                    Toast.makeText(FloatingViewService.this, "Server not responding.", Toast.LENGTH_SHORT).show();
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(FloatingViewService.this, "FloatingViewService : Plugins-003", Toast.LENGTH_SHORT).show();
        }
    }

    //function for uploading recording.
    public int uploadFile(String sourceFileUri) {

        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.exists()) {
            myDB.updateLeadNotFound(uploadFileName);
            Log.d("uploadFileNotExist", "Source File not exist :" + uploadFilePath + "" + uploadFileName);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FloatingViewService.this, "Source File not exist :"
                            /*+ uploadFilePath + " / " + uploadFileName*/, Toast.LENGTH_SHORT).show();
                }
            }, 10000);
            /*runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(FloatingViewService.this, "Source File not exist :"
                            + uploadFilePath + " / " + uploadFileName, Toast.LENGTH_SHORT).show();
                }
            });*/
            return 0;
        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri); //url remaining.............................

                Log.d("upLoadServerUri", upLoadServerUri);

                int SDK_INT = Build.VERSION.SDK_INT;

                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + fileName + "" + lineEnd);
                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                    if (serverResponseCode == 200) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updaterecording(uploadFileName);
                                Log.d("uploadsuccess", "doneee");
                            }
                        }, 10000);

                    }
                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                }


            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FloatingViewService.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                }, 10000);

                Log.e("Uploadfiletoserver", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FloatingViewService.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                }, 10000);
                Log.d("Exception", "Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;
        } // End else block
    }

    //function for uploading new recordings.
    public void updaterecording(final String updatereco) {
        try {
            String strurl = "http://anilsahasrabuddhe.in/CRM/wayto965425/wayto965425_testing_final06052019.php" + "?clientid=wayto965425" + "&caseid=A37&Recording=" + updatereco;

            Log.d("dd37", strurl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("Data updated successfully")) {
                        myDB.updateLead(updatereco);
                        Log.d("recordinguploaded", "recordingdone / " + updatereco);
                        //Toast.makeText(FloatingViewService.this, "Recording updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("VolleyError", String.valueOf(error));
                    Toast.makeText(FloatingViewService.this, "Server not responding.", Toast.LENGTH_SHORT).show();
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(FloatingViewService.this, "FloatingViewService : Plugins-004", Toast.LENGTH_SHORT).show();
        }
    }

    //function for uploading new contact records.
    public void uploadContacts(String name, final String mobno, String email, String compnm, String uploaded, String remark, String crdate, String counid, String imei1, String imei2) {
        try {
            String strurl = "http://anilsahasrabuddhe.in/CRM/wayto965425/wayto965425_testing_final06052019.php" + "?clientid=wayto965425" + "&caseid=A36&ContactName=" + name +
                    "&ContactNo=" + mobno + "&ContactEmail=" + email + "&ContactCompNM=" + compnm +
                    "&Remarks=" + remark + "&CreatedDate=" + today + "&CounselorID=" + counid + "&IMEI1=" + imei1 + "&IMEI2=" + imei2;

            Log.d("dd36", strurl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("Data inserted successfully")) {
                        myDB.updateContact(mobno);
                        //Toast.makeText(FloatingViewService.this, "Qualification inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("VolleyError", String.valueOf(error));
                    Toast.makeText(FloatingViewService.this, "Server not responding.", Toast.LENGTH_SHORT).show();
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(FloatingViewService.this, "FloatingViewService : Plugins-005", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    //function of onclick methods for floating window contents.
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_cancle:
                //closing the widget
                stopSelf();
                break;
            case R.id.img_drop:
                layDropDown.setVisibility(View.VISIBLE);
                mFloatingView.findViewById(R.id.img_drop).setVisibility(View.GONE);
                mFloatingView.findViewById(R.id.img_up).setVisibility(View.VISIBLE);
                mFloatingView.findViewById(R.id.view_droplayout).setVisibility(View.VISIBLE);
                break;
            case R.id.img_up:
                layDropDown.setVisibility(View.GONE);
                mFloatingView.findViewById(R.id.img_up).setVisibility(View.GONE);
                mFloatingView.findViewById(R.id.img_drop).setVisibility(View.VISIBLE);
                mFloatingView.findViewById(R.id.view_droplayout).setVisibility(View.GONE);
                break;
            case R.id.txt_mobilecall:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + strmobNo));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.txt_mobilesms:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("sms:" + strmobNo));
                smsIntent.putExtra("sms_body", "I am sorry I am unable to answer your call right now.\nPlease leave me a text message and\nI will get back to you as soon as possible.\nThanks.");
                smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(smsIntent);
                /*String messageToSend = "I am sorry I am unable to answer your call right now.\n Please leave me a text message and \n I will get back to you as soon as possible. \n Thanks.";
                String number = strmobNo;
                SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null,null);*/
                break;
            case R.id.txt_replywhatsapp:
                boolean installed = appInstalledOrNot("com.whatsapp");
                if (installed) {
                    PackageManager packageManager = context.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    try {
                        String url = "https://api.whatsapp.com/send?phone=+91" + strmobNo /* +"&text=" + URLEncoder.encode("hi", "UTF-8")*/;
                        Log.d("wpurl", url);
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (i.resolveActivity(packageManager) != null) {
                            context.startActivity(i);
                            //stopSelf();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Application not found. Please install WhatsApp.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txt_replyvideo:
                boolean installedVideo = appInstalledOrNot("com.whatsapp");
                if (installedVideo) {
                    PackageManager packageManager = context.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    try {
                        String url = "https://api.whatsapp.com/send?phone=+91" + strmobNo /* +"&text=" + URLEncoder.encode("hi", "UTF-8")*/;
                        Log.d("wpurl", url);
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (i.resolveActivity(packageManager) != null) {
                            context.startActivity(i);
                            //stopSelf();
                            try {
                                String contactNumber = strmobNo;
                                Cursor cursor = getContentResolver()
                                        .query(
                                                ContactsContract.Data.CONTENT_URI,
                                                new String[]{ContactsContract.Data._ID},
                                                ContactsContract.RawContacts.ACCOUNT_TYPE + " = 'com.whatsapp' " +
                                                        "AND " + ContactsContract.Data.MIMETYPE + " = 'vnd.android.cursor.item/vnd.com.whatsapp.video.call' " +
                                                        "AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + contactNumber + "%'",
                                                null,
                                                ContactsContract.Contacts.DISPLAY_NAME
                                        );

                                long id = -1;
                                while (cursor.moveToNext()) {
                                    id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
                                }

                                if (!cursor.isClosed()) {
                                    cursor.close();
                                }

                                Intent intentVideo = new Intent();
                                intentVideo.setAction(Intent.ACTION_VIEW);
                                intentVideo.setDataAndType(Uri.parse("content://com.android.contacts/data/" + id), "vnd.android.cursor.item/vnd.com.whatsapp.video.call");
                                intentVideo.setPackage("com.whatsapp");
                                intentVideo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (intentVideo.resolveActivity(packageManager) != null) {
                                    context.startActivity(intentVideo);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Application not found. Please install WhatsApp.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txt_leadstatus:
                break;
            case R.id.btn_add_lables:
                String packageName = "com.mentebit.bizcalladmin", packageClassName = "com.mentebit.bizcalladmin.ActivityAdminLeadReports";

                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_MAIN);
                intent1.addCategory(Intent.CATEGORY_LAUNCHER);
                intent1.putExtra("bizcallplugins", "bizcallplugins");
                intent1.putExtra("plugins_mobno", strmobNo);
                intent1.setComponent(new ComponentName(packageName, packageClassName));
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(intent1);
                } catch (ActivityNotFoundException a) {
                    a.printStackTrace();
                    Toast.makeText(context, "Bizcall Application not found.\nPlease install 'BizcallCRM'", Toast.LENGTH_LONG).show();
                } catch (SecurityException se) {
                    se.printStackTrace();
                    Toast.makeText(context, "Please update your BizcallCRM app.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //function for checking whatsapp installed or not.
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
