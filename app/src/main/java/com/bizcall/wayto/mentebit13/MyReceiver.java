package com.bizcall.wayto.mentebit13;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MyReceiver extends BroadcastReceiver {
    String time, date, number, savedname;
    long val;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    RecordService recordService;
    MediaRecorder recorder;
    AudioManager audioManager;
    DBHelper myDB;
    DataLeadInfo dataLeadInfo;
    Context mContext;

    @SuppressLint({"NewApi", "MissingPermission"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(final Context context, final Intent intent) {
        //String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        int state = tm.getCallState();
        Log.d("statetype", state+"");

        //final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int flag = sp.getInt("recordflag", 0);
        editor = sp.edit();
        editor.putString("RecordedFile", null);
        editor.apply();
        recordService = new RecordService(context);
        recorder = new MediaRecorder();
        recorder.reset();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mContext = context;

        number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        myDB = new DBHelper(context);

        if (state == TelephonyManager.CALL_STATE_OFFHOOK) {//Outgoing call

            if (number != null) {
                Log.d("OutgoingNO", number);

                time = new CommonMethods().getTIme();
                time = time.replaceAll(" ", "");
                date = new CommonMethods().getDate();
                number = number.replace(" ", "").substring((number.length()) - 10);
                String path = new CommonMethods().getPath();
                String rec = path + "/" + number + "_" + date + "_" + time + "_outgoing.mp3";
                String filename = number + "_" + date + "_" + time + "_outgoing.mp3";

                getContactDetail(number);

                //---------------------------Floating Window-----------------------
                Log.d("nmno", savedname + " / " + number);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    Intent intent1 = new Intent(context, FloatingViewService.class);
                    intent1.putExtra("callnumber", savedname);
                    intent1.putExtra("mobno", number);
                    intent1.putExtra("audioname", filename);
                    context.startForegroundService(intent1);
                    //finish();
                } else if (Settings.canDrawOverlays(context)) {
                    Intent intent2 = new Intent(context, FloatingViewService.class);
                    intent2.putExtra("callnumber", savedname);
                    intent2.putExtra("mobno", number);
                    intent2.putExtra("audioname", filename);
                    context.startForegroundService(intent2);  //startService
                    //finish();
                }

                if (flag != 1) {
                    Log.d("logconname1", savedname + " / " + number + " / " + flag);
                    dataLeadInfo = new DataLeadInfo(savedname, number, "OUTGOING", new CommonMethods().getDate(), new CommonMethods().getTIme(), filename, "0");
                    val = myDB.insertLeadInfoNumber(dataLeadInfo);
                }

              /*  editor = sp.edit();
                editor.putInt("recordflag", 0);
                editor.putString("RecordedFile1", rec);
                editor.commit();*/
              /*  try {
                    recordService.startRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(context, "Exception in start recording" + e.toString(), Toast.LENGTH_SHORT).show();
                }*/
            }
        } else if (state == TelephonyManager.CALL_STATE_RINGING) {//Incoming call

            if (number != null) {
                Log.d("IncomingNo", number);

                /*String callForwardString = "8275021492";
                Intent intentCallForward = new Intent(Intent.ACTION_CALL);
                Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
                intentCallForward.setData(uri2);
                intentCallForward.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE}, 101);
                } else {
                    try {
                        mContext.startActivity(intentCallForward);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }*/

                time = new CommonMethods().getTIme();
                time = time.replaceAll(" ", "");
                date = new CommonMethods().getDate();
                number = number.replace(" ", "").substring((number.length()) - 10);
                String path = new CommonMethods().getPath();
                String rec = path + "/" + number + "_" + date + "_" + time + "_incoming.mp3";
                String filename = number + "_" + date + "_" + time + "_incoming.mp3";

                getContactDetail(number);

                //---------------------------Floating Window-----------------------
                Log.d("nmno", savedname + " / " + number);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    Intent intent1 = new Intent(context, FloatingViewService.class);
                    intent1.putExtra("callnumber", savedname);
                    intent1.putExtra("mobno", number);
                    intent1.putExtra("audioname", filename);
                    context.startForegroundService(intent1);
                    //finish();
                } else if (Settings.canDrawOverlays(context)) {
                    Intent intent2 = new Intent(context, FloatingViewService.class);
                    intent2.putExtra("callnumber", savedname);
                    intent2.putExtra("mobno", number);
                    intent2.putExtra("audioname", filename);
                    context.startForegroundService(intent2);  //startService
                    //finish();
                }

                Log.d("logconname2", savedname + " / " + number);
                dataLeadInfo = new DataLeadInfo(savedname, number, "INCOMING", new CommonMethods().getDate(), new CommonMethods().getTIme(), filename, "0");
                val = myDB.insertLeadInfoNumber(dataLeadInfo);

                editor = sp.edit();
                editor.putInt("recordflag", 1);
                editor.putString("RecordedFile1", rec);
                editor.commit();

               /* try {
                    recordService.startRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(context, "Exception in start recording" + e.toString(), Toast.LENGTH_SHORT).show();
                }*/
            }
        } else if (state == TelephonyManager.CALL_STATE_IDLE) {//Idle state
            Log.d("tag", "Idle State..."+ state);
           /* try {
              //  recordService.onStop();
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(context, "Exception in stop recording", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    public void getContactDetail(String number) {
        Cursor cursor = myDB.getContactName(number);
        if (cursor.moveToPosition(0)) {
            savedname = cursor.getString(0);
            Log.d("receivername", savedname);
        } else {
            savedname = number;
            Log.d("receivername", savedname);
        }
    }
}
