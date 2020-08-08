package com.bizcall.wayto.mentebit13;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PaymentEntry extends AppCompatActivity {

   Spinner spinnerPaymentMode,spinnerBankName,spinnerAccountName;
   EditText edtAmountInINR,edtPaymentReceivedFrom,edtAccountNo,edtRefNo,edtAmtInDollar,edtCurrentDollarRate;
   EditText edtClgFeeYearD,edtHostalFeeYearD,edtOTPFeeD,edtAccidentalInsurance,edtMess,edtAirFeeR,edtIndiaOTC,edtRemarks;
   TextView txtTotalFeeD,txtTotalFeeR,txtGreaterAmtUSD,txtGreaterAmtINR,txtSubmit,txtFID,txtUpload,txtReset,txtInvalidAmount;
   TextView txtSelectedDate,txtSelectedDate1,txtSubmitRate,txtAccountId,txtServiceTax,txtFinalAmount,txtSelectAcntName;
   ImageView imgBack,imgReceipt,imgPath;
   TextView txtSelectDateError,txtSelectDateError1,txtPaymentModeError;
   LinearLayout linearCalender,linearCalender1,linearUnderCurrentDollar,linearAcntRef,linearBankName;
    int year, day, month;
    Calendar myCalendar;
    String actname="",clienturl="",clientid="",bankid="",bankname="",counselorid="",acountid="",accountno="",acntname=""
            ,servicetax="",accountname="";
    ProgressDialog dialog;
    RequestQueue requestQueue;
    ArrayList<String> arrayListBankNames,arrayListBankId,arrayListActNames;
    ArrayList<DataAccountName> arrayListAccountName;
    CheckBox checkboxAttachReceipt;

    String dtPaymentDate="",cPaymentMode="",cAmountINR="",cAmountUSD="",cPaymentFrom="",cBankName="",cFrmAccountNo="",cRefNo="",dtReceivedDate="",nAccountID="",cSTax="",cFinalAmount="",cCurrentLocalRateD=""
            ,CollegeFee1YearD="",HostalFee1YearD="",OneTimeProcessingFeeD="",Accidental_Insurance="",MessFeeD="",IndiaOTC=""
            ,AirFareFeeR="",ReceiptName="",Remarks="";

    SharedPreferences sp;
    private boolean isSpinnerTouched = false;
    String totalFeeD="0",totalFeeR="0",fileID,candidatename;

    int t11,t12,t13,t14,t15,t16,t17,ts,ti,totalUSD=0,totalINR=0,flag=0,temp=0,res=0;
    private String [] mSearchMethod = {"Select","Cash","Account Transfer","Cheque/DemandDraft"};

    public static final int GET_FROM_GALLERY = 1;
    String uploadFilePath = "";
    String uploadFileName = "",urlSMSDate;
    String upLoadServerUri = null;
    int serverResponseCode = 0;
    LinearLayout linearImage;
    Thread thread;
    long timeout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_payment_entry);
            initialize();
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            imgPath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                }
            });
            checkboxAttachReceipt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        linearImage.setVisibility(View.VISIBLE);
                    } else {
                        linearImage.setVisibility(View.GONE);
                    }
                }
            });
            spinnerAccountName.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isSpinnerTouched = true;
                    return false;
                }
            });
            txtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtSubmitCliked();
                }
            });
            txtReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PaymentEntry.this, PaymentEntry.class);
                    intent.putExtra("FileNo1", fileID);
                    intent.putExtra("CName", candidatename);
                    startActivity(intent);
                    finish();
                }
            });
            txtUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dt = new CommonMethods().getDate();
                    String tm = new CommonMethods().getTIme();
                    ReceiptName = fileID + "_" + nAccountID + "_" + dt + "_" + tm;
                    upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=120&FileName=" + ReceiptName;
                    if (temp == 1) {
                        dialog = ProgressDialog.show(PaymentEntry.this, "", "Uploading Payment Receipt", true);

                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //to upload photo of receipt selected to server
                                uploadFile(uploadFilePath + "" + uploadFileName);
                                // insertCallInfo();

                            }
                        }).start();
                    }
                }
            });
            txtSubmitRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dollarrate = edtCurrentDollarRate.getText().toString();
                    if (dollarrate.length() == 0) {
                        linearUnderCurrentDollar.setVisibility(View.GONE);
                        edtCurrentDollarRate.setError("Invalid dollar rate");
                    } else {
                        linearUnderCurrentDollar.setVisibility(View.VISIBLE);
                    }
                }
            });
            spinnerPaymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String paymentmode = spinnerPaymentMode.getSelectedItem().toString();
                    if (paymentmode.contains("Cash")) {
                        linearBankName.setVisibility(View.GONE);
                        linearAcntRef.setVisibility(View.GONE);
                       }
                    else {
                        linearBankName.setVisibility(View.VISIBLE);
                        linearAcntRef.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            edtAirFeeR.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if airfare discount field is changed in INR set updated value to edtAirfareDiscount
                    getAmountINR("AirFee");
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });
            edtIndiaOTC.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if IndiaOTC discount field is changed in INR set updated value to edtIndiaOTCDiscount
                    getAmountINR("IndiaOTC");
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });

            edtClgFeeYearD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if airfare discount field is changed in INR set updated value to edtAirfareDiscount
                    getAmountUSD("ClgFeeD");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            edtHostalFeeYearD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if hostal discount field is changed in USD set updated value to edtHostalDiscount
                    String amt = edtHostalFeeYearD.getText().toString();
                    getAmountUSD("HostalFeeD");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtOTPFeeD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if OTP discount field is changed in USD set updated value to edtOTPDiscount
                    String amt = edtOTPFeeD.getText().toString();
                    getAmountUSD("OTPFee");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtAccidentalInsurance.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if accidental discount field is changed in USD set updated value to edtAccidentalDiscount
                    String amt = edtAccidentalInsurance.getText().toString();
                    getAmountUSD("Accidental");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edtMess.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //if mess discount field is changed in USD set updated value to edtMessDiscount
                    String amt = edtMess.getText().toString();
                    getAmountUSD("Mess");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            spinnerAccountName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    accountname = spinnerAccountName.getSelectedItem().toString();
                    accountno = arrayListAccountName.get(position).getAccountno();
                    servicetax = arrayListAccountName.get(position).getServicetax();
                    acountid = arrayListAccountName.get(position).getAccountid();

                    if (isSpinnerTouched) {
                        if (accountname.contains("Cash")) {
                            txtAccountId.setText(accountno);
                            txtServiceTax.setText(servicetax);
                            if (edtAmountInINR.getText().toString().isEmpty()) {
                                txtFinalAmount.setText("0");
                            } else {
                                txtFinalAmount.setText(edtAmountInINR.getText().toString());
                            }
                            txtSelectAcntName.setVisibility(View.GONE);
                        } else {
                            if (edtAmountInINR.getText().toString().length() == 0) {
                                txtSelectAcntName.setText("Please enter amount first");
                                txtSelectAcntName.setVisibility(View.VISIBLE);
                            } else {
                                txtAccountId.setText(arrayListAccountName.get(position).getAccountno());
                                txtServiceTax.setText(arrayListAccountName.get(position).getServicetax());
                                servicetax = txtServiceTax.getText().toString();
                                if (!servicetax.equals("0")) {
                                    double FnlAmt = Double.parseDouble(edtAmountInINR.getText().toString()) * (Double.parseDouble(servicetax)) / 100;
                                    double finalPayment = (Double.parseDouble(edtAmountInINR.getText().toString()) - FnlAmt);
                                    txtFinalAmount.setText(String.valueOf(finalPayment));
                                } else {
                                    txtFinalAmount.setText(edtAmountInINR.getText().toString());
                                }
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinnerPaymentMode.setSelection(0);
                }
            });
            linearCalender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String myFormat = "yyyy/MM/dd"; //Change as you need
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                            // txtDate.setText(sdf.format(myCalendar.getTime()));
                            txtSelectedDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    };
                    DatePickerDialog dpDialog = new DatePickerDialog(PaymentEntry.this, listener, year, month, day);
                    //myCalendar.add(Calendar.YEAR);
                    // dpDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                    dpDialog.show();

                    //  showDialog(999);
                }
            });
            linearCalender1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String myFormat = "yyyy/MM/dd"; //Change as you need
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                            // txtDate.setText(sdf.format(myCalendar.getTime()));
                            txtSelectedDate1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    };
                    DatePickerDialog dpDialog = new DatePickerDialog(PaymentEntry.this, listener, year, month, day);
                    dpDialog.show();

                    //  showDialog(999);
                }
            });
            if (CheckInternetSpeed.checkInternet(PaymentEntry.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                dialog.dismiss();

                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(PaymentEntry.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                dialog = ProgressDialog.show(PaymentEntry.this, "", "Getting bank names", true);
                newThreadInitilization(dialog);
                //to get list of bank names
                getBanknames();
            }
        }catch (Exception e)
        {
            Toast.makeText(PaymentEntry.this,"Errorcode-461 PaymentEntry onCreate"+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }//onCreate

    private void initialize() {
        requestQueue = Volley.newRequestQueue(PaymentEntry.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", "");
        timeout = sp.getLong("TimeOut", 0);
        clienturl = sp.getString("ClientUrl", "");
        actname=getIntent().getStringExtra("ActName");

        counselorid = sp.getString("Id", "");
        counselorid = counselorid.replace(" ", "");
        linearAcntRef = findViewById(R.id.linearAcntRef);
        linearBankName = findViewById(R.id.linearBankname);
        linearUnderCurrentDollar = findViewById(R.id.linearUnderDollarRate);
        spinnerPaymentMode = findViewById(R.id.spinnerPaymentMode);
        spinnerAccountName = findViewById(R.id.spinnerPaymentMode1);
        spinnerBankName = findViewById(R.id.spinnerBankName);
        edtAmountInINR = findViewById(R.id.edtAmountInINR);
        edtPaymentReceivedFrom = findViewById(R.id.edtPaymentReceivedFrom);
        edtAccountNo = findViewById(R.id.edtAccountNo);
        edtRefNo = findViewById(R.id.edtRefNo);
        edtAmtInDollar = findViewById(R.id.edtAmountInDollar);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtSelectedDate1 = findViewById(R.id.txtSelectedDate1);
        linearCalender = findViewById(R.id.linearCalender);
        linearCalender1 = findViewById(R.id.linearCalender1);
        imgBack = findViewById(R.id.img_back);
        edtCurrentDollarRate = findViewById(R.id.edtCurrentDollarRate);
        txtSubmitRate = findViewById(R.id.txtSubmitDollarRate);
        txtAccountId = findViewById(R.id.txtAccountId);
        txtServiceTax = findViewById(R.id.txtServiceTax);
        txtFinalAmount = findViewById(R.id.txtFinalAmount);
        txtSelectAcntName = findViewById(R.id.txtSelectAcntname);
        edtClgFeeYearD = findViewById(R.id.edtCollegeFee1YearD1);
        edtHostalFeeYearD = findViewById(R.id.edtHostalFee1YearD);
        edtOTPFeeD = findViewById(R.id.edtOneTimeProcessingFeeD);
        edtAccidentalInsurance = findViewById(R.id.edtAccidentalInsurance);
        edtMess = findViewById(R.id.edtMess);
        edtAirFeeR = findViewById(R.id.edtAirFareFeeR);
        edtIndiaOTC = findViewById(R.id.edtIndiaOTC);
        txtTotalFeeD = findViewById(R.id.txtTotalFeeYearD);
        txtTotalFeeR = findViewById(R.id.txtTotalFeeYearR);
        txtGreaterAmtUSD = findViewById(R.id.txtGreaterAmtUSD);
        txtGreaterAmtINR = findViewById(R.id.txtGreaterAmtINR);
        txtSubmit = findViewById(R.id.txtSubmitPaymentEntry);
        txtUpload = findViewById(R.id.txtUploadPaymentEntry);
        txtReset = findViewById(R.id.txtResetPaymentEntry);
        txtSelectDateError = findViewById(R.id.txtSelectDateError);
        txtSelectDateError1 = findViewById(R.id.txtSelectDateError1);
        txtPaymentModeError = findViewById(R.id.txtPaymentModeError);
        txtFID = findViewById(R.id.txtFID);
        edtRemarks = findViewById(R.id.edtRemarks);
        imgReceipt = findViewById(R.id.imgReceipt);
        imgPath = findViewById(R.id.imgDoc);
        txtInvalidAmount = findViewById(R.id.txtInvalidAmount);
        checkboxAttachReceipt = findViewById(R.id.checkboxAttachReceipt);
        linearImage = findViewById(R.id.linearImage);
        arrayListBankNames = new ArrayList<>();
        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(PaymentEntry.this, R.layout.spinner_item1, mSearchMethod);
        spinnerPaymentMode.setAdapter(mSpinnerAdapter);
        myCalendar = Calendar.getInstance();

        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);

        fileID = getIntent().getStringExtra("FileNo1");
        candidatename = getIntent().getStringExtra("CName");
        txtFID.setText("(" + fileID + ")(" + candidatename + ")");
    }//initialize

    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(PaymentEntry.this, "Connection Aborted.", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(Home.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Log.d("TimeThread","cdvmklmv");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(PaymentEntry.this,ClientAccount.class);
        if(actname.contains("SummaryDetails")) {
            intent.putExtra("ActName", actname);
        }else {
            intent.putExtra("ActName", "PaymentEntry");
        }
        intent.putExtra("FileID",fileID);
        startActivity(intent);
        finish();
        //super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                temp=1;
                Uri selectedImage = data.getData();
                Bitmap bitmap = null;
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imgReceipt.setImageBitmap(bitmap);
                String path = getPath(getApplicationContext(), selectedImage);
                uploadFilePath = path.substring(path.indexOf("/storage"), path.lastIndexOf("/") + 1);
                //  Log.d("Path", uploadFilePath);
                uploadFileName = path.substring(uploadFilePath.lastIndexOf("/") + 1);
                Log.d("FileName",uploadFilePath+""+uploadFileName);
            }  catch (Exception e)
            {
                // TODO Auto-generated catch block
                Toast.makeText(PaymentEntry.this,"Errorcode-462 PaymentEntry OnActivityResult "+e.toString(),Toast.LENGTH_SHORT).show();
                Log.d("Exception", String.valueOf(e));
            }
        }
    }//onActivityResult
    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }//getPath

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
                    Toast.makeText(PaymentEntry.this, "Source File not exist :"
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
                   // dialog.dismiss();
                        res=1;
                    insertPaymentEntry();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(PaymentEntry.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

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
                        Toast.makeText(PaymentEntry.this, "MalformedURLException",
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
                        Toast.makeText(PaymentEntry.this, "Got Exception while uploading record ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }//uploadFile
    public void txtSubmitCliked()
    {
        try {
            flag = 0;
            dtPaymentDate = txtSelectedDate.getText().toString();
            cPaymentMode = spinnerPaymentMode.getSelectedItem().toString();
            cAmountINR = edtAmountInINR.getText().toString();
            cAmountUSD = edtAmtInDollar.getText().toString();
            cPaymentFrom = edtPaymentReceivedFrom.getText().toString();
            cBankName = spinnerBankName.getSelectedItem().toString();
            cFrmAccountNo = edtAccountNo.getText().toString();
            cRefNo = edtRefNo.getText().toString();
            dtReceivedDate = txtSelectedDate1.getText().toString();
            nAccountID = acountid;
            cSTax = txtServiceTax.getText().toString();
            cFinalAmount = txtFinalAmount.getText().toString();
            cCurrentLocalRateD = edtCurrentDollarRate.getText().toString();
            CollegeFee1YearD = "0";
            HostalFee1YearD = "0";
            OneTimeProcessingFeeD = "0";
            Accidental_Insurance = "0";
            MessFeeD = "0";
            IndiaOTC = "0";
            AirFareFeeR = "0";
       /* CollegeFee1YearD=String.valueOf(t11);
        HostalFee1YearD=String.valueOf(t12);
        OneTimeProcessingFeeD=String.valueOf(t13);
        Accidental_Insurance=String.valueOf(t14);
        MessFeeD=String.valueOf(t15);
        IndiaOTC=String.valueOf(t17);
        AirFareFeeR=String.valueOf(t16);*/
            Remarks = edtRemarks.getText().toString();
            if (cCurrentLocalRateD.length() == 0) {
                flag = 1;
                edtCurrentDollarRate.setError("Invalid dollar rate");
            }
            if (cPaymentFrom.length() == 0) {
                flag = 1;
                edtPaymentReceivedFrom.setError("Please enter name");
            }
            if (cPaymentMode.contains("Select")) {
                flag = 1;
                txtPaymentModeError.setVisibility(View.VISIBLE);
            } else {
                flag = 0;
                txtPaymentModeError.setVisibility(View.GONE);
            }

            if (cAmountUSD.isEmpty() && cAmountINR.isEmpty()) {
                flag = 1;
                txtInvalidAmount.setVisibility(View.VISIBLE);
            } else {
                txtInvalidAmount.setVisibility(View.GONE);
            }

            if (dtPaymentDate.contains("Payment Date")) {
                flag = 1;
                txtSelectDateError.setVisibility(View.VISIBLE);
                // dtPaymentDate="1900/01/01";
            } else {
                flag = 0;
                txtSelectDateError.setVisibility(View.GONE);
            }
            if (dtReceivedDate.contains("Payment Date")) {
                // dtReceivedDate="1900/01/01";
                flag = 1;
                txtSelectDateError1.setVisibility(View.VISIBLE);
                // dtPaymentDate="1900/01/01";
            } else {
                flag = 0;
                txtSelectDateError1.setVisibility(View.GONE);
            }
            if (res == 0) {
                ReceiptName = "NA";
            }

            if (edtAmountInINR.getText().toString().length() == 0) {
                ti = 0;
            } else {
                ti = Integer.parseInt(cAmountINR);
            }

            if (edtAmtInDollar.getText().toString().length() == 0) {
                flag = 1;
                edtAmtInDollar.setError("Invalid amount");
                ts = 0;
            } else {
                ts = Integer.parseInt(cAmountUSD);
            }

            if (Remarks.isEmpty()) {
                //  Remarks="NA";
                flag = 1;
                edtRemarks.setError("Please enter remarks");
            }
            //  ReceiptName=fileID+"_"+nAccountID+"_"+dt+"_"+tm;

            if (totalUSD > ts) {
                txtGreaterAmtUSD.setVisibility(View.VISIBLE);
                flag = 1;
            } else {
                txtGreaterAmtUSD.setVisibility(View.GONE);
            }

            if (totalINR > ti) {
                txtGreaterAmtINR.setVisibility(View.VISIBLE);
                flag = 1;
            } else {
                txtGreaterAmtINR.setVisibility(View.GONE);
            }
            if (accountname.contains("Select")) {
                txtSelectAcntName.setText("Please select account first");
                txtSelectAcntName.setVisibility(View.VISIBLE);
                flag = 1;
            }

            if (flag == 0) {
                if (CheckInternetSpeed.checkInternet(PaymentEntry.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                    alertDialogBuilder.setTitle("No Internet connection!!!")
                            .setMessage("Can't do further process")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                    dialog.dismiss();

                                }
                            }).show();
                } else if (CheckInternetSpeed.checkInternet(PaymentEntry.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                    alertDialogBuilder.setTitle("Slow Internet speed!!!")
                            .setMessage("Can't do further process")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //insertIMEI();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                    alertDialogBuilder.setMessage("Are you sure you want to insert this payment entry")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog1, int which) {
                                    //insertIMEI();
                                    dialog1.dismiss();
                                    if (temp == 1) {
                                        String dt = new CommonMethods().getDate();
                                        String tm = new CommonMethods().getTIme();
                                        ReceiptName = fileID + "_" + nAccountID + "_" + dt + "_" + tm;
                                        upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=120&FileName=" + ReceiptName;
                                        Log.d("txtsubmit", upLoadServerUri);
                                        dialog = ProgressDialog.show(PaymentEntry.this, "", "Inserting payment entry", true);
                                        newThreadInitilization(dialog);
                                        new Thread(new Runnable() {
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                uploadFile(uploadFilePath + "" + uploadFileName);
                                                // insertCallInfo();

                                            }
                                        }).start();
                                        //  dialog.dismiss();
                                        //  dialog = ProgressDialog.show(PaymentEntry.this, "", "Inserting payment entry", true);

                                    } else {
                                        dialog = ProgressDialog.show(PaymentEntry.this, "", "Inserting payment entry", true);
                                        newThreadInitilization(dialog);
                                        //to insert payment entry to database
                                        insertPaymentEntry();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //insertIMEI();
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }

            }
        }catch (Exception e)
        {
            Toast.makeText(PaymentEntry.this,"Errorcode-463 PaymentEntry txtSubmitClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//txtSubmitClicked
    public void insertPaymentEntry()
    {
        try{
          // String url="";
            String url = clienturl + "?clientid=" + clientid + "&caseid=119&nFileNo="+fileID+"&dtPaymentDate="+dtPaymentDate+"&cPaymentMode="+cPaymentMode+"&cAmountINR="+cAmountINR+"&cAmountUSD="+cAmountUSD+"&cPaymentFrom="+cPaymentFrom+
                "&cBankName="+cBankName+"&cFrmAccountNo="+cFrmAccountNo+"&cRefNo="+cRefNo+"&dtReceivedDate="+dtReceivedDate+"&nAccountID="+nAccountID+"&cSTax="+cSTax+"&cFinalAmount="+cFinalAmount+"&cCurrentLocalRateD=" +cCurrentLocalRateD+
                "&CollegeFee1YearD=0&HostalFee1YearD=0&OneTimeProcessingFeeD=0&Accidental_Insurance=0&MessFeeD=0&IndiaOTC=0&AirFareFeeR=0&ReceiptName="+ReceiptName+"&Remarks="+Remarks;
        Log.d("PaymentEntryUrl", url);
        if (CheckInternet.checkInternet(PaymentEntry.this)) {
            if(CheckServer.isServerReachable(PaymentEntry.this)) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("PaymentEntryResponse", response);
                                    dialog.dismiss();
                                   if(response.contains("Data inserted successfully"))
                                   {
                                       Toast.makeText(PaymentEntry.this,"Payment entry successful",Toast.LENGTH_SHORT).show();
                                       onBackPressed();
                                   }
                                   else {
                                       Toast.makeText(PaymentEntry.this,"Payment entry failed",Toast.LENGTH_SHORT).show();
                                   }

                                } catch (Exception e) {
                                    Toast.makeText(PaymentEntry.this,"Errorcode-465 PaymentEntry insertPaymentEntryResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;

                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(PaymentEntry.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                alertDialogBuilder.setTitle("Network issue!!!!")
                        .setMessage("Try after some time!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                dialog.dismiss();

                            }
                        }).show();
            }
        } else {
            dialog.dismiss();
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
            alertDialogBuilder.setTitle("No Internet connection!!!")
                    .setMessage("Can't do further process")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                            dialog.dismiss();

                        }
                    }).show();
        }

    }catch (Exception e)
    {
        Toast.makeText(PaymentEntry.this,"Errorcode-464 PaymentEntry insertPaymentEntry "+e.toString(),Toast.LENGTH_SHORT).show();
    }
    }//insertPaymentEntry
    public void getAmountUSD(String edtname)
    {
        try {
            if (edtname.contains("ClgFeeD")) {
                if (edtClgFeeYearD.getText().toString().isEmpty()) {
                    t11 = 0;
                } else {
                    t11 = Integer.parseInt(edtClgFeeYearD.getText().toString());
                }
            } else if (edtname.contains("HostalFeeD")) {
                if (edtHostalFeeYearD.getText().toString().isEmpty()) {
                    t12 = 0;
                } else {
                    t12 = Integer.parseInt(edtHostalFeeYearD.getText().toString());
                }
            } else if (edtname.contains("OTPFee")) {

                if (edtOTPFeeD.getText().toString().isEmpty()) {
                    t13 = 0;
                } else {
                    t13 = Integer.parseInt(edtOTPFeeD.getText().toString());
                }
            } else if (edtname.contains("Accidental")) {
                if (edtAccidentalInsurance.getText().toString().isEmpty()) {
                    t14 = 0;
                } else {
                    t14 = Integer.parseInt(edtAccidentalInsurance.getText().toString());
                }
            } else if (edtname.contains("Mess")) {
                if (edtMess.getText().toString().isEmpty()) {
                    t15 = 0;
                } else {
                    t15 = Integer.parseInt(edtMess.getText().toString());
                }
            }

            totalUSD = t11 + t12 + t13 + t14 + t15;
            totalFeeD = String.valueOf(totalUSD);

            txtTotalFeeD.setText(totalFeeD);
        }catch (Exception e)
        {
            Toast.makeText(PaymentEntry.this,"Errorcode-466 PaymentEntry getAmountUSD "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//getAmountUSD
    public void getAmountINR(String edtname) {
        try {
            if (edtname.contains("AirFee")) {
                if (edtAirFeeR.getText().toString().isEmpty()) {
                    t16 = 0;
                } else {
                    t16 = Integer.parseInt(edtAirFeeR.getText().toString());
                }
            } else if (edtname.contains("IndiaOTC")) {
                if (edtIndiaOTC.getText().toString().isEmpty()) {
                    t17 = 0;
                } else {
                    t17 = Integer.parseInt(edtIndiaOTC.getText().toString());
                }
            }

            totalINR = t16 + t17;
            totalFeeR = String.valueOf(totalINR);
      /*  if(total>Integer.parseInt(edtAmountInINR.getText().toString())) {
            txtGreaterAmtINR.setVisibility(View.VISIBLE);
        }else {
            txtGreaterAmtINR.setVisibility(View.GONE);
        }*/
            txtTotalFeeR.setText(totalFeeR);
        }catch (Exception e)
        {
            Toast.makeText(PaymentEntry.this,"Errorcode-467 PaymentEntry getAmountINR "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//getAmountINR
    public void getBanknames()
    {
        try {
            arrayListBankNames = new ArrayList<>();
            arrayListBankId=new ArrayList<>();
           String url = clienturl + "?clientid=" + clientid + "&caseid=117";
            Log.d("BankNamesUrl", url);
            if (CheckInternet.checkInternet(PaymentEntry.this)) {
                if(CheckServer.isServerReachable(PaymentEntry.this)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("BankNamesResponse", response);
                                       // dialog.dismiss();
                                        //get payment mode info like account id
                                        getPaymentMode();
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                bankid = jsonObject1.getString("nBankID");
                                                bankname = jsonObject1.getString("cBank");
                                                arrayListBankNames.add(bankname);
                                                arrayListBankId.add(bankid);
                                            }
                                        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(PaymentEntry.this,
                                                R.layout.spinner_item1, arrayListBankNames);
                                        spinnerBankName.setAdapter(mSpinnerAdapter);

                                    } catch (JSONException e) {
                                        Toast.makeText(PaymentEntry.this,"Errorcode-469 PaymentEntry getBankNamesResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                        Log.d("Exception", String.valueOf(e));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;

                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(PaymentEntry.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                }else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                    dialog.dismiss();

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                dialog.dismiss();

                            }
                        }).show();
            }

        }catch (Exception e)
        {
            Toast.makeText(PaymentEntry.this,"Errorcode-468 PaymentEntry getBankNames "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//getBankNames
    public void getPaymentMode()
    {
        try {
            arrayListAccountName = new ArrayList<>();
            arrayListActNames=new ArrayList<>();

            String url = clienturl + "?clientid=" + clientid + "&caseid=118";
            Log.d("BankNamesUrl", url);
            if (CheckInternet.checkInternet(PaymentEntry.this)) {
                if(CheckServer.isServerReachable(PaymentEntry.this)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("BankNamesResponse", response);
                                        dialog.dismiss();
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            acountid=jsonObject1.getString("nAccountID");
                                            accountno = jsonObject1.getString("cAccountNo");
                                            acntname = jsonObject1.getString("cAccountName");
                                            servicetax = jsonObject1.getString("cServiceTax");
                                            DataAccountName dataAccountName=new DataAccountName(acntname,acountid,servicetax,accountno);
                                            arrayListAccountName.add(dataAccountName);
                                            arrayListActNames.add(acntname);

                                        }
                                        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(PaymentEntry.this,
                                                R.layout.spinner_item1, arrayListActNames);
                                        spinnerAccountName.setAdapter(mSpinnerAdapter);

                                    } catch (JSONException e) {
                                        Toast.makeText(PaymentEntry.this,"Errorcode-471 PaymentEntry getPaymentModeResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                        Log.d("Exception", String.valueOf(e));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;

                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(PaymentEntry.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                }else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                    dialog.dismiss();

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentEntry.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                dialog.dismiss();

                            }
                        }).show();
            }

        }catch (Exception e)
        {
            Toast.makeText(PaymentEntry.this,"Errorcode-470 PaymentEntry getPaymentMode "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//getPaymentMode

}
