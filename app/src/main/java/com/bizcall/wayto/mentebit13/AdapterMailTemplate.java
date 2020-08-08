package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AdapterMailTemplate extends RecyclerView.Adapter<AdapterMailTemplate.MyHolder> {
   Context context;
   ArrayList<DataMailTemplate> arrayListMailTemplate;
   ArrayList<String> arrayListAttachments;
   String mailid,subject,attachment1,attachment2,attachment3,attachment4,attachment5,mailImage;
   String clienturl="",strDocName="",strImage="",ext="";
   SharedPreferences sp;
   String strImageUrl="";
    Uri savedImageURI;
    ProgressDialog dialog;

    public AdapterMailTemplate(Context context, ArrayList<DataMailTemplate> arrayListMailTemplate) {
        this.context = context;
        this.arrayListMailTemplate = arrayListMailTemplate;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_mail_template,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        final DataMailTemplate dataMailTemplate=arrayListMailTemplate.get(i);
        mailid=dataMailTemplate.getMailid();
        subject=dataMailTemplate.getMailsubject();
        attachment1=dataMailTemplate.getAttachments1();
        attachment2=dataMailTemplate.getAttachments2();
        attachment3=dataMailTemplate.getAttachments3();
        attachment4=dataMailTemplate.getAttachments4();
        attachment5=dataMailTemplate.getAttachments5();
        mailImage=dataMailTemplate.getMailImage();
        myHolder.txtMailId.setText(mailid);
        myHolder.txtMailSubject.setText(subject);

        arrayListAttachments=new ArrayList<>();


        myHolder.chkboxSelectMailTemplate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(myHolder.chkboxSelectMailTemplate.isChecked())
               {
                   mailid=dataMailTemplate.getMailid();
                   subject=dataMailTemplate.getMailsubject();
                   attachment1=dataMailTemplate.getAttachments1();
                   attachment2=dataMailTemplate.getAttachments2();
                   attachment3=dataMailTemplate.getAttachments3();
                   attachment4=dataMailTemplate.getAttachments4();
                   attachment5=dataMailTemplate.getAttachments5();
                   mailImage=dataMailTemplate.getMailImage();
                   myHolder.txtMailId.setText(mailid);
                   myHolder.txtMailSubject.setText(subject);

                   arrayListAttachments=new ArrayList<>();

                    MailActiivity.edtEmailSubject.setText(dataMailTemplate.getMailsubject());
                    MailActiivity.edtEmailText.setText(dataMailTemplate.getMailbody());


                    if(!attachment1.contains("NA"))
                    {
                        clienturl=sp.getString("ClientUrl",null);
                        clienturl=clienturl.substring(0,clienturl.lastIndexOf("/"));

                            strImageUrl = clienturl+"/upload-empdoc/"+ dataMailTemplate.getAttachments1();
                        strDocName = dataMailTemplate.getAttachments1();
                        ext=strDocName.substring(strDocName.lastIndexOf(".")+1);
                        Log.d("imageclick", strImage);

                        if(ext.equalsIgnoreCase("pdf"))
                        {
                            File directory;
                            if(ActivityUploadDocs.activityname.equals("EmpDocs"))
                            {
                                directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs");
                            }
                            else
                            {
                                directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/ClientDocs");
                            }
                            // File directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/EmpDocs");
                            directory.mkdirs();

                            // Create a file to save the image
                            final File file = new File(directory, strDocName);
                            new Thread(new Runnable() {
                                public void run() {
                                    DownloadFile(strImageUrl,file);
                                }
                            }).start();
                        }
                        else {
                            Log.d("clickimageurl", strImageUrl);
                             new AdapterMailTemplate.DownloadTask().execute(stringToURL(strImageUrl));
                        }

                        dialog = new ProgressDialog(context);
                        dialog.setCancelable(true);
                        dialog.setMessage("Downloading " + strDocName);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setProgress(0);
                        dialog.setMax(100);
                        dialog.show();

                      //  Picasso.with(context).load(strImageUrl).into(holder.imgUrl);
                        arrayListAttachments.add(strImageUrl);
                    }
                   if(!attachment2.contains("NA"))
                   {
                       arrayListAttachments.add(dataMailTemplate.getAttachments2());
                   }
                   if(!attachment3.contains("NA"))
                   {
                       arrayListAttachments.add(dataMailTemplate.getAttachments3());
                   }
                   if(!attachment4.contains("NA"))
                   {
                       arrayListAttachments.add(dataMailTemplate.getAttachments4());
                   }
                   if(!attachment5.contains("NA"))
                   {
                       arrayListAttachments.add(dataMailTemplate.getAttachments5());
                   }

                   MailActiivity.listViewImages.setVisibility(View.VISIBLE);
                   MailActiivity.listViewImages.setAdapter(new ArrayAdapter<String>(context,R.layout.adapter_listview,arrayListAttachments));
                 mailImage=  dataMailTemplate.getMailImage();
               }
               else
               {
                   MailActiivity.edtEmailText.getText().clear();
                   MailActiivity.edtEmailSubject.getText().clear();
                   if(arrayListAttachments.size()!=0)
                   {
                       arrayListAttachments.clear();
                       /*for(int i=0;i<=arrayListAttachments.size();i++) {
                           arrayListAttachments.remove(arrayListAttachments.get(i));
                       }*/
                   }
               }
            }
        });
        if(attachment1.contains("NA"))
        {
            myHolder.txtAttachment1.setText("NA");
        }
        else {
            myHolder.txtAttachment1.setText(attachment1);
        }
        if(attachment2.contains("NA"))
        {
            myHolder.txtAttachment2.setText("NA");
        }
        else {
            myHolder.txtAttachment2.setText(attachment2);
        }
        if(attachment3.contains("NA"))
        {
            myHolder.txtAttachment3.setText("NA");
        }
        else {
            myHolder.txtAttachment3.setText(attachment3);
        }
        if(attachment4.contains("NA"))
        {
            myHolder.txtAttachment4.setText("NA");
        }
        else {
            myHolder.txtAttachment4.setText(attachment4);

        }
        if(attachment5.contains("NA"))
        {
            myHolder.txtAttachment5.setText("NA");
        }
        else {
            myHolder.txtAttachment5.setText(attachment5);
        }
        if(mailImage.contains("NA"))
        {
            myHolder.txtMailImage.setText("NA");
        }
        else {
            myHolder.txtMailImage.setText(mailImage);
        }

    }

    @Override
    public int getItemCount() {
        return arrayListMailTemplate.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        CheckBox chkboxSelectMailTemplate;
        TextView txtMailId,txtMailSubject,txtAttachment1,txtAttachment2,txtAttachment3,txtAttachment4,txtAttachment5,txtMailImage;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtMailId=itemView.findViewById(R.id.txtMailId);
            txtMailSubject=itemView.findViewById(R.id.txtMailSubject);
            txtAttachment1=itemView.findViewById(R.id.txtAttachment1);
            txtAttachment2=itemView.findViewById(R.id.txtAttachment2);
            txtAttachment3=itemView.findViewById(R.id.txtAttachment3);
            txtAttachment4=itemView.findViewById(R.id.txtAttachment4);
            txtAttachment5=itemView.findViewById(R.id.txtAttachment5);
            txtMailImage=itemView.findViewById(R.id.txtMailImage);
            chkboxSelectMailTemplate=itemView.findViewById(R.id.chkboxSelectMailTemplate);
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

               /* new Thread(new Runnable() {
                    public void run() {
                        Toast.makeText(mContext, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
                    }
                }).start();*/

               // DownloadNotification();
            } else {
                dialog.dismiss();
                Toast.makeText(context, "Download failed..", Toast.LENGTH_LONG).show();
            }
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


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

              /*  Uri imageInternalUri = saveImageToInternalStorage(result);
                dialog.dismiss();
                DownloadNotification();*/
            } else {
                dialog.dismiss();
                Toast.makeText(context, "Download failed..", Toast.LENGTH_LONG).show();
            }
        }
    }
}
