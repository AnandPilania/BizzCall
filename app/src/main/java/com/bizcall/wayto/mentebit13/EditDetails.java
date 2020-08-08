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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class EditDetails extends AppCompatActivity
{
    TextView txtSrno1,txtCourse1,txtMobile1,txtRemarks,txtSrnoTitle,txtActivityNameTitle;
    EditText edtName1,edtCity1,edtAddress1,edtEmail1,edtParent1,edtPincode1,edtRemarks,edtPhone,edtCourse,edtOtherCourse;
    Button btnUpdate,btnCancel;
    Spinner spinnerState1,spinnerStatus,spinnerRefName,spinnerCourse;
    int temp = 0;
    String name11="",address1="",city1="",state11="",email1="",parentno1="",pincode1="";
    String mbl="",parentno="",name="",course="",sr_no="",email="",allocatedDate="",adrs="",city="",state1="",pincode="",statusid,remark,status11,status,refname,refno,selectedCourse;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    ImageView imgBack,imgRefresh;
    int updateDetails;
    String clienturl="",clientid="",counsellorid="",activityname="",totalcount="";
    LinearLayout linearDataRef,linearStatus;
    ArrayList<String> arrayListRefName,arrayListRefId, arrayListStatusId,arrayList1;
    ArrayList<String> arrayListCourse;
    Vibrator vibrator;
    int result=0;
    boolean rslt=false;
    RequestQueue requestQueue;
    String url;
    Thread thread;
    long timeout;
/////////////////////////////////////////This activity is used 2 times first is EditDetails and second is NewLead entry///////////////////////////////
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_editdetails);
        try {
            //to initialize all variables and controls
                initialize();
            //  selectedCourse=spinnerCourse.getSelectedItem().toString();
            spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCourse = spinnerCourse.getSelectedItem().toString();
                    if (selectedCourse.contains("Others")) {
                        edtOtherCourse.getText().clear();
                        //edtOtherCourse.setVisibility(View.VISIBLE);
                        selectedCourse = edtOtherCourse.getText().toString();
                    } else {
                        edtOtherCourse.setText(selectedCourse);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            //checks if activity name is NewLead
            if (activityname.contains("NewLead")) {
                linearDataRef.setVisibility(View.VISIBLE);
                linearStatus.setVisibility(View.VISIBLE);
                txtRemarks.setVisibility(View.VISIBLE);
                edtRemarks.setVisibility(View.VISIBLE);
                edtPhone.setVisibility(View.VISIBLE);
                //  edtCourse.setVisibility(View.VISIBLE);
                spinnerCourse.setVisibility(View.VISIBLE);
                edtOtherCourse.setVisibility(View.VISIBLE);
                txtCourse1.setVisibility(View.GONE);
                txtMobile1.setVisibility(View.GONE);
                txtSrno1.setVisibility(View.GONE);
                txtSrnoTitle.setVisibility(View.GONE);
                btnUpdate.setText("Add");
                txtActivityNameTitle.setText("New Lead");
                if(CheckInternetSpeed.checkInternet(EditDetails.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                else if(CheckInternetSpeed.checkInternet(EditDetails.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                    dialog = ProgressDialog.show(EditDetails.this, "", "Loading Refnames and Status ..", true);
                   newThreadInitilization(dialog);
                   //to get reference names list from server to show on dropdown
                    getRefName();
                    //to get status list that to set for new lead from server to show on dropdown
                    getStatus1();
                }
               // refreshWhenLoading();

            } else //activityname is EditDetails {
                if(CheckInternetSpeed.checkInternet(EditDetails.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                else if(CheckInternetSpeed.checkInternet(EditDetails.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                    dialog = ProgressDialog.show(EditDetails.this, "", "Loading Counselor Data...", true);
                   newThreadInitilization(dialog);
                   //firstly it will get details of client by passing serial no taken from Details activity.
                    getCounselorData(sr_no, counsellorid);
                }
               // refreshWhenLoading();

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   onBtnUpdateClicked();
                }
                });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    //alertDialog.dismiss();
                }
            });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(EditDetails.this,EditDetails.class);
                    intent.putExtra("ActivityName",activityname);
                    startActivity(intent);
                }
            });
        }catch (Exception e)
        {
            Log.d("Exception", String.valueOf(e));
            Toast.makeText(EditDetails.this,"Errorcode-256 EditDetails onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(EditDetails.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
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
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        requestQueue=Volley.newRequestQueue(EditDetails.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        clientid = sp.getString("ClientId", null);
        counsellorid = sp.getString("Id", null);
        counsellorid = counsellorid.replace(" ", "");
        clienturl = sp.getString("ClientUrl", null);
        timeout=sp.getLong("TimeOut",0);
        sr_no = sp.getString("SelectedSrNo", null);

        activityname = getIntent().getStringExtra("ActivityName");

        updateDetails = sp.getInt("UpdateDetails", 0);
        edtOtherCourse = findViewById(R.id.edtOtherCourse);
        txtSrno1 = findViewById(R.id.txtSrno12);
        txtCourse1 = findViewById(R.id.txtCourse12);
        txtMobile1 = findViewById(R.id.txtPhone12);
        edtName1 = findViewById(R.id.edtName11);
        edtAddress1 = findViewById(R.id.edtAddress11);
        edtCity1 = findViewById(R.id.edtCity11);
        spinnerState1 = findViewById(R.id.spinnerState11);
        edtEmail1 = findViewById(R.id.edtEmail11);
        edtParent1 = findViewById(R.id.edtParentNo11);
        edtPincode1 = findViewById(R.id.edtPinCode11);
        btnUpdate = findViewById(R.id.btnUpdtDetails);
        btnCancel = findViewById(R.id.btnCancelUpdate);
        imgBack = findViewById(R.id.img_back);
        linearDataRef = findViewById(R.id.linearRefName);
        linearStatus = findViewById(R.id.linearStatus);
        spinnerRefName = findViewById(R.id.spinnerDataRef);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        edtRemarks = findViewById(R.id.edtRemarks);
        txtRemarks = findViewById(R.id.txtRemarks);
        edtPhone = findViewById(R.id.edtMobile1);
        txtSrnoTitle = findViewById(R.id.txtSrnoTitle);
        //  edtCourse=findViewById(R.id.edtCourse);
        txtActivityNameTitle = findViewById(R.id.txtActivity);
        spinnerCourse = findViewById(R.id.spinnerCourse);
        imgRefresh=findViewById(R.id.imgRefresh);

        arrayListCourse = new ArrayList<>();

        //arrayListCourse.add("Select Course");
        arrayListCourse.add("MBBS");
        arrayListCourse.add("Engineering");
        arrayListCourse.add("Management");
        arrayListCourse.add("Others");

        ArrayAdapter<String> arrayAdapterCourse = new ArrayAdapter<>(EditDetails.this, R.layout.spinner_item1, arrayListCourse);

        // Apply the adapter to the spinner
        spinnerCourse.setAdapter(arrayAdapterCourse);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(EditDetails.this,
                R.array.States, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerState1.setAdapter(adapter1);
    }//close initialize

    public void onBtnUpdateClicked(){
        try {
            temp=0;
            if (btnUpdate.getText() == "Add") {
               mbl=edtPhone.getText().toString();
               if(mbl.isEmpty())
               {
                   temp=1;
                   edtPhone.setError("Invalid mobile");
               }
               else {
                 //  mbl = mbl.substring((mbl.length()) - 10);
                   mbl = mbl.replaceAll(" ", "");
                   mbl=mbl.replace("+","%2B");
               }
                name11 = edtName1.getText().toString();
                name11 = name11.replaceAll("'", "");
                if (name11.length() == 0) {
                    temp=1;
                    edtName1.setError("Please enter name");
                }
                address1 = edtAddress1.getText().toString();
                address1 = address1.replaceAll("'", "");
                if (address1.length() == 0) {
                    address1 = "NA";
                   /* edtAddress1.setError("Please enter address");
                    temp = 1;*/
                }
                city1 = edtCity1.getText().toString();
                city1 = city1.replaceAll("'", "");
                if (city1.length() == 0) {
                    city1 = "NA";
                   /* edtCity1.setError("Please enter city");
                    temp = 1;*/
                }
                state11 = spinnerState1.getSelectedItem().toString();
                if (state11.contains("-Select State-")) {
                    state11 = "NA";
                }
                email1 = edtEmail1.getText().toString();
                email1 = email1.replaceAll("'", "");
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!email1.matches(emailPattern)) {
                    email1 = "NA";
                   /* edtEmail1.setError("Please enter email");
                    temp = 1;*/
                }
                parentno1 = edtParent1.getText().toString();
                if(!parentno1.isEmpty()) {
                    parentno1 = parentno1.replaceAll("'", "");
                    parentno1 = parentno1.replace("+", "%2B");
                }
                if (parentno1.length() < 10 || parentno1.length() == 0) {
                    parentno1 = "NA";
                   /*
                    edtParent1.setError("Invalid number");
                    temp = 1;*/
                }
                pincode1 = edtPincode1.getText().toString();
                pincode1 = pincode1.replaceAll("'", "");
                if (pincode1.length() < 6 || pincode1.length() == 0) {
                    pincode1 = "NA";
                  /*  edtPincode1.setError("Invalid pin");
                    temp = 1;*/
                }
                int pos = spinnerStatus.getSelectedItemPosition();
                status11 = arrayListStatusId.get(pos);
                Log.d("Status**", status11);

                refname = spinnerRefName.getSelectedItem().toString();
                int refid = spinnerRefName.getSelectedItemPosition();

                refno = arrayListRefId.get(refid);
                Log.d("RefID", refno);
                remark = edtRemarks.getText().toString();
                remark = remark.replaceAll("'", "");
                if (remark.length() == 0) {
                    remark = "NA";
                }
                //  course=spinnerCourse.getSelectedItem().toString();
                selectedCourse = edtOtherCourse.getText().toString();
                selectedCourse = selectedCourse.replaceAll("'", "");
                if (selectedCourse.contains("Select Course")) {
                    edtOtherCourse.setError("Please select course");
                }
                if(temp==0) {
                    if (CheckInternetSpeed.checkInternet(EditDetails.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                    } else if (CheckInternetSpeed.checkInternet(EditDetails.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                        dialog = ProgressDialog.show(EditDetails.this, "", "Checking number in database", true);
                        newThreadInitilization(dialog);
                        //to check that phone number already exist or not
                        checkPhoneNumber(mbl);
                    }
                }
            } else {

                name11 = edtName1.getText().toString();
                name11 = name11.replaceAll("'", "");
                if (name11.length() == 0) {
                    name11 = "NA";
                }
                address1 = edtAddress1.getText().toString();
                address1 = address1.replaceAll("'", "");
                if (address1.length() == 0) {
                    address1 = "NA";
                }
                city1 = edtCity1.getText().toString();
                city1 = city1.replaceAll("'", "");
                if (city1.length() == 0) {
                    city1 = "NA";
                }
                state11 = spinnerState1.getSelectedItem().toString();
                if (state11.contains("-Select State-")) {
                    state11 = "NA";
                }
                email1 = edtEmail1.getText().toString();
                email1 = email1.replaceAll("'", "");
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!email1.matches(emailPattern)) {
                }
                parentno1 = edtParent1.getText().toString();
                if(!parentno1.isEmpty()) {
                    parentno1 = parentno1.replaceAll("'", "");
                    parentno1 = parentno1.replace("+", "%2B");
                }
                if (parentno1.length() < 10 || parentno1.length() == 0) {
                    parentno1 = "NA";

                }
                pincode1 = edtPincode1.getText().toString();
                pincode1 = pincode1.replaceAll("'", "");
                if (pincode1.length() < 6 || pincode1.length() == 0) {
                    pincode1 = "NA";
                }
                if (CheckInternetSpeed.checkInternet(EditDetails.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                } else if (CheckInternetSpeed.checkInternet(EditDetails.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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

                    dialog = ProgressDialog.show(EditDetails.this, "", "Inserting counselor details...", true);
                    newThreadInitilization(dialog);
                    //to insert client information to database.
                    insertCounselorDetails();
                }
                //  refreshWhenLoading();
            }
        }catch (Exception e)
        {
            Toast.makeText(EditDetails.this,"Errorcode-257 EditDetails BtnUpdateClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close onBtnUpdateClicked


    public void checkPhoneNumber(String number) {
        try {
            if(CheckServer.isServerReachable(EditDetails.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=70&CounselorID=" + counsellorid + "&PhoneNumber=" + number;
                Log.d("CheckNumberUrl", url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("CheckNumberResponse", response);
                                try {
                                    // arrayListTotal.clear();
                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log.d("Json",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        totalcount = jsonObject1.getString("mobilecount");
                                    }
                                    if (totalcount.equals("0")) {
                                        //  insertNewLead();
                                        //to insert new lead entry to database
                                        insertNewLead();

                                    } else {
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
                                        alertDialogBuilder.setTitle("Mobile number is allocated to someone else")
                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })

                                                .show();
                                    }
                                } catch (JSONException e) {
                                    Log.d("CheckPhoneException", String.valueOf(e));
                                    Toast.makeText(EditDetails.this, "Errorcode-259 EditDetails checkPhoneNumberResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")
                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(EditDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if(dialog.isShowing()){
                    dialog.dismiss();}
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
            Toast.makeText(EditDetails.this,"Errorcode-258 EditDetails checkPhoneNumber "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close checkPhoneNumber

    @Override
    public void onBackPressed()
    {
        try {
            if(dialog.isShowing())
            {
                dialog.dismiss();
            }

            if (activityname.contains("NewLead")) {
                Intent intent = new Intent(EditDetails.this, Home.class);
                intent.putExtra("Activity", "NewLead");
                startActivity(intent);
                finish();
            } else {
                startActivity(new Intent(EditDetails.this, DetailsActivity.class));
                finish();
            }
        }catch (Exception e)
        {
            Toast.makeText(EditDetails.this,"Errorcode-260 EditDetails onBackPressed "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
       // super.onBackPressed();
    }

    public void getCounselorData(String serialno, String cid) {
        try {
            if(CheckServer.isServerReachable(EditDetails.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=32&nSrNo=" + serialno + "&cCounselorID=" + cid;
                Log.d("CounselorUrl", url);
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

                                    txtSrno1.setText(sr_no);
                                    txtCourse1.setText(course);
                                    txtMobile1.setText(mbl);
                                    edtName1.setText(name);
                                    if (edtName1.getText().toString().length() == 0) {
                                        edtName1.setText("NA");
                                    }
                                    edtAddress1.setText(adrs);
                                    if (edtAddress1.getText().toString().length() == 0) {
                                        edtAddress1.setText("NA");
                                    }
                                    edtCity1.setText(city);
                                    if (edtCity1.getText().toString().length() == 0) {
                                        edtCity1.setText("NA");
                                    }
                                    edtParent1.setText(parentno);
                                    if (edtParent1.getText().toString().length() == 0) {
                                        edtParent1.setText("NA");
                                    }
                                    edtEmail1.setText(email);
                                    if (edtEmail1.getText().toString().length() == 0) {
                                        edtEmail1.setText("NA");
                                    }
                                    edtPincode1.setText(pincode);
                                    if (edtPincode1.getText().toString().length() == 0) {
                                        edtPincode1.setText("NA");
                                    }


                                    String[] array = getResources().getStringArray(R.array.States);
                                    for (int i = 0; i < array.length; i++) {
                                        if (array[i].matches(state1)) {
                                            spinnerState1.setSelection(i);
                                        }
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(EditDetails.this, "Errorcode-262 EditDetails getCounselorDataResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(EditDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if(dialog.isShowing()){
                    dialog.dismiss();}
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
            Toast.makeText(EditDetails.this,"Errorcode-261 EditDetails getCounselorData "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//closegetCounselorData

    public void updateCounselorDetails() {
            try {
                url = clienturl + "?clientid=" + clientid + "&caseid=18&Srno=" + sr_no + "&CounselorName=" + name11 + "&Address=" +
                        address1 + "&City=" + city1 + "&State=" + state11 + "&Pincode=" + pincode1 + "&Email=" + email1 + "&ParentNo=" + parentno1;
                Log.d("UCounselorUrl", url);
                if (CheckInternet.checkInternet(EditDetails.this)) {
                    if (CheckServer.isServerReachable(EditDetails.this)) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("*******", response.toString());
                                        try {
                                            Log.d("updateDetailsResponse", response);

                                            if (response.contains("Row updated successfully")) {
                                                int eventid = 5;
                                                //if client's information get updated successfully counselor get point fo that
                                                insertPointCollection(eventid);
                                                startActivity(new Intent(EditDetails.this, DetailsActivity.class));
                                                editor.putInt("UpdateDetails", updateDetails);
                                                editor.commit();
                                                Toast.makeText(EditDetails.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(EditDetails.this, DetailsActivity.class));
                                            } else {
                                                Toast.makeText(EditDetails.this, "Data not updated successfully", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (Exception e) {
                                            Toast.makeText(EditDetails.this, "Errorcode-264 EditDetails updateCounselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
                                            alertDialogBuilder.setTitle("Network issue!!!")

                                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            dialog.dismiss();
                                                        }
                                                    }).show();
                                            dialog.dismiss();
                                            Toast.makeText(EditDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                            // showCustomPopupMenu();
                                            Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                        }

                                    }
                                });
                        requestQueue.add(stringRequest);
                    } else {
                        dialog.dismiss();
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                Toast.makeText(EditDetails.this,"Errorcode-263 EditDetails updateCounselorDetails "+e.toString(),Toast.LENGTH_SHORT).show();
            }
    }//close updateCounselorDetails

    public void insertPointCollection(int eid)
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=36&nCounsellorId=" + counsellorid + "&nEventId=" + eid;
            Log.d("PointCollection", url);
            if (CheckInternet.checkInternet(EditDetails.this)) {
                if (CheckServer.isServerReachable(EditDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("*******", response.toString());
                                    try {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Log.d("PointCollectionResponse", response);
                                        if (response.contains("Data inserted successfully")) {
                                            Toast.makeText(EditDetails.this, "Added point for update details", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditDetails.this, "Point not added for update details", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(EditDetails.this, "Errorcode-266 EditDetails insertPointResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(EditDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
            }else
            {
                if(dialog.isShowing()){
                    dialog.dismiss();}
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
            Toast.makeText(EditDetails.this,"Errorcode-265 EditDetails insertPoint "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }////close insertPointCollection

    public void insertCounselorDetails() {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=31&nSrno=" + sr_no;
            Log.d("InsertCounselorUrl", url);
            if(CheckServer.isServerReachable(EditDetails.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("*******", response.toString());
                                try {
                                    dialog.dismiss();
                                    Log.d("InsertDetailsResponse", response);
                                    if (response.contains("Data inserted successfully")) {
                                        //to update edited information of client
                                        updateCounselorDetails();
                                        Toast.makeText(EditDetails.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditDetails.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(EditDetails.this, "Errorcode-268 EditDetails insertCounselorDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(EditDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if(dialog.isShowing()){
                    dialog.dismiss();}
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
            Toast.makeText(EditDetails.this,"Errorcode-267 EditDetails insertCounselorDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }

}//close insertCounselorDetails
    public void getRefName() {
        try {
            if(CheckServer.isServerReachable(EditDetails.this))
            {
            // arrayListRefrences=new ArrayList<>();
            arrayListRefName = new ArrayList<>();
            arrayListRefId = new ArrayList<>();
            //  arrayListRefName.add(0, "All");
            url = clienturl + "?clientid=" + clientid + "&caseid=47";
                Log.d("RefNameUrl", url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Log.d("RefNameResponse", response);
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                // Log.d("Json",jsonObject.toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String refID = jsonObject1.getString("DataRefID");
                                    String dataRefName = jsonObject1.getString("DataRefName");
                                    //    DataReference dataReference=new DataReference(dataRefName,dataFrom);
                                    arrayListRefName.add(dataRefName);
                                    arrayListRefId.add(refID);
                                    // arrayListRefrences.add(dataReference);
                                    //  String total=jsonObject1.getString("Total No");

                                }
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(EditDetails.this, R.layout.spinner_item1, arrayListRefName);
                                // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerRefName.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();

                                Log.d("RefIdSize", String.valueOf(arrayListRefId.size()));
                            } catch (Exception e) {
                                Toast.makeText(EditDetails.this,"Errorcode-270 EditDetails getRefNamesResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
                                alertDialogBuilder.setTitle("Network issue!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(EditDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
        }else {
                if(dialog.isShowing()){
                dialog.dismiss();}
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
            Toast.makeText(EditDetails.this,"Errorcode-269 EditDetails getRefNames "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }//close getRefName

    public void getStatus1() {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=2";
            Log.d("StatusUrl", url);
            if (CheckInternet.checkInternet(EditDetails.this)) {
                if (CheckServer.isServerReachable(EditDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    arrayListStatusId = new ArrayList<>();
                                    arrayList1 = new ArrayList<>();
                                    Log.d("StatusResponse1", response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String status1 = jsonObject1.getString("cStatus");
                                            statusid = jsonObject1.getString("nStatusID");
                                            Log.d("Status11", statusid + status1);
                                            //StatusInfo statusInfo=new StatusInfo(status1,statusid);
                                            arrayListStatusId.add(statusid);
                                            // Log.d("Json33333",statusInfo.toString());
                                            //arrayList.add(statusInfo);
                                            arrayList1.add(status1);
                                            // Log.d("Json11111",arrayList1.toString());
                                        }
                                        String setselected = status11;

                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditDetails.this, R.layout.spinner_item1, arrayList1);
                                        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerStatus.setAdapter(arrayAdapter);


                                        //  Log.d("Size**", String.valueOf(arrayList1.size()));
                                    } catch (Exception e) {
                                        Toast.makeText(EditDetails.this, "Errorcode-272 EditDetails getStatusResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(EditDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
            Toast.makeText(EditDetails.this,"Errorcode-271 EditDetails getStatus "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close getStatus
    public void insertNewLead()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=45&RefNo=4444&Name=" + name11 + "&Course=" + selectedCourse + "&Mobile=" + mbl + "&Address=" + address1 + "&City=" + city1 + "&State=" + state11 + "&PinCod=" + pincode1
                    + "&Mobile2=" + parentno1 + "&MailID=" + email1 + "&DataRef=" + refno + "&UserID=" + counsellorid + "&Status=" + status11 + "&Remarks=" + remark;
            Log.d("NewLeadUrl", url);
            if (CheckInternet.checkInternet(EditDetails.this)) {
                if (CheckServer.isServerReachable(EditDetails.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("*******", response.toString());
                                    try {
                                        dialog.dismiss();
                                        Log.d("InsertNewLeadResponse", response);
                                        if (response.contains("Data inserted successfully")) {
                                            edtName1.getText().clear();
                                            // edtCourse.getText().clear();
                                            edtPhone.getText().clear();
                                            edtAddress1.getText().clear();
                                            edtCity1.getText().clear();
                                            edtPincode1.getText().clear();
                                            edtParent1.getText().clear();
                                            edtEmail1.getText().clear();
                                            edtRemarks.getText().clear();
                                            spinnerCourse.setSelection(0);
                                            spinnerRefName.setSelection(0);
                                            spinnerState1.setSelection(0);
                                            spinnerStatus.setSelection(0);
                                            // updateCounselorDetails();
                                            Toast.makeText(EditDetails.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditDetails.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(EditDetails.this, "Errorcode-274 EditDetails insertNewLeadResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(EditDetails.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
            }else
            {
                if(dialog.isShowing()){
                    dialog.dismiss();}
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditDetails.this);
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
            Toast.makeText(EditDetails.this,"Errorcode-273 EditDetails insertNewLead "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close insertNewLead

}
