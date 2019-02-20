package com.example.wayto.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterUserInfo adapterUserInfo;
    ArrayList<DataUser> arrayList;
    String url = "http://anilsahasrabuddhe.in/monalitesting/test3.php";
    DataUser dataUser;
    UrlRequest urlRequest;
    String id, name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerCollege);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        getData();
    }

    public void getData() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                try {
                    Log.d("Response****", response);
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        id = jsonObject1.getString("cCounselorID");
                        name = jsonObject1.getString("UserName");
                        email = jsonObject1.getString("cEmailAdd");
                        dataUser = new DataUser(id, name, email);
                        arrayList.add(dataUser);
                    }
                    Log.d("Arraylist", String.valueOf(arrayList.size()));
                    adapterUserInfo = new AdapterUserInfo(MainActivity.this, arrayList);
                    recyclerView.setAdapter(adapterUserInfo);
                    adapterUserInfo.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
