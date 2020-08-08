package com.bizcall.wayto.mentebit13;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class PaymentApproveOrReject extends AppCompatActivity {

    AlertDialog alertDialog1;
    RequestQueue requestQueue;
    ProgressDialog dialog;
    String clienturl,clientid,remarks,paymentid,status;
    String paymentID,fileNo,cname,counselorname,amountINR,amountUSD,accountName,dollarrate,paymentdate,receiveddate,paymentfrom,recieptname;
    SharedPreferences sp;
    int flag=0;
    String totalFeeD="0",totalFeeR="0",fileID,candidatename;



    int t11,t12,t13,t14,t15,t16,t17,ts,ti,totalUSD=0,totalINR=0,temp=0,res=0;
    String fname,lname,mobile,passport,panno,collegeFee1YearD,hostalFee1YearD,oneTimeProcessingFeeD,otherFee1Head,otherFee1D,otherFee2Head,otherFee2D,otherFee3Head,otherFee3D,mess,totalFeeYearD,airFareFeeR,indiaOTC,totalFeeYearR;
    int clgfeetotal,clgfeeReceived,clgfeeDue,hostalFeeTotal,hostalFeeReceived,hostalFeeDue,OTPReceived,OTPTotal,OTPDue,accidentalTotal,
            accidentalReceived,accidentalDue,messTotal,messReceived,messDue,totalReceivedD,totalDueD,airfareTotal,airfareReceived,airfareDues,
            indiaOTCTotal,indiaOTCRecceived,indiaOTCDues,totalReceivedR,totalDuesR;
    String collegeFee1YearD1,collegeFeeFinal,hostalFee1YearD1,hostalFeeFinal,OTPFeeFinal,accidentalFeeFinal,messFeeFinal,IndiaOTCFeeFinal,AirFareFinal,totalFeeFinalD,totalFeeFinalR,oneTimeProcessingFeeD1,Accidental_Insurance,MessFeeD,IndiaOTC,AirFareFee;
    String  CollegeFee1YearD,HostalFee1YearD,OneTimeProcessingFeeD,AirFareFeeR;
    ArrayList<String> arrayListClgId,arrayListCollege;
    TextView txtPaymentId1,txtFileNo1,txtCandidateName1,txtCounselorName1,txtAmountINR1,txtAmountUSD1,txtApprove,txtReject;
    EditText edtRemarks;
     ImageView imgClose,imgBack;
    TextView txtDollarRate,txtPaymentDate,txtReceivedDate,txtPaymentFrom,txtReceiptName,txtSubmit,txtTotalRecivedUSD,txtTotalRecivedINR,txtAccountName;
    TextView txtCollegeFeeTotalD,txtCollegeFeeFinal,txtCollegeFeeReceivedD,txtHostalTotalFeeD,txtHostalFinalFeeD,txtHostalReceivedFeeD,txtOTPTotalFeeD,
            txtOTPFeeFinalD,txtOTPReceivedFeeD,txtAccidentalTotal,txtAccidentalFinal,txtAccidentalReceived,txtMessTotal,txtMessFinal,txtMessReceived,txtTotalFeeD,txtFeeFinalD,txtReceivedFeeD,txtDueFeeD;
    TextView txtAirFareFeeTotal,txtAirFareFeeFinal,txtAirFareFeeReceived,txtIndiaOTCTotal,txtIndiaOTCFinal,txtIndiaOTCReceived,txtTotalFeeTotalR,txtTotalFeeFinalR,txtTotalFeeReceivedR,txtTotalFeeDuesR;
    TextView  txtGreaterAmtUSD,txtGreaterAmtINR, txtCollegeFee1YearD1,txtHostalFee1YearD1,txtOneTimeProcessingFeeD1,txtAccidentalInsurance1,txtMess1,txtTotalFeeYearD1,txtDiscountDetails;
    EditText txtCollegeFeeDueD,txtHostalDueFeeD,txtOTPDueFeeD,txtAccidentalDue,txtMessDue,txtAirFareFeeDues,txtIndiaOTCDues;
    String inr,usd;
    Thread thread;
    long timeout;
    private NotificationManager mNotifyManager;
    private AsyncTask mMyTask;
    String strDocName;
    Uri savedImageURI;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.alert_approve);
            requestQueue = Volley.newRequestQueue(PaymentApproveOrReject.this);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clientid = sp.getString("ClientId", "");
            clienturl = sp.getString("ClientUrl", "");
            timeout = sp.getLong("TimeOut", 0);

            txtDollarRate = findViewById(R.id.txtDollarRate);
            txtPaymentDate = findViewById(R.id.txtPaymentDate);
            txtReceivedDate = findViewById(R.id.txtReceivedDate);
            txtPaymentFrom = findViewById(R.id.txtPaymentFrom);
            txtReceiptName = findViewById(R.id.txtReceiptName);

            txtAccountName = findViewById(R.id.txtAccountName);
            txtCollegeFeeTotalD = findViewById(R.id.txtCollegeFeeDTotalVal);
            txtCollegeFeeReceivedD = findViewById(R.id.txtCollegeFeeDReceivedVal);
            txtCollegeFeeDueD = findViewById(R.id.txtCollegeFeeDDueVal);
            txtTotalRecivedUSD = findViewById(R.id.txtTotalRecivedUSD);
            txtTotalRecivedINR = findViewById(R.id.txtTotalReceivedINR);
            txtHostalTotalFeeD = findViewById(R.id.txtHostalFeeDTotalVal);
            txtHostalReceivedFeeD = findViewById(R.id.txtHostalFeeDReceivedVal);
            txtHostalDueFeeD = findViewById(R.id.txtHostalFeeDDueVal);
            txtCollegeFeeFinal = findViewById(R.id.txtCollegeFeeDFinal);
            txtHostalFinalFeeD = findViewById(R.id.txtHostalFeeFinalD);
            txtOTPFeeFinalD = findViewById(R.id.txtOTPFeeDFinalVal);
            txtAccidentalFinal = findViewById(R.id.txtAccidentalFinalVal);
            txtMessFinal = findViewById(R.id.txtMessFinalVal);
            txtFeeFinalD = findViewById(R.id.txtFeeFinalD);
            txtAirFareFeeFinal = findViewById(R.id.txtAirFareFeeFinallR);
            txtIndiaOTCFinal = findViewById(R.id.txtIndiaOTCFinal);
            txtTotalFeeFinalR = findViewById(R.id.txtTotalFeeFinalR);

            txtOTPTotalFeeD = findViewById(R.id.txtOTPFeeDTotalVal);
            txtOTPReceivedFeeD = findViewById(R.id.txtOTPFeeDReceivedVal);
            txtOTPDueFeeD = findViewById(R.id.txtOTPFeeDDueVal);

            txtAccidentalTotal = findViewById(R.id.txtAccidentalTotalVal);
            txtAccidentalReceived = findViewById(R.id.txtAccidentalReceivedVal);
            txtAccidentalDue = findViewById(R.id.txtAccidentalDueVal);
            txtMessTotal = findViewById(R.id.txtMessTotalVal);
            txtMessReceived = findViewById(R.id.txtMessReceivedVal);
            txtMessDue = findViewById(R.id.txtMessDueVal);

            txtTotalFeeD = findViewById(R.id.txtTotalFeeD);
            txtReceivedFeeD = findViewById(R.id.txtReceivedFeeD);
            txtDueFeeD = findViewById(R.id.txtDueFeeD);

            txtCollegeFee1YearD1 = findViewById(R.id.txtCollegeFee1YearD1);
            txtHostalFee1YearD1 = findViewById(R.id.txtHostalFee1YearD1);
            txtOneTimeProcessingFeeD1 = findViewById(R.id.txtOneTimeProcessingFeeD1);
            txtAccidentalInsurance1 = findViewById(R.id.txtAccidentalInsurance1);
            txtMess1 = findViewById(R.id.txtMess1);
            txtTotalFeeYearD1 = findViewById(R.id.txtTotalFeeYearD1);
            txtAirFareFeeTotal = findViewById(R.id.txtAirFareFeeTotalR);
            txtAirFareFeeReceived = findViewById(R.id.txtAirFareFeeReceivedR);
            txtAirFareFeeDues = findViewById(R.id.txtAirFareFeeDuesR);
            txtIndiaOTCTotal = findViewById(R.id.txtIndiaOTCTotal);
            txtIndiaOTCReceived = findViewById(R.id.txtIndiaOTCReceived);
            txtIndiaOTCDues = findViewById(R.id.txtIndiaOTCDues);
            txtTotalFeeTotalR = findViewById(R.id.txtTotalFeeTotalR);
            txtTotalFeeReceivedR = findViewById(R.id.txtTotalFeeReceivedR);
            txtTotalFeeDuesR = findViewById(R.id.txtTotalFeeYearDuesR);
            txtGreaterAmtUSD = findViewById(R.id.txtGreaterAmtUSD);
            txtGreaterAmtINR = findViewById(R.id.txtGreaterAmtINR);


            txtPaymentId1 = findViewById(R.id.txtPaymentID);
            txtFileNo1 = findViewById(R.id.txtFileNo);
            txtCandidateName1 = findViewById(R.id.txtCandidateName);
            txtCounselorName1 = findViewById(R.id.txtCounselorName);
            txtAmountINR1 = findViewById(R.id.txtAmountINR);
            txtAmountUSD1 = findViewById(R.id.txtAmountUSD);
            txtApprove = findViewById(R.id.txtApprove);
            txtReject = findViewById(R.id.txtReject);
            edtRemarks = findViewById(R.id.edtRemarks);
            imgClose = findViewById(R.id.imgClose);
            paymentID = getIntent().getStringExtra("PaymentID");
            fileNo = getIntent().getStringExtra("FileID");
            cname = getIntent().getStringExtra("CandidateName");
            counselorname = getIntent().getStringExtra("CounselorName");
            amountINR = getIntent().getStringExtra("AmountINR");
            amountUSD = getIntent().getStringExtra("AmountUSD");
            accountName = getIntent().getStringExtra("AccountName");
            dollarrate = getIntent().getStringExtra("DollarRate");
            receiveddate = getIntent().getStringExtra("ReceivedDate");
            paymentdate = getIntent().getStringExtra("PaymentDate");
            paymentfrom = getIntent().getStringExtra("PaymentFrom");
            recieptname = getIntent().getStringExtra("ReceiptName");


            imgBack = findViewById(R.id.img_back);

            txtPaymentId1.setText("PaymentID - " + paymentID);
            txtFileNo1.setText(" (" + fileNo + " - ");
            txtCandidateName1.setText(cname + " - ");
            txtCounselorName1.setText(counselorname + " )");
            txtAccountName.setText(accountName);
            txtDollarRate.setText(dollarrate);
            txtPaymentDate.setText(paymentdate);
            txtReceivedDate.setText(receiveddate);
            txtPaymentFrom.setText(paymentfrom);
            txtReceiptName.setText(recieptname);

            if (CheckInternetSpeed.checkInternet(PaymentApproveOrReject.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
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
            } else if (CheckInternetSpeed.checkInternet(PaymentApproveOrReject.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
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
                dialog = ProgressDialog.show(PaymentApproveOrReject.this, "", "Loading package details", true);
                newThreadInitilization(dialog);
                getPackageDetails(fileNo);
            }
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            if (amountINR.isEmpty()) {
                txtAmountINR1.setText("0");
            } else {
                txtAmountINR1.setText(amountINR);
            }
            if (amountUSD.isEmpty()) {
                txtAmountUSD1.setText("0");
            } else {
                txtAmountUSD1.setText(amountUSD);
            }
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                }
            });

            txtAirFareFeeDues.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getAmountINR("AirFee");
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            txtIndiaOTCDues.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getAmountINR("IndiaOTC");
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });

            txtCollegeFeeDueD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getAmountUSD("ClgFeeD");
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });

            txtHostalDueFeeD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //String amt=edtHostalFeeYearD.getText().toString();
                    getAmountUSD("HostalFeeD");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            txtOTPDueFeeD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // String amt=edtOTPFeeD.getText().toString();
                    getAmountUSD("OTPFee");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            txtAccidentalDue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String amt = txtAccidentalDue.getText().toString();
                    getAmountUSD("Accidental");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            txtMessDue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // String amt=edtMess.getText().toString();
                    getAmountUSD("Mess");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            txtApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 0;
                    if (edtRemarks.getText().toString().length() == 0) {
                        flag = 1;
                        edtRemarks.setError("Please enter remarks");
                    }
                    remarks = edtRemarks.getText().toString();

                    paymentid = txtPaymentId1.getText().toString();
                    if (txtCollegeFeeDueD.getText().toString().length() == 0) {
                        CollegeFee1YearD = "0";
                    } else {
                        CollegeFee1YearD = String.valueOf(t11);
                    }
                    if (txtHostalDueFeeD.getText().toString().length() == 0) {
                        HostalFee1YearD = "0";
                    } else {
                        HostalFee1YearD = String.valueOf(t12);
                    }
                    if (txtOTPDueFeeD.getText().toString().length() == 0) {
                        OneTimeProcessingFeeD = "0";
                    } else {
                        OneTimeProcessingFeeD = String.valueOf(t13);
                    }
                    if (txtAccidentalDue.getText().toString().length() == 0) {
                        Accidental_Insurance = "0";
                    } else {
                        Accidental_Insurance = String.valueOf(t14);
                    }
                    if (txtMessDue.getText().toString().length() == 0) {
                        MessFeeD = "0";
                    } else {
                        MessFeeD = String.valueOf(t15);
                    }
                    if (txtIndiaOTCDues.getText().toString().length() == 0) {
                        IndiaOTC = "0";
                    } else {
                        IndiaOTC = String.valueOf(t17);
                    }
                    if (txtAirFareFeeDues.getText().toString().length() == 0) {
                        AirFareFeeR = "0";
                    } else {
                        AirFareFeeR = String.valueOf(t16);
                    }
              /*  ts=Integer.parseInt(txtAmountUSD1.getText().toString());
                ti=Integer.parseInt(txtAmountINR1.getText().toString());

                if(totalUSD>ts) {
                    txtGreaterAmtUSD.setVisibility(View.VISIBLE);
                    flag=1;
                }else {
                    txtGreaterAmtUSD.setVisibility(View.GONE);
                }

                if(totalINR>ti) {
                    txtGreaterAmtINR.setVisibility(View.VISIBLE);
                    flag=1;
                }else {
                    txtGreaterAmtINR.setVisibility(View.GONE);
                }*/

                    if (flag == 0) {
                        String amt, amt1, msg;
                        if (!amountINR.equals("0")) {
                            amt = "INR =>" + amountINR;
                        } else {
                            amt = "";
                        }

                        if (!amountUSD.equals("0")) {
                            amt1 = "USD =>" + amountUSD;
                        } else {
                            amt1 = "";
                        }

                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
                        alertDialogBuilder.setMessage("Have you received amount in " + amt + " " + amt1 + "in account " + accountName + "?")
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog1, int which) {
                                        //insertIMEI();
                                        dialog = ProgressDialog.show(PaymentApproveOrReject.this, "", "Accepting payment", true);
                                        newThreadInitilization(dialog);
                                        approveOrRejectPayment("1");
                                        dialog1.dismiss();


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //insertIMEI();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }
            });
            txtReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 0;
                    if (edtRemarks.getText().toString().length() == 0) {
                        flag = 1;
                        edtRemarks.setError("Please enter remarks");
                    }
                    remarks = edtRemarks.getText().toString();
                    paymentid = txtPaymentId1.getText().toString();

                    if (flag == 0) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
                        alertDialogBuilder.setMessage("Are you sure you want to reject this amount?")
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog1, int which) {
                                        //insertIMEI();
                                        dialog = ProgressDialog.show(PaymentApproveOrReject.this, "", "Rejecting payment", true);
                                        newThreadInitilization(dialog);
                                        approveOrRejectPayment("3");
                                        dialog1.dismiss();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //insertIMEI();
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                    }
                }
            });
            txtReceiptName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strDocName = txtReceiptName.getText().toString();
                    if (!strDocName.contains(".jpg")) {
                        strDocName = strDocName + ".jpg";
                    }
                    String strUrl = "http://anilsahasrabuddhe.in/CRM/wayto965425/PaymentEntry/" + strDocName;
                    Log.d("dw", strUrl);
                    if (!txtReceiptName.getText().toString().equals("NA")) {
                        mMyTask = new PaymentApproveOrReject.DownloadTask().execute(stringToURL(strUrl));
                        Log.d("qqq", strUrl);

                        dialog = new ProgressDialog(PaymentApproveOrReject.this);
                        dialog.setCancelable(true);
                        dialog.setMessage("Downloading " + strDocName);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setProgress(0);
                        dialog.setMax(100);
                        dialog.show();
                    }
                }
            });
        }catch (Exception e)
            {
                Toast.makeText(PaymentApproveOrReject.this, "Errorcode-494 PaymentApproveOrReject onCreate " + e.toString(), Toast.LENGTH_SHORT).show();
            }
    }//onCreate

    protected URL stringToURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        }catch (Exception e)
    {
        Toast.makeText(PaymentApproveOrReject.this, "Errorcode-495 PaymentApproveOrReject stringToURL " + e.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PaymentApproveOrReject.this, "Download failed..", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Initialize ContextWrapper
        File directory = new File(Environment.getExternalStorageDirectory() + "/Bizcall/PaymentEntry");
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
        Log.d("mypath", savedImageURI + "");
        Toast.makeText(PaymentApproveOrReject.this, "Download Path : " + savedImageURI, Toast.LENGTH_SHORT).show();
        // Return the saved image Uri
        return savedImageURI;
    }

    public void DownloadNotification() {
        Random random = new Random();
        int m = random.nextInt(9999-1000)+1000;
        final int NotiId = m;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String id = PaymentApproveOrReject.this.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = PaymentApproveOrReject.this.getString(R.string.default_notification_channel_title); // Default Channel
        NotificationCompat.Builder builder;
        if (mNotifyManager == null) {
            mNotifyManager = (NotificationManager) PaymentApproveOrReject.this.getSystemService(Context.NOTIFICATION_SERVICE);
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
            builder = new NotificationCompat.Builder(PaymentApproveOrReject.this, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/PaymentEntry/" + strDocName );
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                   FileProvider.getUriForFile(PaymentApproveOrReject.this, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file), "*/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(PaymentApproveOrReject.this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setContentTitle("Downloaded Image");
            builder.setContentText(strDocName);
            builder.setSubText("Tap to view Document.");
            builder.setAutoCancel(true);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{100, 100});
        } else {
            builder = new NotificationCompat.Builder(PaymentApproveOrReject.this, id);
            builder.setSmallIcon(R.drawable.docdownload);
            File file = new File(Environment.getExternalStorageDirectory() + "/Bizcall/PaymentEntry/" + strDocName + ".jpg");
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                                   FileProvider.getUriForFile(PaymentApproveOrReject.this, BuildConfig.APPLICATION_ID + ".provider", file) : Uri.fromFile(file),
                            "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //   intent.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "image/*");    //MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            //   intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(PaymentApproveOrReject.this, 0, intent, 0);

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
                                Toast.makeText(PaymentApproveOrReject.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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

    public void getAmountUSD(String edtname)
    {
        try{
        if(edtname.contains("ClgFeeD"))
        {
            if(txtCollegeFeeDueD.getText().toString().isEmpty())
            {
                t11=0;
            }
            else
            {
                t11=Integer.parseInt(txtCollegeFeeDueD.getText().toString());
            }
        }
        else if(edtname.contains("HostalFeeD"))
        {
            if(txtHostalDueFeeD.getText().toString().isEmpty())
            {
                t12=0;
            }
            else {
                t12=Integer.parseInt(txtHostalDueFeeD.getText().toString());
            }
        }
        else if(edtname.contains("OTPFee"))
        {

            if(txtOTPDueFeeD.getText().toString().isEmpty())
            {
                t13=0;
            }
            else
            {
                t13=Integer.parseInt(txtOTPDueFeeD.getText().toString());
            }
        }
        else if(edtname.contains("Accidental"))
        {
            if(txtAccidentalDue.getText().toString().isEmpty())
            {
                t14=0;
            }
            else {
                t14=Integer.parseInt(txtAccidentalDue.getText().toString());
            }
        }
        else if(edtname.contains("Mess"))
        {
            if(txtMessDue.getText().toString().isEmpty())
            {
                t15=0;
            }
            else {
                t15=Integer.parseInt(txtMessDue.getText().toString());
            }
        }

        totalUSD=t11+t12+t13+t14+t15;
        totalFeeD=String.valueOf(totalUSD);

        txtDueFeeD.setText(totalFeeD);
        }catch (Exception e)
        {
            Toast.makeText(PaymentApproveOrReject.this, "Errorcode-496 PaymentApproveOrReject getAmountUSD " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void getAmountINR(String edtname)
    {
        try{
        if(edtname.contains("AirFee"))
        {
            if(txtAirFareFeeDues.getText().toString().isEmpty())
            {
                t16=0;
            }
            else
            {
                t16=Integer.parseInt(txtAirFareFeeDues.getText().toString());
            }
        }
        else if(edtname.contains("IndiaOTC"))
        {
            if(txtIndiaOTCDues.getText().toString().isEmpty())
            {
                t17=0;
            }
            else {
                t17=Integer.parseInt(txtIndiaOTCDues.getText().toString());
            }
        }

        totalINR=t16+t17;
        totalFeeR=String.valueOf(totalINR);
      /*  if(total>Integer.parseInt(edtAmountInINR.getText().toString())) {
            txtGreaterAmtINR.setVisibility(View.VISIBLE);
        }else {
            txtGreaterAmtINR.setVisibility(View.GONE);
        }*/
        txtTotalFeeDuesR.setText(totalFeeR);
        }catch (Exception e)
        {
            Toast.makeText(PaymentApproveOrReject.this, "Errorcode-497 PaymentApproveOrReject getAmountINR " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void getPackageTotalDetails()
    {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=121&FileNo="+fileNo;
            Log.d("PackageReceivedUrl", url);
            if (CheckInternet.checkInternet(PaymentApproveOrReject.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PackageReceivedResponse", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                    dialog.dismiss();
                                    if (response.contains("[]")) {
                                        // linearPackageDetails.setVisibility(View.GONE);
                                        txtTotalRecivedUSD.setText("Amount Received in USD:0");
                                        txtTotalRecivedINR.setText("Amount Received in INR:0");
                                        //  Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // linearPackageDetails.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            inr= jsonObject1.getString("INR");
                                            usd = jsonObject1.getString("USD");
                                        }
                                        txtTotalRecivedUSD.setText("Amount Received in USD:"+usd);
                                        txtTotalRecivedINR.setText("Amount Received in INR:"+inr);

                                       // getPackageReceivedDetail();


                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(PaymentApproveOrReject.this, "Errorcode-499 PaymentApproveOrReject getPackageTotalDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(PaymentApproveOrReject.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
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
            Toast.makeText(PaymentApproveOrReject.this, "Errorcode-498 PaymentApproveOrReject getPackageTotalDetails " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }

    public void getPackageDetails(String fileID)
    {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=116&FileNo="+fileID;
            Log.d("PackageDetailsUrl", url);
            if (CheckInternet.checkInternet(PaymentApproveOrReject.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PackageDetailsResponse", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                  //  dialog.dismiss();
                                    if (response.contains("[]")) {

                                        //  Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            collegeFee1YearD= jsonObject1.getString("CollegeFee1YearD");
                                            collegeFeeFinal= jsonObject1.getString("CollegeFee1YearD_Final");
                                            hostalFee1YearD = jsonObject1.getString("HostalFee1YearD");
                                            hostalFeeFinal= jsonObject1.getString("HostalFee1YearD_Final");
                                            oneTimeProcessingFeeD = jsonObject1.getString("OneTimeProcessingFeeD");
                                            OTPFeeFinal= jsonObject1.getString("OneTimeProcessingFeeD_Final");
                                            otherFee1D=jsonObject1.getString("Accidental_Insurance");
                                            accidentalFeeFinal= jsonObject1.getString("Accidental_Insurance_Final");
                                            otherFee2D=jsonObject1.getString("MessFeeD");
                                            messFeeFinal= jsonObject1.getString("MessFeeD_Final");
                                            totalFeeYearD = jsonObject1.getString("TotalFeeYearD");
                                            //  collegeFeeFinal= jsonObject1.getString("CollegeFee1YearD");
                                            airFareFeeR = jsonObject1.getString("AirFareFeeR");
                                            AirFareFinal= jsonObject1.getString("AirFareFeeR_Final");
                                            otherFee3D=jsonObject1.getString("IndiaOTC");
                                            IndiaOTCFeeFinal= jsonObject1.getString("IndiaOTC_Final");
                                            totalFeeYearR = jsonObject1.getString("TotalFeeYearR");
                                            // collegeFeeFinal= jsonObject1.getString("CollegeFee1YearD");
                                        }
                                        if(collegeFee1YearD == null || collegeFee1YearD.isEmpty() || collegeFee1YearD.equals("null")||collegeFee1YearD.contains("NA")) {
                                            txtCollegeFeeTotalD.setText("0");
                                        }
                                        else {
                                            txtCollegeFeeTotalD.setText(collegeFee1YearD);
                                        }
                                        if(hostalFee1YearD == null || hostalFee1YearD.isEmpty() || hostalFee1YearD.equals("null")||hostalFee1YearD.contains("NA")) {

                                            txtHostalTotalFeeD.setText("0");
                                        }
                                        else {
                                            txtHostalTotalFeeD.setText(hostalFee1YearD);
                                        }
                                        if(oneTimeProcessingFeeD == null || oneTimeProcessingFeeD.isEmpty() || oneTimeProcessingFeeD.equals("null")||oneTimeProcessingFeeD.contains("NA")) {
                                            txtOTPTotalFeeD.setText("0");
                                        }
                                        else {
                                            txtOTPTotalFeeD.setText(oneTimeProcessingFeeD);
                                        }
                                        if(otherFee1D == null || otherFee1D.isEmpty() || otherFee1D.equals("null")||otherFee1D.contains("NA")) {
                                            txtAccidentalTotal.setText("0");
                                        }
                                        else {
                                            txtAccidentalTotal.setText(otherFee1D);
                                        }
                                        if(otherFee2D == null || otherFee2D.isEmpty() || otherFee2D.equals("null")||otherFee2D.contains("NA")) {
                                            txtMessTotal.setText("0");
                                        }
                                        else {
                                            txtMessTotal.setText(otherFee2D);
                                        }
                                        int totalFeeD=Integer.parseInt(txtCollegeFeeTotalD.getText().toString())+Integer.parseInt(txtHostalTotalFeeD.getText().toString())+
                                                Integer.parseInt(txtOTPTotalFeeD.getText().toString())+Integer.parseInt(txtAccidentalTotal.getText().toString())+Integer.parseInt(txtMessTotal.getText().toString());
                                        txtTotalFeeD.setText(String.valueOf(totalFeeD));
                                        if(airFareFeeR == null || airFareFeeR.isEmpty() || airFareFeeR.equals("null")||airFareFeeR.contains("NA")) {
                                            txtAirFareFeeTotal.setText("0");
                                        }
                                        else {
                                            txtAirFareFeeTotal.setText(airFareFeeR);
                                        }
                                        if(otherFee3D == null || otherFee3D.isEmpty() || otherFee3D.equals("null")||otherFee3D.contains("NA")) {
                                            txtIndiaOTCTotal.setText("0");
                                        }
                                        else {
                                            txtIndiaOTCTotal.setText(otherFee3D);
                                        }
                                        int totalFeeR=Integer.parseInt(txtAirFareFeeTotal.getText().toString())+Integer.parseInt(txtIndiaOTCTotal.getText().toString());

                                        txtTotalFeeTotalR.setText(String.valueOf(totalFeeR));
                                        if(collegeFeeFinal == null || collegeFeeFinal.isEmpty() || collegeFeeFinal.equals("null")||collegeFeeFinal.contains("NA")) {
                                            txtCollegeFeeFinal.setText("0");
                                        }
                                        else {
                                            txtCollegeFeeFinal.setText(collegeFeeFinal);
                                        }
                                        if(hostalFeeFinal == null || hostalFeeFinal.isEmpty() || hostalFeeFinal.equals("null")||hostalFeeFinal.contains("NA")) {
                                            txtHostalFinalFeeD.setText("0");
                                        }
                                        else {
                                            txtHostalFinalFeeD.setText(hostalFeeFinal);
                                        }
                                        if(OTPFeeFinal == null || OTPFeeFinal.isEmpty() || OTPFeeFinal.equals("null")||OTPFeeFinal.contains("NA")) {
                                            txtOTPFeeFinalD.setText("0");
                                        }
                                        else {
                                            txtOTPFeeFinalD.setText(OTPFeeFinal);
                                        }
                                        if(accidentalFeeFinal == null || accidentalFeeFinal.isEmpty() || accidentalFeeFinal.equals("null")||accidentalFeeFinal.contains("NA")) {
                                            txtAccidentalFinal.setText("0");
                                        }
                                        else {
                                            txtAccidentalFinal.setText(accidentalFeeFinal);
                                        }
                                        if(messFeeFinal == null || messFeeFinal.isEmpty() || messFeeFinal.equals("null")||messFeeFinal.contains("NA")) {
                                            txtMessFinal.setText("0");
                                        }
                                        else {
                                            txtMessFinal.setText(messFeeFinal);
                                        }

                                        int totalFinalD1=Integer.parseInt(txtCollegeFeeFinal.getText().toString())+Integer.parseInt(txtHostalFinalFeeD.getText().toString())+
                                                Integer.parseInt(txtOTPFeeFinalD.getText().toString())
                                                +Integer.parseInt(txtAccidentalFinal.getText().toString())+Integer.parseInt(txtMessFinal.getText().toString());

                                        txtFeeFinalD.setText(String.valueOf(totalFinalD1));
                                        if(AirFareFinal == null || AirFareFinal.isEmpty() || AirFareFinal.equals("null")||AirFareFinal.contains("NA")) {
                                            txtAirFareFeeFinal.setText("0");
                                        }
                                        else
                                        {
                                            txtAirFareFeeFinal.setText(AirFareFinal);
                                        }
                                        if(IndiaOTCFeeFinal == null || IndiaOTCFeeFinal.isEmpty() || IndiaOTCFeeFinal.equals("null")||IndiaOTCFeeFinal.contains("NA")) {
                                            txtIndiaOTCFinal.setText("0");
                                        }else
                                        {
                                            txtIndiaOTCFinal.setText(IndiaOTCFeeFinal);
                                        }
                                        int totalFinalR=Integer.parseInt(txtAirFareFeeFinal.getText().toString())+Integer.parseInt(txtIndiaOTCFinal.getText().toString());
                                        txtTotalFeeFinalR.setText(String.valueOf(totalFinalR));
                                        getPackageReceivedDetail();

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(PaymentApproveOrReject.this, "Errorcode-501 PaymentApproveOrReject getPackageDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(PaymentApproveOrReject.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
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
            Toast.makeText(PaymentApproveOrReject.this, "Errorcode-500 PaymentApproveOrReject getPackageDetails " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }
    public void getPackageReceivedDetail()
    {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=122&FileNo="+fileNo;
            Log.d("PckgReceivedDetailsUrl", url);
            if (CheckInternet.checkInternet(PaymentApproveOrReject.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PckgReceivedDetailsRes", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                   // dialog.dismiss();
                                    getPackageTotalDetails();
                                    if (response.contains("[]")) {
                                        txtCollegeFeeReceivedD.setText("0");
                                        txtHostalReceivedFeeD.setText("0");
                                        txtOTPReceivedFeeD.setText("0");
                                        txtAccidentalReceived.setText("0");
                                        txtMessReceived.setText("0");
                                        txtAirFareFeeReceived.setText("0");
                                        txtIndiaOTCReceived.setText("0");

                                        airfareTotal=Integer.parseInt(txtAirFareFeeTotal.getText().toString());
                                        airfareReceived=Integer.parseInt(txtAirFareFeeReceived.getText().toString());
                                        airfareDues=airfareTotal-airfareReceived;
                                        // txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal=Integer.parseInt(txtIndiaOTCTotal.getText().toString());
                                        indiaOTCRecceived=Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues=indiaOTCTotal-indiaOTCRecceived;
                                        // txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                        clgfeetotal=Integer.parseInt(txtCollegeFeeTotalD.getText().toString());
                                        clgfeeReceived=Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                        clgfeeDue=clgfeetotal-clgfeeReceived;
                                        //txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal=Integer.parseInt(txtHostalTotalFeeD.getText().toString());
                                        hostalFeeReceived=Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue=hostalFeeTotal-hostalFeeReceived;
                                        // txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal=Integer.parseInt(txtOTPTotalFeeD.getText().toString());
                                        OTPReceived=Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue=OTPTotal-OTPReceived;
                                        //  txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal=Integer.parseInt(txtAccidentalTotal.getText().toString());
                                        accidentalReceived=Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue=accidentalTotal-accidentalReceived;
                                        //   txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal=Integer.parseInt(txtMessTotal.getText().toString());
                                        messReceived=Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue=messTotal-messReceived;
                                        //   txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD=clgfeeReceived+hostalFeeReceived+OTPReceived+accidentalReceived+messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD=clgfeeDue+hostalFeeDue+OTPDue+accidentalDue+messDue;
                                        //  txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR=airfareReceived+indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR=airfareDues+indiaOTCDues;
                                        //   txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));
                                        // linearPackageDetails.setVisibility(View.GONE);
                                        // Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //  linearPackageDetails.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            collegeFee1YearD1= jsonObject1.getString("CollegeFee1YearD");
                                            hostalFee1YearD1 = jsonObject1.getString("HostalFee1YearD");
                                            oneTimeProcessingFeeD1 = jsonObject1.getString("OneTimeProcessingFeeD");
                                            Accidental_Insurance = jsonObject1.getString("Accidental_Insurance");
                                            MessFeeD=jsonObject1.getString("MessFeeD");
                                            IndiaOTC = jsonObject1.getString("IndiaOTC");
                                            AirFareFee=jsonObject1.getString("AirFareFeeR");
                                        }
                                        if(collegeFee1YearD1 == null || collegeFee1YearD1.isEmpty() || collegeFee1YearD1.equals("null")||collegeFee1YearD1.contains("NA")) {
                                            txtCollegeFeeReceivedD.setText("0");
                                        }
                                        else {
                                            txtCollegeFeeReceivedD.setText(collegeFee1YearD1);
                                        }
                                        if(hostalFee1YearD1 == null || hostalFee1YearD1.isEmpty() || hostalFee1YearD1.equals("null")||hostalFee1YearD1.contains("NA")) {
                                            txtHostalReceivedFeeD.setText("0");
                                        }else
                                        {
                                            txtHostalReceivedFeeD.setText(hostalFee1YearD1);
                                        }

                                        if(oneTimeProcessingFeeD1 == null || oneTimeProcessingFeeD1.isEmpty() || oneTimeProcessingFeeD1.equals("null")||oneTimeProcessingFeeD1.contains("NA")) {
                                            txtOTPReceivedFeeD.setText("0");
                                        }
                                        else {
                                            txtOTPReceivedFeeD.setText(oneTimeProcessingFeeD1);
                                        }
                                        if(Accidental_Insurance == null || Accidental_Insurance.isEmpty() || Accidental_Insurance.equals("null")||Accidental_Insurance.contains("NA")) {
                                            txtAccidentalReceived.setText("0");
                                        }else {
                                            txtAccidentalReceived.setText(Accidental_Insurance);
                                        }
                                        if(MessFeeD == null || MessFeeD.isEmpty() || MessFeeD.equals("null")||MessFeeD.contains("NA"))
                                        {
                                            txtMessReceived.setText("0");
                                        }
                                        else {
                                            txtMessReceived.setText(MessFeeD);
                                        }
                                        if(AirFareFee == null || AirFareFee.isEmpty() || AirFareFee.equals("null")||AirFareFee.contains("NA")) {
                                            txtAirFareFeeReceived.setText("0");
                                        }
                                        else {
                                            txtAirFareFeeReceived.setText(AirFareFee);
                                        }
                                        if(IndiaOTC == null || IndiaOTC.isEmpty() || IndiaOTC.equals("null")||IndiaOTC.contains("NA"))
                                        {
                                            txtIndiaOTCReceived.setText("0");
                                        }
                                        else {
                                            txtIndiaOTCReceived.setText(IndiaOTC);
                                        }

                                        airfareTotal=Integer.parseInt(txtAirFareFeeFinal.getText().toString());
                                        airfareReceived=Integer.parseInt(txtAirFareFeeReceived.getText().toString());
                                        airfareDues=airfareTotal-airfareReceived;
                                        //txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal=Integer.parseInt(txtIndiaOTCFinal.getText().toString());
                                        indiaOTCRecceived=Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues=indiaOTCTotal-indiaOTCRecceived;
                                        // txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                        clgfeetotal=Integer.parseInt(txtCollegeFeeFinal.getText().toString());
                                        clgfeeReceived=Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                        clgfeeDue=clgfeetotal-clgfeeReceived;
                                        // txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal=Integer.parseInt(txtHostalFinalFeeD.getText().toString());
                                        hostalFeeReceived=Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue=hostalFeeTotal-hostalFeeReceived;
                                        //  txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal=Integer.parseInt(txtOTPFeeFinalD.getText().toString());
                                        OTPReceived=Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue=OTPTotal-OTPReceived;
                                        //  txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal=Integer.parseInt(txtAccidentalFinal.getText().toString());
                                        accidentalReceived=Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue=accidentalTotal-accidentalReceived;
                                        // txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal=Integer.parseInt(txtMessFinal.getText().toString());
                                        messReceived=Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue=messTotal-messReceived;
                                        //   txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD=clgfeeReceived+hostalFeeReceived+OTPReceived+accidentalReceived+messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD=clgfeeDue+hostalFeeDue+OTPDue+accidentalDue+messDue;
                                        //  txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR=airfareReceived+indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR=airfareDues+indiaOTCDues;
                                        //  txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));

                                      /*  totalfeeReceived=Integer.parseInt(txtTotalFeeD.getText().toString());
                                        totalReceived=Integer.parseInt(txtReceivedFeeD.getText().toString());
                                        totalDue=totalfeeReceived-totalReceived;
                                        txtDueFeeD.setText(String.valueOf(totalDue));*/


                                         /* txtTotalFeeD.setText(totalFeeYearD);
                                            txtAirFareFeeR.setText("AirFareFeeR : "+airFareFeeR);
                                               txtIndiaOTC.setText(otherFee3Head+" : "+otherFee3D);
                                          txtTotalFeeYearR.setText("TotalFeeYearR :"+totalFeeYearR);*/
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(PaymentApproveOrReject.this, "Errorcode-503 PaymentApproveOrReject PackageReceivedDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(PaymentApproveOrReject.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
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
            Toast.makeText(PaymentApproveOrReject.this, "Errorcode-502 PaymentApproveOrReject getPackageReceivedDetails " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }
    public void approveOrRejectPayment(final String status1)
    {
        try{
            // String url="";
            String url = clienturl + "?clientid=" + clientid + "&caseid=132&Remarks="+remarks+"&PaymentID="+paymentID+"&Status="+status1+"&CollegeFee1YearD="+CollegeFee1YearD+"&HostalFee1YearD="+HostalFee1YearD+
                    "&OneTimeProcessingFeeD="+OneTimeProcessingFeeD+"&Accidental_Insurance="+Accidental_Insurance+"&MessFeeD="+MessFeeD+"&IndiaOTC="+IndiaOTC+"&AirFareFeeR="+AirFareFeeR;
            Log.d("PaymentEntryUrl", url);
            if (CheckInternet.checkInternet(PaymentApproveOrReject.this)) {
                if(CheckServer.isServerReachable(PaymentApproveOrReject.this)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("PaymentEntryResponse", response);
                                        dialog.dismiss();
                                        if(response.contains("Data updated successfully"))
                                        {
                                            if(status1.contains("1")) {
                                                Intent intent=new Intent(PaymentApproveOrReject.this,PaymentApprove.class);
                                                startActivity(intent);
                                                Toast.makeText(PaymentApproveOrReject.this, "Payment approved", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                Intent intent=new Intent(PaymentApproveOrReject.this,PaymentApprove.class);
                                                startActivity(intent);
                                                Toast.makeText(PaymentApproveOrReject.this,"Payment rejected",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            if(status1.contains("1")) {
                                                Toast.makeText(PaymentApproveOrReject.this, "Payment approve failed", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                Toast.makeText(PaymentApproveOrReject.this,"Payment rejection failed",Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(PaymentApproveOrReject.this, "Errorcode-505 PaymentApproveOrReject approveOrRejectPaymentResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(PaymentApproveOrReject.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApproveOrReject.this);
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
            Toast.makeText(PaymentApproveOrReject.this, "Errorcode-504 PaymentApproveOrReject approveOrRejectPayment " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
