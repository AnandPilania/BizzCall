package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
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

public class PointCollectionDetails extends AppCompatActivity {

   ProgressDialog dialog;
   UrlRequest urlRequest;
   String clienturl,clientid,counselorid;
   SharedPreferences sp;
   RecyclerView recyclerCoins;
   AdapterCoinsDetails adapterCoinsDetails;
   ArrayList<DataCoin> coinArrayList;
   LinearLayout linearCoin;
   TextView txtCoin;
   ImageView imgBack,imgCoin;
   String totalcoins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_collection_details);
        recyclerCoins=findViewById(R.id.recyclerCoins);
        coinArrayList=new ArrayList<>();
        linearCoin=findViewById(R.id.linearCoinColumns);
        imgBack=findViewById(R.id.img_back);
        imgCoin=findViewById(R.id.imgCoin);
        txtCoin=findViewById(R.id.txtCoin);


        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        counselorid=sp.getString("Id",null);
        totalcoins=sp.getString("TotalCoin",null);
        counselorid=counselorid.replace(" ","");
        dialog= ProgressDialog.show(PointCollectionDetails.this, "", "Loading...", true);
        getPointCollection();
        txtCoin.setText(totalcoins);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void getPointCollection() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        //String clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php";
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=38&CounsellorId=" + counselorid);
        Log.d("CojnUrl", clienturl+"?clientid=" + clientid + "&caseid=38&CounsellorId=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if(linearCoin.getVisibility()==View.GONE)
                {
                    linearCoin.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearCoin.setVisibility(View.GONE);
                }

                Log.d("CoinResponse", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String pointCollectId = jsonObject1.getString("nPointCollectId");
                        String event = jsonObject1.getString("cEvent");
                        String point = jsonObject1.getString("cPoint");
                        String valid_from = jsonObject1.getString("Valid From");
                        String valid_upto = jsonObject1.getString("Valid Upto");
                        DataCoin dataCoin=new DataCoin(pointCollectId,event,point,valid_from,valid_upto);
                        coinArrayList.add(dataCoin);
                        }
                        adapterCoinsDetails=new AdapterCoinsDetails(PointCollectionDetails.this,coinArrayList);
                         LinearLayoutManager layoutManager=new LinearLayoutManager(PointCollectionDetails.this);
                         layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerCoins.setLayoutManager(layoutManager);
                         recyclerCoins.setAdapter(adapterCoinsDetails);
                        adapterCoinsDetails.notifyDataSetChanged();
                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
