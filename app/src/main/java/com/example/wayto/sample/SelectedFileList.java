package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SelectedFileList extends AppCompatActivity {

    // ArrayList<DataFileList> arrayList;
    TextView txtSlectedFile;
    Button btnUpload;
    ProgressDialog dialog;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int serverResponseCode = 0;
    String upLoadServerUri = null;
    String fileName;
    boolean isUploaded = false;
    /**********  File Path *************/
    String uploadFilePath = "";
    String uploadFileName = "";
    int i;
    ArrayList<DataUploadedList> uploadedLists;
    String path1, newname, clientid;
    DataUploadedList dataUploadedList;
    ImageView imgBack;
    UrlRequest urlRequest;
    String sr_no, mbl, duration1, callDate, id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        setContentView(R.layout.layout_selectedfilelist);
        sr_no = sp.getString("SrNo", null);
        mbl = sp.getString("MobileNo1", null);
        id1 = sp.getString("Id", null);
        id1 = id1.replaceAll(" ", "");
        imgBack = findViewById(R.id.img_back);
        txtSlectedFile = findViewById(R.id.txtListSelectedFiles);
        upLoadServerUri = "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=4";
        //"cases.php?caseid=4";
        // sr_no=sp.getString("SrNo")
        editor = sp.edit();
        uploadedLists = new ArrayList<>();
        duration1 = "0";
        // arrayList=new ArrayList<>();
        Log.d("ArraySize", String.valueOf(RecyclerFiles.arrayList.size()));
        for (int i = 0; i < RecyclerFiles.arrayList.size(); i++) {
            if (RecyclerFiles.arrayList.get(i).getSelected()) {
                String f1 = RecyclerFiles.arrayList.get(i).getFilename();
                dataUploadedList = new DataUploadedList(f1);
                uploadedLists.add(dataUploadedList);
                txtSlectedFile.setText(txtSlectedFile.getText() + "" + RecyclerFiles.arrayList.get(i).getFilename());
            }
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(SelectedFileList.this, "", "Uploading file...", true);
//                RecyclerFiles.arrayList.get(i).setFilename(newname);

                for (i = 0; i < RecyclerFiles.arrayList.size(); i++) {
                    if (RecyclerFiles.arrayList.get(i).getSelected()) {
                        path1 = RecyclerFiles.arrayList.get(i).getFilename();

                        //  RecyclerFiles.arrayList.set(i,new DataFileList(fileName));
                        uploadFilePath = path1.substring(path1.indexOf("/storage"), path1.indexOf("MyRecords/") + 10);
                        uploadFileName = path1.substring(path1.indexOf("MyRecords/") + 10);
                        Log.d("Path***", path1);
                        Log.d("FilePath", uploadFilePath);
                        Log.d("FileName", uploadFileName);
                        callDate = uploadFileName.substring(11, 21);
                        //callDate="1_12_2018_";

                        if (callDate.endsWith("_")) {
                            callDate = callDate.substring(0, callDate.length() - 1);
                        }

                     /*   new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(SelectedFileList.this, "uploading started.....", Toast.LENGTH_SHORT).show();
                                    }
                                });*/

                        uploadFile(uploadFilePath + "" + uploadFileName);
                        insertCallInfo();
                        //uploadFile(path1);
                    }
                      /*  }).start();

                        }*/
                }


            }
        });
    }

    public void getList() {
        for (int i1 = 0; i1 < uploadedLists.size(); i1++) {
            uploadedLists.get(i1).setFname("Uploaded" + uploadedLists.get(i1).getFname());

            String f11 = uploadedLists.get(i1).getFname();
            DataUploadedList dataUploadedList = new DataUploadedList(f11);
            uploadedLists.clear();
            uploadedLists.add(dataUploadedList);

        }
        Log.d("F111", uploadedLists.toString());

        Intent intent = new Intent(SelectedFileList.this, FileListActivity.class);
        intent.putExtra("ActivityName", "SelectedFileList");
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SelectedFileList.this, FileListActivity.class);
        startActivity(intent);
    }

    public void insertCallInfo() {
        String url = "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=6&nSrNo=" + sr_no + "&cFileName=" + uploadFileName + "&cMobileNo=" + mbl + "&cCallDuration=" + duration1 + "&cCallDate=" + callDate + "&nCounselorID=" + id1;
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(url);
        Log.d("InsertUrl1", url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

                dialog.dismiss();
                Log.d("InsertedStatus1", response);
                if (response.contains("Data inserted successfully")) {
                    Toast.makeText(SelectedFileList.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SelectedFileList.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int uploadFile(String sourceFileUri) {


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
                    Toast.makeText(SelectedFileList.this, "Source File not exist :"
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
                            Toast.makeText(SelectedFileList.this, "File Upload Complete.",
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
                        Toast.makeText(SelectedFileList.this, "MalformedURLException",
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
                        Toast.makeText(SelectedFileList.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        }
    }

}
