package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.File;

public class FilterLeads extends AppCompatActivity {
  Button btnAdmissionForm,btnSeperateInternational,btnOnlineFaulty,btnWrongData,btnWhatsappLeads;
  RequestQueue requestQueue;
  UrlRequest urlRequest;
  ProgressDialog dialog;
  String clienturl="",clientid="";
  long timeout;
  SharedPreferences sp;
  SharedPreferences.Editor editor;
  Thread thread;
  String counselorid;
  ImageView imgBack,imgRefresh;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_leads);
        requestQueue = Volley.newRequestQueue(FilterLeads.this);
        btnAdmissionForm=findViewById(R.id.btnAdmissionForm);
        btnSeperateInternational=findViewById(R.id.btnSeperateInternational);
        btnWhatsappLeads=findViewById(R.id.btnWhatsappLeads);
        btnOnlineFaulty=findViewById(R.id.btnOnlineFaulty);
        btnWrongData=findViewById(R.id.btnWrongData);
        imgBack=findViewById(R.id.img_back);
        imgRefresh=findViewById(R.id.imgRefresh);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        counselorid=sp.getString("Id","");
        timeout=sp.getLong("TimeOut",0);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterLeads.this, FilterLeads.class);
              //  intent.putExtra("Activity", "FilterLead");
                //intent.putExtra("MainMenu",MainMenuselected);
                startActivity(intent);
                finish();
            }
        });
        btnWhatsappLeads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=ProgressDialog.show(FilterLeads.this,"","Adding whatsapp leads to open leads",true);
                newThreadInitilization(dialog);
                whastappLeadsToOpenLeads();
            }
        });
        btnWrongData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=ProgressDialog.show(FilterLeads.this,"","Removing wrong data",true);
                newThreadInitilization(dialog);
                removeWrongData();
            }
        });
        btnOnlineFaulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=ProgressDialog.show(FilterLeads.this,"","Removing Faulty Data",true);
                newThreadInitilization(dialog);
                updateOnlineLead();
            }
        });
        btnSeperateInternational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=ProgressDialog.show(FilterLeads.this,"","Seperating international leads",true);
                newThreadInitilization(dialog);
                seperateInternational();
            }
        });
        btnAdmissionForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=ProgressDialog.show(FilterLeads.this,"","Inserting admission form",true);
                newThreadInitilization(dialog);
                admissionForm();
            }
        });

    }
    @Override
    public void onBackPressed() {

                Intent intent = new Intent(FilterLeads.this, Home.class);
                intent.putExtra("Activity", "FilterLead");
                //intent.putExtra("MainMenu",MainMenuselected);
                startActivity(intent);
                finish();

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
                                Toast.makeText(FilterLeads.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
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
    public void whastappLeadsToOpenLeads()
    {
        try {
            urlRequest = UrlRequest.getObject();
            urlRequest.setContext(FilterLeads.this);
            String urlSatatus =clienturl+"?clientid="+clientid+"&caseid=186";
            urlRequest.setUrl(urlSatatus);
            Log.d("WhatsappLeadUrl", urlSatatus);
            urlRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    try {
                        dialog.dismiss();
                        Log.d("WhatsappLeadRes", response);
                        if (response.contains("Data updated successfully")) {
                            // getOnlineLeadRefno(sr_onlinelead);
                            //dialog.dismiss();

                            Intent intent = new Intent(FilterLeads.this, FilterLeads.class);
                            // intent.putExtra("Activity", actname);
                            startActivity(intent);
                            Toast.makeText(FilterLeads.this, "Whatsapp leads added successfully to open leads", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FilterLeads.this, "Whatsapp leads not added to open leads ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //Toast.makeText(FilterLeads.this, "Errorcode-405 AdapterOnlineLead updateOnlineLeadResponse", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e)
        {
            // Toast.makeText(FilterLeads.this, "Errorcode-404 AdapterOnlineLead updateOnlineLead", Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void removeWrongData()
    {
        try {
            urlRequest = UrlRequest.getObject();
            urlRequest.setContext(FilterLeads.this);
            String urlSatatus =clienturl+"?clientid="+clientid+"&caseid=184&CounselorID="+counselorid;
            urlRequest.setUrl(urlSatatus);
            Log.d("removeWrongUrl", urlSatatus);
            urlRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    try {
                        dialog.dismiss();
                        Log.d("removeWrongRes", response);
                        if (response.contains("Row updated successfully")) {
                            // getOnlineLeadRefno(sr_onlinelead);
                            //dialog.dismiss();

                            Intent intent = new Intent(FilterLeads.this, FilterLeads.class);
                            // intent.putExtra("Activity", actname);
                            startActivity(intent);
                            Toast.makeText(FilterLeads.this, "Wrong data removed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FilterLeads.this, "Wrong data not removed ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //Toast.makeText(FilterLeads.this, "Errorcode-405 AdapterOnlineLead updateOnlineLeadResponse", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e)
        {
            // Toast.makeText(FilterLeads.this, "Errorcode-404 AdapterOnlineLead updateOnlineLead", Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void updateOnlineLead()
    {
        try {
            urlRequest = UrlRequest.getObject();
            urlRequest.setContext(FilterLeads.this);
            String urlSatatus =clienturl+"?clientid="+clientid+"&caseid=179";
            urlRequest.setUrl(urlSatatus);
            Log.d("updateOnlineUrl", urlSatatus);
            urlRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    try {
                        dialog.dismiss();
                        Log.d("updateOnlineRes", response);
                        if (response.contains("Row updated successfully")) {
                            // getOnlineLeadRefno(sr_onlinelead);
                            //dialog.dismiss();
                            Intent intent = new Intent(FilterLeads.this, FilterLeads.class);
                            // intent.putExtra("Activity", actname);
                            startActivity(intent);

                            Toast.makeText(FilterLeads.this, "Faulty data removed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FilterLeads.this, "Faulty data not removed ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //Toast.makeText(FilterLeads.this, "Errorcode-405 AdapterOnlineLead updateOnlineLeadResponse", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e)
        {
            // Toast.makeText(FilterLeads.this, "Errorcode-404 AdapterOnlineLead updateOnlineLead", Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void admissionForm()
    {
        try {
            urlRequest = UrlRequest.getObject();
            urlRequest.setContext(FilterLeads.this);
            String urlSatatus =clienturl+"?clientid="+clientid+"&caseid=183";
            urlRequest.setUrl(urlSatatus);
            Log.d("AdmissionFormUrl", urlSatatus);
            urlRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    try {
                        dialog.dismiss();
                        Log.d("AdmissionFormRes", response);
                        if (response.contains("Data inserted successfully")) {
                            // getOnlineLeadRefno(sr_onlinelead);
                            //dialog.dismiss();

                                Intent intent = new Intent(FilterLeads.this, FilterLeads.class);
                               // intent.putExtra("Activity", actname);
                                startActivity(intent);

                            Toast.makeText(FilterLeads.this, "Admission form updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FilterLeads.this, "Admission form not updated ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //Toast.makeText(FilterLeads.this, "Errorcode-405 AdapterOnlineLead updateOnlineLeadResponse", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e)
        {
           // Toast.makeText(FilterLeads.this, "Errorcode-404 AdapterOnlineLead updateOnlineLead", Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }

    public void seperateInternational()
    {
        try {
            urlRequest = UrlRequest.getObject();
            urlRequest.setContext(FilterLeads.this);
            String urlSatatus =clienturl+"?clientid="+clientid+"&caseid=180";
            urlRequest.setUrl(urlSatatus);
            Log.d("internationalUrl", urlSatatus);
            urlRequest.getResponse(new ServerCallback() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    try {
                        dialog.dismiss();
                        Log.d("internationalRes", response);
                        if (response.contains("Row updated successfully")) {
                            // getOnlineLeadRefno(sr_onlinelead);
                            //dialog.dismiss();

                            Intent intent = new Intent(FilterLeads.this, FilterLeads.class);
                            // intent.putExtra("Activity", actname);
                            startActivity(intent);

                            Toast.makeText(FilterLeads.this, "International lead updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FilterLeads.this, "International lead not updated ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //Toast.makeText(FilterLeads.this, "Errorcode-405 AdapterOnlineLead updateOnlineLeadResponse", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e)
        {
            // Toast.makeText(FilterLeads.this, "Errorcode-404 AdapterOnlineLead updateOnlineLead", Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
}
