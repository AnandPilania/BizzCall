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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;

public class LoginLogoutDetails extends AppCompatActivity {
    String clienturl="",clientid="",counselorid="",counselorid1="";
    ProgressDialog dialog;
    RequestQueue requestQueue;
    SharedPreferences sp;
    long timeout;
    AdapterLoginLogoutDetails adapterLoginLogoutDetails;
    ArrayList<DataLoginLogoutDetails> arrayListLoginDetails;
    RecyclerView recyclerLoginLogin;
    Thread thread;
    ImageView imgBack,imgRefresh;
    EditText edtDateFrom,edtDateTo;
    TextView txtNotFound;
    String sundays="",datefrom,dateFrom1="",dateTo1="";
    Date date1;
    private Calendar leavecalendar, leavecalendar1;
    private int day, month, year;
    Button btnSubmit;
    LinearLayout linearLoginDetails;
    int flag=0;
    List<Date> disable;
    static int datediff=0;
    TextView txtSundays,txtTotalDays,txtFullDays,txtHalfDays;
    ArrayList<DataSalaryTotal> arrayListSalaryTotal;
    ArrayList<DataTotalSal> arrayListTotalSal;
    ArrayList<DataSalaryGenerate> arrayListFullDaySalary;
    public static ArrayList<DataSalaryHalfDay> arrayListHalfDaySalary;
    //AdapterSalaryGenerate adapterSalaryGenerate;
   //AdapterSalaryHalfDay adapterSalaryHalfDay;
    String leavebal,halfDaySalary,fullDaySalary,cid,counselorname,panCard,accountNo,bankName,salary,totalDays,numOfSundays,totalcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login_logout_details);
            //to initialize all controls and variables
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
                    Intent intent = new Intent(LoginLogoutDetails.this, LoginLogoutDetails.class);
                    // intent.putExtra("Activity", "CounsellorContact");
                    startActivity(intent);

                }
            });
             //disable = new ArrayList<>();

           // Calendar cal = Calendar.getInstance();

            edtDateFrom.setOnClickListener(new View.OnClickListener() {
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
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flag=0;
                    if (TextUtils.isEmpty(dateFrom1)) {
                        edtDateFrom.setError("Select Date from");
                        flag = 1;
                    }
                    if (TextUtils.isEmpty(dateTo1)) {
                        edtDateTo.setError("Select Date to");
                        flag = 1;
                    }
                    if(flag==0) {
                        dialog = ProgressDialog.show(LoginLogoutDetails.this, "", "Loading LoginDetails", true);
                        newThreadInitilization(dialog);
                        //to get all attendance for particular selected date
                        getLoginLogoutDetails(edtDateFrom.getText().toString(), edtDateTo.getText().toString());
                    }
                }
            });

        }catch (Exception e)
        {
            Toast.makeText(LoginLogoutDetails.this,"Errorcode-529 LoginLogoutDetails onCreate "+e.toString(),Toast.LENGTH_SHORT).show();

        }
    }


    private void initialize()
    {
        requestQueue = Volley.newRequestQueue(LoginLogoutDetails.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        timeout = sp.getLong("TimeOut", 0);
        counselorid = sp.getString("Id", "");
        recyclerLoginLogin = findViewById(R.id.recyclerLoginLogout);
        imgBack = findViewById(R.id.img_back);
        imgRefresh = findViewById(R.id.imgRefresh);
        btnSubmit=findViewById(R.id.btnSubmit);
        edtDateFrom=findViewById(R.id.edtDatefrom);
        edtDateTo=findViewById(R.id.edtDateto);
        txtNotFound=findViewById(R.id.txtNotFound);
        txtSundays=findViewById(R.id.txtSundays);
        txtTotalDays = findViewById(R.id.txtTotalDays);
        txtFullDays = findViewById(R.id.txtFullDays);
        txtHalfDays = findViewById(R.id.txtHalfDays);
        linearLoginDetails=findViewById(R.id.linearLoginDetails);
        counselorid = counselorid.replaceAll(" ", "");

        leavecalendar = Calendar.getInstance();
        leavecalendar1 = Calendar.getInstance();
        day = leavecalendar.get(Calendar.DAY_OF_MONTH);
        year = leavecalendar.get(Calendar.YEAR);
        month = leavecalendar.get(Calendar.MONTH);
    }//initialize

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginLogoutDetails.this, Home.class);
        intent.putExtra("Activity", "CounselorNotification");
        startActivity(intent);
        finish();
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
                    Toast.makeText(LoginLogoutDetails.this,"Errorcode-540 LeaveApplication showDateTo"+e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(LoginLogoutDetails.this, listener, year, month, day);
        // dpDialog.getDatePicker().setMinDate(leavecalendar1.getTimeInMillis());
        dpDialog.show();
    }//showDateTo
    private void showDatefrom() {
        try {
            DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    edtDateFrom.setError(null);
                    dateFrom1= ( year + "-" +(monthOfYear+1)  + "-" +dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                    try {
                        date1 = sdf.parse(dateFrom1);
                        String dt1=sdf.format(date1);
                        edtDateFrom.setText( dt1);
                        disable = new ArrayList<>();

                        leavecalendar.set(Calendar.DAY_OF_MONTH, 1);
                        int month = leavecalendar.get(Calendar.MONTH);
                        do {
                            int dayOfWeek = leavecalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek == Calendar.SUNDAY) {
                                disable.add(leavecalendar.getTime());
                            }
                            leavecalendar.add(Calendar.DAY_OF_MONTH, 1);
                        } while (leavecalendar.get(Calendar.MONTH) == month);

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
            DatePickerDialog dpDialog = new DatePickerDialog(LoginLogoutDetails.this, listener, year, month, day);
            // dpDialog.getDatePicker().setMinDate(leavecalendar.getTimeInMillis());
            dpDialog.show();
        }catch (Exception e)
        {
            Toast.makeText(LoginLogoutDetails.this,"Errorcode-539 LoginLogoutDetails showDateFrom"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//showDatefrom

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
                                Toast.makeText(LoginLogoutDetails.this, "Connection aborted.", Toast.LENGTH_SHORT).show();
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
    public void getSalaryReportHalf(final String caseid1, final String datefrom, final String dateto)
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid="+caseid1+"&FromDate="+datefrom+"&ToDate="+dateto;
            Log.d("HalfDayUrl", url);
            arrayListFullDaySalary=new ArrayList<>();
            arrayListHalfDaySalary=new ArrayList<>();
            if(CheckInternet.checkInternet(LoginLogoutDetails.this))
            {
                if(CheckServer.isServerReachable(LoginLogoutDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    //arrayListLeaveStatus.clear();
                                    Log.d("HalfDayResponse", response);

                                    try {
                                        if(response.contains("[]"))
                                        {
                                            // linearLeaves.setVisibility(View.GONE);
                                            //txtNoLeaves.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            // linearLeaves.setVisibility(View.VISIBLE);
                                            //txtNoLeaves.setVisibility(View.GONE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                counselorid1=jsonObject1.getString("cCounselorId");
                                                counselorname=jsonObject1.getString("cCounselorName");
                                                salary = jsonObject1.getString("cSalary");
                                                totalDays = jsonObject1.getString("Total Days");
                                                totalcount = jsonObject1.getString("Total Count");
                                                halfDaySalary = jsonObject1.getString("Half day Salary");

                                               DataSalaryHalfDay dataSalaryHalfDay = new DataSalaryHalfDay(counselorid1,counselorname,salary,totalDays,totalcount,halfDaySalary);
                                                arrayListHalfDaySalary.add(dataSalaryHalfDay);
                                            }
                                            Log.d("ArrayHalfDay",arrayListHalfDaySalary.size()+"");
                                            for(int i=0;i<arrayListHalfDaySalary.size();i++) {
                                                if(arrayListHalfDaySalary.get(i).getCounselorid().equals(counselorid)) {
                                                    txtHalfDays.setText(arrayListHalfDaySalary.get(i).getTotalCount());
                                                }
                                            }

                                            int c1=0,c2=0;
                                          /*  for(int i=0;i<arrayListFullDaySalary.size();i++)
                                            {
                                                totalcount11 = "0";
                                                hdSalary = "0";
                                                for(int k=0;k<arrayListHalfDaySalary.size();k++) {
                                                    if (arrayListHalfDaySalary.get(k).getCounselorname().equals(arrayListFullDaySalary.get(i).getCounselorname()))
                                                    {
                                                        c2++;
                                                        Log.d("Cnt2",c2+"");
                                                        totalcount11 = arrayListHalfDaySalary.get(k).getTotalCount();
                                                        hdSalary    = arrayListHalfDaySalary.get(k).getHalfDaySalary();
                                                    }
                                                }
                                                DataSalaryTotal dataSalaryTotal=new DataSalaryTotal(arrayListFullDaySalary.get(i).getCounselorid(),arrayListFullDaySalary.get(i).getCounselorname(), arrayListFullDaySalary.get(i).getEmpPan(),arrayListFullDaySalary.get(i).getEmpAccountNo(),arrayListFullDaySalary.get(i).getEmpBankName(),arrayListFullDaySalary.get(i).getSalary(), arrayListFullDaySalary.get(i).getTotalDays(),arrayListFullDaySalary.get(i).getNumOfSundays(),totalcount11,arrayListFullDaySalary.get(i).getTotalCount(),arrayListFullDaySalary.get(i).getFullDaySalary(),hdSalary,additionalholidays,datefrom,dateto,arrayListFullDaySalary.get(i).getLeavebal(),month1,strYear);
                                                arrayListSalaryTotal.add(dataSalaryTotal);
                                            }

                                            arrayListTotalSal=new ArrayList<>();
                                            arrayListTotalSal.clear();
                                            for(int i1=0;i1<arrayListSalaryTotal.size();i1++) {
                                                hd = Double.parseDouble(arrayListSalaryTotal.get(i1).getTotalCountHD());
                                                fd=Double.parseDouble(arrayListSalaryTotal.get(i1).getTotalCountFD());
                                                sunday=Double.parseDouble(arrayListSalaryTotal.get(i1).getNumOfSundays());
                                                if(!arrayListSalaryTotal.get(i1).getHalfDaySalary().equals("0")) {
                                                    totalAttendence=fd+(hd*0.5)+sunday+Double.parseDouble(additionalholidays);
                                                }
                                                else {
                                                    totalAttendence=fd+sunday+Double.parseDouble(additionalholidays);
                                                }


                                                if(totaldays>totalAttendence)
                                                {
                                                    additionalday=totaldays-totalAttendence;
                                                    if(leavebal>additionalday)
                                                    {
                                                        leavebal=leavebal-additionalday;
                                                        totalAttendence=totalAttendence+additionalday;
                                                    }
                                                    else if(leavebal<=additionalday)
                                                    {
                                                        additionalday=leavebal;
                                                        totalAttendence=totalAttendence+additionalday;
                                                        leavebal=leavebal-leavebal;
                                                    }

                                                    updateLeaveBal(dataSalaryGenerateFull.getCounselorid(),leavebal+"");
                                                    insertLeaveBal(additionalday+"",dataSalaryGenerateFull.getCounselorid(),dataSalaryGenerateFull.getCmonth(),dataSalaryGenerateFull.getCyear());

                                                }
                                                else if(totaldays<totalAttendence){
                                                    additionalday=totalAttendence-totaldays;
                                                    leavebal=leavebal+additionalday;
                                                    //totalAttendence=totalAttendence+additionalday;
                                                    updateLeaveBal(dataSalaryGenerateFull.getCounselorid(),leavebal+"");


                                                }

                                                salary1= Double.parseDouble(arrayListSalaryTotal.get(i1).getSalary());
                                                totaldays1=Double.parseDouble(arrayListSalaryTotal.get(i1).getTotalDays());
                                                grossPay=(salary1/totaldays1)*totalAttendence;
                                                BigDecimal aa1 = new BigDecimal(grossPay);
                                                aa1 = aa1.setScale(0, BigDecimal.ROUND_UP);
                                                grossPay=Double.parseDouble(String.valueOf(aa1));
                                                basicpay=(grossPay*45)/100;
                                                HRA=(grossPay*20)/100;
                                                transport=(grossPay*8)/100;
                                                medical=(grossPay*8)/100;
                                                LTA=(grossPay*9)/100;
                                                incentive=(grossPay*10)/100;
                                                professiontax=200;
                                                incometax=0;
                                                netpay=(grossPay-professiontax-incentive);
                                                BigDecimal aa2 = new BigDecimal(netpay);
                                                aa2 = aa2.setScale(0, BigDecimal.ROUND_UP);
                                                netpay=Double.parseDouble(String.valueOf(aa2));
                                                totalnetpay=totalnetpay+netpay;
                                                totalgrosspay=totalgrosspay+grossPay;

                                                BigDecimal aa4 = new BigDecimal(totalgrosspay);
                                                aa4 = aa4.setScale(0, BigDecimal.ROUND_UP);
                                                totalgrosspay=Double.parseDouble(String.valueOf(aa4));

                                                BigDecimal aa3 = new BigDecimal(totalnetpay);
                                                aa3 = aa3.setScale(0, BigDecimal.ROUND_UP);
                                                totalnetpay=Double.parseDouble(String.valueOf(aa3));
                                                DataTotalSal dataTotalSal=new DataTotalSal(totalAttendence+"",grossPay+"",basicpay+"",HRA+"",
                                                        transport+"",medical+"",LTA+"",incentive+"",professiontax+"",incometax+"",netpay+"");
                                                arrayListTotalSal.add(dataTotalSal);
                                            }
                                            adapterSalaryGenerate = new AdapterSalaryGenerate(LoginLogoutDetails.this, arrayListSalaryTotal);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(SalaryGenerate.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerSalaryGenerate.setLayoutManager(layoutManager);
                                            recyclerSalaryGenerate.setAdapter(adapterSalaryGenerate);
                                            adapterSalaryGenerate.notifyDataSetChanged();*/
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(LoginLogoutDetails.this,"ErrorCode-15018:SalaryGenerate getSalaryReportHalfResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginLogoutDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(LoginLogoutDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginLogoutDetails.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginLogoutDetails.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        //edtName.setText("");
                                      //  edtPassword.setText("");
                                dialog.dismiss();

                            }
                        }).show();

            }
        }catch (Exception e)
        {
            Toast.makeText(LoginLogoutDetails.this,"ErrorCode-15017:SalaryGenerate getSalaryReportHalf"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }

    public void getSalaryReport(final String caseid1, final String datefrom, final String dateto)
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid="+caseid1+"&FromDate="+datefrom+"&ToDate="+dateto;
            Log.d("FullDayUrl", url);
            arrayListFullDaySalary=new ArrayList<>();
            arrayListHalfDaySalary=new ArrayList<>();
            if(CheckInternet.checkInternet(LoginLogoutDetails.this))
            {
                if(CheckServer.isServerReachable(LoginLogoutDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // dialog.dismiss();
                                    //arrayListLeaveStatus.clear();
                                    Log.d("FullDayResponse", response);

                                    try {
                                        getSalaryReportHalf("153",datefrom,dateto);
                                        if(response.contains("[]"))
                                        {
                                            dialog.dismiss();
                                            //txtNoData.setVisibility(View.VISIBLE);
                                            //recyclerSalaryGenerate.setVisibility(View.GONE);
                                            //img_exelicon.setVisibility(View.GONE);
                                            // linearLeaves.setVisibility(View.GONE);
                                            //txtNoLeaves.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                           // img_exelicon.setVisibility(View.VISIBLE);
                                            //txtNoData.setVisibility(View.GONE);
                                          //  recyclerSalaryGenerate.setVisibility(View.VISIBLE);
                                            // linearLeaves.setVisibility(View.VISIBLE);
                                            //txtNoLeaves.setVisibility(View.GONE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                counselorid1=jsonObject1.getString("cCounselorId");
                                                counselorname=jsonObject1.getString("cCounselorName");
                                                panCard = jsonObject1.getString("EmpPanCard");
                                                accountNo = jsonObject1.getString("EmpAccountNo");
                                                bankName = jsonObject1.getString("EmpBankName");
                                                salary = jsonObject1.getString("cSalary");
                                                totalDays = jsonObject1.getString("Total Days");
                                                numOfSundays = jsonObject1.getString("NumOfSundays");
                                                totalcount = jsonObject1.getString("Total Count");
                                                fullDaySalary = jsonObject1.getString("Full day Salary");
                                                leavebal=jsonObject1.getString("Leavebal");
                                                DataSalaryGenerate dataSalaryGenerate = new DataSalaryGenerate(counselorid1,counselorname,panCard,accountNo,bankName,salary,totalDays,numOfSundays,totalcount,fullDaySalary,leavebal);
                                                arrayListFullDaySalary.add(dataSalaryGenerate);
                                            }
                                            Log.d("ArrayFullDay",arrayListFullDaySalary.size()+"");
                                            for(int i=0;i<arrayListFullDaySalary.size();i++) {
                                                if(arrayListFullDaySalary.get(i).getCounselorid().equals(counselorid)) {
                                                    txtSundays.setText(arrayListFullDaySalary.get(i).getNumOfSundays());
                                                    txtTotalDays.setText(arrayListFullDaySalary.get(i).getTotalDays());
                                                    txtFullDays.setText(arrayListFullDaySalary.get(i).getTotalCount());
                                                }
                                            }

                                            /* adapterSalaryGenerate = new AdapterSalaryGenerate(SalaryGenerate.this, arrayListFullDaySalary);
                                                LinearLayoutManager layoutManager = new LinearLayoutManager(SalaryGenerate.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerSalaryGenerate.setLayoutManager(layoutManager);
                                            recyclerSalaryGenerate.setAdapter(adapterSalaryGenerate);
                                            adapterSalaryGenerate.notifyDataSetChanged();*/
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(LoginLogoutDetails.this,"ErrorCode-15020:SalaryGenerate getSalaryReportResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginLogoutDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(LoginLogoutDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginLogoutDetails.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginLogoutDetails.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        //edtName.setText("");
                                        //edtPassword.setText("");
                                dialog.dismiss();

                            }
                        }).show();

            }
        }catch (Exception e)
        {
            Toast.makeText(LoginLogoutDetails.this,"ErrorCode-15019:SalaryGenerate getSalaryReport"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcCounselorDetails", String.valueOf(e));
        }
    }

    public void getLoginLogoutDetails(final String df, final String dt)
    {
        try {
            String url=clienturl+"?clientid=" + clientid + "&caseid=145&CounselorID="+counselorid+"&FromDate="+df+"&ToDate="+dt;
            Log.d("CounselorUrl", url);
            arrayListLoginDetails=new ArrayList<>();
            if(CheckInternet.checkInternet(LoginLogoutDetails.this))
            {
                if(CheckServer.isServerReachable(LoginLogoutDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                                   /* for(Date date:disable)
                                    {
                                        Log.d("SunDate",sdf.format(date));
                                        compareDates(edtDateTo.getText().toString(),sdf.format(date));
                                    }
*/
                                   getSalaryReport("152",df,dt);
                                    Log.d("CounselorResponse1", response);
                                    try {
                                        if(response.contains("[]"))
                                        {
                                            linearLoginDetails.setVisibility(View.GONE);
                                            txtNotFound.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            linearLoginDetails.setVisibility(View.VISIBLE);
                                            txtNotFound.setVisibility(View.GONE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                                String LoginDate = jsonObject1.getString("LoginDate");
                                                String LoginTime = jsonObject1.getString("LoginTime");
                                                String LogoutTime = jsonObject1.getString("LogoutTime");
                                                String totalhrs = jsonObject1.getString("TotalHour");
                                                //String createdDate=jsonObject1.getString("dtcreatedDate")
                                                DataLoginLogoutDetails dataLoginLogoutDetails = new DataLoginLogoutDetails(LoginDate, LoginTime, LogoutTime, totalhrs);
                                                arrayListLoginDetails.add(dataLoginLogoutDetails);
                                            }
                                            adapterLoginLogoutDetails = new AdapterLoginLogoutDetails(LoginLogoutDetails.this, arrayListLoginDetails);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(LoginLogoutDetails.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerLoginLogin.setLayoutManager(layoutManager);
                                            recyclerLoginLogin.setAdapter(adapterLoginLogoutDetails);
                                            adapterLoginLogoutDetails.notifyDataSetChanged();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(LoginLogoutDetails.this,"Errorcode-531 LoginLogoutDetails LoginlogoutDetailsResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Log.d("LoginLogoutDetailExc", String.valueOf(e));
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginLogoutDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(LoginLogoutDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginLogoutDetails.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(LoginLogoutDetails.this);
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
            Toast.makeText(LoginLogoutDetails.this,"Errorcode-530 LoginLogoutDetails getLoginlogoutDetails "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcLoginLogoutDetails", String.valueOf(e));
        }
    }//getLoginLogoutDetails
    public void compareDates(String d1,String d2)
    {
        try{
            // If you already have date objects then skip 1
            datediff=0;
            //1
            // Create 2 dates starts
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-mm-dd");
            Date date1 = sdf1.parse(d1);
            Date date2 = sdf1.parse(d2);

            System.out.println("Date1"+sdf1.format(date1));
            System.out.println("Date2"+sdf1.format(date2));System.out.println();

            // Create 2 dates ends
            //1

            // Date object is having 3 methods namely after,before and equals for comparing
            // after() will return true if and only if date1 is after date 2
            if(date1.before(date2)){
                //datediff=1;
              //  txtSundays.setText(sundays.concat(sdf1.format(date2)));

                System.out.println("Date1 is after Date2");
            }

            System.out.println();
        }
        catch(ParseException ex){
            ex.printStackTrace();
        }
      /*  if(datediff==1)
        {
            return true;
        }
        else
            return false;*/
    }//compareDates


}
