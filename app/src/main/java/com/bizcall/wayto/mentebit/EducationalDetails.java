package com.bizcall.wayto.mentebit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;

public class EducationalDetails extends AppCompatActivity {

    ArrayList<String> mArrayQualificationList;
    RecyclerView recyclerEducation;
    Button btnAddEducation;
    ProgressDialog dialog;

    AlertDialog alert;
    AlertDialog.Builder builder;
    Spinner spinnernewqualification,spinnerneweducation;

    EditText edtnewPassYr, edtnewMarkObt, edtnewMarkOutOf, edtnewCourse, edtnewCollegeName, edtnewPercentage;
    String strName,strMobile,strQualification,strCourse,strCollegeName,strPassYr,strMarkObt,strMarkOutOf,strPercentage,strEducationItem,sharedSrno,sharedCounselorId,clientid,clienturl,strnewQualification,strnewCourse,strnewCollege,strnewpassyr,strnewmarkobt,strnewmarkoutof,strnewpercent;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    RequestQueue requestQueue;
    int flag=0;
    View newEducate;
    private AdapterEducationalDetails adapterStudEducation;
    private ArrayList<DataStudEducation> arrayListEducation;
    DataStudEducation dataStudEducation;
    ImageView imgBack;
    TextView txtFileno,txtMobileno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_educational_details);
            requestQueue = Volley.newRequestQueue(EducationalDetails.this);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            txtFileno = findViewById(R.id.txtFileNo);
            txtMobileno = findViewById(R.id.txtMobileNo);
            imgBack = findViewById(R.id.img_back);
            clientid = sp.getString("ClientId", null);
            clienturl = sp.getString("ClientUrl", null);
            strName = sp.getString("FullName", null);
            sharedSrno = getIntent().getStringExtra("FileNo");
            strMobile = sp.getString("MobileNo", null);
            sharedCounselorId = sp.getString("Id", null);
            mArrayQualificationList = new ArrayList<>();
            recyclerEducation = findViewById(R.id.recycler_stud_educate);
            btnAddEducation = findViewById(R.id.btnAddNewEducation);
            txtFileno.setText(sharedSrno + ". " + strName);
            txtMobileno.setText(strMobile);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();

                }
            });


            if (CheckInternetSpeed.checkInternet(EducationalDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EducationalDetails.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(EducationalDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EducationalDetails.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                            }
                        }).show();
            } else {
                dialog = ProgressDialog.show(EducationalDetails.this, "", "Loading educational details", true);
                getEducationDetails();

            }

            btnAddEducation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // dialog = ProgressDialog.show(EducationalDetails.this,"","Loading educational details",true);
                    builder = new AlertDialog.Builder(EducationalDetails.this);
                    LayoutInflater li = LayoutInflater.from(EducationalDetails.this);
                    newEducate = li.inflate(R.layout.lay_newqualification, null);

                    edtnewCourse = newEducate.findViewById(R.id.edtnewCoursename);
                    edtnewCollegeName = newEducate.findViewById(R.id.edtnewCollegeName);
                    edtnewPassYr = newEducate.findViewById(R.id.edtnewPassingYear);
                    edtnewMarkObt = newEducate.findViewById(R.id.edtnewMarks);
                    edtnewMarkOutOf = newEducate.findViewById(R.id.edtnewOutof);
                    edtnewPercentage = newEducate.findViewById(R.id.edtnewPercentage);
                    spinnerneweducation = newEducate.findViewById(R.id.spinnernewqualification);
                  //  dialog = ProgressDialog.show(EducationalDetails.this, "", "Loading Qualification Details", true);
                    spinnerneweducation.setAdapter(new ArrayAdapter<String>(EducationalDetails.this, android.R.layout.simple_spinner_dropdown_item, mArrayQualificationList));

                    spinnerneweducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // On selecting a spinner item
                            strEducationItem = parent.getItemAtPosition(position).toString();
                            // Showing selected spinner item
                            //Toast.makeText(parent.getContext(), "Selected: " + strEducationItem, Toast.LENGTH_SHORT).show();
                            String country = spinnerneweducation.getItemAtPosition(spinnerneweducation.getSelectedItemPosition()).toString();
                           // Toast.makeText(getApplicationContext(), country, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            spinnerneweducation.setSelection(0);
                        }
                    });

                    builder.setView(newEducate);
                    builder.setCancelable(false);

                    // Setting Negative "Cancel" Button
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int whichButton) {
                            dialog1.cancel();
                        }
                    });

                    // Setting Positive "OK" Button
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog1, int which) {
                            flag=0;
                            strnewQualification = spinnerneweducation.getSelectedItem().toString();
                            strnewCourse = edtnewCourse.getText().toString();
                            strnewCollege = edtnewCollegeName.getText().toString();
                            strnewpassyr = edtnewPassYr.getText().toString();
                            strnewmarkobt = edtnewMarkObt.getText().toString();
                            strnewmarkoutof = edtnewMarkOutOf.getText().toString();
                            strnewpercent = edtnewPercentage.getText().toString();

                            if ((strnewCourse.length() == 0 || strnewCollege.length() == 0 || strnewpassyr.length() == 0 ||
                                    strnewmarkobt.length() == 0 || strnewmarkoutof.length() == 0 || strnewpercent.length() == 0)) {
                                Toast.makeText(EducationalDetails.this, "Enter valid inputs.", Toast.LENGTH_SHORT).show();
                               // dialog1.dismiss();
                                flag = 1;
                            }
                            if (flag == 0) {
                                alertOkClicked();
                            }
                        }
                    });
                    alert = builder.create();
                    try {
                        //alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alert.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });
        }catch (Exception e)
        {
            Toast.makeText(EducationalDetails.this,"Errorcode-325 EducationalDetails onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//onCreate
    public void alertOkClicked(){
        try {
            if (CheckInternetSpeed.checkInternet(EducationalDetails.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EducationalDetails.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                            }
                        }).show();
            } else if (CheckInternetSpeed.checkInternet(EducationalDetails.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EducationalDetails.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                            }
                        }).show();
            } else {
                if(CheckServer.isServerReachable(EducationalDetails.this)) {
                    Log.d("educationalinfo", strnewQualification + " " + strnewCourse + " " + strnewCollege + " " + strnewpassyr + " "
                            + strnewmarkobt + " " + strnewmarkoutof + " " + strnewpercent);
                    requestQueue = Volley.newRequestQueue(EducationalDetails.this);
                    String strurl51 = clienturl + "?clientid=" + clientid + "&caseid=D51&SrNo=" + sharedSrno +
                            "&Qualification=" + strnewQualification;

                    Log.d("dd51", strurl51);
                    StringRequest stringRequest51 = new StringRequest(Request.Method.GET, strurl51, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("ChatResponse", response);
                            if (response.contains("Data inserted successfully")) {
                                // Toast.makeText(EducationalDetails.this, "Details Updated successfully", Toast.LENGTH_SHORT).show();

                                //-----------------------------------------------------------------------------
                                String strurl = clienturl + "?clientid=" + clientid + "&caseid=D5&SrNo=" + sharedSrno +
                                        "&Qualification=" + strnewQualification + "&CourseName=" + strnewCourse + "&CollegeName=" + strnewCollege +
                                        "&PassingYear=" + strnewpassyr + "&MarksOpt=" + strnewmarkobt + "&MarksOutOF=" + strnewmarkoutof + "&Persentange=" + strnewpercent;

                                Log.d("dd5", strurl);
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //Log.d("ChatResponse", response);
                                        if (response.contains("Data inserted successfully")) {
                                            Toast.makeText(EducationalDetails.this, "Qualification inserted successfully", Toast.LENGTH_SHORT).show();

                                            //------------------------------Notification to counselor--------------------------------

                                            String strNotify = sharedSrno + "_Client_updated_his_" + strnewQualification + "_Education.";
                                            String strurl3 = clienturl + "?clientid=" + clientid + "&caseid=62&SrNo=" + sharedSrno + "&CounselorId=" + sharedCounselorId +
                                                    "&Notification=" + strNotify;

                                            Log.d("newqualification", strurl3);
                                            StringRequest stringRequest3 = new StringRequest(Request.Method.GET, strurl3, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    //Log.d("ChatResponse", response);
                                                    if (response.contains("Data inserted successfully")) {
                                                        dialog.dismiss();
                                                        Toast.makeText(EducationalDetails.this, "Notification send to Counselor.", Toast.LENGTH_SHORT).show();

                                                        //------------------------------Refresh Education Details---------------------------------
                                                        dialog = ProgressDialog.show(EducationalDetails.this, "", "Inserting educational details", true);
                                                        getEducationDetails();

                                                    } else {
                                                        Toast.makeText(EducationalDetails.this, "Educational details not inserted", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
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
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("VolleyError", String.valueOf(error));
                                    }
                                });

                                stringRequest.setRetryPolicy(new RetryPolicy() {
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
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                requestQueue.add(stringRequest);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("VolleyError", String.valueOf(error));
                        }
                    });

                    stringRequest51.setRetryPolicy(new RetryPolicy() {
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
                    stringRequest51.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest51);
                }else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EducationalDetails.this);
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
            }
        }catch (Exception e)
        {
            Toast.makeText(EducationalDetails.this,"Errorcode-326 EducationalDetails alertOkClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void getEducationDetails()
    {
        try {
            if(CheckServer.isServerReachable(EducationalDetails.this)) {
                String strEducationInfo = clienturl + "?clientid=" + clientid + "&caseid=D4&SrNo=" + sharedSrno;
                Log.d("refreshEdu", strEducationInfo);
                requestQueue = Volley.newRequestQueue(EducationalDetails.this);
                JsonObjectRequest jsonEducationInfo = new JsonObjectRequest(Request.Method.GET, strEducationInfo,
                        null, new educationsuccess(), new educationfail());

                jsonEducationInfo.setRetryPolicy(new RetryPolicy() {
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
                jsonEducationInfo.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(jsonEducationInfo);
            }else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EducationalDetails.this);
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
            Toast.makeText(EducationalDetails.this,"Errorcode-327 EducationalDetails getEducationalDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSpinnerData(String url) {
        try {
           // dialog=ProgressDialog.show(EducationalDetails.this,"","Loading specification",true);
            if (CheckServer.isServerReachable(EducationalDetails.this)) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(dialog.isShowing())
                            {
                                dialog.dismiss();
                            }
                        mArrayQualificationList = new ArrayList<>();
                        mArrayQualificationList.clear();

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String strqualification = jsonObject1.getString("cQualification");
                                mArrayQualificationList.add(strqualification);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EducationalDetails.this, "Errorcode-331 EducationalDetails SpinnerDataResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);
            }else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EducationalDetails.this);
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
        }
        catch (Exception e)
        {
            Toast.makeText(EducationalDetails.this,"Errorcode-330 EducationalDetails loadSpinnerData "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    private class educationsuccess implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try{
           // dialog.dismiss();
            Log.e("Success", String.valueOf(response));
            arrayListEducation = new ArrayList<>();
            adapterStudEducation = new AdapterEducationalDetails(arrayListEducation, EducationalDetails.this);

            LinearLayoutManager lm = new LinearLayoutManager(EducationalDetails.this);
            lm.setOrientation(LinearLayoutManager.VERTICAL);
                String strDocumentList = clienturl + "?clientid=" + clientid + "&caseid=D6";
                Log.d("dd6", strDocumentList);
                loadSpinnerData(strDocumentList);

            //recyclerEducation.addItemDecoration(new DividerItemDecoration(ShowDetailsClass.this, DividerItemDecoration.VERTICAL));
            recyclerEducation.setLayoutManager(lm);
            recyclerEducation.setAdapter(adapterStudEducation);



                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    strQualification = jsonObject.getString("cQualification");
                    strCourse = jsonObject.getString("cCourseName");
                    strCollegeName = jsonObject.getString("cCollegeName");
                    strPassYr = jsonObject.getString("cPassingYear");
                    strMarkObt = jsonObject.getString("cMarksOpt");
                    strMarkOutOf = jsonObject.getString("cMarksOutOF");
                    strPercentage = jsonObject.getString("cPersentange");

                    dataStudEducation = new DataStudEducation(strQualification, strCourse, strCollegeName, strPassYr, strMarkObt, strMarkOutOf, strPercentage);
                    arrayListEducation.add(dataStudEducation);
                    adapterStudEducation.notifyDataSetChanged();
                    Log.d("educationarray", arrayListEducation.size() + "");
                }

            } catch (JSONException e) {
                Toast.makeText(EducationalDetails.this,"Errorcode-328 EducationalDetails EducationalDetailsResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    private class educationfail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("Education Fail", String.valueOf(error));
            Toast.makeText(EducationalDetails.this,"Errorcode-329 EducationalDetails EducationalDetailsResponse failed "+error.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(EducationalDetails.this,SummaryDetails.class);
        intent.putExtra("FileNo",sharedSrno);
        intent.putExtra("MobileNo",strMobile);
        startActivity(intent);
    }
}



