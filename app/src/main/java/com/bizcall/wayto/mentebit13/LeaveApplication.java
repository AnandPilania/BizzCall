package com.bizcall.wayto.mentebit13;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LeaveApplication extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Calendar leavecalendar, leavecalendar1;
    private EditText edtDatefrom, edtDateto, edtReason;
    private TextView txtTotaldays;
    private String dateFrom1="",dateTo1="", totaldays="", cCounselorName, cCounselorID, selectname="", remarks="", ID="";
    private Date date1, date2;
    private int day, month, year;
    Spinner spinnerCounsellorName;
    AlertDialog.Builder builder;
    Button btnsubmit;
    ArrayList<String> mArrayListCounsellorName, mArraylistID;
    ArrayAdapter<String> mAdapterSpinnerCounsellorName;
    RequestQueue requestQueue;
    SharedPreferences sp;
    String counselorname,clientid,clienturl,counselorid;
    long timeout;
    Thread thread;
    int flag=0;
    ProgressDialog dialog;
    ImageView imgBack,imgRefresh;
    LinearLayout linearTotalDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        try {
            initialization();

            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LeaveApplication.this, LeaveApplication.class);
                    startActivity(intent);
                    finish();
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            btnsubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlert();
                }
            });

            leavecalendar.add(Calendar.DAY_OF_MONTH, 1);
            edtDatefrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatefrom();
                }
            });

            leavecalendar1.add(Calendar.DAY_OF_MONTH, 1);
            edtDateto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDateTo();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(LeaveApplication.this,"Errorcode-537 LeaveApplication onCreate"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    // dialog1.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(LeaveApplication.this, "Connection Aborted.", Toast.LENGTH_SHORT).show();
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
        super.onBackPressed();
        Intent intent=new Intent(LeaveApplication.this,LeaveReport.class);
        startActivity(intent);
        finish();
    }

    private void initialization() {
    try {
        edtDatefrom = findViewById(R.id.edtDatefrom);
        edtDateto = findViewById(R.id.edtDateto);
        edtReason = findViewById(R.id.edtReason);
        btnsubmit = findViewById(R.id.btnSubmit);
        txtTotaldays = findViewById(R.id.txtTotaldays);
        imgBack = findViewById(R.id.img_back);
        imgRefresh = findViewById(R.id.imgRefresh);
        spinnerCounsellorName = findViewById(R.id.spinnerCounsellorName);
        linearTotalDays = findViewById(R.id.linearTotalDays);
        leavecalendar = Calendar.getInstance();
        leavecalendar1 = Calendar.getInstance();
        day = leavecalendar.get(Calendar.DAY_OF_MONTH);
        year = leavecalendar.get(Calendar.YEAR);
        month = leavecalendar.get(Calendar.MONTH);

        requestQueue = Volley.newRequestQueue(LeaveApplication.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replaceAll(" ", "");
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        counselorname = sp.getString("Name", null);
        timeout = sp.getLong("TimeOut", 0);
    }catch (Exception e)
    {
        Toast.makeText(LeaveApplication.this,"Errorcode-538 LeaveApplication initialization"+e.toString(),Toast.LENGTH_SHORT).show();
    }

    }//initialization

    private void showDatefrom() {
        try {
            DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dateFrom1= (monthOfYear + 1)+ "/" + dayOfMonth + "/" + year;
                    edtDatefrom.setText( dayOfMonth+ "/" + (monthOfYear + 1) + "/" + year);

                }
            };
            DatePickerDialog dpDialog = new DatePickerDialog(LeaveApplication.this, listener, year, month, day);
            dpDialog.getDatePicker().setMinDate(leavecalendar.getTimeInMillis());
            dpDialog.show();
        }catch (Exception e)
        {
            Toast.makeText(LeaveApplication.this,"Errorcode-539 LeaveApplication showDateFrom"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//showDatefrom

    private void showDateTo()
    {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                edtDateto.setError(null);
                dateTo1= (monthOfYear + 1)+ "/" + dayOfMonth + "/" + year;
                edtDateto.setText( dayOfMonth+ "/" + (monthOfYear + 1) + "/" + year);

                try {
                   // dateFrom = edtDatefrom.getText().toString();
                   // dateTo = edtDateto.getText().toString();
                  //  Log.d("DateToFrom",dateFrom+" "+dateTo);

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                    date1 = sdf.parse(dateFrom1);
                    date2 = sdf.parse(dateTo1);

                    if (date2.compareTo(date1) < 0) {
                        edtDateto.setError("Enter Valid Date.");
                        txtTotaldays.setText("");
                    } else {
                        long difference = Math.abs(date1.getTime() - date2.getTime());
                        long differenceDates = (difference / (24 * 60 * 60 * 1000)) + 1;
                        totaldays = Long.toString(differenceDates);
                        linearTotalDays.setVisibility(View.VISIBLE);
                        txtTotaldays.setText(totaldays + " Days");
                    }
                }catch (Exception e)
                {
                    Toast.makeText(LeaveApplication.this,"Errorcode-540 LeaveApplication showDateTo"+e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(LeaveApplication.this, listener, year, month, day);
        dpDialog.getDatePicker().setMinDate(leavecalendar1.getTimeInMillis());
        dpDialog.show();
    }//showDateTo

    private void showAlert()
    {
        try {
            flag = 0;
            remarks = edtReason.getText().toString();
            remarks=remarks.replace("'","");
            // totaldays=txtTotaldays.getText().toString();

            if (TextUtils.isEmpty(dateFrom1)) {
                edtDatefrom.setError("Select Date from");
                flag = 1;
            }
            if (TextUtils.isEmpty(dateTo1)) {
                edtDateto.setError("Select Date to");
                flag = 1;
            }
            if (TextUtils.isEmpty(remarks)) {
                edtReason.setError("Enter Reason");
                flag = 1;
            }

            if (flag == 0) {
                dialog = ProgressDialog.show(LeaveApplication.this, "", "Submitting leave application", true);
                newThreadInitilization(dialog);
                //to insert leave application to database
                submitLeaveApplicationl(dateFrom1, dateTo1, totaldays, remarks, counselorid);
            }
        }catch (Exception e)
        {
            Toast.makeText(LeaveApplication.this,"Errorcode-542 LeaveApplication showAlert"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//showAlert


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getApplicationContext(), mArrayListCounsellorName.get(position) + " Selected", Toast.LENGTH_LONG).show();

        selectname = mArrayListCounsellorName.get(position);
        ID = mArraylistID.get((int) id);
    }
    public void submitLeaveApplicationl(String strDateFrom,String strDateTo,String strTotalDays,String strRemarks,String strCounselorID)
    {
        try{
           String url=clienturl+"?clientid=" + clientid + "&caseid=146&Step=4&CounselorID=" + strCounselorID +"&DateFrom=" + strDateFrom+"&DateTo=" + strDateTo +"&TotalDays=" + strTotalDays+"&Remarks=" + strRemarks;
            Log.d("ApplicationUrl", url);
            if(CheckInternet.checkInternet(LeaveApplication.this))
            {
                if(CheckServer.isServerReachable(LeaveApplication.this))
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("*******", response.toString());
                                    try {
                                        dialog.dismiss();
                                        Log.d("ApplicationResponse", response);
                                        if (response.contains("Data inserted successfully"))
                                        {
                                            Toast.makeText(LeaveApplication.this, "Application submitted successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(LeaveApplication.this,LeaveApplication.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(LeaveApplication.this, "Application not submitted", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(LeaveApplication.this,"Errorcode-544 LeaveApplication LeaveApplicationResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    if (error.networkResponse != null||error instanceof TimeoutError ||error instanceof NoConnectionError ||error instanceof AuthFailureError ||error instanceof ServerError ||error instanceof NetworkError ||error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LeaveApplication.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(LeaveApplication.this,"Network issue",Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                }else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LeaveApplication.this);
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
                } }else {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LeaveApplication.this);
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
            Toast.makeText(LeaveApplication.this,"Errorcode-543 LeaveApplication submitLeaveApplication"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcSetNotifjcation", String.valueOf(e));
        }

    }//submitLeaveApplicationl

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
