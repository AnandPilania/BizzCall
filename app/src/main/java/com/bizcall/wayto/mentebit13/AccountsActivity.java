package com.bizcall.wayto.mentebit13;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class AccountsActivity extends AppCompatActivity {
    ImageView imgBack,imgRefresh;
    Calendar calendar;
    TextView txtMonth,txtYear,txtJan,txtFeb,txtMar,txtApr,txtMay,txtJun,txtJul,txtAug,txtSep,txtOct,txtNov,txtDec;
    CardView cardViewMonths;
    TextView txtTotalExpINR,txtTotalEXPUSD,txtTotalINwINR,txtTotalInwUSD;
    TextView txtIncome;//show inward in INR
    TextView txtExpenses;//show expense in INR
    TextView txtIncome1;//show inward in USD
    TextView txtExpenses1;//show expense in USD
    TextView txtBalance1;//show total balance in INR
    TextView txtBalance;//show total balance in USD
    TextView txtINR,txtUSD,txtCategoryTypeINR,txtCategoryTypeUSD,txtInwardINR,txtInwardUSD;
    String month,year,clienturl,clientid,url,strINR,strDollar,counselorId;
    RequestQueue requestQueue;
    SharedPreferences sp;
    ProgressDialog dialog, dialog1;
    ArrayList<DataAccounts> arrayListCategory;
    DataAccounts dataAccounts;
    FloatingActionButton fab;
    RecyclerView recyclerExpensesINR,recyclerExpensesUSD,recyclerInwardINR,recyclerInwardUSD;
    String amounttype,amount,category,categoryname,categorytype;
    ArrayList<DataCategorywiseExpenses> arrayListExpensesINR;//list of total expenses in INR
    ArrayList<DataCategorywiseExpenses> arrayListExpensesUSD;//list of total expenses in USD
    ArrayList<DataCategorywiseExpenses> arrayListInwardINR;//list of total inwards in INR
    ArrayList<DataCategorywiseExpenses> arrayListInwardUSD;//list of total inwards in USD
    AdapterCategorywiseExpenses adapterCategorywiseExpenses;
    AdapterCategorywiseInward adapterCategorywiseInward;
    DataCategorywiseExpenses dataCategorywiseExpenses;
    CardView cardExpenseINR,cardExpenseUSD,cardInwardINR,cardInwardUSD;

    ArrayList<Integer> arrayList;
    LinearLayout layoutMe; //to show total payments pending on me
    LinearLayout layoutOther;//to show total payments pending on others
    LinearLayout layoutReject;//to show total payments rejected by other
    TextView txtPendingMe, txtPendingOthers, txtRejected;
    long timeout;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_accounts);
            initialize();

            //-------------------------------------
            if(CheckInternetSpeed.checkInternet(AccountsActivity.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
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
            else if(CheckInternetSpeed.checkInternet(AccountsActivity.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
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
                dialog1 = ProgressDialog.show(AccountsActivity.this, "", "Loading pending details...", true);
                newThreadInitilization(dialog1);
                //to get details of pending bills
                getPendingDetails();
            }
            //-------------------------------------

            layoutMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtPendingMe.getText().equals("0")){
                        Toast.makeText(AccountsActivity.this, "No Pending Bills on You.", Toast.LENGTH_SHORT).show();
                    } else {
                        //to show details of pending payments on you on ActivityPending class
                        Intent intent = new Intent(AccountsActivity.this, ActivityPending.class);
                        intent.putExtra("pendingdetails", "Pending On Me");
                        startActivity(intent);
                        finish();
                    }
                }
            });

            layoutOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtPendingOthers.getText().equals("0")){
                        Toast.makeText(AccountsActivity.this, "No Pending Bills on Others.", Toast.LENGTH_SHORT).show();
                    } else {
                        //to show details of pending payments on others on ActivityPending class
                        Intent intent = new Intent(AccountsActivity.this, ActivityPending.class);
                        intent.putExtra("pendingdetails", "Pending On Others");
                        startActivity(intent);
                        finish();
                    }
                }
            });

            layoutReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtRejected.getText().equals("0")){
                        Toast.makeText(AccountsActivity.this, "No Rejected Bills.", Toast.LENGTH_SHORT).show();
                    } else {
                        //to show details of rejected payments ActivityPending class
                        Intent intent = new Intent(AccountsActivity.this, ActivityPending.class);
                        intent.putExtra("pendingdetails", "Rejected Flow");
                        startActivity(intent);
                        finish();
                    }
                }
            });

            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccountsActivity.this, AccountsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            if(CheckInternetSpeed.checkInternet(AccountsActivity.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
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
            else if(CheckInternetSpeed.checkInternet(AccountsActivity.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
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
                dialog = ProgressDialog.show(AccountsActivity.this, "", "Loading expenses", true);
                newThreadInitilization(dialog);
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            if (getBalance(strINR)) //to get balance in inward and expenses in INR
                            {
                                sleep(300);
                                if (getBalance(strDollar))//to get balance in inward and expenses in USD
                                {
                                    sleep(300);
                                    if (getCategorywiseExpenses(strINR))//to get categorywise detailed balance expenses in INR
                                    {
                                        sleep(300);
                                        if (getCategorywiseExpenses(strDollar))//to get categorywise detailed balance expenses in USD
                                        {
                                            sleep(300);
                                            if (getCategorywiseInward(strINR))//to get categorywise detailed balance inward in INR
                                            {
                                                sleep(300);
                                                if (getCategorywiseInward(strDollar))//to get categorywise detailed balance inward in USD
                                                {
                                                    sleep(300);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        } catch (InterruptedException ex) {
                            Log.i("error", "thread");
                        }

                    }
                };
                t.start();
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccountsActivity.this, AddExpenses.class);
                    startActivity(intent);
                    finish();
                }
            });
            txtMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cardViewMonths.getVisibility() == View.GONE) {
                        txtMonth.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less_white_24dp, 0);
                        cardViewMonths.setVisibility(View.VISIBLE);
                    } else {
                        txtMonth.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_white_24dp, 0);
                        cardViewMonths.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e)
        {
            Toast.makeText(AccountsActivity.this,"Errorcode-414 AccountsActivity onCreate"+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }//onCreate

    private void initialize()
    {
        requestQueue = Volley.newRequestQueue(AccountsActivity.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        timeout=sp.getLong("TimeOut",0);
        counselorId = sp.getString("Id", "");
        counselorId = counselorId.replace(" ", "");
        imgBack = findViewById(R.id.img_back);
        txtMonth = findViewById(R.id.txtMonth);
        txtYear = findViewById(R.id.txtYear);
        txtJan = findViewById(R.id.txtJan);
        txtFeb = findViewById(R.id.txtFeb);
        txtMar = findViewById(R.id.txtMarch);
        txtApr = findViewById(R.id.txtApr);
        txtMay = findViewById(R.id.txtMay);
        txtJun = findViewById(R.id.txtJun);
        txtJul = findViewById(R.id.txtJul);
        txtAug = findViewById(R.id.txtAug);
        txtSep = findViewById(R.id.txtSep);
        txtOct = findViewById(R.id.txtOct);
        txtNov = findViewById(R.id.txtNov);
        txtDec = findViewById(R.id.txtDec);
        cardViewMonths = findViewById(R.id.cardMonths);
        txtIncome = findViewById(R.id.txtIncome);
        txtExpenses = findViewById(R.id.txtExpenses);
        txtBalance = findViewById(R.id.txtTotalBalance);
        txtIncome1 = findViewById(R.id.txtIncome1);
        txtExpenses1 = findViewById(R.id.txtExpenses1);
        txtBalance1 = findViewById(R.id.txtTotalBalance1);
        txtINR = findViewById(R.id.txtINR);
        txtUSD = findViewById(R.id.txtUSD);
        fab = findViewById(R.id.fab);
        imgRefresh = findViewById(R.id.imgRefresh);

        txtPendingMe = findViewById(R.id.txt_pendingme);
        txtPendingOthers = findViewById(R.id.txt_pendingothers);
        txtRejected = findViewById(R.id.txt_rejected);
        layoutMe = findViewById(R.id.layout_me);
        layoutOther = findViewById(R.id.layout_other);
        layoutReject = findViewById(R.id.layout_reject);
        arrayList = new ArrayList<>();

        calendar = Calendar.getInstance();
        year = String.valueOf(calendar.get(Calendar.YEAR));
        getMonthForInt((calendar.get(Calendar.MONTH)));
        txtYear.setText(year);
        // dialog = ProgressDialog.show(AccountsActivity.this, "", "Loading balance", true);
        strINR = txtINR.getText().toString();
        strDollar = txtUSD.getText().toString();
        arrayListExpensesINR=new ArrayList<>();
        arrayListInwardINR=new ArrayList<>();
        arrayListExpensesUSD=new ArrayList<>();
        arrayListInwardUSD=new ArrayList<>();
        arrayListCategory=new ArrayList<>();
        txtCategoryTypeINR=findViewById(R.id.txtCategoryType);
        txtCategoryTypeUSD=findViewById(R.id.txtCategoryType1);
        txtInwardINR=findViewById(R.id.txtCategoryTypeInward);
        txtInwardUSD=findViewById(R.id.txtCategoryTypeInward1);

        recyclerExpensesINR=findViewById(R.id.recyclerCategorywiseINR);
        recyclerExpensesUSD=findViewById(R.id.recyclerCategorywiseUSD);
        recyclerInwardINR=findViewById(R.id.recyclerInwardINR);
        recyclerInwardUSD=findViewById(R.id.recyclerInwardUSD);
        cardExpenseINR=findViewById(R.id.cardExpenseINR);
        cardExpenseUSD=findViewById(R.id.cardExpenseUSD);
        cardInwardINR=findViewById(R.id.cardInwardINR);
        cardInwardUSD=findViewById(R.id.cardInwardUSD);
        txtTotalExpINR=findViewById(R.id.txtTotalExpenseINR);
        txtTotalEXPUSD=findViewById(R.id.txtTotalExpenseUSD);
        txtTotalINwINR=findViewById(R.id.txtTotalInwardINR);
        txtTotalInwUSD=findViewById(R.id.txtTotalInwardUSD);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }//initialize

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
                                Toast.makeText(AccountsActivity.this, "Connection aborted", Toast.LENGTH_SHORT).show();
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

    public void getPendingDetails(){

        final String urlPending = clienturl + "?clientid=" + clientid + "&caseid=127&CounselorID="+counselorId;
        Log.d("urlPending", urlPending);
        StringRequest jsonEducationInfo = new StringRequest(Request.Method.GET, urlPending, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Log.d("url", urlPending);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    Log.d("clientresponce", response);

                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int aa = jsonObject.getInt("pendingONme");
                        arrayList.add(aa);
                    }
                    txtPendingMe.setText(arrayList.get(0)+"");
                    txtPendingOthers.setText(arrayList.get(1)+"");
                    txtRejected.setText(arrayList.get(2)+"");
                    dialog1.dismiss();
                    Log.d("educationarray", arrayList.size() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog1.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog1.dismiss();
            }
        });

        jsonEducationInfo.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonEducationInfo);
    }//getPendingDetails


    @Override
    protected void onResume() {
        super.onResume();

    }

    String getMonthForInt(int num) {
        try {
            month = "wrong";
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getMonths();
            if (num >= 0 && num <= 11) {
                month = months[num];
            }
            month = month.substring(0, 3);
            txtMonth.setText(month);
        }catch (Exception e)
        {
            Toast.makeText(AccountsActivity.this,"Errorcode-415 AccountsActivity getMonthFromInt"+e.toString(),Toast.LENGTH_SHORT).show();
        }
        return month;
    }
    public boolean getCategorywiseInward(final String amounttype) {
        try {
            if(CheckServer.isServerReachable(AccountsActivity.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=111&CounselorID="+counselorId+"&cAmountType="+amounttype;
                Log.d("CategorywiseUrl", url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    if(amounttype.equals(strDollar)) {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                    //arrayListInwardUSD.clear();
                                    //arrayListInwardINR.clear();
                                    Log.d("CategorywiseResponse1", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        amount=jsonObject1.getString("cAmount");
                                        categoryname=jsonObject1.getString("CategoryName");
                                        categorytype=jsonObject1.getString("CategoryType");
                                        // Log.d("Json11111",arrayList1.toString());
                                        dataCategorywiseExpenses=new DataCategorywiseExpenses(categorytype,categoryname,amount,amounttype);
                                        if(amounttype.equals("INR")) {
                                            arrayListInwardINR.add(dataCategorywiseExpenses);
                                        }
                                        else {
                                            arrayListInwardUSD.add(dataCategorywiseExpenses);
                                        }
                                    }
                                  /*  for(int i=0;i<arrayListInward.size();i++)
                                    {
                                        arrayListExpenses.add(arrayListInward.get(i));
                                    }*/

                                    if(amounttype.equals("INR")) {
                                        if(txtTotalINwINR.getText().toString().equals("Total:0"))
                                        {
                                            cardInwardINR.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            cardInwardINR.setVisibility(View.VISIBLE);
                                            txtInwardINR.setText("Inward(" + amounttype + ")");
                                            adapterCategorywiseInward = new AdapterCategorywiseInward(AccountsActivity.this, arrayListInwardINR);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(AccountsActivity.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerInwardINR.setLayoutManager(layoutManager);
                                            recyclerInwardINR.setAdapter(adapterCategorywiseInward);
                                            adapterCategorywiseInward.notifyDataSetChanged();
                                        }
                                    }
                                    else if(amounttype.equals("USD")) {
                                        if(txtTotalInwUSD.getText().toString().equals("Total:0"))
                                        {
                                            cardInwardUSD.setVisibility(View.GONE);
                                        }
                                        else
                                        {   cardInwardUSD.setVisibility(View.VISIBLE);
                                            txtInwardUSD.setText("Inward(" + amounttype + ")");
                                            adapterCategorywiseExpenses = new AdapterCategorywiseExpenses(AccountsActivity.this, arrayListInwardUSD);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(AccountsActivity.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerInwardUSD.setLayoutManager(layoutManager);
                                            recyclerInwardUSD.setAdapter(adapterCategorywiseExpenses);
                                            adapterCategorywiseExpenses.notifyDataSetChanged();
                                        }
                                    }
                                } catch (JSONException e) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(AccountsActivity.this,"Errorcode-417 AccountsActivity getBalanceResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(AccountsActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
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
            Toast.makeText(AccountsActivity.this,"Errorcode-416 AccountsActivity getBalance"+e.toString(),Toast.LENGTH_SHORT).show();
            return false;
        }
    }//getCategorywiseInward
    public boolean getCategorywiseExpenses(final String amounttype) {
        try {
            if(CheckServer.isServerReachable(AccountsActivity.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=110&CounselorID="+counselorId+"&cAmountType="+amounttype;
                Log.d("CategorywiseUrl", url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                   /* if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }*/
                                  //  arrayListExpensesINR.clear();
                                  //  arrayListExpensesUSD.clear();
                                    Log.d("CategorywiseResponse1", response+""+amounttype);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                         amount=jsonObject1.getString("cAmount");
                                         categoryname=jsonObject1.getString("CategoryName");
                                         categorytype=jsonObject1.getString("CategoryType");
                                        // Log.d("Json11111",arrayList1.toString());
                                        dataCategorywiseExpenses=new DataCategorywiseExpenses(categorytype,categoryname,amount,amounttype);
                                        if(amounttype.equals("INR")) {
                                        arrayListExpensesINR.add(dataCategorywiseExpenses);
                                        }
                                        else {
                                            arrayListExpensesUSD.add(dataCategorywiseExpenses);
                                        }
                                    }
                                   /* for(int i=0;i<arrayListExpenses.size();i++)
                                    {
                                        arrayListInward.add(arrayListExpenses.get(i));
                                    }*/

                                    if(amounttype.equals("INR")) {
                                        if(txtTotalExpINR.getText().toString().equals("Total:0"))
                                        {
                                            cardExpenseINR.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            cardExpenseINR.setVisibility(View.VISIBLE);
                                            txtCategoryTypeINR.setText("Expenses(" + amounttype + ")");
                                            adapterCategorywiseExpenses = new AdapterCategorywiseExpenses(AccountsActivity.this, arrayListExpensesINR);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(AccountsActivity.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerExpensesINR.setLayoutManager(layoutManager);
                                            recyclerExpensesINR.setAdapter(adapterCategorywiseExpenses);
                                            adapterCategorywiseExpenses.notifyDataSetChanged();
                                       }
                                    }
                                    else if(amounttype.equals("USD")) {
                                        if(txtTotalEXPUSD.getText().toString().equals("Total:0"))
                                        {
                                            cardExpenseUSD.setVisibility(View.GONE);
                                        }
                                        else {
                                            cardExpenseUSD.setVisibility(View.VISIBLE);
                                            txtCategoryTypeUSD.setText("Expenses(" + amounttype + ")");
                                            adapterCategorywiseInward = new AdapterCategorywiseInward(AccountsActivity.this, arrayListExpensesUSD);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(AccountsActivity.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerExpensesUSD.setLayoutManager(layoutManager);
                                            recyclerExpensesUSD.setAdapter(adapterCategorywiseInward);
                                            adapterCategorywiseInward.notifyDataSetChanged();
                                        } }

                                } catch (JSONException e) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(AccountsActivity.this,"Errorcode-417 AccountsActivity getBalanceResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(AccountsActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
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
            Toast.makeText(AccountsActivity.this,"Errorcode-416 AccountsActivity getCategorywiseexpenses"+e.toString(),Toast.LENGTH_SHORT).show();
            return false;
        }
    }//getCategorywiseExpenses
    public boolean getBalance(final String type) {
        try {
            if(CheckServer.isServerReachable(AccountsActivity.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=96&cAmountType="+type+"&CounselorID="+counselorId;
                Log.d("BalanceUrl", url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                   /* if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }*/
                                    arrayListCategory.clear();
                                    Log.d("BalanceResponse1", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        amount=jsonObject1.getString("cAmount");
                                        category=jsonObject1.getString("CategoryType");
                                        // Log.d("Json11111",arrayList1.toString());
                                         dataAccounts=new DataAccounts(amount,category);
                                        arrayListCategory.add(dataAccounts);
                                    }
                                    if(arrayListCategory.size()==1)
                                    {
                                        if(dataAccounts.getStrCategory().contains("Expenses"))
                                        {
                                            if(type.contains(strINR)) {
                                                txtIncome.setText("0");
                                                txtExpenses.setText(dataAccounts.getStrAmount());
                                            }
                                            else {
                                                txtIncome1.setText("0");
                                                txtExpenses1.setText(dataAccounts.getStrAmount());
                                            }
                                        }
                                        else {
                                            if(type.contains(strINR)) {
                                                txtExpenses.setText("0");
                                                txtIncome.setText(dataAccounts.getStrAmount());
                                            }
                                            else
                                            {
                                                txtExpenses1.setText("0");
                                                txtIncome1.setText(dataAccounts.getStrAmount());
                                            }
                                        }
                                        if(type.contains(strINR)) {
                                            int balance = Integer.parseInt(txtIncome.getText().toString()) - Integer.parseInt(txtExpenses.getText().toString());
                                            txtBalance.setText(balance + "");
                                        }
                                        else {
                                            int balance = Integer.parseInt(txtIncome1.getText().toString()) - Integer.parseInt(txtExpenses1.getText().toString());
                                            txtBalance1.setText(balance + "");
                                        }

                                    }
                                   else if(arrayListCategory.size()==0) {
                                       if(type.contains(strINR)) {
                                           txtIncome.setText("0");
                                           txtExpenses.setText("0");
                                           txtBalance.setText("0");
                                       }
                                       else {

                                               txtIncome1.setText("0");
                                               txtExpenses1.setText("0");
                                               txtBalance1.setText("0");
                                       }
                                    }
                                   else {
                                        if(type.contains(strINR)) {

                                            txtExpenses.setText(arrayListCategory.get(0).getStrAmount());

                                            txtIncome.setText(arrayListCategory.get(1).getStrAmount());
                                        }
                                        else {
                                            txtExpenses1.setText(arrayListCategory.get(0).getStrAmount());

                                            txtIncome1.setText(arrayListCategory.get(1).getStrAmount());
                                        }
                                        if(type.contains(strINR)) {
                                            int balance = Integer.parseInt(txtIncome.getText().toString()) - Integer.parseInt(txtExpenses.getText().toString());
                                            txtBalance.setText(balance + "");
                                        }
                                        else {
                                            int balance = Integer.parseInt(txtIncome1.getText().toString()) - Integer.parseInt(txtExpenses1.getText().toString());
                                            txtBalance1.setText(balance + "");
                                        }
                                    }
                                    txtTotalExpINR.setText("Total:"+txtExpenses.getText().toString());
                                    txtTotalEXPUSD.setText("Total:"+txtExpenses1.getText().toString());
                                    txtTotalINwINR.setText("Total:"+txtIncome.getText().toString());
                                    txtTotalInwUSD.setText("Total:"+txtIncome1.getText().toString());
                                    Log.d("ArraylistCategorySize:",arrayListCategory.size()+"");

                                    txtIncome.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(!txtIncome.getText().toString().equals("0")) {
                                                //to show detailed income balance with grapgicalrepresentation
                                                Intent intent = new Intent(AccountsActivity.this, BalanceDetails.class);
                                                intent.putExtra("AmountType", strINR);
                                                intent.putExtra("CategoryType", "Inward");
                                                intent.putExtra("Amount", txtIncome.getText().toString());
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                    txtExpenses.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(!txtExpenses.getText().toString().equals("0")) {
                                                Intent intent = new Intent(AccountsActivity.this, BalanceDetails.class);
                                                intent.putExtra("AmountType", strINR);
                                                intent.putExtra("Amount", txtExpenses.getText().toString());
                                                intent.putExtra("CategoryType", "Expenses");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                    txtIncome1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(!txtIncome1.getText().toString().equals("0")) {
                                                Intent intent = new Intent(AccountsActivity.this, BalanceDetails.class);
                                                intent.putExtra("AmountType", strDollar);
                                                intent.putExtra("Amount", txtIncome1.getText().toString());
                                                intent.putExtra("CategoryType", "Inward");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                    txtExpenses1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(!txtExpenses1.getText().toString().equals("0")) {
                                                Intent intent = new Intent(AccountsActivity.this, BalanceDetails.class);
                                                intent.putExtra("AmountType", strDollar);
                                                intent.putExtra("Amount", txtExpenses1.getText().toString());
                                                intent.putExtra("CategoryType", "Expenses");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });

                                } catch (JSONException e) {

                                    Toast.makeText(AccountsActivity.this,"Errorcode-417 AccountsActivity getBalanceResponse"+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(AccountsActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccountsActivity.this);
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
        { //Toast.makeText(AccountsActivity.this,"Errorcode-416 AccountsActivity getBalance"+e.toString(),Toast.LENGTH_SHORT).show();
            return false;
        }
    }//getBalance
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent intent=new Intent(AccountsActivity.this, Home.class);
        intent.putExtra("Activity","AccountActivity");
        startActivity(intent);
        finish();
    }
}
