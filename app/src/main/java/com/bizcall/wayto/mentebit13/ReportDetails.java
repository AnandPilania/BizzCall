package com.bizcall.wayto.mentebit13;

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
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportDetails extends AppCompatActivity {

    String activtyName,urlReportDetails,clientUrl,clientId,reportname,counselorid,reportdate;
    SharedPreferences sp;
    ProgressDialog dialog;
    UrlRequest urlRequest;
    RecyclerView recyclerView;
    AdapterReportDetails adapterReportDetails;
    ArrayList<DataReportDetails> arrayList;
    ArrayList<DataStatusReport> arrayListStatusReport;
    ImageView imageView;
    Vibrator vibrator;
    TextView txtActivityName,txtDate,txtSrno,txtCandidateName,txtReport;
    String strActName;
    RequestQueue requestQueue;
    String url,userrole;
    Thread thread;
    long timeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_report_details);
            initialize();
            if (reportname.equals("Call")) {
                txtDate.setText("Date");
                txtSrno.setText("Sr No");
                txtCandidateName.setText("Name");
                txtReport.setText("Duration");
                urlReportDetails = clientUrl + "?clientid=" + clientId + "&CounselorID=" + counselorid + "&caseid=305&ReportId=1&ReportDate=" + reportdate;
            }else if (reportname.equals("CallLog")) {
                txtDate.setText("Date");
                txtSrno.setText("Mobile No");
                txtCandidateName.setText("Name");
                txtReport.setText("Duration");
                String calltype=getIntent().getStringExtra("CallType");

                urlReportDetails = clientUrl + "?clientid=" + clientId + "&CounselorID=" + counselorid + "&caseid=305&ReportId=6&ReportDate=" + reportdate+"&CallType="+calltype;
            } else if (reportname.contains("Message")) {
                txtDate.setText("Date");
                txtSrno.setText("Sr No");
                txtCandidateName.setText("Name");
                txtReport.setText("Message");
                urlReportDetails = clientUrl + "?clientid=" + clientId + "&CounselorID=" + counselorid + "&caseid=305&ReportId=2&ReportDate=" + reportdate;
            } else if (reportname.contains("Remark")) {
                txtDate.setText("Date");
                txtSrno.setText("Sr No");
                txtCandidateName.setText("Name");
                txtReport.setText("Remarks");
                urlReportDetails = clientUrl + "?clientid=" + clientId + "&CounselorID=" + counselorid + "&caseid=305&ReportId=4&ReportDate=" + reportdate;
            } else if (reportname.contains("Point")) {
                txtDate.setText("Date");
                txtSrno.setText("Sr No");
                txtCandidateName.setText("Name");
                txtReport.setText("Points");
                urlReportDetails = clientUrl + "?clientid=" + clientId + "&CounselorID=" + counselorid + "&caseid=305&ReportId=5&ReportDate=" + reportdate;
            }
            if (CheckInternetSpeed.checkInternet(ReportDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReportDetails.this);
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
            } else if (CheckInternetSpeed.checkInternet(ReportDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReportDetails.this);
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

                dialog = ProgressDialog.show(ReportDetails.this, "", "Loading", true);
                newThreadInitilization(dialog);
                getAllReport();
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(ReportDetails.this,"Errorcode-375 ReportDetails onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//onCreate

    private void initialize() {
        imageView = findViewById(R.id.img_back);
        requestQueue = Volley.newRequestQueue(ReportDetails.this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        txtActivityName = findViewById(R.id.txtActivityName);
        txtDate = findViewById(R.id.txtDate);
        txtSrno = findViewById(R.id.txtSrno);
        txtCandidateName = findViewById(R.id.txtCandidateN);
        txtReport = findViewById(R.id.txtreport);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        reportname = sp.getString("ReportName", null);
        clientId = sp.getString("ClientId", null);
        clientUrl = sp.getString("ClientUrl", null);
        counselorid = sp.getString("Id", null);
        userrole=sp.getString("Role","");
        timeout=sp.getLong("TimeOut",0);
        counselorid = counselorid.replace(" ", "");
        Log.d("Report", reportname+" "+userrole);
        strActName = txtActivityName.getText().toString();
        strActName = reportname + " " + strActName;
        txtActivityName.setText(strActName);
        reportdate = sp.getString("ReportDate", null);
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
                                Toast.makeText(ReportDetails.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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

    public void getAllReport()
    {
        try {
            Log.d("ReportDetailUrl", urlReportDetails);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlReportDetails,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("ReportResponse", response);

                                dialog.dismiss();
                                arrayList = new ArrayList<>();
                                arrayList.clear();
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("table data success", String.valueOf(response));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String CallDate = jsonObject1.getString("CAll Date");

                                    String cname = jsonObject1.getString("Candidate Name");
                                    String report = jsonObject1.getString("cCallDuration");
                                    String mobileno="",srno="";
                                    DataReportDetails dataReportDetails;
                                    if(reportname.equals("CallLog"))
                                    {
                                         mobileno=jsonObject1.getString("cMobileNo");
                                    }
                                    else {
                                        srno = jsonObject1.getString("Sr No");
                                    }
                                    if(reportname.equals("CallLog")) {
                                        dataReportDetails = new DataReportDetails(mobileno, report, cname, CallDate);
                                    }
                                    else {
                                        dataReportDetails = new DataReportDetails(srno, report, cname, CallDate);
                                    }
                                        arrayList.add(dataReportDetails);

                                }
                                recyclerView = findViewById(R.id.recyclerReportDetails);
                                adapterReportDetails = new AdapterReportDetails(ReportDetails.this, arrayList);
                                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(manager);
                                recyclerView.setAdapter(adapterReportDetails);
                                adapterReportDetails.notifyDataSetChanged();
                            } catch (Exception e) {
                                Toast.makeText(ReportDetails.this,"Errorcode-377 ReportDetails getAllReportReponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReportDetails.this);
                                alertDialogBuilder.setTitle("Network issue!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(ReportDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);

        }catch (Exception e)
        {
            Toast.makeText(ReportDetails.this,"Errorcode-376 ReportDetails getAllReport "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
            Intent intent = new Intent(ReportDetails.this, GraphReport.class);
            intent.putExtra("ActivityName", reportname + " " + "Report");
            startActivity(intent);
            finish();
    }
}
