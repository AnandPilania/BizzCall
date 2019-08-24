package com.bizcall.wayto.mentebit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityGraphReport extends AppCompatActivity {
    RadioGroup mRadioGroup;
    RadioButton rd_all, rd_date_range;
    LinearLayout linearLayoutData, linear_recycler_data,linearUnderCounselorName;

    //......calender..........
    private Calendar datecalendar;
    DatePickerDialog.OnDateSetListener listener;
    TextView mtv_date_from, mtv_date_to, mButtonLoad;
    EditText edtDatefrom, edtDateto;
    String currentDate, finalDate, callDate;
    int totalCall;
    String stringdateTo, stringdateFrom;
    long timeout;
    android.support.v7.app.AlertDialog alertDialog;

    private int day, month, year;
    Date datefrom, dateto;
    LinearLayout linearLayoutAll;

    // ........URL String.........
    String urlReportNo, table_data, urlReportDatewise, clientUrl, clientId;

    //.......Graph........
    BarChart mBarChart, chart;
    MarkerView mv;

    //.............Data fetch..........
    RequestQueue requestQueue;
    RecyclerView mRecyclerView_Table_data;
    AdapterGraph adapterGraph;
    ImageView imgBack, imgRefresh;
    Vibrator vibrator;

    //.................ArrayList...........
    ArrayList<DataGraph> mSampleDataArrayList_table;
    ArrayList<BarEntry> mBarEntryArrayList;
    ArrayList<String> mStringArrayList;
    ArrayList<BarEntry> arrayListFirstGraphX;
    ArrayList<String> arrayListFirstGraphY;

    ArrayList<String> arrayListCounselorName;
    ArrayList<String> arrayListCounselorId;

    ProgressDialog dialog;
    TextView txtExport, txtActivityName, txtGraphName;
    EditText edtExcelName;
    File directory;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    // private ArrayList<DataExcelExport> mArrayList;
    TextView txtNotFound;
    String existname, activtyName;
    TextView txtDate, txtTotal, txtCallType;
    String id1;
    boolean success;
    Spinner spinnerCounselor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_report);

        try {
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            clientUrl = sp.getString("ClientUrl", null);
            clientId = sp.getString("ClientId", null);
            timeout = sp.getLong("TimeOut", 0);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            mRecyclerView_Table_data = findViewById(R.id.recycler_table);
            txtActivityName = findViewById(R.id.txtActivityName);
            txtGraphName = findViewById(R.id.txtGraphName);
            txtNotFound = findViewById(R.id.txtNotFound);
            linearLayoutAll = findViewById(R.id.linearData);
            txtDate = findViewById(R.id.txtDate);
            txtTotal = findViewById(R.id.txtTotal);
            imgRefresh = findViewById(R.id.imgRefresh);
            txtCallType = findViewById(R.id.txtCallType);
            spinnerCounselor = findViewById(R.id.spinner_counselor);
            linearUnderCounselorName=findViewById(R.id.linearUnderCounselor);
            //id1 = sp.getString("Id", null).trim();
            //id1 = String.valueOf(3);

            //  mArrayList=new ArrayList<>();
            mRadioGroup = findViewById(R.id.radio);
            rd_all = findViewById(R.id.rd_btnn_all);
            rd_date_range = findViewById(R.id.rd_btnn_date_range);

            linearLayoutData = findViewById(R.id.linear_Load_data);
            linear_recycler_data = findViewById(R.id.linear_recycler);
            imgBack = findViewById(R.id.img_back);
            edtDatefrom = findViewById(R.id.edtDatefrom);
            edtDateto = findViewById(R.id.edtDateto);
            mButtonLoad = findViewById(R.id.btn_load);
            mtv_date_from = findViewById(R.id.tv_date_from);
            mtv_date_to = findViewById(R.id.tv_date_to);

            txtExport = findViewById(R.id.txtExport);
            edtExcelName = findViewById(R.id.edtExcelName);
            datecalendar = Calendar.getInstance();
            day = datecalendar.get(Calendar.DAY_OF_MONTH);
            year = datecalendar.get(Calendar.YEAR);
            month = datecalendar.get(Calendar.MONTH);
            datecalendar.add(Calendar.DAY_OF_MONTH, 0);

            datefrom = new Date();

            mBarEntryArrayList = new ArrayList<BarEntry>();
            mStringArrayList = new ArrayList<String>();
            mBarChart = (BarChart) findViewById(R.id.barchart);
            mBarChart.setTouchEnabled(true);
            mBarChart.setDragEnabled(true);
            mBarChart.setScaleEnabled(true);
            mBarChart.setMarkerView(mv);

            activtyName = getIntent().getStringExtra("ActivityName");
            editor.putString("ReportActivity", activtyName);
            editor.commit();

            txtActivityName.setText(activtyName);
            txtGraphName.setText("Graph" + " " + activtyName);
            txtDate.setText(activtyName + " " + "Date");
            if (activtyName.equals("CallLog Report")) {
                txtTotal.setText("Total Call Time in Minutes");
            } else {
                txtTotal.setText("Total" + " " + activtyName);
            }

            final int id = mRadioGroup.getCheckedRadioButtonId();
            rd_all = findViewById(id);
            String rVal = rd_all.getText().toString();
            Log.d("Selected", rVal);
            if (rVal.contains("All")) {
                if (CheckInternetSpeed.checkInternet(ActivityGraphReport.this).contains("0")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
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
                } else if (CheckInternetSpeed.checkInternet(ActivityGraphReport.this).contains("1")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
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
                    dialog = ProgressDialog.show(ActivityGraphReport.this, "", "Loading counselor names", true);
                    getCounselorList();
                   // loadAllReport();

                }
                //refreshWhenLoading();
            } else {
                if (linearLayoutData.getVisibility() == View.GONE) {
                    linearLayoutData.setVisibility(View.VISIBLE);
                    linear_recycler_data.setVisibility(View.GONE);
                }

            }
            spinnerCounselor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    id1 = arrayListCounselorId.get(position);
                    editor = sp.edit();
                    editor.putString("Id", id1);
                    editor.commit();
                    onRadioButtonClicked();
                   // dialog = ProgressDialog.show(GraphReport.this, "", "Loading report", true);
                    //loadAllReport();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityGraphReport.this, ActivityGraphReport.class);
                    intent.putExtra("ActivityName", activtyName);
                    startActivity(intent);
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                    Animatoo.animateSlideRight(ActivityGraphReport.this);
                }
            });

            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    rd_all = findViewById(checkedId);
                    onRadioButtonClicked();

                }
            });  //  mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()

            txtExport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExportClicked();
                }
            });

            mtv_date_from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTxtDateFromClicked();
                }
            });  //  mtv_date_from.setOnClickListener(new View.OnClickListener()

            mButtonLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBtnLoadClicked();

                }

            });   //  mButtonLoad.setOnClickListener(new View.OnClickListener()

            mtv_date_to.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onTxtDateToClicked();

                }
            });    //mtv_date_to.setOnClickListener(new View.OnClickListener()
        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-363 GraphReport onCreate " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("ReportException", String.valueOf(e));
        }
    }//onCreate

    public void getCounselorList() {
        try {
            String CounselorUrl = clientUrl + "?clientid=" + clientId + "&caseid=30";
            Log.d("CounselorUrl", CounselorUrl);

            if (CheckInternet.checkInternet(ActivityGraphReport.this)) {
                if (CheckServer.isServerReachable(ActivityGraphReport.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, CounselorUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    arrayListCounselorId = new ArrayList<>();
                                    arrayListCounselorName = new ArrayList<>();

                                    arrayListCounselorName.add(0, "Select Counselor");
                                    arrayListCounselorId.add(0,"0");
                                    Log.d("CounselorResponse1", response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String id = String.valueOf(jsonObject1.getInt("cCounselorID"));
                                            String name = jsonObject1.getString("cCounselorName");
                                            arrayListCounselorName.add(name);
                                            arrayListCounselorId.add(id);
                                        }
                                        ArrayAdapter<String> dataAdapterState = new ArrayAdapter(ActivityGraphReport.this, R.layout.spinner_item1, arrayListCounselorName);
                                        spinnerCounselor.setAdapter(dataAdapterState);
                                       dataAdapterState.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        Toast.makeText(ActivityGraphReport.this, "Errorcode-189 CounselorContact counselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ActivityGraphReport.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
                    alertDialogBuilder.setTitle("Server Down!!!!")
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
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
            Toast.makeText(ActivityGraphReport.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onTxtDateToClicked() {
        try {
            DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    finalDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

                    try {
                        dateto = simpleDateFormat.parse(finalDate);
                        stringdateTo = simpleDateFormat.format(dateto);
                        edtDateto.setText(finalDate);
                        if (finalDate.length() == 0) {
                            edtDateto.setError("Select date");
                        } else {
                            edtDateto.setError(null);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dpDialog = new DatePickerDialog(ActivityGraphReport.this, listener, year, month, day);
            dpDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 3 * 24 * 60 * 60);
            dpDialog.getDatePicker().setMinDate(datefrom.getTime());
            dpDialog.show();
        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-364 GraphReport txtDateToClicked " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onBtnLoadClicked() {
        try {
            String datefrom = edtDatefrom.getText().toString();
            String dateto = edtDateto.getText().toString();
            if (datefrom.length() == 0 & dateto.length() == 0) {
                edtDatefrom.setError("Select date");
                edtDateto.setError("Select date");

            } else {
                edtDatefrom.setError(null);
                edtDateto.setError(null);
                    if(id1.equals("0"))
                    {
                        Toast.makeText(ActivityGraphReport.this,"Select Counselor Name",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (activtyName.contains("Call Report")) {
                            urlReportDatewise = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=302&DateFrom=" + stringdateFrom + "&DateTo=" + stringdateTo + "&ReportId=1";
                        } else if (activtyName.contains("Message Report")) {
                            urlReportDatewise = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=302&DateFrom=" + stringdateFrom + "&DateTo=" + stringdateTo + "&ReportId=2";
                        } else if (activtyName.contains("Status Report")) {
                            urlReportDatewise = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=306";
                        } else if (activtyName.contains("Remark Report")) {
                            urlReportDatewise = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=302&DateFrom=" + stringdateFrom + "&DateTo=" + stringdateTo + "&ReportId=4";
                        } else if (activtyName.contains("Point Report")) {
                            urlReportDatewise = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=302&DateFrom=" + stringdateFrom + "&DateTo=" + stringdateTo + "&ReportId=5";
                        } else if (activtyName.contains("CallLog Report")) {
                            urlReportDatewise = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=302&DateFrom=" + stringdateFrom + "&DateTo=" + stringdateTo + "&ReportId=6";
                        }
                    }

                Log.d("UrlReportDatewise", urlReportDatewise);
                if (CheckInternetSpeed.checkInternet(ActivityGraphReport.this).contains("0")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
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
                } else if (CheckInternetSpeed.checkInternet(ActivityGraphReport.this).contains("1")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
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

                    dialog = ProgressDialog.show(ActivityGraphReport.this, "", "Loading Datewise Report", true);
                    //  sessionTimeout();
                    getDateWiseReport();
                }
            }
            //  refreshWhenLoading();


            if (linear_recycler_data.getVisibility() == View.VISIBLE) {
                linear_recycler_data.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-365 GraphReport btnLoadClicked " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onTxtDateFromClicked() {
        try {
            listener = new DatePickerDialog.OnDateSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    currentDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        datefrom = simpleDateFormat.parse(currentDate);
                        stringdateFrom = simpleDateFormat.format(datefrom);
                        edtDatefrom.setText(currentDate);
                        if (currentDate.length() == 0) {
                            edtDatefrom.setError("Select date");
                        } else {
                            edtDatefrom.setError(null);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dpDialog = new DatePickerDialog(ActivityGraphReport.this, listener, year, month, day);
            dpDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 3 * 24 * 60 * 60);
            dpDialog.show();
        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-366 GraphReport txtDataFromClicked " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onExportClicked() {
        try {
            int count = 0;
            directory = new File(Environment.getExternalStorageDirectory() + "/Appli");
            directory.mkdirs();

            File sdCardRoot = Environment.getExternalStorageDirectory();
            File yourDir = new File(sdCardRoot, "Appli");

            for (File f : yourDir.listFiles()) {
                if (f.isFile()) {
                    existname = f.getName();
                }
                if ((edtExcelName.getText() + ".xls").equals(existname)) {
                    // edtExcelName.setError("Record Exist");

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
                    alertDialogBuilder.setTitle("File name already exist:")
                            .setMessage("Do you want to replace file?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    saveExcelFile(edtExcelName.getText().toString() + ".xls");
                                    Toast.makeText(ActivityGraphReport.this, "File saved.", Toast.LENGTH_SHORT).show();
                                    edtExcelName.setError(null);
                                    dialog.dismiss();
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    count++;
                }
            }

            if (edtExcelName.getText().length() == 0) {
                edtExcelName.setError("Please Enter Name");
            } else if (count > 0) {
                edtExcelName.setError("Record exist, Rename the record.");
            } else {
                saveExcelFile(edtExcelName.getText().toString() + ".xls");
                Toast.makeText(ActivityGraphReport.this, "File Generated Successfully.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
                alertDialogBuilder.setTitle("Report saved at location:")
                        .setMessage("" + yourDir.getPath() + "/" + edtExcelName.getText().toString() + ".xls")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.dismiss();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        /* .setNegativeButton(android.R.string.no, null)*/
                        .show();
            }
        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-367 GraphReport onExportClicked " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onRadioButtonClicked() {
        try {
            String rVal = rd_all.getText().toString();
            Log.d("SelectedId", rVal);

            if (rVal.contains("All")) {
                if (CheckInternetSpeed.checkInternet(ActivityGraphReport.this).contains("0")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
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
                } else if (CheckInternetSpeed.checkInternet(ActivityGraphReport.this).contains("1")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
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
                    dialog = ProgressDialog.show(ActivityGraphReport.this, "", "Loading report", true);
                    loadAllReport();
                }
            } else {
                dialog.dismiss();
                if (linearLayoutData.getVisibility() == View.GONE) {
                    linearLayoutData.setVisibility(View.VISIBLE);
                    linear_recycler_data.setVisibility(View.GONE);
                    edtDatefrom.getText().clear();
                    edtDateto.getText().clear();
                }
            }
        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-368 GraphReport onRadioBtnClicked " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshWhenLoading() {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if (dialog.isShowing()) {
                    Intent intent = new Intent(ActivityGraphReport.this, ActivityGraphReport.class);
                    //intent.putExtra("Activity",strActivity);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }

    public void sessionTimeout() {
        new CountDownTimer(timeout, 1000) {

            public void onTick(long millisUntilFinished) {
                //mtextView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Intent intent = new Intent(ActivityGraphReport.this, ActivityGraphReport.class);
                    intent.putExtra("ActivityName", activtyName);
                    startActivity(intent);
                    // mtextView.setText("done!");
               /* LayoutInflater li = LayoutInflater.from(getApplicationContext());
                //Creating a view to get the dialog box
                View confirmCall = li.inflate(R.layout.layout_popup_slowinternet, null);
                TextView txtOk = (TextView) confirmCall.findViewById(R.id.txtOkSlowPopoup);

                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(GraphReport.this);
                //Adding our dialog box to the view of alert dialog
                alert.setView(confirmCall);
                //Creating an alert dialog
                alertDialog = alert.create();
                alertDialog.show();

                txtOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // vibrator.vibrate(100);
                        alertDialog.dismiss();
                        editor = sp.edit();
                        editor.putString("Name", null);
                        // editor.putString("Id",null);
                        editor.commit();
                        finish();
                        startActivity(new Intent(GraphReport.this,Login.class));

                    }
                });*/

                }
            }
        }.start();
    }

    public void loadAllReport() {
        try {
            if (id1.equals("0")) {
                dialog.dismiss();
                linearUnderCounselorName.setVisibility(View.GONE);
                Toast.makeText(ActivityGraphReport.this, "Select Counselor Name", Toast.LENGTH_SHORT).show();
            } else {
                linearUnderCounselorName.setVisibility(View.VISIBLE);
            Log.d("Visibility", String.valueOf(linear_recycler_data.getVisibility()));
            linear_recycler_data.setVisibility(View.GONE);
            if (linear_recycler_data.getVisibility() == View.GONE) {
                linearLayoutData.setVisibility(View.GONE);
                linear_recycler_data.setVisibility(View.VISIBLE);

                if (linear_recycler_data.getVisibility() == View.VISIBLE) {

                    //urlReportNo = clientUrl+"?clientid=" + clientId + "&CounselorID=2&caseid=301";
                    if (activtyName.contains("Call Report")) {
                        editor.putString("ReportName", "Call");
                        editor.commit();
                        urlReportNo = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=301&ReportId=1";
                    } else if (activtyName.contains("CallLog Report")) {
                        editor.putString("ReportName", "CallLog");
                        editor.commit();
                        urlReportNo = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=301&ReportId=6";
                    } else if (activtyName.contains("Message Report")) {
                        editor.putString("ReportName", "Message");
                        editor.commit();
                        urlReportNo = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=301&ReportId=2";
                    } else if (activtyName.contains("Status Report")) {
                        editor.putString("ReportName", "Status");
                        editor.commit();
                        urlReportNo = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=301&ReportId=3";
                    } else if (activtyName.contains("Remark Report")) {
                        editor.putString("ReportName", "Remark");
                        editor.commit();
                        urlReportNo = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=301&ReportId=4";
                    } else if (activtyName.contains("Point Report")) {
                        editor.putString("ReportName", "Point");
                        editor.commit();
                        urlReportNo = clientUrl + "?clientid=" + clientId + "&CounselorID=" + id1 + "&caseid=301&ReportId=5";
                    }
                }
                  //  Log.d("UrlReportNo", urlReportNo);
                    //  dialog=ProgressDialog.show(GraphReport.this,"","Loading report",true);
                    if (CheckInternetSpeed.checkInternet(ActivityGraphReport.this).contains("0")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
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
                    } else if (CheckInternetSpeed.checkInternet(ActivityGraphReport.this).contains("1")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
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
                        getAllReport();
                    }
                    // refreshWhenLoading();
           /* JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, urlReportNo, null, new SuccessListenerRecyclerData(), new FailureListenerRecyclerData());
            requestQueue.add(jsonArrayRequest1);*/
                }
            }
        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-369 GraphReport loadAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean saveExcelFile(String fileName) {
        try {
            String path;
            File dir;
            if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                Log.e("Failed", "Storage not available or read only");
                return false;
            }
            success = false;

            //New Workbook
            Workbook workbook = new HSSFWorkbook();

            //Cell style for header row
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            CellStyle cellStyle1 = workbook.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            //New Sheet
            Sheet sheet;
            sheet = workbook.createSheet(edtExcelName.getText().toString());

            // Generate column headings
            Row row;

            row = sheet.createRow(0);

            Cell cellDate, cellTime;

            cellDate = row.createCell(0);
            cellDate.setCellValue("Call Date");
            cellDate.setCellStyle(cellStyle);

            cellTime = row.createCell(1);
            cellTime.setCellValue("Total Calls");
            cellTime.setCellStyle(cellStyle);

            int z = 1;
            for (int k = 0; k < mSampleDataArrayList_table.size(); k++) {
                row = sheet.createRow(z);
                for (int i = 0; i < 2; i++) {
                    cellDate = row.createCell(i);
                    cellDate.setCellValue(mSampleDataArrayList_table.get(k).getDate());
                    cellDate.setCellStyle(cellStyle1);
                }
                for (int j = 1; j < 2; j++) {
                    cellTime = row.createCell(j);
                    cellTime.setCellValue(mSampleDataArrayList_table.get(k).getTime());
                    cellTime.setCellStyle(cellStyle1);
                }
                sheet.setColumnWidth(k, (15 * 350));
                z++;
            }

            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Appli/";
            dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(file);
                workbook.write(fileOutputStream);
                Log.w("FileUtils", "Writing file" + file);
                success = true;
            } catch (IOException e) {
                Log.w("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                Log.w("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != fileOutputStream)
                        fileOutputStream.close();
                } catch (Exception ex) {
                }
            }

        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-370 GraphReport saveExcelFile " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        return success;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    public void getAllReport() {
        try {
            Log.d("UrlReportNo", urlReportNo);
            //if (CheckServer.isServerReachable(GraphReport.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlReportNo,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    dialog.dismiss();
                                    String res = String.valueOf(response);
                                    if (res.contains("[]")) {
                                        txtNotFound.setVisibility(View.VISIBLE);
                                        linearLayoutAll.setVisibility(View.GONE);
                                    } else {
                                        txtNotFound.setVisibility(View.GONE);
                                        linearLayoutAll.setVisibility(View.VISIBLE);

                                    }
                                    mSampleDataArrayList_table = new ArrayList<>();
                                    mSampleDataArrayList_table.clear();
                                    mBarEntryArrayList.clear();
                                    mStringArrayList.clear();
                                    //arrayListFirstGraphX.clear();
                                    //arrayListFirstGraphY.clear();
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("table data success", String.valueOf(response));
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String CallDate = jsonObject1.getString("ReportDate");

                                        int TotalCall = jsonObject1.getInt("TotalReportNo");
                                        String callType = "";
                                        if (activtyName.equals("CallLog Report")) {
                                            callType = jsonObject1.getString("cCallType");
                                            txtCallType.setVisibility(View.VISIBLE);
                                        }

                                        Log.d("data fetch table", CallDate + " " + TotalCall + " ");

                                        DataGraph sampleData = new DataGraph(CallDate, TotalCall, callType);
                                        mSampleDataArrayList_table.add(sampleData);
                                        mBarEntryArrayList.add(new BarEntry(TotalCall, i));
                                        mStringArrayList.add(CallDate);

                                    }
                                    BarDataSet set1 = new BarDataSet(mBarEntryArrayList, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);


                                    BarData data = new BarData(mStringArrayList, set1);
                                    mBarChart.setData(data);

                                    adapterGraph = new AdapterGraph(mSampleDataArrayList_table, getApplicationContext());
                                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                    mRecyclerView_Table_data.setLayoutManager(manager);
                                    mRecyclerView_Table_data.setAdapter(adapterGraph);
                                    adapterGraph.notifyDataSetChanged();
                                } catch (Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(ActivityGraphReport.this, "Errorcode-372 GraphReport getAllReportResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ActivityGraphReport.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            /*} else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GraphReport.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
                        .setMessage("Try after some time!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        *//*edtName.setText("");
                                        edtPassword.setText("");*//*
                                dialog.dismiss();

                            }
                        }).show();
            }*/
        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-371 GraphReport getAllReport " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDateWiseReport() {
        try {
           // if (CheckServer.isServerReachable(GraphReport.this)) {
                Log.d("UrlReportNo", urlReportDatewise);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlReportDatewise,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    dialog.dismiss();
                                    String res = String.valueOf(response);
                                    if (res.contains("[]")) {
                                        txtNotFound.setVisibility(View.VISIBLE);
                                        linearLayoutAll.setVisibility(View.GONE);
                                    } else {
                                        txtNotFound.setVisibility(View.GONE);
                                        linearLayoutAll.setVisibility(View.VISIBLE);

                                    }
                                    mSampleDataArrayList_table = new ArrayList<>();
                                    mSampleDataArrayList_table.clear();
                                    mBarEntryArrayList.clear();
                                    mStringArrayList.clear();
                                    //arrayListFirstGraphX.clear();
                                    //arrayListFirstGraphY.clear();
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("table data success", String.valueOf(response));
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String calltype = "";
                                        if (activtyName.contains("Status Report")) {
                                            callDate = jsonObject1.getString("Status");

                                            totalCall = jsonObject1.getInt("Total No");
                                        } else {
                                            callDate = jsonObject1.getString("ReportDate");

                                            totalCall = jsonObject1.getInt("TotalReportNo");
                                            if (activtyName.equals("CallLog Report")) {
                                                calltype = jsonObject1.getString("cCallType");
                                                txtCallType.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        Log.d("data fetch table", callDate + " " + totalCall + " ");
                                        mBarEntryArrayList.add(new BarEntry(totalCall, i));
                                        mStringArrayList.add(callDate);
                                        DataGraph sampleData = new DataGraph(callDate, totalCall, calltype);
                                        mSampleDataArrayList_table.add(sampleData);
                                        BarDataSet set1 = new BarDataSet(mBarEntryArrayList, " Data Value");
                                        set1.setColors(ColorTemplate.COLORFUL_COLORS);


                                        BarData data = new BarData(mStringArrayList, set1);
                                        mBarChart.setData(data);
                                        mBarChart.invalidate();
                                    }
                                    linear_recycler_data.setVisibility(View.VISIBLE);
                                    adapterGraph = new AdapterGraph(mSampleDataArrayList_table, getApplicationContext());
                                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                    mRecyclerView_Table_data.setLayoutManager(manager);
                                    mRecyclerView_Table_data.setAdapter(adapterGraph);
                                    adapterGraph.notifyDataSetChanged();
                                } catch (Exception e) {
                                    Toast.makeText(ActivityGraphReport.this, "Errorcode-374 GraphReport DatewiseReportResponse" + e.toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
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
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityGraphReport.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ActivityGraphReport.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
           /* } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GraphReport.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
                        .setMessage("Try after some time!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        *//*edtName.setText("");
                                        edtPassword.setText("");*//*
                                dialog.dismiss();

                            }
                        }).show();
            }*/
        } catch (Exception e) {
            Toast.makeText(ActivityGraphReport.this, "Errorcode-373 GraphReport getDatewiseReport" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(ActivityGraphReport.this, ActivityHome.class);
            intent.putExtra("Activity", "GraphReport");
            startActivity(intent);
            finish();
            super.onBackPressed();
            Animatoo.animateSlideRight(ActivityGraphReport.this);
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
    }
}
