package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Bundle;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BalanceDetails extends AppCompatActivity
{
    SharedPreferences sp;
    String clientid,clienturl,counselorid,amountytpe,categorytype;
    ProgressDialog dialog;
    RequestQueue requestQueue;
    AdapterBalanceDetails adapterBalanceDetails;
    RecyclerView recyclerView;
    ImageView imgBack;
    ArrayList<DataBalanceDetails> arrayListBalanceDetails;
    TextView txtCategoryType,txtActivityName;
    String amount;
    ArrayList<BarEntry> mBarEntryArrayList;
    ArrayList<String> mStringArrayList;
    BarChart mBarChart, chart;
    MarkerView mv;
    Thread thread;
    long timeout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    { super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_balance_details);
            initialize();
            dialog = ProgressDialog.show(BalanceDetails.this, "", "Loading Details", true);
            newThreadInitilization(dialog);
            //to show details of balance in text and graphical form
            getBalanceDetails(amountytpe);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(BalanceDetails.this,"Errorcode-436 BalanceDetails onCreate"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//onCreate
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
                                Toast.makeText(BalanceDetails.this, "Connection aborted", Toast.LENGTH_SHORT).show();
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

    private void initialize() {
        requestQueue = Volley.newRequestQueue(BalanceDetails.this);
        imgBack = findViewById(R.id.img_back);
        txtActivityName=findViewById(R.id.txtActivityName);
        txtCategoryType = findViewById(R.id.txtCategoryType);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        counselorid = sp.getString("Id", "");
        counselorid = counselorid.replace(" ", "");
        timeout=sp.getLong("TimeOut",0);
        amountytpe = getIntent().getStringExtra("AmountType");
        categorytype = getIntent().getStringExtra("CategoryType");
        amount=getIntent().getStringExtra("Amount");
        txtCategoryType.setText(categorytype);
        txtActivityName.setText(categorytype+"("+amount+")");

        mBarEntryArrayList = new ArrayList<BarEntry>();
        mStringArrayList = new ArrayList<String>();
        mBarChart = (BarChart) findViewById(R.id.barchart);
        mBarChart.setTouchEnabled(true);
        mBarChart.setDragEnabled(true);
        mBarChart.setScaleEnabled(true);
        mBarChart.setMarkerView(mv);
    }//initialize

    public void getBalanceDetails(final String type) {
        try {
            if(CheckServer.isServerReachable(BalanceDetails.this)) {
                String url = clienturl + "?clientid=" + clientid + "&caseid=97&cAmountType="+type+"&CounselorID="+counselorid+"&CategoryType="+categorytype;
                Log.d("BalanceDetailsUrl", url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    arrayListBalanceDetails=new ArrayList<>();
                                    mBarEntryArrayList.clear();
                                    mStringArrayList.clear();
                                    Log.d("BalanceDetailsResponse1", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String date=jsonObject1.getString("Expense Date");
                                        String category=jsonObject1.getString("CategoryType");
                                        String amount=jsonObject1.getString("cAmount");
                                        String  memo=jsonObject1.getString("cmemo");
                                        String remarks=jsonObject1.getString("cRemarks");
                                        String categoryName=jsonObject1.getString("CategoryName");
                                        String approvedBy=jsonObject1.getString("ApprovedBy");
                                        String approvedRemarks=jsonObject1.getString("ApprovedRemarks");
                                        String  receiptname=jsonObject1.getString("ReceiptFileName");
                                        String from=jsonObject1.getString("From");
                                        String you=jsonObject1.getString("You");
                                        DataBalanceDetails dataBalanceDetails=new DataBalanceDetails(category,amount,date,memo,remarks,categoryName,approvedBy,approvedRemarks,receiptname,amountytpe,from,you);
                                        arrayListBalanceDetails.add(dataBalanceDetails);
                                        int amt=Integer.parseInt(amount);
                                           mBarEntryArrayList.add(new BarEntry(amt, i));
                                        mStringArrayList.add(date);
                                    }
                                    BarDataSet set1 = new BarDataSet(mBarEntryArrayList, " Data Value");
                                    set1.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData data = new BarData(mStringArrayList, set1);
                                    mBarChart.animateXY(1000,1000);
                                    YAxis xAxis=mBarChart.getAxisLeft();
                                    xAxis.setEnabled(false);
                                    mBarChart.setDrawValueAboveBar(false);
                                    mBarChart.setData(data);
                                    mBarChart.setHorizontalFadingEdgeEnabled(true);

                                    recyclerView=findViewById(R.id.recyclerBalanceDetails);
                                    adapterBalanceDetails=new AdapterBalanceDetails(BalanceDetails.this,arrayListBalanceDetails);
                                    LinearLayoutManager layoutManager=new LinearLayoutManager(BalanceDetails.this);
                                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(adapterBalanceDetails);
                                    adapterBalanceDetails.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(BalanceDetails.this, "Errorcode-205 CounselorContact statusResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(BalanceDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(BalanceDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(BalanceDetails.this);
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
        }catch (Exception e)
        {
            Toast.makeText(BalanceDetails.this,"Errorcode-204 CounselorContact getStatus1 "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcStatus", String.valueOf(e));
        }
    }//getBalanceDetails
}
