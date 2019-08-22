package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SplashAfterCall extends AppCompatActivity {

    SharedPreferences sp;
    String sr_no, name1, mobile, recordedfile;
    Spinner spinnerStatus;
    TextView txtSrno, txtName, txtMobile, txtPath;
    ArrayList<StatusInfo> arrayList;
    ArrayList<String> arrayList1;
    UrlRequest urlRequest;
    FileInputStream fis;
    MediaPlayer mediaPlayer;
    String path1;
    ProgressDialog dialog;
    Button btnUpload;

    int serverResponseCode = 0;
    String upLoadServerUri = null;

    /**********  File Path *************/
    String uploadFilePath = "";
    String uploadFileName = "";
    String duration1, remarks, status;
    TextView edtRemarks;
    String callDate, id1;
    boolean isClicked = false;
    AlertDialog alertDialog;
    ImageButton imgClose;
    String clientid,clienturl;
    private long back_pressed = 0;
    //ImageView imgError1,imgError2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_after_call);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        imgClose = findViewById(R.id.imgClose);
        upLoadServerUri = clienturl+"?clientid=" + clientid + "&caseid=4";
        btnUpload = findViewById(R.id.btnUpload);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        txtSrno = findViewById(R.id.txtSerialNo1);
        txtName = findViewById(R.id.txtCName);
        txtMobile = findViewById(R.id.txtMobNumber);
        txtPath = findViewById(R.id.txtPath1);
        edtRemarks = findViewById(R.id.edtRemarks);

        sr_no = sp.getString("Sr.No", null);
        name1 = sp.getString("CName", null);
        mobile = sp.getString("MobileNo", null);
        path1 = sp.getString("RecordedFile", null);
        duration1 = sp.getString("Dur", null);
        //  sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        id1 = sp.getString("Id", null);
        id1 = id1.replace(" ", "");
        txtSrno.setText(sr_no);
        txtName.setText(name1);
        txtMobile.setText(mobile);
        recordedfile = path1.substring(path1.indexOf("/MyRecords") + 10);
        String phn = recordedfile.substring(recordedfile.indexOf("/") + 1, recordedfile.indexOf("_"));
        txtPath.setText(path1);
        // path1=txtPath.getText().toString();
        Log.d("Path***", recordedfile);
        Log.d("Phone***", phn);

        Log.d("Dur111", duration1);
        String[] dur = duration1.split(":");
        int sec = Integer.parseInt(dur[1]);
        Log.d("Seconds", String.valueOf(sec));
        //  int dur=Integer.parseInt(duration1);
        txtPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(path1);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                //   intent.setData(Uri.parse(path1));
                intent.addCategory(Intent.CATEGORY_APP_MUSIC);
                startActivity(intent);
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (sec > 25) {
            Toast.makeText(SplashAfterCall.this, "Call duration is more than 25 seconds", Toast.LENGTH_SHORT).show();
        }

        uploadFilePath = path1.substring(path1.indexOf("/storage"), path1.indexOf("MyRecords/") + 10);
        uploadFileName = path1.substring(path1.indexOf("MyRecords/") + 10);
        callDate = uploadFileName.substring(11, 21);
        //callDate="1_12_2018_";

        if (callDate.endsWith("_")) {
            callDate = callDate.substring(0, callDate.length() - 1);
        }
        //Log.d("Path***",path1);
        Log.d("Date@@@", callDate);
        Log.d("FilePath", uploadFilePath);
        Log.d("FileName", uploadFileName);
        arrayList1 = new ArrayList<>();

        dialog = ProgressDialog.show(SplashAfterCall.this, "", "Uploading file...", true);
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SplashAfterCall.this, "uploading started.....", Toast.LENGTH_SHORT).show();
                    }
                });
                getStatus();
                uploadFile(uploadFilePath + "" + uploadFileName);
                insertCallInfo();

            }
        }).start();
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(SplashAfterCall.this, "", "Loading...", true);
                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(SplashAfterCall.this, "uploading status and remark.....", Toast.LENGTH_SHORT).show();
                            }
                        });
                        isClicked = true;
                        setStatus();

                    }
                }).start();
            }
        });
    }

    public void getStatus() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?caseid=2");
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("StatusResponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Json", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String status1 = jsonObject1.getString("cStatus");
                        //  statusid=jsonObject1.getString("nStatusID");
                        // Log.d("Status11",statusid);
                        //StatusInfo statusInfo=new StatusInfo(status1,statusid);

                        // Log.d("Json33333",statusInfo.toString());
                        //arrayList.add(statusInfo);
                        arrayList1.add(status1);
                        // Log.d("Json11111",arrayList1.toString());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SplashAfterCall.this, R.layout.spinner_item1, arrayList1);
                    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatus.setAdapter(arrayAdapter);
                    Log.d("Size**", String.valueOf(arrayList1.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setStatus() {
        int status1 = (int) spinnerStatus.getSelectedItemId();
        Log.d("updateStatus", String.valueOf(status1));
        // status=status.replaceAll(" ","");
        remarks = edtRemarks.getText().toString();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=5&nSrNo=" + sr_no + "&CurrentStatus=" + status1 + "&cRemarks=" + remarks);
        Log.d("StatusUrl", "http://anilsahasrabuddhe.in/CRM/AnDe828500/updateStatus.php?clientid=" + clientid + "&caseid=5&nSrNo=" + sr_no + "&CurrentStatus=" + status1 + "&cRemarks=" + remarks);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("UpdatedStatus", response);
                if (response.contains("Row updated successfully")) {
                    Toast.makeText(SplashAfterCall.this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SplashAfterCall.this, "Status not updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void insertCallInfo() {
        String url = "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=6&nSrNo=" + sr_no + "&cFileName=" + uploadFileName + "&cMobileNo=" + mobile + "&cCallDuration=" + duration1 + "&cCallDate=" + callDate + "&nCounselorID=" + id1;
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(url);
        Log.d("StatusUrl", url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

                dialog.dismiss();
                Log.d("InsertedStatus", response);
                if (response.contains("Data inserted successfully")) {
                    Toast.makeText(SplashAfterCall.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SplashAfterCall.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SplashAfterCall.this, "Source File not exist :"
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
                            Toast.makeText(SplashAfterCall.this, "File Upload Complete.",
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
                        Toast.makeText(SplashAfterCall.this, "MalformedURLException",
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
                        Toast.makeText(SplashAfterCall.this, "Got Exception : see logcat ",
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

    @Override
    public void onBackPressed() {

        // if(btnUpload.isPressed())
        if (isClicked) {
            super.onBackPressed();
            /* Intent intent=new Intent(SplashAfterCall.this,Home.class);
             startActivity(intent);
             finish();*/
        } else {
            LayoutInflater li = LayoutInflater.from(SplashAfterCall.this);
            //Creating a view to get the dialog box
            View confirmCall = li.inflate(R.layout.layout_update_status, null);

            //confirmCall.setClipToOutline(true);

            Button btnOk = confirmCall.findViewById(R.id.btnOk);

            AlertDialog.Builder alert = new AlertDialog.Builder(SplashAfterCall.this);
            //Adding our dialog box to the view of alert dialog
            alert.setView(confirmCall);
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();

            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });


            // Toast.makeText(SplashAfterCall.this, "Please upload your status and remark", Toast.LENGTH_SHORT).show();
        }
        // back_pressed = System.currentTimeMillis();

    }
}


