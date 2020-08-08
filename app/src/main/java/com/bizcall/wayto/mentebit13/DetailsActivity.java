package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class DetailsActivity extends AppCompatActivity {



    ImageView imgBack,imgRefresh;
    int temp = 0;
    String clienturl,clientid;
    String mbl, parentno, name, course, sr_no, email, allocatedDate, adrs, city, state1, pincode, statusid, remark, status11,counselorid;
    SharedPreferences sp;
    UrlRequest urlRequest;
    TextView txtSrno ,txtCourse,txtMobile,txtName, txtAddress,txtCity,txtState,txtPincode,txtEmail,txtParent;
    Button btnEdit,btnOk;
    ProgressDialog dialog;
    String url,activityname;
    RequestQueue requestQueue;
    Vibrator vibrator;
    Thread thread;
    long timeout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detalis);
        try{
          //to initialize all controls and variables
         initialize();
         //to get all details of selected client
        loadClientData();
        imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    Intent intent=new Intent(DetailsActivity.this,DetailsActivity.class);
                    //intent.putExtra("Activity",activityName);
                    startActivity(intent);
                }
            });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                vibrator.vibrate(100);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailsActivity.this, EditDetails.class);
                intent.putExtra("ActivityName","DetailsActivity");
                startActivity(intent);
                }
        });
        }catch (Exception e)
        {
            Toast.makeText(DetailsActivity.this,"Errorcode-251 DetailsActivity onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }//oncreate closed

    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    // dialog1.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(DetailsActivity.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
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
    public void initialize()
    {
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        requestQueue=Volley.newRequestQueue(DetailsActivity.this);
        imgBack=findViewById(R.id.img_back);
        imgRefresh=findViewById(R.id.imgRefresh);
        txtSrno = findViewById(R.id.txtSrno11);
        txtCourse = findViewById(R.id.txtCourse11);
        txtMobile = findViewById(R.id.txtPhone11);
        txtName = findViewById(R.id.txtName11);
        txtAddress = findViewById(R.id.txtAddress11);
        txtCity = findViewById(R.id.txtCity11);
        txtState = findViewById(R.id.txtState11);
        txtPincode = findViewById(R.id.txtPinCode11);
        txtEmail = findViewById(R.id.txtEmail11);
        txtParent = findViewById(R.id.txtParentNo11);
        btnEdit = findViewById(R.id.btnEditDetails);
        btnOk = findViewById(R.id.btnOk11);

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid=sp.getString("ClientId",null);
        clienturl=sp.getString("ClientUrl",null);
        sr_no = sp.getString("SelectedSrNo", null);
        activityname=sp.getString("ActivityContact",null);
        Log.d("DetailsAct",activityname);
        counselorid = sp.getString("Id", null);
        counselorid=counselorid.replace(" ","");
        timeout=sp.getLong("TimeOut",0);
    }

    @Override
    public void onBackPressed()
    {
        try{
            Intent intent=new Intent(DetailsActivity.this,CounselorContactActivity.class);
            if(activityname.contains("CounselorData"))
            {
                intent.putExtra("ActivityName","CounselorData");
            }
            else  if(activityname.contains("OnlineLead"))
            {
                intent.putExtra("ActivityName","OnlineLead");
            }
            else  if(activityname.contains("OLActivity"))
            {
                intent.putExtra("ActivityName","OLActivity");
            }
            else  if(activityname.contains("OpenLeads"))
            {
                intent.putExtra("ActivityName","OpenLeads");
            }
            else  if(activityname.contains("ConvertedOnlineLead"))
            {
                intent.putExtra("ActivityName","ConvertedOnlineLead");
            }
            else  if(activityname.contains("FormFilled"))
            {
                intent.putExtra("ActivityName","FormFilled");
            }

            startActivity(intent);
      //  super.onBackPressed();
        }catch (Exception e)
        {
            Toast.makeText(DetailsActivity.this,"Errorcode-252 DetailsActivity onBackpressed "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void loadClientData(){
        try {
            if (CheckInternetSpeed.checkInternet(DetailsActivity.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DetailsActivity.this);
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
            } else if (CheckInternetSpeed.checkInternet(DetailsActivity.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DetailsActivity.this);
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
                dialog = ProgressDialog.show(DetailsActivity.this, "", "Loading counselor information...", true);
                newThreadInitilization(dialog);
                getClientData(sr_no, counselorid);
            }
        }catch (Exception e)
        {
            Toast.makeText(DetailsActivity.this,"Errorcode-253 DetailsActivity loadCounselorData "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close loadClientData

    public void getClientData(String serialno, String cid) {
        try {
            if (CheckServer.isServerReachable(DetailsActivity.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=32&nSrNo=" + serialno + "&cCounselorID=" + cid;
                Log.d("CounselorDetailsUrl", url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                Log.d("FetchedResponse", response);

                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("Length", String.valueOf(jsonArray.length()));
                   /* if(jsonArray.length()==0)
                    {
                        startActivity(new Intent(DetailsActivity.this,CounsellorData.class));
                        Toast.makeText(DetailsActivity.this,"This candidate is allocated to someone else",Toast.LENGTH_SHORT).show();
                    }*/
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        name = jsonObject1.getString("cCandidateName");
                                        course = jsonObject1.getString("cCourse");
                                        Log.d("Course&*", course);
                                        mbl = jsonObject1.getString("cMobile");
                                        adrs = jsonObject1.getString("cAddressLine");
                                        city = jsonObject1.getString("cCity");
                                        state1 = jsonObject1.getString("cState");
                                        pincode = jsonObject1.getString("cPinCode");
                                        parentno = jsonObject1.getString("cParantNo");
                                        email = jsonObject1.getString("cEmail");
                                        //  fetchedDataFrom = jsonObject1.getString("cDataFrom");
                                        // fetchedAllocatedTo = jsonObject1.getString("AllocatedTo");
                                        allocatedDate = jsonObject1.getString("AllocationDate");
                                        statusid = jsonObject1.getString("CurrentStatus");
                                        remark = jsonObject1.getString("cRemarks");
                                        //  fetchedCreatedDate = jsonObject1.getString("dtCreatedDate");
                                        status11 = jsonObject1.getString("cStatus");

                                    }
                                    txtSrno.setText(sr_no);
                                    txtCourse.setText(course);
                                    txtMobile.setText(mbl);
                                    txtName.setText(name);
                                    txtAddress.setText(adrs);
                                    if (txtAddress.getText().toString().length() == 0) {
                                        txtAddress.setText("NA");
                                    }
                                    txtCity.setText(city);
                                    if (txtCity.getText().toString().length() == 0) {
                                        txtCity.setText("NA");
                                    }
                                    txtState.setText(state1);
                                    if (txtState.getText().toString().contains("-Select State-")) {
                                        txtState.setText("NA");
                                    }
                                    txtPincode.setText(pincode);
                                    if (txtPincode.getText().toString().length() == 0) {
                                        txtPincode.setText("NA");
                                    }
                                    txtEmail.setText(email);
                                    if (txtEmail.getText().toString().length() == 0) {
                                        txtEmail.setText("NA");
                                    }
                                    txtParent.setText(parentno);
                                    if (txtParent.getText().toString().length() == 0) {
                                        txtParent.setText("NA");
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(DetailsActivity.this, "Errorcode-255 DetailsActivity CounselorDataResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DetailsActivity.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")
                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(DetailsActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DetailsActivity.this);
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
            Toast.makeText(DetailsActivity.this,"Errorcode-254 DetailsActivity getCounselorData "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }//close getClientData
}
