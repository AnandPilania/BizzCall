package com.bizcall.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Timer;
import java.util.TimerTask;

public class CounsellorData extends AppCompatActivity {
    ArrayList<DataCounselor> arraylistCounselor;
    RecyclerView recyclerViewCounselor;
    AdapterCounselorData adapterCounselorData;
    ProgressDialog dialog;
    UrlRequest urlRequest;
    String statusid, cid, status, total, datafrom, refname, srno, cname, course, parentno, mobile, email,strSNO;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView txtActivity, txtRef,txtNotFound,txtAsc,txtDesc,txtMin,txtMax,txtDisplayInfo,txtActivityName;
    ImageView imgBack;
    Spinner spinnerFilter;
    EditText edtSearchText;
    ImageView imgSearch;
    Long timeout;
    String strMin,strMax,url;
    RequestQueue requestQueue;
    String searchAs, searchtext, clientid,clienturl,selectedButton;
    ArrayList<String> arrayListSearchAs;
    AlertDialog alertDialog;
    RadioGroup radioGroup;
    RadioButton radioButton;
    LinearLayout linearOrder;
    Vibrator vibrator;
    Button btnNext,btnPrevious;
    int searchbool=0;
    String searchboolUrl="",searchbooltext="",searchboolAs="", orderboolAs="",strActivity;
    ImageView imgRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_counsellordate);
       // radioGroup=(RadioGroup)findViewById(R.id.radioGroupSearchSort);
        try {
            requestQueue=Volley.newRequestQueue(CounsellorData.this);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            imgRefresh=findViewById(R.id.imgRefresh);
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
            txtMin=findViewById(R.id.txtMin);
            txtMax=findViewById(R.id.txtMax);
            txtDisplayInfo=findViewById(R.id.txtDisplayInfo);
            btnNext=findViewById(R.id.btnLoadMore);
            btnPrevious=findViewById(R.id.btnLoadPrevious);
            txtActivityName=findViewById(R.id.txtActivityName);
            arrayListSearchAs = new ArrayList<>();
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
            dialog = ProgressDialog.show(CounsellorData.this, "", "Loading counselor data...", true);
            strActivity=getIntent().getStringExtra("Activity");
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(CounsellorData.this,CounsellorData.class);
                    intent.putExtra("Activity",strActivity);
                    startActivity(intent);
                }
            });

            if(strActivity.contains("OnlineLead"))
            {
                txtActivityName.setText("Online Lead");
            }
            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            timeout=sp.getLong("TimeOut",0);
            clienturl=sp.getString("ClientUrl",null);
            clientid = sp.getString("ClientId", null);
            cid = sp.getString("Id", null);
            statusid = sp.getString("SStatusId", null);
            status = sp.getString("SCStatus", null);
            total = sp.getString("Count", null);
          //  txtActivity.setText("(" + status + " " + total + ")");
                txtActivity.setText("(" + status + ")");

                cid = cid.replace(" ", "");
            datafrom = sp.getString("DtaFrom", null);
            refname = sp.getString("DataRefName", null);
            Log.d("IDD:", statusid + "" + cid);
            Log.d("DtaFrom", datafrom);
            Log.d("RefName", refname);
            txtRef.setText("(" + refname + ")"+datafrom);

            strMin="1";
            strMax="25";
                txtMin.setText(strMin);
                txtMax.setText(strMax);
                txtDisplayInfo.setText("Displaying "+txtMin.getText().toString()+"-"+txtMax.getText().toString()+" out of"+total);

            if (refname.contains("All"))
            {
                getCounselor(cid, statusid, "nSrNo", strMin, strMax);
            } else {
                getCounselor1(cid, statusid, datafrom, strMin, strMax);
            }

                arrayListSearchAs = new ArrayList<>();
                arrayListSearchAs.add("Serial No");
                arrayListSearchAs.add("Candidate Name");
                arrayListSearchAs.add("Course");
                arrayListSearchAs.add("Mobile");
                arrayListSearchAs.add("Email");
                arrayListSearchAs.add("Parent No");
                arrayListSearchAs.add("Allocation Date");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(CounsellorData.this, R.layout.spinner_item1, arrayListSearchAs);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFilter.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            spinnerFilter.setSelection(0);

            txtDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchDescClick();
                }
            });
            txtAsc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   searchAscClick();
                }
            });
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  btnNextClick();
                }
            });
            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnPreviousClick();
                }
            });

            spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                           searchAsItemSelected();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
            }catch (Exception e)
            {
                dialog.dismiss();
                Toast.makeText(CounsellorData.this,"Volley error while loading counselor data",Toast.LENGTH_SHORT).show();
                Log.d("CounselorDataException", String.valueOf(e));
            }
    }//oncreate close
    public void searchAsItemSelected()
    {
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
    }
    public void btnPreviousClick()
    {

        strMin= String.valueOf(Integer.parseInt(txtMin.getText().toString())-25);
        strMax= String.valueOf(Integer.parseInt(txtMax.getText().toString())-25);
        txtMin.setText(strMin);
        txtMax.setText(strMax);

        txtDisplayInfo.setText("Displaying "+txtMin.getText().toString()+"-"+txtMax.getText().toString()+" out of"+total);
        if(searchbool==1)
        {
            dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
            searchDataForAll(searchbooltext,searchboolAs, orderboolAs,txtMin.getText().toString(),txtMax.getText().toString());
            //  refreshWhenLoading();
        }
        else if(searchbool==2)
        {
            dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
            searchData(searchbooltext,searchboolAs, orderboolAs,txtMin.getText().toString(),txtMax.getText().toString());

        }
        else {

            dialog = ProgressDialog.show(CounsellorData.this, "", "Loading counselor data", true);

            if (refname.contains("All")) {
                getCounselor(cid, statusid,"nSrNo",strMin,strMax);
            } else {
                getCounselor1(cid, statusid, datafrom,strMin,strMax);
            }
        }
    }
    public void btnNextClick()
    {
        strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) + 25);
        strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) + 25);
        txtMin.setText(strMin);
        txtMax.setText(strMax);
        if(searchbool==1)
        {
            dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
            searchDataForAll(searchbooltext,searchboolAs, orderboolAs,txtMin.getText().toString(),txtMax.getText().toString());
            //  refreshWhenLoading();
        }
        else if(searchbool==2)
        {
            dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
            searchData(searchbooltext,searchboolAs, orderboolAs,txtMin.getText().toString(),txtMax.getText().toString());
            //refreshWhenLoading();
        }
        else {
            dialog = ProgressDialog.show(CounsellorData.this, "", "Loading counselor data", true);
            if (refname.contains("All")) {
                getCounselor(cid, statusid,"nSrNo",strMin,strMax);
            } else {
                getCounselor1(cid, statusid, datafrom,strMin,strMax);
            }
            //  refreshWhenLoading();
            // dialog = ProgressDialog.show(CounsellorData.this, "", "Loading", true);
            // getCounselor(cid, statusid, "nSrNo", strMin, strMax);
        }
        txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString() + " out of" + total);

    }

    public void searchAscClick()
    {
        txtMin.setText("1");
        txtMax.setText("25");
        txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString() + " out of" + total);

        searchAs = spinnerFilter.getSelectedItem().toString();
        searchtext=edtSearchText.getText().toString().trim();
        searchtext=searchtext.replaceAll("'","").trim();
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

        // searchCondition="and "+searchAs+" like '%"+searchtext+"%' ";
        if(searchtext==""||searchtext==null)
        {
            edtSearchText.setError("Please insert text");
        }
        else {

            if (refname.contains("All"))
            {
                strMin=txtMin.getText().toString();
                strMax=txtMax.getText().toString();
                // searchCondition = "and " + searchAs + " like '%" + searchtext + "%' ";
                // dialog=ProgressDialog.show(CounsellorData.this,"","Loading",true);
                searchbool=1;
                searchbooltext=searchtext;
                searchboolAs=searchAs;
                orderboolAs=searchAs + " ASC";
                dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                searchDataForAll(searchtext,searchAs, searchAs + " ASC",strMin,strMax);
                // refreshWhenLoading();
                //getCounselor(cid, statusid,searchAs+" "+"DESC");
            } else {
                strMin=txtMin.getText().toString();
                strMax=txtMax.getText().toString();
                //  dialog=ProgressDialog.show(CounsellorData.this,"","Loading",true);
                searchbool=2;
                searchbooltext=searchtext;
                searchboolAs=searchAs;
                orderboolAs=searchAs + " ASC";
                dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                searchData(searchtext,searchAs, searchAs + " ASC",strMin,strMax);
                // refreshWhenLoading();
                // getCounselor1(cid, statusid, datafrom);
            }
        }
    }

    public void searchDescClick()
    {
        txtMin.setText("1");
        txtMax.setText("25");
        txtDisplayInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString() + " out of" + total);

        searchAs = spinnerFilter.getSelectedItem().toString();
        searchtext = edtSearchText.getText().toString().trim();
        searchtext=searchtext.replaceAll("'","").trim();
        Log.d("Searchtext",searchtext);
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
              /*  String searchCondition="and "+searchAs+" like '%"+searchtext+"%' ";
                Log.d("SearchText",searchtext);*/
        if(searchtext==""||searchtext==null)
        {
            edtSearchText.setError("Please insert text");

        }
        else {
            if (refname.contains("All"))
            {
                strMin=txtMin.getText().toString();
                strMax=txtMax.getText().toString();
                // searchCondition = "and " + searchAs + " like '%" + searchtext + "%' ";
                // dialog=ProgressDialog.show(CounsellorData.this,"","Loading",true);
                searchbool=1;
                searchbooltext=searchtext;
                searchboolAs=searchAs;
                orderboolAs=searchAs + " DESC";
                dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                searchDataForAll(searchtext,searchAs, searchAs + " DESC",strMin,strMax);

            } else {
                strMin=txtMin.getText().toString();
                strMax=txtMax.getText().toString();
                //  dialog=ProgressDialog.show(CounsellorData.this,"","Loading",true);
                searchbool=2;
                searchbooltext=searchtext;
                searchboolAs=searchAs;
                orderboolAs=searchAs + " DESC";
                dialog = ProgressDialog.show(CounsellorData.this, "", "Searching data", true);
                searchData(searchtext,searchAs, searchAs + " DESC",strMin,strMax);
                //refreshWhenLoading();
                // getCounselor1(cid, statusid, datafrom);
            }
        }
    }

    public void refreshWhenLoading()
    {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if(dialog.isShowing()) {
                    Intent intent = new Intent(CounsellorData.this, CounsellorData.class);
                    intent.putExtra("Activity",strActivity);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(CounsellorData.this, Home.class);
        intent.putExtra("Activity","CounsellorData");
        intent.putExtra("RefName",refname);
        intent.putExtra("DataFrom",datafrom);
        Log.d("RefName***",refname);
        startActivity(intent);
        finish();

        super.onBackPressed();
        }catch (Exception e)
        {
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void getCounselor(String cid, String sid, String search1, final String strMin, String strMax) {
        //dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);
        arraylistCounselor = new ArrayList<>();
        url=clienturl+"?clientid=" + clientid + "&caseid=101&CounselorId=" + cid + "&statusid=" + sid+"&Sortby="+search1+"&MinVal="+strMin+"&MaxVal="+strMax;
        if(CheckInternet.checkInternet(CounsellorData.this))
        {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                strSNO=jsonObject1.getString("SNO");
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
                                DataCounselor dataCounselor = new DataCounselor(strSNO,srno, cname, course, mobile, parentno, email, allocationDate, adrs, city, state, pincode,statusId,statusStr,remarks);
                                arraylistCounselor.add(dataCounselor);
                            }

                            adapterCounselorData = new AdapterCounselorData(CounsellorData.this, arraylistCounselor);
                            recyclerViewCounselor = findViewById(R.id.recyclerCounselorData);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerViewCounselor.setLayoutManager(layoutManager);
                            recyclerViewCounselor.setAdapter(adapterCounselorData);
                            adapterCounselorData.notifyDataSetChanged();
                            int mval= Integer.parseInt(txtMax.getText().toString());
                            if(txtMin.getText().toString().equals("1")) {
                                btnPrevious.setVisibility(View.GONE);
                                if (Integer.parseInt(txtMax.getText().toString()) <= Integer.parseInt(total)) {
                                    btnNext.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    btnNext.setVisibility(View.GONE);
                                }
                            }
                            else if(mval>Integer.parseInt(total))
                            {
                                btnPrevious.setVisibility(View.VISIBLE);
                                btnNext.setVisibility(View.GONE);
                            }
                            else
                            {
                                btnNext.setVisibility(View.VISIBLE);
                                btnPrevious.setVisibility(View.VISIBLE);
                            }

                            //Log.d("Size**", String.valueOf(arrayList.size()));
                        } catch (JSONException e) {
                            Toast.makeText(CounsellorData.this,"Volley error while loading all counselor data ",Toast.LENGTH_SHORT).show();
                            Log.d("AllCounselorException", String.valueOf(e));
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
                        if (error.networkResponse != null||error instanceof TimeoutError ||error instanceof NoConnectionError ||error instanceof AuthFailureError ||error instanceof ServerError ||error instanceof NetworkError ||error instanceof ParseError) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
                            alertDialogBuilder.setTitle("Server Error!!!")


                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).show();
                            //   dialog.dismiss();
                            Toast.makeText(CounsellorData.this,"Server Error",Toast.LENGTH_SHORT).show();
                            // showCustomPopupMenu();
                            Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                        }

                    }
                });
        requestQueue.add(stringRequest);
        }else
        {
            dialog.dismiss();
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
    }

    public void getCounselor1(String cid, String sid, String datafrom1, final String strMin, String strMax) {
        //dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);
        url=clienturl+"?clientid=" + clientid + "&caseid=102&CounselorId=" + cid + "&statusid=" + sid + "&DataFrom=" + datafrom1+"&MinVal="+strMin+"&MaxVal="+strMax;
        Log.d("CounselorUrl",url);
        arraylistCounselor = new ArrayList<>();
       if(CheckInternet.checkInternet(CounsellorData.this))
       {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dialog.dismiss();

                        Log.d("CounselorResponse", response);
                        try {
                            arraylistCounselor.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            // Log.d("Json",jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                strSNO=jsonObject1.getString("SNO");
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
                                DataCounselor dataCounselor = new DataCounselor(strSNO,srno, cname, course, mobile, parentno, email, allocationdate1, adrs, city, state, pincode,statusId,statusStr,remarks);
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
                            recyclerViewCounselor.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("SelectedRecycler:", String.valueOf(recyclerViewCounselor.getChildAdapterPosition(v)));
                                }
                            });
                            int mval= Integer.parseInt(txtMax.getText().toString());
                            if(txtMin.getText().toString().equals("1")) {
                                btnPrevious.setVisibility(View.GONE);
                                if (Integer.parseInt(txtMax.getText().toString()) <=arraylistCounselor.size()) {
                                    btnNext.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    btnNext.setVisibility(View.GONE);
                                }
                            }
                            else if(mval>arraylistCounselor.size())
                            {
                                btnPrevious.setVisibility(View.VISIBLE);
                                btnNext.setVisibility(View.GONE);
                            }
                            else
                            {
                                btnNext.setVisibility(View.VISIBLE);
                                btnPrevious.setVisibility(View.VISIBLE);
                            }
                            //Log.d("Size**", String.valueOf(arrayList.size()));
                        } catch (JSONException e) {
                            dialog.dismiss();
                            Toast.makeText(CounsellorData.this,"Volley error while loading counselor data Refwise",Toast.LENGTH_SHORT).show();
                            Log.d("CRefwiseException", String.valueOf(e));
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
                        if (error.networkResponse != null||error instanceof TimeoutError ||error instanceof NoConnectionError ||error instanceof AuthFailureError ||error instanceof ServerError ||error instanceof NetworkError ||error instanceof ParseError) {
                            //responsecode1="ServerError";
                              dialog.dismiss();
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
                            alertDialogBuilder.setTitle("Server Error!!!")


                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).show();
                            Toast.makeText(CounsellorData.this,"Server Error",Toast.LENGTH_SHORT).show();
                            // showCustomPopupMenu();
                            Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                        }

                    }
                });
        requestQueue.add(stringRequest);
       }else
       {
           dialog.dismiss();
           android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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
    }
    public void searchData(String searchVal,String searchAs,String orderval,String strMin,String strMax) {
       // refname = refname.replaceAll(" ", "");
        //String url=clienturl+"?clientid="+clientid+"&caseid=24&cDataFrom="+datafrom+"&AllocatedTo="+cid+"&CurrentStatus="+statusid+"&FieldName="+searchAs+"&FieldVal="+searchVal+"&OrderVal="+orderval+"";
        String url=clienturl+"?clientid="+clientid+"&caseid=103&cDataFrom="+datafrom+"&AllocatedTo="+cid+"&CurrentStatus="+statusid+"&FieldName="+searchAs+"&FieldVal="+searchVal+"&OrderVal="+orderval+"&MinVal="+strMin+"&MaxVal="+strMax;

        arraylistCounselor = new ArrayList<>();
        if(CheckInternet.checkInternet(CounsellorData.this))
        {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dialog.dismiss();

                        int mval=Integer.parseInt(txtMax.getText().toString());
                        if(response.contains("[]"))
                        {
                            Log.d("ABCD","ABCD");
                            txtNotFound.setVisibility(View.VISIBLE);

                            //btnNext.setVisibility(View.GONE);
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
                                strSNO=jsonObject1.getString("SNO");
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
                                DataCounselor dataCounselor = new DataCounselor(strSNO,srno, cname, course, mobile, parentno, email, allocationdate1, adrs, city, state, pincode,statusId,statusStr,remarks);
                                arraylistCounselor.add(dataCounselor);
                            }
                            adapterCounselorData = new AdapterCounselorData(CounsellorData.this, arraylistCounselor);
                            recyclerViewCounselor = findViewById(R.id.recyclerCounselorData);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerViewCounselor.setLayoutManager(layoutManager);
                            recyclerViewCounselor.setAdapter(adapterCounselorData);
                            adapterCounselorData.notifyDataSetChanged();

                            if(txtMin.getText().toString().equals("1")) {
                                btnPrevious.setVisibility(View.GONE);
                                btnNext.setVisibility(View.GONE);
                                if (Integer.parseInt(txtMax.getText().toString()) <= arraylistCounselor.size()) {
                                    btnNext.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    btnNext.setVisibility(View.GONE);
                                }
                            }
                            else if(mval>arraylistCounselor.size())
                            {
                                btnPrevious.setVisibility(View.VISIBLE);
                                btnNext.setVisibility(View.GONE);
                            }
                            else
                            {
                                btnNext.setVisibility(View.VISIBLE);
                                btnPrevious.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            dialog.dismiss();
                            Toast.makeText(CounsellorData.this,"Volley error while searching data",Toast.LENGTH_SHORT).show();
                            Log.d("SearchException", String.valueOf(e));
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
                        if (error.networkResponse != null||error instanceof TimeoutError ||error instanceof NoConnectionError ||error instanceof AuthFailureError ||error instanceof ServerError ||error instanceof NetworkError ||error instanceof ParseError) {
                            //responsecode1="ServerError";
                               dialog.dismiss();
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
                            alertDialogBuilder.setTitle("Server Error!!!")


                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).show();
                            Toast.makeText(CounsellorData.this,"Server Error",Toast.LENGTH_SHORT).show();
                            // showCustomPopupMenu();
                            Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                        }

                    }
                });
        requestQueue.add(stringRequest);
        }else
        {
            dialog.dismiss();
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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


    }
    public void searchDataForAll(String searchVal,String searchAs,String orderval,String strMin,String strMax) {
        // refname = refname.replaceAll(" ", "");
        //String url=clienturl+"?clientid="+clientid+"&caseid=25&AllocatedTo="+cid+"&CurrentStatus="+statusid+"&FieldName="+searchtext+" &OrderVal="+orderval+"";
        url=clienturl+"?clientid="+clientid+"&caseid=104&AllocatedTo="+cid+"&CurrentStatus="+statusid+"&FieldName="+searchAs+"&FieldVal="+searchVal+"&OrderVal="+orderval+"&MinVal="+strMin+"&MaxVal="+strMax;
            arraylistCounselor = new ArrayList<>();
        Log.d("SearchUrl", url);
        if(CheckInternet.checkInternet(CounsellorData.this))
        {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        int mval=Integer.parseInt(txtMax.getText().toString());
                        if(response.contains("[]"))
                        {
                            Log.d("ABCD","ABCD");
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
                                strSNO=jsonObject1.optString("SNO");
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
                                DataCounselor dataCounselor = new DataCounselor(strSNO,srno,cname, course, mobile, parentno, email, allocationdate1, adrs, city, state, pincode,statusId,statusStr,remarks);
                                arraylistCounselor.add(dataCounselor);
                            }
                            adapterCounselorData = new AdapterCounselorData(CounsellorData.this, arraylistCounselor);
                            recyclerViewCounselor = findViewById(R.id.recyclerCounselorData);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerViewCounselor.setLayoutManager(layoutManager);
                            recyclerViewCounselor.setAdapter(adapterCounselorData);
                            adapterCounselorData.notifyDataSetChanged();
                            // int mval= Integer.parseInt(txtMax.getText().toString());
                            if(txtMin.getText().toString().equals("1")) {
                                btnPrevious.setVisibility(View.GONE);
                                if (Integer.parseInt(txtMax.getText().toString()) <= arraylistCounselor.size()) {
                                    btnNext.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    btnNext.setVisibility(View.GONE);
                                }
                            }
                            else if(mval>arraylistCounselor.size())
                            {
                                btnPrevious.setVisibility(View.VISIBLE);
                                btnNext.setVisibility(View.GONE);
                            }
                            else
                            {
                                btnNext.setVisibility(View.VISIBLE);
                                btnPrevious.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            Toast.makeText(CounsellorData.this,"Got exception while searching all data",Toast.LENGTH_SHORT).show();
                            Log.d("SearchAllException", String.valueOf(e));
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
                        if (error.networkResponse != null||error instanceof TimeoutError ||error instanceof NoConnectionError ||error instanceof AuthFailureError ||error instanceof ServerError ||error instanceof NetworkError ||error instanceof ParseError) {
                            //responsecode1="ServerError";

                              dialog.dismiss();
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
                            alertDialogBuilder.setTitle("Server Error!!!")


                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).show();
                            Toast.makeText(CounsellorData.this,"Server Error",Toast.LENGTH_SHORT).show();
                            // showCustomPopupMenu();
                            Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                        }

                    }
                });
        requestQueue.add(stringRequest);
        }else
        {
            dialog.dismiss();
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CounsellorData.this);
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


    }
}

