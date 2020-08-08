package com.bizcall.wayto.mentebit13;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

//import com.google.android.gms.common.api.GoogleApiClient;


public class Dashboard extends AppCompatActivity
{
    String counselorid="", counselorname="",emailid="", role="", mobile="", status1="", statusid1="";
    TextView txtCoin, txtDiamond, txtMobile,txtNoData;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    int temp;

    AlertDialog alertDialog,alertRefName;
    int check=0,pos;
    String activityname;
    ArrayList<String> arrayList1;
    private boolean isSpinnerTouched = false;
    Long timeout;
    Thread thread;
   // String date1;
    GPSTracker gps;

    String uploadFilePath = "";
    String uploadFileName = "",url;
    String upLoadServerUri = null;
    int serverResponseCode = 0;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String uname1, id, statusid;
    Intent intent;

    Toast toast;
    Login login;
    String dataFrom, dataFrom1;
    ArrayList<DataStatusTotal> arrayListTotal;//to store list of total leads statuswise with count
    AdapterStatusTotalCount adapterTotal;//to show list of total leads statuswise with count on adapter.
    RecyclerView recyclerViewStatusTotal;
    Spinner spinnerRef;

    ArrayList<String> arrayListRefId;
    ImageView imgCoin;
    private Vibrator vibrator;
    RequestQueue requestQueue;

    String callPhoneno="", finalCallType="", strdateFormated="", callDuration="";
    String clienturl="",clientid="";
    String IMEINumber1="",IMEINumber2="";

    android.app.AlertDialog.Builder builder;
    ArrayList<DataNotification> arrayListNotification;
    ImageView imgRefresh;

    NetworkInfo info;
    ImageView imgBack;
    public static EditText edtDatefrom,edtDateTo;

   public static String dateFrom1="",dateTo1="",country,course,refname="";
    Date date1;
    private Calendar leavecalendar, leavecalendar1;
    public static int day, month, year;
    CheckBox chkCountry,chkCourse;
    Spinner spinnerCountry,spinnerCourse;
    LinearLayout linearCountry,linearCourse,linearCountryCourse;
    ArrayList<String> arrayListCountry,arrayListCourse;
    TextView txtLoad;
    int alltotal=0;
    TextView  txtTotalLeads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_dashboard);
            //to initialize all varaibles and controls
            initialize();


            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Dashboard.this,Dashboard.class);
                    intent.putExtra("Activity","Dashboard");
                    startActivity(intent);
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
            edtDateTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //select date to option from calendar
                    showDateTo();
                }
            });

            chkCountry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(chkCountry.isChecked())
                    {
                        linearCountryCourse.setVisibility(View.VISIBLE);
                        linearCountry.setVisibility(View.VISIBLE);
                    }
                    else {
                        linearCountry.setVisibility(View.GONE);
                    }
                }
            });

            chkCourse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(chkCourse.isChecked())
                    {
                        linearCountryCourse.setVisibility(View.VISIBLE);
                        linearCourse.setVisibility(View.VISIBLE);
                    }
                    else {
                        linearCourse.setVisibility(View.GONE);
                    }
                }
            });
            //set spinner data load on touch of spinner not on activity opened.
            spinnerRef.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isSpinnerTouched = true;
                    return false;
                }
            });

            txtLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //counselorid,clientid,clienturl;
                   txtLoadClicked();
                }
            });

            if(CheckInternetSpeed.checkInternet(Dashboard.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
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
            else if(CheckInternetSpeed.checkInternet(Dashboard.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
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
                dialog = ProgressDialog.show(Dashboard.this, "", "Loading all data for refnames...", false,true);
               newThreadInitilization(dialog);
              // function is used to get data ref names which are loaded in spinner
                if(dateFrom1.length()==0)
                {
                    dateFrom1="1900-01-01";
                }
                if(dateTo1.length()==0)
                {
                    dateTo1= ( year + "-" +(month+1)  + "-" +day);
                }

                getRefName(dateFrom1,dateTo1);

            }
        }catch (Exception e)
        {
            Toast.makeText(Dashboard.this,"Errorcode-116 Dashboard oncreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }//onCreate close

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
                                Toast.makeText(Dashboard.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
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
    public void initialize()
    {
        requestQueue=Volley.newRequestQueue(Dashboard.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        imgRefresh=findViewById(R.id.imgRefresh);
        imgBack=findViewById(R.id.img_back);
        txtLoad=findViewById(R.id.txtLoad);
        txtTotalLeads=findViewById(R.id.txtTotalLeads);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putInt("numOfCalls", 0).apply();
        activityname = getIntent().getStringExtra("Activity");

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString("ActivityContact", "Home");
        editor.commit();
        // sp=getSharedPreferences("Settings",Context.MODE_PRIVATE);
        IMEINumber1 = sp.getString("IMEI1", null);
        IMEINumber2 = sp.getString("IMEI2", null);
        timeout=sp.getLong("TimeOut",0);

        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        Log.d("UrlHome", clienturl);
        timeout = sp.getLong("TimeOut", 0);
        Log.d("timeout", String.valueOf(timeout));
        login = new Login();
        uname1 = sp.getString("Name", null);
        id = sp.getString("Id", null);
        Log.d("U***", uname1);
        Log.d("I***", id);

        counselorname = sp.getString("Name", null);
        counselorname=counselorname.replaceAll(" ","");
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replaceAll(" ", "");
        emailid = sp.getString("EmailId", null);
        role = sp.getString("Role", null);
        mobile = sp.getString("MobileNumber", null);
        statusid1 = sp.getString("StatusId", null);
        statusid1 = statusid1.replaceAll(" ", "");

        spinnerRef = findViewById(R.id.spinnerFilter);
        txtCoin = findViewById(R.id.txtCoin);
        txtDiamond = findViewById(R.id.txtDiamond);
        imgCoin = findViewById(R.id.imgCoin);
        recyclerViewStatusTotal = findViewById(R.id.recyclerStatusTotalCnt);
        edtDatefrom=findViewById(R.id.edtDatefrom);
        edtDateTo=findViewById(R.id.edtDateto);
        chkCountry=findViewById(R.id.chkCountry);
        chkCourse=findViewById(R.id.chkCourse);
        linearCountry=findViewById(R.id.linearCountry);
        linearCourse=findViewById(R.id.linearCourse);
        spinnerCountry=findViewById(R.id.spinnerCountry);
        spinnerCourse=findViewById(R.id.spinnerCourse);
        linearCountryCourse=findViewById(R.id.linearCountryCourse);
        txtNoData = findViewById(R.id.txtNoData);

        arrayListCountry=new ArrayList<>();
        arrayListCourse=new ArrayList<>();

        arrayListTotal = new ArrayList<>();
        gps = new GPSTracker(Dashboard.this);
        leavecalendar = Calendar.getInstance();
        leavecalendar1 = Calendar.getInstance();
        day = leavecalendar.get(Calendar.DAY_OF_MONTH);
        year = leavecalendar.get(Calendar.YEAR);
        month = leavecalendar.get(Calendar.MONTH);
        dateTo1= ( year + "-" +(month+1)  + "-" +day);
    }//Close initialize
    public void txtLoadClicked()
    {
        try {
            String spinnerValue = spinnerRef.getSelectedItem().toString();
            if (spinnerValue.equals("All")) {
                String dtaFrom = "0";
                String refname = "All";
                editor.putString("DtaFrom", dtaFrom);
                editor.putString("DataRefName", refname);
                editor.commit();
                String StepId = clienturl + "?clientid=" + clientid + "&caseid=12&CounsellorId=" + counselorid;
                url = returnStep(StepId);
                Log.d("Returncase11", StepId);
                Log.d("URLcase11", url);
                dialog = ProgressDialog.show(Dashboard.this, "", "Loading data");
                newThreadInitilization(dialog);
                getAllRefName(url);
            } else {
                int pos = (int) spinnerRef.getSelectedItemId();
                pos = pos - 1;
                String dataFromFinal = arrayListRefId.get(pos);
                String dtaFrom = dataFromFinal;
                String refname = spinnerRef.getSelectedItem().toString();
                editor.putString("DtaFrom", dtaFrom);
                editor.putString("DataRefName", refname);
                editor.commit();
                String StepId = clienturl + "?clientid=" + clientid + "&caseid=13&CounsellorId=" + counselorid + "&DataFrom=" + dataFromFinal;
                url = returnStep(StepId);
                Log.d("Returncase11", StepId);
                Log.d("URLcase11", url);
                dialog = ProgressDialog.show(Dashboard.this, "", "Loading data");
                newThreadInitilization(dialog);
                getAllRefName(url);
            }
        }catch (Exception e)
        {
            Toast.makeText(Dashboard.this,"Errorcode-116 Dashboard txtLoadClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void showDateTo()
    {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                edtDateTo.setError(null);
                dateTo1= ( year + "-" +(monthOfYear+1)  + "-" +dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    date1 = sdf.parse(dateTo1);
                    String dt1=sdf.format(date1);
                    edtDateTo.setText(dt1);
                }catch (Exception e)
                {
                    Toast.makeText(Dashboard.this,"Errorcode-540 LeaveApplication showDateTo"+e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(Dashboard.this, listener, year, month, day);
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


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dpDialog = new DatePickerDialog(Dashboard.this, listener, year, month, day);
            // dpDialog.getDatePicker().setMinDate(leavecalendar.getTimeInMillis());
            dpDialog.show();
        }catch (Exception e)
        {
            Toast.makeText(Dashboard.this,"Errorcode-539 LoginLogoutDetails showDateFrom"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//showDatefrom

    public void getCountryList() {
        try {
            if(CheckServer.isServerReachable(Dashboard.this))
            {
                url = clienturl + "?clientid=" + clientid + "&caseid=174&CounselorID=" + counselorid;
                Log.d("CountryUrl",url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(!activityname.equals("CounsellorData")) {
                                        if (dialog.isShowing()) {

                                            dialog.dismiss();
                                        }
                                    }
                                    getCourseList();

                                    // alertRefName.dismiss();
                                    Log.d("CountryResponse", response);
                                    arrayListCountry.clear();
                                   // arrayListCountry.add("Select");
                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log.d("Json",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String country = jsonObject1.getString("cCountry");
                                        arrayListCountry.add(country);
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Dashboard.this, R.layout.spinner_item1, arrayListCountry);
                                    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerCountry.setAdapter(arrayAdapter);

                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    Toast.makeText(Dashboard.this, "Errorcode-142 Dashboard AllRefNamesResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("CountryException", String.valueOf(e));
                                    //  e.printStackTrace();
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
                                    // responsecode1="ServerError";
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Dashboard.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }
                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
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
            Toast.makeText(Dashboard.this,"Errorcode-141 Dashboard getAllRefNames "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//Close getCountryList
    public void getCourseList() {
        try {
            if(CheckServer.isServerReachable(Dashboard.this))
            {
                url = clienturl + "?clientid=" + clientid + "&caseid=175&CounselorID=" + counselorid;
                Log.d("CourseUrl",url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (dialog.isShowing()) {

                                        dialog.dismiss();
                                    }

                                    // alertRefName.dismiss();
                                    Log.d("CourseResponse", response);
                                    arrayListCourse.clear();
                                  //  arrayListCourse.add("Select");
                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log.d("Json",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String course = jsonObject1.getString("cCourse");
                                        arrayListCourse.add(course);
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Dashboard.this, R.layout.spinner_item1, arrayListCourse);
                                    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerCourse.setAdapter(arrayAdapter);
                                    if(activityname.equals("CounsellorData")) {
                                        dateFrom1 = sp.getString("DateFrom", "");
                                        dateTo1 = sp.getString("DateTo", "");
                                        edtDatefrom.setText(dateFrom1);
                                        edtDateTo.setText(dateTo1);

                                        country = getIntent().getStringExtra("Country");
                                        // dataFrom = getIntent().getStringExtra("DataFrom");
                                        for (int i = 0; i < arrayListCountry.size(); i++) {
                                            if (arrayListCountry.get(i).equals(country)) {
                                                spinnerCountry.setSelection(i);
                                            }
                                        }
                                        course = getIntent().getStringExtra("Course");
                                        // dataFrom = getIntent().getStringExtra("DataFrom");
                                        for (int i = 0; i < arrayListCourse.size(); i++) {
                                            if (arrayListCourse.get(i).equals(course)) {
                                                spinnerCourse.setSelection(i);
                                            }
                                        }


                                        txtLoadClicked();
                                    }



                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    Toast.makeText(Dashboard.this, "Errorcode-142 Dashboard AllRefNamesResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("CountryException", String.valueOf(e));
                                    //  e.printStackTrace();
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
                                    // responsecode1="ServerError";
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Dashboard.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }
                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
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
            Toast.makeText(Dashboard.this,"Errorcode-141 Dashboard getAllRefNames "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//Close getCourseList
    public void getRefName(String datefrom, final String dateto) {
        try {
            if(CheckServer.isServerReachable(Dashboard.this)) {
                // arrayListRefrences=new ArrayList<>();
                arrayList1 = new ArrayList<>();
                arrayListRefId = new ArrayList<>();
                arrayList1.add(0, "All");

                String StepId = clienturl + "?clientid=" + clientid + "&caseid=11&CounsellorId=" + counselorid ;
                url = returnStep(StepId);
                Log.d("Returncase11", StepId);
                Log.d("URLcase11", url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    getCountryList();

                                    if(!activityname.equals("CounsellorData")) {
                                        if (dialog.isShowing()) {

                                            dialog.dismiss();
                                        }
                                    }
                                    Log.d("RefNameResponse", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log.d("Json",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        dataFrom = jsonObject1.getString("cDataFrom");
                                        String dataRefName = jsonObject1.getString("DataRefName");
                                        //    DataReference dataReference=new DataReference(dataRefName,dataFrom);
                                        arrayList1.add(dataRefName);
                                        arrayListRefId.add(dataFrom);
                                        // arrayListRefrences.add(dataReference);
                                        //  String total=jsonObject1.getString("Total No");
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter(Dashboard.this, R.layout.spinner_item1, arrayList1);
                                    // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerRef.setAdapter(arrayAdapter);
                                    // getLastUploadedLocation();
                                    arrayAdapter.notifyDataSetChanged();
                                    if(activityname.equals("CounsellorData")){
                                        refname = getIntent().getStringExtra("RefName");
                                        dataFrom = getIntent().getStringExtra("DataFrom");
                                        for (int i = 0; i < arrayList1.size(); i++) {
                                            if (arrayList1.get(i).equals(refname)) {
                                                spinnerRef.setSelection(i);
                                            }
                                        }
                                    }


                                    //Log.d("RefIdSize", String.valueOf(arrayListRefId.size()));
                                } catch (JSONException e) {
                                    Toast.makeText(Dashboard.this, "Errorcode-140 Dashboard getRefResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("RefException", String.valueOf(e));
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
                                    // responsecode1="ServerError";
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Dashboard.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
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
            Toast.makeText(Dashboard.this,"Errorcode-139 Dashboard getRefNames "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//Close getRefName

    public void getAllRefName(String urlFinal) {
        try {
            if(CheckServer.isServerReachable(Dashboard.this))
            {
                url =urlFinal;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (dialog.isShowing()) {

                                        dialog.dismiss();
                                    }
                                 /*   if (alertRefName.isShowing()) {
                                        alertRefName.dismiss();
                                    }*/
                                    // alertRefName.dismiss();
                                    Log.d("AllRefNameResponse", response);
                                    arrayListTotal.clear();
                                    alltotal=0;
                                    if(response.contains("[]"))
                                    {
                                        txtNoData.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        txtNoData.setVisibility(View.GONE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        // Log.d("Json",jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String status = jsonObject1.getString("cStatus");
                                            String cstauts = jsonObject1.getString("currentstatus");
                                            String total = jsonObject1.getString("Total No");
                                            DataStatusTotal dataStatusTotal = new DataStatusTotal(status, cstauts, total);
                                            arrayListTotal.add(dataStatusTotal);
                                        }
                                        for(int i=0;i<arrayListTotal.size();i++)
                                        {
                                            alltotal=alltotal+Integer.parseInt(arrayListTotal.get(i).getTotal());
                                        }
                                        Log.d("AllTotal", String.valueOf(alltotal));
                                        txtTotalLeads.setVisibility(View.VISIBLE);
                                        txtTotalLeads.setText("TotalLeads:"+String.valueOf(alltotal));
                                        adapterTotal = new AdapterStatusTotalCount(arrayListTotal, Dashboard.this);
                                        recyclerViewStatusTotal = findViewById(R.id.recyclerStatusTotalCnt);
                                        recyclerViewStatusTotal.setLayoutManager(new GridLayoutManager(Dashboard.this, 2));
                                        recyclerViewStatusTotal.setAdapter(adapterTotal);
                                        adapterTotal.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    Toast.makeText(Dashboard.this, "Errorcode-142 Dashboard AllRefNamesResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("AllRefException", String.valueOf(e));
                                    //  e.printStackTrace();
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
                                    // responsecode1="ServerError";
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Dashboard.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }
                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Dashboard.this);
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
            Toast.makeText(Dashboard.this,"Errorcode-141 Dashboard getAllRefNames "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//Close getAllRefName

    @Override
    public void onBackPressed() {
        try {
            Intent intent=new Intent(Dashboard.this,Home.class);
            startActivity(intent);
            finish();
        }catch (Exception e)
        {
            Toast.makeText(Dashboard.this,"Errorcode-514 Dashboard OnBackpressed "+e.toString(),Toast.LENGTH_SHORT).show();        }
    }

    public String returnStep(String frwdURL)
    {
        //String Frwddatefrom="";
        //String Frwddateto="";
        String Frwddateto=edtDateTo.getText().toString();
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
            Frwddateto=edtDateTo.getText().toString();
        }
        Log.d("DateFrom",Frwddatefrom+" "+Frwddateto);
        if(chkCountry.isChecked()&&chkCourse.isChecked())
        {
            country=spinnerCountry.getSelectedItem().toString();
            course=spinnerCourse.getSelectedItem().toString();
            editor=sp.edit();
            editor.putString("cCountry",country);
            editor.putString("cCourse",course);
            editor.commit();

            String NewURL="&FromDate="+Frwddatefrom+"&ToDate="+Frwddateto+"&Step=4&cCountry="+spinnerCountry.getSelectedItem().toString()+"&cCourse="+spinnerCourse.getSelectedItem().toString();
            frwdURL +=NewURL;
            return frwdURL;
        }
        else if(chkCountry.isChecked())
        {
            country=spinnerCountry.getSelectedItem().toString();
            editor=sp.edit();
            editor.putString("cCountry",country);
            editor.putString("cCourse","NA");
            editor.commit();
            String NewURL="&FromDate="+Frwddatefrom+"&ToDate="+Frwddateto+"&Step=2&cCountry="+spinnerCountry.getSelectedItem().toString();
            frwdURL +=NewURL;
            return frwdURL;
        }
        else if(chkCourse.isChecked())
        {
           // String course=spinnerCourse.getSelectedItem().toString();

            course=spinnerCourse.getSelectedItem().toString();
            editor=sp.edit();
            editor.putString("cCountry","NA");
            editor.putString("cCourse",course);
            editor.commit();

            String NewURL="&FromDate="+Frwddatefrom+"&ToDate="+Frwddateto+"&Step=3&cCourse="+spinnerCourse.getSelectedItem().toString();
            frwdURL +=NewURL;
            return frwdURL;
        }
        else
        {
            //country=spinnerCountry.getSelectedItem().toString();
            //course=spinnerCourse.getSelectedItem().toString();
            editor=sp.edit();
            editor.putString("cCountry","NA");
            editor.putString("cCourse","NA");
            editor.commit();
            String NewURL="&FromDate="+Frwddatefrom+"&ToDate="+Frwddateto+"&Step=1";
            frwdURL +=NewURL;
            return frwdURL;
        }
    }
}



