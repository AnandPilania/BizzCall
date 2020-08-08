package com.bizcall.wayto.mentebit13;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class PaymentHistoryDetails extends AppCompatActivity
{
    String clienturl,clientid,bankid,bankname,counselorid,acountid,acntname,servicetax;
        ProgressDialog dialog;
        RequestQueue requestQueue;
    EditText edtAmountInINR,edtPaymentReceivedFrom,edtAccountNo,edtRefNo,edtAmtInDollar,edtCurrentDollarRate;
    EditText edtClgFeeYearD,edtHostalFeeYearD,edtOTPFeeD,edtAccidentalInsurance,edtMess,edtAirFeeR,edtIndiaOTC;
    TextView txtReceiptName,txtTotalFeeD,txtTotalFeeR,txtGreaterAmtUSD,txtGreaterAmtINR,txtSubmit,txtFID,txtUpload,txtReset,txtPaymentMode,txtPaymentID;
    TextView txtSelectedDate,txtSelectedDate1,txtSubmitRate,txtAccountId,txtServiceTax,txtFinalAmount,txtSelectAcntName,txtBankName,txtRemarks;

    String paymentid,dtPaymentDate,cPaymentMode,cAmountINR,cAmountUSD,cPaymentFrom,cBankName,cFrmAccountNo,cRefNo,dtReceivedDate,nAccountID,cSTax,cFinalAmount,cCurrentLocalRateD
            ,CollegeFee1YearD,HostalFee1YearD,OneTimeProcessingFeeD,Accidental_Insurance,MessFeeD,IndiaOTC
            ,AirFareFeeR,ReceiptName,Remarks,date1;
    SharedPreferences sp;
    String totalFeeD="0",totalFeeR="0",fileID,candidatename;
    ImageView imgBack;

    private AsyncTask mMyTask;
    private NotificationManager mNotifyManager;
    String strDocName;
    Uri savedImageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history_details);
        requestQueue= Volley.newRequestQueue(PaymentHistoryDetails.this);
        sp=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid=sp.getString("ClientId","");
        clienturl=sp.getString("ClientUrl","");
        counselorid=sp.getString("Id","");
        counselorid=counselorid.replace(" ","");


        edtAmountInINR=findViewById(R.id.edtAmountInINR);
        edtPaymentReceivedFrom=findViewById(R.id.edtPaymentReceivedFrom);
        edtAccountNo=findViewById(R.id.edtAccountNo);
        edtRefNo=findViewById(R.id.edtRefNo);
        edtAmtInDollar=findViewById(R.id.edtAmountInDollar);
        txtSelectedDate=findViewById(R.id.txtSelectedDate);
        txtSelectedDate1=findViewById(R.id.txtSelectedDate1);
        txtReceiptName=findViewById(R.id.txtDownloadReceipt);

        imgBack=findViewById(R.id.img_back);
        edtCurrentDollarRate=findViewById(R.id.edtCurrentDollarRate);
        txtPaymentID=findViewById(R.id.txtPID);
        txtSubmitRate=findViewById(R.id.txtSubmitDollarRate);
        txtAccountId=findViewById(R.id.txtAccountId);
        txtServiceTax=findViewById(R.id.txtServiceTax);
        txtFinalAmount=findViewById(R.id.txtFinalAmount);
        txtSelectAcntName=findViewById(R.id.txtSelectAcntname);
        edtClgFeeYearD=findViewById(R.id.edtCollegeFee1YearD1);
        edtHostalFeeYearD=findViewById(R.id.edtHostalFee1YearD);
        edtOTPFeeD=findViewById(R.id.edtOneTimeProcessingFeeD);
        edtAccidentalInsurance=findViewById(R.id.edtAccidentalInsurance);
        edtMess=findViewById(R.id.edtMess);
        edtAirFeeR=findViewById(R.id.edtAirFareFeeR);
        edtIndiaOTC=findViewById(R.id.edtIndiaOTC);
        txtTotalFeeD=findViewById(R.id.txtTotalFeeYearD);
        txtTotalFeeR=findViewById(R.id.txtTotalFeeYearR);
        txtGreaterAmtUSD=findViewById(R.id.txtGreaterAmtUSD);
        txtGreaterAmtINR=findViewById(R.id.txtGreaterAmtINR);
        txtSubmit=findViewById(R.id.txtSubmitPaymentEntry);
        txtUpload=findViewById(R.id.txtUploadPaymentEntry);
        txtReset=findViewById(R.id.txtResetPaymentEntry);
        txtFID=findViewById(R.id.txtFID);
        txtRemarks=findViewById(R.id.txtRemarks);
        txtPaymentMode=findViewById(R.id.txtPaymentMode);
        txtBankName=findViewById(R.id.txtBankName);
        paymentid=getIntent().getStringExtra("PaymentId");
        fileID=getIntent().getStringExtra("FileNo1");
        txtPaymentID.setText("("+fileID+")("+counselorid+")");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtReceiptName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strDocName=txtReceiptName.getText().toString();
                if(!strDocName.contains(".jpg"))
                {
                    strDocName=strDocName+".jpg";
                }
                String strUrl = "http://anilsahasrabuddhe.in/CRM/wayto965425/PaymentEntry/" + strDocName;
                Log.d("dw", strUrl);
                if(!ReceiptName.contains("NA"))
                {
                    mMyTask = new DownloadTask().execute(stringToURL(strUrl));
                   // Log.d("qqq", strUrl);

                    dialog = new ProgressDialog(PaymentHistoryDetails.this);
                    dialog.setCancelable(true);
                    dialog.setMessage("Downloading " + strDocName);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setProgress(0);
                    dialog.setMax(100);
                    dialog.show();
                }
            }
        });

        if (CheckInternetSpeed.checkInternet(PaymentHistoryDetails.this).contains("0")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentHistoryDetails.this);
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
        } else if (CheckInternetSpeed.checkInternet(PaymentHistoryDetails.this).contains("1")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentHistoryDetails.this);
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
        } else {
            dialog = ProgressDialog.show(PaymentHistoryDetails.this, "", "Getting payment details", true);
           getPaymentHistory();
        }
    }
    protected URL stringToURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private class DownloadTask extends AsyncTask<URL, Void, Bitmap> {
        // Before the tasks execution
        protected void onPreExecute() {
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();

                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result) {

            if (result != null) {
                // Save bitmap to internal storage
                Uri imageInternalUri = saveImageToInternalStorage(result);
                dialog.dismiss();
                DownloadNotification();
            } else {
                dialog.dismiss();
                Toast.makeText(PaymentHistoryDetails.this, "Download failed..", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Initialize ContextWrapper
        File directory = new File(Environment.getExternalStorageDirectory() + "Bizcall/PaymentEntry");
        directory.mkdirs();

        // Create a file to save the image
        File file = new File(directory, strDocName);

        try {
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        savedImageURI = Uri.parse(file.getAbsolutePath());
        Log.d("mypath", savedImageURI + "");
        Toast.makeText(PaymentHistoryDetails.this, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
        // Return the saved image Uri
        return savedImageURI;
    }

    public void DownloadNotification() {
        Random random = new Random();
        int m = random.nextInt(9999-1000)+1000;
        final int NotiId = m;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String id = getString(R.string.default_notification_channel_id); // default_channel_id
        String title = getString(R.string.default_notification_channel_title); // Default Channel
        NotificationCompat.Builder builder;
        if (mNotifyManager == null) {
            mNotifyManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = mNotifyManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 100});
                mNotifyManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(PaymentHistoryDetails.this, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/PaymentEntry/" + strDocName );
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                   FileProvider.getUriForFile(PaymentHistoryDetails.this, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file), "*/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(PaymentHistoryDetails.this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded Image");
            builder.setContentText(strDocName);
            builder.setSubText("Tap to view Document.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        } else {
            builder = new NotificationCompat.Builder(PaymentHistoryDetails.this, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/PaymentEntry/" + strDocName + ".jpg");
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                   FileProvider.getUriForFile(PaymentHistoryDetails.this, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "image/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(PaymentHistoryDetails.this, 0, intent, 0);

            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded Image");
            builder.setContentText(strDocName);
            builder.setSubText("Tap to view Document.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        }
        mNotifyManager.notify(NotiId, builder.build());
    }


    public void getPaymentHistory()
    {
        try {
           String url = clienturl + "?clientid=" + clientid + "&caseid=126&PaymentID="+paymentid;
            Log.d("PaymentHistoryUrl", url);
            if (CheckInternet.checkInternet(PaymentHistoryDetails.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PaymentHistoryRes", response);
                                try {
                                    dialog.dismiss();
                                    if (response.contains("[]")) {
                                        //linearPaymentHistory.setVisibility(View.GONE);
                                        Toast.makeText(PaymentHistoryDetails.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //linearPaymentHistory.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            paymentid= jsonObject1.getString("nPaymentID");
                                            cAmountINR = jsonObject1.getString("cAmountINR");
                                            cAmountUSD = jsonObject1.getString("cAmountUSD");
                                            cPaymentFrom=jsonObject1.getString("cPaymentFrom");
                                            cBankName=jsonObject1.getString("cBankName");
                                            cFrmAccountNo=jsonObject1.getString("cFrmAccountNo");
                                            cRefNo=jsonObject1.getString("cRefNo");
                                            JSONObject jsonObject3=jsonObject1.getJSONObject("dtReceivedDate");
                                            dtReceivedDate=jsonObject3.getString("date");
                                            dtReceivedDate=dtReceivedDate.substring(0,dtReceivedDate.indexOf(" "));
                                            nAccountID=jsonObject1.getString("nAccountID");
                                            cSTax=jsonObject1.getString("cSTax");
                                            cFinalAmount=jsonObject1.getString("cFinalAmount");
                                            cCurrentLocalRateD=jsonObject1.getString("cCurrentLocalRateD");
                                            CollegeFee1YearD=jsonObject1.getString("CollegeFee1YearD");
                                            HostalFee1YearD=jsonObject1.getString("HostalFee1YearD");
                                            OneTimeProcessingFeeD=jsonObject1.getString("OneTimeProcessingFeeD");
                                            Accidental_Insurance=jsonObject1.getString("Accidental_Insurance");
                                            MessFeeD=jsonObject1.getString("MessFeeD");
                                            IndiaOTC=jsonObject1.getString("IndiaOTC");
                                            AirFareFeeR=jsonObject1.getString("AirFareFeeR");
                                            ReceiptName=jsonObject1.getString("ReceiptName");
                                            Remarks=jsonObject1.getString("Remarks");
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("dtPaymentDate");
                                            date1=jsonObject2.getString("date");
                                            dtPaymentDate=date1.substring(0,date1.indexOf(" "));

                                            if(cAmountUSD == null || cAmountUSD.isEmpty() || cAmountUSD.equals("null"))
                                            {
                                                cAmountUSD="0";
                                            }
                                            if(cAmountINR == null || cAmountINR.isEmpty() || cAmountINR.equals("null"))
                                            {
                                                cAmountINR="0";
                                            }

                                        }
                                        if(ReceiptName == null || ReceiptName.isEmpty() || ReceiptName.equals("null")) {
                                            txtReceiptName.setText("NA");
                                        }
                                        else {
                                            txtReceiptName.setText(ReceiptName);
                                        }

                                        edtCurrentDollarRate.setText(cCurrentLocalRateD);
                                        if(dtPaymentDate == null || dtPaymentDate.isEmpty() || dtPaymentDate.equals("null"))
                                        {
                                            txtSelectedDate.setText("NA");
                                        }
                                        else {
                                            txtSelectedDate.setText(dtPaymentDate);
                                        }
                                        if(cPaymentMode == null || cPaymentMode.isEmpty() || cPaymentMode.equals("null")) {
                                            txtPaymentMode.setText("NA");
                                        }
                                        else {
                                            txtPaymentMode.setText(cPaymentMode);
                                        }
                                        if(cAmountINR == null || cAmountINR.isEmpty() || cAmountINR.equals("null"))
                                        {
                                            edtAmountInINR.setText("0");
                                        }
                                        else {
                                            edtAmountInINR.setText(cAmountINR);
                                        }

                                        if(cAmountUSD == null || cAmountUSD.isEmpty() || cAmountUSD.equals("null"))
                                        {
                                            edtAmtInDollar.setText("0");
                                        }
                                        else {
                                            edtAmtInDollar.setText(cAmountUSD);
                                        }
                                        if(cPaymentFrom == null || cPaymentFrom.isEmpty() || cPaymentFrom.equals("null")) {
                                            edtPaymentReceivedFrom.setText("NA");
                                        }
                                        else
                                        {
                                            edtPaymentReceivedFrom.setText(cPaymentFrom);
                                        }

                                        if(cBankName == null || cBankName.isEmpty() || cBankName.equals("null"))
                                        {
                                            txtBankName.setText("NA");
                                        }
                                        else {
                                            txtBankName.setText(cBankName);
                                        }
                                        if(nAccountID == null || nAccountID.isEmpty() || nAccountID.equals("null")) {
                                            edtAccountNo.setText("NA");
                                        }
                                        else {
                                            edtAccountNo.setText(nAccountID);
                                        }
                                        if(cRefNo == null || cRefNo.isEmpty() || cRefNo.equals("null"))
                                        {
                                            edtRefNo.setText("NA");
                                        }
                                        else {
                                            edtRefNo.setText(cRefNo);
                                        }
                                        if(dtReceivedDate == null || dtReceivedDate.isEmpty() || dtReceivedDate.equals("null"))
                                        {
                                            txtSelectedDate1.setText("NA");
                                        }
                                        else {
                                            txtSelectedDate1.setText(dtReceivedDate);
                                        }
                                        if(cFrmAccountNo == null || cFrmAccountNo.isEmpty() || cFrmAccountNo.equals("null"))
                                        {
                                            txtAccountId.setText("NA");
                                        }
                                        else {
                                            txtAccountId.setText(cFrmAccountNo);
                                        }
                                        if(cSTax == null || cSTax.isEmpty() || cSTax.equals("null"))
                                        {
                                            txtServiceTax.setText("NA");
                                        }
                                        else {
                                            txtServiceTax.setText(cSTax);
                                        }
                                        if(cFinalAmount == null || cFinalAmount.isEmpty() || cFinalAmount.equals("null")) {
                                            txtFinalAmount.setText("0");
                                        }
                                        else {
                                            txtFinalAmount.setText(cFinalAmount);
                                        }
                                        if(CollegeFee1YearD == null || CollegeFee1YearD.isEmpty() || CollegeFee1YearD.equals("null"))
                                        {
                                            edtClgFeeYearD.setText("0");
                                        }
                                        else {
                                            edtClgFeeYearD.setText(CollegeFee1YearD);
                                        }
                                        if(HostalFee1YearD == null || HostalFee1YearD.isEmpty() || HostalFee1YearD.equals("null")) {
                                            edtHostalFeeYearD.setText("0");
                                        }
                                        else {
                                            edtHostalFeeYearD.setText(HostalFee1YearD);
                                        }
                                        if(OneTimeProcessingFeeD == null || OneTimeProcessingFeeD.isEmpty() || OneTimeProcessingFeeD.equals("null"))
                                        {
                                            edtOTPFeeD.setText("0");
                                        }
                                        else
                                        {
                                            edtOTPFeeD.setText(OneTimeProcessingFeeD);
                                        }
                                        if(Accidental_Insurance == null || Accidental_Insurance.isEmpty() || Accidental_Insurance.equals("null"))
                                        {
                                            edtAccidentalInsurance.setText("0");
                                        }
                                        else {
                                            edtAccidentalInsurance.setText(Accidental_Insurance);
                                        }
                                        if(MessFeeD == null || MessFeeD.isEmpty() || MessFeeD.equals("null")) {
                                            edtMess.setText("0");
                                        }
                                        else {
                                            edtMess.setText(MessFeeD);
                                        }
                                        if(AirFareFeeR == null || AirFareFeeR.isEmpty() || AirFareFeeR.equals("null"))
                                        {
                                            edtAirFeeR.setText("0");
                                        }
                                        else {
                                            edtAirFeeR.setText(AirFareFeeR);
                                        }

                                        if(IndiaOTC == null || IndiaOTC.isEmpty() || IndiaOTC.equals("null")) {
                                            edtIndiaOTC.setText("0");
                                        }
                                        else {
                                            edtIndiaOTC.setText(IndiaOTC);
                                        }
                                        if(totalFeeD == null || totalFeeD.isEmpty() || totalFeeD.equals("null")) {
                                            txtTotalFeeD.setText("0");
                                        }
                                        else {
                                            txtTotalFeeD.setText(totalFeeD);
                                        }
                                        if(totalFeeR == null || totalFeeR.isEmpty() || totalFeeR.equals("null")) {
                                            txtTotalFeeR.setText("0");
                                        }
                                        else {
                                            txtTotalFeeR.setText(totalFeeR);
                                        }
                                        if(Remarks == null || Remarks.isEmpty() || Remarks.equals("null"))
                                        {
                                            txtRemarks.setText("NA");
                                        }
                                        else {
                                            txtRemarks.setText(Remarks);
                                        }

                                       // txtSelectAcntName.setText();


                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(PaymentHistoryDetails.this,"Errorcode-347 CollegeAttorny getCollegeListResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentHistoryDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(PaymentHistoryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentHistoryDetails.this);
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
        }catch (Exception e)
        {
            Toast.makeText(PaymentHistoryDetails.this,"Errorcode-346 CollegeAttorny getCollegeList "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }
}
