package com.bizcall.wayto.mentebit13;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SummaryDetails extends AppCompatActivity {

    TextView txtSummTitle;
    ImageView imgRight;
    String strDocNM, strDoccount, strName;
    TextView txtPersonalTitle, txtEducationTitle, txtDocumentTitle, txtHistory, txtAccount;
    TextView txtName, txtMobile, txtPassportNo, txtPanNo, txtFileID;
    TextView txtCollegeFeeTotalD, txtCollegeFeeFinal, txtCollegeFeeReceivedD, txtCollegeFeeDueD, txtHostalTotalFeeD, txtHostalFinalFeeD, txtHostalReceivedFeeD, txtHostalDueFeeD, txtOTPTotalFeeD,
            txtOTPFeeFinalD, txtOTPReceivedFeeD, txtOTPDueFeeD, txtAccidentalTotal, txtAccidentalFinal, txtAccidentalReceived, txtAccidentalDue, txtMessTotal, txtMessFinal, txtMessReceived, txtMessDue, txtTotalFeeD, txtFeeFinalD, txtReceivedFeeD, txtDueFeeD;
    TextView txtAirFareFeeTotal, txtAirFareFeeFinal, txtAirFareFeeReceived, txtAirFareFeeDues, txtIndiaOTCTotal, txtIndiaOTCFinal, txtIndiaOTCReceived, txtIndiaOTCDues, txtTotalFeeTotalR, txtTotalFeeFinalR, txtTotalFeeReceivedR, txtTotalFeeDuesR;
    TextView txtCollegeFee1YearD1, txtHostalFee1YearD1, txtOneTimeProcessingFeeD1, txtAccidentalInsurance1, txtMess1, txtTotalFeeYearD1, txtDiscountDetails;
    TextView txtNoResult, txtCollegeName, txtCourse, txtNoCollege, txtClgInfoTitle;
    TextView txtSubmit, txtTotalRecivedUSD, txtTotalRecivedINR;
    LinearLayout linearReport, linearAccountInfo;
    private LinearLayout linearPersonalInfo, linearCollegeInfo, linearPackageDetails, linearSearchBy, linearClgName, linearSpinnerClg, linearAmountDetails;
    LinearLayout linearPaymentHistory;
    ArrayList<DataPaymentHistory> arrayListPaymentHistory;
    String inr, usd, paymentid, amountUSD, amountINR, date1, approved, clgid, clgname, coursename;

    int clgfeetotal, clgfeeReceived, clgfeeDue, hostalFeeTotal, hostalFeeReceived, hostalFeeDue, OTPReceived, OTPTotal, OTPDue, accidentalTotal,
            accidentalReceived, accidentalDue, messTotal, messReceived, messDue, totalReceivedD, totalDueD, airfareTotal, airfareReceived, airfareDues,
            indiaOTCTotal, indiaOTCRecceived, indiaOTCDues, totalReceivedR, totalDuesR;
    String collegeFee1YearD1, collegeFeeFinal, hostalFee1YearD1, hostalFeeFinal, OTPFeeFinal, accidentalFeeFinal, messFeeFinal, IndiaOTCFeeFinal, AirFareFinal, totalFeeFinalD, totalFeeFinalR, oneTimeProcessingFeeD1, Accidental_Insurance, MessFeeD, IndiaOTC, AirFareFee;
    ArrayList<String> arrayListClgId, arrayListCollege;

    private ArrayList<DataMasterEntry> arrayListMasterEntry;
    ArrayList<DataSummary> personalinfoArrayList;
    ArrayList<DataSummary> educationsArrayList;
    ArrayList<DataSummary> documentsArrayList;
    AdapterInfoSummary adapterPersonalInfo, adapterEducation, adapterDocuments;
    DataSummary detailsSummary;

    RecyclerView recyclerPaymentHistory;
    AdapterPaymentHistory adapterPaymentHistory;
    int flag = 0;
    Thread thread;
    long timeout;

    SharedPreferences sp;
    ProgressDialog progressBar, dialog;
    RequestQueue requestQueue;
    String clienturl, clientid, counselorid, sharedSrno, sharedMobile, sharedAddress, sharedEmail, sharedCity, sharedState, sharedPincode, cname;
    SharedPreferences.Editor editor;
    TextView txtPersonal, txtEducational, txtUplaodDoc, txtFileNo, txtMobileNo, txtSms, txtCollegeAttorny;
    ImageView imgBack;
    RecyclerView recyclerPersonalInfo, recyclerEducation, recyclerDocuments;
    Vibrator vibrator;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    EditText edtName1, edtEmail1, edtPhone1, edtAddress1, edtCity, edtPincode, edtAmount, edtOther;
    TextView txtShortLink, txtFormNo, txtSendPaymentTitle, txtSendLink, txtSend,txtReset;
    Button btnWhatsapp, btnSMS, btnWeb, btnEmail;
    LinearLayout linearSendPayment, linearShortLink, linearSMS;
    Spinner spinnerPaymentFor, spinnerState;
    ArrayList<String> arrayListCounsultancy;
    String sendPaymentName, sendPaymentPhone, sendPaymentAddress, sendPaymentEmail, sendPaymentFormNo, sendPaymentAmount, sendPaymentFor, sendPaymentFor1, sendPaymentCity, sendPaymentState, sendPaymentPincode;
    int temp = 0;

    String msg, orderid1, fileID, fname, lname, mobile, passport, panno, collegeFee1YearD, hostalFee1YearD, oneTimeProcessingFeeD, otherFee1Head, otherFee1D, otherFee2Head, otherFee2D, otherFee3Head, otherFee3D, mess, totalFeeYearD, airFareFeeR, indiaOTC, totalFeeYearR;
    public static String receiptl, paymentl, invoicel;
    ///////////////////////////////ReceivedPaymentDetails////////////////////////////
    AdapterReceivedPaymentDetails adapterReceivedPaymentDetails;
    ArrayList<DataReceivedPaymentDetails> arrayListReceivedPayment;
    DataReceivedPaymentDetails dataReceivedPaymentDetails;
    RecyclerView recyclerReceivedPayment;
    String paymentDate, orderamount, orderfor, orderstatus, orderid, paymethod, invoiceId, trackingid, bankrefno, billname, billphone,
            billemail, address, city, state, pin, datepay, website, paymentthrough, formno;
    HorizontalScrollView horizontalReceivedPayment;
    TextView txtPaymentNotReceived, txtReceivePaymentTitle;
    public static WebView webViewInvoice, webViewReceipt;
    String actname="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_summary_details);
            //to initialize controls and variable
            initialize();

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
            txtFileNo.setText(sharedSrno + ". " + strName);
            txtMobileNo.setText(sharedMobile);
            personalinfoArrayList = new ArrayList<>();
            educationsArrayList = new ArrayList<>();
            documentsArrayList = new ArrayList<>();

            recyclerPersonalInfo = findViewById(R.id.recycler_personalinfo);
            recyclerEducation = findViewById(R.id.recycler_education);
            recyclerDocuments = findViewById(R.id.recycler_documents);

            adapterPersonalInfo = new AdapterInfoSummary(personalinfoArrayList, SummaryDetails.this);
            adapterEducation = new AdapterInfoSummary(educationsArrayList, SummaryDetails.this);
            adapterDocuments = new AdapterInfoSummary(documentsArrayList, SummaryDetails.this);

            edtName1.setText(strName);
            edtEmail1.setText(sharedEmail);
            edtAddress1.setText(sharedAddress);
            edtPhone1.setText(sharedMobile);
            txtFormNo.setText(sharedSrno);
            edtCity.setText(sharedCity);
            edtPincode.setText(sharedPincode);

            arrayListCounsultancy = new ArrayList<>();//to store list of payment for option

            //arrayListCourse.add("Select Course");
            arrayListCounsultancy.add("5000 @Consultancy charges");
            arrayListCounsultancy.add("10000 @Consultancy charges");
            arrayListCounsultancy.add("25000 @Consultancy charges");
            arrayListCounsultancy.add("Others");

            txtReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(linearSendPayment.getVisibility()==View.GONE) {
                        linearSendPayment.setVisibility(View.VISIBLE);
                        linearShortLink.setVisibility(View.GONE);
                        linearSMS.setVisibility(View.GONE);
                    }
                    else
                    { linearShortLink.setVisibility(View.VISIBLE);
                        linearSendPayment.setVisibility(View.GONE);
                        linearSMS.setVisibility(View.GONE);
                    }
                }
            });

            txtReceivePaymentTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(linearSMS.getVisibility()==View.VISIBLE)
                    {
                        linearSMS.setVisibility(View.GONE);
                    }
                    if(linearShortLink.getVisibility()==View.VISIBLE)
                    {
                        linearShortLink.setVisibility(View.GONE);
                    }
                    dialog = ProgressDialog.show(SummaryDetails.this, "", "Loading received payment details", true);
                    newThreadInitilization(dialog);
                    //to get received payment details
                    getReceivedPaymentDetails(sharedSrno);
                }
            });

            ArrayAdapter<String> arrayAdapterCourse = new ArrayAdapter<>(SummaryDetails.this, R.layout.spinner_item1, arrayListCounsultancy);

            // Apply the adapter to the spinner
            spinnerPaymentFor.setAdapter(arrayAdapterCourse);
            spinnerPaymentFor.setSelection(0);
            String item = spinnerPaymentFor.getSelectedItem().toString();
            if (item.contains("@")) {
                sendPaymentFor = item.substring(item.indexOf("@"));
                edtAmount.setText(item.substring(0, item.indexOf("@")));
                Log.d("SEndPayment", sendPaymentFor);
            }
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(SummaryDetails.this,
                    R.array.States, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinnerState.setAdapter(adapter1);
            for (int i = 0; i < spinnerState.getCount(); i++) {
                if (sharedState.contains(spinnerState.getItemAtPosition(i).toString())) {

                    spinnerState.setSelection(i);
                }
            }
            btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnMailClicked();
                }
            });

            btnWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnWhatsappClicked();
                }
            });
            btnSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnSMSClicked();
                }
            });
            btnWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnWebClicked();
                }
            });

/*
            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //  spinnerState.setSelection(0);
                }
            });*/
            spinnerPaymentFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = spinnerPaymentFor.getSelectedItem().toString();
                    if (item.contains("@")) {
                        sendPaymentFor = item.substring(item.indexOf("@"));
                        edtAmount.setText(item.substring(0, item.indexOf("@")));
                    }
                    if (item.contains("Others")) {
                        edtOther.setVisibility(View.VISIBLE);
                        sendPaymentFor = edtOther.getText().toString();
                        edtAmount.setText("");
                    } else {
                        edtOther.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinnerPaymentFor.setSelection(0);
                }
            });

            LinearLayoutManager lm = new LinearLayoutManager(SummaryDetails.this);
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerPersonalInfo.addItemDecoration(new DividerItemDecoration(SummaryDetails.this, DividerItemDecoration.VERTICAL));
            recyclerPersonalInfo.setLayoutManager(lm);
            recyclerPersonalInfo.setAdapter(adapterPersonalInfo);

            LinearLayoutManager lm1 = new LinearLayoutManager(SummaryDetails.this);
            lm1.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerEducation.addItemDecoration(new DividerItemDecoration(SummaryDetails.this, DividerItemDecoration.VERTICAL));
            recyclerEducation.setLayoutManager(lm1);
            recyclerEducation.setAdapter(adapterEducation);

            LinearLayoutManager lm2 = new LinearLayoutManager(SummaryDetails.this);
            lm2.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerDocuments.addItemDecoration(new DividerItemDecoration(SummaryDetails.this, DividerItemDecoration.VERTICAL));
            recyclerDocuments.setLayoutManager(lm2);
            recyclerDocuments.setAdapter(adapterDocuments);
            txtCollegeAttorny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(SummaryDetails.this, CollegeAttornyActivity.class);
                    intent1.putExtra("Activity", "Summary");
                    startActivity(intent1);
                }
            });
            tabLayout.addTab(tabLayout.newTab().setText("Remarks"));
            tabLayout.addTab(tabLayout.newTab().setText("Status"));
            tabLayout.addTab(tabLayout.newTab().setText("Call"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            txtSendPaymentTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linearSendPayment.getVisibility() == View.GONE) {
                        linearSendPayment.setVisibility(View.VISIBLE);
                    } else {
                        linearSendPayment.setVisibility(View.GONE);
                    }
                }
            });
            txtSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temp = 0;
                    sendPaymentName = edtName1.getText().toString();
                    sendPaymentEmail = edtEmail1.getText().toString();
                    sendPaymentAddress = edtAddress1.getText().toString();
                    sendPaymentPhone = edtPhone1.getText().toString().trim();
                    sendPaymentFormNo = txtFormNo.getText().toString().trim();
                    sendPaymentAmount = edtAmount.getText().toString().trim();
                    sendPaymentCity = edtCity.getText().toString();
                    sendPaymentPincode = edtPincode.getText().toString().trim();
                    sendPaymentFor1 = spinnerPaymentFor.getSelectedItem().toString();
                    sendPaymentState = spinnerState.getSelectedItem().toString();
                    sendPaymentPhone = sendPaymentPhone.substring(sendPaymentPhone.length() - 10);
                    if (sendPaymentFor1.contains("@")) {
                        sendPaymentFor = sendPaymentFor1.substring(sendPaymentFor1.indexOf("@"));
                        String amt=sendPaymentFor1.substring(0, sendPaymentFor1.indexOf("@"));
                        Log.d("AmountSelected",amt+" "+sendPaymentFor);
                        edtAmount.setText(amt);
                    } else if (sendPaymentFor1.contains("Others")) {
                        sendPaymentFor = edtOther.getText().toString();

                        if (sendPaymentFor.length() == 0) {
                            edtOther.setError("Enter payment for");
                            temp = 1;
                        } else {
                            sendPaymentFor = edtOther.getText().toString();
                        }
                    }

                    if (sendPaymentName.length() == 0) {
                        edtName1.setError("Invalid name");
                        temp = 1;
                    }
                    if (sendPaymentEmail.length() == 0) {
                        edtEmail1.setError("Invalid mail");
                        temp = 1;
                    }
                    if (sendPaymentPhone.length() == 0 || sendPaymentPhone.length() < 10 || sendPaymentPhone.length() > 10) {
                        edtPhone1.setError("Invalid phone");
                        temp = 1;
                    }
                    if (sendPaymentAddress.length() == 0) {
                        edtAddress1.setError("Invalid address");
                        temp = 1;
                    }
                    if (sendPaymentAmount.length() == 0) {
                        edtAmount.setError("Invalid amount");
                        temp = 1;
                    }

                    if (temp == 0) {
                        dialog = ProgressDialog.show(SummaryDetails.this, "", "Sending information", true);
                        newThreadInitilization(dialog);
                        //to get paymentlink by sending parameters
                        getShortLink(sendPaymentFormNo, sendPaymentName, sendPaymentPhone, sendPaymentEmail, sendPaymentAddress, sharedCity, sharedState, sharedPincode, sendPaymentFor, sendPaymentAmount, counselorid);
                    }
                }
            });
            txtSendLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                    alertDialogBuilder.setMessage("Are you sure you want to send payment link of amount" + sendPaymentAmount + "/- for " + sendPaymentFor + " to " + sendPaymentName + " with file no:" + sendPaymentFormNo)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    linearSMS.setVisibility(View.VISIBLE);
                                    //to insert all payment details
                                    insertPaymentLinkDetails(orderid1, "NA", "NA", sendPaymentName, sendPaymentPhone, sendPaymentEmail, sendPaymentAddress, sendPaymentCity, sendPaymentState, sendPaymentPincode, "NA", sendPaymentAmount, "NA", "Awaiting", "NA", "NA", sendPaymentFormNo, counselorid, sendPaymentFor, sendPaymentAmount, txtShortLink.getText().toString(), invoicel, receiptl);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                }
            });

            txtAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SummaryDetails.this, ClientAccount.class);
                    intent.putExtra("FileID", sharedSrno);
                    intent.putExtra("ActName", "SummaryDetails");
                    startActivity(intent);
                }
            });

            txtHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  vibrator.vibrate(100);
                    if (linearReport.getVisibility() == View.GONE) {
                        linearReport.setVisibility(View.VISIBLE);
                    } else {
                        linearReport.setVisibility(View.GONE);
                    }
                }
            });

            txtPersonalTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerPersonalInfo.getVisibility() == View.GONE) {
                        recyclerPersonalInfo.setVisibility(View.VISIBLE);
                        recyclerEducation.setVisibility(View.VISIBLE);
                        recyclerDocuments.setVisibility(View.VISIBLE);
                    } else {
                        recyclerPersonalInfo.setVisibility(View.GONE);
                        recyclerEducation.setVisibility(View.GONE);
                        recyclerDocuments.setVisibility(View.GONE);
                    }
                }
            });

            txtSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor = sp.edit();
                    editor.putString("CName", strName);
                    editor.putString("SelectedMobile", sharedMobile);
                    editor.putString("SelectedSrNo", sharedSrno);
                    editor.putString("SelectedEmail", sharedEmail);
                    editor.commit();
                    //to send message via sms,whatsapp,web
                    Intent intent1 = new Intent(SummaryDetails.this, MessageActivity.class);
                    intent1.putExtra("Activity", "Summary");
                    startActivity(intent1);
                }
            });
            if (CheckInternetSpeed.checkInternet(SummaryDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(SummaryDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        }).show();
            } else {
                String strDocumentList = clienturl + "?clientid=" + clientid + "&caseid=D13" + "&SrNo=" + sharedSrno + "&MobileNo=" + sharedMobile;
                Log.d("doclisturl", strDocumentList);
                progressBar = ProgressDialog.show(SummaryDetails.this, "", "Loading Summary", true);
                newThreadInitilization(progressBar);
                //get summary of uploaded documents,educational info and personal info
                loadSummaryData(strDocumentList);
            }
            txtPersonal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SummaryDetails.this, PersonalInfoActivity.class);
                    intent.putExtra("FileNo", sharedSrno);
                    intent.putExtra("MobileNo", sharedMobile);
                    startActivity(intent);
                }
            });
            txtEducational.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SummaryDetails.this, EducationalDetails.class);
                    intent.putExtra("FileNo", sharedSrno);
                    intent.putExtra("MobileNo", sharedMobile);
                    startActivity(intent);
                }
            });
            txtUplaodDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent uploaddocs = new Intent(SummaryDetails.this, ActivityUploadDocs.class);
                    uploaddocs.putExtra("Activity", "ClientDocs");
                    uploaddocs.putExtra("FileNo", sharedSrno);
                    uploaddocs.putExtra("MobileNo", sharedMobile);
                   // uploaddocs.putExtra("Email",strMail);
                    uploaddocs.putExtra("Name",strName);
                    startActivity(uploaddocs);
                }
            });
        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-311 SummaryDetails onCreate " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//onCreate
    public void initialize()
    {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        webViewInvoice = findViewById(R.id.webViewInvoice);
        webViewReceipt = findViewById(R.id.webViewReceipt);
        txtPersonal = findViewById(R.id.txt_personal);
        txtEducational = findViewById(R.id.txt_education);
        txtUplaodDoc = findViewById(R.id.txt_documents);
        imgBack = findViewById(R.id.img_back);
        txtFileNo = findViewById(R.id.txtFileNo);
        txtMobileNo = findViewById(R.id.txtMobileNo);
        txtSms = findViewById(R.id.txt_sms);
        txtCollegeAttorny = findViewById(R.id.txt_college);

        txtPersonalTitle = findViewById(R.id.txtPersonalTitle);
        txtEducationTitle = findViewById(R.id.txtEducationTitle);
        txtDocumentTitle = findViewById(R.id.txtDocumentTitle);
        txtHistory = findViewById(R.id.txtReportTitle);
        linearReport = findViewById(R.id.linearReport);
        txtAccount = findViewById(R.id.txtAccountTitle);
        linearAccountInfo = findViewById(R.id.linearPersonalInfo);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        txtDiscountDetails = findViewById(R.id.txtDiscountOffer);
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

        recyclerPaymentHistory = findViewById(R.id.recyclerPaymentHistory);

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

        txtFileID = findViewById(R.id.txtFileID);
        txtSubmit = findViewById(R.id.txtSubmit);
        linearPersonalInfo = findViewById(R.id.linearPersonalInfo);
        linearCollegeInfo = findViewById(R.id.linearCollegeInfo);
        linearPackageDetails = findViewById(R.id.linearPackageDetails);
        linearAmountDetails = findViewById(R.id.linearAmountDetails);
        linearClgName = findViewById(R.id.linearClgName);
        linearSpinnerClg = findViewById(R.id.linearSpinnerClgList);
        linearPaymentHistory = findViewById(R.id.linearPaymentHistory);

        txtName = findViewById(R.id.txtCandidateName);
        txtMobile = findViewById(R.id.txtMobile1);
        txtPassportNo = findViewById(R.id.txtPassportNo);
        txtPanNo = findViewById(R.id.txtPanNo);
        txtCollegeName = findViewById(R.id.txtCollegeName);
        txtCourse = findViewById(R.id.txtCourse);
        txtNoCollege = findViewById(R.id.txtNoCollege);
        txtClgInfoTitle = findViewById(R.id.txtClgInfoTitle);
        edtName1 = findViewById(R.id.edtName);
        edtEmail1 = findViewById(R.id.edtEmail);
        edtPhone1 = findViewById(R.id.edtPhone);
        edtAddress1 = findViewById(R.id.edtAddress);
        edtAmount = findViewById(R.id.edtAmount);
        txtFormNo = findViewById(R.id.txtFormNo);
        edtCity = findViewById(R.id.edtCity);
        edtPincode = findViewById(R.id.edtPincode);
        spinnerState = findViewById(R.id.spinnerState);
        spinnerPaymentFor = findViewById(R.id.spinnerPaymentFor);
        linearSendPayment = findViewById(R.id.linearSendPayment);
        edtOther = findViewById(R.id.edtOther);
        txtSendPaymentTitle = findViewById(R.id.txtSendPaymentTitle);
        txtSend = findViewById(R.id.txtSend);
        txtSendLink = findViewById(R.id.txtSendLink);
        txtReset=findViewById(R.id.txtReset);
        linearShortLink = findViewById(R.id.linearShortLink);
        linearSMS = findViewById(R.id.linearSMS);
        txtShortLink = findViewById(R.id.txtShortLink);
        btnWhatsapp = findViewById(R.id.btnWhatsapp);
        btnSMS = findViewById(R.id.btnSMS);
        btnWeb = findViewById(R.id.btnWeb);
        btnEmail = findViewById(R.id.btnEmail);

        horizontalReceivedPayment = findViewById(R.id.horizontalReceivedPayment);
        txtPaymentNotReceived = findViewById(R.id.txtPaymentNotReceived);
        txtReceivePaymentTitle = findViewById(R.id.txtReceivePaymentTitle);

      //  txtSummTitle = findViewById(R.id.txt_summ_title);
       // imgRight = findViewById(R.id.img_tickmark);

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        strName = sp.getString("FullName", null);
        clienturl = sp.getString("ClientUrl", null);
        clientid = sp.getString("ClientId", null);
        sharedSrno = sp.getString("FileNo", null);
        counselorid = sp.getString("Id", "");
        counselorid = counselorid.replaceAll(" ", "");
        sharedMobile = sp.getString("MobileNo", null);
        sharedAddress = sp.getString("ClientAddress", "");
        sharedEmail = sp.getString("ClientEmail", "");
        sharedCity = sp.getString("City", "");
        sharedState = sp.getString("State", "");
        sharedPincode = sp.getString("Pincode", "");
        timeout = sp.getLong("TimeOut", 0);
    }//initialize

    public void insertPaymentLinkDetails(String orderid, String trackingid, String bankrefno, String billname,
                                         String billphone, String billemail, String cAddress, String cCity,
                                         String cState, String cPin, String paymethod, String orderamount,
                                         String datepay, String orderstatus, String cWebsite, String cPaymentthrough,
                                         String formNo, String counselorID, String orderFor, String orderReqAmount,
                                         String srtpayLink, String srtInvoiceLink, String srtReceiptLink) {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=163&Orderid=" + orderid + "&Trackingid=" + trackingid + "&Bankrefno=" + bankrefno + "&Billname=" + billname +
                    "&Billphone=" + billphone + "&Billemail=" + billemail + "&cAddress=" + cAddress + "&cCity=" + cCity +
                    "&cState=" + cState + "&cPin=" + cPin + "&Paymethod=" + paymethod +
                    "&Orderamount=" + orderamount + "&Datepay=" + datepay + "&Orderstatus=" + orderstatus + "&cWebsite=" + cWebsite +
                    "&cPaymentthrough=" + cPaymentthrough + "&FormNo=" + formNo + "&CounselorID=" + counselorID + "&OrderFor=" + orderFor +
                    "&OrderReqAmount=" + orderReqAmount + "&SrtpayLink=" + srtpayLink + "&SrtInvoiceLink=" + srtInvoiceLink + "&SrtReceiptLink=" + srtReceiptLink;

            Log.d("PaymentLinkDetailsUrl", url);
            // arrayListLoginDetails = new ArrayList<>();
            if (CheckInternet.checkInternet(SummaryDetails.this)) {
                if (CheckServer.isServerReachable(SummaryDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // dialog.dismiss();

                                    Log.d("PaymentLinkDetailRes", response);
                                    try {
                                        if (response.contains("Data inserted successfully")) {
                                            linearSMS.setVisibility(View.VISIBLE);
                                            Toast.makeText(SummaryDetails.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        // Toast.makeText(context, "Errorcode-531 LoginLogoutDetails LoginlogoutDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Log.d("LeavebalExc", String.valueOf(e));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;
                                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(SummaryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-530 LoginLogoutDetails getLoginlogoutDetails " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcLoginLogoutDetails", String.valueOf(e));
        }
    }//close insertPaymentLinkDetails
    private void getReceivedPaymentDetails(final String fileid) {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=148&FormNo=" + fileid;
            Log.d("ReceivedPaymentUrl", url);
            arrayListReceivedPayment = new ArrayList<>();
            if (CheckInternet.checkInternet(SummaryDetails.this)) {
                if (CheckServer.isServerReachable(SummaryDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        dialog.dismiss();
                                        Log.d("ReceivedPaymentResponse", response);
                                        if (response.contains("[]")) {
                                            horizontalReceivedPayment.setVisibility(View.GONE);
                                            txtPaymentNotReceived.setVisibility(View.VISIBLE);
                                            // Toast.makeText(SummaryDetails.this,"Payment not received",Toast.LENGTH_SHORT).show();
                                        } else {
                                            horizontalReceivedPayment.setVisibility(View.VISIBLE);
                                            txtPaymentNotReceived.setVisibility(View.GONE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                paymentDate = jsonObject1.getString("Payment Date");
                                                paymentDate = paymentDate.substring(0, paymentDate.indexOf(" "));
                                                orderamount = jsonObject1.getString("orderamount");
                                                orderfor = jsonObject1.getString("OrderFor");
                                                orderstatus = jsonObject1.getString("orderstatus");
                                                orderid = jsonObject1.getString("orderid");
                                                paymethod = jsonObject1.getString("paymethod");
                                                invoiceId = jsonObject1.getString("invoiceId");
                                                trackingid = jsonObject1.getString("trackingid");
                                                bankrefno = jsonObject1.getString("bankrefno");
                                                billname = jsonObject1.getString("billname");
                                                billphone = jsonObject1.getString("billphone");
                                                billemail = jsonObject1.getString("billemail");
                                                address = jsonObject1.getString("cAddress");
                                                city = jsonObject1.getString("city");
                                                state = jsonObject1.getString("cstate");
                                                pin = jsonObject1.getString("cpin");
                                                datepay = jsonObject1.getString("datepay");
                                                website = jsonObject1.getString("cwebsite");
                                                paymentthrough = jsonObject1.getString("cpaymentthrough");
                                                formno = jsonObject1.getString("cFormno");
                                                String cid = jsonObject1.getString("cCounselorid");
                                                String paymentlink11 = jsonObject1.getString("Srtpaylink");
                                                String invoicelink11 = jsonObject1.getString("SrtInvoiceLink");
                                                String receiptlink11 = jsonObject1.getString("SrtReceiptLink");

                                                DataReceivedPaymentDetails dataReceivedPaymentDetails = new DataReceivedPaymentDetails(paymentDate, orderamount, orderfor, orderstatus, orderid, paymethod, invoiceId,
                                                        trackingid, bankrefno, billname, billphone, billemail, address, city, state, pin, datepay, website, paymentthrough, formno, cid, paymentlink11, invoicelink11, receiptlink11);
                                                arrayListReceivedPayment.add(dataReceivedPaymentDetails);
                                            }
                                            recyclerReceivedPayment = findViewById(R.id.recyclerReceivedPaymentDetails);
                                            adapterReceivedPaymentDetails = new AdapterReceivedPaymentDetails(SummaryDetails.this, arrayListReceivedPayment);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(SummaryDetails.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerReceivedPayment.setLayoutManager(layoutManager);
                                            recyclerReceivedPayment.setAdapter(adapterReceivedPaymentDetails);
                                            adapterReceivedPaymentDetails.notifyDataSetChanged();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(SummaryDetails.this, "Errorcode-189 CounselorContact counselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Log.d("CounselorDetailExceptio", String.valueOf(e));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;
                                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(SummaryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
        } catch (Exception e) {
            //dialog.dismiss();
            Toast.makeText(SummaryDetails.this, "Errorcode-460 ClientAccount getDetails" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }//close getReceivedPaymentDetails

    private void btnWhatsappClicked() {
        if (!sendPaymentPhone.contains("+91"))
        {
            sendPaymentPhone = "+91" + sendPaymentPhone;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + sendPaymentPhone + "&text=" + msg));
        startActivity(intent);
        // finish();
    }//close btnWhatsappClicked

    public void btnMailClicked() {
        String subject = "Thank you for choosing Select Your University";
        String text = "Dear " + strName + ", \n" +
                "\n" +
                "Thanking you for choosing Select Your University as your preferred study abroad partner. \n" +
                "\n" +
                "Your file number is " + sharedSrno + ". Total amount to be paid is INR " + sendPaymentAmount + "/- against " + sendPaymentFor +
                "\n" +
                "Kindly make the payment at the link: \n" + txtShortLink.getText().toString() + " .Please read the T&C before making payment. \n" +
                "\n" +
                "\n" +
                "Thanks & Regards,\n" +
                "\n" +
                "Management \n" +
                "\n" +
                "Select Your University.";
        String emailAddressList[] = {sendPaymentEmail};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddressList);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setType("plain/text");
        startActivity(Intent.createChooser(intent, "Send mail"));
    }//close btnMailClicked

    public void btnSMSClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + sendPaymentPhone));
        intent.putExtra("sms_body", msg);
        startActivity(intent);
    }

    public void btnWebClicked() {
        try {
            if (CheckInternetSpeed.checkInternet(SummaryDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
            } else if (CheckInternetSpeed.checkInternet(SummaryDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
                //  dialog = ProgressDialog.show(MessageActivity.this, "", "Loading Message...", true);
                sendWebSms();
            }
                       /* String urlWeb=clienturl+"?clientid="+ clientid+"&caseid=301&Sms="+msg+"&Mobile="+smbl;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urlWeb));//+ smbl + "&text=" + msg));
                        startActivity(intent);
                        finish();*/
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//close btnWebClicked

    public void sendWebSms() {
        try {
            if (CheckInternetSpeed.checkInternet(SummaryDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
            } else if (CheckInternetSpeed.checkInternet(SummaryDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
                //dialog = ProgressDialog.show(MessageActivity.this, "", "Loading counselor information...", true);
                sendSms();
            }
        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-282 MessageActivity sendWebSMS " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//close sendWebSms

    public void sendSms() {
        try {
            // requestQueue= Volley.newRequestQueue(context);
            String url = clienturl + "?clientid=" + clientid + "&caseid=86&CandidateName=" + msg + "&Mobile=" + mobile + "&SmsNo=2&SrNo=0&CounselorID=" + counselorid;
            Log.d("SendSmsUrl", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // dialog.dismiss();
                            Log.d("*******", response.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("description");
                                JSONArray jsonArray = jsonObject1.getJSONArray("batch_dtl");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    String status = jsonObject2.getString("status");
                                    Log.d("StatusMsg", status);
                                    if (status.contains("SENT")) {
                                        Toast.makeText(SummaryDetails.this, "Message sent", Toast.LENGTH_SHORT).show();
                                        // edtMessage.setText("");
                                    } else {
                                        Toast.makeText(SummaryDetails.this, "Message not sent", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                Log.d("SendSMSResponse", response);


                            } catch (Exception e) {
                                Toast.makeText(SummaryDetails.this, "Errorcode-281 MessageActivity sendSMSRessponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Exception", String.valueOf(e));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error == null || error.networkResponse == null)
                                return;
                            final String statusCode = String.valueOf(error.networkResponse.statusCode);
                            //get response body and parse with appropriate encoding
                            if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                           /* android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("Network issue!!!")


                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).show();*/

                                // Toast.makeText(context,"Network issue",Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }
                        }
                    });
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-280 MessageActivity sendSMS " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//close sendSms

    public void newThreadInitilization(final ProgressDialog dialog1) {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(SummaryDetails.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
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
       // super.onBackPressed();
        actname=getIntent().getStringExtra("Activity");

            Intent intent = new Intent(SummaryDetails.this, MasterEntry.class);
            intent.putExtra("ActivityName", "SummaryDetails");
            startActivity(intent);
            finish();

    }

    public void btnGetDetailsClicked() {
        try {
            //  fileID = edtFileNo.getText().toString();

            if (CheckInternetSpeed.checkInternet(SummaryDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
            } else if (CheckInternetSpeed.checkInternet(SummaryDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
                dialog = ProgressDialog.show(SummaryDetails.this, "", "Loading details", true);
                newThreadInitilization(dialog);
                getDetails(sharedSrno);
            }

        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-459 ClientAccount btnGetDetailsClicked" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getShortLink(String formno, String name, String mobile, String email, String address, String city, String state, String pin, String paymentfor, String amount, String cid)
    {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=147&FormNo=" + formno + "&Name=" + name + "&Mobile=" + mobile + "&Email=" + email + "&Address=" + address + "&PaymentFor=" + paymentfor + "&Amount=" + amount + "&City=" + city + "&State=" + state + "&Pin=" + pin + "&CounselorID=" + counselorid;
            Log.d("ShortLinkUrl", url);
            if (CheckInternet.checkInternet(SummaryDetails.this)) {
                if (CheckServer.isServerReachable(SummaryDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    linearSendPayment.setVisibility(View.GONE);

                                    Log.d("ShortLinkResponse1", response);
                                    if (edtOther.getVisibility() == View.VISIBLE) {
                                        edtOther.setVisibility(View.GONE);
                                    }
                                    edtAmount.setText("");
                                    linearShortLink.setVisibility(View.VISIBLE);
                                    String invoice = response.substring(0, response.indexOf(" "));
                                    txtShortLink.setText("u4l.in?" + invoice);
                                    orderid1 = invoice;
                                    receiptl = "u4l.in?" + response.substring(response.lastIndexOf(" ")).trim();
                                    invoicel = "u4l.in?" + response.substring(response.indexOf(" ") + 1, response.lastIndexOf(" ")).trim();
                                    paymentl = txtShortLink.getText().toString();

                                    Log.d("OrderId", orderid1 + " " + receiptl + " " + invoicel);
                                    msg = "Dear " + sendPaymentName + ", \n" +
                                            "\n" +
                                            "Thanking you for choosing Select Your University as your preferred study abroad partner. \n" +
                                            "\n" +
                                            "Your file number is " + sharedSrno + ". Total amount to be paid is INR " + sendPaymentAmount + "/- against " + sendPaymentFor +
                                            "\n" +
                                            "Kindly make the payment at the following link.\n " + txtShortLink.getText().toString() + " Please read the TandC before making payment. ";
                                    // msg="Kindly follow this link to make payment: \n"+response;
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;
                                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(SummaryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
        } catch (Exception e) {
            //dialog.dismiss();
            Toast.makeText(SummaryDetails.this, "Errorcode-460 ClientAccount getDetails" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }//close getShortLink
    private void getDetails(final String fileid) {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=98&FileID=" + fileid + "&CounselorID=" + counselorid + "&Step=0";
            Log.d("DetailsUrl", url);
            if (CheckInternet.checkInternet(SummaryDetails.this)) {
                if (CheckServer.isServerReachable(SummaryDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        Log.d("DetailsResponse1", response);
                                        if (response.contains("[]")) {
                                            dialog.dismiss();
                                            // linearName.setVisibility(View.GONE);
                                            Toast.makeText(SummaryDetails.this, "File not exist", Toast.LENGTH_SHORT).show();
                                        } else {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                fname = jsonObject1.getString("cCandidateFName");
                                                lname = jsonObject1.getString("cCandidateLName");
                                                mobile = jsonObject1.getString("cMobile");
                                                passport = jsonObject1.getString("cPassportNo");
                                                panno = jsonObject1.getString("cPanNo");
                                            }
                                            getCollegeDetails(fileid);
                                            linearPersonalInfo.setVisibility(View.VISIBLE);
                                            /*linearName.setVisibility(View.VISIBLE);
                                            edtName.setText(fname + " " + lname);*/
                                            txtName.setText(fname + " " + lname);
                                            txtFileID.setText(sharedSrno + ".");

                                            if (mobile.length() == 0) {
                                                txtMobile.setText("NA");
                                            } else {
                                                txtMobile.setText(mobile);
                                            }
                                            if (passport.length() == 0) {
                                                txtPassportNo.setText("NA");
                                            } else {
                                                txtPassportNo.setText(passport);
                                            }
                                            if (panno.length() == 0) {
                                                txtPanNo.setText("NA");
                                            } else {
                                                txtPanNo.setText(panno);
                                            }
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(SummaryDetails.this, "Errorcode-189 CounselorContact counselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Log.d("CounselorDetailExceptio", String.valueOf(e));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;
                                    final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(SummaryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
        } catch (Exception e) {
            //dialog.dismiss();
            Toast.makeText(SummaryDetails.this, "Errorcode-460 ClientAccount getDetails" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }//close getDetails

    public void getCollegeDetails(String fileID) {
        try {
            arrayListMasterEntry = new ArrayList<>();
            String url = clienturl + "?clientid=" + clientid + "&caseid=113&CounselorID=" + counselorid + "&FileNo=" + fileID + "&Step=0";
            Log.d("CollegeDetailsUrl", url);
            if (CheckInternet.checkInternet(SummaryDetails.this)) {
                if (CheckServer.isServerReachable(SummaryDetails.this)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("CollgeDetailsResponse", response);
                                        linearCollegeInfo.setVisibility(View.VISIBLE);

                                        if (response.contains("[]")) {
                                            txtNoCollege.setVisibility(View.VISIBLE);
                                            // fab.setVisibility(View.GONE);
                                            dialog.dismiss();
                                            linearPackageDetails.setVisibility(View.GONE);
                                            linearPaymentHistory.setVisibility(View.GONE);
                                            txtClgInfoTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_white_24dp, 0);
                                            linearClgName.setVisibility(View.GONE);
                                        } else {
                                            txtClgInfoTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                            txtNoCollege.setVisibility(View.GONE);
                                            linearClgName.setVisibility(View.VISIBLE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                clgid = jsonObject1.getString("nCollegeID");
                                                clgname = jsonObject1.getString("cCollegeName");
                                                coursename = jsonObject1.getString("cCourseName");
                                            }
                                            txtCollegeName.setText(clgname);
                                            txtCourse.setText(coursename);
                                            getPackageDetails(clgid);

                                            //fab.setVisibility(View.VISIBLE);

                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(SummaryDetails.this, "Errorcode-456 ClientAccount getCollegeDetailsResponse" + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(SummaryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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

        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-455 ClientAccount getCollegeDetails" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//close getCollegeDetails

    public void getPackageDetails(String clgid) {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=116&FileNo=" + sharedSrno;
            Log.d("PackageDetailsUrl", url);
            if (CheckInternet.checkInternet(SummaryDetails.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PackageDetailsResponse", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                    dialog.dismiss();
                                    if (response.contains("[]")) {
                                        linearPackageDetails.setVisibility(View.GONE);
                                        //  Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        linearPackageDetails.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            collegeFee1YearD = jsonObject1.getString("CollegeFee1YearD");
                                            collegeFeeFinal = jsonObject1.getString("CollegeFee1YearD_Final");
                                            hostalFee1YearD = jsonObject1.getString("HostalFee1YearD");
                                            hostalFeeFinal = jsonObject1.getString("HostalFee1YearD_Final");
                                            oneTimeProcessingFeeD = jsonObject1.getString("OneTimeProcessingFeeD");
                                            OTPFeeFinal = jsonObject1.getString("OneTimeProcessingFeeD_Final");
                                            otherFee1D = jsonObject1.getString("Accidental_Insurance");
                                            accidentalFeeFinal = jsonObject1.getString("Accidental_Insurance_Final");
                                            otherFee2D = jsonObject1.getString("MessFeeD");
                                            messFeeFinal = jsonObject1.getString("MessFeeD_Final");
                                            totalFeeYearD = jsonObject1.getString("TotalFeeYearD");
                                            //  collegeFeeFinal= jsonObject1.getString("CollegeFee1YearD");
                                            airFareFeeR = jsonObject1.getString("AirFareFeeR");
                                            AirFareFinal = jsonObject1.getString("AirFareFeeR_Final");
                                            otherFee3D = jsonObject1.getString("IndiaOTC");
                                            IndiaOTCFeeFinal = jsonObject1.getString("IndiaOTC_Final");
                                            totalFeeYearR = jsonObject1.getString("TotalFeeYearR");
                                            // collegeFeeFinal= jsonObject1.getString("CollegeFee1YearD");
                                        }
                                        if (collegeFee1YearD == null || collegeFee1YearD.isEmpty() || collegeFee1YearD.equals("null") || collegeFee1YearD.contains("NA")) {
                                            txtCollegeFeeTotalD.setText("0");
                                        } else {
                                            txtCollegeFeeTotalD.setText(collegeFee1YearD);
                                        }
                                        if (hostalFee1YearD == null || hostalFee1YearD.isEmpty() || hostalFee1YearD.equals("null") || hostalFee1YearD.contains("NA")) {

                                            txtHostalTotalFeeD.setText("0");
                                        } else {
                                            txtHostalTotalFeeD.setText(hostalFee1YearD);
                                        }
                                        if (oneTimeProcessingFeeD == null || oneTimeProcessingFeeD.isEmpty() || oneTimeProcessingFeeD.equals("null") || oneTimeProcessingFeeD.contains("NA")) {
                                            txtOTPTotalFeeD.setText("0");
                                        } else {
                                            txtOTPTotalFeeD.setText(oneTimeProcessingFeeD);
                                        }
                                        if (otherFee1D == null || otherFee1D.isEmpty() || otherFee1D.equals("null") || otherFee1D.contains("NA")) {
                                            txtAccidentalTotal.setText("0");
                                        } else {
                                            txtAccidentalTotal.setText(otherFee1D);
                                        }
                                        if (otherFee2D == null || otherFee2D.isEmpty() || otherFee2D.equals("null") || otherFee2D.contains("NA")) {
                                            txtMessTotal.setText("0");
                                        } else {
                                            txtMessTotal.setText(otherFee2D);
                                        }
                                        int totalFeeD = Integer.parseInt(txtCollegeFeeTotalD.getText().toString()) + Integer.parseInt(txtHostalTotalFeeD.getText().toString()) +
                                                Integer.parseInt(txtOTPTotalFeeD.getText().toString()) + Integer.parseInt(txtAccidentalTotal.getText().toString()) + Integer.parseInt(txtMessTotal.getText().toString());


                                        txtTotalFeeD.setText(String.valueOf(totalFeeD));

                                        if (airFareFeeR == null || airFareFeeR.isEmpty() || airFareFeeR.equals("null") || airFareFeeR.contains("NA")) {
                                            txtAirFareFeeTotal.setText("0");
                                        } else {
                                            txtAirFareFeeTotal.setText(airFareFeeR);
                                        }
                                        if (otherFee3D == null || otherFee3D.isEmpty() || otherFee3D.equals("null") || otherFee3D.contains("NA")) {
                                            txtIndiaOTCTotal.setText("0");
                                        } else {
                                            txtIndiaOTCTotal.setText(otherFee3D);
                                        }
                                        int totalFeeR = Integer.parseInt(txtAirFareFeeTotal.getText().toString()) + Integer.parseInt(txtIndiaOTCTotal.getText().toString());

                                        txtTotalFeeTotalR.setText(String.valueOf(totalFeeR));

                                        if (collegeFeeFinal == null || collegeFeeFinal.isEmpty() || collegeFeeFinal.equals("null") || collegeFeeFinal.contains("NA")) {
                                            txtCollegeFeeFinal.setText("0");
                                        } else {
                                            txtCollegeFeeFinal.setText(collegeFeeFinal);
                                        }
                                        if (hostalFeeFinal == null || hostalFeeFinal.isEmpty() || hostalFeeFinal.equals("null") || hostalFeeFinal.contains("NA")) {
                                            txtHostalFinalFeeD.setText("0");
                                        } else {
                                            txtHostalFinalFeeD.setText(hostalFeeFinal);
                                        }
                                        if (OTPFeeFinal == null || OTPFeeFinal.isEmpty() || OTPFeeFinal.equals("null") || OTPFeeFinal.contains("NA")) {
                                            txtOTPFeeFinalD.setText("0");
                                        } else {
                                            txtOTPFeeFinalD.setText(OTPFeeFinal);
                                        }
                                        if (accidentalFeeFinal == null || accidentalFeeFinal.isEmpty() || accidentalFeeFinal.equals("null") || accidentalFeeFinal.contains("NA")) {
                                            txtAccidentalFinal.setText("0");
                                        } else {
                                            txtAccidentalFinal.setText(accidentalFeeFinal);
                                        }
                                        if (messFeeFinal == null || messFeeFinal.isEmpty() || messFeeFinal.equals("null") || messFeeFinal.contains("NA")) {
                                            txtMessFinal.setText("0");
                                        } else {
                                            txtMessFinal.setText(messFeeFinal);
                                        }

                                        int totalFinalD1 = Integer.parseInt(txtCollegeFeeFinal.getText().toString()) + Integer.parseInt(txtHostalFinalFeeD.getText().toString()) +
                                                Integer.parseInt(txtOTPFeeFinalD.getText().toString())
                                                + Integer.parseInt(txtAccidentalFinal.getText().toString()) + Integer.parseInt(txtMessFinal.getText().toString());

                                        txtFeeFinalD.setText(String.valueOf(totalFinalD1));
                                        if (AirFareFinal == null || AirFareFinal.isEmpty() || AirFareFinal.equals("null") || AirFareFinal.contains("NA")) {
                                            txtAirFareFeeFinal.setText("0");
                                        } else {
                                            txtAirFareFeeFinal.setText(AirFareFinal);
                                        }
                                        if (IndiaOTCFeeFinal == null || IndiaOTCFeeFinal.isEmpty() || IndiaOTCFeeFinal.equals("null") || IndiaOTCFeeFinal.contains("NA")) {
                                            txtIndiaOTCFinal.setText("0");
                                        } else {
                                            txtIndiaOTCFinal.setText(IndiaOTCFeeFinal);
                                        }
                                        int totalFinalR = Integer.parseInt(txtAirFareFeeFinal.getText().toString()) + Integer.parseInt(txtIndiaOTCFinal.getText().toString());
                                        txtTotalFeeFinalR.setText(String.valueOf(totalFinalR));


                                        getPackageTotalDetails();

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(SummaryDetails.this, "Errorcode-449 ClientAccount getPackageDetailsResponse" + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(SummaryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-448 ClientAccount getPackageDetails" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList", e.toString());
        }
    }//close getPackageDetails

    public void getPackageTotalDetails() {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=121&FileNo=" + fileID;
            Log.d("PackageReceivedUrl", url);
            if (CheckInternet.checkInternet(SummaryDetails.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PackageReceivedResponse", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                    // dialog.dismiss();
                                    getPackageReceivedDetail();
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
                                            inr = jsonObject1.getString("INR");
                                            usd = jsonObject1.getString("USD");
                                        }
                                        txtTotalRecivedUSD.setText("Amount Received in USD:" + usd);
                                        txtTotalRecivedINR.setText("Amount Received in INR:" + inr);


                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(SummaryDetails.this, "Errorcode-451 ClientAccount PackageTotalDetailsResponse" + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(SummaryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-450 ClientAccount getPackageTotalDetails" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList", e.toString());
        }
    }//close getPackageTotalDetails

    public void getPackageReceivedDetail() {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=122&FileNo=" + fileID;
            Log.d("PckgReceivedDetailsUrl", url);
            if (CheckInternet.checkInternet(SummaryDetails.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PckgReceivedDetailsRes", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                    //  dialog.dismiss();
                                    getPaymentHistory();
                                    if (response.contains("[]")) {
                                        txtCollegeFeeReceivedD.setText("0");
                                        txtHostalReceivedFeeD.setText("0");
                                        txtOTPReceivedFeeD.setText("0");
                                        txtAccidentalReceived.setText("0");
                                        txtMessReceived.setText("0");
                                        txtAirFareFeeReceived.setText("0");
                                        txtIndiaOTCReceived.setText("0");

                                        airfareTotal = Integer.parseInt(txtAirFareFeeTotal.getText().toString());
                                        airfareReceived = Integer.parseInt(txtAirFareFeeReceived.getText().toString());
                                        airfareDues = airfareTotal - airfareReceived;
                                        txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal = Integer.parseInt(txtIndiaOTCTotal.getText().toString());
                                        indiaOTCRecceived = Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues = indiaOTCTotal - indiaOTCRecceived;
                                        txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                        clgfeetotal = Integer.parseInt(txtCollegeFeeTotalD.getText().toString());
                                        clgfeeReceived = Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                        clgfeeDue = clgfeetotal - clgfeeReceived;
                                        txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal = Integer.parseInt(txtHostalTotalFeeD.getText().toString());
                                        hostalFeeReceived = Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue = hostalFeeTotal - hostalFeeReceived;
                                        txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal = Integer.parseInt(txtOTPTotalFeeD.getText().toString());
                                        OTPReceived = Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue = OTPTotal - OTPReceived;
                                        txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal = Integer.parseInt(txtAccidentalTotal.getText().toString());
                                        accidentalReceived = Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue = accidentalTotal - accidentalReceived;
                                        txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal = Integer.parseInt(txtMessTotal.getText().toString());
                                        messReceived = Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue = messTotal - messReceived;
                                        txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD = clgfeeReceived + hostalFeeReceived + OTPReceived + accidentalReceived + messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD = clgfeeDue + hostalFeeDue + OTPDue + accidentalDue + messDue;
                                        txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR = airfareReceived + indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR = airfareDues + indiaOTCDues;
                                        txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));
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
                                            collegeFee1YearD1 = jsonObject1.getString("CollegeFee1YearD");
                                            hostalFee1YearD1 = jsonObject1.getString("HostalFee1YearD");
                                            oneTimeProcessingFeeD1 = jsonObject1.getString("OneTimeProcessingFeeD");
                                            Accidental_Insurance = jsonObject1.getString("Accidental_Insurance");
                                            MessFeeD = jsonObject1.getString("MessFeeD");
                                            IndiaOTC = jsonObject1.getString("IndiaOTC");
                                            AirFareFee = jsonObject1.getString("AirFareFeeR");
                                        }
                                        if (collegeFee1YearD1 == null || collegeFee1YearD1.isEmpty() || collegeFee1YearD1.equals("null") || collegeFee1YearD1.contains("NA")) {
                                            txtCollegeFeeReceivedD.setText("0");
                                        } else {
                                            txtCollegeFeeReceivedD.setText(collegeFee1YearD1);
                                        }
                                        if (hostalFee1YearD1 == null || hostalFee1YearD1.isEmpty() || hostalFee1YearD1.equals("null") || hostalFee1YearD1.contains("NA")) {
                                            txtHostalReceivedFeeD.setText("0");
                                        } else {
                                            txtHostalReceivedFeeD.setText(hostalFee1YearD1);
                                        }

                                        if (oneTimeProcessingFeeD1 == null || oneTimeProcessingFeeD1.isEmpty() || oneTimeProcessingFeeD1.equals("null") || oneTimeProcessingFeeD1.contains("NA")) {
                                            txtOTPReceivedFeeD.setText("0");
                                        } else {
                                            txtOTPReceivedFeeD.setText(oneTimeProcessingFeeD1);
                                        }
                                        if (Accidental_Insurance == null || Accidental_Insurance.isEmpty() || Accidental_Insurance.equals("null") || Accidental_Insurance.contains("NA")) {
                                            txtAccidentalReceived.setText("0");
                                        } else {
                                            txtAccidentalReceived.setText(Accidental_Insurance);
                                        }
                                        if (MessFeeD == null || MessFeeD.isEmpty() || MessFeeD.equals("null") || MessFeeD.contains("NA")) {
                                            txtMessReceived.setText("0");
                                        } else {
                                            txtMessReceived.setText(MessFeeD);
                                        }
                                        if (AirFareFee == null || AirFareFee.isEmpty() || AirFareFee.equals("null") || AirFareFee.contains("NA")) {
                                            txtAirFareFeeReceived.setText("0");
                                        } else {
                                            txtAirFareFeeReceived.setText(AirFareFee);
                                        }
                                        if (IndiaOTC == null || IndiaOTC.isEmpty() || IndiaOTC.equals("null") || IndiaOTC.contains("NA")) {
                                            txtIndiaOTCReceived.setText("0");
                                        } else {
                                            txtIndiaOTCReceived.setText(IndiaOTC);
                                        }

                                      /*  txtOTPReceivedFeeD.setText(oneTimeProcessingFeeD1);
                                        txtAccidentalReceived.setText(Accidental_Insurance);
                                        txtMessReceived.setText(MessFeeD);
                                        txtAirFareFeeReceived.setText(AirFareFee);
                                        txtIndiaOTCReceived.setText(IndiaOTC);*/

                                        airfareTotal = Integer.parseInt(txtAirFareFeeFinal.getText().toString());
                                        airfareReceived = Integer.parseInt(txtAirFareFeeReceived.getText().toString());
                                        airfareDues = airfareTotal - airfareReceived;
                                        txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal = Integer.parseInt(txtIndiaOTCFinal.getText().toString());
                                        indiaOTCRecceived = Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues = indiaOTCTotal - indiaOTCRecceived;
                                        txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                        clgfeetotal = Integer.parseInt(txtCollegeFeeFinal.getText().toString());
                                        clgfeeReceived = Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                        clgfeeDue = clgfeetotal - clgfeeReceived;
                                        txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal = Integer.parseInt(txtHostalFinalFeeD.getText().toString());
                                        hostalFeeReceived = Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue = hostalFeeTotal - hostalFeeReceived;
                                        txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal = Integer.parseInt(txtOTPFeeFinalD.getText().toString());
                                        OTPReceived = Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue = OTPTotal - OTPReceived;
                                        txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal = Integer.parseInt(txtAccidentalFinal.getText().toString());
                                        accidentalReceived = Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue = accidentalTotal - accidentalReceived;
                                        txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal = Integer.parseInt(txtMessFinal.getText().toString());
                                        messReceived = Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue = messTotal - messReceived;
                                        txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD = clgfeeReceived + hostalFeeReceived + OTPReceived + accidentalReceived + messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD = clgfeeDue + hostalFeeDue + OTPDue + accidentalDue + messDue;
                                        txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR = airfareReceived + indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR = airfareDues + indiaOTCDues;
                                        txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));

                                      /*  totalfeeReceived=Integer.parseInt(txtTotalFeeD.getText().toString());
                                        totalReceived=Integer.parseInt(txtReceivedFeeD.getText().toString());
                                        totalDue=totalfeeReceived-totalReceived;
                                        txtDueFeeD.setText(String.valueOf(totalDue));*/
                                        Log.d("txtReceivedVal", txtCollegeFeeReceivedD.getText().toString());


                                         /* txtTotalFeeD.setText(totalFeeYearD);
                                            txtAirFareFeeR.setText("AirFareFeeR : "+airFareFeeR);
                                               txtIndiaOTC.setText(otherFee3Head+" : "+otherFee3D);
                                          txtTotalFeeYearR.setText("TotalFeeYearR :"+totalFeeYearR);*/
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(SummaryDetails.this, "Errorcode-447 ClientAccount PackageReceivedDetailsResponse" + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(SummaryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-446 ClientAccount getPackageReceivedDetails" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList", e.toString());
        }
    }//close getPackageReceivedDetails

    public void getPaymentHistory() {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=125&FileNo=" + sharedSrno;
            Log.d("PaymentHistoryUrl", url);
            if (CheckInternet.checkInternet(SummaryDetails.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PaymentHistoryRes", response);
                                try {
                                    arrayListPaymentHistory = new ArrayList<>();
                                    dialog.dismiss();
                                    if (response.contains("[]")) {
                                        linearPaymentHistory.setVisibility(View.GONE);
                                        //  Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        linearPaymentHistory.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            paymentid = jsonObject1.getString("nPaymentID");
                                            amountINR = jsonObject1.getString("cAmountINR");
                                            amountUSD = jsonObject1.getString("cAmountUSD");
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("dtPaymentDate");
                                            String paymentDate;
                                            date1 = jsonObject2.getString("date");
                                            paymentDate = date1.substring(0, date1.indexOf(" "));
                                            approved = jsonObject1.getString("IsApprovedbyAccountant");
                                            if (amountUSD.length() == 0) {
                                                amountUSD = "0";
                                            }
                                            if (amountINR.length() == 0) {
                                                amountINR = "0";
                                            }
                                            DataPaymentHistory dataPaymentHistory = new DataPaymentHistory(fileID, paymentid, amountUSD, amountINR, paymentDate, approved);
                                            arrayListPaymentHistory.add(dataPaymentHistory);
                                        }
                                        adapterPaymentHistory = new AdapterPaymentHistory(SummaryDetails.this, arrayListPaymentHistory);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(SummaryDetails.this);
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerPaymentHistory.setLayoutManager(layoutManager);
                                        recyclerPaymentHistory.setAdapter(adapterPaymentHistory);
                                        adapterPaymentHistory.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(SummaryDetails.this, "Errorcode-445 ClientAccount getPaymentHistoryResponse" + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(SummaryDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-444 ClientAccount getPaymentHistory" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList", e.toString());
        }
    }//close getPaymentHistory

    private void loadSummaryData(String url) {
        try {
            if (CheckServer.isServerReachable(SummaryDetails.this)) {
                requestQueue = Volley.newRequestQueue(SummaryDetails.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("summ", response);
                            if (jsonObject.getInt("success") == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    strDocNM = jsonObject1.getString("");
                                    strDoccount = jsonObject1.getString("Total");
                                    detailsSummary = new DataSummary(strDocNM, strDoccount);
                                    if (strDocNM.contains("Personal")) {
                                        personalinfoArrayList.add(detailsSummary);
                                        adapterPersonalInfo.notifyDataSetChanged();
                                    } else if (strDocNM.contains("Education")) {
                                        educationsArrayList.add(detailsSummary);
                                        adapterEducation.notifyDataSetChanged();
                                    } else if (strDocNM.contains("Document")) {
                                        documentsArrayList.add(detailsSummary);
                                        adapterDocuments.notifyDataSetChanged();
                                    }
                                }
                                Log.d("personal", personalinfoArrayList.size() + "");
                                Log.d("educ", educationsArrayList.size() + "");
                                Log.d("docum", documentsArrayList.size() + "");

                                progressBar.dismiss();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(SummaryDetails.this, "Errorcode-313 SummaryDetails SummaryDataResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);
            } else {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
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
        } catch (Exception e) {
            Toast.makeText(SummaryDetails.this, "Errorcode-312 SummaryDetails loadSummaryData " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//close loadSummaryData
}
