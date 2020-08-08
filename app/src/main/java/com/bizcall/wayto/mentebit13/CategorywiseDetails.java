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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Random;

public class CategorywiseDetails extends AppCompatActivity {


    String categoryname,categorytype,amounttype,clienturl,clientid,counselorid;
    RequestQueue requestQueue;
    ProgressDialog dialog;
    SharedPreferences sp;
    TextView txtActivityname,txtNotFound,txtAssigned,txtDate,txtCategoryName,txtApproved,txtAmount,txtMemo,txtAdminRemarks,txtDownloadReceipt,txtCategoryType;
    LinearLayout linearCard;
    DataBalanceDetails dataBalanceDetails;
    ArrayList<DataBalanceDetails> arrayListBalanceDetails;
    ImageView imgBack;
    String amount,date,category,remarks,memo,categoryName,approvedBy,approvedRemarks,receiptname,from,you;
    private AsyncTask mMyTask;
    private NotificationManager mNotifyManager;
    String strDocName;
    Uri savedImageURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue= Volley.newRequestQueue(CategorywiseDetails.this);
        setContentView(R.layout.activity_categorywise_details);
        sp=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid=sp.getString("ClientId","");
        clienturl=sp.getString("ClientUrl","");
        counselorid=sp.getString("Id","");
        counselorid=counselorid.replace(" ","");
        imgBack=findViewById(R.id.img_back);
        txtActivityname=findViewById(R.id.txtActivityName);
        txtAssigned=findViewById(R.id.txtAssigned);
        txtDate=findViewById(R.id.txtDate);
        txtCategoryName=findViewById(R.id.txtCategoryName);
        txtApproved=findViewById(R.id.txtApproved);
        txtAmount=findViewById(R.id.txtAmount);
        txtMemo=findViewById(R.id.txtMemo);
        txtAdminRemarks=findViewById(R.id.txtAdminRemarks);
        txtDownloadReceipt=findViewById(R.id.txtDownloadReceipt);
        linearCard=findViewById(R.id.linearCard);
        categorytype=getIntent().getStringExtra("CategoryType");
        Log.d("Category",categorytype);
        categoryname=getIntent().getStringExtra("CategoryName");
        amounttype=getIntent().getStringExtra("AmountType");
        txtActivityname.setText(categorytype);
        arrayListBalanceDetails=new ArrayList<>();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dialog=ProgressDialog.show(CategorywiseDetails.this,"","Loading details",true);
        getBalanceDetails(amounttype);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent=new Intent(CategorywiseDetails.this,AccountsActivity.class);
        startActivity(intent);
        finish();
    }

    public void getBalanceDetails(final String type) {
        try {
            if(CheckServer.isServerReachable(CategorywiseDetails.this)) {
                String url = clienturl + "?clientid=" + clientid + "&caseid=112&cAmountType="+type+"&CounselorID="+counselorid+"&CategoryTpe="+categorytype+"&CategoryName="+categoryname;
                Log.d("BalanceDetailsUrl", url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Log.d("BalanceDetailsResponse1", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                         date=jsonObject1.getString("Expense Date");
                                         category=jsonObject1.getString("CategoryType");
                                         amount=jsonObject1.getString("cAmount");
                                          memo=jsonObject1.getString("cmemo");
                                         remarks=jsonObject1.getString("cRemarks");
                                         categoryName=jsonObject1.getString("CategoryName");
                                         approvedBy=jsonObject1.getString("ApprovedBy");
                                         approvedRemarks=jsonObject1.getString("ApprovedRemarks");
                                          receiptname=jsonObject1.getString("ReceiptFileName");
                                         from=jsonObject1.getString("From");
                                         you=jsonObject1.getString("You");
                                         dataBalanceDetails=new DataBalanceDetails(category,amount,date,memo,remarks,categoryName,approvedBy,approvedRemarks,receiptname,type,from,you);
                                         arrayListBalanceDetails.add(dataBalanceDetails);
                                    }

                                        txtDate.setText(date);
                                        String amounttype = type;
                                        if (amounttype.contains("INR")) {
                                            txtAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rupee1, 0, 0, 0);
                                        } else {
                                            txtAmount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dollar1, 0, 0, 0);
                                        }
                                        txtDownloadReceipt.setText(receiptname);
                                        txtAmount.setText(amount);
                                        txtCategoryName.setText(categoryName);
                                        String adminremarks = approvedRemarks;
                                        txtApproved.setText("ApprovedStatus: " + approvedBy);
                                        txtMemo.setText(memo);
                                        Log.d("From",from);
                                        if (categorytype.equals("Expenses")) {
                                            if (from.equals("Not Allocated")) {
                                                txtAssigned.setText(you);
                                            } else {
                                                txtAssigned.setText(you + " --> " + from);
                                            }
                                        } else {
                                            if (from.equals("Not Allocated")) {
                                                txtAssigned.setText(you);
                                            } else {
                                                txtAssigned.setText(you+ " <-- " + from);
                                            }
                                        }
                                    txtDownloadReceipt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            strDocName = receiptname;
                                            if (!strDocName.contains(".jpg")) {
                                                strDocName = strDocName + ".jpg";
                                            }
                                            String strUrl = "http://anilsahasrabuddhe.in/CRM/wayto965425/CashOutReceipt/" + strDocName;
                                            Log.d("dw", strUrl);
                                            if (!receiptname.equals("NA")) {
                                                mMyTask = new DownloadTask().execute(stringToURL(strUrl));
                                                Log.d("qqq", strUrl);
                                                dialog = new ProgressDialog(CategorywiseDetails.this);
                                                dialog.setCancelable(true);
                                                dialog.setMessage("Downloading " + strDocName);
                                                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                dialog.setProgress(0);
                                                dialog.setMax(100);
                                                dialog.show();
                                            }
                                        }
                                    });

                                } catch (JSONException e) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(CategorywiseDetails.this, "Errorcode-205 CounselorContact statusResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error == null || error.networkResponse == null)
                                    return;
                                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CategorywiseDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(CategorywiseDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CategorywiseDetails.this);
                alertDialogBuilder.setTitle("Network issue!!!!")
                        .setMessage("Try after some time!")

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
            Toast.makeText(CategorywiseDetails.this,"Errorcode-204 CounselorContact getStatus1 "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcStatus", String.valueOf(e));
        }
    }
    protected URL stringToURL(String urlString)
    {
        try
        {
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
            try
            {
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
                Toast.makeText(CategorywiseDetails.this, "Download failed..", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Initialize ContextWrapper
        File directory = new File(Environment.getExternalStorageDirectory() + "/CashOutReceipt");
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
        Toast.makeText(CategorywiseDetails.this, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
        // Return the saved image Uri
        return savedImageURI;
    }

    public void DownloadNotification() {
        Random random = new Random();
        int m = random.nextInt(9999-1000)+1000;
        final int NotiId = m;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String id = CategorywiseDetails.this.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = CategorywiseDetails.this.getString(R.string.default_notification_channel_title); // Default Channel
        NotificationCompat.Builder builder;
        if (mNotifyManager == null) {
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
            builder = new NotificationCompat.Builder(CategorywiseDetails.this, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/CashOutReceipt/" + strDocName );
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                   FileProvider.getUriForFile(CategorywiseDetails.this, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file), "*/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(CategorywiseDetails.this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded Image");
            builder.setContentText(strDocName);
            builder.setSubText("Tap to view Document.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        } else {
            builder = new NotificationCompat.Builder(CategorywiseDetails.this, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Appli/" + strDocName + ".jpg");
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                    FileProvider.getUriForFile(CategorywiseDetails.this, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "image/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(CategorywiseDetails.this, 0, intent, 0);

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
}
