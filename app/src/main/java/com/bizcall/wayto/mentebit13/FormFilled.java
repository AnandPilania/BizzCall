package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class FormFilled extends AppCompatActivity {
    Spinner spinnerFilter;
    ArrayList<String> arrayListOpenLeadRefName,arrayListOpenLeadRefId;
    RecyclerView recyclerOpenLead;
    AdapterFormFilled adapterFormFilled;
    String url,strMin,strMax,caseid,strRefId,strRefName;
    UrlRequest urlRequest;
    ArrayList<DataFormFilled> arrayListFormFilled;
    TextView txtMax,txtMin,txtNotFound,txtDisplayInfo;
    Button btnPrevious,btnNext;
    ProgressDialog dialog;
    String clienturl,clientid,counselorid,name,course,mbl,adrs,city,state1,pincode,parentno,email,fetchedDataFrom,fetchedAllocatedTo,allocatedDate,statusid,remark,fetchedCreatedDate,status11;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ImageView imgBack;
    Vibrator vibrator;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_form_filled);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            requestQueue = Volley.newRequestQueue(FormFilled.this);
            //  recyclerOpenLead=findViewById(R.id.recyclerOpenLeads);
           // spinnerFilter = findViewById(R.id.spinnerFilter);
            txtNotFound = findViewById(R.id.txtNotFound1);
            txtMin = findViewById(R.id.txtMin);
            txtMax = findViewById(R.id.txtMax);
            btnNext = findViewById(R.id.btnLoadMore);
            btnPrevious = findViewById(R.id.btnLoadPrevious);
            txtDisplayInfo = findViewById(R.id.txtDisplayInfo);
            imgBack = findViewById(R.id.img_back);

            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            clientid = sp.getString("ClientId", null);
            clienturl = sp.getString("ClientUrl", null);
            counselorid = sp.getString("Id", null);

            // imgRefresh = findViewById(R.id.imgRefresh);

             strMin = "1";
            strMax = "25";
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
            arrayListOpenLeadRefName = new ArrayList<>();
            arrayListOpenLeadRefId = new ArrayList<>();

            // arrayListOpenLeadRefName.add("--Select Leads--");
            //  arrayListSpinnerValues.add("Online Leads");
            editor.putString("ActOnlineLead", "OpenLeads");
            editor.commit();
            loadFormFilled();

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnNextClicked();
                    }
            });
            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   btnPreviousClicked();
                   }
            });


        } catch (Exception e) {
            Toast.makeText(FormFilled.this,"Errorcode-244 FormFilled Oncreate "+e.toString(),Toast.LENGTH_SHORT).show();            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(FormFilled.this, Home.class);
            intent.putExtra("Activity", "FormFilled");
            //intent.putExtra("MainMenu",MainMenuselected);
            startActivity(intent);
            finish();
        }catch (Exception e)
        {
            Toast.makeText(FormFilled.this,"Errorcode-245 FormFilled OnBackPressed "+e.toString(),Toast.LENGTH_SHORT).show();            dialog.dismiss();
        }
    }
    public void loadFormFilled()
    {
        try {
            if (CheckInternetSpeed.checkInternet(FormFilled.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FormFilled.this);
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
            } else if (CheckInternetSpeed.checkInternet(FormFilled.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FormFilled.this);
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

                dialog = ProgressDialog.show(FormFilled.this, "", "Loading List of Form Filled", false, true);
                getFormFilled(strMin, strMax);
            }
        }catch (Exception e)
        {
            Toast.makeText(FormFilled.this,"Errorcode-246 FormFilled loadFormFilled "+e.toString(),Toast.LENGTH_SHORT).show();            dialog.dismiss();
        }
    }
    public void btnNextClicked(){
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) + 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) + 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            if (CheckInternetSpeed.checkInternet(FormFilled.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FormFilled.this);
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
            } else if (CheckInternetSpeed.checkInternet(FormFilled.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FormFilled.this);
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
                dialog = ProgressDialog.show(FormFilled.this, "", "Loading List of Form Filled", false, true);
                getFormFilled(strMin, strMax);
            }
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
        }catch (Exception e)
        {
            Toast.makeText(FormFilled.this,"Errorcode-247 FormFilled btnNextClicked "+e.toString(),Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
    public void btnPreviousClicked(){
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) - 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) - 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
            txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
            if (CheckInternetSpeed.checkInternet(FormFilled.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FormFilled.this);
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
            } else if (CheckInternetSpeed.checkInternet(FormFilled.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FormFilled.this);
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

                dialog = ProgressDialog.show(FormFilled.this, "", "Loading List of Form Filled", false, true);
                getFormFilled(strMin, strMax);
            }
        }catch (Exception e)
        {
            Toast.makeText(FormFilled.this,"Errorcode-248 FormFilled btnPreviousClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void getFormFilled(String strMin1, String strMax1) {
        try {
            if(CheckServer.isServerReachable(FormFilled.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=82&MinVal=" + strMin1 + "&MaxVal=" + strMax1;
                Log.d("FormFillUrl", url);
                arrayListFormFilled = new ArrayList<>();
                if (CheckInternet.checkInternet(FormFilled.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    dialog.dismiss();
                                    Log.d("FormFilledResponse", response);
                                    // int mval=Integer.parseInt(txtMax.getText().toString());
                                    try {
                                        if (response.contains("[]") || response.contains("Error in query")) {
                                            txtDisplayInfo.setVisibility(View.GONE);
                                            txtNotFound.setVisibility(View.VISIBLE);
                                        } else {
                                            txtDisplayInfo.setVisibility(View.VISIBLE);
                                            txtNotFound.setVisibility(View.GONE);

                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String sr_no = String.valueOf(jsonObject1.getInt("id"));
                                                name = jsonObject1.getString("firstname");
                                                course = jsonObject1.getString("cWebsite");
                                                mbl = jsonObject1.getString("mobile");
                                                //  adrs = jsonObject1.getString("cAddressLine");
                                    /*city = jsonObject1.getString("cCity");
                                    state1 = jsonObject1.getString("cState");
                                    pincode = jsonObject1.getString("cPinCode");
                                    parentno = jsonObject1.getString("cParantNo");
                                    email = jsonObject1.getString("cEmail");
                                    fetchedDataFrom = jsonObject1.getString("cDataFrom");
                                    fetchedAllocatedTo = jsonObject1.getString("AllocatedTo");
                                    allocatedDate = jsonObject1.getString("AllocationDate");
                                    statusid = jsonObject1.getString("CurrentStatus");
                                    remark = jsonObject1.getString("cRemarks");
                                    fetchedCreatedDate = jsonObject1.getString("dtCreatedDate");
                                    status11 = jsonObject1.getString("cStatus");*/
                                                // displayno=jsonObject1.getString("displayno");
                                                DataFormFilled dataFormFilled = new DataFormFilled(sr_no, name, mbl, course);
                                                arrayListFormFilled.add(dataFormFilled);

                                            }
                                            //  recyclerOnlineLead=findViewById(R.id.recyclerNotification);
                                            adapterFormFilled = new AdapterFormFilled(FormFilled.this, arrayListFormFilled);

                                            recyclerOpenLead = findViewById(R.id.recyclerOpenLeads);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(FormFilled.this);
                                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerOpenLead.setLayoutManager(layoutManager);
                                            recyclerOpenLead.setAdapter(adapterFormFilled);
                                            adapterFormFilled.notifyDataSetChanged();
                                            Log.d("OnlineArraysize", String.valueOf(arrayListFormFilled.size()));

                                            if (txtMin.getText().toString().equals("1")) {
                                                btnPrevious.setVisibility(View.GONE);
                                                btnNext.setVisibility(View.GONE);
                                                if (25 <= arrayListFormFilled.size()) {
                                                    btnNext.setVisibility(View.VISIBLE);
                                                } else {
                                                    btnNext.setVisibility(View.GONE);
                                                }
                                            } else if (25 > arrayListFormFilled.size()) {
                                                btnPrevious.setVisibility(View.VISIBLE);
                                                btnNext.setVisibility(View.GONE);
                                            } else {
                                                btnNext.setVisibility(View.VISIBLE);
                                                btnPrevious.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(FormFilled.this, "Errorcode-250 FormFilled FormFilledResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.d("OnlineLeadException", String.valueOf(e));
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FormFilled.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")
                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(FormFilled.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FormFilled.this);
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
            }else {
                if(dialog.isShowing()){
                    dialog.dismiss();}
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(FormFilled.this);
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
            Toast.makeText(FormFilled.this,"Errorcode-249 FormFilled getFormFilled "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
