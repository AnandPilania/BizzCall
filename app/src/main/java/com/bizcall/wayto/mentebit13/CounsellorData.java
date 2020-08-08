package com.bizcall.wayto.mentebit13;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CounsellorData extends AppCompatActivity {
    ArrayList<DataCounselor> arraylistCounselor;
    RecyclerView recyclerViewCounselor;
    AdapterCounselorData adapterCounselorData;
    ProgressDialog dialog;
    UrlRequest urlRequest;
    String statusid="", cid="", status="", total="", datafrom="", refname="", srno="", cname="", course="", parentno="", mobile="", email="",strSNO="";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView txtActivity, txtRef,txtNotFound,txtAsc,txtDesc,txtMin,txtMax,txtDisplayInfo,txtActivityName;
    ImageView imgBack;
    Spinner spinnerFilter;
    EditText edtSearchText;
    ImageView imgSearch;
    Long timeout;
    String strMin,strMax,url;
    RequestQueue requestQueue;
    String searchAs, searchtext, clientid,clienturl,selectedButton;
    ArrayList<String> arrayListSearchAs;
    AlertDialog alertDialog;
    RadioGroup radioGroup;
    RadioButton radioButton;
    LinearLayout linearOrder;
    Vibrator vibrator;
    Button btnNext,btnPrevious;
    int searchbool=0;
    String searchboolUrl="",searchbooltext="",searchboolAs="", orderboolAs="",strActivity="";
    ImageView imgRefresh;
    Thread thread;
    String dateFrom1="",dateTo1="";
    EditText edtDatefrom,edtDateto;
    Date date1;
    int isDateSelected=0;
    private Calendar leavecalendar, leavecalendar1;
    private int day, month, year;
    TextView txtSubmit;
    String cCountry,cCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_counsellordate);
        try {
            initialize();
            txtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isDateSelected=0;
                    dateFrom1=edtDatefrom.getText().toString();
                    dateTo1=edtDateto.getText().toString();
                    if(dateFrom1.length()==0)
                    {
                        edtDatefrom.setError("Invalid date");
                        isDateSelected=1;
                    }
                    if(dateTo1.length()==0)
                    {
                        edtDateto.setError("Invalid date");
                        isDateSelected=1;
                    }

                    if(isDateSelected==0)
                    {
                        dialog = ProgressDialog.show(CounsellorData.this, "", "Loading counselor data...", true);
                        newThreadInitilization(dialog);
                        if (refname.contains("All")) {

                            String StepId = clienturl + "?clientid=" + clientid + "&caseid=101&CounselorId=" + cid+ "&statusid=" + statusid + "&Sortby=nSrNo&MinVal=" + strMin + "&MaxVal=" + strMax;
                            url = returnStep(StepId);
                            Log.d("Returncase11", StepId);
                            Log.d("URLcase11", url);
                            getCounselor(url);
                        } else {
                            String StepId = clienturl + "?clientid=" + clientid + "&caseid=102&CounselorId=" + cid + "&statusid=" + statusid + "&DataFrom=" + datafrom + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                            url = returnStep(StepId);
                            Log.d("Returncase11", StepId);
                            Log.d("URLcase11", url);
                            getCounselor(url);

                        }
                    }
                }
            });
            edtDatefrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //select date to option from calendar
                    showDatefrom();
                }
            });

            // leavecalendar1.add(Calendar.DAY_OF_MONTH, 1);
            edtDateto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //select date to option from calendar
                    showDateTo();
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });

            strActivity=getIntent().getStringExtra("Activity");
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(CounsellorData.this,CounsellorData.class);
                    intent.putExtra("Activity",strActivity);
                    startActivity(intent);
                }
            });

            if(strActivity.contains("OnlineLead"))
            {
                txtActivityName.setText("Online Lead");
            }
            txtMin.setText(strMin);
                txtMax.setText(strMax);
                txtDisplayInfo.setText("Displaying "+txtMin.getText().toString()+"-"+txtMax.getText().toString()+"\nOut of "+total);
            if(CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
            else if(CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
            }
            else {
                dialog = ProgressDialog.show(CounsellorData.this, "", "Loading counselor data...", true);
                 newThreadInitilization(dialog);
                if (refname.contains("All")) {

                    String StepId = clienturl + "?clientid=" + clientid + "&caseid=101&CounselorId=" + cid+ "&statusid=" + statusid + "&Sortby=nSrNo&MinVal=" + strMin + "&MaxVal=" + strMax;
                    url = returnStep(StepId);
                    Log.d("Returncase11", StepId);
                    Log.d("URLcase11", url);
                    getCounselor(url);
                } else {
                    String StepId  = clienturl + "?clientid=" + clientid + "&caseid=102&CounselorId=" + cid + "&statusid=" + statusid + "&DataFrom=" + datafrom + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                    url = returnStep(StepId);
                    Log.d("Returncase11", StepId);
                    Log.d("URLcase11", url);
                    getCounselor(url);
                }
            }

                arrayListSearchAs = new ArrayList<>();
                arrayListSearchAs.add("Serial No");
                arrayListSearchAs.add("Candidate Name");
                arrayListSearchAs.add("Course");
                arrayListSearchAs.add("Mobile");
                arrayListSearchAs.add("Email");
                arrayListSearchAs.add("Parent No");
                arrayListSearchAs.add("Allocation Date");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(CounsellorData.this, R.layout.spinner_item1, arrayListSearchAs);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFilter.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            spinnerFilter.setSelection(0);

            txtDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to search leads in descending order
                    searchDescClick();
                }
            });
            txtAsc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to search leads in ascending order
                   searchAscClick();
                }
            });
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  btnNextClick();
                }
            });
            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnPreviousClick();
                }
            });

            spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           searchAsItemSelected();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
            }catch (Exception e)
            {
               // dialog.dismiss();
                Toast.makeText(CounsellorData.this,"Errorcode-148 CounselorData oncreate "+e.toString(),Toast.LENGTH_SHORT).show();
                Log.d("CounselorDataException", String.valueOf(e));
            }
    }//oncreate close
    public void initialize()
    {
        requestQueue=Volley.newRequestQueue(CounsellorData.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        imgRefresh=findViewById(R.id.imgRefresh);
        edtDatefrom=findViewById(R.id.edtDatefrom);
        edtDateto=findViewById(R.id.edtDateto);
        txtAsc=findViewById(R.id.txtAsc);
        txtDesc=findViewById(R.id.txtDesc);
        txtActivity = findViewById(R.id.txtSlectedStatus);
        txtRef = findViewById(R.id.txtDataRef);
        imgBack = findViewById(R.id.img_back);
        spinnerFilter = findViewById(R.id.spinnerFilter1);
        edtSearchText = findViewById(R.id.edtSearchtext);
        imgSearch = findViewById(R.id.img_search);
        txtNotFound=findViewById(R.id.txtNotFound1);
        linearOrder=findViewById(R.id.linearOrder);
        txtMin=findViewById(R.id.txtMin);
        txtMax=findViewById(R.id.txtMax);
        txtDisplayInfo=findViewById(R.id.txtDisplayInfo);
        btnNext=findViewById(R.id.btnLoadMore);
        btnPrevious=findViewById(R.id.btnLoadPrevious);
        txtActivityName=findViewById(R.id.txtActivityName);
        recyclerViewCounselor = findViewById(R.id.recyclerCounselorData);

        txtSubmit=findViewById(R.id.txtSubmit);
        arrayListSearchAs = new ArrayList<>();
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        timeout=sp.getLong("TimeOut",0);
        clienturl=sp.getString("ClientUrl",null);
        clientid = sp.getString("ClientId", null);
        cid = sp.getString("Id", null);
        statusid = sp.getString("SStatusId", null);
        status = sp.getString("SCStatus", null);
        total = sp.getString("Count", null);
        dateFrom1=sp.getString("DateFrom","");
        dateTo1=sp.getString("DateTo","");
        cCountry=sp.getString("cCountry","");
        cCourse=sp.getString("cCourse","");

        //  txtActivity.setText("(" + status + " " + total + ")");
        txtActivity.setText("(" + status + ")");

        cid = cid.replace(" ", "");
        datafrom = sp.getString("DtaFrom", null);
        refname = sp.getString("DataRefName", null);
        Log.d("IDD:", statusid + "" + cid);
        Log.d("DtaFrom", datafrom);
        Log.d("RefName", refname);
        txtRef.setText("(" + refname + ")  "+datafrom);
        edtDatefrom.setText(dateFrom1);
        edtDateto.setText(dateTo1);

        strMin="1";
        strMax="25";

        leavecalendar = Calendar.getInstance();
        leavecalendar1 = Calendar.getInstance();
        day = leavecalendar.get(Calendar.DAY_OF_MONTH);
        year = leavecalendar.get(Calendar.YEAR);
        month = leavecalendar.get(Calendar.MONTH);
    }//close initialize
    private void showDateTo()
    {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                edtDateto.setError(null);
                dateTo1= ( year + "-" +(monthOfYear+1)  + "-" +dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    date1 = sdf.parse(dateTo1);
                    String dt1=sdf.format(date1);
                    edtDateto.setText(dt1);


                }catch (Exception e)
                {
                    Toast.makeText(CounsellorData.this,"Errorcode-540 LeaveApplication showDateTo"+e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(CounsellorData.this, listener, year, month, day);
        // dpDialog.getDatePicker().setMinDate(leavecalendar1.getTimeInMillis());
        dpDialog.show();
    }//showDateTo
    private void showDatefrom() {
        try {
            DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    edtDatefrom.setError(null);
                    dateFrom1= ( year + "-" +(monthOfYear+1)  + "-" +dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                    try {
                        date1 = sdf.parse(dateFrom1);
                        String dt1=sdf.format(date1);
                        edtDatefrom.setText( dt1);

                        //  leavecalendar.set(Calendar.DAY_OF_MONTH, 1);
                        //int month = leavecalendar.get(Calendar.MONTH);


                        // SimpleDateFormat fmt = new SimpleDateFormat("EEE M/d/yyyy");
                       /* for (Date date : disable)
                            sundays=sundays.concat(sdf.format(date))+"\n";
                            txtSundays.setText(sundays);*/
                        //System.out.println(sdf.format());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dpDialog = new DatePickerDialog(CounsellorData.this, listener, year, month, day);
            // dpDialog.getDatePicker().setMinDate(leavecalendar.getTimeInMillis());
            dpDialog.show();
        }catch (Exception e)
        {
            Toast.makeText(CounsellorData.this,"Errorcode-539 LoginLogoutDetails showDateFrom"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//showDatefrom
    public void searchAsItemSelected()
    {
        try {
            searchAs = spinnerFilter.getSelectedItem().toString();
            if (searchAs.contains("Serial No")) {
                searchAs = "nSrNo";
            } else if (searchAs.contains("Candidate Name")) {
                searchAs = "cCandidateName";
            } else if (searchAs.contains("Course")) {
                searchAs = "cCourse";
            } else if (searchAs.contains("Mobile")) {
                searchAs = "cMobile";
            } else if (searchAs.contains("Email")) {
                searchAs = "cEmail ";
            } else if (searchAs.contains("Parent No")) {
                searchAs = "cParantNo ";
            } else if (searchAs.contains("Allocation Date")) {
                searchAs = "AllocationDate";
            }
        }catch (Exception e)
        {
            Toast.makeText(CounsellorData.this,"Errorcode-149 CounselorData spinnerSearchAs clicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close searchAsItemSelected
    public void btnPreviousClick()
    {
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) - 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) - 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString() + " out of" + total);
            if (searchbool == 1) {
                if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                   newThreadInitilization(dialog);

                    String StepId =  clienturl + "?clientid=" + clientid + "&caseid=104&CounselorId=" + cid + "&CurrentStatus=" + statusid + "&FieldName=" + searchAs + "&FieldVal=" + searchbooltext + "&OrderVal=" + orderboolAs + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                    url = returnStep(StepId);
                    Log.d("Returncase11", StepId);
                    Log.d("URLcase11", url);
                    getCounselor(url);
                    //to search data under all option selected from dashboard
                  //  searchDataForAll(searchbooltext, searchboolAs, orderboolAs, txtMin.getText().toString(), txtMax.getText().toString(),dateFrom1,dateTo1);
                }
                //  refreshWhenLoading();
            } else if (searchbool == 2) {
                if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                    if(dateFrom1.length()==0)
                    {
                        dateFrom1="1900-01-01";
                    }
                    if(dateTo1.length()==0)
                    {
                        dateTo1= ( year + "-" +(month+1)  + "-" +day);
                    }
                    newThreadInitilization(dialog);
                    String StepId =  clienturl + "?clientid=" + clientid + "&caseid=103&cDataFrom=" + datafrom + "&CounselorId=" + cid + "&CurrentStatus=" + statusid + "&FieldName=" + searchAs + "&FieldVal=" + searchbooltext + "&OrderVal=" + orderboolAs + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                    url = returnStep(StepId);
                    Log.d("Returncase11", StepId);
                    Log.d("URLcase11", url);
                    getCounselor(url);
                  //to search data under selected status value from dashboard
                   // searchData(searchbooltext, searchboolAs, orderboolAs, txtMin.getText().toString(), txtMax.getText().toString(),dateFrom1,dateTo1);
                }

            } else {
                if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    dialog = ProgressDialog.show(CounsellorData.this, "", "Loading counselor data...", true);
                    if (refname.contains("All")) {

                        String StepId = clienturl + "?clientid=" + clientid + "&caseid=101&CounselorId=" + cid+ "&statusid=" + statusid + "&Sortby=nSrNo&MinVal=" + strMin + "&MaxVal=" + strMax;
                        url = returnStep(StepId);
                        Log.d("Returncase11", StepId);
                        Log.d("URLcase11", url);
                        getCounselor(url);
                    } else {

                        String StepId = clienturl + "?clientid=" + clientid + "&caseid=102&CounselorId=" + cid + "&statusid=" + statusid + "&DataFrom=" + datafrom + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                        url = returnStep(StepId);
                        Log.d("Returncase11", StepId);
                        Log.d("URLcase11", url);
                        getCounselor(url);
                    }
                }
            }
        }catch (Exception e)
        {
            Toast.makeText(CounsellorData.this,"Errorcode-150 CounselorData BtnPrevious Clicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }////close btnPreviousClick

    public void btnNextClick()
    {
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) + 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) + 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            if (searchbool == 1) {
                if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                    newThreadInitilization(dialog);

                    //to search data under all option selected from dashboard
                    String StepId =  clienturl + "?clientid=" + clientid + "&caseid=104&CounselorId=" + cid + "&CurrentStatus=" + statusid + "&FieldName=" + searchAs + "&FieldVal=" + searchbooltext + "&OrderVal=" + orderboolAs + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                    url = returnStep(StepId);
                    Log.d("Returncase104", StepId);
                    Log.d("URLcase104", url);
                    getCounselor(url);
                    //searchDataForAll(searchbooltext, searchboolAs, orderboolAs, txtMin.getText().toString(), txtMax.getText().toString(),dateFrom1,dateTo1);
                }
                //  refreshWhenLoading();
            } else if (searchbool == 2) {
                if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);

                    newThreadInitilization(dialog);
                    String StepId =  clienturl + "?clientid=" + clientid + "&caseid=103&cDataFrom=" + datafrom + "&CounselorId=" + cid + "&CurrentStatus=" + statusid + "&FieldName=" + searchAs + "&FieldVal=" + searchbooltext + "&OrderVal=" + orderboolAs + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                    url = returnStep(StepId);
                    Log.d("Returncase11", StepId);
                    Log.d("URLcase11", url);
                    //to search data under selected status value from dashboard
                    getCounselor(url);

                  //  searchData(searchbooltext, searchboolAs, orderboolAs, txtMin.getText().toString(), txtMax.getText().toString(),dateFrom1,dateTo1);
                }
                //refreshWhenLoading();
            } else {
                if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    dialog = ProgressDialog.show(CounsellorData.this, "", "Loading counselor data...", true);
                    if (refname.contains("All")) {

                        String StepId = clienturl + "?clientid=" + clientid + "&caseid=101&CounselorId=" + cid+ "&statusid=" + statusid + "&Sortby=nSrNo&MinVal=" + strMin + "&MaxVal=" + strMax;
                        url = returnStep(StepId);
                        Log.d("Returncase11", StepId);
                        Log.d("URLcase11", url);
                        getCounselor(url);
                    } else {
                        String StepId = clienturl + "?clientid=" + clientid + "&caseid=102&CounselorId=" + cid + "&statusid=" + statusid + "&DataFrom=" + datafrom + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                        url = returnStep(StepId);
                        Log.d("Returncase11", StepId);
                        Log.d("URLcase11", url);
                        getCounselor(url);
                    }
                }
            }
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString() + " out of" + total);
        }catch (Exception e)
        {
            Toast.makeText(CounsellorData.this,"Errorcode-151 CounselorData BtnNext clicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close btnNextClick

    public void searchAscClick()
    {
        try {
            txtMin.setText("1");
            txtMax.setText("25");
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString() + " out of" + total);

            searchAs = spinnerFilter.getSelectedItem().toString();
            searchtext = edtSearchText.getText().toString().trim();
            searchtext = searchtext.replaceAll("'", "").trim();
            if (searchAs.contains("Serial No")) {
                searchAs = "nSrNo";
            } else if (searchAs.contains("Candidate Name")) {
                searchAs = "cCandidateName";
            } else if (searchAs.contains("Course")) {
                searchAs = "cCourse";
            } else if (searchAs.contains("Mobile")) {
                searchAs = "cMobile";
            } else if (searchAs.contains("Email")) {
                searchAs = "cEmail ";
            } else if (searchAs.contains("Parent No")) {
                searchAs = "cParantNo ";
            } else if (searchAs.contains("Allocation Date")) {
                searchAs = "AllocationDate";
            }

            // searchCondition="and "+searchAs+" like '%"+searchtext+"%' ";
            if (searchtext == "" || searchtext == null) {
                edtSearchText.setError("Please insert text");
            } else {

                if (refname.contains("All")) {
                    strMin = txtMin.getText().toString();
                    strMax = txtMax.getText().toString();
                    // searchCondition = "and " + searchAs + " like '%" + searchtext + "%' ";
                    // dialog=ProgressDialog.show(CounsellorData.this,"","Loading",true);
                    searchbool = 1;
                    searchbooltext = searchtext;
                    searchboolAs = searchAs;
                    orderboolAs = searchAs + " ASC";
                    if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                        dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                        newThreadInitilization(dialog);
                        if(dateFrom1.length()==0)
                        {
                            dateFrom1="1900-01-01";
                        }
                        if(dateTo1.length()==0)
                        {
                            dateTo1= ( year + "-" +(month+1)  + "-" +day);
                        }
                        //to search data under all option selected from dashboard
                        String StepId =  clienturl + "?clientid=" + clientid + "&caseid=104&CounselorId=" + cid + "&CurrentStatus=" + statusid + "&FieldName=" + searchAs + "&FieldVal=" + searchbooltext + "&OrderVal=" + orderboolAs + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                        url = returnStep(StepId);
                        Log.d("Returncase11", StepId);
                        Log.d("URLcase11", url);
                        getCounselor(url);
                       // searchDataForAll(searchbooltext, searchboolAs, orderboolAs, txtMin.getText().toString(), txtMax.getText().toString(),dateFrom1,dateTo1);
                    }
                    // refreshWhenLoading();
                    //getCounselor(cid, statusid,searchAs+" "+"DESC");
                } else {
                    strMin = txtMin.getText().toString();
                    strMax = txtMax.getText().toString();
                    //  dialog=ProgressDialog.show(CounsellorData.this,"","Loading",true);
                    searchbool = 2;
                    searchbooltext = searchtext;
                    searchboolAs = searchAs;
                    orderboolAs = searchAs + " ASC";
                    if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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

                        dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                        if(dateFrom1.length()==0)
                        {
                            dateFrom1="1900-01-01";
                        }
                        if(dateTo1.length()==0)
                        {
                            dateTo1= ( year + "-" +(month+1)  + "-" +day);
                        }
                        newThreadInitilization(dialog);
                        //to search data under selected status value from dashboard
                        String StepId =  clienturl + "?clientid=" + clientid + "&caseid=103&cDataFrom=" + datafrom + "&CounselorId=" + cid + "&CurrentStatus=" + statusid + "&FieldName=" + searchAs + "&FieldVal=" + searchbooltext + "&OrderVal=" + orderboolAs + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                        url = returnStep(StepId);
                        Log.d("Returncase11", StepId);
                        Log.d("URLcase11", url);
                        getCounselor(url);
                      //  searchData(searchbooltext, searchboolAs, orderboolAs, txtMin.getText().toString(), txtMax.getText().toString(),dateFrom1,dateTo1);
                    }
                    // refreshWhenLoading();
                    // getCounselor1(cid, statusid, datafrom);
                }
            }
        }catch (Exception e)
        {
            Toast.makeText(CounsellorData.this,"Errorcode-152 CounselorData searchASC clicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close searchAscClick

    public void searchDescClick()
    {
        try {
            txtMin.setText("1");
            txtMax.setText("25");
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString() + " out of" + total);

            searchAs = spinnerFilter.getSelectedItem().toString();
            searchtext = edtSearchText.getText().toString().trim();
            searchtext = searchtext.replaceAll("'", "").trim();
            Log.d("Searchtext", searchtext);
            if (searchAs.contains("Serial No")) {
                searchAs = "nSrNo";
            } else if (searchAs.contains("Candidate Name")) {
                searchAs = "cCandidateName";
            } else if (searchAs.contains("Course")) {
                searchAs = "cCourse";
            } else if (searchAs.contains("Mobile")) {
                searchAs = "cMobile";
            } else if (searchAs.contains("Email")) {
                searchAs = "cEmail ";
            } else if (searchAs.contains("Parent No")) {
                searchAs = "cParantNo ";
            } else if (searchAs.contains("Allocation Date")) {
                searchAs = "AllocationDate";
            }
              /*  String searchCondition="and "+searchAs+" like '%"+searchtext+"%' ";
                Log.d("SearchText",searchtext);*/
            if (searchtext == "" || searchtext == null) {
                edtSearchText.setError("Please insert text");

            } else {
                if (refname.contains("All")) {
                    strMin = txtMin.getText().toString();
                    strMax = txtMax.getText().toString();
                    // searchCondition = "and " + searchAs + " like '%" + searchtext + "%' ";
                    // dialog=ProgressDialog.show(CounsellorData.this,"","Loading",true);
                    searchbool = 1;
                    searchbooltext = searchtext;
                    searchboolAs = searchAs;
                    orderboolAs = searchAs + " DESC";
                    if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                        dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                        newThreadInitilization(dialog);
                        if(dateFrom1.length()==0)
                        {
                            dateFrom1="1900-01-01";
                        }
                        if(dateTo1.length()==0)
                        {
                            dateTo1= ( year + "-" +(month+1)  + "-" +day);
                        }
                        //to search data under all option selected from dashboard
                        String StepId =  clienturl + "?clientid=" + clientid + "&caseid=104&CounselorId=" + cid + "&CurrentStatus=" + statusid + "&FieldName=" + searchAs + "&FieldVal=" + searchbooltext + "&OrderVal=" + orderboolAs + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                        url = returnStep(StepId);
                        Log.d("Returncase11", StepId);
                        Log.d("URLcase11", url);
                        getCounselor(url);
                       // searchDataForAll(searchbooltext, searchboolAs, orderboolAs, txtMin.getText().toString(), txtMax.getText().toString(),dateFrom1,dateTo1);
                    }

                } else {
                    strMin = txtMin.getText().toString();
                    strMax = txtMax.getText().toString();
                    //  dialog=ProgressDialog.show(CounsellorData.this,"","Loading",true);
                    searchbool = 2;
                    searchbooltext = searchtext;
                    searchboolAs = searchAs;
                    orderboolAs = searchAs + " DESC";
                    if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                    } else if (CheckInternetSpeed.checkInternet(CounsellorData.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
                        dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                        if(dateFrom1.length()==0)
                        {
                            dateFrom1="1900-01-01";
                        }
                        if(dateTo1.length()==0)
                        {
                            dateTo1= ( year + "-" +(month+1)  + "-" +day);
                        }
                        newThreadInitilization(dialog);
                        //to search data under selected status value from dashboard
                        String StepId =  clienturl + "?clientid=" + clientid + "&caseid=103&cDataFrom=" + datafrom + "&CounselorId=" + cid + "&CurrentStatus=" + statusid + "&FieldName=" + searchAs + "&FieldVal=" + searchbooltext + "&OrderVal=" + orderboolAs + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                        url = returnStep(StepId);
                        Log.d("Returncase11", StepId);
                        Log.d("URLcase11", url);
                        getCounselor(url);
                     //   searchData(searchbooltext, searchboolAs, orderboolAs, txtMin.getText().toString(), txtMax.getText().toString(),dateFrom1,dateTo1);
                    }
                    //refreshWhenLoading();
                    // getCounselor1(cid, statusid, datafrom);
                }
            }
        }catch (Exception e)
        {
            Toast.makeText(CounsellorData.this,"Errorcode-153 CounselorData searchDESC clicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close searchDescClick


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
                                Toast.makeText(CounsellorData.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(Home.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("TimeThread","cdvmklmv");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(CounsellorData.this, Dashboard.class);
            intent.putExtra("Activity", "CounsellorData");
            intent.putExtra("RefName", refname);
            intent.putExtra("DataFrom", datafrom);
            intent.putExtra("CounselorId",cid);
            intent.putExtra("Country",cCountry);
            intent.putExtra("Course",cCourse);
            Log.d("RefName***", refname);
            startActivity(intent);
            finish();
      //  super.onBackPressed();
        }catch (Exception e)
        {
            Toast.makeText(CounsellorData.this,"Errorcode-154 CounselorData onBackPressed "+e.toString(),Toast.LENGTH_SHORT).show();        }
    }

    public String returnStep(String frwdURL)
    {
        String Frwddateto=edtDateto.getText().toString();
        String Frwddatefrom=edtDatefrom.getText().toString();
        if(Frwddatefrom.length()==0)
        {
            Frwddatefrom="1900-01-01";
        }
        else
        {
            //Frwddatefrom="1900-01-01";
            Frwddatefrom=edtDatefrom.getText().toString();
        }
        if(Frwddateto.length()==0)
        {
            //Frwddateto="2020-03-03";
            Frwddateto= ( year + "-" +(month+1)  + "-" +day);
        }
        else
        {
            //Frwddateto="2020-03-03";
            Frwddateto=edtDateto.getText().toString();
        }
        Log.d("DateFrom",Frwddatefrom+" "+Frwddateto);
        if(!cCourse.equals("NA")&&!cCountry.equals("NA"))
        {
            String NewURL="&FromDate="+Frwddatefrom+"&ToDate="+Frwddateto+"&Step=4&cCountry="+cCountry+"&cCourse="+cCourse;
            frwdURL +=NewURL;
            return frwdURL;
        }
        else if(!cCountry.equals("NA"))
        {
            String NewURL="&FromDate="+Frwddatefrom+"&ToDate="+Frwddateto+"&Step=2&cCountry="+cCountry;
            frwdURL +=NewURL;
            return frwdURL;
        }
        else if(!cCourse.equals("NA"))
        {
            // String course=spinnerCourse.getSelectedItem().toString();


            String NewURL="&FromDate="+Frwddatefrom+"&ToDate="+Frwddateto+"&Step=3&cCourse="+cCourse;
            frwdURL +=NewURL;
            return frwdURL;
        }
        else
        {
            String NewURL="&FromDate="+Frwddatefrom+"&ToDate="+Frwddateto+"&Step=1";
            frwdURL +=NewURL;
            return frwdURL;
        }

    }
    public void getCounselor(String url) {
        try {
            if(CheckServer.isServerReachable(CounsellorData.this)) {
                //dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);
                arraylistCounselor = new ArrayList<>();
               // url = clienturl + "?clientid=" + clientid + "&caseid=101&CounselorId=" + cid + "&statusid=" + sid + "&Sortby=" + search1 + "&MinVal=" + strMin + "&MaxVal=" + strMax+"&FromDate="+datefrom+"&ToDate="+dateto;
         /*       String StepId = clienturl + "?clientid=" + clientid + "&caseid=101&CounsellorId=" + cid+ "&statusid=" + sid + "&Sortby=" + search1 + "&MinVal=" + strMin + "&MaxVal=" + strMax;
                url = returnStep(StepId);
                Log.d("Returncase11", StepId);
                Log.d("URLcase11", url);*/

                Log.d("IfAllSelectedUrl",url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("CounselorResponse", response);
                                try {
                                    if(response.contains("[]"))
                                    {
                                        txtNotFound.setVisibility(View.VISIBLE);
                                        recyclerViewCounselor.setVisibility(View.GONE);
                                    }
                                    else {
                                        txtNotFound.setVisibility(View.GONE);

                                        arraylistCounselor.clear();
                                        JSONObject jsonObject = new JSONObject(response);
                                        // Log.d("Json",jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            strSNO = jsonObject1.getString("SNO");
                                            String srno = jsonObject1.getString("nSrNo");
                                            String cname = jsonObject1.getString("cCandidateName");
                                            String course = jsonObject1.getString("cCourse");
                                            String mobile = jsonObject1.getString("cMobile");
                                            String parentno = jsonObject1.getString("cParantNo");
                                            String email = jsonObject1.getString("cEmail");
                                            String allocationDate = jsonObject1.getString("AllocationDate");
                                            Log.d("DateAllocation", allocationDate);
                                            allocationDate = allocationDate.substring(allocationDate.indexOf(":") + 2, 20);
                                            String adrs = jsonObject1.getString("cAddressLine");
                                            String city = jsonObject1.getString("cCity");
                                            String state = jsonObject1.getString("cState");
                                            String pincode = jsonObject1.getString("cPinCode");
                                            String statusId = jsonObject1.getString("nStatusID");
                                            String statusStr = jsonObject1.getString("cStatus");
                                            String remarks = jsonObject1.getString("cRemarks");
                                            //String datafrom=jsonObject1.getString("cDataFrom");

                                            Log.d("Status11", srno);
                                            DataCounselor dataCounselor = new DataCounselor(strSNO, srno, cname, course, mobile, parentno, email, allocationDate, adrs, city, state, pincode, statusId, statusStr, remarks);
                                            arraylistCounselor.add(dataCounselor);
                                        }

                                        adapterCounselorData = new AdapterCounselorData(CounsellorData.this, arraylistCounselor);
                                        recyclerViewCounselor = findViewById(R.id.recyclerCounselorData);
                                        recyclerViewCounselor.setVisibility(View.VISIBLE);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerViewCounselor.setLayoutManager(layoutManager);
                                        recyclerViewCounselor.setAdapter(adapterCounselorData);
                                        adapterCounselorData.notifyDataSetChanged();
                                        int mval = Integer.parseInt(txtMax.getText().toString());
                                        if (txtMin.getText().toString().equals("1")) {
                                            btnPrevious.setVisibility(View.GONE);
                                            if (Integer.parseInt(txtMax.getText().toString()) <= Integer.parseInt(total)) {
                                                btnNext.setVisibility(View.VISIBLE);
                                            } else {
                                                btnNext.setVisibility(View.GONE);
                                            }
                                        } else if (mval > Integer.parseInt(total)) {
                                            btnPrevious.setVisibility(View.VISIBLE);
                                            btnNext.setVisibility(View.GONE);
                                        } else {
                                            btnNext.setVisibility(View.VISIBLE);
                                            btnPrevious.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    //Log.d("Size**", String.valueOf(arrayList.size()));
                                } catch (JSONException e) {
                                    Toast.makeText(CounsellorData.this, "Errorcode-156 CounselorData getCounselorResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("AllCounselorException", String.valueOf(e));
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    //   dialog.dismiss();
                                    Toast.makeText(CounsellorData.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
            Toast.makeText(CounsellorData.this,"Errorcode-155 CounselorData getCounselor "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//Close getCounselor



}

