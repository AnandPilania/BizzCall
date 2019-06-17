package com.bizcall.wayto.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sp;
    String uname, id1;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        uname = sp.getString("Name", null);
         id1=sp.getString("Id",null);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        iv = (ImageView) findViewById(R.id.imageSplash);
        iv.setAnimation(anim);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                if (uname==null&&id1==null) {
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(Splash.this, Home.class);
                    i.putExtra("Activity","Splash");
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
