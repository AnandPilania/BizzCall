package com.bizcall.wayto.mentebit13;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClientAccount extends AppCompatActivity {

    private Spinner mSearchspinner;
    private LinearLayout linearPersonalInfo,linearCollegeInfo,linearPackageDetails,linearSearchBy,linearClgName,linearSpinnerClg,linearAmountDetails;
    private RecyclerView mRecyclerviewTabledetails;
    HorizontalScrollView horizontalScrollView;
    TextView txtNoResult,txtCollegeName,txtCourse,txtNoCollege,txtClgInfoTitle,txtFileID;

    private ArrayList<DataMasterEntry> arrayListMasterEntry;
    private AdapterSearchFile myAdapter;
    private DataMasterEntry dataMasterEntry;
    Button btnAllDetails;

    private TextView txtTitle,txtSpinnerError;
    private EditText edtSearchText;

    String strFileId,strSrNo,strAllocatedTo,strFname, strLname,strDob,searchAs,result,clgid,clgname,coursename;
    private String counselorid,clienturl,clientid,url;
    ProgressDialog dialog;
    RequestQueue requestQueue;
    Button btnSearch,btnAlldetails;
    Vibrator vibrator;
    LinearLayout linearTableColumn;
    ArrayList<String> arrayListClgId,arrayListCollege;
    Spinner spinnerCollegeList;
    String pos,pos1;
    private String [] mSearchMethod = {"Select", "File ID", "Serial No","First Name", "Last Name","Parent Name","Pan No","Aadhaar No", "Passport No","Mobile No","Parent No","EmailID","City","State","Pin Code"};
    ImageView imgClose,imgBack;
    TextView txtSubmit,txtTotalRecivedUSD,txtTotalRecivedINR;
    EditText edtFileNo;
    TextView txtSearch,txtGetDetails,txtName,txtMobile,txtPassportNo,txtPanNo;
    TextView txtCollegeFeeTotalD,txtCollegeFeeFinal,txtCollegeFeeReceivedD,txtCollegeFeeDueD,txtHostalTotalFeeD,txtHostalFinalFeeD,txtHostalReceivedFeeD,txtHostalDueFeeD,txtOTPTotalFeeD,
            txtOTPFeeFinalD,txtOTPReceivedFeeD,txtOTPDueFeeD,txtAccidentalTotal,txtAccidentalFinal,txtAccidentalReceived,txtAccidentalDue,txtMessTotal,txtMessFinal,txtMessReceived,txtMessDue,txtTotalFeeD,txtFeeFinalD,txtReceivedFeeD,txtDueFeeD;
    TextView txtAirFareFeeTotal,txtAirFareFeeFinal,txtAirFareFeeReceived,txtAirFareFeeDues,txtIndiaOTCTotal,txtIndiaOTCFinal,txtIndiaOTCReceived,txtIndiaOTCDues,txtTotalFeeTotalR,txtTotalFeeFinalR,txtTotalFeeReceivedR,txtTotalFeeDuesR;
    TextView txtCollegeFee1YearD1,txtHostalFee1YearD1,txtOneTimeProcessingFeeD1,txtAccidentalInsurance1,txtMess1,txtTotalFeeYearD1,txtDiscountDetails;

    String fileID,fname,lname,mobile,passport,panno,collegeFee1YearD,hostalFee1YearD,oneTimeProcessingFeeD,otherFee1Head,otherFee1D,otherFee2Head,otherFee2D,otherFee3Head,otherFee3D,mess,totalFeeYearD,airFareFeeR,indiaOTC,totalFeeYearR;
   AlertDialog alertDialog1;
    SharedPreferences sp;
    String usd,inr;
    FloatingActionButton fab;
    int clgfeetotal,clgfeeReceived,clgfeeDue,hostalFeeTotal,hostalFeeReceived,hostalFeeDue,OTPReceived,OTPTotal,OTPDue,accidentalTotal,
            accidentalReceived,accidentalDue,messTotal,messReceived,messDue,totalReceivedD,totalDueD,airfareTotal,airfareReceived,airfareDues,
    indiaOTCTotal,indiaOTCRecceived,indiaOTCDues,totalReceivedR,totalDuesR;
    String collegeFee1YearD1,collegeFeeFinal,hostalFee1YearD1,hostalFeeFinal,OTPFeeFinal,accidentalFeeFinal,messFeeFinal,IndiaOTCFeeFinal,AirFareFinal,totalFeeFinalD,totalFeeFinalR,oneTimeProcessingFeeD1,Accidental_Insurance,MessFeeD,IndiaOTC,AirFareFee;
    RecyclerView recyclerPaymentHistory;
    AdapterPaymentHistory adapterPaymentHistory;
    ArrayList<DataPaymentHistory> arrayListPaymentHistory;
    String actname,paymentid,amountUSD,amountINR,date1,approved;
    LinearLayout linearPaymentHistory,linearFileNo;
    Thread thread;
    long timeout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_account);
        try {
           initialize();

            if(actname.contains("SummaryDetails"))
            {
                setTheme(android.R.style.Theme_Material_Light_Dialog_Alert);
                linearFileNo.setVisibility(View.GONE);
                fileID=getIntent().getStringExtra("FileID");
                //to check internet connection and call getDetails function
                getFileInfo();
            }
            else if(actname.contains("PaymentEntry")||actname.equals("DiscountOffer")) {

                fileID=getIntent().getStringExtra("FileID");
                edtFileNo.setText(fileID);
                //to check internet connection and call getDetails function
                getFileInfo();
            }
            txtClgInfoTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtNoCollege.getVisibility() == View.VISIBLE) {
                        txtNoCollege.setVisibility(View.GONE);
                        linearSpinnerClg.setVisibility(View.VISIBLE);
                        txtSubmit.setVisibility(View.VISIBLE);
                        dialog = ProgressDialog.show(ClientAccount.this, "", "Loading college list", true);
                        newThreadInitilization(dialog);
                        //get list of colleges
                        getCollegeList();
                    }

                }
            });
            txtDiscountDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtDiscountClicked();
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ClientAccount.this, PaymentEntry.class);
                    intent.putExtra("FileNo1", fileID);
                    intent.putExtra("ActName",actname);
                    intent.putExtra("CName", txtName.getText().toString());
                    startActivity(intent);
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            spinnerCollegeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pos = arrayListClgId.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinnerCollegeList.setSelection(0);
                }
            });
            txtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                    } else if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                        dialog = ProgressDialog.show(ClientAccount.this, "", "Allocating college", true);
                        newThreadInitilization(dialog);
                        //to insert allocated college details to database
                        submitCollege();
                    }
                }
            });
            txtGetDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog=ProgressDialog.show(ClientAccount.this,"","Loading Details",true);
                    //to check internet and call getDetails function
                    btnGetDetailsClicked();
                }
            });
            txtSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //alert to search file id from masterentry
                    showAlert();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(ClientAccount.this,"Errorcode-436 ClientAccount onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }//onCreate

    private void initialize() {
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

        fab = findViewById(R.id.fab);
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
        linearFileNo=findViewById(R.id.linearFileNo);
        edtFileNo = findViewById(R.id.edtFileNo);
        txtSearch = findViewById(R.id.txtSearch);
        txtGetDetails = findViewById(R.id.txtGetDetails);
        txtName = findViewById(R.id.txtCandidateName);
        txtMobile = findViewById(R.id.txtMobile1);
        txtPassportNo = findViewById(R.id.txtPassportNo);
        txtPanNo = findViewById(R.id.txtPanNo);
        txtCollegeName = findViewById(R.id.txtCollegeName);
        txtCourse = findViewById(R.id.txtCourse);
        txtNoCollege = findViewById(R.id.txtNoCollege);
        txtClgInfoTitle = findViewById(R.id.txtClgInfoTitle);
        spinnerCollegeList = findViewById(R.id.spinnerCollgeList);
        imgBack = findViewById(R.id.img_back);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(ClientAccount.this);
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        timeout = sp.getLong("TimeOut", 0);
        counselorid = sp.getString("Id", "");
        counselorid = counselorid.replace(" ", "");

        actname=getIntent().getStringExtra("ActName");
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
                                Toast.makeText(ClientAccount.this, "Connection aborted", Toast.LENGTH_SHORT).show();
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
    private void txtDiscountClicked()
    {
        //to get discount offer
        Intent intent=new Intent(ClientAccount.this,DiscountOffer.class);
        intent.putExtra("FineNo1",fileID);
        startActivity(intent);
    }//txtDiscountClicked

    @Override
    public void onBackPressed() {
        if(actname.contains("SummaryDetails"))
        {
            Intent intent=new Intent(ClientAccount.this,SummaryDetails.class);
            intent.putExtra("FileNo",fileID);
            intent.putExtra("MobileNo",mobile);
            startActivity(intent);
            finish();

        }
        else {
            Intent intent = new Intent(ClientAccount.this, Home.class);
            intent.putExtra("Activity", "ClientAccount");
            startActivity(intent);
            finish();
        }
    }


    public void showAlert()
    {
        try {
            LayoutInflater li = LayoutInflater.from(ClientAccount.this);
            //Creating a view to get the dialog box
            View view = li.inflate(R.layout.activity_search_file, null);
            horizontalScrollView = view.findViewById(R.id.search_horizontalscroll);
            txtNoResult = view.findViewById(R.id.txtNoResultFound);
            linearTableColumn = view.findViewById(R.id.linearTableColumn);
            // imgBack = findViewById(R.id.img_back);
            imgClose = view.findViewById(R.id.imgClose);
            mSearchspinner = view.findViewById(R.id.search_spinner);
            btnSearch = view.findViewById(R.id.btn_search);
            btnAlldetails = view.findViewById(R.id.btnAlldetails);
            linearSearchBy = view.findViewById(R.id.searchby_layout);
            edtSearchText = view.findViewById(R.id.edtSearchtext);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtSpinnerError = view.findViewById(R.id.txtSpinnerError);

            mRecyclerviewTabledetails = view.findViewById(R.id.detail_recyclerview);

            final AlertDialog.Builder alert = new AlertDialog.Builder(ClientAccount.this);
            //Adding our dialog box to the view of alert dialog
            alert.setView(view);
            //Creating an alert dialog
            alertDialog1 = alert.create();
            alertDialog1.show();
            alertDialog1.setCancelable(false);

            ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(ClientAccount.this,
                    android.R.layout.simple_spinner_dropdown_item, mSearchMethod);
            mSearchspinner.setAdapter(mSpinnerAdapter);
       /* imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
            }
        });*/
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                }
            });
            mSearchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    searchAsItemSelected();
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            txtTitle.setText("File ID");
                            break;
                        case 2:
                            txtTitle.setText("Serial No");
                            break;
                        case 3:
                            txtTitle.setText("First Name");
                            break;
                        case 4:
                            txtTitle.setText("Last Name");
                            break;
                        case 5:
                            txtTitle.setText("Parent Name");
                            break;
                        case 6:
                            txtTitle.setText("Pan No");
                            break;
                        case 7:
                            txtTitle.setText("Aadhar No");
                            break;
                        case 8:
                            txtTitle.setText("Passport No");
                            break;
                        case 9:
                            txtTitle.setText("Mobile No");
                            break;
                        case 10:
                            txtTitle.setText("Parent No");
                            break;
                        case 11:
                            txtTitle.setText("Email ID");
                            break;
                        case 12:
                            txtTitle.setText("City");
                            break;
                        case 13:
                            txtTitle.setText("State");
                            break;
                        case 14:
                            txtTitle.setText("Pin code");
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });
            btnAlldetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("0")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                        } else if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("1")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                            dialog = ProgressDialog.show(ClientAccount.this, "", "Loading All Details", true);
                            newThreadInitilization(dialog);
                            //to get all details of particular file id like name address etc
                            getAllDetails();
                        }
                    }catch (Exception e)
                    {
                        Toast.makeText(ClientAccount.this,"Errorcode-438 ClientAccount btnAllDetailsClick "+e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String searchtext = edtSearchText.getText().toString();
                        String searchAs = mSearchspinner.getSelectedItem().toString();
                        if (searchAs.contains("Select")) {
                            txtSpinnerError.setVisibility(View.VISIBLE);
                        } else {
                            txtSpinnerError.setVisibility(View.GONE);
                            if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("0")) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                            } else if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("1")) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                                if (searchtext.length() != 0) {
                                    dialog = ProgressDialog.show(ClientAccount.this, "", "Searching data", true);
                                    newThreadInitilization(dialog);
                                    searchDetails(searchtext);
                                } else {
                                    edtSearchText.setError("Enter text to search");
                                }
                            }
                        }
                    }catch (Exception e)
                    {
                        Toast.makeText(ClientAccount.this,"Errorcode-339 ClientAccount btnSearchClick "+e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(ClientAccount.this,"Errorcode-437 ClientAccount showAlert "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//showAlert
    public void getAllDetails()
    {
        try {
            arrayListMasterEntry = new ArrayList<>();
            url = clienturl + "?clientid=" + clientid + "&caseid=78&CounselorID=" + counselorid+"&Step=0";
            Log.d("AllDetailsUrl", url);
            if (CheckInternet.checkInternet(ClientAccount.this)) {
                if(CheckServer.isServerReachable(ClientAccount.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("AllDetailsResponse", response);
                                    try {
                                        dialog.dismiss();
                                        if (response.contains("[]")) {
                                            horizontalScrollView.setVisibility(View.GONE);
                                            txtNoResult.setVisibility(View.VISIBLE);
                                            // linearTableColumn.setVisibility(View.VISIBLE);
                                        } else {
                                            horizontalScrollView.setVisibility(View.VISIBLE);
                                            txtNoResult.setVisibility(View.GONE);

                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                strFileId = jsonObject1.getString("nFileID");
                                                strSrNo = jsonObject1.getString("nSrNo");
                                                strAllocatedTo = jsonObject1.getString("AllocatedTo");
                                                strFname = jsonObject1.getString("cCandidateFName");
                                                strLname = jsonObject1.getString("cCandidateLName");
                                                JSONObject jsonObject2 = jsonObject1.getJSONObject("dtDOB");
                                                strDob = jsonObject2.getString("date");
                                                String strGender = jsonObject1.getString("cGender");
                                                String strParentName = jsonObject1.getString("cParentName");
                                                String strPanNo = jsonObject1.getString("cPanNo");
                                                String strAdharNo = jsonObject1.getString("cAadhaarNo");
                                                String strPassportNo = jsonObject1.getString("cPassportNo");
                                                String strMobile = jsonObject1.getString("cMobile");
                                                String strParentNo = jsonObject1.getString("cParantNo");
                                                String strEmail = jsonObject1.getString("cEmail");
                                                String strPincode = jsonObject1.getString("cPinCode");
                                                String strAddress = jsonObject1.getString("cAddressLine");
                                                String strCity = jsonObject1.getString("cCity");
                                                String strState = jsonObject1.getString("cState");
                                                String strCreatedDt = jsonObject1.getString("dtCreatedDate");
                                                String strUpdatedDt = jsonObject1.getString("dtUpdatedDate");
                                                String strRemarks = jsonObject1.getString("cRemarks");

                                                DataMasterEntry dataMasterEntry = new DataMasterEntry(strFileId, strSrNo, strFname, strLname,
                                                        strDob, strGender, strParentName, strAddress, strCity, strState, strPincode, strMobile, strParentNo, strEmail, strRemarks, "Confirmed");
                                                arrayListMasterEntry.add(dataMasterEntry);
                                            }
                                            myAdapter = new AdapterSearchFile(arrayListMasterEntry, ClientAccount.this);

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ClientAccount.this);
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                                            mRecyclerviewTabledetails.addItemDecoration(new DividerItemDecoration(ClientAccount.this, DividerItemDecoration.VERTICAL));
                                            mRecyclerviewTabledetails.setLayoutManager(linearLayoutManager);
                                            mRecyclerviewTabledetails.setAdapter(myAdapter);
                                        }

                                    } catch (Exception e)
                                    {
                                        Toast.makeText(ClientAccount.this,"Errorcode-441 ClientAccount getAllDetailsResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            Toast.makeText(ClientAccount.this,"Errorcode-440 ClientAccount getAllDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }//getAllDetails

    public void submitCollege()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=115&CollegeId="+pos+"&CounselorID="+counselorid+"&FileNo="+fileID;
            Log.d("GetCollegeListUrl", url);
            if (CheckInternet.checkInternet(ClientAccount.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("CollegeListResponse", response);
                                try {
                                    if(response.contains("Data inserted successfully"))
                                    {
                                        linearSpinnerClg.setVisibility(View.GONE);
                                        txtSubmit.setVisibility(View.GONE);
                                        getCollegeDetails(fileID);
                                    }


                                } catch (Exception e) {
                                    Toast.makeText(ClientAccount.this,"Errorcode-443 ClientAccount submitCollegeResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            Toast.makeText(ClientAccount.this,"Errorcode-442 ClientAccount submitCollege "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//submitCollege
    public void getPaymentHistory()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=125&FileNo="+fileID;
            Log.d("PaymentHistoryUrl", url);
            if (CheckInternet.checkInternet(ClientAccount.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PaymentHistoryRes", response);
                                try {
                                  arrayListPaymentHistory=new ArrayList<>();
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
                                            paymentid= jsonObject1.getString("nPaymentID");
                                            amountINR = jsonObject1.getString("cAmountINR");
                                            amountUSD = jsonObject1.getString("cAmountUSD");
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("dtPaymentDate");
                                            String paymentDate;
                                            date1=jsonObject2.getString("date");
                                           paymentDate=date1.substring(0,date1.indexOf(" "));
                                            approved=jsonObject1.getString("IsApprovedbyAccountant");
                                            if(amountUSD.length()==0)
                                            {
                                                amountUSD="0";
                                            }
                                            if(amountINR.length()==0)
                                            {
                                                amountINR="0";
                                            }
                                            DataPaymentHistory dataPaymentHistory=new DataPaymentHistory(fileID,paymentid,amountUSD,amountINR,paymentDate,approved);
                                            arrayListPaymentHistory.add(dataPaymentHistory);
                                        }
                                        adapterPaymentHistory=new AdapterPaymentHistory(ClientAccount.this,arrayListPaymentHistory);
                                       LinearLayoutManager layoutManager=new LinearLayoutManager(ClientAccount.this);
                                       layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerPaymentHistory.setLayoutManager(layoutManager);
                                        recyclerPaymentHistory.setAdapter(adapterPaymentHistory);
                                        adapterPaymentHistory.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ClientAccount.this,"Errorcode-445 ClientAccount getPaymentHistoryResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            Toast.makeText(ClientAccount.this,"Errorcode-444 ClientAccount getPaymentHistory"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//getPaymentHistory
    public void getPackageReceivedDetail()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=122&FileNo="+fileID;
            Log.d("PckgReceivedDetailsUrl", url);
            if (CheckInternet.checkInternet(ClientAccount.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PckgReceivedDetailsRes", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                  //  dialog.dismiss();
                                    //to get detailed information about payments done by client
                                    getPaymentHistory();
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
                                        txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal=Integer.parseInt(txtIndiaOTCTotal.getText().toString());
                                        indiaOTCRecceived=Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues=indiaOTCTotal-indiaOTCRecceived;
                                        txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                        clgfeetotal=Integer.parseInt(txtCollegeFeeTotalD.getText().toString());
                                        clgfeeReceived=Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                        clgfeeDue=clgfeetotal-clgfeeReceived;
                                        txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal=Integer.parseInt(txtHostalTotalFeeD.getText().toString());
                                        hostalFeeReceived=Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue=hostalFeeTotal-hostalFeeReceived;
                                        txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal=Integer.parseInt(txtOTPTotalFeeD.getText().toString());
                                        OTPReceived=Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue=OTPTotal-OTPReceived;
                                        txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal=Integer.parseInt(txtAccidentalTotal.getText().toString());
                                        accidentalReceived=Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue=accidentalTotal-accidentalReceived;
                                        txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal=Integer.parseInt(txtMessTotal.getText().toString());
                                        messReceived=Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue=messTotal-messReceived;
                                        txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD=clgfeeReceived+hostalFeeReceived+OTPReceived+accidentalReceived+messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD=clgfeeDue+hostalFeeDue+OTPDue+accidentalDue+messDue;
                                        txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR=airfareReceived+indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR=airfareDues+indiaOTCDues;
                                        txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));
                                       // linearPackageDetails.setVisibility(View.GONE);
                                       // Toast.makeText(ClientAccount.this, "No Package Available!", Toast.LENGTH_SHORT).show();
                                    } else
                                        {
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

                                      /*  txtOTPReceivedFeeD.setText(oneTimeProcessingFeeD1);
                                        txtAccidentalReceived.setText(Accidental_Insurance);
                                        txtMessReceived.setText(MessFeeD);
                                        txtAirFareFeeReceived.setText(AirFareFee);
                                        txtIndiaOTCReceived.setText(IndiaOTC);*/

                                        airfareTotal=Integer.parseInt(txtAirFareFeeFinal.getText().toString());
                                        airfareReceived=Integer.parseInt(txtAirFareFeeReceived.getText().toString());
                                        airfareDues=airfareTotal-airfareReceived;
                                        txtAirFareFeeDues.setText(String.valueOf(airfareDues));

                                        indiaOTCTotal=Integer.parseInt(txtIndiaOTCFinal.getText().toString());
                                        indiaOTCRecceived=Integer.parseInt(txtIndiaOTCReceived.getText().toString());
                                        indiaOTCDues=indiaOTCTotal-indiaOTCRecceived;
                                        txtIndiaOTCDues.setText(String.valueOf(indiaOTCDues));

                                         clgfeetotal=Integer.parseInt(txtCollegeFeeFinal.getText().toString());
                                         clgfeeReceived=Integer.parseInt(txtCollegeFeeReceivedD.getText().toString());
                                         clgfeeDue=clgfeetotal-clgfeeReceived;
                                         txtCollegeFeeDueD.setText(String.valueOf(clgfeeDue));

                                        hostalFeeTotal=Integer.parseInt(txtHostalFinalFeeD.getText().toString());
                                        hostalFeeReceived=Integer.parseInt(txtHostalReceivedFeeD.getText().toString());
                                        hostalFeeDue=hostalFeeTotal-hostalFeeReceived;
                                        txtHostalDueFeeD.setText(String.valueOf(hostalFeeDue));

                                        OTPTotal=Integer.parseInt(txtOTPFeeFinalD.getText().toString());
                                        OTPReceived=Integer.parseInt(txtOTPReceivedFeeD.getText().toString());
                                        OTPDue=OTPTotal-OTPReceived;
                                        txtOTPDueFeeD.setText(String.valueOf(OTPDue));

                                        accidentalTotal=Integer.parseInt(txtAccidentalFinal.getText().toString());
                                        accidentalReceived=Integer.parseInt(txtAccidentalReceived.getText().toString());
                                        accidentalDue=accidentalTotal-accidentalReceived;
                                        txtAccidentalDue.setText(String.valueOf(accidentalDue));

                                        messTotal=Integer.parseInt(txtMessFinal.getText().toString());
                                        messReceived=Integer.parseInt(txtMessReceived.getText().toString());
                                        messDue=messTotal-messReceived;
                                        txtMessDue.setText(String.valueOf(messDue));

                                        totalReceivedD=clgfeeReceived+hostalFeeReceived+OTPReceived+accidentalReceived+messReceived;
                                        txtReceivedFeeD.setText(String.valueOf(totalReceivedD));

                                        totalDueD=clgfeeDue+hostalFeeDue+OTPDue+accidentalDue+messDue;
                                        txtDueFeeD.setText(String.valueOf(totalDueD));

                                        totalReceivedR=airfareReceived+indiaOTCRecceived;
                                        txtTotalFeeReceivedR.setText(String.valueOf(totalReceivedR));

                                        totalDuesR=airfareDues+indiaOTCDues;
                                        txtTotalFeeDuesR.setText(String.valueOf(totalDuesR));

                                      /*  totalfeeReceived=Integer.parseInt(txtTotalFeeD.getText().toString());
                                        totalReceived=Integer.parseInt(txtReceivedFeeD.getText().toString());
                                        totalDue=totalfeeReceived-totalReceived;
                                        txtDueFeeD.setText(String.valueOf(totalDue));*/
                                      Log.d("txtReceivedVal",txtCollegeFeeReceivedD.getText().toString());


                                         /* txtTotalFeeD.setText(totalFeeYearD);
                                            txtAirFareFeeR.setText("AirFareFeeR : "+airFareFeeR);
                                               txtIndiaOTC.setText(otherFee3Head+" : "+otherFee3D);
                                          txtTotalFeeYearR.setText("TotalFeeYearR :"+totalFeeYearR);*/
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ClientAccount.this,"Errorcode-447 ClientAccount PackageReceivedDetailsResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            Toast.makeText(ClientAccount.this,"Errorcode-446 ClientAccount getPackageReceivedDetails"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//getPackageReceivedDetails
    public void getPackageDetails()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=116&FileNo="+fileID;
            Log.d("PackageDetailsUrl", url);
            if (CheckInternet.checkInternet(ClientAccount.this)) {
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
                                    //to get total amount received in USD and INR
                                      getPackageTotalDetails();

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ClientAccount.this,"Errorcode-449 ClientAccount getPackageDetailsResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            Toast.makeText(ClientAccount.this,"Errorcode-448 ClientAccount getPackageDetails"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//getPackageDetails
    public void getPackageTotalDetails()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=121&FileNo="+fileID;
            Log.d("PackageTotalReceivedUrl", url);
            if (CheckInternet.checkInternet(ClientAccount.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PackageReceivedResponse", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                   // dialog.dismiss();
                                    //to get details of package received from client to user
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
                                             inr= jsonObject1.getString("INR");
                                            usd = jsonObject1.getString("USD");
                                        }
                                        txtTotalRecivedUSD.setText("Amount Received in USD:"+usd);
                                        txtTotalRecivedINR.setText("Amount Received in INR:"+inr);




                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ClientAccount.this,"Errorcode-451 ClientAccount PackageTotalDetailsResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            Toast.makeText(ClientAccount.this,"Errorcode-450 ClientAccount getPackageTotalDetails"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//getPackageTotalDetails
    public void getCollegeList()
    {
        try {
               url = clienturl + "?clientid=" + clientid + "&caseid=114";
                Log.d("GetCollegeListUrl", url);
                if (CheckInternet.checkInternet(ClientAccount.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("CollegeListResponse", response);
                                    try {
                                        arrayListCollege = new ArrayList<>();
                                        arrayListClgId = new ArrayList<>();
                                        dialog.dismiss();
                                        if (response.contains("[]")) {
                                            Toast.makeText(ClientAccount.this, "No Colleges Available!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String clgid = jsonObject1.getString("nCollegeID");
                                                String clgname = jsonObject1.getString("cCollegeName");

                                                arrayListClgId.add(clgid);
                                                arrayListCollege.add(clgname);
                                            }

                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(ClientAccount.this, R.layout.spinner_item1, arrayListCollege);
                                            //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spinnerCollegeList.setAdapter(arrayAdapter);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(ClientAccount.this,"Errorcode-453 ClientAccount getCollegeListResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                Toast.makeText(ClientAccount.this,"Errorcode-452 ClientAccount getCollegeList"+e.toString(),Toast.LENGTH_SHORT).show();
                Log.d("ExcClgList",e.toString());
            }
    }//getCollegeList
    public void searchAsItemSelected()
    {
        try {
            arrayListMasterEntry = new ArrayList<>();
            searchAs = mSearchspinner.getSelectedItem().toString();
            if (searchAs.contains("First Name")) {
                searchAs = "cCandidateFName";
            } else if (searchAs.contains("Serial No")) {
                searchAs = "nSrNo";
            } else if (searchAs.contains("Last Name")) {
                searchAs = "cCandidateLName";
            } else if (searchAs.contains("Parent Name")) {
                searchAs = "cParentName";
            } else if (searchAs.contains("Parent No")) {
                searchAs = "cParantNo";
            } else if (searchAs.contains("City")) {
                searchAs = "cCity";
            } else if (searchAs.contains("State")) {
                searchAs = "cState";
            } else if (searchAs.contains("Pin Code")) {
                searchAs = "cPinCode ";
            } else if (searchAs.contains("Mobile No")) {
                searchAs = "cMobile ";
            } else if (searchAs.contains("EmailID")) {
                searchAs = "cEmail";
            } else if (searchAs.contains("File ID")) {
                searchAs = "nFileID";
            } else if (searchAs.contains("Pan No")) {
                searchAs = "cPanNo";
            } else if (searchAs.contains("Passport No")) {
                searchAs = "cPassportNo";
            } else if (searchAs.contains("Aadhaar No")) {
                searchAs = "cAadhaarNo";
            }
        }catch (Exception e)
        {
            Toast.makeText(ClientAccount.this,"Errorcode-454 ClientAccount searchAsItemSelected"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//seachAsItemSelected
    public void getCollegeDetails(String fileID) {
        try {
            arrayListMasterEntry = new ArrayList<>();
            url = clienturl + "?clientid=" + clientid+"&caseid=113&CounselorID=" + counselorid+"&FileNo="+fileID+"&Step=0";
            Log.d("CollegeDetailsUrl", url);
            if (CheckInternet.checkInternet(ClientAccount.this)) {
                if (CheckServer.isServerReachable(ClientAccount.this)) {

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
                                           fab.setVisibility(View.GONE);
                                            dialog.dismiss();
                                           linearPackageDetails.setVisibility(View.GONE);
                                           linearPaymentHistory.setVisibility(View.GONE);
                                            txtClgInfoTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_white_24dp, 0);
                                           linearClgName.setVisibility(View.GONE);
                                        }else
                                        {
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
                                            //to get details of  package for allocated college
                                            getPackageDetails();

                                            fab.setVisibility(View.VISIBLE);

                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(ClientAccount.this,"Errorcode-456 ClientAccount getCollegeDetailsResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            Toast.makeText(ClientAccount.this,"Errorcode-455 ClientAccount getCollegeDetails"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//getCollegeDetails
    public void searchDetails(String searchtext) {
        try {
            arrayListMasterEntry = new ArrayList<>();
            url = clienturl + "?clientid=" + clientid+"&caseid=79&SearchAs=" +searchAs+ "&SearchVal="+ searchtext + "&CounselorID=" + counselorid+"&Step=0";
            Log.d("SearchDetailsUrl", url);
            if (CheckInternet.checkInternet(ClientAccount.this)) {
                if (CheckServer.isServerReachable(ClientAccount.this)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("SearchDetailsResponse", response);
                                        dialog.dismiss();
                                        if (response.contains("[]")) {
                                            linearTableColumn.setVisibility(View.GONE);
                                            horizontalScrollView.setVisibility(View.GONE);
                                            txtNoResult.setVisibility(View.VISIBLE);
                                        } else {
                                            horizontalScrollView.setVisibility(View.VISIBLE);
                                            txtNoResult.setVisibility(View.GONE);
                                            linearTableColumn.setVisibility(View.VISIBLE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                strFileId = jsonObject1.getString("nFileID");
                                                strSrNo = jsonObject1.getString("nSrNo");
                                                strAllocatedTo = jsonObject1.getString("AllocatedTo");
                                                strFname = jsonObject1.getString("cCandidateFName");
                                                strLname = jsonObject1.getString("cCandidateLName");
                                                JSONObject jsonObject2 = jsonObject1.getJSONObject("dtDOB");
                                                strDob = jsonObject2.getString("date");
                                                String strGender = jsonObject1.getString("cGender");
                                                String strParentName = jsonObject1.getString("cParentName");
                                                String strPanNo = jsonObject1.getString("cPanNo");
                                                String strAdharNo = jsonObject1.getString("cAadhaarNo");
                                                String strPassportNo = jsonObject1.getString("cPassportNo");
                                                String strMobile = jsonObject1.getString("cMobile");
                                                String strParentNo = jsonObject1.getString("cParantNo");
                                                String strEmail = jsonObject1.getString("cEmail");
                                                String strPincode = jsonObject1.getString("cPinCode");
                                                String strAddress = jsonObject1.getString("cAddressLine");
                                                String strCity = jsonObject1.getString("cCity");
                                                String strState = jsonObject1.getString("cState");
                                                String strCreatedDt = jsonObject1.getString("dtCreatedDate");
                                                String strUpdatedDt = jsonObject1.getString("dtUpdatedDate");
                                                String strRemarks = jsonObject1.getString("cRemarks");

                                                DataMasterEntry dataMasterEntry = new DataMasterEntry(strFileId, strSrNo, strFname, strLname,
                                                        strDob, strGender, strParentName, strAddress, strCity, strState, strPincode, strMobile, strParentNo, strEmail, strRemarks, "Confirmed");
                                                arrayListMasterEntry.add(dataMasterEntry);
                                            }
                                            myAdapter = new AdapterSearchFile(arrayListMasterEntry, ClientAccount.this);

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ClientAccount.this);
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                                            mRecyclerviewTabledetails.addItemDecoration(new DividerItemDecoration(ClientAccount.this, DividerItemDecoration.VERTICAL));
                                            mRecyclerviewTabledetails.setLayoutManager(linearLayoutManager);
                                            mRecyclerviewTabledetails.setAdapter(myAdapter);
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(ClientAccount.this,"Errorcode-458 ClientAccount searchDetailsResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            Toast.makeText(ClientAccount.this,"Errorcode-457 ClientAccount searchDetails"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//searchDetails
    public void getFileInfo()
    {
        if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("0")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
        } else if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("1")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            dialog = ProgressDialog.show(ClientAccount.this, "", "Loading details", true);
            newThreadInitilization(dialog);
            //to get details of selected file id
            getDetails(fileID);
        }
    }//getFileInfo

    public void btnGetDetailsClicked()
    {
        try {
            fileID = edtFileNo.getText().toString();
            if (fileID.length() == 0) {
                edtFileNo.setError("Invalid file no");
            } else {
                if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                } else if (CheckInternetSpeed.checkInternet(ClientAccount.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
                    dialog = ProgressDialog.show(ClientAccount.this, "", "Loading details", true);
                    newThreadInitilization(dialog);
                    getDetails(fileID);
                }
            }
        }catch (Exception e)
        {
            Toast.makeText(ClientAccount.this,"Errorcode-459 ClientAccount btnGetDetailsClicked"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//btnGetDetailsClicked

    private void getDetails(final String fileid)
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid=98&FileID="+fileid + "&CounselorID=" + counselorid+"&Step=0";
            Log.d("DetailsUrl", url);
            if(CheckInternet.checkInternet(ClientAccount.this))
            {
                if(CheckServer.isServerReachable(ClientAccount.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{

                                        Log.d("DetailsResponse1", response);
                                        if(response.contains("[]"))
                                        {
                                            dialog.dismiss();
                                           // linearName.setVisibility(View.GONE);
                                            Toast.makeText(ClientAccount.this,"File not exist",Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                fname = jsonObject1.getString("cCandidateFName");
                                                lname = jsonObject1.getString("cCandidateLName");
                                                mobile=jsonObject1.getString("cMobile");
                                                passport=jsonObject1.getString("cPassportNo");
                                                panno=jsonObject1.getString("cPanNo");
                                            }
                                            //to get details of college allocated or not
                                            getCollegeDetails(fileid);
                                            linearPersonalInfo.setVisibility(View.VISIBLE);
                                            /*linearName.setVisibility(View.VISIBLE);
                                            edtName.setText(fname + " " + lname);*/
                                            txtName.setText(fname+" "+lname);
                                            txtFileID.setText(fileID+".");
                                            if(mobile.length()==0)
                                            {
                                                txtMobile.setText("NA");
                                            }
                                            else {
                                                txtMobile.setText(mobile);
                                            }
                                            if(passport.length()==0)
                                            {
                                                txtPassportNo.setText("NA");
                                            }
                                            else {
                                                txtPassportNo.setText(passport);
                                            }
                                            if(panno.length()==0){
                                                txtPanNo.setText("NA");
                                            }else
                                                {
                                                    txtPanNo.setText(panno);
                                                }
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(ClientAccount.this, "Errorcode-189 CounselorContact counselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ClientAccount.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ClientAccount.this);
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
            //dialog.dismiss();
            Toast.makeText(ClientAccount.this,"Errorcode-460 ClientAccount getDetails"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }//getDetails
}
