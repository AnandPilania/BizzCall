package com.bizcall.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchAllActivity extends AppCompatActivity {

    EditText edtSearchText;
    ImageView imgRefresh,imgBack;
    Spinner spinnerSearchAs;
    String searchtext,searchAs,clientid,cid;
    ArrayList<String> arrayListSearchAs;
    AdapterSearchCounselorData adapterCounselorData;
    RecyclerView recyclerViewCounselor;
    ArrayList<DataCounselor> arraylistCounselor;
    UrlRequest urlRequest;
    SharedPreferences sp;
    ProgressDialog dialog;
    TextView txtNotFound;
    String clienturl,currentstatus;
    long timeout;
    TextView txtAsc,txtDesc;
    String strMin,strMax;
    TextView txtMin,txtMax,txtDisplayInfo;
    Vibrator vibrator;
    Button btnNext,btnPreviuos;
    String total;
    int searchbool=0;
    String searchboolUrl="",searchbooltext="",searchboolAs="", orderboolAs="";
    RequestQueue requestQueue;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);
        try
        {
            requestQueue=Volley.newRequestQueue(SearchAllActivity.this);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
       edtSearchText=findViewById(R.id.edtSearchtext);
       imgRefresh=findViewById(R.id.imgRefresh);
       // imgSearch=findViewById(R.id.img_search1);
        spinnerSearchAs=findViewById(R.id.spinnerFilter1);
        txtNotFound=findViewById(R.id.txtNotFound);
        arrayListSearchAs=new ArrayList<>();
        imgBack=findViewById(R.id.img_backSearch);
        txtAsc=findViewById(R.id.txtAsc);
        txtDesc=findViewById(R.id.txtDesc);
        txtMin=findViewById(R.id.txtMin);
        txtMax=findViewById(R.id.txtMax);
        btnNext=findViewById(R.id.btnLoadMore);
        btnPreviuos=findViewById(R.id.btnLoadPrevious);
        txtDisplayInfo=findViewById(R.id.txtDisplayInfo);

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        currentstatus=sp.getString("SStatusId", null);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        timeout=sp.getLong("TimeOut",0);
        cid = sp.getString("Id", null);
        cid=cid.replace(" ","");

        arrayListSearchAs.add("Serial No");
        arrayListSearchAs.add("Candidate Name");
        arrayListSearchAs.add("Course");
        arrayListSearchAs.add("Mobile");
        arrayListSearchAs.add("Email");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(SearchAllActivity.this, R.layout.spinner_item1, arrayListSearchAs);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearchAs.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        spinnerSearchAs.setSelection(0);
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(SearchAllActivity.this,SearchAllActivity.class);
                   // intent.putExtra("Activity",activityName);
                    startActivity(intent);
                }
            });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
              //  startActivity(new Intent(SearchAllActivity.this,Home.class));
            }
        });
            strMin="1";
            strMax="25";
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying "+txtMin.getText().toString()+"-"+txtMax.getText().toString());

        txtAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchtext = edtSearchText.getText().toString().trim();
                searchtext=searchtext.replaceAll("'","").trim();
                txtMin.setText("1");
                txtMax.setText("25");
                txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
                        //  status = status.replaceAll(" ", "");

                searchAs = spinnerSearchAs.getSelectedItem().toString();
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
                }

                if (searchtext.length() == 0) {
                    // searchData("", searchAs + " ASC");
                    edtSearchText.setError("Please insert text");
                    // getCounselor1(cid, statusid, datafrom);
                } else {
                    strMin=txtMin.getText().toString();
                    strMax=txtMax.getText().toString();
                    searchbool=1;
                    searchbooltext=searchtext;
                    searchboolAs=searchAs;
                    orderboolAs=searchAs + " ASC";
                    if(CheckInternetSpeed.checkInternet(SearchAllActivity.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SearchAllActivity.this);
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
                    else if(CheckInternetSpeed.checkInternet(SearchAllActivity.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SearchAllActivity.this);
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
                        dialog = ProgressDialog.show(SearchAllActivity.this, "", "Searching data", true);
                        searchData(searchtext, searchAs, searchAs + " ASC", strMin, strMax);
                    }
                }
            }
                  });
        txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAs = spinnerSearchAs.getSelectedItem().toString();
                searchtext = edtSearchText.getText().toString().trim();
                searchtext=searchtext.replaceAll("'","").trim();
                txtMin.setText("1");
                txtMax.setText("25");
                txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());

                if(searchAs.contains("Serial No"))
                {
                    searchAs="nSrNo";
                }
                else if (searchAs.contains("Candidate Name"))
                {
                    searchAs="cCandidateName";
                }
                else if(searchAs.contains("Course"))
                {
                    searchAs="cCourse";
                }
                else if(searchAs.contains("Mobile"))
                {
                    searchAs="cMobile";
                }
                else if(searchAs.contains("Email"))
                {
                    searchAs="cEmail ";
                }
                else if(searchAs.contains("Parent No"))
                {
                    searchAs="cParantNo ";
                }
                else if(searchAs.contains("Allocation Date"))
                {
                    searchAs="AllocationDate";
                }


                if(searchtext.length()==0)
                {
                    edtSearchText.setError("Please insert text");
                }
                else {

                    strMin=txtMin.getText().toString();
                    strMax=txtMax.getText().toString();
                    searchbool=1;
                    searchbooltext=searchtext;
                    searchboolAs=searchAs;
                    orderboolAs=searchAs + " ASC";
                    if(CheckInternetSpeed.checkInternet(SearchAllActivity.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SearchAllActivity.this);
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
                    else if(CheckInternetSpeed.checkInternet(SearchAllActivity.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SearchAllActivity.this);
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
                        dialog = ProgressDialog.show(SearchAllActivity.this, "", "Searching data", true);
                        searchData(searchtext, searchAs, searchAs + " DESC", strMin, strMax);
                    }
                   // refreshWhenLoading();
                        // getCounselor1(cid, statusid, datafrom);
                }
            }
        });
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(searchbool==1)
                    {
                        strMin= String.valueOf(Integer.parseInt(txtMin.getText().toString())+25);
                        strMax= String.valueOf(Integer.parseInt(txtMax.getText().toString())+25);
                        txtMin.setText(strMin);
                        txtMax.setText(strMax);

                        dialog=ProgressDialog.show(SearchAllActivity.this,"","Searching data",true);
                        searchData(searchbooltext,searchboolAs, orderboolAs,txtMin.getText().toString(),txtMax.getText().toString());
                           // refreshWhenLoading();
                    }

                    txtDisplayInfo.setText("Displaying "+txtMin.getText().toString()+"-"+txtMax.getText().toString());

                }
            });
            btnPreviuos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strMin= String.valueOf(Integer.parseInt(txtMin.getText().toString())-25);
                    strMax= String.valueOf(Integer.parseInt(txtMax.getText().toString())-25);
                    txtMin.setText(strMin);
                    txtMax.setText(strMax);
                     /* searchbool=1;
                        searchbooltext=searchtext;
                        searchboolAs=searchAs;
                        orderboolAs=searchAs + " ASC";*/

                    txtDisplayInfo.setText("Displaying "+txtMin.getText().toString()+"-"+txtMax.getText().toString());
                    if(CheckInternetSpeed.checkInternet(SearchAllActivity.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SearchAllActivity.this);
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
                    else if(CheckInternetSpeed.checkInternet(SearchAllActivity.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SearchAllActivity.this);
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

                        dialog = ProgressDialog.show(SearchAllActivity.this, "", "Searching data", true);
                        searchData(searchbooltext, searchboolAs, orderboolAs, txtMin.getText().toString(), txtMax.getText().toString());
                    }
                 //   refreshWhenLoading();
                }
            });

            } catch (Exception e) {
            Toast.makeText(SearchAllActivity.this,"Got exception,can't load",Toast.LENGTH_SHORT);
            Log.d("SearchException", String.valueOf(e));
        }
        }//onCreate
    public void refreshWhenLoading()
    {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if(dialog.isShowing()) {
                    Intent intent = new Intent(SearchAllActivity.this, SearchAllActivity.class);
                  //  intent.putExtra("Activity",strActivity);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }
    public void searchData(String searchVal, String searchAs, String orderval, String strMin, final String strMax) {

        url=clienturl+"?clientid="+clientid+"&caseid=105&AllocatedTo="+cid+"&FieldName="+searchAs+"&FieldVal="+searchVal+"&OrderVal="+orderval+"&MinVal="+strMin+"&MaxVal="+strMax;
        arraylistCounselor = new ArrayList<>();
        Log.d("SearchUrl",url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        int mval=Integer.parseInt(txtMax.getText().toString());
                        Log.d("MaxVal", String.valueOf(mval));
                        if(response.contains("[]"))
                        {
                            txtNotFound.setVisibility(View.VISIBLE);

                        }
                        else
                        {
                            txtNotFound.setVisibility(View.GONE);

                        }

                        Log.d("SearchResponse", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // Log.d("Json",jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String strSNO=jsonObject1.getString("SNO");
                                String srno = jsonObject1.getString("nSrNo");
                                String cname = jsonObject1.getString("cCandidateName");
                                String course = jsonObject1.getString("cCourse");
                                String parentno = jsonObject1.getString("cParantNo");
                                String mobile = jsonObject1.getString("cMobile");
                                String email = jsonObject1.getString("cEmail");
                                String allocationdate1 = jsonObject1.getString("AllocationDate");
                                String adrs = jsonObject1.getString("cAddressLine");
                                String city = jsonObject1.getString("cCity");
                                String state = jsonObject1.getString("cState");
                                String pincode = jsonObject1.getString("cPinCode");
                                String statusId=jsonObject1.getString("CurrentStatus");
                                String statusStr=jsonObject1.getString("cStatus");
                                String remarks=jsonObject1.getString("cRemarks");
                                //String datafrom=jsonObject1.getString("cDataFrom");

                                Log.d("Status11", srno);
                                DataCounselor dataCounselor = new DataCounselor(strSNO,srno, cname, course, mobile, parentno, email, allocationdate1, adrs, city, state, pincode,statusId,statusStr,remarks);
                                arraylistCounselor.add(dataCounselor);
                            }
                            Log.d("SearchArraySize", String.valueOf(arraylistCounselor.size()));
                            adapterCounselorData = new AdapterSearchCounselorData(SearchAllActivity.this, arraylistCounselor);
                            recyclerViewCounselor = findViewById(R.id.recyclerCounselorData1);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerViewCounselor.setLayoutManager(layoutManager);
                            recyclerViewCounselor.setAdapter(adapterCounselorData);
                            adapterCounselorData.notifyDataSetChanged();

                            if(txtMin.getText().toString().equals("1")) {
                                btnNext.setVisibility(View.GONE);
                                btnPreviuos.setVisibility(View.GONE);
                                if (Integer.parseInt(txtMax.getText().toString()) <= arraylistCounselor.size()) {
                                    btnNext.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    btnNext.setVisibility(View.GONE);

                                }
                            }
                            else if(25>arraylistCounselor.size())
                            {
                                btnPreviuos.setVisibility(View.VISIBLE);
                                btnNext.setVisibility(View.GONE);
                            }
                            else
                            {
                                btnNext.setVisibility(View.VISIBLE);
                                btnPreviuos.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
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
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SearchAllActivity.this);
                            alertDialogBuilder.setTitle("Server Error!!!")


                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).show();
                            dialog.dismiss();
                            Toast.makeText(SearchAllActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
                            // showCustomPopupMenu();
                            Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                        }

                    }
                });
        requestQueue.add(stringRequest);
        }

    @Override
    public void onBackPressed() {
        try{
        Intent intent=new Intent(SearchAllActivity.this,Home.class);
        intent.putExtra("Activity","SearchAllActivity");
        startActivity(intent);
        super.onBackPressed();
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
    }
}
