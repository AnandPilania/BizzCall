package com.bizcall.wayto.mentebit13;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    public RecyclerView recyclerReminder;
    public static AdapterReminder myAdapter;
    static ArrayList<DataReminder> mArrayList;
    private TextView mtxtrecords;
    private ProgressDialog dialog;
    private DataReminder dataReminder;
    private Handler notifyHandler = new Handler();
    Thread thread, t2;
    long timeout;
    String clientid,counsellorid,clienturl,totalcoins;
    int TotalReminderMatch = 0;
    int TotalReminderNotMatch = 0;
    AlertDialog.Builder builder;
    DialogInterface dialogInterface;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ImageView imgBack,imgCoin,imgRefresh;
    public static TextView txtCoin,txtDiamond;
    UrlRequest urlRequest;
    TextView txtNoReminder;
    LinearLayout linearReminder;
    Vibrator vibrator;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        try{
            //to initialize all controls and varaibles
            initialize();
            imgCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                startActivity(new Intent(ReminderActivity.this,PointCollectionDetails.class));
            }
        });
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ReminderActivity.this,ReminderActivity.class);
                   // intent.putExtra("Activity",activityName);
                    startActivity(intent);
                }
            });

      //  mtxtrecords = findViewById(R.id.total_rec);

            counsellorid=counsellorid.replace(" ","");//to remove space from counselorid string

            if(CheckInternetSpeed.checkInternet(ReminderActivity.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReminderActivity.this);
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
            else if(CheckInternetSpeed.checkInternet(ReminderActivity.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReminderActivity.this);
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

                dialog = ProgressDialog.show(ReminderActivity.this, "", "Loading reminders", true);
                newThreadInitilization(dialog);
                //to get list of reminders set by counselor
                getReminders();
            }

                // refreshWhenLoading();
               // t1.sleep(120000);

      imgBack.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              vibrator.vibrate(100);
              onBackPressed();
          }
      });
        } catch (Exception e) {
            dialog.dismiss();
            Toast.makeText(ReminderActivity.this,"Errorcode-386 ReminderActivity onCreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ReminderException", String.valueOf(e));
        }
    }
    //onCreate
    public void newThreadInitilization(final ProgressDialog dialog1)
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(ReminderActivity.this, "Connection Aborted", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(Home.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Log.d("TimeThread","cdvmklmv");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
public void initialize()
{
    sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
    vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    editor=sp.edit();
    clientid=sp.getString("ClientId",null);
    counsellorid=sp.getString("Id",null);
    clienturl=sp.getString("ClientUrl",null);
    totalcoins=sp.getString("TotalCoin",null);
    timeout=sp.getLong("TimeOut",0);
    recyclerReminder = findViewById(R.id.recycleReminder);
    imgBack=findViewById(R.id.img_back);
    txtCoin=findViewById(R.id.txtCoin);
    imgCoin=findViewById(R.id.imgCoin);
    txtDiamond=findViewById(R.id.txtDiamond);
    imgRefresh=findViewById(R.id.imgRefresh);
    txtCoin.setText(totalcoins);
    txtNoReminder=findViewById(R.id.txtNoReminder);
    linearReminder=findViewById(R.id.linearReminderColumns);
}//close initialize

    public void getReminders(){
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=33&nCounsellorId=" + counsellorid;
            Log.d("ReminderUrl",url);
            if (CheckServer.isServerReachable(ReminderActivity.this)) {
                // dialog=ProgressDialog.show(ReminderActivity.this,"","Loading reminders",true);
                RequestQueue requestQueue = Volley.newRequestQueue(ReminderActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        null, new success(), new fail());
                requestQueue.add(jsonObjectRequest);
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReminderActivity.this);
                alertDialogBuilder.setTitle("Network issue!!!!")
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
        }catch (Exception e)
        {
            Toast.makeText(ReminderActivity.this,"Errorcode-387 ReminderActivity getReminder "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//close getReminders

    private class success implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try{
            dialog.dismiss();
            Log.e("Success", String.valueOf(response));

                String res= String.valueOf(response);
                if(res.contains("[]"))
                {
                    linearReminder.setVisibility(View.GONE);
                    txtNoReminder.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearReminder.setVisibility(View.VISIBLE);
                    txtNoReminder.setVisibility(View.GONE);
                }

                mArrayList = new ArrayList<>();
                TotalReminderMatch=0;
                TotalReminderNotMatch=0;
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String cId = jsonObject.getString("SR NO");
                    String cName = jsonObject.getString("NAME");
                    String cCourse = jsonObject.getString("COURSE");
                    String cMobile1 = jsonObject.getString("MOBILE");
                    String cMobile2 = jsonObject.getString("MOBILE2");
                    String cRemarks = jsonObject.getString("REMARKS");

                    JSONObject mjsonCallDate = jsonObject.getJSONObject("CALL DATE");
                    String cDate = mjsonCallDate.getString("date");

                    String cTime = jsonObject.getString("CALL TIME");
                    String callId=jsonObject.getString("CallingId");

                    dataReminder = new DataReminder(cId, cName, cCourse, cMobile1, cMobile2, cRemarks, cDate, cTime,callId);
                    mArrayList.add(dataReminder);

                 //   mtxtrecords.setText(mArrayList.size() + "");
                  //  dialog.dismiss();
                }

                myAdapter = new AdapterReminder(ReminderActivity.this,mArrayList);
                LinearLayoutManager lm = new LinearLayoutManager(ReminderActivity.this);
                lm.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerReminder.addItemDecoration(new DividerItemDecoration(ReminderActivity.this, DividerItemDecoration.VERTICAL));
                recyclerReminder.setLayoutManager(lm);
                recyclerReminder.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                int extramin = 30;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, extramin);
                String date1 = sdf.format(cal.getTime());

                for (int i = 0; i <mArrayList.size(); i++) {
                    String mdate = mArrayList.get(i).getmDate().substring(0, 10);
                    String mtime = mArrayList.get(i).getmTime();

                    if (mtime.length() == 9) {
                        mtime = "0" + mtime.replace(".", "");
                    } else {
                        mtime = mtime.replace(".", "");
                    }
                    String DBDateFormat = mdate + " " + mtime;
                    Log.d("DBDATE=====", DBDateFormat);
                    Log.d("current date=====", date1);

                    if (date1.compareTo(DBDateFormat) < 0) {
                        TotalReminderMatch++;
                        Log.d("match date========", TotalReminderMatch + "");
                    } else {
                        TotalReminderNotMatch++;
                        Log.d("not match date========", TotalReminderNotMatch + "");
                    }
                }
                notifyHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        //Define sound URI
                        final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Call Details")
                                .setContentText("Calls Arriving / Pending => " + TotalReminderMatch +" / "+ TotalReminderNotMatch)
                                .setWhen(System.currentTimeMillis())
                                .setSound(soundUri)
                                .setTimeoutAfter(2000)
                                .setVibrate(new long[]{1000,1000,1000,1000,1000}); //This sets the sound to play


                        Intent intent = new Intent(ReminderActivity.this, ReminderActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(ReminderActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingIntent);

                        if(TotalReminderMatch!=0&&TotalReminderNotMatch!=0) {
                            //Display notification
                            notificationManager.notify(0, mBuilder.build());
                        }
                    }
                });

            } catch (Exception e) {
                Toast.makeText(ReminderActivity.this,"Errorcode-388 ReminderActivity getReminderResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                Log.d("Exception", String.valueOf(e));
            }
        }
    }

    private class fail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            dialog.dismiss();
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReminderActivity.this);
            alertDialogBuilder.setTitle("Network issue!!!")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            Log.e("Fail", String.valueOf(error));
        }
    }

     @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ReminderActivity.this, Home.class);
        intent.putExtra("Activity","ReminderActivity");
        startActivity(intent);
        finish();
    }
}

