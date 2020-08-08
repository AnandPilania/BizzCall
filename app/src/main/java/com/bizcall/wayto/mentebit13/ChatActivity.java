package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AdapterChat myAdapter;
    private ArrayList<DataChat> mArrayList;
    private DataChat details;
    private RequestQueue requestQueue;
    private Thread thread;
    long timeout;

    private EditText edtMessage;
    private ImageView imgbtnSend;
    private String msg;
    ImageView imgBack,imgRefresh;
    private String url,clienturl,clientid,counselorid;
    SharedPreferences sp;
    ProgressDialog dialog;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_chat);
            initialize();

            Runnable mthread = new myJSON();
            thread = new Thread(mthread);
            thread.start();
            //mJson();
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    onBackPressed();
                }
            });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ChatActivity.this,ChatActivity.class);
                    startActivity(intent);
                }
            });
            imgbtnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtMessage.getText().toString().length() == 0) {
                        edtMessage.setError("Write Message");
                    } else {
                        msg = edtMessage.getText().toString();
                       // dialog = ProgressDialog.show(ChatActivity.this, "", "Sending message", true);
                        if(CheckInternetSpeed.checkInternet(ChatActivity.this).contains("0")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ChatActivity.this);
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
                        else if(CheckInternetSpeed.checkInternet(ChatActivity.this).contains("1")) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ChatActivity.this);
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
                        }
                        else {
                            //to save inserted message to database
                            saveMsg(msg);
                        }
                        //refreshWhenLoading();
                        edtMessage.setText("");
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(ChatActivity.this,"Got exception,can't load",Toast.LENGTH_SHORT);
            Log.d("ChatException", String.valueOf(e));
        }
    }
    //onCreate
    public void initialize()
    {
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clienturl = sp.getString("ClientUrl", null);
        clientid = sp.getString("ClientId", null);
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replace(" ", "");
        url = clienturl + "?clientid=" + clientid + "&caseid=42";
        Log.d("ChatUrl", url);
        requestQueue = Volley.newRequestQueue(ChatActivity.this);
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        imgRefresh=findViewById(R.id.imgRefresh);
        recyclerView = findViewById(R.id.chat_recyclerview);
        edtMessage = findViewById(R.id.txt_insert);
        imgbtnSend = findViewById(R.id.imgbtn_send);
        imgBack = findViewById(R.id.img_back);
        mArrayList = new ArrayList<>();
        myAdapter = new AdapterChat(mArrayList, ChatActivity.this);

        LinearLayoutManager lm = new LinearLayoutManager(ChatActivity.this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.addItemDecoration(new DividerItemDecoration(ChatActivity.this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(myAdapter);
    }

       class myJSON implements Runnable{

        @Override
        public void run() {
            try {
                while (true) {
                    //dialog=ProgressDialog.show(ChatActivity.this,"","Loading messages",true);
                    RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                            null, new success(), new fail());
                    requestQueue.add(jsonObjectRequest);
                    thread.sleep(5000);
                   // refreshWhenLoading();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*public void mJson() {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new success(), new fail());
        requestQueue.add(jsonObjectRequest);
    }*/

    public void saveMsg(String msg) {
        String strurl = clienturl+"?clientid="+clientid+"&caseid=43&ChatMsg=" + msg + "&cCounsellorID="+counselorid+"&ChatTo=0";
       Log.d("ChatUrl",strurl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ChatResponse", response);
                if (response.contains("Data inserted successfully")) {
                    mArrayList = new ArrayList<>();
                    myAdapter = new AdapterChat(mArrayList, ChatActivity.this);
                    new myJSON();
                    //mJson();
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(ChatActivity.this, "Msg inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, "Msg not inserted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", String.valueOf(error));
            }
        });
        requestQueue.add(stringRequest);
    }

    private class success implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("Success", String.valueOf(response));
            try {
              //  dialog.dismiss();
                mArrayList.clear();
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String mdate = jsonObject.getString("Date");
                    String msender = jsonObject.getString("Sender");
                    String mmessage = jsonObject.getString("Message");

                    details = new DataChat(mdate, msender, mmessage);
                    mArrayList.add(details);
                    myAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class fail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {

           // dialog.dismiss();
            Log.e("Fail", String.valueOf(error));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ChatActivity.this, Home.class);
        intent.putExtra("Activity","Chat");
        startActivity(intent);
        finish();
       // super.onBackPressed();
    }
}
