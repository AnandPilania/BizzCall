package com.bizcall.wayto.mentebit13;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;


//import com.google.android.gms.common.api.GoogleApiClient;

public class Home extends AppCompatActivity {
    String counselorid = "", uname = "", counselorname = "", userpass = "", emailpass = "", emailid = "", role = "", mobile = "", status1 = "", statusid1 = "";
    TextView txtUName, txtCoin, txtDiamond, txtMobile, txtLogout, txtCID;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    AlertDialog alertDialog;
    int check = 0, pos;
    String activityname = "";
    ArrayList<String> arrayList1;
    NavigationView navigationView;
    Long timeout;
    Thread thread;
    FirebaseAuth mAuth;
    String latitude = "", longitude = "", date1 = "", currentDate = "", currentTime = "", time1 = "", uploadLatitude = "", uploadLongitude = "";
    GPSTracker gps;
    String uploadcalls = "", uploadsms = "";
    int matchforCall = 0, matchforsms = 0;
    Handler handler;
    String cmpULat = "", cmpULng = "", cmpLat = "", cmpLng = "";
    Toolbar toolbar;
    String uploadFilePath = "";
    String uploadFileName = "", urlSMSDate, url;
    String upLoadServerUri = null;
    int serverResponseCode = 0;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String uname1, id, statusid, sid;
    Intent intent;
    TextView mtextView, txtEditProfile;
    Button btnSubmit;
    Toast toast;
    Login login;
    ArrayList<DataStatusTotal> arrayListTotal;
    RecyclerView recyclerView;
    String loginDate = "", clienturl = "";
    String clientid = "";


    private long back_pressed = 0;

    private Handler notifyHandler = new Handler();
    int TotalReminderMatch = 0;
    int TotalReminderNotMatch = 0;
    ArrayList<DataReminder> mArrayList;
    DataReminder dataReminder;

    ImageView imgCoin, imgProfile;
    private Vibrator vibrator;
    public static final int GET_FROM_GALLERY = 1;
    View view_Group;

    ExpandableListAdapter mMenuAdapter;//For Navigationdrawer
    ExpandableListView expandableList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DrawerLayout mDrawerLayout;
    DrawerLayout drawer;

    RequestQueue requestQueue;

    //SMS ////
    String urlCallDate;
    private ArrayList<CallDetialswithIMEI> mCallArrayList;

    private SimpleDateFormat sdfSaveArray;
    String[] filePathColumn;
    int counturl = 0,isSelected=0;
    String callPhoneno, finalCallType, strdateFormated, callDuration, callphAccID;

    private ArrayList<CallDetialswithIMEI> mUploadCallArrayList;
    private ArrayList<CallDetailsFetch> mUrlCallArrayList;


    private ArrayList<SMSDetails> mSMSArrayList, mUploadSMSArrayList;
    private ArrayList<SMSDetailsFetch> mUrlSMSArrayList;


    private CallDetailsFetch callDetailsFetch;
    private SMSDetails smsDetails;
    private SMSDetailsFetch smsDetailsFetch;


    private CallDetialswithIMEI callDetialswithIMEI;
    private SimpleDateFormat sdfComapre;

    String IMEINumber1, IMEINumber2, deviceId, verifyid;
    Timer timer = new Timer();

    android.app.AlertDialog.Builder builder;
    android.app.AlertDialog alert;

    ////Notification
    RecyclerView recyclerViewNotification;
    AdapterNotification adapterNotification;
    ArrayList<DataNotification> arrayListNotification;
    ImageView imgNotification;//if no notification
    ImageView imgNotification1;//if notification come
    ImageView imgRefresh;
    int clickcount = 0;
    NotificationManager notificationManager;
    DataNotification dataNotification;

    NetworkInfo info;
    EditText edtUserName, edtUserPassword, edtEmailID, edtEmailPWD, edtMobile;
    ImageView imgPhoto;
    RequestCreator photo;
    String strImageUrl;
    int temp = 0;

    public static final String CHANNEL_ID = "push_notification";
    public static final String CHANNEL_NAME = "push_notification coding";
    public static final String CHANNEL_DESC = "push_notification description";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_home);
            requestQueue = Volley.newRequestQueue(Home.this);
            mAuth = FirebaseAuth.getInstance();

            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            //check if all permissions are allowed or not
            if (checkPermission()) {
                Toast.makeText(Home.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            }
            //request permission
            // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAPTURE_AUDIO_OUTPUT}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, 1);

            //all variables and controls initialized here
            init();

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
                dialog = ProgressDialog.show(Home.this, "", "Loading details", true);
               // Log.d("mydata", verifyid+" "+emailid+" "+mobile);
                newThreadInitilization(dialog);
               checkCounselor();
            }
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home.this, Home.class);
                    intent.putExtra("Activity", "Home");
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
                    //function is used to display top 10 notifications.
                    displayNotification();
                }
            });

            txtLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutClick();
                }
            });

            deleteCache(Home.this);
            Picasso.with(Home.this).load(strImageUrl).placeholder(R.mipmap.ic_launcher).into(imgProfile);

            navigationView.setItemIconTintList(null);
            if (navigationView != null) {
                setupDrawerContent(navigationView);
            }
            txtEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userProfileEditor();
                }
            });
            //To create list of main menus and submenus on navigation drawer.
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
                    Intent intent;
                    switch (selected) {

                        case "Other Site's Leads":
                            // case "Submenu1":
                            intent = new Intent(Home.this, OnlineLead.class);
                            intent.putExtra("ActivityLeads", "OnlineLead");
                            startActivity(intent);
                            break;
                       /* case "IVR Missed Tollfree":
                            // case "Submenu1":
                            intent = new Intent(Home.this, IVRMissedTollFree.class);
                            intent.putExtra("ActivityLeads", "IVRMissed");
                            startActivity(intent);
                            break;*/
                        case "Form Filled":
                            // case "Submenu1":
                            intent = new Intent(Home.this, FormFilled.class);
                            //intent.putExtra("Activity", "OnlineLead");
                            startActivity(intent);
                            break;
                        case "Open Leads":
                            intent = new Intent(Home.this, OpenLeads.class);
                            intent.putExtra("Activity", "OpenLead");
                            startActivity(intent);
                            break;
                        case "Dashboard":
                            // case "Submenu1":
                            intent = new Intent(Home.this, Dashboard.class);
                            intent.putExtra("Activity", selected);
                            //intent.putExtra("MainMenu",MainMenuselected);
                            startActivity(intent);
                            break;
                        case "Filter Leads":
                            intent = new Intent(Home.this, FilterLeads.class);
                            intent.putExtra("ActivityName", "NewLead");
                            startActivity(intent);
                            break;

                        case "Send Notification":
                            intent = new Intent(Home.this, ActivitySendNotify.class);
                            intent.putExtra("Activity", selected);
                            //intent.putExtra("MainMenu",MainMenuselected);
                            startActivity(intent);
                            break;
                        case "LoginLogoutDetails":
                            intent = new Intent(Home.this, LoginLogoutDetails.class);
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
                            intent.putExtra("Activity", "Message");
                            startActivity(intent);
                            break;

                        case "Mail Template":
                            // case "Submenu1":
                            intent = new Intent(Home.this, MessageTemplate.class);
                            intent.putExtra("Activity", "Mail");
                            startActivity(intent);
                            break;
                        /*case "Location":
                            // case "Submenu1":
                            intent = new Intent(Home.this, LocationAlert.class);
                            intent.putExtra("Activity","Mail");
                            startActivity(intent);
                            break;*/


                        case "Total Calls Made":
                            intent = new Intent(Home.this, TotalCallMade.class);
                            startActivity(intent);
                            break;
                        case "Location Report":
                            // case "Submenu1":
                            intent = new Intent(Home.this, MapsActivity.class);
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
                        case "Call Log Report":
                            intent = new Intent(Home.this, GraphReport.class);
                            intent.putExtra("ActivityName", "CallLog Report");
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
                            intent = new Intent(Home.this, NewClientEntry.class);
                            intent.putExtra("Activity", "Home");
                            startActivity(intent);
                            break;
                        case "Office Accounts":
                            intent = new Intent(Home.this, AccountsActivity.class);
                            intent.putExtra("Activity", "Home");
                            startActivity(intent);
                            break;
                        case "Client Account":
                            intent = new Intent(Home.this, ClientAccount.class);
                            intent.putExtra("ActName", "Home");
                            startActivity(intent);
                            break;
                        case "Client Report":
                            intent = new Intent(Home.this, ClientReport.class);
                            intent.putExtra("Activity", "Home");
                            startActivity(intent);
                            break;
                        case "Payment Approve":
                            intent = new Intent(Home.this, PaymentApprove.class);
                            startActivity(intent);
                            break;
                        case "Received Payment Report":
                            intent = new Intent(Home.this, ReceivedPaymentReport.class);
                            startActivity(intent);
                            break;
                        case "Attendance":
                            // case "Submenu1":
                            intent = new Intent(Home.this, Attendence.class);
                            intent.putExtra("Activity", selected);
                            startActivity(intent);
                            break;
                        case "LeaveApplication":
                            intent = new Intent(Home.this, LeaveReport.class);
                            intent.putExtra("Activity", selected);
                            startActivity(intent);
                            break;
                        case "Salary Slip":
                            intent = new Intent(Home.this, SalarySlip.class);
                            intent.putExtra("Activity", selected);
                            startActivity(intent);
                            break;
                        case "Upload Emp Documents":
                            intent = new Intent(Home.this, ActivityUploadDocs.class);
                            intent.putExtra("Activity", "EmpDocs");
                            startActivity(intent);
                            break;
                        case "Employee Details":
                            intent = new Intent(Home.this, EmpDetails.class);
                            intent.putExtra("Activity", "EmpDocs");
                            startActivity(intent);
                            break;
                        case "Push Notification":
                            intent = new Intent(Home.this, PushNotificationActivity.class);
                           // intent.putExtra("Activity", "EmpDocs");
                            startActivity(intent);
                            break;
                        case "Call Log with Recording":
                            intent = new Intent(Home.this, MainActivity.class);
                            // intent.putExtra("Activity", "EmpDocs");
                            startActivity(intent);
                            break;
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
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                    //Log.d("DEBUG", "heading clicked");

                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-516 Home oncreate " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }//onCreate close

    private void firebaseAuthentication(String subscribe, String email, String mobile) {
        Log.d("accountcre","11");
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            //progressDialog.dismiss();
            Log.d("accountcreated","1");
        } else {
            if (email.equals("") || mobile.equals("")) {
                userProfileEditor();
            } else {
                FirebaseMessaging.getInstance().subscribeToTopic(subscribe);
                createUser(email,mobile);
               // createFirebaseUser(email, mobile);
                Log.d("infirebase", email+" "+mobile+" "+subscribe);
            }
        }
    }

    private void createUser(String memail, String mpassword){
        mAuth.createUserWithEmailAndPassword(memail, mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   // startProfileActivity();
                } else {
                    loginFirebase(emailid, mobile);
//Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loginFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //startProfileActivity();
                } else {
                    Toast.makeText(Home.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createFirebaseUser(String email, String mobile) {
        mAuth.createUserWithEmailAndPassword(email, mobile).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Home.this, "Firebase User created successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Home.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("firebaseerror", task.getException().getMessage());
                }
            }
        });
    }

    private void userProfileEditor() {
        try {
            Toast.makeText(Home.this,"Please update all fields",Toast.LENGTH_SHORT).show();
            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            //Creating a view to get the dialog box
            View confirmCall = li.inflate(R.layout.alert_editprofile, null);
            edtUserName = confirmCall.findViewById(R.id.edtUserName);
            edtUserPassword = confirmCall.findViewById(R.id.edtUserPassword);
            edtEmailID = confirmCall.findViewById(R.id.edtEmailId);
            edtEmailPWD = confirmCall.findViewById(R.id.edtEmailPassword);
            edtMobile = confirmCall.findViewById(R.id.edtMobile);
            TextView txtUpdate = confirmCall.findViewById(R.id.txtUpdate);
            TextView txtCancel = confirmCall.findViewById(R.id.txtCancel);
            imgPhoto = confirmCall.findViewById(R.id.imgProfile);
            TextView txtEditPhoto = confirmCall.findViewById(R.id.txtEditPhoto);
            AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
            //Adding our dialog box to the view of alert dialog
            alert.setView(confirmCall);
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            edtUserName.setText(counselorname);
            edtUserPassword.setText(userpass);
            edtEmailID.setText(emailid);
            edtEmailPWD.setText(emailpass);
            edtMobile.setText(mobile);

            //if(!profilephoto.isEmpty()){
            deleteCache(Home.this);
            String strImageUrl = "http://anilsahasrabuddhe.in/CRM/wayto965425/upload-photo/" + counselorid + "_" + counselorname + ".jpg";
            Log.d("ProfileP", strImageUrl);
            Picasso.with(Home.this).load(strImageUrl).placeholder(R.mipmap.ic_launcher).into(imgPhoto);
            //}
            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                                /*Intent pickIntent = new Intent();
                                pickIntent.setType("image/*");
                                pickIntent.setAction(Intent.ACTION_GET_CONTENT);*/
                    Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
                    pickPhotoIntent.setType("image/*");
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    pickPhotoIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    startActivityForResult(pickPhotoIntent, GET_FROM_GALLERY);
                }
            });
            txtEditPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
                    pickPhotoIntent.setType("image/*");
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    pickPhotoIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    startActivityForResult(pickPhotoIntent, GET_FROM_GALLERY);
                }
            });

            txtUpdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    temp = 0;
                    String uname = edtUserName.getText().toString();
                    String upass = edtUserPassword.getText().toString();
                    String email = edtEmailID.getText().toString();
                    String epass = edtEmailPWD.getText().toString();
                    String mbl = edtMobile.getText().toString();
                    if (uname.length() == 0) {
                        temp = 1;
                        edtUserName.setError("Invalid name");
                    }
                    if (upass.length() == 0) {
                        temp = 1;
                        edtUserPassword.setError("Invalid passowrd");
                    }
                    if (email.length() == 0) {
                        temp = 1;
                        edtEmailID.setError("Invalid emailid");
                    }
                    if (epass.length() == 0) {
                        temp = 1;
                        edtEmailPWD.setError("Invalid password");
                    }
                    if (mbl.length() == 0) {
                        temp = 1;
                        edtMobile.setError("Invalid mobile no");
                    }
                    if (temp == 0) {
                        dialog = ProgressDialog.show(Home.this, "", "Updating information", true);
                        newThreadInitilization(dialog);
                        //This function is used to edit  user profile
                        updateProfile(uname, upass, email, epass, mbl);
                    }
                }
            });

            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-117 Home BtnLogout clicked " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void checkData() {
        try {
            if (CheckServer.isServerReachable(Home.this)) {
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(getApplicationContext());
                url = clienturl + "?clientid=" + clientid + "&caseid=1&UserName=" + counselorname + "&UserPassword=" + userpass;
                Log.d("LoginUrl", url);
                urlRequest.setUrl(url);
                urlRequest.getResponse(new ServerCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(String response) throws JSONException {

                        dialog.dismiss();
                        Log.d("LoginResponse", response);
                        if (response.startsWith("Array")) {
                            //  final String uname = response.substring(response.indexOf("[UserName] =>") + 13, response.indexOf("[UserPassword] =>"));
                            //final String userpass = response.substring(response.indexOf("[UserPassword] =>") + 18, response.indexOf("[UserRole] =>"));
                             emailid = response.substring(response.indexOf("[cEmailAdd] =>") + 14, response.indexOf("[cEmailPassword] =>")).trim();
                             emailpass = response.substring(response.indexOf("[cEmailPassword] =>") + 20, response.indexOf("[cMobileNO] =>")).trim();
                             mobile = response.substring(response.indexOf("[cMobileNO] =>") + 14, response.indexOf("[cProfilePhoto] => ")).trim();
                            //role = response.substring(response.indexOf("[UserRole] =>") + 13, response.indexOf("[Status] =>"));
                            //counselorId = response.substring(response.indexOf("[cCounselorID] =>") + 17, response.indexOf(" [cCounselorName] =>"));
                            //final String statusid = response.substring(response.indexOf("[Status] =>") + 11, response.indexOf("[cEmailAdd] =>"));
                            //strImei1 = response.substring(response.indexOf("[IMEI1] =>") + 10, response.indexOf("[IMEI2]")).trim();
                            //strImei2 = response.substring(response.indexOf("[IMEI2] =>") + 10, response.indexOf("[ProfilePhoto] =>")).trim();
                            // strImei2 = strImei2.substring(1, strImei2.length() - 4).trim();
                          //  role = role.replace(" ", "");
                            //  Log.d("IMEI", strImei1 + " " + strImei2);
                            firebaseAuthentication(verifyid, emailid, mobile);

                            //Log.d("Role", role);
                           /* if(role.contains("2"))
                            {
                                editor.putString("Name", uname);
                                editor.putString("Id", counselorId);
                                editor.putString("EmailId", emailID);
                                editor.putString("Role", role);
                                editor.putString("MobileNo", mobile);
                                editor.putString("StatusId", statusid);
                                editor.commit();
                                intent=new Intent(Login.this,ActivityHome.class);
                                startActivity(intent);
                            }
                            else {
*/
                        }
                    }
                });
            }

        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-109 Login Login function " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void init() {
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
        deviceId = sp.getString("DeviceID", null);
        verifyid = sp.getString("verifyid", null);
        timeout = sp.getLong("TimeOut", 0);

        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        timeout = sp.getLong("TimeOut", 0);
        counselorid = sp.getString("Id", null);
        uname = sp.getString("Name", null);
        Log.d("UrlHome", clienturl);

        Log.d("timeout", String.valueOf(timeout));
        login = new Login();
        uname1 = sp.getString("Name", null);
        id = sp.getString("Id", null);
        Log.d("U***", uname1);
        Log.d("I***", id);
        counselorname = sp.getString("Name", null).trim();
        userpass = sp.getString("UserPass", null).trim();
       // emailpass = sp.getString("EmailPass", null).trim();
        counselorname = counselorname.replaceAll(" ", "");
        counselorid = sp.getString("Id", null).trim();
        counselorid = counselorid.replaceAll(" ", "");
        //emailid = sp.getString("EmailId", null).trim();
        Log.d("Iemail", emailid);
        role = sp.getString("Role", null);
        //mobile = sp.getString("MobileNumber", null).trim();
        statusid1 = sp.getString("StatusId", null);
        statusid1 = statusid1.replaceAll(" ", "");
        upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=46&CounselorId=" + counselorid + "_" + counselorname;
        Log.d("UploadUrl", upLoadServerUri);

        toolbar = findViewById(R.id.toolbar1);

        deleteCache(Home.this);

        strImageUrl = "http://anilsahasrabuddhe.in/CRM/wayto965425/upload-photo/" + counselorid + "_" + counselorname + ".jpg";
        Log.d("ProfileP", strImageUrl);
        // photo=Picasso.with(Home.this).load(strImageUrl);

        //spinnerRef = findViewById(R.id.spinnerFilter);
        txtCoin = findViewById(R.id.txtCoin);
        imgRefresh = findViewById(R.id.imgRefresh);
        txtDiamond = findViewById(R.id.txtDiamond);
        imgCoin = findViewById(R.id.imgCoin);
        imgNotification = findViewById(R.id.imgNotification);
        imgNotification1 = findViewById(R.id.imgNotification1);

        setSupportActionBar(toolbar);

        // recyclerView = findViewById(R.id.recyclerStatusTotalCnt);
        arrayListNotification = new ArrayList<>();
        gps = new GPSTracker(Home.this);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        txtUName = headerView.findViewById(R.id.txtUName);
        txtCID = headerView.findViewById(R.id.txtID);
        txtEditProfile = headerView.findViewById(R.id.txtEditProfile);
        txtLogout = headerView.findViewById(R.id.txtLogout);
        navigationView = findViewById(R.id.nav_view);
        txtUName.setText(counselorid + "." + uname);
        txtCID.setText(counselorid + "." + uname);
        imgProfile = headerView.findViewById(R.id.imageProfile);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        expandableList = findViewById(R.id.navigationmenu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager mgr = getSystemService(NotificationManager.class);
            mgr.createNotificationChannel(channel);
        }
    }//Close initialization

    public void newThreadInitilization(final ProgressDialog dialog1) {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(timeout);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog1.isShowing()) {
                                dialog1.dismiss();
                                Toast.makeText(Home.this, "Connection Aborted.", Toast.LENGTH_SHORT).show();
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

    public void logoutClick() {
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
                    editor.putString("CheckPermission", null);
                    editor.commit();
                    intent = new Intent(Home.this, Login.class);
                    intent.putExtra("ActLogin", "Home");
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-517 Home BtnLogout clicked " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void updateProfile(String username, String userpass, String email, String emailpass, String mbl) {
        try {
            if (CheckServer.isServerReachable(Home.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=136&CounselorID=" + counselorid + "&UserName=" + username + "&UserPassword=" + userpass + "&EmailId=" + email + "&EmailPassword=" + emailpass + "&Mobile=" + mbl;
                Log.d("UpdateProfileUrl", url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Log.d("updateProfileRes", response);

                                    if (response.contains("Data inserted successfully")) {
                                        alertDialog.dismiss();
                                        checkData();
                                        if(isSelected==1) {
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
                                        }
                                        Toast.makeText(Home.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Home.this, "Profile not updated", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(Home.this, "Errorcode-133 Home profileupdateresponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Home.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-132 Home getNotification " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /////////////////////////////////////Location//////////////////////////////////////////////////////////////
    public void getLastUploadedLocation() {
        try {
            String url = clienturl + "?clientid=" + clientid + "&caseid=134&CounselorID=" + id;
            // arrayListClientReport=new ArrayList<>();
            if (CheckServer.isServerReachable(Home.this)) {
                Log.d("LastUploadedLocationUrl", url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    // dialog.dismiss();
                                    String res = String.valueOf(response);
                                    if (res.contains("[]")) //check if location not uploaed previously
                                    {
                                        uploadLocation(Home.this, id, uploadLatitude, uploadLongitude);
                                    } else {

                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("LastUploadedLocationRes", String.valueOf(response));
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            latitude = jsonObject1.getString("latitude");
                                            longitude = jsonObject1.getString("longitude");
                                            date1 = jsonObject1.getString("Date");
                                            time1 = jsonObject1.getString("Time");
                                            Log.d("LastLocation", latitude + " " + longitude + " " + date1 + " " + time1);
                                        }

                                        currentDate = new CommonMethods().getDate();
                                        currentDate = currentDate.replaceAll("_", "/");

                                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                        currentTime = format.format(new Date());
                                        Date date1 = format.parse(time1);
                                        Date date2 = format.parse(currentTime);

                                        //  date2=format.format(date2);
                                        long mills = date2.getTime() - date1.getTime();
                                        //  Log.d("Data1", ""+date1.getTime());
                                        //  Log.d("Data2", ""+date2.getTime());
                                        int hours = (int) (mills / (1000 * 60 * 60));
                                        int mins = (int) (mills / (1000 * 60)) % 60;


                                        Log.d("CDateTime", mins + " " + currentTime);
                                        if (latitude.length() > 3 || latitude.length() > 3) {
                                            cmpULat = uploadLatitude.substring(5, 8);
                                            cmpULng = uploadLongitude.substring(5, 8);
                                            cmpLat = latitude.substring(5, 8);
                                            cmpLng = longitude.substring(5, 8);
                                            Log.d("CMPLatLng", cmpLat + " " + cmpULat + " " + cmpLng + " " + cmpULng);
                                        }
                                        String min = String.valueOf(mins);

                                        if (cmpULat.equals(cmpLat) || cmpULng.equals(cmpLng)) {

                                            if (mins > 30 || min.contains("-")) {
                                                //to upload user's current location to server
                                                uploadLocation(Home.this, id, uploadLatitude, uploadLongitude);
                                            } else {

                                                //this function is used to check total reminders set for that current date
                                                getReminderNotification();
                                            }
                                        } else {

                                            //to upload user's current location to server
                                            uploadLocation(Home.this, id, uploadLatitude, uploadLongitude);

                                        }
                                    }

                                } catch (Exception e) {
                                  //  Toast.makeText(Home.this, "Errorcode-515 Home LastUploadedLocationResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(Home.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
          //  Toast.makeText(Home.this, "Errorcode-518 Home getLastUploadedLocation " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void fetch(final Context context1) {
        // Check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            uploadLatitude = String.valueOf(latitude);
            uploadLongitude = String.valueOf(longitude);

            // \n is for new line
            Log.d("HomeLatLng", latitude + " / " + longitude + "\n");
            //this function is used to get last uploaded location of user
            getLastUploadedLocation();

        } else {
            Toast.makeText(context1, "Please turn on location", Toast.LENGTH_SHORT).show();
            getReminderNotification();
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            //gps.showSettingsAlert();
        }
    /* }
        }, 0, TIME_INTERVAL);*/
    }

    public void uploadLocation(final Context context, String counselorid, final String latitude, final String longitude) {

       /* if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        } else {*/
        String strurl = clienturl + "?clientid=" + clientid + "&caseid=93&latitude=" + latitude + "&longitude=" + longitude + "&CounselorID=" + counselorid;
        Log.d("uploadlocationurl", strurl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("Data inserted successfully")) {
                    Log.d("homelocation", "harsha");
                    // Toast.makeText(context, "Location Upload"+"\nlat    : " + currentLat + "\nlong : " + currentLong, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Record not inserted successfully", Toast.LENGTH_SHORT).show();
                }
                //  dialog=ProgressDialog.show(Home.this,"","Loading notifications",true);
                // newThreadInitilization(dialog);
                getReminderNotification();

                Log.d("urlcounthome ", "&latitude=" + latitude + " / " + "&longitude=" + longitude + " / " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", String.valueOf(error));
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-119 Home GetCallDetails " + e.toString(), Toast.LENGTH_SHORT).show();
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
            mCallArrayList.clear();
            mUrlCallArrayList = new ArrayList<>();
            mUrlCallArrayList.clear();
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

            if (CheckServer.isServerReachable(Home.this)) {
                // requestQueue = Volley.newRequestQueue(Home.this);
                urlCallDate = clienturl + "?clientid=" + clientid + "&caseid=57&cDeviceID=" + deviceId;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCallDate, null,
                        new callSuccess(), new callFail());
                Log.d("UrlLastCallDate", urlCallDate);

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-120 Home CallLogDetails " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadCalls(String callPhoneno, String finalCallType, final String strdateFormated,
                            String callDuration, final String callIMEI1, String callIMEI2, String callphAccID) {
        try {
            if (CheckServer.isServerReachable(Home.this)) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(Home.this);
                } else {
                    String strurl = clienturl + "?clientid=" + clientid + "&caseid=53&MobileNo=" + callPhoneno +
                            "&CallType=" + finalCallType + "&CallDate=" + strdateFormated + "&CallDuration=" + callDuration +
                            "&cDeviceID=" + deviceId + "&PhoneAccountId=" + callphAccID;
                    Log.d("CallUrl", strurl);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("UploadCallResponse", response);
                           /* if(uploadcalls.equals("size0")) {
                                if (counturl == mCallArrayList.size())
                                {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    getSMSDetails();
                                }
                            }else
                            {
                                if (counturl == mUploadCallArrayList.size()) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    getSMSDetails();
                                }
                            }*/
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
            } else {

                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-121 Home UploadCallLog " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private class callSuccess implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                Log.e("success", String.valueOf(response));
           /* if(dialog.isShowing())
            {
                dialog.dismiss();
            }
             */
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    JSONObject lastdate = jsonObject.getJSONObject("dtCallDate");
                    String mlastdate = lastdate.getString("date");

                    Log.d("lastdate", mlastdate);
                    callDetailsFetch = new CallDetailsFetch(mlastdate);
                    mUrlCallArrayList.add(callDetailsFetch);
                }
                //Log.d("MUrlCallArraySize",mUrlCallArrayList.size()+"");
                if (mUrlCallArrayList.size() == 0) {
                    if (mCallArrayList.size() > 50) {
                        Log.d("Entered", "urlCall>50");
                        // dialog = ProgressDialog.show(Home.this, "Background Process is running", "Please wait a while", true);
                        dialog = new ProgressDialog(Home.this);
                        dialog.setMax(mCallArrayList.size());
                        dialog.setMessage("Please wait a while");
                        dialog.setTitle("Background Process is running");
                        dialog.setCancelable(false);
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Entered", "urlCall>50CancelClicked");
                                dialog.dismiss();
                                getSMSDetails();
                            }
                        });
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (dialog.getProgress() <= dialog.getMax()) {
                                        Thread.sleep(200);
                                        handler.sendMessage(handler.obtainMessage());
                                        if (dialog.getProgress() == dialog.getMax()) {
                                            dialog.dismiss();
                                            Log.d("Entered", "urlCall>50WhileThread");
                                            getSMSDetails();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);

                                dialog.incrementProgressBy(1);
                            }
                        };
                    } else {
                        //  dialog = ProgressDialog.show(Home.this, "", "Uploading calls", true);
                        Log.d("Entered", "urlCall not >50");
                        dialog = new ProgressDialog(Home.this);
                        dialog.setMax(mCallArrayList.size());
                        dialog.setMessage("Uploading calls");
                        dialog.setTitle("");
                        dialog.setCancelable(false);
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Entered", "urlCall not>50CancelClicked");
                                dialog.dismiss();
                                getSMSDetails();
                            }
                        });
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (dialog.getProgress() <= dialog
                                            .getMax()) {
                                        Thread.sleep(200);
                                        handler.sendMessage(handler.obtainMessage());
                                        if (dialog.getProgress() == dialog
                                                .getMax()) {
                                            dialog.dismiss();
                                            Log.d("Entered", "urlCall not>50WhileThread");
                                            getSMSDetails();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                dialog.incrementProgressBy(1);
                            }
                        };
                    }

                }

                //  Log.d("urlarrlen", mUrlCallArrayList.size() + "");
                for (int i = 0; i < mCallArrayList.size(); i++) {
                    String clno = mCallArrayList.get(i).getmCallMobileNo();
                    String cltype = mCallArrayList.get(i).getmCallType();
                    String cldate = mCallArrayList.get(i).getmCallDate();
                    String clduration = mCallArrayList.get(i).getmCallDuration();
                    String climei1 = mCallArrayList.get(i).getmIMEI1();
                    String climei2 = mCallArrayList.get(i).getmIMEI2();
                    String clphAccID = mCallArrayList.get(i).getmphAccID();
                    matchforCall++;
                    if (mUrlCallArrayList.size() == 0) {
                        uploadcalls = "size0";
                        uploadCalls(clno, cltype, cldate, clduration, climei1, climei2, clphAccID);

                    } else {
                        String strCompareRecord = mUrlCallArrayList.get(mUrlCallArrayList.size() - 1).getmfthCallDate();
                        Date dtMydate = sdfComapre.parse(strCompareRecord);
                        String strUrlDate = sdfComapre.format(dtMydate);
                        // Log.d("datematch","testing");
                        if (strUrlDate.compareTo(cldate) < 0) {
                            Log.d("datematch", strUrlDate + " / " + cldate);
                            callDetialswithIMEI = new CallDetialswithIMEI(clno, cltype, cldate, clduration, climei1, climei2, clphAccID);
                            mUploadCallArrayList.add(callDetialswithIMEI);
                        }
                    }

                }

                //  Log.d("recordscall", "call upload without date");
                Log.d("uploadarraysizecall", mUploadCallArrayList.size() + "");
                if (mUploadCallArrayList.size() == 0) {
                    getSMSDetails();
                }
                if (mUploadCallArrayList.size() > 0) {
                    if (mUploadCallArrayList.size() > 50) {
                        Log.d("Entered", "uploadcall>50");
                        //dialog = ProgressDialog.show(Home.this, "Background Process is running", "Please wait a while", true);
                        dialog = new ProgressDialog(Home.this);
                        dialog.setMax(mUploadCallArrayList.size());
                        dialog.setMessage("Please wait a while");
                        dialog.setTitle("Background Process is running");
                        dialog.setCancelable(false);
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Entered", "uploadcall>50CancelClicked");
                                dialog.dismiss();
                                getSMSDetails();
                            }
                        });
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (dialog.getProgress() <= dialog
                                            .getMax()) {
                                        Thread.sleep(200);
                                        handler.sendMessage(handler.obtainMessage());
                                        if (dialog.getProgress() == dialog
                                                .getMax()) {
                                            // Log.d("Entered","uploadcall>50WhileThread");
                                            dialog.dismiss();
                                            //getSMSDetails();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                dialog.incrementProgressBy(1);
                            }
                        };
                    } else {
                        Log.d("Entered", "uploadcall not>50");
                        //    dialog = ProgressDialog.show(Home.this, "", "Uploading calls", true);
                        dialog = new ProgressDialog(Home.this);
                        dialog.setMax(mUploadCallArrayList.size());
                        dialog.setMessage("Uploading calls");
                        dialog.setTitle("");
                        dialog.setCancelable(false);
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("Entered", "uploadcall not>50CancelClicked");
                                getSMSDetails();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (dialog.getProgress() <= dialog
                                            .getMax()) {
                                        Thread.sleep(200);
                                        handler.sendMessage(handler.obtainMessage());
                                        if (dialog.getProgress() == dialog
                                                .getMax()) {
                                            // Log.d("Entered","uploadcall not>50WhileThread");
                                            dialog.dismiss();
                                            // getSMSDetails();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                dialog.incrementProgressBy(1);
                            }
                        };
                    }
                }
                for (int i = 0; i < mUploadCallArrayList.size(); i++) {
                    String clno = mUploadCallArrayList.get(i).getmCallMobileNo();
                    String cltype = mUploadCallArrayList.get(i).getmCallType();
                    String cldate = mUploadCallArrayList.get(i).getmCallDate();
                    String clduration = mUploadCallArrayList.get(i).getmCallDuration();
                    String climei1 = mUploadCallArrayList.get(i).getmIMEI1();
                    String climei2 = mUploadCallArrayList.get(i).getmIMEI2();
                    String clphAccID = mUploadCallArrayList.get(i).getmphAccID();
                    uploadcalls = "size1";
                    uploadCalls(clno, cltype, cldate, clduration, climei1, climei2, clphAccID);
                }

                // Log.d("recordscall", "call upload with date");
            } catch (Exception e) {
                Toast.makeText(Home.this, "Errorcode-122 Home UploadCallResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                //  e.printStackTrace();
            }
        }
    }

    private class callFail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(Home.this, "Errorcode-123 Home CallLogDetails Fail " + error.toString(), Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-123 Home SmsDetails " + e.toString(), Toast.LENGTH_SHORT).show();
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
            //   Log.d("smsarraylist", String.valueOf(mSMSArrayList.size()));
            // txtttlsmsrecord.setText(mSMSArrayList.size()+"");
            if (CheckServer.isServerReachable(Home.this)) {
                // requestQueue = Volley.newRequestQueue(Home.this);
                urlSMSDate = clienturl + "?clientid=" + clientid + "&caseid=58&cDeviceID=" + deviceId;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSMSDate, null,
                        new smsSuccess(), new smsFail());
                Log.d("Lastsmsurl", urlSMSDate);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);
            } else {
                // dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
           // Toast.makeText(Home.this, "Errorcode-124 Home SmsLogDetails " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadSMS(String smsPhoneno, String finalsmsType, final String strdateFormated,
                          String smsBody, String smsIMEI1, String smsIMEI2) {
        try {
            if (CheckServer.isServerReachable(Home.this)) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(Home.this);
                } else {
                    String strurl = clienturl + "?clientid=" + clientid + "&caseid=54&MobileNo=" + smsPhoneno +
                            "&SMSType=" + finalsmsType + "&SMSDate=" + strdateFormated + "&SMSBODY=" + smsBody +
                            "&cDeviceID=" + deviceId;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("ChatResponse", response);

                           /* if(uploadsms.equals("size0")) {
                                if (counturl == mSMSArrayList.size())
                                {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }
                            }else if(uploadsms.equals("size1"))
                            {
                                if (counturl == mUploadSMSArrayList.size()) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                }
                            }*/

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
            } else {
                //dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
          //  Toast.makeText(Home.this, "Errorcode-125 Home UploadSmsLog" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void displayNotification() {
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-126 Home DisplayNotification " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private class smsSuccess implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("successSMSURl", urlSMSDate);
                Log.e("successSMS", String.valueOf(response));

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
                if (mUrlSMSArrayList.size() == 0) {
                    if (mSMSArrayList.size() > 50) {
                        // dialog = ProgressDialog.show(Home.this, "Background Process is running", "Please wait a while", true);
                        dialog = new ProgressDialog(Home.this);
                        dialog.setMax(mSMSArrayList.size());
                        dialog.setMessage("Please wait a while");
                        dialog.setTitle("Background Process is running");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (dialog.getProgress() <= dialog
                                            .getMax()) {
                                        Thread.sleep(200);
                                        handler.sendMessage(handler.obtainMessage());
                                        if (dialog.getProgress() == dialog
                                                .getMax()) {
                                            dialog.dismiss();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                dialog.incrementProgressBy(1);
                            }
                        };
                    } else {
                        // dialog = ProgressDialog.show(Home.this, "", "Uploading sms details", true);
                        dialog = new ProgressDialog(Home.this);
                        dialog.setMax(mSMSArrayList.size());
                        dialog.setMessage("Uploading sms details");
                        dialog.setTitle("");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (dialog.getProgress() <= dialog
                                            .getMax()) {
                                        Thread.sleep(200);
                                        handler.sendMessage(handler.obtainMessage());
                                        if (dialog.getProgress() == dialog
                                                .getMax()) {
                                            dialog.dismiss();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                dialog.incrementProgressBy(1);
                            }
                        };
                    }
                    //newThreadInitilization(dialog);
                }

                Collections.reverse(mSMSArrayList);
                for (int i = 0; i < mSMSArrayList.size(); i++) {
                    String smsno = mSMSArrayList.get(i).getmSMSMobileNo();
                    String smstype = mSMSArrayList.get(i).getmSMStype();
                    String smsdate = mSMSArrayList.get(i).getmSMSdate();
                    String smsbody = mSMSArrayList.get(i).getmSMSbody();
                    String smsimei1 = mSMSArrayList.get(i).getmSMSIMEI1();
                    String smsimei2 = mSMSArrayList.get(i).getmSMSIMEI2();
                    matchforsms++;
                    if (mUrlSMSArrayList.size() == 0) {
                        uploadsms = "size0";
                        uploadSMS(smsno, smstype, smsdate, smsbody, smsimei1, smsimei2);
                    } else {
                        String strCompareRecord = mUrlSMSArrayList.get(mUrlSMSArrayList.size() - 1).getmfthSMSDate();
                        Date dtMydate = sdfComapre.parse(strCompareRecord);
                        String strUrlDate = sdfComapre.format(dtMydate).substring(0, 17);
                        //Log.d("ddd", strUrlDate);
                        strUrlDate = strUrlDate + sdfComapre.format(dtMydate).substring(18, 20);
                        //Log.d("ddd1", strUrlDate);
                        if (strUrlDate.compareTo(smsdate) < 0) {
                            Log.d("datematch", strUrlDate + " / " + smsdate);
                            smsDetails = new SMSDetails(smsno, smstype, smsdate, smsbody, smsimei1, smsimei2);
                            mUploadSMSArrayList.add(smsDetails);
                        }
                    }
                }
               /* if(dialog.isShowing())
                {
                    dialog.dismiss();
                }*/
                Log.d("uploadsmsarraysize", mUploadSMSArrayList.size() + "");
                //   txtnewsmsrecords.setText(mUploadSMSArrayList.size() + "");
                if (mUploadSMSArrayList.size() > 0) {
                    if (mUploadSMSArrayList.size() > 50) {
                        //dialog = ProgressDialog.show(Home.this, "Background Process is running", "Please wait a while", true);
                        dialog = new ProgressDialog(Home.this);
                        dialog.setMax(mUploadSMSArrayList.size());
                        dialog.setMessage("Please wait a while");
                        dialog.setTitle("Background Process is running");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (dialog.getProgress() <= dialog
                                            .getMax()) {
                                        Thread.sleep(200);
                                        handler.sendMessage(handler.obtainMessage());
                                        if (dialog.getProgress() == dialog
                                                .getMax()) {
                                            dialog.dismiss();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                dialog.incrementProgressBy(1);
                            }
                        };
                    } else {

                        dialog = new ProgressDialog(Home.this);
                        dialog.setMax(mUploadSMSArrayList.size());
                        dialog.setMessage("Uploading sms");
                        dialog.setTitle("");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    while (dialog.getProgress() <= dialog
                                            .getMax()) {
                                        Thread.sleep(200);
                                        handler.sendMessage(handler.obtainMessage());
                                        if (dialog.getProgress() == dialog
                                                .getMax()) {
                                            if (!isFinishing()) {
                                                dialog.dismiss();
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            dialog.incrementProgressBy(1);
                        }
                    };
                    // dialog = ProgressDialog.show(Home.this, "", "Uploading sms", true);
                    // newThreadInitilization(dialog);
                }
                if (mUploadSMSArrayList.size() > 0) {
                    for (int i = 0; i < mUploadSMSArrayList.size(); i++) {
                        String smsno = mUploadSMSArrayList.get(i).getmSMSMobileNo();
                        String smstype = mUploadSMSArrayList.get(i).getmSMStype();
                        String smsdate = mUploadSMSArrayList.get(i).getmSMSdate();
                        String smsbody = mUploadSMSArrayList.get(i).getmSMSbody();
                        String smsimei1 = mUploadSMSArrayList.get(i).getmSMSIMEI1();
                        String smsimei2 = mUploadSMSArrayList.get(i).getmSMSIMEI2();
                        Log.d("recordsms", smsbody);
                        uploadsms = "size1";
                        uploadSMS(smsno, smstype, smsdate, smsbody, smsimei1, smsimei2);
                    }
                }
                // Toast.makeText(Home.this, "SMS Records Upload Successfully.", Toast.LENGTH_SHORT).show();
                //  Log.d("recordsms", "sms upload");
            } catch (Exception e) {
               // Toast.makeText(Home.this, "Errorcode-127 Home UploadSmsResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private class smsFail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
           // Toast.makeText(Home.this, "Errorcode-128 Home UploadSms fail " + error.toString(), Toast.LENGTH_SHORT).show();
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
            listDataHeader.add("Accounts");
            listDataHeader.add("HRMS");
            //listDataHeader.add("Logout");
            // Adding child data
            List<String> heading5 = new ArrayList<String>();
            heading5.add("Attendance");
            heading5.add("LoginLogoutDetails");
            heading5.add("LeaveApplication");
            heading5.add("Salary Slip");
            heading5.add("Upload Emp Documents");
            heading5.add("Employee Details");

            List<String> heading1 = new ArrayList<String>();
            heading1.add("Other Site's Leads");
            heading1.add("Dashboard");
            heading1.add("Filter Leads");
            heading1.add("Send Notification");
            heading1.add("Push Notification");

            heading1.add("New Lead");
            heading1.add("Open Leads");
            //  heading1.add("Location");
            //   heading1.add("Form Filled");
            heading1.add("Reminder");
            heading1.add("SMS Template");
            heading1.add("Mail Template");
            heading1.add("Search");
            heading1.add("Chat");

            //  heading1.add("Send Bulk SMS");
            List<String> heading3 = new ArrayList<String>();
            //  heading3.add("New Client Entry");
            heading3.add("Master Entry");

            List<String> heading2 = new ArrayList<String>();
            heading2.add("Call Log with Recording");
            heading2.add("Call Log Report");
            heading2.add("Lead Count");
            heading2.add("Location Report");
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
            // heading2.add("Upload File");
            List<String> heading4 = new ArrayList<String>();
            heading4.add("Client Account");
            heading4.add("Client Report");
            heading4.add("Payment Approve");
            heading4.add("Received Payment Report");
            heading4.add("Office Accounts");

            listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data
            listDataChild.put(listDataHeader.get(1), heading3);
            listDataChild.put(listDataHeader.get(2), heading2);
            listDataChild.put(listDataHeader.get(3), heading4);
            listDataChild.put(listDataHeader.get(4), heading5);
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-129 Home NavigationMenu " + e.toString(), Toast.LENGTH_SHORT).show();
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

                        } catch (Exception e) {
                            Toast.makeText(Home.this, "Errorcode-130 Home NavigationItemSelected " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            try {
                isSelected=1;
                Uri selectedImage = data.getData();
                String scheme = selectedImage.getScheme();
                filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                //get path of selected image from gallary
                String path = getPath(getApplicationContext(), selectedImage);

                Log.d("Path", path);
                uploadFilePath = path.substring(path.indexOf("/storage"), path.lastIndexOf("/") + 1);
                Log.d("Path", path + " " + uploadFilePath);

                uploadFileName = path.substring(uploadFilePath.lastIndexOf("/") + 1);
                Log.d("Path", path + " " + uploadFilePath + " " + uploadFileName);

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                cursor.close();
                try {
                    //to get image from uri.
                    Bitmap bmp = getBitmapFromUri(selectedImage);
                    imgProfile.setImageBitmap(bmp);
                    imgPhoto.setImageBitmap(bmp);

                    //imgCamera.setImageBitmap(bmp);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // ImageView.setImageBitmap(bmp);

            } catch (Exception e) {
                Log.d("ExcOnActvtyRsltUpldDocs", e.toString());
                Toast.makeText(Home.this, "Errorcode-519 Home onActivityResult " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }//Close getBitmapFromUri

    public void getNotification() {
        //dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);
        try {
            if (CheckServer.isServerReachable(Home.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=63&CounselorID=" + counselorid;
                Log.d("Notificationurl",url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                  /*  if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }*/
                                    Log.d("getNotificationRes", response);

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
                                    //  Log.d("ArraylistNotification", String.valueOf(arrayListNotification.size()));
                                    for (int i = 0; i < arrayListNotification.size(); i++) {
                                        //  Log.d("entered", "Notification");
                                        dataNotification = arrayListNotification.get(i);
                                        //to show all notifications on adapter
                                        createNotification(dataNotification, Home.this);
                                    }
                                    //this function is used to get total points earned by user.
                                    getPointCollection();
                                } catch (Exception e) {
                                    Toast.makeText(Home.this, "Errorcode-133 Home GetNotificationResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Home.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-132 Home getNotification " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//Close getNotification

    public void createNotification(DataNotification aMessage, Context context) {
        try {
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            final int NOTIFY_ID = m; // ID of notification
            String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
            String title = context.getString(R.string.default_notification_channel_title); // Default Channel
            Intent intent = null;
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
                } else if(aMessage.getStrNotificaion().contains("reallocated by")) {
                    intent=new Intent(context,CounselorContactActivity.class);
                }else {
                    editor.putString("SelectedSrNo", dataNotification.getSrno());
                    // Log.d("SrNo***", aMessage.getSrno());
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
                    //   Log.d("SrNo***", aMessage.getSrno());
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-134 Home createNotification " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//Close createNotification

    public void getReminderNotification() {
        try {
            //dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);
            if (CheckInternet.checkInternet(Home.this)) {
                if (CheckServer.isServerReachable(Home.this)) {
                    String urlNotification = clienturl + "?clientid=" + clientid + "&caseid=65&CounselorID=" + counselorid;
                    Log.d("ReminderNourl",urlNotification);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlNotification,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                       /* if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }*/
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
                                        //this function is used to get all notifications other than reminder like message from other counselor,if new client is reallocated to user,if client upload his documents etc.
                                        getNotification();
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
                                        alertDialogBuilder.setTitle("Network issue!!!")
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
                                        Toast.makeText(Home.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    // dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-135 Home getReminderNotification " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//Close getReminderNotification

    public void checkCounselor() {
        try {
            //dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);
            if (CheckInternet.checkInternet(Home.this)) {
                if (CheckServer.isServerReachable(Home.this)) {
                    String urlNotification = clienturl + "?clientid=" + clientid + "&caseid=182&CounselorID=" + counselorid;
                    Log.d("CheckCounselorUrl",urlNotification);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlNotification,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Log.d("CheckCounselorRes", response);
                                        if (response.contains("[]")) {
                                            Toast.makeText(Home.this,"Invalid User",Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(Home.this,Login.class);
                                            intent.putExtra("ActLogin","Home");
                                            startActivity(intent);
                                        }
                                        else {
                                            checkData();
                                            newThreadInitilization(dialog);
                                            //this function is used to fetch current location of user
                                            fetch(Home.this);
                                            //this function is used to get user's last login details
                                            lastLoginDetails();
                                        }

                                     /*   JSONObject jsonObject = new JSONObject(response);
                                        // Log.d("Json",jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            uname = jsonObject1.getString("UserName");
                                            userpass = jsonObject1.getString("UserPassword");
                                            emailid = jsonObject1.getString("cEmailAdd");
                                            emailpass = jsonObject1.getString("cEmailPassword");
                                            mobile = jsonObject1.getString("cMobileNO");
                                            role = jsonObject1.getString("UserRole");
                                            counselorid = jsonObject1.getString("cCounselorID");
                                            statusid = jsonObject1.getString("Status");
                                            IMEINumber2 = jsonObject1.getString("IMEI1");
                                            IMEINumber2 = jsonObject1.getString("IMEI2");
                                        }
                                        editor.putString("IMEI1", IMEINumber1);
                                        editor.putString("IMEI2", IMEINumber2);
                                        //editor.commit();
                                        editor.putString("Name", uname);
                                        editor.putString("UserPass", userpass);
                                        editor.putString("Id", counselorid);
                                        editor.putString("EmailId", emailid);
                                        editor.putString("EmailPass", emailpass);
                                        editor.putString("Role", role);
                                        editor.putString("MobileNumber", mobile);
                                        editor.putString("StatusId", statusid);
                                        editor.commit();*/
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
                                        alertDialogBuilder.setTitle("Network issue!!!")
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
                                        Toast.makeText(Home.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }

                                }
                            });
                    requestQueue.add(stringRequest);
                } else {
                    // dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-135 Home getReminderNotification " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//Close getReminderNotification

    public void lastLoginDetails() {
        //dialog = ProgressDialog.show(Home.this, "Loading", "Please wait.....", false, true);
        try {
            if (CheckServer.isServerReachable(Home.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=14&CounsellorId=" + counselorid;
                arrayListTotal = new ArrayList<>();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Log.d("*******", response.toString());

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
                                    Toast.makeText(Home.this, "Errorcode-138 Home LastLoginDetails " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Home.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-137 Home lastLoginDetails " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//Close lastLoginDetails

    public void getPointCollection() {
        try {
            if (CheckServer.isServerReachable(Home.this)) {
                url = clienturl + "?clientid=" + clientid + "&caseid=37&nCounsellorId=" + counselorid;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Log.d("CoinResponse", response);
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
                                    // dialog=ProgressDialog.show(Home.this,"","Uploading call details",true);
                                    getCallDetails();

                                } catch (JSONException e) {
                                    Toast.makeText(Home.this, "Errorcode-146 Home PointCollectionResponse " + e.toString(), Toast.LENGTH_SHORT).show();
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
                                    alertDialogBuilder.setTitle("Network issue!!!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(Home.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-145 Home getPointCollection " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }//Close getPointCollection

    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (back_pressed + 2000 > System.currentTimeMillis()) {
                // need to cancel the toast here
                toast.cancel();
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
        } catch (Exception e) {
            Toast.makeText(Home.this, "Errorcode-147 Home OnBackpressed " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }//Close onBackpressed

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
            Intent intent = new Intent(Home.this, Home.class);
            intent.putExtra("Activity", "Home");
            startActivity(intent);

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
                            Intent intent = new Intent(Home.this, Home.class);
                            intent.putExtra("Activity", "Home");
                            startActivity(intent);
                            /*deleteCache(Home.this);
                            Picasso.with(Home.this).load(strImageUrl).placeholder(R.mipmap.ic_launcher).into(imgProfile);*/
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
    }//Close uploadFile

    private boolean checkPermission() {
        int i = 0;

        String[] perm = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        List<String> reqPerm = new ArrayList<>();

        for (String permis : perm) {
            int resultPhone = ContextCompat.checkSelfPermission(Home.this, permis);
            if (resultPhone == PackageManager.PERMISSION_GRANTED)
                i++;
            else {
                reqPerm.add(permis);
            }
        }
        if (i == 12)
            return true;
        else
            return requestPermission(reqPerm);
    }//Close checkPermission

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
}



