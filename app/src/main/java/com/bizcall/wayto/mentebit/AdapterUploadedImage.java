package com.bizcall.wayto.mentebit;

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
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class AdapterUploadedImage extends RecyclerView.Adapter<AdapterUploadedImage.Holder> {
    private ArrayList<DataUploadDocs> mDetailUploadedDocs;
    private Context mContext;
    private AsyncTask mMyTask;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    ProgressDialog dialog;
    DataUploadDocs details;
    String strDocName, strImageUrl, strImage, strImageClick, strUrl;
    Uri savedImageURI;

    public AdapterUploadedImage(ArrayList<DataUploadDocs> mDetailUploadedDocs, Context mContext) {
        this.mDetailUploadedDocs = mDetailUploadedDocs;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterUploadedImage.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_uploadedimage, null);
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterUploadedImage.Holder holder, final int i) {
        details = mDetailUploadedDocs.get(i);
        strImage = mDetailUploadedDocs.get(i).getUploadedDocName();
        strImageUrl = "http://anilsahasrabuddhe.in/CRM/wayto965425/AccountDocument/" + strImage + ".jpg";

        holder.txtName.setText(details.getUploadedDocName());
        Picasso.with(mContext).load(strImageUrl).into(holder.imgUrl);

        holder.imgUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strDocName = mDetailUploadedDocs.get(i).getUploadedDocName();
                Log.d("imageclick", strImage);

                strUrl = "http://anilsahasrabuddhe.in/CRM/wayto965425/AccountDocument/" + strDocName + ".jpg";
                Log.d("clickimageurl", strUrl);

                mMyTask = new DownloadTask().execute(stringToURL(strUrl));

                dialog = new ProgressDialog(mContext);
                dialog.setCancelable(true);
                dialog.setMessage("Downloading " + strDocName);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setProgress(0);
                dialog.setMax(100);
                dialog.show();
            }
        });

        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strDocName = mDetailUploadedDocs.get(i).getUploadedDocName();
                Log.d("clickdoc", strDocName);

                strUrl = "http://anilsahasrabuddhe.in/CRM/wayto965425/AccountDocument/" + strDocName + ".jpg";
                Log.d("clickdocurl", strUrl);

                mMyTask = new DownloadTask().execute(stringToURL(strUrl));

                dialog = new ProgressDialog(mContext);
                dialog.setCancelable(true);
                dialog.setMessage("Downloading " + strDocName);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setProgress(0);
                dialog.setMax(100);
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDetailUploadedDocs.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imgUrl;
        TextView txtName;

        public Holder(@NonNull View itemView) {
            super(itemView);

            imgUrl = itemView.findViewById(R.id.imageView1);
            txtName = itemView.findViewById(R.id.img_name);
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
                dialog.dismiss();
                DownloadNotification();
            } else {
                dialog.dismiss();
                Toast.makeText(mContext, "Download failed..", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Initialize ContextWrapper
        File directory = new File(Environment.getExternalStorageDirectory() + "/Appli");
        directory.mkdirs();

        // Create a file to save the image
        File file = new File(directory, strDocName + ".jpg");

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
        Toast.makeText(mContext, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
        // Return the saved image Uri
        return savedImageURI;
    }

    public void DownloadNotification() {
        Random random = new Random();
        int m = random.nextInt(9999-1000)+1000;
        final int NotiId = m;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String id = mContext.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = mContext.getString(R.string.default_notification_channel_title); // Default Channel
        NotificationCompat.Builder builder;

        if (mNotifyManager == null) {
            mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
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
            builder = new NotificationCompat.Builder(mContext, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Appli/" + strDocName + ".jpg");
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                    android.support.v4.content.FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID+".provider",file), "*/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded Image");
            builder.setContentText(strDocName);
            builder.setSubText("Tap to view Document.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        } else {
            builder = new NotificationCompat.Builder(mContext, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Appli/" + strDocName + ".jpg");
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                    android.support.v4.content.FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file), "image/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

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