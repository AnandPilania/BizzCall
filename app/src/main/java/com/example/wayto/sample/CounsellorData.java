package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CounsellorData extends AppCompatActivity {
    ArrayList<DataCounselor> arraylistCounselor;
    RecyclerView recyclerViewCounselor;
    AdapterCounselorData adapterCounselorData;
    ProgressDialog dialog;
    UrlRequest urlRequest;
    String statusid, cid, status, total, datafrom, refname, srno, cname, course, parentno, mobile, email;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView txtActivity, txtRef;
    ImageView imgBack;
    Spinner spinnerFilter;
    TextView edtSearchText;
    ImageView imgSearch;
    String searchAs, searchtext, clientid;
    ArrayList<String> arrayListSearchAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_counsellordate);
        txtActivity = findViewById(R.id.txtSlectedStatus);
        txtRef = findViewById(R.id.txtDataRef);
        imgBack = findViewById(R.id.img_back);
        spinnerFilter = findViewById(R.id.spinnerFilter1);
        edtSearchText = findViewById(R.id.edtSearchtext);
        imgSearch = findViewById(R.id.img_search);
        arrayListSearchAs = new ArrayList<>();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dialog = ProgressDialog.show(CounsellorData.this, "", "Loading...", true);

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        clientid = sp.getString("ClientId", null);
        cid = sp.getString("Id", null);
        statusid = sp.getString("SStatusId", null);
        status = sp.getString("SCStatus", null);
        total = sp.getString("Count", null);
        txtActivity.setText("(" + status + " " + total + ")");
        cid = cid.replace(" ", "");
        datafrom = sp.getString("DtaFrom", null);
        refname = sp.getString("DataRefName", null);
        Log.d("IDD:", statusid + "" + cid);
        Log.d("DtaFrom", datafrom);
        Log.d("RefName", refname);
        txtRef.setText("(" + refname + ")");
        // getCounselor(cid, statusid);
        if (refname.contains("All")) {
            getCounselor(cid, statusid);
        } else {
            getCounselor1(cid, statusid, datafrom);
        }
        arrayListSearchAs.add("Serial No");
        arrayListSearchAs.add("Candidate Name");
        arrayListSearchAs.add("cCourse");
        arrayListSearchAs.add("Mobile");
        arrayListSearchAs.add("Email");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(CounsellorData.this, R.layout.spinner_item1, arrayListSearchAs);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchtext = edtSearchText.getText().toString();
                Log.d("SearchText", searchtext);
                if (!searchtext.isEmpty()) {
                    status = status.replaceAll(" ", "");
                    searchAs = spinnerFilter.getSelectedItem().toString();
                    searchData();
                } else {
                    edtSearchText.setError("Please enter text");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CounsellorData.this, Home.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public void getCounselor(String cid, String sid) {
        //dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);

        arraylistCounselor = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=3&CounselorId=" + cid + "&statusid=" + sid);
        Log.d("CounselorUrl", "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=3&CounselorId=" + cid + "&statusid=" + sid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                //  TableLayout tableLayout=findViewById(R.id.tblCounselorData);
                //  tableLayout.setVisibility(View.VISIBLE);

                Log.d("CounselorResponse", response);
                try {
                    arraylistCounselor.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String srno = jsonObject1.getString("nSrNo");
                        String cname = jsonObject1.getString("cCandidateName");
                        String course = jsonObject1.getString("cCourse");
                        String mobile = jsonObject1.getString("cMobile");
                        String parentno = jsonObject1.getString("cParantNo");
                        String email = jsonObject1.getString("cEmail");
                        String allocationDate = jsonObject1.getString("AllocationDate");
                        String adrs = jsonObject1.getString("cAddressLine");
                        String city = jsonObject1.getString("cCity");
                        String state = jsonObject1.getString("cState");
                        String pincode = jsonObject1.getString("cPinCode");
                        //String datafrom=jsonObject1.getString("cDataFrom");

                        Log.d("Status11", srno);
                        DataCounselor dataCounselor = new DataCounselor(srno, cname, course, mobile, parentno, email, allocationDate, adrs, city, state, pincode);
                        arraylistCounselor.add(dataCounselor);
                    }

                    adapterCounselorData = new AdapterCounselorData(CounsellorData.this, arraylistCounselor);
                    recyclerViewCounselor = findViewById(R.id.recyclerCounselorData);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewCounselor.setLayoutManager(layoutManager);
                    recyclerViewCounselor.setAdapter(adapterCounselorData);
                    adapterCounselorData.notifyDataSetChanged();
                    /*recyclerViewCounselor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Log.d("SelectedRecycler:", String.valueOf(recyclerViewCounselor.getChildAdapterPosition(v)));
                        }
                    });*/
                    //Log.d("Size**", String.valueOf(arrayList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getCounselor1(String cid, String sid, String datafrom1) {
        //dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);

        arraylistCounselor = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=10&CounselorId=" + cid + "&statusid=" + sid + "&DataFrom=" + datafrom1);
        Log.d("CounselorUrl1", "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=10&CounselorId=" + cid + "&statusid=" + sid + "&DataFrom=" + datafrom1);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                //  TableLayout tableLayout=findViewById(R.id.tblCounselorData);
                //  tableLayout.setVisibility(View.VISIBLE);

                Log.d("CounselorResponse", response);
                try {
                    arraylistCounselor.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        srno = jsonObject1.getString("nSrNo");
                        cname = jsonObject1.getString("cCandidateName");
                        course = jsonObject1.getString("cCourse");
                        parentno = jsonObject1.getString("cParantNo");
                        mobile = jsonObject1.getString("cMobile");
                        email = jsonObject1.getString("cEmail");
                        String allocationdate1 = jsonObject1.getString("AllocationDate");

                        String adrs = jsonObject1.getString("cAddressLine");
                        String city = jsonObject1.getString("cCity");
                        String state = jsonObject1.getString("cState");
                        String pincode = jsonObject1.getString("cPinCode");
                        //String datafrom=jsonObject1.getString("cDataFrom");

                        Log.d("Status11", srno);
                        DataCounselor dataCounselor = new DataCounselor(srno, cname, course, mobile, parentno, email, allocationdate1, adrs, city, state, pincode);
                        arraylistCounselor.add(dataCounselor);
                    }
                    editor.putString("SrNo", srno);
                    editor.putString("MobileNo1", mobile);
                    editor.commit();

                    adapterCounselorData = new AdapterCounselorData(CounsellorData.this, arraylistCounselor);
                    recyclerViewCounselor = findViewById(R.id.recyclerCounselorData);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewCounselor.setLayoutManager(layoutManager);
                    recyclerViewCounselor.setAdapter(adapterCounselorData);
                    adapterCounselorData.notifyDataSetChanged();
                    /*recyclerViewCounselor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Log.d("SelectedRecycler:", String.valueOf(recyclerViewCounselor.getChildAdapterPosition(v)));
                        }
                    });*/
                    //Log.d("Size**", String.valueOf(arrayList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void searchData() {
        refname = refname.replaceAll(" ", "");
        dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);

        arraylistCounselor = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/SearchCounselorData.php?SearchAs=" + searchAs + "&SearchFor=" + searchtext + "&refname=" + datafrom + "&CurrentStatus=" + status + "&CounselorId=" +
                cid);
        Log.d("SearchUrl", "http://anilsahasrabuddhe.in/CRM/AnDe828500/SearchCounselorData.php?SearchAs=" + searchAs + "&SearchFor=" + searchtext + "&DataFrom=" + refname + "&CurrentStatus=" + status + "&CounselorId=" +
                cid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                Log.d("SearchResponse", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String srno = jsonObject1.getString("nSrNo");
                        String cname = jsonObject1.getString("cCandidateName");
                        String course = jsonObject1.getString("cCourse");
                        String parentno = jsonObject1.getString("cParentMobileNo");
                        String mobile = jsonObject1.getString("cMobile");
                        String email = jsonObject1.getString("cEmail");
                        String allocationdate1 = jsonObject1.getString("AllocationDate");
                        String adrs = jsonObject1.getString("cAddressLine");
                        String city = jsonObject1.getString("cCity");
                        String state = jsonObject1.getString("cState");
                        String pincode = jsonObject1.getString("cPinCode");
                        //String datafrom=jsonObject1.getString("cDataFrom");

                        Log.d("Status11", srno);
                        DataCounselor dataCounselor = new DataCounselor(srno, cname, course, mobile, parentno, email, allocationdate1, adrs, city, state, pincode);
                        arraylistCounselor.add(dataCounselor);
                    }
                    adapterCounselorData = new AdapterCounselorData(CounsellorData.this, arraylistCounselor);
                    recyclerViewCounselor = findViewById(R.id.recyclerCounselorData);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewCounselor.setLayoutManager(layoutManager);
                    recyclerViewCounselor.setAdapter(adapterCounselorData);
                    adapterCounselorData.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

