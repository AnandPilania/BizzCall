package com.bizcall.wayto.mentebit13;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Build;
import android.os.Environment;

import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class AdapterUploadedDocs extends RecyclerView.Adapter<AdapterUploadedDocs.Holder> {
    private ArrayList<DataUploadDocs> mDetailUploadedDocs;
    private Context mContext;
    private AsyncTask mMyTask;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    ProgressDialog dialog;
    DataUploadDocs details;
    String strDocName, ext, strspannable, strUrl, btnClick;
    Uri savedImageURI;
    String clientid, clienturl, mail, subject;
    SharedPreferences sp;
    String actname;
    Handler handler = new Handler();

    public AdapterUploadedDocs(ArrayList<DataUploadDocs> mDetailUploadedDocs, Context mContext) {
        this.mDetailUploadedDocs = mDetailUploadedDocs;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterUploadedDocs.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_uploaded_docs, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterUploadedDocs.Holder holder, final int i)
    {
        details = mDetailUploadedDocs.get(i);
        holder.mDocid.setText(details.getUploadedDocID());
        holder.mDocname.setText(details.getUploadedDocName());

        sp = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        clienturl = clienturl.substring(0, clienturl.lastIndexOf("/"));
        actname=sp.getString("ActName","");

        mail = sp.getString("ClientEmail", null);

        strspannable = holder.mDocname.getText().toString();
        Log.d("mmm", strspannable);
        SpannableString spannableString = new SpannableString(strspannable);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        holder.mDocname.setText(spannableString);

        holder.chkAttach.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.chkAttach.isChecked()) {
                    strDocName = mDetailUploadedDocs.get(i).getUploadedDocName();
                    ext = strDocName.substring(strDocName.lastIndexOf(".") + 1);
                    Log.d("qq", strDocName + "" + ext);
                   if(actname.equals("UploadDocs"))
                    {
                        ActivityUploadDocs.attachmentArrayList.add(strDocName);
                    }
                }
            }
        });

        holder.mDocname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick = "Download";
                strDocName = mDetailUploadedDocs.get(i).getUploadedDocName();
                ext = strDocName.substring(strDocName.lastIndexOf(".") + 1);
                Log.d("qq", strDocName + "" + ext);
                strUrl = clienturl + "/upload-empdoc/" + strDocName;
                Log.d("dw", strUrl);
                if (ext.equalsIgnoreCase("pdf")||ext.equalsIgnoreCase("doc")||ext.equalsIgnoreCase("docx")||ext.equalsIgnoreCase("xslx")||ext.equalsIgnoreCase("xsl")) {
                    File directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs");
                    directory.mkdirs();
                    // Create a file to save the image
                    final File file = new File(directory, strDocName);
                    new Thread(new Runnable() {
                        public void run() {
                            DownloadFile(strUrl, file);
                        }
                    }).start();
                } else {
                    mMyTask = new DownloadTask().execute(stringToURL(strUrl));
                }
                Log.d("qqq", strUrl);

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
        TextView mDocid, mDocname;
        CheckBox chkAttach;

        public Holder(@NonNull View itemView) {
            super(itemView);
            chkAttach = itemView.findViewById(R.id.chkAttach);
            mDocid = itemView.findViewById(R.id.txt_upl_docid);
            mDocname = itemView.findViewById(R.id.txt_upl_docname);

            mDocname.setMovementMethod(LinkMovementMethod.getInstance());
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
                dialog.dismiss();
                Uri imageInternalUri = saveImageToInternalStorage(result);
               // saveImageToInternalStorage(imageInternalUri);
                DownloadNotification();
            } else {
                dialog.dismiss();
                Toast.makeText(mContext, "Download failed..", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        Log.d("Entered", "Image");
        // Initialize ContextWrapper
        File directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs");
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
        Uri path = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", directory);
        // Uri path = Uri.fromFile(file);
        /*if (btnClick.equals("Attachment")) {

            sendMail(path, mail, subject);
        }*/
        Log.d("mypath", savedImageURI + "");
        Toast.makeText(mContext, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
        // Return the saved image Uri
        return savedImageURI;
    }

    public void DownloadNotification() {
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
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
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs/" + strDocName);
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                   FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
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
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs/" + strDocName + ".jpg");
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                    FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
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

    private void DownloadFile(String fileURL, File directory) {
        try {
            FileOutputStream f = new FileOutputStream(directory);
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            //  c.setDoOutput(true);
            c.connect();

            InputStream in = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            if (len1 != 0) {
                // Save bitmap to internal storage
                dialog.dismiss();
                savedImageURI = Uri.parse(directory.getAbsolutePath());
                Log.d("mypath", savedImageURI + "");
                Uri path = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", directory);
                /*if (btnClick.equals("Attachment")) {
                    sendMail(path, mail, subject);
                }*/
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();

                DownloadNotification();
            } else {
                dialog.dismiss();
                Toast.makeText(mContext, "Download failed..", Toast.LENGTH_LONG).show();
            }
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "File not found.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
}


