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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchAllActivity extends AppCompatActivity {

    EditText edtSearchText;
    ImageView imgSearch,imgBack;
    Spinner spinnerSearchAs;
    String searchText,searchAs,clientid,cid;
    ArrayList<String> arrayListSearchAs;
    AdapterSearchCounselorData adapterCounselorData;
    RecyclerView recyclerViewCounselor;
    ArrayList<DataCounselor> arraylistCounselor;
    UrlRequest urlRequest;
    SharedPreferences sp;
    ProgressDialog dialog;
    TextView txtNotFound;
    String clienturl,currentstatus;
    long timeout;
    TextView txtAsc,txtDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);
       edtSearchText=findViewById(R.id.edtSearchtext);
       // imgSearch=findViewById(R.id.img_search1);
        spinnerSearchAs=findViewById(R.id.spinnerFilter1);
        txtNotFound=findViewById(R.id.txtNotFound);
        arrayListSearchAs=new ArrayList<>();
        imgBack=findViewById(R.id.img_backSearch);
        txtAsc=findViewById(R.id.txtAsc);
        txtDesc=findViewById(R.id.txtDesc);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        currentstatus=sp.getString("SStatusId", null);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        timeout=sp.getLong("TimeOut",0);
        cid = sp.getString("Id", null);
        cid=cid.replace(" ","");

        arrayListSearchAs.add("Serial No");
        arrayListSearchAs.add("Candidate Name");
        arrayListSearchAs.add("Course");
        arrayListSearchAs.add("Mobile");
        arrayListSearchAs.add("Email");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(SearchAllActivity.this, R.layout.spinner_item1, arrayListSearchAs);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearchAs.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchAllActivity.this,Home.class));
            }
        });

        txtAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = edtSearchText.getText().toString();
                //  status = status.replaceAll(" ", "");
                searchAs = spinnerSearchAs.getSelectedItem().toString();
                if (searchAs.contains("Serial No")) {
                    searchAs = "nSrNo";
                } else if (searchAs.contains("Candidate Name")) {
                    searchAs = "cCandidateName";
                } else if (searchAs.contains("Course")) {
                    searchAs = "cCourse";
                } else if (searchAs.contains("Mobile")) {
                    searchAs = "cMobile";
                } else if (searchAs.contains("Email")) {
                    searchAs = "cEmail ";
                }
                String searchCondition = "and " + searchAs + " like 'AAAA" + searchText + "AAAA' ";
                if (searchText == "" || searchText == null) {
                    searchData("", searchAs + " ASC");
                    // getCounselor1(cid, statusid, datafrom);
                } else {
                    searchData(searchCondition, searchAs + " ASC");
                    // getCounselor1(cid, statusid, datafrom);
                }
            }
                  });
        txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAs = spinnerSearchAs.getSelectedItem().toString();
                searchText = edtSearchText.getText().toString();

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

                String searchCondition="and "+searchAs+" like '%"+searchText+"%' ";
                if(searchText==""||searchText==null)
                {
                        searchData("", searchAs + " DESC");
                        // getCounselor1(cid, statusid, datafrom);
                }
                else {
                    searchData(searchCondition, searchAs + " DESC");
                        // getCounselor1(cid, statusid, datafrom);
                }
            }
        });
        }

    public void searchData(String searchText,String orderVal) {
        // refname = refname.replaceAll(" ", "");
       // http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=AnDe828500&caseid=26&AllocatedTo=8&FieldName=cCandidateName&FieldValue=ahamd
        String url=clienturl+"?clientid="+clientid+"&caseid=34&AllocatedTo="+cid+"&FieldName="+searchText+"&OrderVal="+orderVal+"";
       // lienturl+"?clientid="+clientid+"&caseid=24&cDataFrom="+datafrom+"&AllocatedTo="+cid+"&FieldName="+searchtext+" &OrderVal="+orderval+"";
        dialog = ProgressDialog.show(SearchAllActivity.this, "Loading", "Please wait.....", false, true);

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
                    adapterCounselorData = new AdapterSearchCounselorData(SearchAllActivity.this, arraylistCounselor);
                    recyclerViewCounselor = findViewById(R.id.recyclerCounselorData1);
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
