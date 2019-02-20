package com.example.wayto.sample;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class PhoneStateReceiver extends BroadcastReceiver {

    static final String TAG = "State";
    static final String TAG1 = " Inside State";
    public static String phoneNumber;
    public static String name;
    static Boolean recordStarted;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        //   Boolean switchCheckOn = pref.getBoolean("switchOn", true);

        //  if (switchCheckOn) {
        try {
            System.out.println("Receiver Start");

            // boolean callWait=pref.getBoolean("recordStarted",false);
            Bundle extras = intent.getExtras();
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            Log.d(TAG, " onReceive: " + state);
            Toast.makeText(context, "Call detected(Incoming/Outgoing) " + state, Toast.LENGTH_SHORT).show();
               /* WakeLocker.acquire(context);
                // do something
                WakeLocker.release();*/

            if (extras != null) {

                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    Log.d(TAG1, " Inside " + state);
                    /*int j=pref.getInt("numOfCalls",0);
                    pref.edit().putInt("numOfCalls",++j).apply();
                    Log.d(TAG, "onReceive: num of calls "+ pref.getInt("numOfCalls",0));*/
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)/*&& pref.getInt("numOfCalls",1)==1*/) {
                    int j = pref.getInt("numOfCalls", 0);
                    pref.edit().putInt("numOfCalls", ++j).apply();
                    Log.d(TAG, "onReceive: num of calls " + pref.getInt("numOfCalls", 0));
                    Log.d(TAG1, " recordStarted in offhook: " + recordStarted);
                    Log.d(TAG1, " Inside " + state);
                    if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {
                        phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                        Log.d("PHONE1111", "outgoing,ringing:" + phoneNumber);
                    } else {
                        phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        Log.d(TAG1, " Phone Number in receiver " + phoneNumber);
                    }
                    ServiceConnection mConnection = new ServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName name, IBinder service) {
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName name) {

                        }
                    };
                    if (pref.getInt("numOfCalls", 1) == 1) {
                        Intent reivToServ = new Intent(context, RecordService.class);
                        reivToServ.putExtra("number", phoneNumber);
                        context.startService(reivToServ);

                            /*context.bindService(new Intent(context,RecordService.class),
                                    mConnection, Context.BIND_AUTO_CREATE);*/
                          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(reivToServ);
                            }*/


                        //name=new CommonMethods().getContactName(phoneNumber,context);

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

                    }
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    int k = pref.getInt("numOfCalls", 1);
                    pref.edit().putInt("numOfCalls", --k).apply();
                    int l = pref.getInt("numOfCalls", 0);
                    Log.d(TAG1, " Inside " + state);
                    recordStarted = pref.getBoolean("recordStarted", false);
                    Log.d(TAG1, " recordStarted in idle :" + recordStarted);
                    if (recordStarted && l == 0) {
                        Log.d(TAG1, " Inside to stop recorder " + state);

                        context.stopService(new Intent(context, RecordService.class));
                        pref.edit().putBoolean("recordStarted", false).apply();
                           /* if(isAppForground(context))
                            {
                                Intent intent1 = new Intent(context, SplashAfterCall.class);
                                context.startActivity(intent1);
                            }*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAppForground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }
        return true;
    }
}

