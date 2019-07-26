package com.bizcall.wayto.sample;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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

public class AdapterTotalCallMade extends RecyclerView.Adapter<AdapterTotalCallMade.MyHolder> {
    ArrayList<DataTotalCallMade> arrayList;
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String clienturl,clientid;
    String url;

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
        String curl=clienturl.substring(0,clienturl.indexOf("CRM/")+4);
        url=curl+clientid+"/upload-test/";
        Log.d("Curl",url);
        myHolder.txtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
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
                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

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
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(context,
                    message, Toast.LENGTH_LONG).show();
        }
    }
}
