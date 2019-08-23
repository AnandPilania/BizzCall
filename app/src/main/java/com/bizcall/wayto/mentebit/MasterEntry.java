package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
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

public class MasterEntry extends AppCompatActivity {

    private Spinner mSearchspinner;
    private LinearLayout linearLayout;
    private RecyclerView mRecyclerviewTabledetails;
    HorizontalScrollView horizontalScrollView;
    TextView txtNoResult;

    private ArrayList<DataMasterEntry> arrayListMasterEntry;
    private AdapterMasterEntry myAdapter;
    private DataMasterEntry dataMasterEntry;
    Button btnAllDetails;

    private TextView txtTitle,txtSpinnerError;
    private EditText edtSearchText;
    private String  counselorid,strfirstname,strlastname,strparentname, strcity, strpincode, strmobile, stremailid,
            strreferenceno, strreferenceno2, strpassportno,clienturl,clientid,url;
  String strFileId,strSrNo,strAllocatedTo,strFname, strLname,strDob,searchAs,result;
    ProgressDialog dialog;
    SharedPreferences sp;
    RequestQueue requestQueue;
    Button btnSearch;
    Vibrator vibrator;
    LinearLayout linearTableColumn;

    private String [] mSearchMethod = {"Select", "File ID", "Serial No","First Name", "Last Name","Parent Name","Pan No","Aadhaar No", "Passport No","Mobile No","Parent No","EmailID","City","State","Pin Code"};
        ImageView imgBack;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            setContentView(R.layout.activity_master_entry);
            horizontalScrollView = findViewById(R.id.search_horizontalscroll);
            txtNoResult = findViewById(R.id.txtNoResultFound);
            linearTableColumn = findViewById(R.id.linearTableColumn);
            imgBack = findViewById(R.id.img_back);
            mSearchspinner = findViewById(R.id.search_spinner);
            btnSearch = findViewById(R.id.btn_search);
            linearLayout = findViewById(R.id.searchby_layout);
            edtSearchText = findViewById(R.id.edtSearchtext);
            txtTitle = findViewById(R.id.txtTitle);
            txtSpinnerError = findViewById(R.id.txtSpinnerError);
            mRecyclerviewTabledetails = findViewById(R.id.detail_recyclerview);
            btnAllDetails = findViewById(R.id.btnAlldetails);
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            clientid = sp.getString("ClientId", null);
            clienturl = sp.getString("ClientUrl", null);
            counselorid = sp.getString("Id", null);
            counselorid = counselorid.replaceAll(" ", "");

            requestQueue = Volley.newRequestQueue(MasterEntry.this);
            // arrayListMasterEntry = new ArrayList<>();

            ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(MasterEntry.this,
                    android.R.layout.simple_spinner_dropdown_item, mSearchMethod);
            mSearchspinner.setAdapter(mSpinnerAdapter);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
            mSearchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    searchAsItemSelected();
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            txtTitle.setText("File ID");
                            break;
                        case 2:
                            txtTitle.setText("Serial No");
                            break;
                        case 3:
                            txtTitle.setText("First Name");
                            break;
                        case 4:
                            txtTitle.setText("Last Name");
                            break;
                        case 5:
                            txtTitle.setText("Parent Name");
                            break;
                        case 6:
                            txtTitle.setText("Pan No");
                            break;
                        case 7:
                            txtTitle.setText("Aadhar No");
                            break;
                        case 8:
                            txtTitle.setText("Passport No");
                            break;
                        case 9:
                            txtTitle.setText("Mobile No");
                            break;
                        case 10:
                            txtTitle.setText("Parent No");
                            break;
                        case 11:
                            txtTitle.setText("Email ID");
                            break;
                        case 12:
                            txtTitle.setText("City");
                            break;
                        case 13:
                            txtTitle.setText("State");
                            break;
                        case 14:
                            txtTitle.setText("Pin code");
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            btnAllDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckInternetSpeed.checkInternet(MasterEntry.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
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
                    } else if (CheckInternetSpeed.checkInternet(MasterEntry.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
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
                        dialog = ProgressDialog.show(MasterEntry.this, "", "Loading All Details", true);
                        getAllDetails();
                    }
                }
            });
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String searchtext = edtSearchText.getText().toString();
                    String searchAs = mSearchspinner.getSelectedItem().toString();
                    if (searchAs.contains("Select")) {
                        txtSpinnerError.setVisibility(View.VISIBLE);
                    } else {
                        txtSpinnerError.setVisibility(View.GONE);
                        if (CheckInternetSpeed.checkInternet(MasterEntry.this).contains("0")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
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
                        } else if (CheckInternetSpeed.checkInternet(MasterEntry.this).contains("1")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
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
                            dialog = ProgressDialog.show(MasterEntry.this, "", "Searching data", true);
                            searchDetails(searchtext);
                        }
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(MasterEntry.this,"Errorcode-305 MasterEntry onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//onCreate

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MasterEntry.this,Home.class);
        intent.putExtra("Activity","MasterEntry");
        startActivity(intent);
        super.onBackPressed();
    }

    public void searchAsItemSelected()
    {
        try {
            arrayListMasterEntry = new ArrayList<>();
            searchAs = mSearchspinner.getSelectedItem().toString();
            if (searchAs.contains("First Name")) {
                searchAs = "cCandidateFName";
            } else if (searchAs.contains("Serial No")) {
                searchAs = "nSrNo";
            } else if (searchAs.contains("Last Name")) {
                searchAs = "cCandidateLName";
            } else if (searchAs.contains("Parent Name")) {
                searchAs = "cParentName";
            } else if (searchAs.contains("Parent No")) {
                searchAs = "cParantNo";
            } else if (searchAs.contains("City")) {
                searchAs = "cCity";
            } else if (searchAs.contains("State")) {
                searchAs = "cState";
            } else if (searchAs.contains("Pin Code")) {
                searchAs = "cPinCode ";
            } else if (searchAs.contains("Mobile No")) {
                searchAs = "cMobile ";
            } else if (searchAs.contains("EmailID")) {
                searchAs = "cEmail";
            } else if (searchAs.contains("File ID")) {
                searchAs = "nFileID";
            } else if (searchAs.contains("Pan No")) {
                searchAs = "cPanNo";
            } else if (searchAs.contains("Passport No")) {
                searchAs = "cPassportNo";
            } else if (searchAs.contains("Aadhaar No")) {
                searchAs = "cAadhaarNo";
            }
        }catch (Exception e)
        {
            Toast.makeText(MasterEntry.this,"Errorcode-306 MasterEntry searchAsItemSelected "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void searchDetails(String searchtext)
    {
        try {
            arrayListMasterEntry = new ArrayList<>();
            url = clienturl + "?clientid=" + clientid + "&caseid=79&SearchAs=" + searchAs + "&SearchVal=" + searchtext + "&CounselorID=" + counselorid;
            Log.d("SearchDetailsUrl", url);
            if (CheckInternet.checkInternet(MasterEntry.this)) {
                if(CheckServer.isServerReachable(MasterEntry.this)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("SearchDetailsResponse", response);
                                         dialog.dismiss();
                                        if (response.contains("[]")) {
                                            // linearTableColumn.setVisibility(View.GONE);
                                            horizontalScrollView.setVisibility(View.GONE);
                                            txtNoResult.setVisibility(View.VISIBLE);
                                        } else {
                                            horizontalScrollView.setVisibility(View.VISIBLE);
                                            txtNoResult.setVisibility(View.GONE);
                                            //linearTableColumn.setVisibility(View.VISIBLE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                strFileId = jsonObject1.getString("nFileID");
                                                strSrNo = jsonObject1.getString("nSrNo");
                                                strAllocatedTo = jsonObject1.getString("AllocatedTo");
                                                strFname = jsonObject1.getString("cCandidateFName");
                                                strLname = jsonObject1.getString("cCandidateLName");
                                                JSONObject jsonObject2 = jsonObject1.getJSONObject("dtDOB");
                                                strDob = jsonObject2.getString("date");
                                                String strGender = jsonObject1.getString("cGender");
                                                String strParentName = jsonObject1.getString("cParentName");
                                                String strPanNo = jsonObject1.getString("cPanNo");
                                                String strAdharNo = jsonObject1.getString("cAadhaarNo");
                                                String strPassportNo = jsonObject1.getString("cPassportNo");
                                                String strMobile = jsonObject1.getString("cMobile");
                                                String strParentNo = jsonObject1.getString("cParantNo");
                                                String strEmail = jsonObject1.getString("cEmail");
                                                String strPincode = jsonObject1.getString("cPinCode");
                                                String strAddress = jsonObject1.getString("cAddressLine");
                                                String strCity = jsonObject1.getString("cCity");
                                                String strState = jsonObject1.getString("cState");
                                                String strCreatedDt = jsonObject1.getString("dtCreatedDate");
                                                String strUpdatedDt = jsonObject1.getString("dtUpdatedDate");
                                                String strRemarks = jsonObject1.getString("cRemarks");

                                                DataMasterEntry dataMasterEntry = new DataMasterEntry(strFileId, strSrNo, strFname, strLname,
                                                        strDob, strGender, strParentName, strAddress, strCity, strState, strPincode, strMobile, strParentNo, strEmail, strRemarks, "Confirmed");
                                                arrayListMasterEntry.add(dataMasterEntry);
                                            }
                                            myAdapter = new AdapterMasterEntry(arrayListMasterEntry, MasterEntry.this);

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MasterEntry.this);
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                                            mRecyclerviewTabledetails.addItemDecoration(new DividerItemDecoration(MasterEntry.this, DividerItemDecoration.VERTICAL));
                                            mRecyclerviewTabledetails.setLayoutManager(linearLayoutManager);
                                            mRecyclerviewTabledetails.setAdapter(myAdapter);
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MasterEntry.this, "Errorcode-308 MasterEntry searchDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(MasterEntry.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
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
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
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
            Toast.makeText(MasterEntry.this,"Errorcode-307 MasterEntry searchDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void getAllDetails()
    {
        try {
            arrayListMasterEntry = new ArrayList<>();
            url = clienturl + "?clientid=" + clientid + "&caseid=78&CounselorID=" + counselorid;
            Log.d("AllDetailsUrl", url);
            if (CheckInternet.checkInternet(MasterEntry.this)) {
                if(CheckServer.isServerReachable(MasterEntry.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("AllDetailsResponse", response);
                                    try {
                                        dialog.dismiss();
                                        if (response.contains("[]")) {
                                            horizontalScrollView.setVisibility(View.GONE);
                                            txtNoResult.setVisibility(View.VISIBLE);
                                            // linearTableColumn.setVisibility(View.VISIBLE);
                                        } else {
                                            horizontalScrollView.setVisibility(View.VISIBLE);
                                            txtNoResult.setVisibility(View.GONE);

                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                strFileId = jsonObject1.getString("nFileID");
                                                strSrNo = jsonObject1.getString("nSrNo");
                                                strAllocatedTo = jsonObject1.getString("AllocatedTo");
                                                strFname = jsonObject1.getString("cCandidateFName");
                                                strLname = jsonObject1.getString("cCandidateLName");
                                                JSONObject jsonObject2 = jsonObject1.getJSONObject("dtDOB");
                                                strDob = jsonObject2.getString("date");
                                                String strGender = jsonObject1.getString("cGender");
                                                String strParentName = jsonObject1.getString("cParentName");
                                                String strPanNo = jsonObject1.getString("cPanNo");
                                                String strAdharNo = jsonObject1.getString("cAadhaarNo");
                                                String strPassportNo = jsonObject1.getString("cPassportNo");
                                                String strMobile = jsonObject1.getString("cMobile");
                                                String strParentNo = jsonObject1.getString("cParantNo");
                                                String strEmail = jsonObject1.getString("cEmail");
                                                String strPincode = jsonObject1.getString("cPinCode");
                                                String strAddress = jsonObject1.getString("cAddressLine");
                                                String strCity = jsonObject1.getString("cCity");
                                                String strState = jsonObject1.getString("cState");
                                                String strCreatedDt = jsonObject1.getString("dtCreatedDate");
                                                String strUpdatedDt = jsonObject1.getString("dtUpdatedDate");
                                                String strRemarks = jsonObject1.getString("cRemarks");

                                                DataMasterEntry dataMasterEntry = new DataMasterEntry(strFileId, strSrNo, strFname, strLname,
                                                        strDob, strGender, strParentName, strAddress, strCity, strState, strPincode, strMobile, strParentNo, strEmail, strRemarks, "Confirmed");
                                                arrayListMasterEntry.add(dataMasterEntry);
                                            }
                                            myAdapter = new AdapterMasterEntry(arrayListMasterEntry, MasterEntry.this);

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MasterEntry.this);
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                                            mRecyclerviewTabledetails.addItemDecoration(new DividerItemDecoration(MasterEntry.this, DividerItemDecoration.VERTICAL));
                                            mRecyclerviewTabledetails.setLayoutManager(linearLayoutManager);
                                            mRecyclerviewTabledetails.setAdapter(myAdapter);
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MasterEntry.this, "Errorcode-310 MasterEntry AllDetailsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")


                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(MasterEntry.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
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
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MasterEntry.this);
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
            Toast.makeText(MasterEntry.this,"Errorcode-309 MasterEntry getAllDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
}


