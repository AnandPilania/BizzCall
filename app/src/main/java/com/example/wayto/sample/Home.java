package com.example.wayto.sample;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String counselorid, uname, emailid, role, mobile, status1, statusid1, selectedStatus;
    TextView txtUName, txtCoin, txtDiamond, txtRole, txtMobile, txtCounselorId, txtSelectedStatus, txtCID, txtSID;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    AlertDialog alertDialog;
    int pos,check=0;
    // ArrayList<StatusInfo> arrayList;

    //  ArrayList<String> arrayList1;
    //  Spinner spinnerStatus;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String uname1, id, statusid, sid;
    Intent intent;
    TextView mtextView;
    Button btnSubmit;
    Toast toast;
    Login login;
    String dataFrom, dataFrom1;
    ArrayList<DataStatusTotal> arrayListTotal;
    AdapterStatusTotalCount adapterTotal;
    RecyclerView recyclerView;
    String loginDate,clienturl;
    Spinner spinnerRef;
    // ArrayList<DataReference> arrayListRefrences;
    ArrayList<String> arrayListRefId;
    String clientid;
    Long timeout;
    private long back_pressed = 0;

    private Handler notifyHandler = new Handler();
    Thread t1;
    int TotalReminderMatch = 0;
    int TotalReminderNotMatch = 0;
    ArrayList<DataReminder> mArrayList;
    DataReminder dataReminder;
    ProgressDialog progressDialog;
    ImageView imgCoin;


   /* @Override
    protected void onResume() {
        super.onResume();
        if(checkPermission())
        {
            Toast.makeText(Home.this,"Permission already granted",Toast.LENGTH_SHORT).show();
        }
    }*/

    class MyJSON implements Runnable {
        @Override
        public void run() {
            try {
                String url = "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=33";
                Log.d("ReminderUrl",url);
                while (true) {
                    RequestQueue requestQueue = Volley.newRequestQueue(Home.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                            null, new success(), new fail());
                    requestQueue.add(jsonObjectRequest);
                    t1.sleep(120000);

                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (checkPermission()) {
            Toast.makeText(Home.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAPTURE_AUDIO_OUTPUT}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putInt("numOfCalls", 0).apply();

        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        timeout=sp.getLong("TimeOut",0);
        Log.d("timeout", String.valueOf(timeout));
        login = new Login();
        uname = sp.getString("Name", null);
        counselorid = sp.getString("Id", null);
        counselorid = counselorid.replaceAll(" ", "");
        emailid = sp.getString("EmailId", null);
        role = sp.getString("Role", null);
        mobile = sp.getString("MobileNo", null);
        statusid1 = sp.getString("StatusId", null);
        statusid1 = statusid1.replaceAll(" ", "");
        Log.d("Mobile*", mobile);
        Log.d("Name*", uname);
        Log.d("Id*", counselorid);
        Log.d("Email*", emailid);
        Log.d("Role*", role);
        Log.d("statusid*", statusid1);

        uname1 = sp.getString("Name", null);
        id = sp.getString("Id", null);
        Log.d("U***", uname1);
        Log.d("I***", id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        Runnable mthread1 = new MyJSON();
          //Runnable mthread2 = new compareDate();

        t1 = new Thread(mthread1);
         // t2 = new Thread(mthread2);

        t1.start();

        spinnerRef = findViewById(R.id.spinnerFilter);
        txtCoin=findViewById(R.id.txtCoin);
        txtDiamond=findViewById(R.id.txtDiamond);
        imgCoin=findViewById(R.id.imgCoin);
        //arrayList = new ArrayList<>();
        // arrayList1 = new ArrayList<>();
        // TextView txtUsername=findViewById(R.id.txtUserName);
        // TextView txtRole11=findViewById(R.id.txtRole11);
        setSupportActionBar(toolbar);
        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //spinnerStatus = findViewById(R.id.spinnerStatus);
        //  getStatus();
        recyclerView = findViewById(R.id.recyclerStatusTotalCnt);




       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // checkPermission();
        dialog = ProgressDialog.show(Home.this, "", "Loading...", true);
       /* new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                      //  Toast.makeText(Home.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                    }
                });*/

        //getStatusCount();
        mtextView=findViewById(R.id.txtTimer);
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

                    AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
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
                            startActivity(new Intent(Home.this,Login.class));

                        }
                    });

                    }
            }
        }.start();
        editor = sp.edit();
        editor.putString("DtaFrom", "0");
        editor.commit();

        getRefName();
        getAllRefName();
        lastLoginDetails();
        getPointCollection();

          /*  }
        }).start();*/

          imgCoin.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startActivity(new Intent(Home.this,PointCollectionDetails.class));
              }
          });
        spinnerRef.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    pos = position;
                    String texttoload = spinnerRef.getSelectedItem().toString();
                    editor = sp.edit();
                    editor.putString("DataRefName", texttoload);
                    editor.commit();
                    LayoutInflater li = LayoutInflater.from(getApplicationContext());
                    //Creating a view to get the dialog box
                    View confirmCall = li.inflate(R.layout.layout_confirm_loadtext, null);
                    TextView txtYes = (TextView) confirmCall.findViewById(R.id.txtYes);
                    TextView txtNo = (TextView) confirmCall.findViewById(R.id.txtNo);
                    TextView txtLoad = confirmCall.findViewById(R.id.txtConfirmLoad);
                    txtLoad.setText("Do you want to load " + texttoload + "?");
                    AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
                    //Adding our dialog box to the view of alert dialog
                    alert.setView(confirmCall);
                    //Creating an alert dialog
                    alertDialog = alert.create();
                if (++check > 1) {
                    alertDialog.show();
                }

                    txtYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (pos == 0) {
                                editor = sp.edit();
                                editor.putString("DtaFrom", "0");
                                editor.commit();
                                dialog = ProgressDialog.show(Home.this, "", "Loading...", true);
                           /* new Thread(new Runnable() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            //  Toast.makeText(Home.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                        }
                                    });*/

                                //getStatusCount();
                                getAllRefName();
                               /* }
                            }).start();*/

                            } else {
                                int p = pos - 1;

                                // DataReference dataReference1=arrayListRefrences.get(position);
                                dataFrom1 = arrayListRefId.get(p);
                                Log.d("RefId", dataFrom1);
                                editor = sp.edit();
                                editor.putString("DtaFrom", dataFrom1);
                                editor.commit();
                                dialog = ProgressDialog.show(Home.this, "", "Loading...", true);
                           /* new Thread(new Runnable() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            //  Toast.makeText(Home.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                        }
                                    });*/

                                //getStatusCount();
                                getRestRefName();
                              /*  }
                            }).start();*/


                            }
                            alertDialog.dismiss();

                        }
                    });

                    txtNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerRef.setSelection(0);

            }


        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        // txtUId = headerView.findViewById(R.id.txtUId);
        txtUName = headerView.findViewById(R.id.txtUName);
        // txtEmail=headerView.findViewById(R.id.txtEmailAdd);
        // txtRole = headerView.findViewById(R.id.txtRole1);
        txtMobile = headerView.findViewById(R.id.txtMobileNo1);
        //txtUsername.setText(uname);
        // txtRole11.setText(role);
        // txtUId.setText(counselorid);
        String un = txtUName.getText().toString();
        Log.d("username1111", un);
        txtUName.setText(uname);
        //txtEmail.setText(emailid);
        //  txtRole.setText("AnDe828500");
        txtMobile.setText(mobile);

        navigationView.setNavigationItemSelectedListener(this);
    }



    public void lastLoginDetails() {
        //dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);

        arrayListTotal = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=14&CounsellorId=" + counselorid);
        Log.d("lastloginResponse", clienturl+"?clientid=" + clientid + "&caseid=14&CounsellorId=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                JSONObject jsonObject = new JSONObject(response);
                // Log.d("Json",jsonObject.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    loginDate = jsonObject1.getString("dtLoginDate");
                }
                TextView txtLastLogin = findViewById(R.id.txtLastLogin);
                txtLastLogin.setText(loginDate);
                Log.d("LastLoginResponse", response);

            }
        });
    }

    /*public void getStatusCount() {

        arrayListTotal = new ArrayList<>();
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
                        arrayListTotal.add(dataStatusTotal);
                    }

                    adapterTotal = new AdapterStatusTotalCount(arrayListTotal, Home.this);
                    recyclerView = findViewById(R.id.recyclerStatusTotalCnt);
                    recyclerView.setLayoutManager(new GridLayoutManager(Home.this, 2));

                    recyclerView.setAdapter(adapterTotal);
                    adapterTotal.notifyDataSetChanged();

                    //   Log.d("Size**", String.valueOf(arrayList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
*/
    public void getRefName() {
        // arrayListRefrences=new ArrayList<>();
        final ArrayList<String> arrayList1 = new ArrayList<>();
        arrayListRefId = new ArrayList<>();
        arrayList1.add(0, "All");
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=11&CounsellorId=" + counselorid);
        Log.d("RefNameUrl", clienturl+"?clientid=" + clientid + "&caseid=11&CounsellorId=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("RefNameUrl", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        dataFrom = jsonObject1.getString("cDataFrom");
                        String dataRefName = jsonObject1.getString("DataRefName");
                        //    DataReference dataReference=new DataReference(dataRefName,dataFrom);
                        arrayList1.add(dataRefName);
                        arrayListRefId.add(dataFrom);
                        // arrayListRefrences.add(dataReference);
                        //  String total=jsonObject1.getString("Total No");

                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter(Home.this, R.layout.spinner_item1, arrayList1);
                    // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRef.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();

                    Log.d("RefIdSize", String.valueOf(arrayListRefId.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getAllRefName() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        //String clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php";
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=12&CounsellorId=" + counselorid);
        Log.d("AllRefNameUrl", clienturl+"?clientid=" + clientid + "&caseid=12&CounsellorId=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("AllRefNameResponse", response);
                try {
                    arrayListTotal.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String status = jsonObject1.getString("cStatus");
                        String cstauts = jsonObject1.getString("currentstatus");
                        String total = jsonObject1.getString("Total No");
                        DataStatusTotal dataStatusTotal = new DataStatusTotal(status, cstauts, total);
                        arrayListTotal.add(dataStatusTotal);
                    }

                    adapterTotal = new AdapterStatusTotalCount(arrayListTotal, Home.this);
                    recyclerView = findViewById(R.id.recyclerStatusTotalCnt);
                    recyclerView.setLayoutManager(new GridLayoutManager(Home.this, 2));

                    recyclerView.setAdapter(adapterTotal);
                    adapterTotal.notifyDataSetChanged();


                    //   Log.d("Size**", String.valueOf(arrayList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getRestRefName() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=13&CounsellorId=" + counselorid + "&DataFrom=" + dataFrom1);
        Log.d("RestRefNameUrl", clienturl+"?clientid=" + clientid + "&caseid=13&CounsellorId=" + counselorid + "&DataFrom=" + dataFrom1);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("RestRefNameResponse", response);
                try {
                    arrayListTotal.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String status = jsonObject1.getString("cStatus");
                        String cstauts = jsonObject1.getString("currentstatus");
                        String total = jsonObject1.getString("Total No");
                        DataStatusTotal dataStatusTotal = new DataStatusTotal(status, cstauts, total);
                        arrayListTotal.add(dataStatusTotal);
                    }

                    adapterTotal = new AdapterStatusTotalCount(arrayListTotal, Home.this);
                    recyclerView = findViewById(R.id.recyclerStatusTotalCnt);
                    recyclerView.setLayoutManager(new GridLayoutManager(Home.this, 2));
                    recyclerView.setAdapter(adapterTotal);
                    adapterTotal.notifyDataSetChanged();
                    //   Log.d("Size**", String.valueOf(arrayList.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
        });
    }
    public void getPointCollection() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        //String clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php";
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=37&CounsellorId=" + counselorid);
        Log.d("CojnUrl", clienturl+"?clientid=" + clientid + "&caseid=37&CounsellorId=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("CoinResponse", response);
                try {
                    arrayListTotal.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String totalcoin = jsonObject1.getString("Total Coin");
                        txtCoin.setText(totalcoin);
                        editor = sp.edit();
                        editor.putString("TotalCoin",totalcoin);
                        editor.commit();
                    }
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (back_pressed + 2000 > System.currentTimeMillis()) {
            // need to cancel the toast here
            toast.cancel();
            // code for exit
           /* editor=sp.edit();
            editor.putString("Name",null);
            // editor.putString("Id",null);
            editor.commit();

            login.edtName.getText().clear();
            login.edtPassword.getText().clear();
            login.edtName.clearFocus();
            login.edtPassword.clearFocus();*/
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else {
            // ask user to press back button one more time to close app
            toast = Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT);
            toast.show();
        }
        back_pressed = System.currentTimeMillis();
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {

            // Handle the camera action
        } else if (id == R.id.nav_upload) {
            Intent intent = new Intent(Home.this, FileListActivity.class);
            intent.putExtra("ActivityName", "Home");
            startActivity(intent);

        }else if(id==R.id.nav_reminder)
        {
            startActivity(new Intent(Home.this,ReminderActivity.class));
        }
        else if (id == R.id.nav_template) {
            Intent intent = new Intent(Home.this, MessageTemplate.class);
            startActivity(intent);
        } else if (id == R.id.nav_totalCallMade) {
            Intent intent = new Intent(Home.this, TotalCallMade.class);
            startActivity(intent);

        } /*else if (id == R.id.nav_browse) {

            Intent intent = new Intent(Home.this, BrowseActivity.class);
            startActivity(intent);

        }*/ /*else if (id == R.id.nav_send) {

        }*/ else if (id == R.id.nav_logout) {
            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            //Creating a view to get the dialog box
            View confirmCall = li.inflate(R.layout.layout_confirm_logout, null);
            TextView txtYes = (TextView) confirmCall.findViewById(R.id.txtYes);
            TextView txtNo = (TextView) confirmCall.findViewById(R.id.txtNo);

            AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
            //Adding our dialog box to the view of alert dialog
            alert.setView(confirmCall);
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();

            txtYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    editor = sp.edit();
                    editor.putString("Name", null);
                    // editor.putString("Id",null);
                    editor.commit();
                    intent = new Intent(Home.this, Login.class);
                    startActivity(intent);
                    finish();

                }
            });

            txtNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

        }else if(id==R.id.nav_search)
        {
            startActivity(new Intent(Home.this,SearchAllActivity.class));
        }
        else if(id==R.id.nav_report)
        {
            startActivity(new Intent(Home.this,ReportActivity.class));
        }
        else if (id == R.id.nav_bulksms) {

            Intent intent = new Intent(Home.this, BulkSmsActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_graphreport)
        {
            startActivity(new Intent(Home.this,GraphReport.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
  /*  public void getStatus()
    {
        dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);

        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?caseid=2");
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

              if(dialog.isShowing())
              {
                  dialog.dismiss();
              }
                Log.d("StatusResponse", response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        status1=jsonObject1.getString("cStatus");
                        statusid=jsonObject1.getString("nStatusID");
                        Log.d("Status11",statusid);
                        StatusInfo statusInfo=new StatusInfo(status1,statusid);

                        // Log.d("Json33333",statusInfo.toString());
                        arrayList.add(statusInfo);
                        arrayList1.add(status1);
                        // Log.d("Json11111",arrayList1.toString());
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(Home.this,R.layout.spinner_item1,arrayList1);
                   // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatus.setAdapter(arrayAdapter);
                    Log.d("Size**", String.valueOf(arrayList1.size()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        }*/



    /*@Override
    protected void onResume() {
        super.onResume();
        Log.e("Check", "onResume: ");
        if(checkPermission())
        {
            Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
           *//* if(checkResume==false) {
              //  setUi();
                // this.callDetailsList=new DatabaseManager(this).getAllDetails();
              //  rAdapter.notifyDataSetChanged();
            }*//*
        }
    }*/
   /* protected void onPause()
    {
        super.onPause();
        SharedPreferences pref3=PreferenceManager.getDefaultSharedPreferences(this);
        if(pref3.getBoolean("pauseStateVLC",false)) {
            checkResume = true;
            pref3.edit().putBoolean("pauseStateVLC",false).apply();
        }
        else
            checkResume=false;
    }*/

   /* public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        MenuItem item=menu.findItem(R.id.mySwitch);

        View view = getLayoutInflater().inflate(R.layout.switch_layout,null,false) ;

        final SharedPreferences pref1= PreferenceManager.getDefaultSharedPreferences(this);

        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.switchCheck);
        switchCompat.setChecked(pref1.getBoolean("switchOn",true));
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.d("Switch", "onCheckedChanged: " +isChecked);
                    Toast.makeText(getApplicationContext(), "Call Recorder ON", Toast.LENGTH_LONG).show();
                    pref1.edit().putBoolean("switchOn",isChecked).apply();
                }else{
                    Log.d("Switch", "onCheckedChanged: " +isChecked);
                    Toast.makeText(getApplicationContext(), "Call Recorder OFF", Toast.LENGTH_LONG).show();
                    pref1.edit().putBoolean("switchOn",isChecked).apply();
                }
            }
        });
        item.setActionView(view);
        return true;
    }*/

    private boolean checkPermission() {
        int i = 0;

        String[] perm = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE};
        List<String> reqPerm = new ArrayList<>();

        for (String permis : perm) {
            int resultPhone = ContextCompat.checkSelfPermission(Home.this, permis);
            if (resultPhone == PackageManager.PERMISSION_GRANTED)
                i++;
            else {
                reqPerm.add(permis);
            }
        }

        if (i == 5)
            return true;
        else
            return requestPermission(reqPerm);
    }


    private boolean requestPermission(List<String> perm) {
        // String[] permissions={Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String[] listReq = new String[perm.size()];
        listReq = perm.toArray(listReq);
        for (String permissions : listReq) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permissions)) {
                Toast.makeText(getApplicationContext(), "Phone Permissions needed for " + permissions, Toast.LENGTH_LONG);
            }
        }

        ActivityCompat.requestPermissions(Home.this, listReq, 1);


        return false;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted to access Phone calls", Toast.LENGTH_LONG);
                else
                    Toast.makeText(getApplicationContext(), "You can't access Phone calls", Toast.LENGTH_LONG);
                break;
        }
    }

    private class success implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            Log.e("Success", String.valueOf(response));
            try {
                mArrayList = new ArrayList<>();
                TotalReminderMatch = 0;
                TotalReminderNotMatch = 0;
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
                   // progressDialog.dismiss();
                }


                int extramin = 30;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, extramin);
                String date1 = sdf.format(cal.getTime());

                for (int i = 0; i < mArrayList.size(); i++) {
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
                                .setContentText("Calls Arriving / Pending => " + TotalReminderMatch + " / " + TotalReminderNotMatch)
                                .setWhen(System.currentTimeMillis())
                                .setSound(soundUri)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}); //This sets the sound to play

                        Intent intent = new Intent(Home.this, ReminderActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(Home.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingIntent);

                        //Display notification
                        notificationManager.notify(0, mBuilder.build());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class fail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Fail", String.valueOf(error));
        }
    }

}



