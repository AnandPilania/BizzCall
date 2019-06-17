package com.bizcall.wayto.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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

    String clientid, sr_no;

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    UrlRequest urlRequest;
    String duration1, counselorid, date,clienturl;

    private long startHTime = 0L;
    private Handler customHandler = new Handler();

        RecordService recordService;

    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        editor=sp.edit();

        recordService=new RecordService(context);

        recorder = new MediaRecorder();
        recorder.reset();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mmr = new MediaMetadataRetriever();

        //   Boolean switchCheckOn = pref.getBoolean("switchOn", true);

        //  if (switchCheckOn) {
        try {
            System.out.println("Receiver Start");
            final TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            //telephony.EXTRA_STATE;
            telephony.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber)
                {
                    super.onCallStateChanged(state,incomingNumber);
                    phoneNumber=incomingNumber;
                    mbl=incomingNumber;
                    editor.putString("StateNumber",incomingNumber);
                    editor.commit();
                    /* //reivToServ.putExtra("CALLEDNO", incomingNumber);*/

                    Log.d("PieincomingNumber : ",incomingNumber);


                    if (state==1) {
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

                        int j = pref.getInt("numOfCalls", 0);
                        pref.edit().putInt("numOfCalls", ++j).apply();
                        Log.d(TAG, "onReceive: num of calls " + pref.getInt("numOfCalls", 0));
                        Log.d(TAG1, " recordStarted in offhook: " + recordStarted);
                        Log.d(TAG1, " Inside " + state);
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O)
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
                        }
                       /* else
                        {

                            phoneNumber=incomingNumber;
                            Log.d("OMG","This is action call on pie"+phoneNumber);
                            editor.putString("StateNumber",phoneNumber);
                            editor.commit();
                        }*/

                        if (pref.getInt("numOfCalls", 1) == 1)
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
                            }
                            // Intent reivToServ = new Intent(context, RecordService.class);
                          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(reivToServ);

                            }
                            else
                            {*/
                                recordService.startRecord();
                               // context.startService(reivToServ);
                                                 //}

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

                           /* Intent intent1 = new Intent(context, CounselorContactActivity.class);
                            intent1.putExtra("ActivityName","ServiceRecord");

                         //   editor.commit();

                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);*/
                               /* Intent intent1 = new Intent(context, CounselorContactActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);*/

                           // new RecordService().stopSelf();
                            Toast.makeText(context, "Call detected(Incoming/Outgoing) " + state, Toast.LENGTH_SHORT).show();
                            }
                    }

                }
            },PhoneStateListener.LISTEN_CALL_STATE);


            // boolean callWait=pref.getBoolean("recordStarted",false);
            //  Bundle extras = intent.getExtras();
            //  String state = extras.getString(TelephonyManager.EXTRA_STATE);
            //   Log.d(TAG, " onReceive: " + state);
           /* if(Build.VERSION.SDK_INT >= 26 && intent!=null && intent.getExtras() !=null
                    && TextUtils.isEmpty(intent.getExtras().getString("incoming_number"))){
                return;
            }*/
            //   Toast.makeText(context, "Call detected(Incoming/Outgoing) " + state, Toast.LENGTH_SHORT).show();
               /* WakeLocker.acquire(context);
                // do something
                WakeLocker.release();*/

            // if (extras != null)
           /* if(Build.VERSION.SDK_INT >= 26 && extras!=null && intent.getExtras() !=null
                    && TextUtils.isEmpty(intent.getExtras().getString("incoming_number"))){
*/
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
    }


}
