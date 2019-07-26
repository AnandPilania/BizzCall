package com.bizcall.wayto.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//import com.google.android.gms.common.api.GoogleApiClient;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String counselorid, uname, emailid, role, mobile, status1, statusid1, selectedStatus;
    TextView txtUName, txtCoin, txtDiamond, txtRole, txtMobile, txtCounselorId, txtLogout, txtCID, txtSID;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    AlertDialog alertDialog,alertRefName;
    int check=0,position1,pos;
    String activityname;
    ArrayList<String> arrayList1;
    private boolean isSpinnerTouched = false;
    NavigationView navigationView;

    WifiManager wifiManager;
    // ArrayList<StatusInfo> arrayList;

    String uploadFilePath = "";
    String uploadFileName = "",urlSMSDate,url;
    String upLoadServerUri = null;
    int serverResponseCode = 0;

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
    ImageView imgCoin,imgProfile;
    private Vibrator vibrator;
    public static final int GET_FROM_GALLERY = 1;
    View view_Group;

    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DrawerLayout mDrawerLayout;

    RequestQueue requestQueue;
    Button btnLogin,btnVerify;
    //SMS ////
    String urlCallDate;
    private ArrayList<CallDetialswithIMEI> mCallArrayList;

    private SimpleDateFormat sdfSaveArray;


    int counturl = 0;


    String callPhoneno, finalCallType, strdateFormated, callDuration, strImei1, strImei2, callphAccID;

    private ArrayList<CallDetialswithIMEI>  mUploadCallArrayList;
    private ArrayList<CallDetailsFetch> mUrlCallArrayList;


    private ArrayList<SMSDetails> mSMSArrayList, mUploadSMSArrayList;
    private ArrayList<SMSDetailsFetch> mUrlSMSArrayList;


    private CallDetailsFetch callDetailsFetch;
    private SMSDetails smsDetails;
    private SMSDetailsFetch smsDetailsFetch;


    private CallDetialswithIMEI callDetialswithIMEI;


    private SimpleDateFormat sdfComapre;

    String IMEINumber1,IMEINumber2;

    ////Notification
    RecyclerView recyclerViewNotification;
    AdapterNotification adapterNotification;
    ArrayList<DataNotification> arrayListNotification;
    ImageView imgNotification,imgNotification1,imgRefresh;
    int clickcount=0;
    NotificationManager notificationManager;
    DataNotification dataNotification;
    NetworkInfo info;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        requestQueue=Volley.newRequestQueue(Home.this);

            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (checkPermission()) {
                Toast.makeText(Home.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            }
           // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAPTURE_AUDIO_OUTPUT}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);

            imgRefresh=findViewById(R.id.imgRefresh);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            pref.edit().putInt("numOfCalls", 0).apply();
            activityname = getIntent().getStringExtra("Activity");

            sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            editor.putString("ActivityContact", "Home");
            editor.commit();
            // sp=getSharedPreferences("Settings",Context.MODE_PRIVATE);
            IMEINumber1 = sp.getString("IMEI1", null);
            IMEINumber2 = sp.getString("IMEI2", null);

            clientid = sp.getString("ClientId", null);
            clienturl = sp.getString("ClientUrl", null);
            Log.d("UrlHome", clienturl);
            timeout = sp.getLong("TimeOut", 0);
            Log.d("timeout", String.valueOf(timeout));
            login = new Login();
            uname1 = sp.getString("Name", null);
            id = sp.getString("Id", null);
            Log.d("U***", uname1);
            Log.d("I***", id);
            uname = sp.getString("Name", null);
            counselorid = sp.getString("Id", null);
            counselorid = counselorid.replaceAll(" ", "");
            emailid = sp.getString("EmailId", null);
            role = sp.getString("Role", null);
            mobile = sp.getString("MobileNo", null);
            statusid1 = sp.getString("StatusId", null);
            statusid1 = statusid1.replaceAll(" ", "");

            upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=46&CounselorId=" + id;
            Log.d("UploadUrl", upLoadServerUri);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
          //  Runnable mthread1 = new MyJSON();
            //Runnable mthread2 = new compareDate();

           // t1 = new Thread(mthread1);
            // t2 = new Thread(mthread2);

            //t1.start();

            spinnerRef = findViewById(R.id.spinnerFilter);
            txtCoin = findViewById(R.id.txtCoin);
            txtDiamond = findViewById(R.id.txtDiamond);
            imgCoin = findViewById(R.id.imgCoin);
            imgNotification = findViewById(R.id.imgNotification);
            imgNotification1 = findViewById(R.id.imgNotification1);

            setSupportActionBar(toolbar);

            recyclerView = findViewById(R.id.recyclerStatusTotalCnt);
            arrayListNotification = new ArrayList<>();

            //getUserLocation();

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

            final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
            mtextView = findViewById(R.id.txtTimer);
            if(CheckInternetSpeed.checkInternet(Home.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
            else if(CheckInternetSpeed.checkInternet(Home.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
                dialog = ProgressDialog.show(Home.this, "", "Loading all data for refnames...", false,true);
                getRefName();
            }
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Home.this,Home.class);
                        intent.putExtra("Activity",activityname);
                        startActivity(intent);
                    }
                });

            imgCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(100);
                    startActivity(new Intent(Home.this, PointCollectionDetails.class));
                }
            });

            imgNotification1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayNotification();
                    }
            });

            spinnerRef.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isSpinnerTouched = true;

                    return false;
                }
            });
            spinnerRef.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                    pos = position;
                    spinnerItemSelected();
                    }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {//alertRefName.dismiss();
                    //spinnerRef.setSelection(0);
                    //  getAllRefName();

                }

            });

           navigationView = findViewById(R.id.nav_view);

            View headerView = navigationView.getHeaderView(0);
            txtUName = headerView.findViewById(R.id.txtUName);
            txtMobile = headerView.findViewById(R.id.txtMobileNo1);
            txtLogout=headerView.findViewById(R.id.txtLogout);

            String un = txtUName.getText().toString();
            Log.d("username1111", un);
            txtUName.setText(uname);
            txtMobile.setText(mobile);
            imgProfile = headerView.findViewById(R.id.imageProfile);
            txtLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutClick();
                }
            });
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                }
            });
            navigationView.setItemIconTintList(null);
            if (navigationView != null) {
                setupDrawerContent(navigationView);
            }
            prepareListData();
            mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

            // setting list adapter
            expandableList.setAdapter(mMenuAdapter);
            expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                  // Toast.makeText(Home.this, "Header: " + String.valueOf(groupPosition) + "\nItem: " + String.valueOf(childPosition), Toast.LENGTH_SHORT).show();

                   final String selected = (String) mMenuAdapter.getChild(
                            groupPosition, childPosition);

                    // id = (String) mMenuAdapter.getChild(groupPosition, childPosition);
                    Intent intent;
                    switch (selected) {

                        case "Online Leads":
                            // case "Submenu1":
                            intent = new Intent(Home.this, OnlineLead.class);
                            intent.putExtra("ActivityLeads", "OnlineLead");
                            startActivity(intent);
                            break;
                        case "Form Filled":
                            // case "Submenu1":
                            intent = new Intent(Home.this, FormFilled.class);
                            //intent.putExtra("Activity", "OnlineLead");
                            startActivity(intent);
                            break;
                        case "Open Leads":
                            intent=new Intent(Home.this,OpenLeads.class);
                            intent.putExtra("Activity","OpenLead");
                            startActivity(intent);
                            break;
                            case "Dashboard":
                            // case "Submenu1":
                            intent = new Intent(Home.this, Home.class);
                            intent.putExtra("Activity", selected);
                            //intent.putExtra("MainMenu",MainMenuselected);
                            startActivity(intent);
                            break;
                        case "Converted Online Lead":
                            // case "Submenu1":
                            intent = new Intent(Home.this, OnlineLead.class);
                            intent.putExtra("ActivityLeads", "ConvertedOnlineLead");
                            startActivity(intent);
                            break;


                        case "New Lead":
                            intent = new Intent(Home.this, EditDetails.class);
                            intent.putExtra("ActivityName", "NewLead");
                            startActivity(intent);
                            break;

                        case "Upload File":
                            // case "Submenu1":
                            intent = new Intent(Home.this, FileListActivity.class);
                            intent.putExtra("ActivityName", "Home");
                            startActivity(intent);
                            break;

                        case "Reminder":
                            startActivity(new Intent(Home.this, ReminderActivity.class));
                            break;

                        case "Master Entry":
                            startActivity(new Intent(Home.this, MasterEntry.class));
                            break;

                        case "SMS Template":

                            // case "Submenu1":
                            intent = new Intent(Home.this, MessageTemplate.class);
                            intent.putExtra("Activity","Message");
                            startActivity(intent);
                            break;

                        case "Mail Template":
                            // case "Submenu1":
                            intent = new Intent(Home.this, MessageTemplate.class);
                            intent.putExtra("Activity","Mail");
                            startActivity(intent);
                            break;

                        case "Total Calls Made":
                            intent = new Intent(Home.this, TotalCallMade.class);
                            startActivity(intent);
                            break;

                        case "Search":
                            // case "Submenu1":
                            intent = new Intent(Home.this, SearchAllActivity.class);
                            startActivity(intent);
                            break;
                        case "Lead Count":

                            // case "Submenu1":
                            intent = new Intent(Home.this, LeadCount.class);
                            startActivity(intent);
                            break;

                        case "Reallocation Report":

                            // case "Submenu1":
                            intent = new Intent(Home.this, AllocationReport.class);
                            startActivity(intent);
                            break;
                        case "First Call Report":
                            // case "Submenu1":
                            intent = new Intent(Home.this, FirstCallReport.class);
                            startActivity(intent);
                            break;
                        case "Call Report":

                            // case "Submenu1":
                            intent = new Intent(Home.this, GraphReport.class);
                            intent.putExtra("ActivityName", "Call Report");
                            startActivity(intent);
                            break;

                        case "Message Report":
                            intent = new Intent(Home.this, GraphReport.class);
                            intent.putExtra("ActivityName", "Message Report");
                            startActivity(intent);
                            break;
                        case "Status Report":
                            intent = new Intent(Home.this, StatusReport.class);
                            intent.putExtra("ActivityName", "Status Report");
                            startActivity(intent);
                            break;
                        case "Status Report DataRefwise":
                            intent = new Intent(Home.this, StatusReport.class);
                            intent.putExtra("ActivityName", "Status Report DataRefwise");
                            startActivity(intent);
                            break;
                        case "Remark Report":
                            intent = new Intent(Home.this, GraphReport.class);
                            intent.putExtra("ActivityName", "Remark Report");
                            startActivity(intent);
                            break;
                        case "Point Report":
                            intent = new Intent(Home.this, GraphReport.class);
                            intent.putExtra("ActivityName", "Point Report");
                            startActivity(intent);
                            break;
                        case "Login Time Report":
                            intent = new Intent(Home.this, LastLoginDetails.class);
                            intent.putExtra("ActivityName", "Login Report");
                            startActivity(intent);
                            break;
                        case "Chat":
                            // case "Submenu1":
                            intent = new Intent(Home.this, ChatActivity.class);
                            startActivity(intent);
                            break;
                        case "Notifications":
                            intent = new Intent(Home.this, AllNotifications.class);
                            startActivity(intent);
                            break;
                        case "New Client Entry":
                                intent=new Intent(Home.this,NewClientEntry.class);
                                intent.putExtra("Activity","Home");
                                startActivity(intent);

                    }


                    view.setSelected(true);
                    if (view_Group != null) {
                        view_Group.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    view_Group = view;
                    view_Group.setBackgroundColor(Color.parseColor("#DDDDDD"));
                    drawer.closeDrawers();
                    return false;
                }


            });

            expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l)
                {

                    //Log.d("DEBUG", "heading clicked");

                    return false;
                }
            });

   /////////////////////////////////////////Navigation code previous///////////////////////////
          /*  View headerView = navigationView.getHeaderView(0);
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
            imgProfile = headerView.findViewById(R.id.imageProfile);
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                }
            });
                setMenu(true);*/
            //navigationView.setNavigationItemSelectedListener(this);

        }catch (Exception e)
        {
            Toast.makeText(Home.this,"Errorcode-116 Dashboard oncreate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }//onCreate close
    public void logoutClick()
    {
        try {
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
                    editor.putString("Id", null);
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
        }catch (Exception e)
        {
            Toast.makeText(Home.this,"Errorcode-117 Dashboard BtnLogout clicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void spinnerItemSelected()
    {
        try {
            if (isSpinnerTouched) {

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
                final AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
                //Adding our dialog box to the view of alert dialog
                alert.setView(confirmCall);
                //Creating an alert dialog
                alertRefName = alert.create();
                //if (++check > 1) {
                alertRefName.show();
                // }

                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (pos == 0) {

                            editor = sp.edit();
                            editor.putString("DtaFrom", "0");
                            editor.commit();
                            dialog = ProgressDialog.show(Home.this, "", "Loading All Data...", true);
                            alertRefName.dismiss();
                            //getStatusCount();
                            if (CheckInternetSpeed.checkInternet(Home.this).contains("0")) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
                            } else if (CheckInternetSpeed.checkInternet(Home.this).contains("1")) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
                            } else {
                                getAllRefName();
                            }

                            //   refreshWhenLoading();


                        } else {
                            alertRefName.dismiss();
                            int p = pos - 1;

                            // DataReference dataReference1=arrayListRefrences.get(position);
                            dataFrom1 = arrayListRefId.get(p);
                            Log.d("RefId", dataFrom1);
                            editor = sp.edit();
                            editor.putString("DtaFrom", dataFrom1);
                            editor.commit();
                            dialog = ProgressDialog.show(Home.this, "", "Loading Refnamewise data...", true);
                            alertRefName.dismiss();
                            if (CheckInternetSpeed.checkInternet(Home.this).contains("0")) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
                            } else if (CheckInternetSpeed.checkInternet(Home.this).contains("1")) {
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
                            } else {
                                getRestRefName();
                            }

                            // refreshWhenLoading();
                              /*  }
                            }).start();*/
                        }
                    }
                });

                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertRefName.dismiss();
                    }
                });
            }
        }catch (Exception e)
        {
            Toast.makeText(Home.this,"Errorcode-118 Dashboard SpinnerItemSelected clicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshWhenLoading()
    {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if(dialog.isShowing()) {
                    Intent intent = new Intent(Home.this, Home.class);
                    intent.putExtra("Activity",activityname);
                    startActivity(intent);// when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }
        }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

    }


    //-----------------------------------Calls--------------------------------------------

    @SuppressLint("NewApi")
    public void getCallDetails() {
            try {
                if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_CALL_LOG)) {
                        ActivityCompat.requestPermissions(Home.this, new String[]
                                {Manifest.permission.READ_CALL_LOG}, 1);
                    } else {
                        ActivityCompat.requestPermissions(Home.this, new String[]
                                {Manifest.permission.READ_CALL_LOG}, 1);
                    }
                } else {
                    if (CheckInternetSpeed.checkInternet(Home.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
                    } else if (CheckInternetSpeed.checkInternet(Home.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
                    } else {
                        getCallLogDetails();
                    }

                }
            }catch (Exception e)
            {
                Toast.makeText(Home.this,"Errorcode-119 Dashboard GetCallDetails "+e.toString(),Toast.LENGTH_SHORT).show();
            }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCallLogDetails() {
            try {
                Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                        null, null, null);
                int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
                int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
                int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
                int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
                int phAccID = managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);

                mCallArrayList = new ArrayList<>();
                mUrlCallArrayList = new ArrayList<>();
                mUploadCallArrayList = new ArrayList<>();
                sdfComapre = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");

                while (managedCursor.moveToNext()) {
                    callPhoneno = managedCursor.getString(number);
                    String callType = managedCursor.getString(type);
                    String callDate = managedCursor.getString(date);
                    Date callDayTime = new Date(Long.valueOf(callDate));
                    callDuration = managedCursor.getString(duration);
                    callphAccID = managedCursor.getString(phAccID);
                    finalCallType = null;

                    int dircode = Integer.parseInt(callType);
                    switch (dircode) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            finalCallType = "OUTGOING";
                            break;

                        case CallLog.Calls.INCOMING_TYPE:
                            finalCallType = "INCOMING";
                            break;

                        case CallLog.Calls.MISSED_TYPE:
                            finalCallType = "MISSED";
                            break;
                    }
                    sdfSaveArray = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
                    strdateFormated = sdfSaveArray.format(callDayTime);

                    callDetialswithIMEI = new CallDetialswithIMEI(callPhoneno, finalCallType, strdateFormated, callDuration, IMEINumber1, IMEINumber2, callphAccID);
                    mCallArrayList.add(callDetialswithIMEI);
                }
                Log.d("callarraysize", mCallArrayList.size() + "");

                if(CheckServer.isServerReachable(Home.this)) {
                    requestQueue = Volley.newRequestQueue(Home.this);
                    urlCallDate = clienturl + "?clientid=" + clientid + "&caseid=57&IMEI=" + IMEINumber1;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCallDate, null,
                            new callSuccess(), new callFail());
                    Log.d("UrlLastCallDate", urlCallDate);

                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(jsonObjectRequest);
                }
                else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                    alertDialogBuilder.setTitle("Server Down!!!!")
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
                Toast.makeText(Home.this,"Errorcode-120 Dashboard CallLogDetails "+e.toString(),Toast.LENGTH_SHORT).show();
            }
    }

    public void uploadCalls(String callPhoneno, String finalCallType, final String strdateFormated,
                            String callDuration, final String callIMEI1, String callIMEI2, String callphAccID)
    {
        try {
            if(CheckServer.isServerReachable(Home.this)) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(Home.this);
                } else {
                    String strurl = clienturl + "?clientid=" + clientid + "&caseid=53&MobileNo=" + callPhoneno +
                            "&CallType=" + finalCallType + "&CallDate=" + strdateFormated + "&CallDuration=" + callDuration +
                            "&IMEI1=" + callIMEI1 + "&IMEI2=" + callIMEI2 + "&PhoneAccountId=" + callphAccID;
                    Log.d("CallUrl", strurl);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("UploadCallResponse", response);
                   /* if (response.contains("Data inserted successfully")) {
                        Toast.makeText(Home.this, "Record inserted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Home.this, "Record not inserted", Toast.LENGTH_SHORT).show();
                    }*/
                            counturl++;
                            Log.d("url count", counturl + " / " + "&CallDate=" + strdateFormated + " / " + response + " / " + callIMEI1);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("VolleyError", String.valueOf(error));
                        }
                    });

                    stringRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    requestQueue.add(stringRequest);
                }
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(Home.this,"Errorcode-121 Dashboard UploadCallLog "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private class callSuccess implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try{
            Log.e("success", String.valueOf(response));
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    JSONObject lastdate = jsonObject.getJSONObject("dtCallDate");
                    String mlastdate = lastdate.getString("date");

                    Log.d("lastdate", mlastdate);
                    callDetailsFetch = new CallDetailsFetch(mlastdate);
                    mUrlCallArrayList.add(callDetailsFetch);
                }
                Log.d("urlarrlen", mUrlCallArrayList.size() + "");
                for (int i = 0; i < mCallArrayList.size(); i++) {
                    String clno = mCallArrayList.get(i).getmCallMobileNo();
                    String cltype = mCallArrayList.get(i).getmCallType();
                    String cldate = mCallArrayList.get(i).getmCallDate();
                    String clduration = mCallArrayList.get(i).getmCallDuration();
                    String climei1 = mCallArrayList.get(i).getmIMEI1();
                    String climei2 = mCallArrayList.get(i).getmIMEI2();
                    String clphAccID = mCallArrayList.get(i).getmphAccID();

                    if (mUrlCallArrayList.size() == 0) {
                        uploadCalls(clno, cltype, cldate, clduration, climei1, climei2, clphAccID);
                    } else {
                        String strCompareRecord = mUrlCallArrayList.get(mUrlCallArrayList.size() - 1).getmfthCallDate();
                        Date dtMydate = sdfComapre.parse(strCompareRecord);
                        String strUrlDate = sdfComapre.format(dtMydate);

                        if (strUrlDate.compareTo(cldate) < 0) {
                            Log.d("datematch", strUrlDate + " / " + cldate);
                            callDetialswithIMEI = new CallDetialswithIMEI(clno, cltype, cldate, clduration, climei1, climei2, clphAccID);
                            mUploadCallArrayList.add(callDetialswithIMEI);
                        }
                    }
                }
                Log.d("recordscall", "call upload without date");
                Log.d("uploadarraysizecall", mUploadCallArrayList.size() + "");

            for (int i = 0; i < mUploadCallArrayList.size(); i++) {
                String clno = mUploadCallArrayList.get(i).getmCallMobileNo();
                String cltype = mUploadCallArrayList.get(i).getmCallType();
                String cldate = mUploadCallArrayList.get(i).getmCallDate();
                String clduration = mUploadCallArrayList.get(i).getmCallDuration();
                String climei1 = mUploadCallArrayList.get(i).getmIMEI1();
                String climei2 = mUploadCallArrayList.get(i).getmIMEI2();
                String clphAccID = mUploadCallArrayList.get(i).getmphAccID();

                uploadCalls(clno, cltype, cldate, clduration, climei1, climei2, clphAccID);
            }
            Log.d("recordscall", "call upload with date");
            }  catch (Exception e) {
                Toast.makeText(Home.this,"Errorcode-122 Dashboard UploadCallResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                //  e.printStackTrace();
            }
        }
    }

    private class callFail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(Home.this,"Errorcode-123 Dashboard CallLogDetails Fail "+error.toString(),Toast.LENGTH_SHORT).show();
            Log.e("Fail", String.valueOf(error));
        }
    }


    //-----------------------------------SMS-------------------------------------------------
    @SuppressLint("NewApi")
    public void getSMSDetails() {
        try {
            if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_SMS)) {
                    ActivityCompat.requestPermissions(Home.this, new String[]
                            {Manifest.permission.READ_SMS}, 2);
                } else {
                    ActivityCompat.requestPermissions(Home.this, new String[]
                            {Manifest.permission.READ_SMS}, 2);
                }
            } else {
                getSMSLogDetails();
            }
        }catch (Exception e)
        {
            Toast.makeText(Home.this,"Errorcode-123 Dashboard SmsDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getSMSLogDetails() {
        try {
            Uri uri = Uri.parse("content://sms");
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            mSMSArrayList = new ArrayList<>();
            mUrlSMSArrayList = new ArrayList<>();
            mUploadSMSArrayList = new ArrayList<>();
            sdfComapre = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");

            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    String smsbody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    String phoneno = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    Date smsDayTime = new Date(Long.valueOf(date));
                    String smstype = cursor.getString(cursor.getColumnIndexOrThrow("type"));

                    String typeOfSMS = null;
                    switch (Integer.parseInt(smstype)) {
                        case 1:
                            typeOfSMS = "INBOX";
                            break;

                        case 2:
                            typeOfSMS = "SENT";
                            break;

                        case 3:
                            typeOfSMS = "DRAFT";
                            break;
                    }
                    sdfSaveArray = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strdateFormated = sdfSaveArray.format(smsDayTime);

                    smsDetails = new SMSDetails(phoneno, typeOfSMS, strdateFormated, smsbody, IMEINumber1, IMEINumber2);
                    mSMSArrayList.add(smsDetails);
                    cursor.moveToNext();
                }
            }
            Log.d("smsarraylist", String.valueOf(mSMSArrayList.size()));
            // txtttlsmsrecord.setText(mSMSArrayList.size()+"");
            if(CheckServer.isServerReachable(Home.this)) {
                requestQueue = Volley.newRequestQueue(Home.this);
                urlSMSDate = clienturl + "?clientid=" + clientid + "&caseid=58&IMEI=" + IMEINumber1;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSMSDate, null,
                        new smsSuccess(), new smsFail());
                Log.d("Lastsms", urlSMSDate);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(Home.this,"Errorcode-124 Dashboard SmsLogDetails "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadSMS(String smsPhoneno, String finalsmsType, final String strdateFormated,
                          String smsBody, String smsIMEI1, String smsIMEI2) {
        try {
            if(CheckServer.isServerReachable(Home.this)) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(Home.this);
                } else {
                    String strurl = clienturl + "?clientid=" + clientid + "&caseid=54&MobileNo=" + smsPhoneno +
                            "&SMSType=" + finalsmsType + "&SMSDate=" + strdateFormated + "&SMSBODY=" + smsBody +
                            "&IMEI1=" + smsIMEI1 + "&IMEI2=" + smsIMEI2;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("ChatResponse", response);
                            if (response.contains("SMS inserted successfully")) {
                                //Toast.makeText(MainActivity.this, "Record inserted successfully", Toast.LENGTH_SHORT).show();
                            }
                            counturl++;
                            Log.d("url count", counturl + " / " + "&SMSDate=" + strdateFormated + " / " + response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("VolleyError", String.valueOf(error));
                        }
                    });

                    stringRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                }
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(Home.this,"Errorcode-125 Dashboard UploadSmsLog"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void displayNotification(){
        try {
            clickcount = clickcount + 1;
            AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
            LayoutInflater li = LayoutInflater.from(Home.this);
            //Creating a view to get the dialog box
            View confirmCall = li.inflate(R.layout.layout_notification, null);
            recyclerViewNotification = confirmCall.findViewById(R.id.recyclerNotification);
            alert.setView(confirmCall);
            alertDialog = alert.create();

            if (clickcount % 1 == 0) {
                //  alert.setView(confirmCall);

                LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewNotification.setLayoutManager(layoutManager);
                //adapterNotification=new AdapterNotification(Home.this,arrayListNotification);
                recyclerViewNotification.setAdapter(adapterNotification);

                //Adding our dialog box to the view of alert dialog

                //Creating an alert dialog

                alertDialog.show();
            } else {
                alertDialog.dismiss();
            }
        }catch (Exception e)
        {
            Toast.makeText(Home.this,"Errorcode-126 Dashboard DisplayNotification "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private class smsSuccess implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                Log.d("success", urlSMSDate);
                Log.e("success", String.valueOf(response));

                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("dtSMSDate");
                    String mlastdate = jsonObject1.getString("date");

                    Log.d("lastdate", mlastdate);
                    smsDetailsFetch = new SMSDetailsFetch(mlastdate);
                    mUrlSMSArrayList.add(smsDetailsFetch);

                    // txtsmslastdate.setText(mlastdate+"");
                }
                Log.d("urlarrlensms", mUrlSMSArrayList.size() + "");

                Collections.reverse(mSMSArrayList);
                for (int i = 0; i < mSMSArrayList.size(); i++) {
                    String smsno = mSMSArrayList.get(i).getmSMSMobileNo();
                    String smstype = mSMSArrayList.get(i).getmSMStype();
                    String smsdate = mSMSArrayList.get(i).getmSMSdate();
                    String smsbody = mSMSArrayList.get(i).getmSMSbody();
                    String smsimei1 = mSMSArrayList.get(i).getmSMSIMEI1();
                    String smsimei2 = mSMSArrayList.get(i).getmSMSIMEI2();

                    if (mUrlSMSArrayList.size() == 0) {
                        uploadSMS(smsno, smstype, smsdate, smsbody, smsimei1, smsimei2);
                    } else {
                        String strCompareRecord = mUrlSMSArrayList.get(mUrlSMSArrayList.size() - 1).getmfthSMSDate();
                        Date dtMydate = sdfComapre.parse(strCompareRecord);
                        String strUrlDate = sdfComapre.format(dtMydate).substring(0, 17);
                        Log.d("ddd", strUrlDate);
                        strUrlDate = strUrlDate + sdfComapre.format(dtMydate).substring(18, 20);
                        Log.d("ddd1", strUrlDate);
                        if (strUrlDate.compareTo(smsdate) < 0) {
                            Log.d("datematch", strUrlDate + " / " + smsdate);
                            smsDetails = new SMSDetails(smsno, smstype, smsdate, smsbody, smsimei1, smsimei2);
                            mUploadSMSArrayList.add(smsDetails);
                        }
                    }
                }
                Log.d("uploadsmsarraysize", mUploadSMSArrayList.size() + "");
                //   txtnewsmsrecords.setText(mUploadSMSArrayList.size() + "");


                for (int i = 0; i < mUploadSMSArrayList.size(); i++) {
                    String smsno = mUploadSMSArrayList.get(i).getmSMSMobileNo();
                    String smstype = mUploadSMSArrayList.get(i).getmSMStype();
                    String smsdate = mUploadSMSArrayList.get(i).getmSMSdate();
                    String smsbody = mUploadSMSArrayList.get(i).getmSMSbody();
                    String smsimei1 = mUploadSMSArrayList.get(i).getmSMSIMEI1();
                    String smsimei2 = mUploadSMSArrayList.get(i).getmSMSIMEI2();
                    Log.d("recordsms", smsbody);
                    uploadSMS(smsno, smstype, smsdate, smsbody, smsimei1, smsimei2);
                }
                Toast.makeText(Home.this, "SMS Records Upload Successfully.", Toast.LENGTH_SHORT).show();
                Log.d("recordsms", "sms upload");
            } catch (Exception e) {
                Toast.makeText(Home.this, "Errorcode-127 Dashboard UploadSmsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private class smsFail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(Home.this,"Errorcode-128 Dashboard UploadSms fail "+error.toString(),Toast.LENGTH_SHORT).show();
            Log.e("Fail", String.valueOf(error));
        }
    }

    private void prepareListData() {
        try {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            // Adding data header
            listDataHeader.add("Lead Management");
            listDataHeader.add("Document Management");
            listDataHeader.add("Reports");
            //listDataHeader.add("Logout");
            // Adding child data
            List<String> heading1 = new ArrayList<String>();
            heading1.add("Online Leads");
            heading1.add("Dashboard");
            heading1.add("New Lead");
            heading1.add("Open Leads");
            heading1.add("Form Filled");
            heading1.add("Reminder");
            heading1.add("SMS Template");
            // heading1.add("Mail Template");
            heading1.add("Search");
            heading1.add("Chat");


            //  heading1.add("Send Bulk SMS");
            List<String> heading3 = new ArrayList<String>();
            //  heading3.add("New Client Entry");
            heading3.add("Master Entry");

            List<String> heading2 = new ArrayList<String>();
            heading2.add("Lead Count");
            heading2.add("Total Calls Made");
            heading2.add("Reallocation Report");
            heading2.add("Converted Online Lead");
            heading2.add("First Call Report");
            heading2.add("Call Report");
            heading2.add("Message Report");
            heading2.add("Status Report");
            heading2.add("Status Report DataRefwise");
            heading2.add("Remark Report");
            heading2.add("Point Report");
            heading2.add("Login Time Report");
            heading2.add("Notifications");
            heading2.add("Upload File");

            listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data
            listDataChild.put(listDataHeader.get(1), heading3);
            listDataChild.put(listDataHeader.get(2), heading2);
        }catch (Exception e)
        {
            Toast.makeText(Home.this,"Errorcode-129 Dashboard NavigationMenu "+e.toString(),Toast.LENGTH_SHORT).show();
        }
        }
        private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        try {
                            menuItem.setChecked(true);
                            mDrawerLayout.closeDrawers();

                        }catch (Exception e)
                        {
                            Toast.makeText(Home.this,"Errorcode-130 Dashboard NavigationItemSelected "+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                Bitmap bitmap = null;
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imgProfile.setImageBitmap(bitmap);
                String path=selectedImage.getPath();
                Log.d("Photo Path",path);
                uploadFilePath=path.substring(path.indexOf("/raw")+6,path.lastIndexOf("/")+1);
                Log.d("File Path",uploadFilePath);
                uploadFileName=path.substring(path.lastIndexOf("/")+1);
                //uploadFileName=id;
                Log.d("File Name",uploadFileName);
              //  uploadFile(uploadFilePath+""+uploadFileName);
                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // Toast.makeText(CounselorContactActivity.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                            }
                        });

                        uploadFile(uploadFilePath + "" + uploadFileName);
                       // insertCallInfo();

                    }
                }).start();

            }  catch (Exception e) {
                // TODO Auto-generated catch block
                Toast.makeText(Home.this,"Errorcode-131 Dashboard OnActivityResult "+e.toString(),Toast.LENGTH_SHORT).show();
                Log.d("Exception", String.valueOf(e));
            }
        }
    }
    public void getNotification() {
        //dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);
        try {
            if(CheckServer.isServerReachable(Home.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=63&CounselorID=" + counselorid;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("getNotificationRes", response);
                                try {
                                    if (response.contains("[]")) {
                                        imgNotification.setVisibility(View.VISIBLE);
                                        imgNotification1.setVisibility(View.GONE);
                                    } else {
                                        imgNotification.setVisibility(View.GONE);
                                        imgNotification1.setVisibility(View.VISIBLE);
                                    }
                                    JSONObject jsonObject = new JSONObject(response);
                                    // Log.d("Json",jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String notification = jsonObject1.getString("cNotification");
                                        String srno = jsonObject1.getString("nSrNo");
                                        String notificationId = jsonObject1.getString("nNotificationID");
                                        DataNotification dataNotification = new DataNotification(notification, srno, notificationId);
                                        arrayListNotification.add(dataNotification);
                                    }
                                    adapterNotification = new AdapterNotification(Home.this, arrayListNotification);
                                    adapterNotification.notifyDataSetChanged();
                                    Log.d("ArraylistNotification", String.valueOf(arrayListNotification.size()));
                                    for (int i = 0; i < arrayListNotification.size(); i++) {
                                        Log.d("entered", "Notification");
                                        dataNotification = arrayListNotification.get(i);
                                        createNotification(dataNotification, Home.this);
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(Home.this, "Errorcode-133 Dashboard GetNotificationResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Log.d("Exception", String.valueOf(e));
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
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    // responsecode1="ServerError";
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Home.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(Home.this,"Errorcode-132 Dashboard getNotification "+e.toString(),Toast.LENGTH_SHORT).show();
        }

        }


    public void createNotification(DataNotification aMessage, Context context) {
        try {
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            final int NOTIFY_ID = m; // ID of notification
            String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
            String title = context.getString(R.string.default_notification_channel_title); // Default Channel
            Intent intent;
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;
            final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (notificationManager == null) {
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notificationManager.getNotificationChannel(id);

                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{50, 0, 0, 0, 0, 0, 0, 0, 0});
                    notificationManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(context, id);
                if (aMessage.getStrNotificaion().contains("Reminder")) {
                    // editor.putString("SelectedSrNo",dataNotification.getSrno());
                    //editor.commit();
                    intent = new Intent(context, ReminderActivity.class);
                    // intent.putExtra("ActivityName", "Home");
                } else if (aMessage.getStrNotificaion().contains("_Client_uploaded") || aMessage.getStrNotificaion().contains("_Client_updated")) {
                    intent = new Intent(context, MasterEntry.class);
                    // startActivity(intent);
                } else {
                    editor.putString("SelectedSrNo", dataNotification.getSrno());
                    Log.d("SrNo***", aMessage.getSrno());
                    editor.putString("ActivityContact", "Home1");
                    editor.commit();
                    intent = new Intent(context, Home.class);
                    // intent.putExtra("SelectedSrNo", aMessage.getSrno());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                builder.setContentTitle(aMessage.getStrNotificaion())                            // required
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                        .setContentText(context.getString(R.string.app_name)) // required
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(aMessage.getStrNotificaion())
                        .setSound(soundUri)
                        .setVibrate(new long[]{100, 100, 100, 100, 100, 100, 100, 100, 100});
            } else {
                builder = new NotificationCompat.Builder(context, id);
                if (aMessage.getStrNotificaion().contains("Reminder")) {
                    // editor.putString("SelectedSrNo",aMessage.getSrno());
                    //editor.commit();
                    intent = new Intent(context, ReminderActivity.class);
                    //  intent.putExtra("ActivityName", "Home");
                } else if (aMessage.getStrNotificaion().contains("Client_uploaded") || aMessage.getStrNotificaion().contains("_Client_updated")) {
                    intent = new Intent(Home.this, MasterEntry.class);
                    startActivity(intent);
                } else {
                    editor.putString("SelectedSrNo", aMessage.getSrno());
                    Log.d("SrNo***", aMessage.getSrno());
                    editor.putString("ActivityContact", "Home1");
                    editor.commit();
                    intent = new Intent(context, Home.class);
                    //intent.putExtra("SelectedSrNo", aMessage.getSrno());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                builder.setContentTitle(aMessage.getStrNotificaion())                            // required
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                        .setContentText(context.getString(R.string.app_name)) // required
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(aMessage.getStrNotificaion())
                        .setSound(soundUri)
                        .setVibrate(new long[]{100, 100, 100, 100, 100, 100, 100, 100, 100});
            }
            notificationManager.notify(NOTIFY_ID, builder.build());
        }catch (Exception e)
        {
            Toast.makeText(Home.this,"Errorcode-134 Dashboard createNotification "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void getReminderNotification() {
        try {
            //dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);
            if (CheckInternet.checkInternet(Home.this)) {
                if (CheckServer.isServerReachable(Home.this)) {
                    String urlNotification = clienturl + "?clientid=" + clientid + "&caseid=65&CounselorID=" + counselorid;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlNotification,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("*******", response.toString());
                                    try {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Log.d("reminderNotificationRes", response);
                                        if (response.contains("[]")) {
                                            imgNotification.setVisibility(View.VISIBLE);
                                            imgNotification1.setVisibility(View.GONE);
                                        } else {
                                            imgNotification.setVisibility(View.GONE);
                                            imgNotification1.setVisibility(View.VISIBLE);
                                        }
                                        JSONObject jsonObject = new JSONObject(response);
                                        // Log.d("Json",jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String notification = jsonObject1.getString("cNotification");
                                            String srno = jsonObject1.getString("nSrNo");
                                            String nid = jsonObject1.getString("nNotificationID");
                                            DataNotification dataNotification = new DataNotification(notification, srno, nid);
                                            arrayListNotification.add(dataNotification);
                                        }
                                        adapterNotification = new AdapterNotification(Home.this, arrayListNotification);
                                        adapterNotification.notifyDataSetChanged();
                                        Log.d("ArraylistNotification", String.valueOf(arrayListNotification.size()));

                                    } catch (Exception e) {
                                        Toast.makeText(Home.this, "Errorcode-136 Dashboard ReminderNotificationResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Log.d("Exception", String.valueOf(e));
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
                                    if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                        // responsecode1="ServerError";
                                        dialog.dismiss();
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                                        alertDialogBuilder.setTitle("Server Error!!!")
                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //insertIMEI();
                                                        //edtName.setText("");
                                                        //edtPassword.setText("");
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        Toast.makeText(Home.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(Home.this,"Errorcode-135 Dashboard getReminderNotification "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    public void lastLoginDetails() {
        //dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);
    try {
        if(CheckServer.isServerReachable(Home.this)) {
            url = clienturl + "?clientid=" + clientid + "&caseid=14&CounsellorId=" + counselorid;
            arrayListTotal = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("*******", response.toString());
                            try {
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
                            } catch (Exception e) {
                                Toast.makeText(Home.this, "Errorcode-138 Dashboard LastLoginDetails " + e.toString(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Log.d("Exception", String.valueOf(e));
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
                            if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                // responsecode1="ServerError";
                                dialog.dismiss();
                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                                alertDialogBuilder.setTitle("Server Error!!!")


                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        }).show();
                                Toast.makeText(Home.this, "Server Error", Toast.LENGTH_SHORT).show();
                                // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                            }

                        }
                    });
            requestQueue.add(stringRequest);
        }else {
            dialog.dismiss();
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
            alertDialogBuilder.setTitle("Server Down!!!!")
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
        Toast.makeText(Home.this,"Errorcode-137 Dashboard lastLoginDetails "+e.toString(),Toast.LENGTH_SHORT).show();
    }
    }

    public void getRefName() {
        try {
            if(CheckServer.isServerReachable(Home.this)) {
                // arrayListRefrences=new ArrayList<>();
                arrayList1 = new ArrayList<>();
                arrayListRefId = new ArrayList<>();
                arrayList1.add(0, "All");
                url = clienturl + "?clientid=" + clientid + "&caseid=11&CounsellorId=" + counselorid;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Log.d("RefNameUrl", response);
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
                                    getCallDetails();
                                    getSMSDetails();
                                    getReminderNotification();
                                    getNotification();
                                    lastLoginDetails();
                                    getPointCollection();

                                    if (activityname.contains("CounsellorData")) {
                                        String dtfrom1 = getIntent().getStringExtra("RefName");
                                        Log.d(":From***", dtfrom1);
                                        for (int i = 0; i < arrayList1.size(); i++) {
                                            if (arrayList1.get(i).matches(dtfrom1)) {
                                                spinnerRef.setSelection(i);
                                                pos = spinnerRef.getSelectedItemPosition();
                                            }
                                        }

                                        //pos = position;


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
                                        final AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
                                        //Adding our dialog box to the view of alert dialog
                                        alert.setView(confirmCall);
                                        //Creating an alert dialog
                                        alertRefName = alert.create();
                                        //if (++check > 1) {
                                        alertRefName.show();
//                         }
                                        txtYes.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (pos == 0) {
                                                    editor = sp.edit();
                                                    editor.putString("DtaFrom", "0");
                                                    editor.commit();
                                                    dialog = ProgressDialog.show(Home.this, "", "Loading all data...", true);
                                                    alertRefName.dismiss();
                                                    //getStatusCount();
                                                    getAllRefName();


                                                } else {
                                                    alertRefName.dismiss();
                                                    int p = pos - 1;

                                                    // DataReference dataReference1=arrayListRefrences.get(position);
                                                    dataFrom1 = arrayListRefId.get(p);
                                                    Log.d("RefId", dataFrom1);
                                                    editor = sp.edit();
                                                    editor.putString("DtaFrom", dataFrom1);
                                                    editor.commit();
                                                    dialog = ProgressDialog.show(Home.this, "", "Loading data refnamewise...", true);
                           /* new Thread(new Runnable() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            //  Toast.makeText(Home.this,"uploading started.....",Toast.LENGTH_SHORT).show();
                                        }
                                    });*/

                                                    //getStatusCount();
                                                    alertRefName.dismiss();
                                                    getRestRefName();
                              /*  }
                            }).start();*/

                                                }
                                            }
                                        });

                                        txtNo.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertRefName.dismiss();
                                                if (++check == 1) {
                                                    spinnerRef.setSelection(0);
                                                    getAllRefName();
                                                }
                                            }
                                        });
                                    } else {
                                        pos = 0;
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
                                        final AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
                                        //Adding our dialog box to the view of alert dialog
                                        alert.setView(confirmCall);
                                        //Creating an alert dialog
                                        alertRefName = alert.create();
                                        //if (++check > 1) {
                                        alertRefName.show();
//                         }

                                        txtYes.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (pos == 0) {
                                                    editor = sp.edit();
                                                    editor.putString("DtaFrom", "0");
                                                    editor.commit();
                                                    dialog = ProgressDialog.show(Home.this, "", "Loading all data...", true);
                                                    alertRefName.dismiss();
                                                    //getStatusCount();
                                                    getAllRefName();


                                                } else {
                                                    alertRefName.dismiss();
                                                    int p = pos - 1;

                                                    // DataReference dataReference1=arrayListRefrences.get(position);
                                                    dataFrom1 = arrayListRefId.get(p);
                                                    Log.d("RefId", dataFrom1);
                                                    editor = sp.edit();
                                                    editor.putString("DtaFrom", dataFrom1);
                                                    editor.commit();
                                                    dialog = ProgressDialog.show(Home.this, "", "Loading data refnamewise...", true);


                                                    //getStatusCount();
                                                    alertRefName.dismiss();
                                                    getRestRefName();

                                                }


                                            }
                                        });

                                        txtNo.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertRefName.dismiss();
                                                if (++check == 1) {
                                                    spinnerRef.setSelection(0);
                                                    getAllRefName();
                                                }
                                            }
                                        });
                                    }
                                    arrayAdapter.notifyDataSetChanged();

                                    Log.d("RefIdSize", String.valueOf(arrayListRefId.size()));
                                } catch (JSONException e) {
                                    Toast.makeText(Home.this, "Errorcode-140 Dashboard getRefResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("RefException", String.valueOf(e));
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
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    // responsecode1="ServerError";
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Home.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(Home.this,"Errorcode-139 Dashboard getRefNames "+e.toString(),Toast.LENGTH_SHORT).show();
        }
        }

        public void getAllRefName() {
        try {
            if(CheckServer.isServerReachable(Home.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=12&CounsellorId=" + counselorid;
            /*if(CheckInternet.checkInternet(Home.this))
            {*/
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                alertRefName.dismiss();
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
                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    Toast.makeText(Home.this, "Errorcode-142 Dashboard AllRefNamesResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("AllRefException", String.valueOf(e));
                                    //  e.printStackTrace();
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
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    // responsecode1="ServerError";
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Home.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(Home.this,"Errorcode-141 Dashboard getAllRefNames "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void getRestRefName() {
        try {
            if(CheckServer.isServerReachable(Home.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=13&CounsellorId=" + counselorid + "&DataFrom=" + dataFrom1;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                if (alertRefName.isShowing()) {
                                    alertRefName.dismiss();
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
                                    Toast.makeText(Home.this, "Errorcode-144 Dashboard RestRefResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Log.d("RestRefException", String.valueOf(e));
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
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    // responsecode1="ServerError";
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Home.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }
                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(Home.this,"Errorcode-143 Dashboard getRestRefName "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void getPointCollection() {
        try {
            if(CheckServer.isServerReachable(Home.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=37&nCounsellorId=" + counselorid;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

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
                                        editor.putString("TotalCoin", totalcoin);
                                        editor.commit();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(Home.this, "Errorcode-146 Dashboard PointCollectionResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Log.d("PointCollectException", String.valueOf(e));
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
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    // responsecode1="ServerError";
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Home.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Server Down!!!!")
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
            Toast.makeText(Home.this,"Errorcode-145 Dashboard getPointCollection "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        try {
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
        }catch (Exception e)
        {
            Toast.makeText(Home.this,"Errorcode-147 Dashboard OnBackpressed "+e.toString(),Toast.LENGTH_SHORT).show();        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            setMenu(false);

            // Handle the camera action
        }else if (id == R.id.nav_onlinelead) {

            Intent intent = new Intent(Home.this, CounsellorData.class);
            intent.putExtra("Activity", "OnlineLead");
            startActivity(intent);
            // Handle the camera action
        }

        else if (id == R.id.nav_upload) {
            setMenu(false);
            Intent intent = new Intent(Home.this, FileListActivity.class);
            intent.putExtra("ActivityName", "Home");
            startActivity(intent);

        }else if(id==R.id.nav_reminder)
        {
            setMenu(false);
            startActivity(new Intent(Home.this,ReminderActivity.class));
        }
        else if (id == R.id.nav_chat) {
            setMenu(false);
            Intent intent = new Intent(Home.this, ChatActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_totalCallMade) {
            setMenu(false);
            Intent intent = new Intent(Home.this, TotalCallMade.class);
            startActivity(intent);

        } else if (id == R.id.nav_template) {
            setMenu(false);

            Intent intent = new Intent(Home.this, MessageTemplate.class);
            startActivity(intent);

        } /*else if (id == R.id.nav_newcliententry)
        {
            startActivity(new Intent(Home.this,NewClientEntry.class));

        }*/ else if (id == R.id.nav_logout) {

            setMenu(false);
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

        } else if(id==R.id.nav_newlead)
        {
            setMenu(false);
            Intent intent= new Intent(Home.this,EditDetails.class);
            intent.putExtra("ActivityName","NewLead");
            startActivity(intent);
        }
        else if(id==R.id.nav_search)
        {
            setMenu(false);
            startActivity(new Intent(Home.this,SearchAllActivity.class));
        }
        else if(id==R.id.nav_report)
        {
            setMenu(true);
           /* getDrawerToggleDelegate().isNavigationVisible();
            if(navigationView.getVisibility(false))
            {

            }*/
           // navigationView.getMenu().setGroupVisible(R.id.group_report, true);
          /*  Intent intent = new Intent(Home.this, ReportActivity.class);
            startActivity(intent);*/
                //id=R.id.nav_reallocationreport
        }
        else if (id == R.id.nav_reallocationreport) {

            Intent intent = new Intent(Home.this, AllocationReport.class);
            intent.putExtra("ActivityName","Reallocation Report");
            startActivity(intent);
        }
        else if (id == R.id.nav_logintimereport) {

            Intent intent = new Intent(Home.this, LastLoginDetails.class);
            intent.putExtra("ActivityName","Call Report");
            startActivity(intent);
        }
        else if (id == R.id.nav_callreport) {

            Intent intent = new Intent(Home.this, GraphReport.class);
            intent.putExtra("ActivityName","Call Report");
            startActivity(intent);
        }
        else if(id==R.id.nav_msgreport)
        {
           // setMenu(false);
            Intent intent=new Intent(Home.this,GraphReport.class);
            intent.putExtra("ActivityName","Message Report");
            startActivity(intent);
        } else if (id == R.id.nav_pointreport) {

            Intent intent = new Intent(Home.this, GraphReport.class);
            intent.putExtra("ActivityName","Point Report");
            startActivity(intent);
        }
        else if (id == R.id.nav_recordingreport) {

            Intent intent = new Intent(Home.this, GraphReport.class);
            intent.putExtra("ActivityName","Recording Report");
            startActivity(intent);
        }
        else if (id == R.id.nav_statusreport) {

            Intent intent = new Intent(Home.this, GraphReport.class);
            intent.putExtra("ActivityName","Status Report");
            startActivity(intent);
        }
        else if (id == R.id.nav_remarksreport) {

            Intent intent = new Intent(Home.this, GraphReport.class);
            intent.putExtra("ActivityName","Remark Report");
            startActivity(intent);
        }
        else if(id==R.id.nav_allrecords)
        {
            setMenu(false);
            startActivity(new Intent(Home.this,AllRecords.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setMenu(boolean mode)
    {
        if(mode==false)
        {
            mode=true;
        }
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_reallocationreport).setVisible(mode);
        nav_Menu.findItem(R.id.nav_callreport).setVisible(mode);
        nav_Menu.findItem(R.id.nav_msgreport).setVisible(mode);

        nav_Menu.findItem(R.id.nav_pointreport).setVisible(mode);
        nav_Menu.findItem(R.id.nav_statusreport).setVisible(mode);
        nav_Menu.findItem(R.id.nav_remarksreport).setVisible(mode);
       // nav_Menu.findItem(R.id.nav_recordingreport).setVisible(mode);
        nav_Menu.findItem(R.id.nav_logintimereport).setVisible(mode);
       // nav_Menu.findItem(R.id.nav_editdetailsreport).setVisible(mode);

    }
    public int uploadFile(String sourceFileUri) {

        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.exists()) {
            dialog.dismiss();

            Log.d("uploadFileNotExist", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);


            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(Home.this, "Source File not exist :"
                            + uploadFilePath + "" + uploadFileName, Toast.LENGTH_SHORT).show();
                }
            });

            return 0;
        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + fileName + "" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                /*if(serverResponseCode==409)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashAfterCall.this, "File already exist.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Home.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(Home.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Uploadfiletoserver", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(Home.this, "Got Exception while uploading record ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    private boolean checkPermission() {
        int i = 0;

        String[] perm = {/*Manifest.permission.READ_PHONE_STATE,*/ Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE,Manifest.permission.READ_CALL_LOG,Manifest.permission.WRITE_CALL_LOG,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR};
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
                                .setSmallIcon(R.drawable.bizcall)
                                .setContentTitle("Call Details")
                                .setContentText("Calls Arriving / Pending => " + TotalReminderMatch + " / " + TotalReminderNotMatch)
                                .setWhen(System.currentTimeMillis())
                                .setSound(soundUri)
                                .setTimeoutAfter(2000)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}); //This sets the sound to play

                        Intent intent = new Intent(Home.this, ReminderActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(Home.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingIntent);

                        //Display notification
                        if(TotalReminderMatch!=0&&TotalReminderNotMatch!=0)
                        {
                            notificationManager.notify(0, mBuilder.build());
                        }
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



