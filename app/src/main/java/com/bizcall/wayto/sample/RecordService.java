package com.bizcall.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;

public class RecordService
{
    public static final String TAGS = " Inside Service";
    public static MediaRecorder recorder;
    public static AudioManager audioManager;
    public  static MediaMetadataRetriever mmr;
    public static SharedPreferences sp;
    public static  String clientid, sr_no;
   public static SharedPreferences.Editor editor;
    public static long timeInMilliseconds = 0L;
    public static long timeSwapBuff = 0L;
    public static long updatedTime = 0L;
    UrlRequest urlRequest;
   public static String duration1, counselorid, phoneNumber, date,clienturl;
    ProgressDialog dialog;
    static Context context;


    public static long startHTime = 0L;
    public static Handler customHandler = new Handler();

        public RecordService(Context context)
        {
            this.context=context;
        }
    /*

       @Override
        public void onCreate() {

            Intent notificationIntent = new Intent(this, Home.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.bizcall)
                    .setContentTitle("My Awesome App")
                    .setContentText("Doing some work...")
                    .setContentIntent(pendingIntent).build();

            startForeground(1337, notification);
            super.onCreate();
        }
    */
   static Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
           /* Log.d("ADuration", "" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));*/
            duration1 = "" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs);
            customHandler.postDelayed(this, 0);
        }
    };

   public static void startRecord(){
     Log.d("InStart","1");
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        sr_no = sp.getString("Sr.No", null);
        counselorid=sp.getString("Id",null);
        counselorid=counselorid.replaceAll(" ","");
        recorder = new MediaRecorder();
        recorder.reset();
        timeSwapBuff=0L;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mmr = new MediaMetadataRetriever();
        //audioManager.setMode(AudioManager.MODE_IN_CALL);
        phoneNumber=PhoneStateReceiver.phoneNumber;
       Log.d("NumberAfter",phoneNumber+"mona");
       /* phoneNumber=intent.getStringExtra("CNumber");
        if(TextUtils.isEmpty(phoneNumber)){
            phoneNumber=sp.getString("StateNumber",null);

        }
        Log.d("NumberPIE",phoneNumber+"mona");*/


       /* if(!TextUtils.isEmpty(phoneNumber))
        {
            Log.d("Phone",phoneNumber);
        }
        else
        {
            phoneNumber="9096844152";
        }*/
         Log.d(TAGS, "Phone number in service: "+phoneNumber);

        String time = new CommonMethods().getTIme();
        time = time.replaceAll(" ", "");
        date = new CommonMethods().getDate();

        String path = new CommonMethods().getPath();
        /* Uri uri = Uri.parse(pathStr);*/
       /* mmr.setDataSource(path);

        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);*/
        String rec = path + "/" +sr_no + "_" + date + "_" + time + ".mp3";
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            if (Build.VERSION.SDK_INT >= 10) {
                recorder.setAudioSamplingRate(44100);
                recorder.setAudioEncodingBitRate(96000);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            } else {
                // older version of Android, use crappy sounding voice codec
               recorder.setAudioSamplingRate(11000);
                recorder.setAudioEncodingBitRate(12200);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            }
            Log.d("RecPath", rec);
            recorder.setOutputFile(rec);
            editor = sp.edit();
            editor.putString("RecordedFile", rec);
            editor.putString("AudioDuration", duration1);
            editor.commit();
            } catch (RuntimeException ex) {
        }
        try {
            recorder.prepare();
            recorder.start();
            startHTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // audioManager.setMode(AudioManager.MODE_IN_CALL);
        //audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);

try {
    audioManager.setMode(AudioManager.MODE_IN_CALL);
    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);

    Log.d(TAGS, "onStartCommand: " + "Recording started");
}
catch (Exception e)
{
    Log.d("Exception", String.valueOf(e));
}
        // onTaskRemoved(intent);


    }

    public static void onStop() {

        try {
            Log.d("InStop","1");
          //  dialog = ProgressDialog.show(RecordService.this, "", "Loading...", true);
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
            /*counselorid = sp.getString("Id", null);
            counselorid = counselorid.replaceAll(" ", "");
            phoneNumber=phoneNumber.replace("+91","");*/
            //checkNumber(counselorid,phoneNumber);
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            Log.d("DurRecord", duration1);
            editor = sp.edit();
            editor.putString("Dur", duration1);
          //  editor.putString("SelectedSrNo",sr_no);
           String srno=sp.getString("SelectedSrNo",null);
           editor.commit();


           /* Intent intent1 = new Intent(context, CounselorContactActivity.class);
            intent1.putExtra("ActivityName","ServiceRecord");
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);*/
            Log.d(TAGS, "onDestroy: " + "Recording stopped");

        } catch (RuntimeException ex) {
                Log.d("Exception", String.valueOf(ex));
        }
    }

 /*   public void checkNumber(String cid, final String phoneNumber1) {
        // dialog = ProgressDialog.show(RecordService.this, "Loading", "Please wait.....", false, true);
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=9&CounselorId=" + cid + "&cMobile=" + phoneNumber1);
        Log.d("CheckUrl", clienturl+"?clientid=" + clientid + "&caseid=9&CounselorId=" + cid + "&cMobile=" + phoneNumber1);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
               // dialog.dismiss();

                Log.d("CheckResponse", response);
                if (response.contains("Present")) {
                    Intent intent = new Intent(RecordService.this, CounselorContactActivity.class);
                    intent.putExtra("ActivityName","ServiceRecord");
                  //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(RecordService.this, SplashNumberNotPresent.class);
                    intent.putExtra("Phone", phoneNumber1);
                    intent.putExtra("Date", date);
                  //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }*/
  /*  @Override
    public void onTaskRemoved(Intent rootIntent)
    {
      *//*  Intent notificationIntent = new Intent(this, Home.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bizcall)
                .setContentTitle("My Awesome App")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent).build();
                startForeground(1337, notification);*//*
     *//*         AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getPackageName());

        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, pendingIntent);*//*
     *//*AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePI);*//*
        rootIntent=new Intent(this,PhoneStateReceiver.class);
        sendBroadcast(rootIntent);
        super.onTaskRemoved(rootIntent);
    }*/
}
