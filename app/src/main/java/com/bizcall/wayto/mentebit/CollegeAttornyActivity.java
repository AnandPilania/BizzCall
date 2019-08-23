package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class CollegeAttornyActivity extends AppCompatActivity {
        String url,clienturl,clientid,counselorid,fileno,fullname,mobile;
        RequestQueue requestQueue;
        ProgressDialog dialog;
        SharedPreferences sp;
        Spinner spinnerCollegeList,spinnerAttornyList;
        ArrayList<String> arrayListCollege;
        ArrayList<String> arrayListAttorny;
        ArrayList<String> arrayListClgId,arrayListAttornyId;
        TextView txtFileNo,txtMobile,txtSubmit,txtUpdateClgAttorny,txtAllocatedClg,txtAllocatedAttorny,txtSelectClg,txtSelectAttorny;
        LinearLayout linearCollege;
        ImageView imgBack;
        String pos,pos1,clgid,attornyid;
        int clickedcount;
        LinearLayout linearSpinnerClg,linearSpinnerAttorny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_college_attorny);
            txtSelectClg = findViewById(R.id.txtSelectClg);
            txtSelectAttorny = findViewById(R.id.txtSelectAttorny);
            spinnerCollegeList = findViewById(R.id.spinnerCollgeList);
            spinnerAttornyList = findViewById(R.id.spinnerAttornyList);
            linearSpinnerAttorny = findViewById(R.id.linearSpinnerAttornylist);
            linearSpinnerClg = findViewById(R.id.linearSpinnerClgList);
            txtSubmit = findViewById(R.id.txtSubmitClgAttorny);
            txtAllocatedClg = findViewById(R.id.txtAllocatedClg);
            txtAllocatedAttorny = findViewById(R.id.txtAllocatedAttorny);
            txtUpdateClgAttorny = findViewById(R.id.txtUpdateClgAttorny);
            txtFileNo = findViewById(R.id.txtFileNo);
            txtMobile = findViewById(R.id.txtMobileNo);
            imgBack = findViewById(R.id.img_back);
            linearCollege = findViewById(R.id.linearCollege);
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clientid = sp.getString("ClientId", null);
            clienturl = sp.getString("ClientUrl", null);
            counselorid = sp.getString("Id", null);
            fileno = sp.getString("FileNo", null);
            mobile = sp.getString("MobileNo", null);
            fullname = sp.getString("FullName", null);
            txtFileNo.setText(fileno + "." + fullname);
            txtMobile.setText(mobile);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            if (CheckInternetSpeed.checkInternet(CollegeAttornyActivity.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
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
            } else if (CheckInternetSpeed.checkInternet(CollegeAttornyActivity.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
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
                dialog = ProgressDialog.show(CollegeAttornyActivity.this, "", "Checking college allocated or not", true);
                getCollegeList();
            }

            spinnerCollegeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pos = arrayListClgId.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinnerCollegeList.setSelection(0);
                }
            });
            txtUpdateClgAttorny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
                    alertDialogBuilder.setTitle("Are you sure you want to change this college?")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    linearSpinnerAttorny.setVisibility(View.VISIBLE);
                                    linearSpinnerClg.setVisibility(View.VISIBLE);
                                    txtAllocatedAttorny.setVisibility(View.GONE);
                                    txtAllocatedClg.setVisibility(View.GONE);
                                    txtSelectClg.setText("College Name");
                                    txtSelectAttorny.setText("Attorny Name");
                                    txtSubmit.setVisibility(View.VISIBLE);
                                    txtUpdateClgAttorny.setVisibility(View.GONE);
                                   // dialog.dismiss();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }
            });
            spinnerAttornyList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pos1 = arrayListAttornyId.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinnerAttornyList.setSelection(0);
                }
            });
            txtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckInternetSpeed.checkInternet(CollegeAttornyActivity.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
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
                    } else if (CheckInternetSpeed.checkInternet(CollegeAttornyActivity.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
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
                        dialog = ProgressDialog.show(CollegeAttornyActivity.this, "", "Allocating college", true);
                        allocateCollege();
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(CollegeAttornyActivity.this,"Errorcode-342 UploadDocs onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcCollgeAttorny",e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(CollegeAttornyActivity.this,SummaryDetails.class);
        startActivity(intent);
        super.onBackPressed();
    }
    public void allocateCollege()
    {
        try {

            url = clienturl + "?clientid=" + clientid + "&caseid=90&FileNo=" + fileno + "&CollegeId=" + pos + "&CounselorID=" + pos1;
            Log.d("AllocateUrl", url);
            if (CheckInternet.checkInternet(CollegeAttornyActivity.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("allocateCollegeResponse", response);
                                try {
                                    dialog.dismiss();
                                    if (response.contains("Data inserted successfully")) {
                                        Toast.makeText(CollegeAttornyActivity.this, "College allocated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CollegeAttornyActivity.this, "College not allocated", Toast.LENGTH_SHORT).show();
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

                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(CollegeAttornyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
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
            Toast.makeText(CollegeAttornyActivity.this,"Errorcode-343 CollegeAttorny allocateCollege "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcAllocateClg",e.toString());
        }

    }

    public void checkCollege()
    {
        try {

            url = clienturl + "?clientid=" + clientid + "&caseid=87&FileNo=" + fileno;
            Log.d("AllDetailsUrl", url);
            if (CheckInternet.checkInternet(CollegeAttornyActivity.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("CheckCollegeResponse", response);
                                try {

                                    dialog.dismiss();
                                    if (response.contains("[]")) {
                                        //linearCollege.setVisibility(View.VISIBLE);
                                        linearSpinnerAttorny.setVisibility(View.VISIBLE);
                                        linearSpinnerClg.setVisibility(View.VISIBLE);
                                        txtAllocatedClg.setVisibility(View.GONE);
                                        txtAllocatedAttorny.setVisibility(View.GONE);
                                        txtSubmit.setVisibility(View.VISIBLE);
                                        txtUpdateClgAttorny.setVisibility(View.GONE);
                                    } else {
                                        txtSelectClg.setText("Allocated College Name");
                                        txtSelectAttorny.setText("Allocated Attorny Name");
                                        txtUpdateClgAttorny.setVisibility(View.VISIBLE);
                                        txtSubmit.setVisibility(View.GONE);
                                        linearSpinnerAttorny.setVisibility(View.GONE);
                                        linearSpinnerClg.setVisibility(View.GONE);
                                        txtAllocatedClg.setVisibility(View.VISIBLE);
                                        txtAllocatedAttorny.setVisibility(View.VISIBLE);

                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            clgid = jsonObject1.getString("nCollegeID");
                                            attornyid = jsonObject1.getString("cCounselorID");
                                        }
                                        for (int i = 0; i < spinnerCollegeList.getCount(); i++) {
                                            Log.d("ArraylistClgId", arrayListClgId.get(i));
                                            if (arrayListClgId.get(i).contains(clgid)) {
                                                txtAllocatedClg.setText(arrayListCollege.get(i));
                                            }
                                        }
                                        for (int i = 0; i < spinnerAttornyList.getCount(); i++) {
                                            Log.d("ArraylistAttornyId", arrayListAttornyId.get(i));
                                            if (arrayListAttornyId.get(i).contains(attornyid)) {
                                                txtAllocatedAttorny.setText(arrayListAttorny.get(i));
                                            }
                                        }


                                   /* android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
                                    alertDialogBuilder.setTitle("College/Attorny already allocated")
                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    linearCollege.setVisibility(View.GONE);
                                                    Intent intent=new Intent(CollegeAttornyActivity.this,SummaryDetails.class);
                                                    startActivity(intent);
                                                    dialog.dismiss();
                                                }
                                            })  .show();*/
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(CollegeAttornyActivity.this,"Errorcode-345 CollegeAttorny checkCollegeResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(CollegeAttornyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
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
            Log.d("ExcCheckClg",e.toString());
            Toast.makeText(CollegeAttornyActivity.this,"Errorcode-344 CollegeAttorny checkCollege "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
    public void getCollegeList()
    {
        try {

            url = clienturl + "?clientid=" + clientid + "&caseid=88";
            Log.d("GetCollegeListUrl", url);
            if (CheckInternet.checkInternet(CollegeAttornyActivity.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("CollegeListResponse", response);
                                try {
                                    arrayListCollege = new ArrayList<>();
                                    arrayListClgId = new ArrayList<>();
                                    dialog.dismiss();
                                    if (response.contains("[]")) {
                                        Toast.makeText(CollegeAttornyActivity.this, "No Colleges Available!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String clgid = jsonObject1.getString("nCollegeID");
                                            String clgname = jsonObject1.getString("cCollegeName");
                                            arrayListClgId.add(clgid);
                                            arrayListCollege.add(clgname);
                                        }
                                        getAttornyList();
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(CollegeAttornyActivity.this, R.layout.spinner_item1, arrayListCollege);
                                        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerCollegeList.setAdapter(arrayAdapter);
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(CollegeAttornyActivity.this,"Errorcode-347 CollegeAttorny getCollegeListResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(CollegeAttornyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
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
            Toast.makeText(CollegeAttornyActivity.this,"Errorcode-346 CollegeAttorny getCollegeList "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }

    public void getAttornyList()
    {
        try {

        url=clienturl+"?clientid=" + clientid + "&caseid=89";
        Log.d("AttornyListUrl", url);
        if(CheckInternet.checkInternet(CollegeAttornyActivity.this))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("AttornyListResponse", response);
                            try {
                                arrayListAttorny=new ArrayList<>();
                                arrayListAttornyId=new ArrayList<>();
                                dialog.dismiss();
                                if(response.contains("[]"))
                                {
                                    Toast.makeText(CollegeAttornyActivity.this,"No Attorny!",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("Length", String.valueOf(jsonArray.length()));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String counselorname=jsonObject1.getString("cCounselorName");
                                        String counselorid=jsonObject1.getString("cCounselorID");
                                        arrayListAttornyId.add(counselorid);
                                        arrayListAttorny.add(counselorname);
                                    }
                                    checkCollege();

                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter(CollegeAttornyActivity.this, R.layout.spinner_item1, arrayListAttorny);
                                    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerAttornyList.setAdapter(arrayAdapter);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(CollegeAttornyActivity.this,"Errorcode-349 CollegeAttorny getAttornyListResponse "+e.toString(),Toast.LENGTH_SHORT).show();
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
                            if (error.networkResponse != null||error instanceof TimeoutError ||error instanceof NoConnectionError ||error instanceof AuthFailureError ||error instanceof ServerError ||error instanceof NetworkError ||error instanceof ParseError) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                dialog.dismiss();
                                Toast.makeText(CollegeAttornyActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
        }else {
            dialog.dismiss();
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CollegeAttornyActivity.this);
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
        } }catch (Exception e)
        {
            Toast.makeText(CollegeAttornyActivity.this,"Errorcode-348 CollegeAttorny getAttornyList "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcAttornyList",e.toString());
        }
    }

}
