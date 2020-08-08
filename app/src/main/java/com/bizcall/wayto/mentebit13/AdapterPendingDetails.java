package com.bizcall.wayto.mentebit13;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class AdapterPendingDetails extends RecyclerView.Adapter<AdapterPendingDetails.MyHolder> {
    ArrayList<DataPendingDetails> accountsArrayList;
    Context context;
    RequestQueue requestQueue;
    EditText edtNewRemark;
    String strDocName;
    Uri savedImageURI;
    private AsyncTask mMyTask;
    private NotificationManager mNotifyManager;

    public AdapterPendingDetails(ArrayList<DataPendingDetails> accountsArrayList, Context context) {
        this.accountsArrayList = accountsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_pending_details, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        requestQueue = Volley.newRequestQueue(context);
        edtNewRemark = new EditText(context);

        if (i % 2 == 0) {
            myHolder.linearCard.setBackgroundResource(R.drawable.balance_card_border);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            myHolder.linearCard.setBackgroundResource(R.drawable.balance_card_border1);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        final DataPendingDetails dataBalanceDetails = accountsArrayList.get(i);
        myHolder.txtDate.setText(dataBalanceDetails.getpExpenceDate());
        String amounttype=dataBalanceDetails.getpAmountType();
        if(amounttype.contains("INR")) {
            myHolder.txtAmount.setCompoundDrawablesWithIntrinsicBounds(  R.drawable.rupee1, 0,0, 0);
        }
        else if (amounttype.contains("USD")){
            myHolder.txtAmount.setCompoundDrawablesWithIntrinsicBounds(  R.drawable.dollar1, 0,0, 0);
        }
        myHolder.txtAmount.setText(dataBalanceDetails.getpAmount());
        myHolder.txtCategoryName.setText(dataBalanceDetails.getpCategoryName());
        myHolder.txtAdminRemarks.setText(dataBalanceDetails.getpRemark());
        myHolder.txtApproved.setText("ApprovedStatus: " + dataBalanceDetails.getpApprovedBy());
        myHolder.txtMemo.setText(dataBalanceDetails.getpMemo());
        myHolder.txtDownloadReceipt.setText(dataBalanceDetails.getpReceiptFileName());
        myHolder.txtAssigned.setText(dataBalanceDetails.getpFrom() + " --> " + dataBalanceDetails.getpYou());

        myHolder.txtnewremark.setPaintFlags(myHolder.txtnewremark.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        myHolder.txtnewremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                edtNewRemark.setLayoutParams(lp);
                new AlertDialog.Builder(context)
                        .setTitle("Add Remark")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myHolder.txtnewremark.setText("Remark :- "+edtNewRemark.getText().toString());
                                myHolder.txterrorremark.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setView(edtNewRemark)
                        .show();



            }
        });

        myHolder.imgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edtNewRemark.getText().toString().equals("")) {
                        myHolder.txterrorremark.setVisibility(View.VISIBLE);
                    } else {
                        ActivityPending.progressDialog = ProgressDialog.show(context, "", "Loading...", true);
                        String accepturl = ActivityPending.clienturl + "?clientid=" + ActivityPending.clientid + "&caseid=129&Remarks=" + edtNewRemark.getText().toString() + "&Status=1&CashOutId=" + dataBalanceDetails.getpCashOutId();
                        Log.d("accepturl", accepturl);

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, accepturl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("Data updated successfully")) {
                                    ActivityPending.progressDialog.dismiss();
                                    getPendingOnMe();
                                    Toast.makeText(context, "Record uploaded successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Record uploading fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ActivityPending.progressDialog.dismiss();
                            }
                        });
                        requestQueue.add(stringRequest);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ActivityPending.progressDialog.dismiss();
                }
            }
        });

        myHolder.imgReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edtNewRemark.getText().toString().equals("")) {
                        myHolder.txterrorremark.setVisibility(View.VISIBLE);
                    } else {
                        ActivityPending.progressDialog = ProgressDialog.show(context, "", "Loading...", true);
                        String rejecturl = ActivityPending.clienturl + "?clientid=" + ActivityPending.clientid + "&caseid=129&Remarks=" + edtNewRemark.getText().toString() + "&Status=3&CashOutId=" + dataBalanceDetails.getpCashOutId();
                        Log.d("rejecturl", rejecturl);

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, rejecturl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("Data updated successfully")) {
                                    ActivityPending.progressDialog.dismiss();
                                    getPendingOnMe();
                                    Toast.makeText(context, "Record uploaded successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Record uploading fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ActivityPending.progressDialog.dismiss();
                            }
                        });
                        requestQueue.add(stringRequest);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ActivityPending.progressDialog.dismiss();
                }
            }
        });

        myHolder.txtDownloadReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strDocName=accountsArrayList.get(i).getpReceiptFileName();
                if(!strDocName.contains(".jpg"))
                {
                    strDocName=strDocName+".jpg";
                }
                String strUrl = "http://anilsahasrabuddhe.in/CRM/wayto965425/CashOutReceipt/" + strDocName;
                Log.d("dw", strUrl);
                if(!myHolder.txtDownloadReceipt.getText().toString().equals("NA")) {
                    mMyTask = new DownloadTask().execute(stringToURL(strUrl));
                    Log.d("qqq", strUrl);

                    ActivityPending.progressDialog = new ProgressDialog(context);
                    ActivityPending.progressDialog.setCancelable(true);
                    ActivityPending.progressDialog.setMessage("Downloading " + strDocName);
                    ActivityPending.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    ActivityPending.progressDialog.setProgress(0);
                    ActivityPending.progressDialog.setMax(100);
                    ActivityPending.progressDialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountsArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtAssigned, txtDate, txtCategoryName, txtApproved, txtAmount, txtMemo, txtAdminRemarks, txtDownloadReceipt, txtnewremark, txterrorremark;
        LinearLayout linearCard;
        ImageView imgAccept, imgReject;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtAssigned = itemView.findViewById(R.id.txtAssigned);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            txtApproved = itemView.findViewById(R.id.txtApproved);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtMemo = itemView.findViewById(R.id.txtMemo);
            txtAdminRemarks = itemView.findViewById(R.id.txtAdminRemarks);
            txtDownloadReceipt = itemView.findViewById(R.id.txtDownloadReceipt);
            linearCard = itemView.findViewById(R.id.linearCard);
            txtnewremark = itemView.findViewById(R.id.txtnewremark);
            txterrorremark = itemView.findViewById(R.id.txt_remarkerror);
            imgAccept = itemView.findViewById(R.id.img_accept);
            imgReject = itemView.findViewById(R.id.img_reject);
        }
    }

    public void getPendingOnMe() {
        ActivityPending.arrayListPendingDetails = new ArrayList<>();
        ActivityPending.arrayListPendingDetails.clear();

        ActivityPending.adapterPendingDetails=new AdapterPendingDetails(ActivityPending.arrayListPendingDetails,context);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ActivityPending.recyclerView.setLayoutManager(layoutManager);
        ActivityPending.recyclerView.setAdapter(ActivityPending.adapterPendingDetails);

        ActivityPending.progressDialog = ProgressDialog.show(context, "","Loading...",true);
        String urlPending = ActivityPending.clienturl + "?clientid=" + ActivityPending.clientid + "&caseid=128&CounselorID="+ActivityPending.counselorId+"&Step=1";
        Log.d("urlPending", urlPending);
        StringRequest jsonEducationInfo = new StringRequest(Request.Method.GET, urlPending, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contains("[]")){
                        ActivityPending.txtNotFound.setVisibility(View.VISIBLE);
                        ActivityPending.progressDialog.dismiss();
                    } else {
                        JSONObject jsonObject1 = new JSONObject(response);
                        Log.d("clientresponce", response);

                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String strCashOutId = jsonObject.getString("nCashOutId");
                            String strExpenceDate = jsonObject.getString("Expense Date");
                            String strAmountType = jsonObject.getString("cAmountType");
                            String strAmount = jsonObject.getString("cAmount");
                            String strCategoryType = jsonObject.getString("CategoryType");
                            String strMemo = jsonObject.getString("cmemo");
                            String strRemark = jsonObject.getString("cRemarks");
                            String strCategoryName = jsonObject.getString("CategoryName");
                            String strApprovedBy = jsonObject.getString("ApprovedBy");
                            String strReceiptFileName = jsonObject.getString("ReceiptFileName");
                            String strFrom = jsonObject.getString("From");
                            String strYou = jsonObject.getString("You");

                            DataPendingDetails dataPendingDetails = new DataPendingDetails(strCashOutId, strExpenceDate, strAmountType,
                                    strAmount, strCategoryType, strMemo, strRemark, strCategoryName, strApprovedBy, strReceiptFileName,
                                    strFrom, strYou);
                            ActivityPending.arrayListPendingDetails.add(dataPendingDetails);
                            ActivityPending.adapterPendingDetails.notifyDataSetChanged();
                        }
                        ActivityPending.progressDialog.dismiss();
                        //Log.d("educationarray",  + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ActivityPending.progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                ActivityPending.progressDialog.dismiss();
            }
        });

        jsonEducationInfo.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonEducationInfo);
    }

    // Custom method to convert string to url
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
                ActivityPending.progressDialog.dismiss();
                DownloadNotification();
            } else {
                ActivityPending.progressDialog.dismiss();
                Toast.makeText(context, "Download failed..", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Initialize ContextWrapper
        File directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/CashOutReceipt");
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
        Toast.makeText(context, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
        // Return the saved image Uri
        return savedImageURI;
    }

    public void DownloadNotification() {
        Random random = new Random();
        int m = random.nextInt(9999-1000)+1000;
        final int NotiId = m;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = context.getString(R.string.default_notification_channel_title); // Default Channel
        NotificationCompat.Builder builder;
        if (mNotifyManager == null) {
            mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
            builder = new NotificationCompat.Builder(context, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/CashOutReceipt/" + strDocName );
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file), "*/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded Image");
            builder.setContentText(strDocName);
            builder.setSubText("Tap to view Document.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        } else {
            builder = new NotificationCompat.Builder(context, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/CashOutReceipt/" + strDocName + ".jpg");
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "image/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

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
