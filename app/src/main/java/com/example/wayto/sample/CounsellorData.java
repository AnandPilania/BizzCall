package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    TextView txtActivity, txtRef,txtNotFound,txtAsc,txtDesc;
    ImageView imgBack;
    Spinner spinnerFilter;
    EditText edtSearchText;
    ImageView imgSearch;
    Long timeout;
    String searchAs, searchtext, clientid,clienturl,selectedButton;
    ArrayList<String> arrayListSearchAs;
    AlertDialog alertDialog;
    RadioGroup radioGroup;
    RadioButton radioButton;
    LinearLayout linearOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_counsellordate);
       // radioGroup=(RadioGroup)findViewById(R.id.radioGroupSearchSort);
        txtAsc=findViewById(R.id.txtAsc);
        txtDesc=findViewById(R.id.txtDesc);
        txtActivity = findViewById(R.id.txtSlectedStatus);
        txtRef = findViewById(R.id.txtDataRef);
        imgBack = findViewById(R.id.img_back);
        spinnerFilter = findViewById(R.id.spinnerFilter1);
        edtSearchText = findViewById(R.id.edtSearchtext);
        imgSearch = findViewById(R.id.img_search);
        txtNotFound=findViewById(R.id.txtNotFound1);
        linearOrder=findViewById(R.id.linearOrder);
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
        timeout=sp.getLong("TimeOut",0);
        clienturl=sp.getString("ClientUrl",null);
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
            getCounselor(cid, statusid,"nSrNo");
        } else {
            getCounselor1(cid, statusid, datafrom);
        }
       // int selectedId=radioGroup.getCheckedRadioButtonId();
       // radioButton=(RadioButton)findViewById(selectedId);
        //  selectedButton=radioButton.getText().toString();
      //  Log.d("RadioButton", String.valueOf(selectedId));
       /* if(selectedButton.contains("Sort"))
        {*/
            arrayListSearchAs = new ArrayList<>();
            arrayListSearchAs.add("Serial No");
            arrayListSearchAs.add("Candidate Name");
            arrayListSearchAs.add("Course");
            arrayListSearchAs.add("Mobile");
            arrayListSearchAs.add("Email");
            arrayListSearchAs.add("Parent No");
            arrayListSearchAs.add("Allocation Date");
           /* edtSearchText.setVisibility(View.GONE);
            imgSearch.setVisibility(View.GONE);
            linearOrder.setVisibility(View.VISIBLE);*/

       /* radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {*/
               /* int selectedId=radioGroup.getCheckedRadioButtonId();
                radioButton=(RadioButton)findViewById(selectedId);
                selectedButton=radioButton.getText().toString();
                Log.d("RadioButton", String.valueOf(selectedId));
                if(selectedButton.contains("Sort"))
                {*/
                   /* arrayListSearchAs=new ArrayList<>();
                    arrayListSearchAs = new ArrayList<>();
                    arrayListSearchAs.add("Serial No");
                    arrayListSearchAs.add("Candidate Name");
                    arrayListSearchAs.add("Course");
                    arrayListSearchAs.add("Mobile");
                    arrayListSearchAs.add("Email");
                    arrayListSearchAs.add("Parent No");
                    arrayListSearchAs.add("Allocation Date");
                    edtSearchText.setVisibility(View.GONE);
                    imgSearch.setVisibility(View.GONE);
                    linearOrder.setVisibility(View.VISIBLE);*/
              //  }
             /*   else
                {
                    arrayListSearchAs=new ArrayList<>();
                    arrayListSearchAs.add("Serial No");
                    arrayListSearchAs.add("Candidate Name");
                    arrayListSearchAs.add("Course");
                    arrayListSearchAs.add("Mobile");
                    arrayListSearchAs.add("Email");
                    arrayListSearchAs.add("Parent No");
                    edtSearchText.setVisibility(View.VISIBLE);
                    imgSearch.setVisibility(View.VISIBLE);
                    linearOrder.setVisibility(View.GONE);*/



        //});


        new CountDownTimer(timeout, 1000) {

            public void onTick(long millisUntilFinished) {
                //mtextView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                    // mtextView.setText("done!");
                    LayoutInflater li = LayoutInflater.from(getApplicationContext());
                    //Creating a view to get the dialog box
                    View confirmCall = li.inflate(R.layout.layout_popup_slowinternet, null);
                    TextView txtOk = (TextView) confirmCall.findViewById(R.id.txtOkSlowPopoup);

                    AlertDialog.Builder alert = new AlertDialog.Builder(CounsellorData.this);
                    //Adding our dialog box to the view of alert dialog
                    alert.setView(confirmCall);
                    //Creating an alert dialog
                    alertDialog = alert.create();
                    alertDialog.show();

                    txtOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            editor = sp.edit();
                            editor.putString("Name", null);
                            // editor.putString("Id",null);
                            editor.commit();
                            finish();
                            startActivity(new Intent(CounsellorData.this,Login.class));

                        }
                    });
                    }
            }
        }.start();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(CounsellorData.this, R.layout.spinner_item1, arrayListSearchAs);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        spinnerFilter.setSelection(0);

        txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAs = spinnerFilter.getSelectedItem().toString();
                searchtext = edtSearchText.getText().toString();
                if(searchAs.contains("Serial No"))
                {
                    searchAs="nSrNo";
                }
                else if (searchAs.contains("Candidate Name"))
                {
                    searchAs="cCandidateName";
                }
                else if(searchAs.contains("Course"))
                {
                    searchAs="cCourse";
                }
                else if(searchAs.contains("Mobile"))
                {
                    searchAs="cMobile";
                }
                else if(searchAs.contains("Email"))
                {
                    searchAs="cEmail ";
                }
                else if(searchAs.contains("Parent No"))
                {
                    searchAs="cParantNo ";
                }
                else if(searchAs.contains("Allocation Date"))
                {
                    searchAs="AllocationDate";
                }
                String searchCondition="and "+searchAs+" like '%"+searchtext+"%' ";
                Log.d("SearchText",searchtext);
                if(searchtext==""||searchtext==null)
                {
                    if (refname.contains("All")) {
                        searchDataForAll("", searchAs + " DESC");
                        //getCounselor(cid, statusid,searchAs+" "+"DESC");
                    } else {
                        searchData("", searchAs + " DESC");
                        // getCounselor1(cid, statusid, datafrom);
                    }
                }
                else {
                    if (refname.contains("All")) {
                        searchDataForAll(searchCondition, searchAs + " DESC");
                        //getCounselor(cid, statusid,searchAs+" "+"DESC");
                    } else {
                        searchData(searchCondition, searchAs + " DESC");
                        // getCounselor1(cid, statusid, datafrom);
                    }
                }
            }
        });
        txtAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAs = spinnerFilter.getSelectedItem().toString();
                searchtext=edtSearchText.getText().toString();
                if(searchAs.contains("Serial No"))
                {
                    searchAs="nSrNo";
                }
                else if (searchAs.contains("Candidate Name"))
                {
                    searchAs="cCandidateName";
                }
                else if(searchAs.contains("Course"))
                {
                    searchAs="cCourse";
                }
                else if(searchAs.contains("Mobile"))
                {
                    searchAs="cMobile";
                }
                else if(searchAs.contains("Email"))
                {
                    searchAs="cEmail ";
                }
                else if(searchAs.contains("Parent No"))
                {
                    searchAs="cParantNo ";
                }
                else if(searchAs.contains("Allocation Date"))
                {
                    searchAs="AllocationDate";
                }

                String searchCondition="and "+searchAs+" like '%"+searchtext+"%' ";
                    if(searchtext==""||searchtext==null)
                    {
                        if (refname.contains("All")) {
                            searchDataForAll("", searchAs + " ASC");
                            //getCounselor(cid, statusid,searchAs+" "+"DESC");
                        } else {
                            searchData("", searchAs + " ASC");
                            // getCounselor1(cid, statusid, datafrom);
                        }
                    }
                    else {
                        if (refname.contains("All")){

                            searchDataForAll(searchCondition, searchAs + " ASC");
                            //getCounselor(cid, statusid,searchAs+" "+"DESC");
                        } else {
                            searchData(searchCondition, searchAs + " ASC");
                            // getCounselor1(cid, statusid, datafrom);
                        }
                    }
            }
        });

      //  searchAs = spinnerFilter.getSelectedItem().toString();
        //Log.d("SearchAs",searchAs);

        /*if (refname.contains("All")) {
            getCounselor(cid, statusid,searchAs);
        } else {
            getCounselor1(cid, statusid, datafrom);
        }*/

                spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        searchAs = spinnerFilter.getSelectedItem().toString();
                        if(searchAs.contains("Serial No"))
                        {
                            searchAs="nSrNo";
                        }
                        else if (searchAs.contains("Candidate Name"))
                        {
                            searchAs="cCandidateName";
                        }
                        else if(searchAs.contains("Course"))
                        {
                            searchAs="cCourse";
                        }
                        else if(searchAs.contains("Mobile"))
                        {
                            searchAs="cMobile";
                        }
                        else if(searchAs.contains("Email"))
                        {
                            searchAs="cEmail ";
                        }
                        else if(searchAs.contains("Parent No"))
                        {
                            searchAs="cParantNo ";
                        }
                        else if(searchAs.contains("Allocation Date"))
                        {
                            searchAs="AllocationDate";
                        }

                       /* if (refname.contains("All")) {
                            getCounselor(cid, statusid,searchAs);
                        } else {
                            getCounselor1(cid, statusid, datafrom);
                        }*/
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

      /*  imgSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                searchtext = edtSearchText.getText().toString();
               *//* try {
                    searchtext = URLEncoder.encode(searchtext, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*//*
                Log.d("SearchText", searchtext);
                *//*if (!searchtext.isEmpty()) {*//*
                    status = status.replaceAll(" ", "");
                    searchAs = spinnerFilter.getSelectedItem().toString();
                    if(searchAs.contains("Serial No"))
                    {
                        searchAs="nSrNo";
                    }
                    else if (searchAs.contains("Candidate Name"))
                    {
                        searchAs="cCandidateName";
                    }
                    else if(searchAs.contains("Course"))
                    {
                        searchAs="cCourse";
                    }
                    else if(searchAs.contains("Mobile"))
                    {
                        searchAs="cMobile";
                    }
                    else if(searchAs.contains("Email"))
                    {
                        searchAs="cEmail ";
                    }
                    else if(searchAs.contains("Parent No"))
                    {
                        searchAs="cParantNo ";
                    }
                    else if(searchAs.contains("Allocation Date"))
                    {
                        searchAs="AllocationDate";
                    }

                    if(refname.contains("All"))
                    {
                        searchDataForAll();
                    }
                    else
                    {
                        searchData();
                    }

               *//* } else {
                    edtSearchText.setError("Please enter text");
                }*//*
            }
        });*/
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CounsellorData.this, Home.class);
        startActivity(intent);
        super.onBackPressed();
    }

          /* if (refname.contains("All")) {
               getCounselor(cid, statusid, searchAs);
           } else {
               getCounselor1(cid, statusid, datafrom);
           }*/




    public void getCounselor(String cid, String sid,String search1) {
        //dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);

        arraylistCounselor = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=3&CounselorId=" + cid + "&statusid=" + sid+"&Sortby="+search1);
        Log.d("CounselorUrl", clienturl+"?clientid=" + clientid + "&caseid=3&CounselorId=" + cid + "&statusid=" + sid+"&Sortby="+search1);
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
                        String statusId=jsonObject1.getString("nStatusID");
                        String statusStr=jsonObject1.getString("cStatus");
                        String remarks=jsonObject1.getString("cRemarks");
                        //String datafrom=jsonObject1.getString("cDataFrom");

                        Log.d("Status11", srno);
                        DataCounselor dataCounselor = new DataCounselor(srno, cname, course, mobile, parentno, email, allocationDate, adrs, city, state, pincode,statusId,statusStr,remarks);
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
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=10&CounselorId=" + cid + "&statusid=" + sid + "&DataFrom=" + datafrom1);
        Log.d("CounselorUrl1", clienturl+"?clientid=" + clientid + "&caseid=10&CounselorId=" + cid + "&statusid=" + sid + "&DataFrom=" + datafrom1);
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
                        String statusId=jsonObject1.getString("nStatusID");
                        String statusStr=jsonObject1.getString("cStatus");
                        String remarks=jsonObject1.getString("cRemarks");
                        //String datafrom=jsonObject1.getString("cDataFrom");

                        Log.d("Status11", srno);
                        DataCounselor dataCounselor = new DataCounselor(srno, cname, course, mobile, parentno, email, allocationdate1, adrs, city, state, pincode,statusId,statusStr,remarks);
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

    public void searchData(String searchtext,String orderval) {
       // refname = refname.replaceAll(" ", "");
        String url=clienturl+"?clientid="+clientid+"&caseid=24&cDataFrom="+datafrom+"&AllocatedTo="+cid+"&CurrentStatus="+statusid+"&FieldName="+searchtext+" &OrderVal="+orderval+"";
        dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);

        arraylistCounselor = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(url);
        Log.d("SearchSpecUrl", url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if(response.contains("[]"))
                {
                    txtNotFound.setVisibility(View.VISIBLE);

                }
                else
                {
                    txtNotFound.setVisibility(View.GONE);
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
                        String parentno = jsonObject1.getString("cParantNo");
                        String mobile = jsonObject1.getString("cMobile");
                        String email = jsonObject1.getString("cEmail");
                        String allocationdate1 = jsonObject1.getString("AllocationDate");
                        String adrs = jsonObject1.getString("cAddressLine");
                        String city = jsonObject1.getString("cCity");
                        String state = jsonObject1.getString("cState");
                        String pincode = jsonObject1.getString("cPinCode");
                        String statusId=jsonObject1.getString("CurrentStatus");
                        String statusStr=jsonObject1.getString("cStatus");
                        String remarks=jsonObject1.getString("cRemarks");
                        //String datafrom=jsonObject1.getString("cDataFrom");

                        Log.d("Status11", srno);
                        DataCounselor dataCounselor = new DataCounselor(srno, cname, course, mobile, parentno, email, allocationdate1, adrs, city, state, pincode,statusId,statusStr,remarks);
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
    public void searchDataForAll(String searchtext,String orderval) {
        // refname = refname.replaceAll(" ", "");
        String url=clienturl+"?clientid="+clientid+"&caseid=25&AllocatedTo="+cid+"&CurrentStatus="+statusid+"&FieldName="+searchtext+" &OrderVal="+orderval+"";
                   // "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=AnDe828500&caseid=25&AllocatedTo=8&CurrentStatus=2&FieldName=cCandidateName&FieldValue=ahamd";
        dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);

        arraylistCounselor = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(url);
        Log.d("SearchUrl", url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException
            {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                if(response.contains("[]"))
                {
                    txtNotFound.setVisibility(View.VISIBLE);

                }
                else
                {
                    txtNotFound.setVisibility(View.GONE);
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
                        String parentno = jsonObject1.getString("cParantNo");
                        String mobile = jsonObject1.getString("cMobile");
                        String email = jsonObject1.getString("cEmail");
                        String allocationdate1 = jsonObject1.getString("AllocationDate");
                        String adrs = jsonObject1.getString("cAddressLine");
                        String city = jsonObject1.getString("cCity");
                        String state = jsonObject1.getString("cState");
                        String pincode = jsonObject1.getString("cPinCode");
                        String statusId=jsonObject1.getString("CurrentStatus");
                        String statusStr=jsonObject1.getString("cStatus");
                        String remarks=jsonObject1.getString("cRemarks");
                        //String datafrom=jsonObject1.getString("cDataFrom");

                        Log.d("Status11", statusStr);
                        DataCounselor dataCounselor = new DataCounselor(srno, cname, course, mobile, parentno, email, allocationdate1, adrs, city, state, pincode,statusId,statusStr,remarks);
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

