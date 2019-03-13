package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BulkSmsActivity extends AppCompatActivity
{
    Spinner spinnerRef;
    ArrayList<String> arrayListRefId;
    String clientid,dataFrom,dataFrom1,clienturl;
    long timeout;
    int pos,check=0;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    AlertDialog alertDialog;
    String uname,counselorid,emailid,role,mobile,statusid1,totalcoin;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    RecyclerView recyclerCounselor;
    ArrayList<DataStatusTotal> arrayListTotal;
    AdapterStatusTotalCount adapterTotal;
    ImageView imgBack,imgCoin;
    TextView txtCoin,txtDiamond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_report);

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        totalcoin=sp.getString("TotalCoin",null);
        timeout=sp.getLong("TimeOut",0);
        uname = sp.getString("Name", null);
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replaceAll(" ", "");
        emailid = sp.getString("EmailId", null);
        role = sp.getString("Role", null);
        mobile = sp.getString("MobileNo", null);
        statusid1 = sp.getString("StatusId", null);
        statusid1 = statusid1.replaceAll(" ", "");
        dialog = ProgressDialog.show(BulkSmsActivity.this, "", "Loading...", true);

        imgBack=findViewById(R.id.img_back);
        spinnerRef = findViewById(R.id.spinnerFilter1);
        recyclerCounselor=findViewById(R.id.recyclerStatusTotalCnt1);
        txtCoin=findViewById(R.id.txtCoin);
        txtDiamond=findViewById(R.id.txtDiamond);
        imgCoin=findViewById(R.id.imgCoin);
        imgCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BulkSmsActivity.this,PointCollectionDetails.class));
            }
        });

        arrayListTotal=new ArrayList<>();
        getRefName();
        getAllRefName();
            txtCoin.setText(totalcoin);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        spinnerRef.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pos = position;
                String texttoload = spinnerRef.getSelectedItem().toString();
                editor = sp.edit();
                editor.putString("DataRefName", texttoload);
                editor.commit();
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                //Creating a view to get the dialog box
                View confirmCall = li.inflate(R.layout.layout_confirm_loadtext, null);
                TextView txtYes = (TextView) confirmCall.findViewById(R.id.txtYes);
                TextView txtNo = (TextView) confirmCall.findViewById(R.id.txtNo);
                TextView txtLoad = confirmCall.findViewById(R.id.txtConfirmLoad);
                txtLoad.setText("Do you want to load " + texttoload + "?");
                AlertDialog.Builder alert = new AlertDialog.Builder(BulkSmsActivity.this);
                //Adding our dialog box to the view of alert dialog
                alert.setView(confirmCall);
                //Creating an alert dialog
                alertDialog = alert.create();
                if (++check > 1) {
                    alertDialog.show();
                }

                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pos == 0) {
                            editor = sp.edit();
                            editor.putString("DtaFrom", "0");
                            editor.commit();
                            dialog = ProgressDialog.show(BulkSmsActivity.this, "", "Loading...", true);
                           /* new Thread(new Runnable() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            //  Toast.makeText(Home.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                        }
                                    });*/

                            //getStatusCount();
                            getAllRefName();
                               /* }
                            }).start();*/

                        } else {
                            int p = pos - 1;

                            // DataReference dataReference1=arrayListRefrences.get(position);
                            dataFrom1 = arrayListRefId.get(p);
                            Log.d("RefId", dataFrom1);
                            editor = sp.edit();
                            editor.putString("DtaFrom", dataFrom1);
                            editor.commit();
                            dialog = ProgressDialog.show(BulkSmsActivity.this, "", "Loading...", true);
                           /* new Thread(new Runnable() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            //  Toast.makeText(Home.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                        }
                                    });*/

                            //getStatusCount();
                            getRestRefName();
                              /*  }
                            }).start();*/


                        }
                        alertDialog.dismiss();

                    }
                });

                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerRef.setSelection(0);

            }


        });
    }

    public void getRefName() {
        // arrayListRefrences=new ArrayList<>();
        final ArrayList<String> arrayList1 = new ArrayList<>();
        arrayListRefId = new ArrayList<>();
        arrayList1.add(0, "All");
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=11&CounsellorId=" + counselorid);
        Log.d("StatusCountUrl", clienturl+"?clientid=" + clientid + "&caseid=11&CounsellorId=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("StatusTotalResponse", response);
                try {

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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter(BulkSmsActivity.this, R.layout.spinner_item1, arrayList1);
                    // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRef.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();

                    Log.d("RefIdSize", String.valueOf(arrayListRefId.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getAllRefName() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=12&CounsellorId=" + counselorid);
        Log.d("StatusCountUrl", clienturl+"?clientid=" + clientid + "&caseid=12&CounsellorId=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("StatusTotalResponse", response);
                try {
                    arrayListTotal.clear();
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

                  AdapterStatusReport  adapterStatusReport = new AdapterStatusReport(arrayListTotal, BulkSmsActivity.this);
                    //recyclerCounselor = findViewById(R.id.recyclerStatusTotalCnt);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(BulkSmsActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerCounselor.setLayoutManager(layoutManager);
                    recyclerCounselor.setAdapter(adapterStatusReport);
                    adapterStatusReport.notifyDataSetChanged();


                    //   Log.d("Size**", String.valueOf(arrayList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getRestRefName() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=13&CounsellorId=" + counselorid + "&DataFrom=" + dataFrom1);
        Log.d("StatusCountUrl", clienturl+"?clientid=" + clientid + "&caseid=13&CounsellorId=" + counselorid + "&DataFrom=" + dataFrom1);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("StatusTotalResponse", response);
                try {
                    arrayListTotal.clear();
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

                    AdapterStatusReport adapterStatusReport = new AdapterStatusReport(arrayListTotal, BulkSmsActivity.this);
                   // recyclerCounselor = findViewById(R.id.recyclerStatusTotalCnt);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(BulkSmsActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerCounselor.setLayoutManager(layoutManager);
                    recyclerCounselor.setAdapter(adapterStatusReport);
                    adapterStatusReport.notifyDataSetChanged();
                    //   Log.d("Size**", String.valueOf(arrayList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
