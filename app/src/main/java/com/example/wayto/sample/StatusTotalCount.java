package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatusTotalCount extends AppCompatActivity {

    ProgressDialog dialog;
    UrlRequest urlRequest;
    ArrayList<DataStatusTotal> arrayList;
    AdapterStatusTotalCount adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_total_count);
        // recyclerView=findViewById(R.id.recyclerStatusTotalCnt);
        getStatusCount();

    }

    public void getStatusCount() {
        dialog = ProgressDialog.show(StatusTotalCount.this, "Loading", "Please wait.....", false, true);

        arrayList = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/StatusTotalCount.php");
        Log.d("StatusCountUrl", "http://anilsahasrabuddhe.in/CRM/AnDe828500/StatusTotalCount.php");
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("StatusTotalResponse", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String status = jsonObject1.getString("cStatus");
                        String cstauts = jsonObject1.getString("currentstatus");
                        String total = jsonObject1.getString("Total No");

                        DataStatusTotal dataStatusTotal = new DataStatusTotal(status, cstauts, total);
                        arrayList.add(dataStatusTotal);
                    }

                    adapter = new AdapterStatusTotalCount(arrayList, StatusTotalCount.this);
                    recyclerView = findViewById(R.id.recyclerStatusTotalCnt);
                    recyclerView.setLayoutManager(new GridLayoutManager(StatusTotalCount.this, 2));

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    Log.d("Size**", String.valueOf(arrayList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
