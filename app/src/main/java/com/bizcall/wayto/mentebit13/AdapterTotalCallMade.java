package com.bizcall.wayto.mentebit13;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class AdapterTotalCallMade extends RecyclerView.Adapter<AdapterTotalCallMade.MyHolder> {
    ArrayList<DataTotalCallMade> arrayList;
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String clienturl,clientid;
    String url;
    private NotificationManager mNotifyManager;
    private ProgressDialog progressDialog;
    private String fileName;
    private String folder,curl;

    public AdapterTotalCallMade(Context context, ArrayList<DataTotalCallMade> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterTotalCallMade.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.lyout_adapter_total_calls_made, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterTotalCallMade.MyHolder myHolder, int i) {

        DataTotalCallMade dataTotalCallMade = arrayList.get(i);
        myHolder.txtSr.setText(dataTotalCallMade.getSrno());
        myHolder.txtDuration.setText(dataTotalCallMade.getDuration());
        myHolder.txtDate.setText(dataTotalCallMade.getDate1());
        myHolder.txtFile.setText(dataTotalCallMade.getFilename());
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        clienturl=sp.getString("ClientUrl",null);
        clientid=sp.getString("ClientId",null);
         curl=clienturl.substring(0,clienturl.indexOf("CRM/")+4);


        myHolder.txtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url=curl+clientid+"/upload-test/";
                url = url+myHolder.txtFile.getText().toString();
                Log.d("Downloadurl",url);
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                else {
                    new DownloadFile().execute(url);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView txtSr, txtDuration, txtDate, txtFile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtSr = itemView.findViewById(R.id.txtSrNo);
            txtDuration = itemView.findViewById(R.id.txtCallDuration);
            txtDate = itemView.findViewById(R.id.txtCallDate);
            txtFile = itemView.findViewById(R.id.txtFile);
        }
    }
    public class DownloadFile  extends AsyncTask<String, String, String> {


        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "Bizcall/DownloadedRecording/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d( "Progress: ", String.valueOf(+ (int) ((total * 100) / lengthOfFile)));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            progressDialog.dismiss();
            DownloadNotification();
            // Display File path after downloading
            Toast.makeText(context,
                    message, Toast.LENGTH_LONG).show();
        }
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
            File file = new File(folder+fileName );
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                   FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "audio/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file), "*/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded recording");
            builder.setContentText(fileName);
            builder.setSubText("Tap to play audio.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        } else {
            builder = new NotificationCompat.Builder(context, id);
            builder.setSmallIcon(R.drawable.docdownload);
            //File file = new File(Environment.getExternalStorageDirectory() + "/androiddeft/" + fileName );
            File file = new File(folder+fileName );
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "audio/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

          //  intent.setDataAndType(Uri.fromFile(file), "audio/*");
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "image/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded Image");
            builder.setContentText(fileName);
            builder.setSubText("Tap to play audio.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        }
        mNotifyManager.notify(NotiId, builder.build());
    }
    public void audioPlayer(String path, String fileName){
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();

        try {
            mp.setDataSource(path + File.separator + fileName);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
