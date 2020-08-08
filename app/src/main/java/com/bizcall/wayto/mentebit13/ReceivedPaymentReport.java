package com.bizcall.wayto.mentebit13;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceivedPaymentReport extends AppCompatActivity {

    String month="",year="",clienturl="",clientid="",url="",counselorId="";
    String fileno="",fname="",lname="",refno="",amountINR="",paymentdate="",paymentMode="",paymentFrom="",paymentremarks="",receiptname="",currentLocalRate="",approvalremarks="";
    RequestQueue requestQueue;
    SharedPreferences sp;
    ProgressDialog dialog, dialog1;
    long timeout;
    Spinner spinnerApprovedFor;
    RecyclerView recyclerReceivedPayment;
    EditText edtSearchtext;
    AdapterReceivedPaymentReport adapterReceivedPaymentReport;
    ArrayList<DataReceivedPaymentReport> arrayListReceivedPayment;
    DataReceivedPaymentReport dataReceivedPaymentReport;
    TextView txtLoad,txtNoDetails;
    Thread thread;
    ArrayList<String> arrayListActNames;
    ArrayList<DataAccountName> arrayListAccountName;
    String acountid="",acntname="",servicetax="",accountno="";
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_negi_payment_receive);
            //to initialize all controls and variables
            initialize();
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            edtSearchtext.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    // TODO Auto-generated method stub
                    Log.d("CountTapped", count + "");
                    if (count == 0) {
                        if (edtSearchtext.getText().toString().length() == 0) {
                            //to get list of all received payments
                            getReceivedPayment(url);
                        /*adapterAllAmountDetails = new AdapterAllAmountDetails(PaymentApprove.this, arrayListAllAmount);
                        recyclerAllAmount.setAdapter(adapterAllAmountDetails);
                        adapterAllAmountDetails.notifyDataSetChanged();*/
                        } else {
                            //to get text entered in searchview and compare it with displayedlist
                            filter(edtSearchtext.getText().toString());
                        }
                    } else {
                        //to get text entered in searchview and compare it with displayedlist
                        filter(edtSearchtext.getText().toString());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {

                    // filter your list from your input

                    //you can use runnable postDelayed like 500 ms to delay search text
                }
            });
            if (CheckInternetSpeed.checkInternet(ReceivedPaymentReport.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReceivedPaymentReport.this);
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
            } else if (CheckInternetSpeed.checkInternet(ReceivedPaymentReport.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReceivedPaymentReport.this);
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
                dialog = ProgressDialog.show(ReceivedPaymentReport.this, "", "Getting received amount details", true);
                newThreadInitilization(dialog);
                //to get list of payment modes to be displayed on dropdown
                //after selecting payment mode option from dropdown you will get list of all received payment
                getPaymentMode();
            }


            txtLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String acountname = spinnerApprovedFor.getSelectedItem().toString();
                    int pos = spinnerApprovedFor.getSelectedItemPosition();
                    String acntid = arrayListAccountName.get(pos).getAccountid();
                    if (acountname.equals("All")) {
                        url = clienturl + "?clientid=" + clientid + "&caseid=135&Step=2";
                    } else {
                        url = clienturl + "?clientid=" + clientid + "&caseid=135&Step=1&AccountID=" + acntid;
                    }


                    //recyclerAllAmount.setVisibility(View.VISIBLE);
                    //linearTitle.setVisibility(View.VISIBLE);
                    // allAmountUrl = clienturl + "?clientid=" + clientid + "&caseid=131";
                    if (CheckInternetSpeed.checkInternet(ReceivedPaymentReport.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReceivedPaymentReport.this);
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
                    } else if (CheckInternetSpeed.checkInternet(ReceivedPaymentReport.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReceivedPaymentReport.this);
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
                        dialog = ProgressDialog.show(ReceivedPaymentReport.this, "", "Getting received amount details", true);
                        newThreadInitilization(dialog);
                        //to get list of all received payments under selected payment mode
                        getReceivedPayment(url);
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(ReceivedPaymentReport.this, "Errorcode-506 ReceivedPaymentReport onCreate " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize() {
        requestQueue = Volley.newRequestQueue(ReceivedPaymentReport.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        timeout = sp.getLong("TimeOut", 0);
        counselorId = sp.getString("Id", "");
        counselorId = counselorId.replace(" ", "");

        spinnerApprovedFor = findViewById(R.id.spinnerApprovedfor);
        recyclerReceivedPayment = findViewById(R.id.recyclerReceivedPayment);
        txtLoad = findViewById(R.id.txtLoad);
        edtSearchtext = findViewById(R.id.edtSearchtext);
        txtNoDetails = findViewById(R.id.txtNoDetails);
        imgBack = findViewById(R.id.img_back);

    }

    @Override
    public void onBackPressed() {
        try {
           // super.onBackPressed();
            Intent intent = new Intent(ReceivedPaymentReport.this, Home.class);
            intent.putExtra("Activity", "AccountActivity");
            startActivity(intent);
            finish();
        }catch (Exception e)
        {
            Toast.makeText(ReceivedPaymentReport.this, "Errorcode-507 ReceivedPaymentReport onBackpressed " + e.toString(), Toast.LENGTH_SHORT).show();
        }
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
                                Toast.makeText(ReceivedPaymentReport.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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

    public void updateList(ArrayList<DataReceivedPaymentReport> list){
        try {
            arrayListReceivedPayment = list;
        /*if(arrayListAllAmount.size()==0)
        {
            linearTitle.setVisibility(View.GONE);
            txtNoDetails.setVisibility(View.VISIBLE);
            recyclerAllAmount.setVisibility(View.GONE);
        }else {*/
            adapterReceivedPaymentReport = new AdapterReceivedPaymentReport(ReceivedPaymentReport.this, arrayListReceivedPayment);
            recyclerReceivedPayment.setAdapter(adapterReceivedPaymentReport);
            adapterReceivedPaymentReport.notifyDataSetChanged();
        }catch (Exception e)
        {
            Toast.makeText(ReceivedPaymentReport.this, "Errorcode-508 ReceivedPaymentReport updateList " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        //}
    }//updateList
    void filter(String text){
        try{
        ArrayList<DataReceivedPaymentReport> temp = new ArrayList();
        for(DataReceivedPaymentReport d: arrayListReceivedPayment){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getFileno().contains(text)||d.getFname().contains(text)||d.getAmountINR().contains(text)||d.getLname().contains(text)||
                    d.getRefno().contains(text)||d.getPaymentFrom().contains(text)||d.getPaymentMode().contains(text)){
                temp.add(d);
            }

        }
        //update recyclerview
        if(temp.size()==0)
        {
            recyclerReceivedPayment.setVisibility(View.GONE);
           // linearTitle.setVisibility(View.VISIBLE);
            txtNoDetails.setVisibility(View.VISIBLE);
        }
        else {
            recyclerReceivedPayment.setVisibility(View.VISIBLE);
            //linearTitle.setVisibility(View.VISIBLE);
            txtNoDetails.setVisibility(View.GONE);
            updateList(temp);
        }
        }catch (Exception e)
        {
            Toast.makeText(ReceivedPaymentReport.this, "Errorcode-509 ReceivedPaymentReport filter " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//filter

    public boolean getReceivedPayment(final String url1) {
        try {
            if(CheckServer.isServerReachable(ReceivedPaymentReport.this)) {

                Log.d("ReceivedPaymentUrl", url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    dialog.dismiss();
                                    arrayListReceivedPayment=new ArrayList<>();
                                    arrayListReceivedPayment.clear();
                                    //arrayListInwardUSD.clear();
                                    //arrayListInwardINR.clear();

                                    Log.d("ReceivedPaymentResponse", response);
                                    if(response.contains("[]"))
                                    {
                                        edtSearchtext.setVisibility(View.GONE);
                                        txtNoDetails.setVisibility(View.VISIBLE);
                                    }
                                    {
                                        edtSearchtext.setVisibility(View.VISIBLE);
                                        txtNoDetails.setVisibility(View.GONE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            fileno = jsonObject1.getString("nFileNo");
                                            fname = jsonObject1.getString("cCandidateFName");
                                            lname = jsonObject1.getString("cCandidateLName");
                                            paymentdate = jsonObject1.getString("Payment Date");
                                            paymentMode = jsonObject1.getString("cPaymentMode");
                                            amountINR = jsonObject1.getString("cAmountINR");
                                            paymentFrom = jsonObject1.getString("cPaymentFrom");
                                            currentLocalRate = jsonObject1.getString("cCurrentLocalRateD");
                                            receiptname = jsonObject1.getString("ReceiptName");
                                            paymentremarks = jsonObject1.getString("Payment remarks");
                                            approvalremarks = jsonObject1.getString("Approval remarks");
                                            refno = jsonObject1.getString("cRefNo");

                                            // Log.d("Json11111",arrayList1.toString());
                                            dataReceivedPaymentReport = new DataReceivedPaymentReport(fileno, fname, lname, refno, amountINR, paymentdate, paymentMode, paymentFrom, paymentremarks, receiptname, currentLocalRate, approvalremarks);
                                            arrayListReceivedPayment.add(dataReceivedPaymentReport);
                                        }
                                        adapterReceivedPaymentReport = new AdapterReceivedPaymentReport(ReceivedPaymentReport.this, arrayListReceivedPayment);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(ReceivedPaymentReport.this);
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerReceivedPayment.setLayoutManager(layoutManager);
                                        recyclerReceivedPayment.setAdapter(adapterReceivedPaymentReport);
                                        adapterReceivedPaymentReport.notifyDataSetChanged();

                                    }
                                } catch (JSONException e) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(ReceivedPaymentReport.this, "Errorcode-511 ReceivedPaymentReport getReceivedPaymentResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReceivedPaymentReport.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(ReceivedPaymentReport.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReceivedPaymentReport.this);
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
            return true;
        }catch (Exception e)
        {
            Toast.makeText(ReceivedPaymentReport.this, "Errorcode-510 ReceivedPaymentReport getReceivedPayment " + e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }//getReceivedPayment
    public void getPaymentMode()
    {
        try {
            arrayListAccountName = new ArrayList<>();
            arrayListActNames=new ArrayList<>();
            //  arrayListActNames.add(1,"All");

            String url = clienturl + "?clientid=" + clientid + "&caseid=118";
            Log.d("BankNamesUrl", url);
            if (CheckInternet.checkInternet(ReceivedPaymentReport.this)) {
                if(CheckServer.isServerReachable(ReceivedPaymentReport.this)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        Log.d("BankNamesResponse", response);
                                         dialog.dismiss();

                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            accountno = jsonObject1.getString("cAccountNo");
                                            acountid = jsonObject1.getString("nAccountID");
                                            acntname = jsonObject1.getString("cAccountName");
                                            servicetax = jsonObject1.getString("cServiceTax");
                                            DataAccountName dataAccountName=new DataAccountName(acntname,acountid,servicetax,accountno);
                                            arrayListAccountName.add(dataAccountName);
                                            arrayListActNames.add(acntname);

                                        }
                                        if(arrayListActNames.get(0).contains("Select"))
                                        {
                                            arrayListActNames.remove(0);
                                            arrayListActNames.add(0,"All");
                                        }
                                        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(ReceivedPaymentReport.this,
                                                android.R.layout.simple_spinner_dropdown_item, arrayListActNames);
                                        spinnerApprovedFor.setAdapter(mSpinnerAdapter);

                                    } catch (JSONException e) {
                                        Toast.makeText(ReceivedPaymentReport.this, "Errorcode-513 ReceivedPaymentReport getPaymentModeResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.d("Exception", String.valueOf(e));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error == null || error.networkResponse == null)
                                        return;

                                    //get response body and parse with appropriate encoding
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReceivedPaymentReport.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(ReceivedPaymentReport.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                }else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReceivedPaymentReport.this);
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
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReceivedPaymentReport.this);
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
            Toast.makeText(ReceivedPaymentReport.this, "Errorcode-512 ReceivedPaymentReport getPaymentMode " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//getPaymentMode
}
