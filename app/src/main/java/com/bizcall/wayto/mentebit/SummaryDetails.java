package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SummaryDetails extends AppCompatActivity {

    TextView txtSummTitle;
    ImageView imgRight;
    String strDocNM, strDoccount,strName;

    ArrayList<DataSummary> personalinfoArrayList;
    ArrayList<DataSummary> educationsArrayList;
    ArrayList<DataSummary> documentsArrayList;
    AdapterInfoSummary adapterPersonalInfo, adapterEducation, adapterDocuments;
    DataSummary detailsSummary;
    int flag = 0;

    SharedPreferences sp;
    ProgressDialog progressBar;
    RequestQueue requestQueue;
    String clienturl,clientid,sharedSrno,sharedMobile,cname;
    SharedPreferences.Editor editor;
    TextView txtPersonal,txtEducational,txtUplaodDoc,txtFileNo,txtMobileNo,txtSms,txtCollegeAttorny;
    ImageView imgBack;
    RecyclerView recyclerPersonalInfo,recyclerEducation,recyclerDocuments;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_summary_details);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            txtPersonal = findViewById(R.id.txt_personal);
            txtEducational = findViewById(R.id.txt_education);
            txtUplaodDoc = findViewById(R.id.txt_documents);
            imgBack = findViewById(R.id.img_back);
            txtFileNo = findViewById(R.id.txtFileNo);
            txtMobileNo = findViewById(R.id.txtMobileNo);
            txtSms = findViewById(R.id.txt_sms);
            txtCollegeAttorny = findViewById(R.id.txt_college);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            strName = sp.getString("FullName", null);
            clienturl = sp.getString("ClientUrl", null);
            clientid = sp.getString("ClientId", null);
            sharedSrno = sp.getString("FileNo", null);
            sharedMobile = sp.getString("MobileNo", null);

           /* editor = sp.edit();
            editor.putString("FileNo", sharedSrno);
            editor.commit();*/

            txtSummTitle = findViewById(R.id.txt_summ_title);
            imgRight = findViewById(R.id.img_tickmark);
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

            //---------------------------------------------------------------
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
            txtSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor=sp.edit();
                    editor.putString("CName", strName);
                    editor.putString("SelectedMobile", sharedMobile);
                    editor.putString("SelectedSrNo",sharedSrno);
                    editor.commit();
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
                    uploaddocs.putExtra("FileNo", sharedSrno);
                    uploaddocs.putExtra("MobileNo", sharedMobile);
                    startActivity(uploaddocs);
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(SummaryDetails.this,"Errorcode-311 SummaryDetails onCreate "+e.toString(),Toast.LENGTH_SHORT).show();        }
    }//onCreate

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SummaryDetails.this,MasterEntry.class);
        startActivity(intent);
    }

    private void loadSummaryData(String url) {
        try{
            if(CheckServer.isServerReachable(SummaryDetails.this)) {
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
            }else {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SummaryDetails.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
         Toast.makeText(SummaryDetails.this,"Errorcode-312 SummaryDetails loadSummaryData "+e.toString(),Toast.LENGTH_SHORT).show();
     }
    }


}
