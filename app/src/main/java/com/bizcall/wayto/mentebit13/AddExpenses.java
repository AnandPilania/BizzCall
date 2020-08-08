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
import android.os.Vibrator;
import android.provider.MediaStore;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Date;
import java.util.Locale;

public class AddExpenses extends AppCompatActivity {
    private Spinner mSearchspinner;
    private LinearLayout linearLayout;
    private RecyclerView mRecyclerviewTabledetails;
    HorizontalScrollView horizontalScrollView;
    TextView txtNoResult;

    private ArrayList<DataMasterEntry> arrayListMasterEntry;
    private AdapterSearchFile myAdapter;
    private DataMasterEntry dataMasterEntry;
    Button btnAllDetails;

    private TextView txtTitle,txtSpinnerError;
    private EditText edtSearchText;
    private String counselorid,clienturl,clientid,url;
    String strFileId,strSrNo,strAllocatedTo,strFname, strLname,strDob,searchAs,result;
    ProgressDialog dialog;
    RequestQueue requestQueue;
    Button btnSearch;
    Vibrator vibrator;
    LinearLayout linearTableColumn;

    private String [] mSearchMethod = {"Select", "File ID", "Serial No","First Name", "Last Name","Parent Name","Pan No","Aadhaar No", "Passport No","Mobile No","Parent No","EmailID","City","State","Pin Code"};
    ImageView imgClose;

    Spinner spinnerAmountType,spinnerCategory,spinnerCounselor,spinnerCategoryType;
    TextView txtDate,btnGetDetails,txtSearchFile;
    EditText edtAmount,edtFileNo,edtMemo,edtName,edtCollege;
    CheckBox checkBoxCounselor,checkBoxFileNo;
    Button btnOk;
    AlertDialog alertDialog1;
    int flag=0,temp=0;
    SharedPreferences sp;
    ImageView imgCalender,imgBack,imgPath,imgReceipt;
    String categoryval;

    ArrayList<DataAmountType> arrayListAmountType;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListCategoryIdExpense;
    ArrayList<String> arrayListCategoryIdInward;
    ArrayList<String> arrayListExpenses;
    ArrayList<String> arrayListInward;
    ArrayList<DataCategory> arrayListCategory;
    ArrayList<String> arrayListAmount;
    ArrayList<String> arrayListAmountID;
    DataCategory dataCategory;
    LinearLayout linearCounselor,linearFileNo,linearCalender,linearName;
    DataListCounselor dataListCounselor;
    ArrayList<DataListCounselor> arrayListCounselorDetails;
    ArrayList<String> arrayListCounselorName;
    ArrayList<String> arrayListCounselorId;
    String counselorID="",amountID="",categoryID="",fname="",lname="",amount,fileID,name,memo,date1,receiptfilename,msg,cTime;
    int year, day, month;
    Calendar myCalendar;
    public static final int GET_FROM_GALLERY = 1;
    String uploadFilePath = "";
    String uploadFileName = "",urlSMSDate;
    String upLoadServerUri = null;
    int serverResponseCode = 0;
    Date dt1;
    Thread thread;
    long timeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses2);
        try {
           initialize();

            if (CheckInternetSpeed.checkInternet(AddExpenses.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
            } else if (CheckInternetSpeed.checkInternet(AddExpenses.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                dialog = ProgressDialog.show(AddExpenses.this, "", "Loading amount type", true);
                newThreadInitilization(dialog);
                //to get list of counselors when payment to be assign to counselor
                getCounselorDetails();
            }
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            btnGetDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to get details of file that is assigned to whom to assign payment to that file
                    btnGetDetailsClicked();
                }
            });
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnOkClicked();
                }
            });
            txtSearchFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //alert to search file number from masterentry
                    showAlert();

                }
            });
            checkBoxCounselor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (linearCounselor.getVisibility() == View.GONE) {
                        linearCounselor.setVisibility(View.VISIBLE);
                    } else {
                        linearCounselor.setVisibility(View.GONE);
                    }
                }
            });
            checkBoxFileNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (linearFileNo.getVisibility() == View.GONE) {
                        linearFileNo.setVisibility(View.VISIBLE);
                    } else {
                        linearFileNo.setVisibility(View.GONE);
                    }
                }
            });
            spinnerCounselor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    counselorID = arrayListCounselorId.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spinnerCategory.getSelectedItemPosition() == 0)//expenses
                    {
                        checkBoxCounselor.setVisibility(View.VISIBLE);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(AddExpenses.this, R.layout.spinner_item1, arrayListExpenses);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategoryType.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();

                    } else//inward
                        {
                        checkBoxCounselor.setVisibility(View.GONE);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(AddExpenses.this, R.layout.spinner_item1, arrayListInward);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategoryType.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinnerCategory.setSelection(0);
                }
            });
            spinnerCategoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerCategory.getSelectedItemPosition() == 0) {
                        categoryID = arrayListCategoryIdExpense.get(position);
                    } else {
                        categoryID = arrayListCategoryIdInward.get(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerAmountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    amountID = arrayListAmountID.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinnerAmountType.setSelection(0);
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
                            txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    };
                    DatePickerDialog dpDialog = new DatePickerDialog(AddExpenses.this, listener, year, month, day);
                    //myCalendar.add(Calendar.YEAR);
                    // dpDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                    dpDialog.show();

                    //  showDialog(999);
                }
            });
            imgPath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(AddExpenses.this,"Errorcode-418 AddExpenses onCreate"+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }//onCreate

    private void initialize() {
        myCalendar = Calendar.getInstance();
        requestQueue = Volley.newRequestQueue(AddExpenses.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clienturl = sp.getString("ClientUrl", "");
        clientid = sp.getString("ClientId", "");
        timeout = sp.getLong("TimeOut", 0);
        counselorid = sp.getString("Id", "");
        counselorid = counselorid.replace(" ", "");
        spinnerCategoryType = findViewById(R.id.spinnerCategoryType);
        spinnerAmountType = findViewById(R.id.spinnerAmountType);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCounselor = findViewById(R.id.spinnerCounselor);
        edtName = findViewById(R.id.txtName);
        edtCollege = findViewById(R.id.txtCollege);
        txtDate = findViewById(R.id.txtSelectedDate);
        edtAmount = findViewById(R.id.edtAmount);
        edtFileNo = findViewById(R.id.edtFileNo);
        edtMemo = findViewById(R.id.edtMemo);
        checkBoxCounselor = findViewById(R.id.checkCounselor);
        checkBoxFileNo = findViewById(R.id.checkFile);
        imgCalender = findViewById(R.id.imgCalender);
        imgBack = findViewById(R.id.img_back);
        btnOk = findViewById(R.id.btnOk);
        linearCounselor = findViewById(R.id.linearCounselor);
        linearFileNo = findViewById(R.id.linearFileNo);
        linearCalender = findViewById(R.id.linearCalender);
        linearName = findViewById(R.id.linearName);
        btnGetDetails = findViewById(R.id.txtGetDetails);
        imgPath = findViewById(R.id.imgDoc);
        imgReceipt = findViewById(R.id.imgReceipt);
        txtSearchFile = findViewById(R.id.txtSearch);
        arrayList = new ArrayList<>();
        arrayListExpenses = new ArrayList<>();
        arrayListInward = new ArrayList<>();
        arrayListCategory = new ArrayList<>();
        arrayListCategoryIdExpense = new ArrayList<>();
        arrayListCategoryIdInward = new ArrayList<>();
        arrayListCounselorId = new ArrayList<>();
        arrayListCounselorName = new ArrayList<>();
        arrayList.add(0, "Expenses");
        arrayList.add(1, "Inward");

        myCalendar = Calendar.getInstance();

        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(AddExpenses.this, R.layout.spinner_item1, arrayList);
        // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(arrayAdapter);
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
                                Toast.makeText(AddExpenses.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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
   public void showAlert()
   {
       try {
           LayoutInflater li = LayoutInflater.from(AddExpenses.this);
           //Creating a view to get the dialog box
           View view = li.inflate(R.layout.activity_search_file, null);
           horizontalScrollView = view.findViewById(R.id.search_horizontalscroll);
           txtNoResult = view.findViewById(R.id.txtNoResultFound);
           linearTableColumn = view.findViewById(R.id.linearTableColumn);
           // imgBack = findViewById(R.id.img_back);
           imgClose = view.findViewById(R.id.imgClose);
           mSearchspinner = view.findViewById(R.id.search_spinner);
           btnSearch = view.findViewById(R.id.btn_search);
           linearLayout = view.findViewById(R.id.searchby_layout);
           edtSearchText = view.findViewById(R.id.edtSearchtext);
           txtTitle = view.findViewById(R.id.txtTitle);
           txtSpinnerError = view.findViewById(R.id.txtSpinnerError);
           mRecyclerviewTabledetails = view.findViewById(R.id.detail_recyclerview);

           final AlertDialog.Builder alert = new AlertDialog.Builder(AddExpenses.this);


           //Adding our dialog box to the view of alert dialog
           alert.setView(view);
           //Creating an alert dialog
           alertDialog1 = alert.create();
           alertDialog1.show();
           alertDialog1.setCancelable(false);

           ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(AddExpenses.this,
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

           btnSearch.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String searchtext = edtSearchText.getText().toString();
                   String searchAs = mSearchspinner.getSelectedItem().toString();
                   if (searchAs.contains("Select")) {
                       txtSpinnerError.setVisibility(View.VISIBLE);
                   } else {
                       txtSpinnerError.setVisibility(View.GONE);
                       if (CheckInternetSpeed.checkInternet(AddExpenses.this).contains("0")) {
                           android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                       } else if (CheckInternetSpeed.checkInternet(AddExpenses.this).contains("1")) {
                           android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                               dialog = ProgressDialog.show(AddExpenses.this, "", "Searching data", true);
                               newThreadInitilization(dialog);
                               searchDetails(searchtext);
                           } else {
                               edtSearchText.setError("Enter text to search");
                           }
                       }
                   }
               }
           });
       }catch (Exception e)
       {
           Toast.makeText(AddExpenses.this,"Errorcode-419 AddExpenses showAlert"+e.toString(),Toast.LENGTH_SHORT).show();
       }
   }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent intent=new Intent(AddExpenses.this,AccountsActivity.class);
        startActivity(intent);
        finish();
    }
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
            Toast.makeText(AddExpenses.this,"Errorcode-420 AddExpenses searchAsItemSelected "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void searchDetails(String searchtext)
    {
        try {
            arrayListMasterEntry = new ArrayList<>();
            url = clienturl + "?clientid=" + clientid + "&caseid=79&SearchAs=" + searchAs + "&SearchVal=" + searchtext + "&CounselorID=" + counselorid+"&Step=0";
            Log.d("SearchDetailsUrl", url);
            if (CheckInternet.checkInternet(AddExpenses.this)) {
                if(CheckServer.isServerReachable(AddExpenses.this)) {

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
                                            myAdapter = new AdapterSearchFile(arrayListMasterEntry, AddExpenses.this);

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddExpenses.this);
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                                            mRecyclerviewTabledetails.addItemDecoration(new DividerItemDecoration(AddExpenses.this, DividerItemDecoration.VERTICAL));
                                            mRecyclerviewTabledetails.setLayoutManager(linearLayoutManager);
                                            mRecyclerviewTabledetails.setAdapter(myAdapter);
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(AddExpenses.this, "Errorcode-421 AddExpenses searchDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(AddExpenses.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
            Toast.makeText(AddExpenses.this,"Errorcode-422 AddExpenses searchDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }
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
            }
        catch (Exception e)
        {
            Toast.makeText(AddExpenses.this,"Errorcode-423 AddExpenses onActivtyResult "+e.toString(),Toast.LENGTH_SHORT).show();
        }
        }
    }
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
    }

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
                    Toast.makeText(AddExpenses.this, "Source File not exist :"
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
                        receiptfilename = counselorid + "_" + categoryval + "_" + categoryID + "_" + date1 + "_" + cTime;
                        addExpenses(receiptfilename);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AddExpenses.this, "File Upload Complete.",
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
                        Toast.makeText(AddExpenses.this, "MalformedURLException",
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
                        Toast.makeText(AddExpenses.this, "Got Exception while uploading record ",
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
    public void btnOkClicked()
    {
        try {
            flag = 0;
            if (categoryID.equals("0")) {
                flag = 1;
                Toast.makeText(AddExpenses.this, "Select category", Toast.LENGTH_SHORT).show();
            }
            amount = edtAmount.getText().toString();
            if (amount.length() == 0) {
                edtAmount.setError("Invalid amount");
                flag = 1;
            }
            if (checkBoxCounselor.isChecked()) {
                if (counselorID.contains("0")) {
                    flag = 1;
                    Toast.makeText(AddExpenses.this, "Select counselor", Toast.LENGTH_SHORT).show();
                }
            } else {
                counselorID = "0";
            }
            if (checkBoxFileNo.isChecked()) {
                if (edtName.getText().toString().length() == 0) {
                    flag = 1;
                    Toast.makeText(AddExpenses.this, "Select file no", Toast.LENGTH_SHORT).show();
                } else {
                    name = edtName.getText().toString();
                }
            } else {
                name = "NA";
                fileID = "0";
            }
            memo = edtMemo.getText().toString();
            if (memo.length() == 0) {
                flag = 1;
                edtMemo.setError("Invalid memo");
            }
            if (spinnerCategory.getSelectedItemPosition() == 0) {
                categoryval = "1";
            } else {
                categoryval = "2";
            }
            date1 = txtDate.getText().toString();
            if (date1.contains("Select Date")) {
                flag = 1;
                Toast.makeText(AddExpenses.this, "Select date", Toast.LENGTH_SHORT).show();
            }
            receiptfilename = "NA";
            if (flag == 0) {
                if (!checkBoxCounselor.isChecked()) {
                    msg = amount + " " + spinnerAmountType.getSelectedItem().toString() + " for " + spinnerCategoryType.getSelectedItem().toString() + " has been paid on " + name + "'s account, remarks are " + memo + " on date " + date1;
                }
                if (!checkBoxFileNo.isChecked()) {
                    msg = amount + " " + spinnerAmountType.getSelectedItem().toString() + " for " + spinnerCategoryType.getSelectedItem().toString() + " has been paid to " + spinnerCounselor.getSelectedItem().toString() +
                            " ,remarks are " + memo + " on date " + date1;
                }
                if (!checkBoxCounselor.isChecked() && !checkBoxFileNo.isChecked()) {
                    msg = amount + " " + spinnerAmountType.getSelectedItem().toString() + " for " + spinnerCategoryType.getSelectedItem().toString() + " has been paid, remarks are " + memo + " on date " + date1;
                }
                if (checkBoxCounselor.isChecked() && checkBoxFileNo.isChecked()) {
                    msg = amount + " " + spinnerAmountType.getSelectedItem().toString() + " for " + spinnerCategoryType.getSelectedItem().toString() + " has been paid to " + spinnerCounselor.getSelectedItem().toString() +
                            " on " + name + "'s account, remarks are " + memo + " on date " + date1;
                }

                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
                alertDialogBuilder.setMessage(msg)
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int which) {
                                //insertIMEI();
                                dialog1.dismiss();

                                cTime = new CommonMethods().getTIme();
                                cTime = cTime.replaceAll(" ", "");
                           /* dt1=myCalendar.getTime();
                            cTime= String.valueOf(dt1);*/
                                Log.d("TIME!", cTime);
                                upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=100&CounselorID=" + counselorid + "&Category=" + categoryval + "&CategoryID=" + categoryID +
                                        "&Date1=" + date1 + "_" + cTime;
                                if (temp == 1) {
                                    if (CheckInternetSpeed.checkInternet(AddExpenses.this).contains("0")) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                                    } else if (CheckInternetSpeed.checkInternet(AddExpenses.this).contains("1")) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                                        dialog = ProgressDialog.show(AddExpenses.this, "", "Adding expenses", true);
                                        newThreadInitilization(dialog);
                                        new Thread(new Runnable() {
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //upload selected receipt image to server
                                                uploadFile(uploadFilePath + "" + uploadFileName);
                                                // insertCallInfo();

                                            }
                                        }).start();
                                    }
                                } else {
                                    if (CheckInternetSpeed.checkInternet(AddExpenses.this).contains("0")) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                                    } else if (CheckInternetSpeed.checkInternet(AddExpenses.this).contains("1")) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                                        dialog = ProgressDialog.show(AddExpenses.this, "", "Adding expenses", true);
                                        newThreadInitilization(dialog);
                                        //to insert expenses or inwards to database
                                        addExpenses("NA");
                                    }
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
        } catch (Exception e)
        {
            Toast.makeText(AddExpenses.this,"Errorcode-424 AddExpenses btnOKClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    private void addExpenses(String receiptfilename) {

        try {
                String url=clienturl+"?clientid=" + clientid + "&caseid=99&AccountCategoryID="+categoryID+"&cmemo="+memo
                +"&cAmount="+amount+"&cAmountType="+amountID+"&nCounselorID="+counselorid+"&ExpenseDate="+date1+"&ReceiptFileName="
                +receiptfilename+"&toCounselorID="+counselorID+"&FileNo="+fileID;
                Log.d("AddExpenseUrl", url);
                if(CheckInternet.checkInternet(AddExpenses.this))
                {
                    if(CheckServer.isServerReachable(AddExpenses.this)) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try{
                                            dialog.dismiss();
                                            Log.d("AddExpenseResponse1", response);
                                            if(response.contains("Data inserted successfully"))
                                            {
                                                Intent intent=new Intent(AddExpenses.this,AccountsActivity.class);
                                                startActivity(intent);
                                               Toast.makeText(AddExpenses.this,"Added expenses",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(AddExpenses.this,"Insertion failed",Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (Exception e) {
                                            Toast.makeText(AddExpenses.this,"Errorcode-426 AddExpenses addExpensesResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
                                            alertDialogBuilder.setTitle("Network issue!!!")

                                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            dialog.dismiss();
                                                        }
                                                    }).show();
                                            dialog.dismiss();
                                            Toast.makeText(AddExpenses.this, "Network issue", Toast.LENGTH_SHORT).show();
                                            // showCustomPopupMenu();
                                            Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                        }
                                    }
                                });
                        requestQueue.add(stringRequest);
                    }else
                    {
                        dialog.dismiss();
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                dialog.dismiss();
                Toast.makeText(AddExpenses.this,"Errorcode-425 AddExpenses addExpenses "+e.toString(),Toast.LENGTH_SHORT).show();
               Log.d("ExcCounselorDetails", String.valueOf(e));
            }
    }//addExpenses
    public void btnGetDetailsClicked()
    {
        try {
            fileID = edtFileNo.getText().toString();
            if (fileID.length() == 0) {
                edtFileNo.setError("Invalid file no");
            } else {
                dialog = ProgressDialog.show(AddExpenses.this, "", "Loading details", true);
                newThreadInitilization(dialog);
                getDetails(fileID);
            }
        }catch (Exception e)
        {
            Toast.makeText(AddExpenses.this,"Errorcode-427 AddExpenses btnGetDetailsClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//btnGetDetailsClicked
    private void getDetails(String fileid)
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid=98&FileID="+fileid + "&CounselorID=" + counselorid+"&Step=0";
                Log.d("DetailsUrl", url);
                if(CheckInternet.checkInternet(AddExpenses.this))
                {
                    if(CheckServer.isServerReachable(AddExpenses.this)) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try{
                                        dialog.dismiss();
                                        Log.d("DetailsResponse1", response);
                                            if(response.contains("[]"))
                                            {
                                                linearName.setVisibility(View.GONE);
                                                Toast.makeText(AddExpenses.this,"File not exist",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                JSONObject jsonObject = new JSONObject(response);
                                                Log.d("Json", jsonObject.toString());
                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    fname = jsonObject1.getString("cCandidateFName");
                                                    lname = jsonObject1.getString("cCandidateLName");
                                                }
                                                linearName.setVisibility(View.VISIBLE);
                                                edtName.setText(fname + " " + lname);
                                            }


                                        } catch (JSONException e) {
                                            Toast.makeText(AddExpenses.this,"Errorcode-429 AddExpenses getDetailsResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            Log.d("GetDetailExceptio", String.valueOf(e));
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
                                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
                                            alertDialogBuilder.setTitle("Network issue!!!")

                                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            dialog.dismiss();
                                                        }
                                                    }).show();
                                            dialog.dismiss();
                                            Toast.makeText(AddExpenses.this, "Network issue", Toast.LENGTH_SHORT).show();
                                            // showCustomPopupMenu();
                                            Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                        }
                                    }
                                });
                        requestQueue.add(stringRequest);
                    }else
                    {
                        dialog.dismiss();
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                dialog.dismiss();
                Toast.makeText(AddExpenses.this,"Errorcode-428 AddExpenses getDetails "+e.toString(),Toast.LENGTH_SHORT).show();
                Log.d("ExcGetDetails", String.valueOf(e));
            }
    }//getDetails

   /* private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    try {

                myCalendar = Calendar.getInstance();
                        //arg0.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        myCalendar.set(Calendar.YEAR, arg1);
                        myCalendar.set(Calendar.MONTH, arg2);
                        myCalendar.set(Calendar.DAY_OF_MONTH, arg3);
                        String myFormat = "yyyy/MM/dd"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        txtDate.setText(sdf.format(myCalendar.getTime()));
                        day = arg1;
                        month = arg2;
                        year = arg3;
                        showDate(day, month + 1, year);
                    } catch (Exception e) {
                        Toast.makeText(AddExpenses.this,"Errorcode-300 NewClientEntry DataPickerDialog "+e.toString(),Toast.LENGTH_SHORT).show();
                        Log.d("Exception", String.valueOf(e));
                    }
                }
            };*/

   /* public void setDate(View view) {
        try{
            showDialog(999);
            Toast.makeText(getApplicationContext(), "ca",
                    Toast.LENGTH_SHORT)
                    .show();
        }catch (Exception e)
        {
            Toast.makeText(AddExpenses.this,"Errorcode-301 NewClientEntry setDate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        try{
            if (id == 999) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        myDateListener, year, month, day);
                myCalendar.add(Calendar.YEAR, -19);
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());

                               return datePickerDialog;
            }
        }catch (Exception e)
        {
            Toast.makeText(AddExpenses.this,"Errorcode-302 NewClientEntry createDialog "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        try{
            txtDate.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }catch (Exception e)
        {
            Log.d("Exception", String.valueOf(e));
        }
    }*/
    public void getCounselorDetails() {
        try {
            arrayListCounselorDetails=new ArrayList<>();
            arrayListCounselorName.add(0,"Select Counselor");
            arrayListCounselorId.add(0,"0");
           String url=clienturl+"?clientid=" + clientid + "&caseid=30";
            Log.d("CounselorUrl", url);
            if(CheckInternet.checkInternet(AddExpenses.this))
            {
                if(CheckServer.isServerReachable(AddExpenses.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("CounselorResponse1", response);
                                    //get amount type i.e INR USD to set on dropdown
                                    getAmountType();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String id = String.valueOf(jsonObject1.getInt("cCounselorID"));
                                            String name = jsonObject1.getString("cCounselorName");
                                            dataListCounselor = new DataListCounselor(id, name);
                                            arrayListCounselorDetails.add(dataListCounselor);
                                        }
                                        for(int i=0;i<arrayListCounselorDetails.size();i++)
                                        {
                                            arrayListCounselorName.add(arrayListCounselorDetails.get(i).getCounselorname());
                                            arrayListCounselorId.add(arrayListCounselorDetails.get(i).getCounselorid());
                                        }
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(AddExpenses.this, R.layout.spinner_item1, arrayListCounselorName);
                                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerCounselor.setAdapter(arrayAdapter);
                                        arrayAdapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        Toast.makeText(AddExpenses.this,"Errorcode-431 AddExpenses counselorDetailsResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(AddExpenses.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
            Toast.makeText(AddExpenses.this,"Errorcode-430 AddExpenses getCounselorDetails "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }//getCounselorDetails
    public void getAmountType() {
        try {
            if(CheckServer.isServerReachable(AddExpenses.this)) {
                String url = clienturl + "?clientid=" + clientid +"&caseid=A16";
                Log.d("AmountTypeUrl", url);
                arrayListAmountType=new ArrayList<>();
                arrayListAmount=new ArrayList<>();
                arrayListAmountID=new ArrayList<>();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("AmountTypeResponse1", response);
                                    dialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String amounttypeid=jsonObject1.getString("nAmountTypeid");
                                        String amountType=jsonObject1.getString("cAmountType");
                                        DataAmountType dataAmountType=new DataAmountType(amounttypeid,amountType);
                                        arrayListAmountType.add(dataAmountType);
                                    }
                                    for(int i=0;i<arrayListAmountType.size();i++)
                                    {
                                        arrayListAmount.add(arrayListAmountType.get(i).getAmounttype());
                                        arrayListAmountID.add(arrayListAmountType.get(i).getAmounttypeid());
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter(AddExpenses.this, R.layout.spinner_item1, arrayListAmount);
                                    // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerAmountType.setAdapter(arrayAdapter);
                                    arrayAdapter.notifyDataSetChanged();
                                    if(spinnerAmountType.getSelectedItemPosition()==0)
                                    {
                                        amountID=arrayListAmountID.get(0);
                                    }

                                    getCategoryType();

                                } catch (JSONException e) {
                                    Toast.makeText(AddExpenses.this,"Errorcode-433 AddExpenses getAmountTypeResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(AddExpenses.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
        }catch (Exception e)
        {
            Toast.makeText(AddExpenses.this,"Errorcode-432 AddExpenses getAmountType "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcStatus", String.valueOf(e));
        }
    }//getAmountType
    public void getCategoryType() {
        try {
            if(CheckServer.isServerReachable(AddExpenses.this)) {
                String url = clienturl + "?clientid=" + clientid +"&caseid=A12";
                Log.d("CategoryUrl", url);
                //arrayListCategory=new ArrayList<>();
                /*arrayListCategory.clear();
                arrayListInward.clear();
                arrayListExpenses.clear();*/
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    arrayListCategoryIdExpense.clear();
                                    arrayListCategoryIdInward.clear();
                                    arrayListInward.add(0,"-Select Category-");
                                    arrayListExpenses.add(0,"-Select Category-");
                                    Log.d("CategoryResponse1", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String categoryid=jsonObject1.getString("AccountCategoryID");
                                        String categoryname=jsonObject1.getString("CategoryName");
                                        String categorytype=jsonObject1.getString("CategoryType");
                                        String date=jsonObject1.getString("dtcreatedDate");
                                        //  String dt21=date.substring(date.indexOf("date")+7,date.indexOf(","));
                                        dataCategory=new DataCategory(categoryid,categoryname,categorytype,date);
                                        arrayListCategory.add(dataCategory);
                                    }
                                    arrayListCategoryIdExpense.clear();
                                    arrayListCategoryIdInward.clear();
                                    arrayListCategoryIdExpense.add(0,"0");
                                    arrayListCategoryIdInward.add(0,"0");

                                    for(int i=0;i<arrayListCategory.size();i++)
                                    {
                                        if(arrayListCategory.get(i).getCategorytype().contains("Expenses"))
                                        {
                                            String name=arrayListCategory.get(i).getCategoryname();
                                           // DataCategoryName dataCategoryName=new DataCategoryName(name);
                                            arrayListExpenses.add(name);
                                            arrayListCategoryIdExpense.add(arrayListCategory.get(i).getCategoryid());
                                        }
                                        else {
                                            String name1=arrayListCategory.get(i).getCategoryname();
                                            //DataCategoryName dataCategoryName=new DataCategoryName(name1);
                                            arrayListInward.add(name1);
                                            arrayListCategoryIdInward.add(arrayListCategory.get(i).getCategoryid());
                                        }
                                    }
                                    if(spinnerCategory.getSelectedItemPosition()==0)
                                    {
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(AddExpenses.this,R.layout.spinner_item1, arrayListExpenses);
                                         arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerCategoryType.setAdapter(arrayAdapter);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Log.d("CategorySize",arrayListExpenses.size()+" "+arrayListInward.size()+" "+arrayListCategoryIdExpense.size()+" "+arrayListCategoryIdInward.size());

                                } catch (JSONException e) {
                                    Toast.makeText(AddExpenses.this,"Errorcode-435 AddExpenses getExpensesResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(AddExpenses.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddExpenses.this);
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
        }catch (Exception e)
        {
            Toast.makeText(AddExpenses.this,"Errorcode-434 AddExpenses getExpenses "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcStatus", String.valueOf(e));
        }
    }//getExpenses
}
