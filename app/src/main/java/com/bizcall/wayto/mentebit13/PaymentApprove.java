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

import java.util.ArrayList;
import java.util.List;

public class PaymentApprove extends AppCompatActivity {

    TextView txtNoDetails,txtLoad,txtPendingAmountINR,txtPendingAmountUSD,txtApprovedAmountINR,txtApprovedAmountUSD,txtRejectedAmountINR,txtRejectedAmountUSD,txtSelectAcntName;
    Spinner spinnerApprovedFor;
    SharedPreferences sp;
    String clienturl="",clientid="",counselorId="";
    ProgressDialog dialog;
    String paymentid="",fileno="",candidatename="",counselorname="",amountINR="",amountUSD="",accountname="",dollarrate="",
            paymentdate="",receiveddate="",paymentfrom="",recieptname="";
    RequestQueue requestQueue;
    ArrayList<String> arrayListAmount;
    ArrayList<String> arrayListActNames;
    ArrayList<DataAccountName> arrayListAccountName;
    String acountid,acntname,servicetax,accountno;
    AdapterAllAmountDetails adapterAllAmountDetails;
    ArrayList<DataAllAmountDetails> arrayListAllAmount;
    RecyclerView recyclerAllAmount;
    LinearLayout linearTitle;
    ImageView imgBack;
    String allAmountUrl="";
   AlertDialog alertDialog1;
    EditText edtSearchtext;
    List<DataAllAmountDetails> displayedList;
    Thread thread;
    long timeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_payment_approve);
         initialize();
        edtSearchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                Log.d("CountTapped",count+"");
                if(count==0) {
                    if (edtSearchtext.getText().toString().length() == 0) {
                        //get list of all  payments to be approve
                        getAllAmountDetails(allAmountUrl);
                        /*adapterAllAmountDetails = new AdapterAllAmountDetails(PaymentApprove.this, arrayListAllAmount);
                        recyclerAllAmount.setAdapter(adapterAllAmountDetails);
                        adapterAllAmountDetails.notifyDataSetChanged();*/
                    }
                    else {
                        //to get text entered in searchview and compare it with displayedlist
                        filter(edtSearchtext.getText().toString());
                    }
                }
                else {
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
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acountname=spinnerApprovedFor.getSelectedItem().toString();
                int pos=spinnerApprovedFor.getSelectedItemPosition();
                String acntid=arrayListAccountName.get(pos).getAccountid();
                if(acountname.equals("All")) {
                    allAmountUrl = clienturl + "?clientid=" + clientid + "&caseid=131&Step=1";
                }
                else {
                    allAmountUrl = clienturl + "?clientid=" + clientid + "&caseid=131&Step=0&AccountID="+acntid;
                }

                //recyclerAllAmount.setVisibility(View.VISIBLE);
                    //linearTitle.setVisibility(View.VISIBLE);
                    // allAmountUrl = clienturl + "?clientid=" + clientid + "&caseid=131";
                    if (CheckInternetSpeed.checkInternet(PaymentApprove.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
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
                    } else if (CheckInternetSpeed.checkInternet(PaymentApprove.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
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
                        dialog = ProgressDialog.show(PaymentApprove.this, "", "Getting all amount details", true);
                        newThreadInitilization(dialog);
                        //get list of all  payments to be approve
                        getAllAmountDetails(allAmountUrl);
                    }
            }
        });
        if (CheckInternetSpeed.checkInternet(PaymentApprove.this).contains("0")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
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
        } else if (CheckInternetSpeed.checkInternet(PaymentApprove.this).contains("1")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
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
            dialog = ProgressDialog.show(PaymentApprove.this, "", "Getting all amount details", true);
            newThreadInitilization(dialog);
            //to get list of payment modes to be displayed on dropdown
            //after selecting payment mode option from dropdown you will get list of all received payment
            getPaymentMode();
        }
        } catch (Exception e) {
            Toast.makeText(PaymentApprove.this, "Errorcode-484 PaymentApprove onCreate " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//onCreate

    private void initialize() {
        requestQueue= Volley.newRequestQueue(PaymentApprove.this);
        sp=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid=sp.getString("ClientId","");
        clienturl=sp.getString("ClientUrl","");
        timeout=sp.getLong("TimeOut",0);
        counselorId=sp.getString("Id","");
        imgBack=findViewById(R.id.img_back);
        txtLoad=findViewById(R.id.txtLoad);
        txtPendingAmountINR=findViewById(R.id.txtPendingAmountINR);
        txtPendingAmountUSD=findViewById(R.id.txtPendingAmountUSD);
        txtApprovedAmountINR=findViewById(R.id.txtApprovedAmountINR);
        txtApprovedAmountUSD=findViewById(R.id.txtApprovedAmountUSD);
        txtRejectedAmountINR=findViewById(R.id.txtRejectedAmountINR);
        txtRejectedAmountUSD=findViewById(R.id.txtRejectedAmountUSD);
        spinnerApprovedFor=findViewById(R.id.spinnerApprovedfor);
        recyclerAllAmount=findViewById(R.id.recyclerAllAmount);
        txtSelectAcntName=findViewById(R.id.txtSelectAcntname);
        linearTitle=findViewById(R.id.linearTitle);
        txtNoDetails=findViewById(R.id.txtNoDetails);
        edtSearchtext=findViewById(R.id.edtSearchtext);
        displayedList=new ArrayList<>();

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
                                Toast.makeText(PaymentApprove.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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
    public void updateList(ArrayList<DataAllAmountDetails> list){
        try{
        arrayListAllAmount = list;
        /*if(arrayListAllAmount.size()==0)
        {
            linearTitle.setVisibility(View.GONE);
            txtNoDetails.setVisibility(View.VISIBLE);
            recyclerAllAmount.setVisibility(View.GONE);
        }else {*/
            adapterAllAmountDetails = new AdapterAllAmountDetails(PaymentApprove.this, arrayListAllAmount);
            recyclerAllAmount.setAdapter(adapterAllAmountDetails);
            adapterAllAmountDetails.notifyDataSetChanged();
        //}
        } catch (Exception e) {
            Toast.makeText(PaymentApprove.this, "Errorcode-485 PaymentApprove updateList " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//updateList

    void filter(String text){
        try{
        ArrayList<DataAllAmountDetails> temp = new ArrayList();
        for(DataAllAmountDetails d: arrayListAllAmount){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getFileno().contains(text)||d.getCounselorname().contains(text)||d.getAmountINR().contains(text)||d.getAmountUSD().contains(text)||
                    d.getCandidatename().contains(text)){
                temp.add(d);
            }

        }
        //update recyclerview
        if(temp.size()==0)
        {
            recyclerAllAmount.setVisibility(View.GONE);
            linearTitle.setVisibility(View.VISIBLE);
            txtNoDetails.setVisibility(View.VISIBLE);
        }
        else {
            recyclerAllAmount.setVisibility(View.VISIBLE);
            linearTitle.setVisibility(View.VISIBLE);
            txtNoDetails.setVisibility(View.GONE);
            updateList(temp);
        }
        } catch (Exception e) {
            Toast.makeText(PaymentApprove.this, "Errorcode-486 PaymentApprove filter " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//filter



    public void getPaymentMode()
    {
        try {
            arrayListAccountName = new ArrayList<>();
            arrayListActNames=new ArrayList<>();
          //  arrayListActNames.add(1,"All");

            String url = clienturl + "?clientid=" + clientid + "&caseid=118";
            Log.d("BankNamesUrl", url);
            if (CheckInternet.checkInternet(PaymentApprove.this)) {
                if(CheckServer.isServerReachable(PaymentApprove.this)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //get details of pending,approved and rejected payments
                                        getAmountDetals();
                                        Log.d("BankNamesResponse", response);
                                       // dialog.dismiss();

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
                                        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(PaymentApprove.this,
                                                android.R.layout.simple_spinner_dropdown_item, arrayListActNames);
                                        spinnerApprovedFor.setAdapter(mSpinnerAdapter);

                                    } catch (JSONException e) {
                                        Toast.makeText(PaymentApprove.this, "Errorcode-488 PaymentApprove getPaymentModeResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(PaymentApprove.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
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
            Toast.makeText(PaymentApprove.this, "Errorcode-487 PaymentApprove getPaymentMode " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//getPaymentMode
    public boolean getAmountDetals() {
        try {
            if(CheckServer.isServerReachable(PaymentApprove.this)) {
               String url = clienturl + "?clientid=" + clientid + "&caseid=130";
                Log.d("BalanceUrl", url);
                arrayListAmount=new ArrayList<>();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }

                                    Log.d("BalanceResponse1", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String Column=jsonObject1.getString("Column");
                                        String total=jsonObject1.getString("Total");
                                        arrayListAmount.add(total);

                                    }
                                    if(arrayListAmount.get(0).isEmpty()) {
                                        txtPendingAmountINR.setText("0");
                                    }
                                    else {
                                        txtPendingAmountINR.setText(arrayListAmount.get(0));
                                    }
                                    if(arrayListAmount.get(1).isEmpty()) {
                                        txtApprovedAmountINR.setText("0");
                                    }
                                    else {
                                        txtApprovedAmountINR.setText(arrayListAmount.get(1));
                                    }
                                    if(arrayListAmount.get(2).isEmpty()) {
                                        txtRejectedAmountINR.setText("0");
                                    }
                                    else {
                                        txtRejectedAmountINR.setText(arrayListAmount.get(2));
                                    }
                                    if(arrayListAmount.get(3).isEmpty()) {
                                        txtPendingAmountUSD.setText("0");
                                    }
                                    else {
                                        txtPendingAmountUSD.setText(arrayListAmount.get(3));
                                    }
                                    if(arrayListAmount.get(4).isEmpty()) {
                                        txtApprovedAmountUSD.setText("0");
                                    }
                                    else {
                                        txtApprovedAmountUSD.setText(arrayListAmount.get(4));
                                    }
                                    if(arrayListAmount.get(5).isEmpty()) {
                                        txtRejectedAmountUSD.setText("0");
                                    }
                                    else {
                                        txtRejectedAmountUSD.setText(arrayListAmount.get(5));
                                    }

                                } catch (JSONException e) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(PaymentApprove.this, "Errorcode-490 PaymentApprove getAmountDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(PaymentApprove.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
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
            Toast.makeText(PaymentApprove.this, "Errorcode-489 PaymentApprove getAmountDetails " + e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }//getAmountDetals

    public boolean getAllAmountDetails(String url) {
        try {
            if(CheckServer.isServerReachable(PaymentApprove.this)) {

                Log.d("AllAmountUrl", url);
                arrayListAllAmount=new ArrayList<>();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }

                                    Log.d("AllAmountResponse1", response);
                                    if(response.contains("[]"))
                                    {
                                        linearTitle.setVisibility(View.VISIBLE);
                                        txtNoDetails.setVisibility(View.VISIBLE);
                                        recyclerAllAmount.setVisibility(View.GONE);
                                    }
                                    else {
                                        recyclerAllAmount.setVisibility(View.VISIBLE);
                                        txtNoDetails.setVisibility(View.GONE);
                                        linearTitle.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            paymentid = jsonObject1.getString("nPaymentID");
                                            fileno = jsonObject1.getString("nFileNo");
                                            candidatename = jsonObject1.getString("Name");
                                            counselorId = jsonObject1.getString("cCounselorname");
                                            amountINR = jsonObject1.getString("amountINR");
                                            amountUSD = jsonObject1.getString("amountUSD");
                                            accountname=jsonObject1.getString("cAccountName");

                                            dollarrate=jsonObject1.getString("cCurrentLocalRateD");
                                            paymentdate=jsonObject1.getString("Payment date");
                                            receiveddate=jsonObject1.getString("Received Date");
                                            paymentfrom=jsonObject1.getString("cPaymentFrom");
                                            recieptname=jsonObject1.getString("ReceiptName");

                                            DataAllAmountDetails dataAllAmountDetails = new DataAllAmountDetails(paymentid, fileno, candidatename, counselorId, amountINR, amountUSD,accountname,dollarrate,paymentdate,receiveddate,paymentfrom,recieptname);
                                            arrayListAllAmount.add(dataAllAmountDetails);
                                        }
                                        adapterAllAmountDetails = new AdapterAllAmountDetails(PaymentApprove.this, arrayListAllAmount);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(PaymentApprove.this);
                                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerAllAmount.setLayoutManager(layoutManager);
                                        recyclerAllAmount.setAdapter(adapterAllAmountDetails);
                                        adapterAllAmountDetails.notifyDataSetChanged();
                                    }
                                    } catch (JSONException e) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(PaymentApprove.this, "Errorcode-492 PaymentApprove getAmountAllDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(PaymentApprove.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PaymentApprove.this);
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
            Toast.makeText(PaymentApprove.this, "Errorcode-491 PaymentApprove getAmountAllDetails " + e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }//getAllAmountDetails

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(PaymentApprove.this, Home.class);
            intent.putExtra("Activity", "PaymentApprove");
            startActivity(intent);
            finish();
        }catch (Exception e)
        {
            Toast.makeText(PaymentApprove.this, "Errorcode-493 PaymentApprove onBackpressed " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
