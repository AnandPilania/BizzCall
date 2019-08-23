package com.bizcall.wayto.mentebit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class PersonalInfoActivity extends AppCompatActivity {


    EditText edtFirstName, edtLastName, edtDOB, edtParentName, edtPANNo, edtAdhaarNo, edtPassportNo,
            edtMobile1, edtMobile2, edtEmail, edtAddress, edtCity, edtPincode, edtCollegeName, edtPercentage;

    String strnewfirstname, strnewlastname, strnewgender, strnewmobile1, strnewdob, strnewpanno, strnewaadharno, strnewpassportno, strnewemail,
            strnewparentname, strnewparentno, strnewaddress, strnewcity, strnewstate, strnewpincode;
    String userCounselorName, userSrNo, userFirstName, userLastName, userDOB, userGender, userPANNo, userAadharNo, userPassportNo,
            userMobile, userParentName, userParentNo, userEmail, userAddress, userCity, userState, userPinCode;
    Spinner spinnernewgender, spinnernewstate;
    Button btnEditPersonalInfo, btnUpdatePersonalInfo;
    Button btninfo, btncommun;
    private int day, month, year;
    private Calendar leavecalendar;

    AlertDialog.Builder builder;
    AlertDialog alert;
    private ProgressDialog progressBar;
    private RequestQueue requestQueue;
    int flag=0;
    ImageView imgBack;

    TextView txtSrNo, txtCandidateName;
    SharedPreferences sp;

    String sharedSrno, sharedFirstName, sharedLastName, sharedMobile, sharedCounselorName, sharedCounselorId, sharedCounselorEmail, sharedCounselorNo, sharedUrl, sharedClientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_personal_info);
            sharedSrno = getIntent().getStringExtra("FileNo");
            sharedMobile = getIntent().getStringExtra("MobileNo");
            imgBack = findViewById(R.id.img_back);
            edtFirstName = findViewById(R.id.edtFirstName);
            edtLastName = findViewById(R.id.edtLastName);
            edtDOB = findViewById(R.id.edtSelectedDate);
            edtPANNo = findViewById(R.id.edtPanNo);
            edtAdhaarNo = findViewById(R.id.edtAdharNo);
            edtPassportNo = findViewById(R.id.edtPassportNo);
            edtMobile1 = findViewById(R.id.edtMobile1);
            edtEmail = findViewById(R.id.edtEmail);

            edtParentName = findViewById(R.id.edtParentName);
            edtMobile2 = findViewById(R.id.edtMobile2);
            edtAddress = findViewById(R.id.edtAddress);
            edtCity = findViewById(R.id.edtCity);
            edtPincode = findViewById(R.id.edtPin);

            txtSrNo = findViewById(R.id.txt_viewdetailsSrNo);
            txtCandidateName = findViewById(R.id.txt_viewdetailsCandidateName);

            spinnernewgender = findViewById(R.id.edtsex);
            spinnernewstate = findViewById(R.id.edtstate);
            spinnernewgender.setEnabled(false);
            spinnernewstate.setEnabled(false);

            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE); // 0 - for private mode

            sharedUrl = sp.getString("ClientUrl", null);
            sharedClientID = sp.getString("ClientId", null);
            sharedCounselorId = sp.getString("Id", null);
            sharedCounselorId = sharedCounselorId.replaceAll(" ", "");

            Log.d("tagg", sharedSrno + " " + sharedFirstName + " " + sharedMobile + " " + sharedCounselorName);

            btnEditPersonalInfo = findViewById(R.id.btn_editcommun);
            btnUpdatePersonalInfo = findViewById(R.id.btn_updatecommun);
            btninfo = findViewById(R.id.btn_PersonalInfo);
            btncommun = findViewById(R.id.btn_Communication);
            if (CheckInternetSpeed.checkInternet(PersonalInfoActivity.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PersonalInfoActivity.this);
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
            } else if (CheckInternetSpeed.checkInternet(PersonalInfoActivity.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PersonalInfoActivity.this);
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
                progressBar = ProgressDialog.show(PersonalInfoActivity.this, "", "Loading Personal Details", false, true);
                getPersonalInfo();
            }
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            btnEditPersonalInfo.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
                    edtFirstName.setFocusableInTouchMode(true);
                    edtFirstName.requestFocus();
                    edtLastName.setFocusableInTouchMode(true);
                    edtLastName.requestFocus();

                    leavecalendar = Calendar.getInstance();
                    day = leavecalendar.get(Calendar.DAY_OF_MONTH);
                    year = leavecalendar.get(Calendar.YEAR);
                    month = leavecalendar.get(Calendar.MONTH);
                    edtDOB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    edtDOB.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                }
                            };
                            DatePickerDialog dpDialog = new DatePickerDialog(PersonalInfoActivity.this, listener, year, month, day);
                            dpDialog.show();
                        }
                    });

                    spinnernewgender.setEnabled(true);
                    edtPANNo.setFocusableInTouchMode(true);
                    edtPANNo.requestFocus();
                    edtAdhaarNo.setFocusableInTouchMode(true);
                    edtAdhaarNo.requestFocus();
                    edtPassportNo.setFocusableInTouchMode(true);
                    edtPassportNo.requestFocus();
                    edtEmail.setFocusableInTouchMode(true);
                    edtEmail.requestFocus();
                    edtParentName.setFocusableInTouchMode(true);
                    edtParentName.requestFocus();
                    edtMobile2.setFocusableInTouchMode(true);
                    edtMobile2.requestFocus();
                    edtAddress.setFocusableInTouchMode(true);
                    edtAddress.requestFocus();
                    edtCity.setFocusableInTouchMode(true);
                    edtCity.requestFocus();
                    spinnernewstate.setEnabled(true);
                    edtPincode.setFocusableInTouchMode(true);
                    edtPincode.requestFocus();

                    btnUpdatePersonalInfo.setVisibility(View.VISIBLE);
                    btnEditPersonalInfo.setVisibility(View.GONE);
                }
            });
            btnUpdatePersonalInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                btnUpdateClicked();

                }
            });
        }catch (Exception e)
        {
            Toast.makeText(PersonalInfoActivity.this,"Errorcode-314 PersonalInfo onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//onCreate

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(PersonalInfoActivity.this,SummaryDetails.class);
        intent.putExtra("FileNo",sharedSrno);
        intent.putExtra("MobileNo",sharedMobile);
        startActivity(intent);
    }
    public void btnUpdateClicked(){
        try {
            builder = new AlertDialog.Builder(PersonalInfoActivity.this);
            builder.setTitle("Do you want Update Information?")
                    .setCancelable(false)
                    .setIcon(R.drawable.exclamationicon)
                    .setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    edtFirstName.setFocusable(false);
                    edtLastName.setFocusable(false);
                    edtDOB.setFocusable(false);
                    edtParentName.setFocusable(false);
                    edtPANNo.setFocusable(false);
                    edtAdhaarNo.setFocusable(false);
                    edtPassportNo.setFocusable(false);
                    edtMobile1.setFocusable(false);
                    edtMobile2.setFocusable(false);
                    edtEmail.setFocusable(false);
                    edtAddress.setFocusable(false);
                    edtCity.setFocusable(false);
                    edtPincode.setFocusable(false);
                    spinnernewgender.setEnabled(false);
                    spinnernewstate.setEnabled(false);
                    btnEditPersonalInfo.setVisibility(View.VISIBLE);
                    btnUpdatePersonalInfo.setVisibility(View.GONE);
                    dialog.cancel();
                }
            });
            // Setting Positive "OK" Button
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    flag = 0;
                    strnewfirstname = edtFirstName.getText().toString();
                    strnewlastname = edtLastName.getText().toString();
                    strnewdob = edtDOB.getText().toString();
                    strnewgender = spinnernewgender.getSelectedItem().toString();
                    strnewpanno = edtPANNo.getText().toString();
                    strnewaadharno = edtAdhaarNo.getText().toString();
                    strnewpassportno = edtPassportNo.getText().toString();
                    strnewmobile1 = edtMobile1.getText().toString();
                    strnewemail = edtEmail.getText().toString();

                    strnewparentname = edtParentName.getText().toString();
                    strnewparentno = edtMobile2.getText().toString();
                    strnewaddress = edtAddress.getText().toString();
                    strnewcity = edtCity.getText().toString();
                    strnewstate = spinnernewstate.getSelectedItem().toString();
                    strnewpincode = edtPincode.getText().toString();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (strnewfirstname.equals("")) {
                        edtFirstName.setError("Invalid firstname");
                        flag = 1;
                    } else if (strnewlastname.equals("")) {
                        edtLastName.setError("Invalid lastname");
                        flag = 1;
                    } else if (strnewmobile1.equals("")) {
                        edtMobile1.setError("Invalid mobile");
                        flag = 1;
                    } else if (strnewemail.equals("") || !strnewemail.matches(emailPattern)) {
                        edtEmail.setError("Invalid email");
                        flag = 1;
                    } else if (strnewparentname.equals("")) {
                        edtParentName.setError("Invalid parentname");
                        flag = 1;
                    }
                    //progressBar.dismiss();

                    if (flag == 0) {
                        if (CheckInternetSpeed.checkInternet(PersonalInfoActivity.this).contains("0")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PersonalInfoActivity.this);
                            alertDialogBuilder.setTitle("No Internet connection!!!")
                                    .setMessage("Can't do further process")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else if (CheckInternetSpeed.checkInternet(PersonalInfoActivity.this).contains("1")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PersonalInfoActivity.this);
                            alertDialogBuilder.setTitle("Slow Internet speed!!!")
                                    .setMessage("Can't do further process")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            progressBar = ProgressDialog.show(PersonalInfoActivity.this, "", "Inserting personal ", false, true);

                            Log.d("newpersonalinfo", strnewfirstname + " " + strnewlastname + " " + strnewdob + " " + strnewgender + " "
                                    + strnewpanno + " " + strnewaadharno + " " + strnewpassportno + " " + strnewmobile1 + " " + strnewemail);
                            insertPersonalInfoD2();

                            //------------------------------------------------------------------
                            // http://anilsahasrabuddhe.in/CRM/wayto965425/wayto965425_testing_final06052019.php?clientid=wayto965425&caseid=62&SrNo=2&Notification=2%20Client%20updated%20his%20Details&CounselorId=1
                            btnEditPersonalInfo.setVisibility(View.VISIBLE);
                            btnUpdatePersonalInfo.setVisibility(View.GONE);
                        }
                    }
                }
            });
            alert = builder.create();
            try {
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e)
        {
            Toast.makeText(PersonalInfoActivity.this,"Errorcode-315 PersonalInfo btnUpdateClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void setNotification()
    {
        try {
            String strNotify = sharedSrno + "_client_updated_details";
            String strurl3 = sharedUrl + "?clientid=" + sharedClientID + "&caseid=62&SrNo=" + sharedSrno + "&CounselorId=" + sharedCounselorId +
                    "&Notification=" + strNotify;

            Log.d("newqualification", strurl3);
            StringRequest stringRequest3 = new StringRequest(Request.Method.GET, strurl3, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("Data inserted successfully")) {
                        progressBar.dismiss();
                        Intent intent = new Intent(PersonalInfoActivity.this, PersonalInfoActivity.class);
                        intent.putExtra("FileNo", sharedSrno);
                        intent.putExtra("MobileNo", sharedMobile);
                        startActivity(intent);
                        Toast.makeText(PersonalInfoActivity.this, "Notification sent to Counselor", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PersonalInfoActivity.this,"Errorcode-317 PersonalInfo setNotificationResponse "+error.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", String.valueOf(error));
                }
            });

            stringRequest3.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            stringRequest3.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest3);
        }catch (Exception e)
        {
            Toast.makeText(PersonalInfoActivity.this,"Errorcode-316 PersonalInfo setNotification "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void insertPersonalInfoD3()
    {
        try {
            String strurl2 = sharedUrl + "?clientid=" + sharedClientID + "&caseid=D3&SrNo=" + sharedSrno + "&AllocatedTo=" + sharedCounselorId +
                    "&CandidateFName=" + strnewfirstname + "&CandidateLName=" + strnewlastname + "&DOB=" + strnewdob +
                    "&Gender=" + strnewgender + "&PanNo=" + strnewpanno + "&AadhaarNo=" + strnewaadharno +
                    "&PassportNo=" + strnewpassportno + "&Mobile=" + strnewmobile1 + "&Email=" + strnewemail +
                    "&ParentName=" + strnewparentname + "&ParantNo=" + strnewparentno + "&AddressLine=" + strnewaddress +
                    "&City=" + strnewcity + "&State=" + strnewstate + "&PinCode=" + strnewpincode;

            Log.d("newqualification", strurl2);
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, strurl2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("Data inserted successfully")) {
                        // progressBar.dismiss();
                        setNotification();

                        Toast.makeText(PersonalInfoActivity.this, "Personal Info inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PersonalInfoActivity.this,"Errorcode-319 PersonalInfo PersonalInfoD3Response "+error.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", String.valueOf(error));
                }
            });

            stringRequest2.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                }
            });
            stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest2);
        }catch (Exception e)
        {
            Toast.makeText(PersonalInfoActivity.this,"Errorcode-318 PersonalInfo insertPersonalInfoD3 "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void insertPersonalInfoD2()
    {
        try {
            if(CheckServer.isServerReachable(PersonalInfoActivity.this)) {
                requestQueue = Volley.newRequestQueue(PersonalInfoActivity.this);
                String strurl1 = sharedUrl + "?clientid=" + sharedClientID + "&caseid=D2&SrNo=" + sharedSrno;
                Log.d("dd2", strurl1);
                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, strurl1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("ChatResponse", response);
                        if (response.contains("Row updated successfully")) {
                            insertPersonalInfoD3();
                            //  progressBar.dismiss();
                            //  Toast.makeText(PersonalInfoActivity.this, "Personal Info inserted successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PersonalInfoActivity.this, "Errorcode-321 PersonalInfo PersonalInfoD2Response " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("VolleyError", String.valueOf(error));
                    }
                });
                requestQueue.add(stringRequest1);
            }else {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PersonalInfoActivity.this);
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
            Toast.makeText(PersonalInfoActivity.this,"Errorcode-320 PersonalInfo insertPersonalInfoD2 "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    public void getPersonalInfo()
    {
        try {
            if(CheckServer.isServerReachable(PersonalInfoActivity.this)) {
                String strUrl = sharedUrl + "?clientid=" + sharedClientID + "&caseid=D1&SrNo=" + sharedSrno + "&MobileNo=" + sharedMobile;
                Log.d("D1Url", strUrl);
                requestQueue = Volley.newRequestQueue(PersonalInfoActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strUrl,
                        null, new personalInfoSuccess(), new personalInfoFail());

                jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 50000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 50000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);
            }
            else {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PersonalInfoActivity.this);
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
            Toast.makeText(PersonalInfoActivity.this,"Errorcode-322 PersonalInfo getPersonalInfo "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    private class personalInfoSuccess implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            Log.e("Success1", String.valueOf(response));
                    //progressBar.dismiss();
            try {
                progressBar.dismiss();
                JSONArray jsonArray = response.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    userCounselorName = jsonObject.getString("cCounselorName");
                    userSrNo = jsonObject.getString("nSrNo");
                    userFirstName = jsonObject.getString("cCandidateFName");
                    userLastName = jsonObject.getString("cCandidateLName");
                    userDOB = jsonObject.getString("dtDOB");
                    userGender = jsonObject.getString("cGender");
                    userPANNo = jsonObject.getString("cPanNo");
                    userAadharNo = jsonObject.getString("cAadhaarNo");
                    userPassportNo = jsonObject.getString("cPassportNo");
                    userMobile = jsonObject.getString("cMobile");
                    userEmail = jsonObject.getString("cEmail");
                    userParentName = jsonObject.getString("cParentName");
                    userParentNo = jsonObject.getString("cParantNo");
                    userAddress = jsonObject.getString("cAddressLine");
                    userCity = jsonObject.getString("cCity");
                    userState = jsonObject.getString("cState");
                    userPinCode = jsonObject.getString("cPinCode");


                    Log.d("recordviewdetails", userCounselorName + " / " + userSrNo + " / " + userFirstName + " / " + userLastName + " / " +
                            userDOB + " / " + userGender + " / " + userPANNo + " / " + userAadharNo + " / " + userPassportNo + " / " + userMobile + " / " + userEmail + " / " +
                            userParentName + " / " + userParentNo + " / " + userAddress + " / " + userCity + " / " + userState + " / " + userPinCode);

                }

                // --------------------------------spinners-----------------------------------------------

                ArrayAdapter<CharSequence> dataAdapterGender = ArrayAdapter.createFromResource(PersonalInfoActivity.this, R.array.Gender, android.R.layout.simple_spinner_item );
                dataAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnernewgender.setAdapter(dataAdapterGender);

                ArrayAdapter<CharSequence> dataAdapterState = ArrayAdapter.createFromResource(PersonalInfoActivity.this, R.array.States, android.R.layout.simple_spinner_item );
                dataAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnernewstate.setAdapter(dataAdapterState);

                txtSrNo.setText(sharedSrno);
                txtCandidateName.setText(userFirstName+" "+userLastName);
                edtFirstName.setText(userFirstName);
                edtLastName.setText(userLastName);
                edtDOB.setText(userDOB);
                for (int i = 0; i<spinnernewgender.getCount(); i++){
                    if (spinnernewgender.getItemAtPosition(i).toString().contains(userGender)){
                        spinnernewgender.setSelection(i);
                    }
                }
                edtPANNo.setText(userPANNo);
                edtAdhaarNo.setText(userAadharNo);
                edtPassportNo.setText(userPassportNo);
                edtMobile1.setText(userMobile);
                edtEmail.setText(userEmail);

                edtParentName.setText(userParentName);
                edtMobile2.setText(userParentNo);
                edtAddress.setText(userAddress);
                edtCity.setText(userCity);
                for (int i = 0; i<spinnernewstate.getCount(); i++){
                    if (spinnernewstate.getItemAtPosition(i).toString().contains(userState)){
                        spinnernewstate.setSelection(i);
                    }
                }
                edtPincode.setText(userPinCode);
            } catch (JSONException e) {
                Toast.makeText(PersonalInfoActivity.this,"Errorcode-323 PersonalInfo PersonalInfoResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private class personalInfoFail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(PersonalInfoActivity.this,"Errorcode-324 PersonalInfo PersonalInfoResponse failed "+error.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Fail", String.valueOf(error));
        }
    }

}
