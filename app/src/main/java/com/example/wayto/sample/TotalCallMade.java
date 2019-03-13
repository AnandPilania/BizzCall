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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TotalCallMade extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterTotalCallMade adapterTotalCallMade;
    ArrayList<DataTotalCallMade> arrayList;
    ProgressDialog dialog;
    UrlRequest urlRequest;
    TextView txtTotalCallNo;
    ImageView imgBack,imgCoin;
    SharedPreferences sp;
    String counselorid, clientid,clienturl,totalcoins;
    TextView txtMsg;
    LinearLayout linearLayout;
    TextView txtCoin,txtDiamond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_call_made);

        txtCoin=findViewById(R.id.txtCoin);
      //  txtDiamond=findViewById(R.id.txtDiamond);
        txtTotalCallNo = findViewById(R.id.txtTotalCallNo);
        imgBack = findViewById(R.id.img_back);
        txtMsg = findViewById(R.id.txtNoCallMadeMsg);
        linearLayout = findViewById(R.id.linearCallColumns);
        imgCoin=findViewById(R.id.imgCoin);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dialog = ProgressDialog.show(TotalCallMade.this, "Loading", "Please wait.....", false, true);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replaceAll(" ", "");
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        totalcoins=sp.getString("TotalCoin",null);
        Log.d("CID", clientid);
        getTotalCallNo();
        getTotalCallMade();
        txtCoin.setText(totalcoins);
        imgCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TotalCallMade.this,PointCollectionDetails.class));
            }
        });

    }

    public void getTotalCallMade() {

        arrayList = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=15&CounsellorId=" + counselorid);
        Log.d("TotalCallMadeUrl", clienturl+"?clientid=" + clientid + "&caseid=15&CounsellorId=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                linearLayout.setVisibility(View.VISIBLE);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("TotalCallResponse", response);

                if (response.contains("[]")) {
                    linearLayout.setVisibility(View.GONE);
                    txtMsg.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    txtMsg.setVisibility(View.GONE);
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        // Log.d("Json",jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String srno = jsonObject1.getString("nSrNo");
                            String duration = jsonObject1.getString("cCallDuration");
                            String cdate = jsonObject1.getString("Call date");
                            String filename = jsonObject1.getString("cFileName");
                            DataTotalCallMade dataTotalCallMade = new DataTotalCallMade(srno, duration, cdate, filename);
                            arrayList.add(dataTotalCallMade);
                        }
                        adapterTotalCallMade = new AdapterTotalCallMade(TotalCallMade.this, arrayList);
                        recyclerView = findViewById(R.id.recycleTotalCallMade);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapterTotalCallMade);
                        adapterTotalCallMade.notifyDataSetChanged();

                        Log.d("Size**", String.valueOf(arrayList.size()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getTotalCallNo() {
        // dialog = ProgressDialog.show(TotalCallMade.this, "Loading", "Please wait.....", false, true);

        arrayList = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=17&CounsellorId=" + counselorid);
        Log.d("CallNoUrl", clienturl+"?clientid=" + clientid + "&caseid=17&CounsellorId=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("CallNoResponse", response);

                if (response.contains("[]")) {
                    txtTotalCallNo.setText("0");
                } else {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("Json", jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String cnt = object.getString("");
                            txtTotalCallNo.setText(cnt);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
