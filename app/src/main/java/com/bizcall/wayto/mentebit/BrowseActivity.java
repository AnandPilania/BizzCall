package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BrowseActivity extends AppCompatActivity {

    Button btnBrowse, btnUpload;
    TextView txtPath;
    String Fpath, clientid;
    int clicked = 0;
    int serverResponseCode = 0;
    String uploadFilePath = "";
    String uploadFileName = "";
    ProgressDialog dialog;
    ImageView imgBack;
    SharedPreferences sp;
    String upLoadServerUri = null;
    String clienturl;
    Long timeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        timeout=sp.getLong("TimeOut",0);
        upLoadServerUri = clienturl+"?clientid=" + clientid + "&caseid=4";
        btnBrowse = findViewById(R.id.btnBrowse);
        imgBack = findViewById(R.id.img_back);
        btnUpload = findViewById(R.id.btnUpload);
        txtPath = findViewById(R.id.txtPath);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int PICKFILE_RESULT_CODE = 1;
                clicked = 1;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicked > 0) {
                    if (Fpath.contains(".jpg") || Fpath.contains(".png") || Fpath.contains(".pdf") || Fpath.contains(".doc") || Fpath.contains(".mp3")) {
                        dialog = ProgressDialog.show(BrowseActivity.this, "", "Uploading file...", true);
                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(BrowseActivity.this, "uploading started.....", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                uploadFilePath = Fpath.substring(0, Fpath.lastIndexOf("/") + 1);
                                uploadFileName = Fpath.substring(Fpath.lastIndexOf("/") + 1);

                                Log.d("tag", uploadFilePath + "" + uploadFileName);
                                uploadFile(uploadFilePath + "" + uploadFileName);

                            }
                        }).start();


                        //uploadFile(uploadFilePath + "" + uploadFileName);
                    } else {
                        Toast.makeText(BrowseActivity.this, "Select only image or pdf or doc file ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        /*Uri uri=Uri.parse(data.getData().toString());
        Fpath=uri.getPath();
        Fpath = data.getDataString();
        Log.d("Path444",Fpath);
+
        uploadFilePath=Fpath.substring(0,Fpath.lastIndexOf("/")+1);
        uploadFilePath=uploadFilePath.replaceAll(":","");
        uploadFileName=Fpath.substring(Fpath.lastIndexOf("/")+1);
        uploadFileName=uploadFileName.replace(" ","");*/

        if (resultCode == RESULT_OK) {

            File file = new File(String.valueOf(Environment.getExternalStorageDirectory()));
            Log.d("File###", file.toString());
            String pp = data.getDataString();
            pp = pp.substring(pp.indexOf("external_files") + 14);
            Fpath = file.toString() + pp;
            Log.d("PP***", Fpath);
            //Uri uri = Uri.parse(data.getData().toString());
            //  Uri returnUri =data.getData();
            getDropboxIMGSize(Fpath);
            txtPath.setVisibility(View.VISIBLE);
            txtPath.setText(Fpath);
            // Log.d("patho",uri.getPath());
            //Log.d("path",myfile.getAbsolutePath());
        } else if (resultCode == RESULT_CANCELED) {
        } else {
            Toast.makeText(getApplicationContext(),
                    "Error", Toast.LENGTH_SHORT)
                    .show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getDropboxIMGSize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        Log.d("ImageSize", String.valueOf(imageHeight) + "/" + String.valueOf(imageWidth));

    }

    public int uploadFile(String sourceFileUri) {

        Log.d("SourceFile", sourceFileUri);
        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.exists()) {
            dialog.dismiss();

            Log.d("uploadFileNotExist", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(BrowseActivity.this, "Source File not exist :"
                            + uploadFilePath + "" + uploadFileName, Toast.LENGTH_SHORT).show();
                }
            });
            return 0;
        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + fileName + "" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                /*if(serverResponseCode==409)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashAfterCall.this, "File already exist.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(BrowseActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
               /* else
                {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(SplashAfterCall.this, "File already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
*/
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(BrowseActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Uploadfiletoserver", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(BrowseActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

}
