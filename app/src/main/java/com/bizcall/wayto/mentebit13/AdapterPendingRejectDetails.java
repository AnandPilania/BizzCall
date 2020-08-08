package com.bizcall.wayto.mentebit13;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

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

public class AdapterPendingRejectDetails extends RecyclerView.Adapter<AdapterPendingRejectDetails.MyHolder> {
    ArrayList<DataPendingRejectDetails> accountsArrayList;
    Context context;
    String strDocName;
    Uri savedImageURI;
    private AsyncTask mMyTask;
    private NotificationManager mNotifyManager;

    public AdapterPendingRejectDetails(ArrayList<DataPendingRejectDetails> accountsArrayList, Context context) {
        this.accountsArrayList = accountsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_balance_details,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        if (i % 2 == 0) {
            myHolder.linearCard.setBackgroundResource(R.drawable.balance_card_border);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            myHolder.linearCard.setBackgroundResource(R.drawable.balance_card_border1);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        DataPendingRejectDetails details=accountsArrayList.get(i);

        String amounttype=details.getrAmountType();
        if(amounttype.contains("INR")) {
            myHolder.txtAmount.setCompoundDrawablesWithIntrinsicBounds(  R.drawable.rupee1, 0,0, 0);
        }
        else if (amounttype.contains("USD")){
            myHolder.txtAmount.setCompoundDrawablesWithIntrinsicBounds(  R.drawable.dollar1, 0,0, 0);
        }
        myHolder.txtDate.setText(details.getrExpenceDate());
        myHolder.txtAmount.setText(details.getrAmount());
        myHolder.txtCategoryName.setText(details.getrCategoryName());
        myHolder.txtApproved.setText("ApprovedStatus: "+details.getrApprovedBy());
        myHolder.txtMemo.setText(details.getrMemo());
        myHolder.txtAssigned.setText(details.getrFrom() + " --> " + details.getrYou());
        myHolder.txtDownloadReceipt.setText(details.getrReceiptFileName());

        myHolder.txtDownloadReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strDocName=accountsArrayList.get(i).getrReceiptFileName();
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
        TextView txtAssigned,txtDate,txtCategoryName,txtApproved,txtAmount,txtMemo,txtAdminRemarks,txtDownloadReceipt;
        LinearLayout linearCard;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            txtAssigned=itemView.findViewById(R.id.txtAssigned);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtCategoryName=itemView.findViewById(R.id.txtCategoryName);
            txtApproved=itemView.findViewById(R.id.txtApproved);
            txtAmount=itemView.findViewById(R.id.txtAmount);
            txtMemo=itemView.findViewById(R.id.txtMemo);
            txtAdminRemarks=itemView.findViewById(R.id.txtAdminRemarks);
            txtDownloadReceipt=itemView.findViewById(R.id.txtDownloadReceipt);
            linearCard=itemView.findViewById(R.id.linearCard);
        }
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
