package com.example.wayto.sample;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity {


    private RelativeLayout imageRelativeLayout;
    private LinearLayout distanceLinearLayout, gemsLinearLayout, coinLinearLayout;
    private TextView txtTitle, txtAirtime, txtBackflip, txtFlip, txtNeckflip,txtUsername,txtCoins;
    String clientid,clienturl,totalcoins;
    ImageView imgBack;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        imageRelativeLayout = findViewById(R.id.mrelativelayout);
        distanceLinearLayout = findViewById(R.id.mdistance);
        gemsLinearLayout = findViewById(R.id.mgems);
        coinLinearLayout = findViewById(R.id.mcoin);
        txtTitle = findViewById(R.id.mtitle);
        txtAirtime = findViewById(R.id.u_airtime);
        txtBackflip = findViewById(R.id.u_backflip);
        txtFlip = findViewById(R.id.u_flip);
        txtNeckflip = findViewById(R.id.u_neckflip);
        txtUsername=findViewById(R.id.txtUserName);
        txtCoins=findViewById(R.id.txtCoins);
        imgBack=findViewById(R.id.img_back);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);

        totalcoins=sp.getString("TotalCoin",null);
        txtUsername.setText(clientid);
        txtCoins.setText(totalcoins);

        ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageRelativeLayout, "scaleX", 0f, 1f).setDuration(500);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(txtTitle, "scaleY", 0f, 1f).setDuration(500);
       /* ObjectAnimator oa3 = ObjectAnimator.ofFloat(distanceLinearLayout, "scaleY", 0f, 1f).setDuration(500);
        ObjectAnimator oa4 = ObjectAnimator.ofFloat(gemsLinearLayout, "scaleY", 0f, 1f).setDuration(500);*/
        ObjectAnimator oa5 = ObjectAnimator.ofFloat(coinLinearLayout, "scaleY", 0f, 1f).setDuration(500);
        ObjectAnimator oa6 = ObjectAnimator.ofFloat(txtAirtime, "scaleY", 0f, 1f).setDuration(500);
        ObjectAnimator oa7 = ObjectAnimator.ofFloat(txtBackflip, "scaleY", 0f, 1f).setDuration(500);
        ObjectAnimator oa8 = ObjectAnimator.ofFloat(txtFlip, "scaleY", 0f, 1f).setDuration(500);
        ObjectAnimator oa9 = ObjectAnimator.ofFloat(txtNeckflip, "scaleY", 0f, 1f).setDuration(500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(oa1, oa2, oa5, oa6, oa7, oa8, oa9);//oa3, oa4
        animatorSet.start();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}


