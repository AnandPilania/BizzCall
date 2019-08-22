package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SplashNumberNotPresent extends AppCompatActivity {

    TextView txtPhone;
    TextView edtName, edtRemark, edtAddress, edtCity, edtPin, edtParentNo, edtEmail;
    Spinner spinnerStatus, spinnerCourse, spinnerState;
    Button btnSubmit;
    String path1, date1, id1, phone, name, course, address, city, state, pincode, email, parent_no, remark;
    int status;
    ArrayList<String> arrayList1;
    UrlRequest urlRequest;
    ImageButton imgClose;
    ProgressDialog dialog;
    int serverResponseCode = 0;
    String upLoadServerUri = null,clickedButton="";
    SharedPreferences sp;
    AlertDialog alertDialog1;
    Calendar calendar;
    Date date,dt22,dt33;
    String newDTime="",callDate="";
    int clickedcount=0;


    /**********  File Path *************/
    String uploadFilePath = "";
    String uploadFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_number_not_present);
        showAlert();
        // getStatus();
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        upLoadServerUri = "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?caseid=4";
        txtPhone = findViewById(R.id.txtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtCity = findViewById(R.id.edtCity);
        edtPin = findViewById(R.id.edtPinCode);
        edtParentNo = findViewById(R.id.edtParentNo);
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtRemark = findViewById(R.id.edtRemark);
        spinnerStatus = findViewById(R.id.spinnerStatus1);
        btnSubmit = findViewById(R.id.btnSubmit1);
        imgClose = findViewById(R.id.imgClose);
        spinnerCourse = findViewById(R.id.spinnerCourse);
        spinnerState = findViewById(R.id.spinnerState);

        id1 = sp.getString("Id", null);
        id1 = id1.replace(" ", "");
        path1 = sp.getString("RecordedFile", null);
        uploadFilePath = path1.substring(path1.indexOf("/storage"), path1.indexOf("MyRecords/") + 10);
        uploadFileName = path1.substring(path1.indexOf("MyRecords/") + 10);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Courses, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCourse.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.States, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerState.setAdapter(adapter1);

        phone = getIntent().getStringExtra("Phone");
        // date1=getIntent().getStringExtra("Date");
        // date1=date1.replaceAll("_","");
        txtPhone.setText(phone);
        arrayList1 = new ArrayList<>();
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
       // dialog = ProgressDialog.show(SplashNumberNotPresent.this, "", "Uploading file...", true);
      /*  new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SplashNumberNotPresent.this, "uploading started.....", Toast.LENGTH_SHORT).show();
                    }
                });
               // getStatus();
                //uploadFile(uploadFilePath + "" + uploadFileName);


            }
        }).start();*/

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString();
                address = edtAddress.getText().toString();
                city = edtCity.getText().toString();
                parent_no = edtParentNo.getText().toString();
                email = edtEmail.getText().toString();
                pincode = edtPin.getText().toString();
                course = spinnerCourse.getSelectedItem().toString();
                state = spinnerState.getSelectedItem().toString();
                status = (int) spinnerStatus.getSelectedItemId();
                Log.d("statusid", String.valueOf(status));
                remark = edtRemark.getText().toString();
              // putNewUser();
            }

        });
    }
    public void showAlert()
    {
        LayoutInflater li = LayoutInflater.from(SplashNumberNotPresent.this);
        //Creating a view to get the dialog box
        View confirmCall = li.inflate(R.layout.layout_confirm_call, null);
        LinearLayout linearCall,linearUrl,linearCallDetails,linearConfirmCall,linearCallDuration;
        TextView txtCalledNumber,txtDateTime,txtDuration,txtNoCallDetails;
        Button btnGetDetails,btnOk;
        //confirmCall.setClipToOutline(true);
        final EditText edtCalledNumber = confirmCall.findViewById(R.id.edtCallNumber);
        linearCall = confirmCall.findViewById(R.id.linearCall1);
        linearUrl = confirmCall.findViewById(R.id.linearUrl);
        TextView txtYes = (TextView) confirmCall.findViewById(R.id.txtYes);
        TextView txtNo = (TextView) confirmCall.findViewById(R.id.txtNo);
        txtCalledNumber = confirmCall.findViewById(R.id.txtCallingNumber);
        txtDateTime = confirmCall.findViewById(R.id.txtDateTime);
        txtDuration = confirmCall.findViewById(R.id.txtDuration);
        linearCallDetails = confirmCall.findViewById(R.id.linearCallDetails);
        linearConfirmCall = confirmCall.findViewById(R.id.linearConfirmCall);
        btnGetDetails = confirmCall.findViewById(R.id.btnGetDetails);
        btnOk = confirmCall.findViewById(R.id.btnOk);
        linearCallDuration = confirmCall.findViewById(R.id.linearCallDuration);
        txtNoCallDetails = confirmCall.findViewById(R.id.txtNoCallMade);
        TextView txtSrno = confirmCall.findViewById(R.id.txtSrno);
        TextView txtName = confirmCall.findViewById(R.id.txtName);
        final TextView txtUrl = confirmCall.findViewById(R.id.txtUrl);
        // TextView txtRefname=confirmCall.findViewById(R.id.txtRefNo);
        TextView txtActivityName = confirmCall.findViewById(R.id.txtActivity);
        //txtSrno.setText(sr_no);
        //txtName.setText(txtCName.getText().toString());
        //txtActivityName.setText(activityName);
        final AlertDialog.Builder alert = new AlertDialog.Builder(SplashNumberNotPresent.this);
        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmCall);
        //Creating an alert dialog
        alertDialog1 = alert.create();
        alertDialog1.show();
        alertDialog1.setCancelable(false);
        linearConfirmCall.setVisibility(View.GONE);
        linearCallDetails.setVisibility(View.VISIBLE);
        calendar = Calendar.getInstance();
        linearConfirmCall.setVisibility(View.GONE);
        linearCallDetails.setVisibility(View.VISIBLE);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dt22 = new Date();
        dt22 = calendar.getTime();
        // txtAppNo.setText(edtCallNo.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        // SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dtime = calendar.getTime() + "";
        Log.d("DTime", dtime);
        // newDTime=sdf.format(dtime);
        Date date = calendar.getTime();
        newDTime = sdf.format(date);
        txtDateTime.setText(newDTime);
       // txtCalledNumber.setText(selectedmbl);

        btnGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtntnGetDetailsClicked();
            }
        });
    }
    public void onBtntnGetDetailsClicked(){
        try{
           /* path1 = sp.getString("RecordedFile", null);
            //Log.d("RecPath1", path1);
            uploadFilePath = path1.substring(path1.indexOf("/storage"), path1.indexOf("MyRecords/") + 10);
            uploadFileName = path1.substring(path1.indexOf("MyRecords/") + 10);
            callDate = uploadFileName.substring(7, 17);

            //callDate="1_12_2018_";

            if (callDate.endsWith("_")) {
                callDate = callDate.substring(0, callDate.length() - 1);
            }*/
            try {
                if (path1 != null && !path1.isEmpty() && !path1.equals("null")) {
                    callUploadFile();
                }
            }catch (Exception e) {
                Toast.makeText(SplashNumberNotPresent.this,"Exception while uploading file",Toast.LENGTH_SHORT).show();
            }

           // uploadFile()
   /* Calendar calendar1 = Calendar.getInstance();
    dt33 = new Date();
    dt33 = calendar1.getTime();
    long diffInMs = dt33.getTime() - dt22.getTime();
    long diffInSec1 = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
            Log.d("TImeDiff", String.valueOf(diffInSec1));
            if (diffInSec1 <= 1) {
        clickedcount = 0;
    }
            Log.d("ClickedCount", String.valueOf(clickedcount));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (manager.getMode() == AudioManager.MODE_IN_CALL) {
            clickedcount = 0;
            // linearCall.setVisibility(View.GONE);
            Toast.makeText(SplashNumberNotPresent.this, "Can't get call details while on call", Toast.LENGTH_SHORT).show();
        } else {
            clickedcount++;
            if (clickedcount == 1) {
                RecordService recordService=new RecordService(SplashNumberNotPresent.this);
                recordService.onStop();
                //linearCall.setVisibility(View.VISIBLE);
                clickedButton = "GetDetails";
             //   getConnectionQuality(clickedButton);
                // refreshWhenLoading();
            }
            else {
                clickedcount=0;
            }
        }
    }*/
}catch (Exception e){
        Toast.makeText(SplashNumberNotPresent.this,"Errorcode-166 CounselorContact BtnGetDetails clicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
        }
    public void callUploadFile(){
        try {

            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // dialog.dismiss();
                        }
                    });
                    uploadFile(uploadFilePath + "" + uploadFileName);
                }
            }).start();
        }
        catch (Exception e)
        {
            Toast.makeText(SplashNumberNotPresent.this,"Errorcode-186 CounselorContact uploadFile "+e.toString(),Toast.LENGTH_SHORT).show();                        Log.d("UplaodException", String.valueOf(e));
        }
    }

    private void putNewUser() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=AnDe828500&cCandidateName=" + name + "&cCourse=" + course + "&cMobile=" + phone + "&cAddressLine=" +
                address + "&cCity=" + city + "&cState=" + state + "&cPinCode=" + pincode + "&cEmail=" + email + "&AllocatedTo=" + id1 + "&CurrentStatus=" + status + "&cRemarks=" + remark);

       /*
                $ADate=$_GET[''];
        $CStatus=$_GET[''];
        $Remarks =$_GET[''];*/
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();
                Log.d("UpdatedStatus", response);
                if (response.contains("Data inserted successfully")) {
                    Toast.makeText(SplashNumberNotPresent.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SplashNumberNotPresent.this, "Data not saved", Toast.LENGTH_SHORT).show();
                }


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
                        arrayList1.add(status1);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SplashNumberNotPresent.this, R.layout.spinner_item1, arrayList1);
                    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatus.setAdapter(arrayAdapter);
                    Log.d("Size**", String.valueOf(arrayList1.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
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

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.d("uploadFileNotExist", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(SplashNumberNotPresent.this, "Source File not exist :"
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
                            Toast.makeText(SplashNumberNotPresent.this, "File Upload Complete.",
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
                        Toast.makeText(SplashNumberNotPresent.this, "MalformedURLException",
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
                        Toast.makeText(SplashNumberNotPresent.this, "Got Exception : see logcat ",
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
