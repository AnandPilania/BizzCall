package com.bizcall.wayto.mentebit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class LeadCount extends AppCompatActivity {

    WebView webViewLeadCount;
    ImageView imgBack;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lead_count);
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        imgBack=findViewById(R.id.img_back);
        webViewLeadCount=findViewById(R.id.webViewLeadCount);
        if(CheckInternetSpeed.checkInternet(LeadCount.this).contains("0")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LeadCount.this);
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
        }
        else if(CheckInternetSpeed.checkInternet(LeadCount.this).contains("1")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LeadCount.this);
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
        }
        else {
            webViewLeadCount.setWebViewClient(new LeadCount.MyBrowser());
            String url = "http://anilsahasrabuddhe.in/rohit-testing/reportsnew/new1.php";
            webViewLeadCount.getSettings().setLoadsImagesAutomatically(true);
            webViewLeadCount.getSettings().setJavaScriptEnabled(true);
            webViewLeadCount.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webViewLeadCount.loadUrl(url);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
            }
        });

    }
    private class MyBrowser extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(LeadCount.this,Home.class);
        intent.putExtra("Activity","LeadCount");
        startActivity(intent);
       // super.onBackPressed();
    }
}
