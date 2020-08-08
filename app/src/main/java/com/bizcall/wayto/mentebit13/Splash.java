package com.bizcall.wayto.mentebit13;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Timer;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sp;
    String uname, id1;
    private ImageView iv;
    RequestQueue requestQueue;
    private SimpleDateFormat sdfComapre;
    String IMEINumber1, IMEINumber2;
    final int TIME_INTERVAL = 1800000;
    Timer timer = new Timer();
    double currentLat, currentLong;
    LatLng latLng;
    android.app.AlertDialog.Builder builder;
    android.app.AlertDialog alert;
   // final int TIME_INTERVAL = 1800000; //half hour.1800000
    String clienturl, clientid, checkPer;
    public static int flag = 0;
    GoogleMap googleMap;
    MapsActivity mapsActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        uname = sp.getString("Name", null);
        id1 = sp.getString("Id", null);
      //  IMEINumber1 = sp.getString("IMEI1", null);
        //IMEINumber2 = sp.getString("IMEI2", null);
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        checkPer = sp.getString("CheckPermission", null);
        Animation anim = AnimationUtils.loadAnimation(Splash.this, R.anim.fadeout);
        iv = findViewById(R.id.imageSplash);
        iv.setAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (uname == null && id1 == null) {
                    Intent i = new Intent(Splash.this, Login.class);
                    i.putExtra("ActLogin","Splash");
                    startActivity(i);
                } else {
                  /*  if (checkPer != null) {
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                setAlarm();
                                Log.d("flagSplash", ":flagSplash");
                            }
                        }, 0, TIME_INTERVAL);
                    }*/
                    Intent i = new Intent(Splash.this, Home.class);
                    i.putExtra("Activity", "Splash");
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @SuppressLint("ShortAlarm")
    private void setAlarm() {
        flag = 1;
        deleteCache(Splash.this);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
      /*  Intent intent = new Intent(Splash.this, MyAlarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(Splash.this, 0, intent, 0);
        manager.set(AlarmManager.RTC, 0, pendingIntent);*/

        // Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
        // manager.cancel(pendingIntent);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
