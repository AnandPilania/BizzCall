package com.bizcall.wayto.mentebit13;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Attendence extends AppCompatActivity implements AsyncResponse{

   String url="",clienturl="",clientid="",counselorid="",counselorname="",approvedIp="",systemIp="",logintime="",currentDate="",date11="",hftime="" ,fltime="";
   ProgressDialog dialog;
   RequestQueue requestQueue;
   SharedPreferences sp;
   long timeout;
   TextView txtLocation,txtApprovedIp,txtSystemIp,txtNotConnected,txtLoginTime,txtHalfDay,txtFullDay,txtLoginThrough;
   Thread thread;
   ArrayList<String> arrayListApprovedIp;
   ArrayList<String> arrayListLatLng;
   ArrayList<String> arrayListLat;
   ArrayList<String> arrayListLng;
   ImageView imgBack,imgLogin,imgLogout,imgRefresh;
   ListView listViewApprovedIP;
   LinearLayout linearLoginTime,linearHalfFullTime;
    String ip1="http://ipv4bot.whatismyipaddress.com";
    Handler handler=new Handler();
    String result="";
    IPFinder  ipFinder=new IPFinder();
    SharedPreferences.Editor editor;
    SimpleDateFormat simpleDateFormat;
    int count=0,setTrue=0,setFalse=0;
    String btnClicked="",uploadLatitude="",uploadLongitude="";
    Double uplt1,uplg1;
    GPSTracker gps;
    double slt1,dlt1,slg1,dlg1,flt1,flg1;
    LinearLayout linearLocation,linearIpAddress;
    BigDecimal aa,aa1,bb,bb1;
    String empno="",empid="",empname="",empemail="",empdob="",empgender="",empaddress="",empcity="",empstate="",emppin="",empisactive="",empjoindate="",empdepartment="",
            empleavebal="",empdesignation="",emppan="",empadhar="",empaccname="",empaccno="",empbankname="",empifscno="",empbranchname="",empmobile="";
    ArrayList<DataEmpDetails> arrayListEmpDetails;
    AdapterEmpDetails adapterEmpDetails;
    RecyclerView recyclerEmpDetails;
    ImageView imgDoc;
    String imgName="",ext="";
    String uploadFilePath = "";
    String uploadFileName = "",urlSMSDate;
    String upLoadServerUri = null;
    int serverResponseCode = 0;
    int temp=0;
    LinearLayout linearLoginProcess,linearImage;
    Button btnChoosePhoto;
    public static final int GET_FROM_GALLERY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_attendence);
            initialize();
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Attendence.this,Attendence.class);
                    startActivity(intent);
                }
            });
/* final WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);*/

       /* WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        systemIp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());*/
            // Log.d("IPP", " "+systemIp);

            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            imgLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    btnClicked="Login";
                    Log.d("LoginClicked",count+"");
                    //to get approved ip and location list
                    getEmpIPList();

                    //  Calendar calendar=Calendar.getInstance();

                }
            });

            imgLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClicked="Logout";

                    //to get approved ip and location list
                    getEmpIPList();


                }
            });
        }catch (Exception e)
        {
            Toast.makeText(Attendence.this,"Errorcode-520 Attendence oncreate "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }//onCreate

    private void initialize()
    {
        requestQueue = Volley.newRequestQueue(Attendence.this);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        gps = new GPSTracker(Attendence.this);
        editor = sp.edit();
        clientid = sp.getString("ClientId", "");
        clienturl = sp.getString("ClientUrl", "");
        counselorname=sp.getString("Name","");
        counselorid = sp.getString("Id", "");
        counselorid = counselorid.replaceAll(" ", "");
        timeout = sp.getLong("TimeOut", 0);
        imgRefresh=findViewById(R.id.imgRefresh);
        txtApprovedIp = findViewById(R.id.txtApprovedIp);
        txtSystemIp = findViewById(R.id.txtSystemIp);
        txtNotConnected = findViewById(R.id.txtNotConnected);
        imgLogin = findViewById(R.id.imgLogin);
        imgLogout = findViewById(R.id.imgLogout);
        imgDoc=findViewById(R.id.imgReceipt);
        imgBack = findViewById(R.id.img_back);
        linearLoginTime=findViewById(R.id.linearLoginTime);
        linearHalfFullTime=findViewById(R.id.linearHalfFullTime);
        txtLoginTime=findViewById(R.id.txtLogintime);
        txtHalfDay=findViewById(R.id.txtHalfday);
        txtFullDay=findViewById(R.id.txtFullday);
        listViewApprovedIP=findViewById(R.id.recyclerApprovedIP);
        txtLoginThrough=findViewById(R.id.txtLoginThrough);
        txtLocation=findViewById(R.id.txtLocation);
        linearIpAddress=findViewById(R.id.linearIPAddress);
        linearLocation=findViewById(R.id.linearLocation);
        recyclerEmpDetails=findViewById(R.id.recyclerEmpDetails);
        btnChoosePhoto=findViewById(R.id.btnChoosePhoto);
        linearLoginProcess=findViewById(R.id.linearLoginProcess);
        linearImage=findViewById(R.id.linearImage);
        dialog = ProgressDialog.show(Attendence.this, "", "Getting approved IP address list", true);
        newThreadInitilization(dialog);
        ipFinder.execute();
        ipFinder.delegate = (AsyncResponse) this;

       // getEmpDetails();
        btnClicked="onCreate";
        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnChoosePhoto.getText().equals("Choose Photo")) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                } else {
                    String dt = new CommonMethods().getDate();
                    String tm = new CommonMethods().getTIme();
                    imgName = counselorname +"_"+tm+""+ext;
                    upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=178&FileName=" + imgName;
                    Log.d("UploadUrl", upLoadServerUri + " " + uploadFilePath + " " + uploadFileName);
                  //  if (temp == 1) {
                        dialog = ProgressDialog.show(Attendence.this, "", "Uploading photo", true);

                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                    }
                                });
                                //to upload photo selected to server
                                uploadFile(uploadFilePath + "" + uploadFileName);
                            }
                        }).start();
                    }
                //}
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                temp=1;
                btnChoosePhoto.setText("Upload");
                Uri selectedImage = data.getData();
                Bitmap bitmap = null;
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imgDoc.setImageBitmap(bitmap);
                String path = getPath(getApplicationContext(), selectedImage);
                uploadFilePath = path.substring(path.indexOf("/storage"), path.lastIndexOf("/") + 1);
                //  Log.d("Path", uploadFilePath);
                uploadFileName = path.substring(uploadFilePath.lastIndexOf("/") + 1);
                ext=uploadFileName.substring(uploadFileName.indexOf("."));
                Log.d("FileName",uploadFilePath+""+uploadFileName+" "+ext);

            }  catch (Exception e)
            {
                // TODO Auto-generated catch block
                Toast.makeText(Attendence.this,"Errorcode-462 PaymentEntry OnActivityResult "+e.toString(),Toast.LENGTH_SHORT).show();
                Log.d("Exception", String.valueOf(e));
            }
        }
    }//onActivityResult
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
    }//getPath



    public void fetch(final Context context1){
        // Check if GPS enabled
        if (gps.canGetLocation()) {
            //get current latitude and longitude of user
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            uploadLatitude = String.valueOf(latitude);
            uploadLongitude = String.valueOf(longitude);
            arrayListLatLng=new ArrayList<>();
            arrayListLng=new ArrayList<>();
            arrayListLat=new ArrayList<>();
            Log.d("HomeLatLng", latitude + " / " + longitude + "\n");
            if(latitude!=0.0&&longitude!=0.0) {
                uploadLatitude = uploadLatitude.substring(0, 7);
                uploadLongitude = uploadLongitude.substring(0, 6);
            }

           /* dlt1=Double.parseDouble("28.6451759");
            dlg1=Double.parseDouble("77.331904");*/
                txtLocation.setText(uploadLatitude + "/" + uploadLongitude);
                dlt1 = Double.parseDouble(uploadLatitude);
                dlg1 = Double.parseDouble(uploadLongitude);
                Log.d("UpadatedLatLng", uploadLatitude + " / " + uploadLongitude + "\n");
                for (int i = 0; i < arrayListApprovedIp.size(); i++)
                {
                    if (arrayListApprovedIp.get(i).contains("/"))
                    {
                        arrayListLatLng.add(arrayListApprovedIp.get(i));
                    }
                }
                Log.d("LatLngArraylist", arrayListLatLng.size() + "");
                for (int i = 0; i < arrayListLatLng.size(); i++) {
                    String latlng = arrayListLatLng.get(i);
                    String lat = latlng.substring(0, latlng.indexOf("/"));
                    String lng = latlng.substring(latlng.indexOf("/") + 1);
                    lat = lat.substring(0, 7);
                    lng = lng.substring(0, 6);
                    arrayListLat.add(lat);
                    arrayListLng.add(lng);
                }
                Log.d("LatArrayLngArray", arrayListLat.size() + "/" + arrayListLng.size());
                //to check whether user is within approved location or not
                for (int i = 0; i < arrayListLat.size(); i++)
                {
                    slt1 = Double.parseDouble(arrayListLat.get(i));
                    slg1 = Double.parseDouble(arrayListLng.get(i));

                    aa = new BigDecimal(slt1);
                    bb = new BigDecimal(dlt1);
                    aa = aa.setScale(4, BigDecimal.ROUND_UP);
                    bb = bb.setScale(4, BigDecimal.ROUND_UP);
                    Log.d("RLat", "" + aa + "/" + bb);
                    BigDecimal llat = aa.subtract(bb);

                    aa1 = new BigDecimal(slg1);
                    bb1 = new BigDecimal(dlg1);
                    aa1 = aa1.setScale(4, BigDecimal.ROUND_UP);
                    bb1 = bb1.setScale(4, BigDecimal.ROUND_UP);
                    Log.d("RLat", "" + aa1 + "/" + bb1);
                    BigDecimal llng = aa1.subtract(bb1);
                    Log.d("LocDiff",llat+" "+llng);

                    uplt1 = Double.parseDouble(String.valueOf(llat));
                    uplg1 = Double.parseDouble(String.valueOf(llng));
                    txtLocation.setText("Location Lat:" + uplt1 + "\n Lng:" + uplg1);
                    if (uplt1>= -0.0005&&uplt1 >= -0.0002 && uplt1 <= 0.0002 && uplg1 >= -0.0002 && uplg1 <= 0.0010&&uplg1>=-0.0011) {

                        Log.d("FLat", "" + llat + "/" + llng);
              /*if(uplt1.equals("0.0000")||uplt1.equals("0.0001")||uplt1.equals("0.0002")||uplt1.equals("-0.0001")||uplt1.equals("-0.0002")) {
                  Log.d("Entered","ltif");
                  if (uplg1.equals("0.0000")||uplg1.equals("0.0001")||uplg1.equals("0.0002")||uplg1.equals("-0.0001")||uplg1.equals("-0.0002")||uplg1.equals("0.0010")||uplg1.equals("-0.0010"))*/

                        txtLoginThrough.setText("Login through Location");
                        //\n ApprovedRLat:"+aa+""+"  CurrentRLat:"+bb+" \n ApprovedtRLog:"+aa1+"  CurrentRLog:"+bb1+"\n Diff:"+uplt1+"/"+uplg1);
                        Log.d("Entered", "lgif");
                        setTrue = 1;
                    } else {
                        setFalse = 1;
                    }

                    // flt1=(slt1)-(dlt1);
                    //Log.d("DiffLAT",Double.compare(dlt1,slt1)+"");
                    /*String diff=String.valueOf(flt1);
                     */
                }
            Log.d("setTrueLoc", "" + setTrue + " " + btnClicked);
            if (setTrue == 1) {
               // txtLoginThrough.setText("Login through Location");
                //\n ApprovedRLat:"+aa+""+"  CurrentRLat:"+bb+" \n ApprovedtRLog:"+aa1+"  CurrentRLog:"+bb1+"\n Diff:"+uplt1+"/"+uplg1);
                linearIpAddress.setVisibility(View.GONE);
                if (btnClicked.equals("onCreate")) {
                    //to get current date from server
                    getCurrentDate();
                } else if (btnClicked.equals("Login")) {
                    dialog.dismiss();
                    if (count == 1) {
                        if(temp==1) {
                            imgLogin.setVisibility(View.GONE);
                            String dt = new CommonMethods().getDate();
                            String tm = new CommonMethods().getTIme();
                            imgName = counselorname +"_"+tm+""+ext;
                            String loginurl = clienturl + "?clientid=" + clientid + "&caseid=177&CounselorID='" + counselorid +"'&IsLogin='1'&Ipaddress='" + systemIp + "'&LogoutIpAddress='0.0.0.0'&LoginImage='" + imgName + "'&LogoutImage='no image'";
                            dialog = ProgressDialog.show(Attendence.this, "", "Logging in", true);
                            newThreadInitilization(dialog);
                            //after clicked on Login button details will be inserted to database
                            insertLoginTime(imgName);
                        }else {
                            Toast.makeText(Attendence.this,"Select photo first",Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (btnClicked.equals("Logout")) {
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        dialog.dismiss();
                        Log.d("CLiked", "111");
                        String date11 = simpleDateFormat.format(new Date());
                        //   String logintime11 = sp.getString("LoginTime", "");
                        //  Log.d("CurrentDate**", logintime11);
                        Date date1 = simpleDateFormat.parse(logintime);
                        Date date2 = simpleDateFormat.parse(date11);
                        //to check login time and current time,when half day  and full day will complete
                        printDifference(date1, date2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                txtNotConnected.setVisibility(View.VISIBLE);
                txtNotConnected.setText("You are not within approved location");
                imgLogin.setVisibility(View.GONE);
                imgLogout.setVisibility(View.GONE);
            }
            } else {
                Toast.makeText(context1, "Please turn on location", Toast.LENGTH_SHORT).show();
            }



    /* }
        }, 0, TIME_INTERVAL);*/
    }//fetch
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(Attendence.this, Home.class);
        intent.putExtra("Activity", "CounselorNotification");
        startActivity(intent);
        finish();
    }

    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        final long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
        alertDialogBuilder.setTitle("Worktime completed="+elapsedHours+" hrs:"+elapsedMinutes+"mins:"+elapsedSeconds+"secs")
                .setMessage("Are you sure you want to logout?")


                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                        if(temp==1) {
                            String dt = new CommonMethods().getDate();
                            String tm = new CommonMethods().getTIme();
                            imgName = counselorname + "_" + tm+""+ext;
                            updateLogout(imgName);
                        }else {
                            Toast.makeText(Attendence.this,"Select photo first",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setCancelable(false).show();
    }

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
                                Toast.makeText(Attendence.this, "Something is wrong, Please try again.", Toast.LENGTH_SHORT).show();
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

    public void getEmpDetails()
    { try {
            String url=clienturl+"?clientid=" + clientid + "&caseid=176";
            Log.d("EmpDetailsUrl", url);
            arrayListEmpDetails=new ArrayList<>();
            if(CheckInternet.checkInternet(Attendence.this))
            {
                if(CheckServer.isServerReachable(Attendence.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                    Log.d("EmpDetailsResponse", response);
                                    try {
                                        if(response.contains("[]"))
                                        {
                                          //  empno="NA";
                                            empid="NA";
                                            empname="NA";
                                            empdob="NA";

                                         }else
                                         {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                             //  empno = jsonObject1.getString("EmpNo");
                                                empid = jsonObject1.getString("EmpIdNo");
                                                empname = jsonObject1.getString("EmpFirstName") + " " + jsonObject1.getString("EmpLastName");
                                                /*empjoindate = jsonObject1.getString("EmpJoinDate");
                                                empjoindate = empjoindate.substring(9, empjoindate.lastIndexOf(" "));
                                                empmobile = jsonObject1.getString("EmpMobileNo");
                                                empemail = jsonObject1.getString("EmpEmailId");*/
                                                empdob = jsonObject1.getString("EmpDOB");
                                                empdob = empdob.substring(9, empdob.lastIndexOf(" "));
                                               /* empaddress = jsonObject1.getString("EmpAddress");
                                                empcity = jsonObject1.getString("EmpCity");
                                                empstate = jsonObject1.getString("EmpState");
                                                emppin = jsonObject1.getString("EmpPinCode");
                                                empgender = jsonObject1.getString("EmpGender");
                                                emppan = jsonObject1.getString("EmpPanCard");
                                                empadhar = jsonObject1.getString("EmpAadhaarCard");
                                                empaccname = jsonObject1.getString("EmpAccountName");
                                                empaccno = jsonObject1.getString("EmpAccountNo");
                                                empifscno = jsonObject1.getString("EmpIfscCode");
                                                empbankname = jsonObject1.getString("EmpBankName");
                                                empbranchname = jsonObject1.getString("EmpBranchName");
                                                empisactive = jsonObject1.getString("IsActive");
                                                counselorid = jsonObject1.getString("cCounselorID");
                                                empdesignation = jsonObject1.getString("EmpDesignation");
                                                empdepartment = jsonObject1.getString("EmpDepartment");
                                                empleavebal = jsonObject1.getString("Leavebal");*/
                                               DataEmpDetails dataEmpDetails=new DataEmpDetails(empid,empname,empdob,"imgProfile");
                                               arrayListEmpDetails.add(dataEmpDetails);
                                            }
                                         AdapterEmpDetails adapterEmpDetails=new AdapterEmpDetails(Attendence.this,arrayListEmpDetails);
                                            LinearLayoutManager layoutManager=new LinearLayoutManager(Attendence.this);
                                            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                            recyclerEmpDetails.setLayoutManager(layoutManager);
                                            recyclerEmpDetails.setAdapter(adapterEmpDetails);
                                            adapterEmpDetails.notifyDataSetChanged();
                                         /*   txtEmpNo.setText(empno);
                                            txtEmpID.setText(empid);
                                            txtEmpName.setText(empname);
                                            txtEmpJoindate.setText(empjoindate);
                                            txtEmpMobile.setText(empmobile);
                                            txtEmpEmail.setText(empemail);
                                            txtEmpDOB.setText(empdob);
                                            txtEmpAddress.setText(empaddress);
                                            txtEmpCity.setText(empcity);
                                            txtEmpState.setText(empstate);
                                            txtEmpPin.setText(emppin);
                                            txtEmpGender.setText(empgender);
                                            txtEmpPanno.setText(emppan);
                                            txtEmpAdharno.setText(empadhar);
                                            txtEmpAccName.setText(empaccname);
                                            txtEmpAccNo.setText(empaccno);
                                            txtEmpBankName.setText(empbankname);
                                            txtEmpBranchName.setText(empbranchname);
                                            txtEmpIFSCNo.setText(empifscno);
                                            txtEmpIsactive.setText(empisactive);
                                            txtEmpDesignation.setText(empdesignation);
                                            txtEmpDepartment.setText(empdepartment);
                                            txtEmpLeavebal.setText(empleavebal);
                                            txtCounselorID.setText(counselorid);*/
                                        }

                                        //getPendingApproval();
                                    } catch (JSONException e) {
                                        Toast.makeText(Attendence.this,"Errorcode-555 EmpDetails getEmpDetailsResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Log.d("CounselorDetailExceptio", String.valueOf(e));
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
                                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                                        alertDialogBuilder.setTitle("Network issue!!!")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                        dialog.dismiss();
                                        Toast.makeText(Attendence.this, "Network issue", Toast.LENGTH_SHORT).show();
                                        // showCustomPopupMenu();
                                        Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                    }
                                }
                            });
                    requestQueue.add(stringRequest);
                }else
                {
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                    alertDialogBuilder.setTitle("Network issue!!!!")
                            .setMessage("Try after some time!")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
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
        }catch (Exception e)
        {
            Toast.makeText(Attendence.this,"Errorcode-554 EmpDetails getEmpDetails "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcApprovedLeaves", String.valueOf(e));
        }
    }//getEmpDetails

    public void getEmpIPList()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=139";
            Log.d("EmpListUrl", url);
            arrayListApprovedIp=new ArrayList<>();
            if (CheckInternet.checkInternet(Attendence.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("EmpListResponse", response);
                                try {
                                      //  dialog.dismiss();

                                       // txtLogin.setVisibility(View.GONE);
                                       // txtLogout.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            approvedIp=jsonObject1.getString("cIpAddress");
                                            arrayListApprovedIp.add(approvedIp);
                                        }
                                       // txtApprovedIp.setText("Approved IP:"+"\n");
                                    ArrayAdapter<String> arrayAdapterCourse = new ArrayAdapter<>(Attendence.this, R.layout.spinner_item1, arrayListApprovedIp);

                                    // Apply the adapter to the spinner
                                    listViewApprovedIP.setAdapter(arrayAdapterCourse);

                                       /* for(int i=0;i<arrayListApprovedIp.size();i++)
                                        {
                                            txtApprovedIp.append(arrayListApprovedIp.get(i) + "\n");
                                        }
*/

                                   txtSystemIp.setText(systemIp);
                                    //to check that if current ip of wifi is present in approved ip list or not
                                       /* for(int i=0;i<arrayListApprovedIp.size();i++)
                                        {
                                            if(arrayListApprovedIp.get(i).equals(systemIp))
                                            {
                                                setTrue=1;
                                            }
                                            else
                                            {
                                                setFalse=1;
                                            }
                                        }
                                        Log.d("setTrueIp",""+setTrue+" "+btnClicked);

                                    if(setTrue==1)//if user is within approved ip address
                                    {
                                        txtLoginThrough.setText("Login through IP");
                                        linearLocation.setVisibility(View.GONE);*/
                                        if(btnClicked.equals("onCreate")) {
                                            getCurrentDate();
                                        }
                                        else if(btnClicked.equals("Login"))
                                        {
                                            dialog.dismiss();
                                            if (count == 1) {
                                                if(temp==1) {
                                                    String dt = new CommonMethods().getDate();
                                                    String tm = new CommonMethods().getTIme();
                                                    imgName = counselorname +"_" + tm+""+ext;
                                                    imgLogin.setVisibility(View.GONE);
                                                    String loginurl = clienturl + "?clientid=" + clientid + "&caseid=177&CounselorID='" + counselorid +"'&IsLogin='1'&Ipaddress='" + systemIp + "'&LogoutIpAddress='0.0.0.0'&LoginImage='"+imgName+"'&LogoutImage='no image'";
                                                    dialog = ProgressDialog.show(Attendence.this, "", "Logging in", true);
                                                    newThreadInitilization(dialog);
                                                    //after clicked on Login button time will be inserted to database
                                                    insertLoginTime(imgName);
                                                }else {
                                                    Toast.makeText(Attendence.this,"Select photo first",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        else if(btnClicked.equals("Logout"))
                                        {
                                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try {
                                                dialog.dismiss();
                                                Log.d("CLiked", "111");
                                                String date11 = simpleDateFormat.format(new Date());
                                             //   String logintime11 = sp.getString("LoginTime", "");
                                              //  Log.d("CurrentDate**", logintime11);
                                                Date date1 = simpleDateFormat.parse(logintime);
                                                Date date2 = simpleDateFormat.parse(date11);
                                                //to check login time and current time,when half day  and full day will complete
                                                printDifference(date1, date2);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                   /* }
                                    else
                                    {
                                        dialog.dismiss();
                                       // txtNotConnected.setVisibility(View.VISIBLE);
                                        imgLogin.setVisibility(View.GONE);
                                        imgLogout.setVisibility(View.GONE);
                                        //to fetch current latitude and longitude of user
                                        fetch(Attendence.this);
                                    }*/



                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    Toast.makeText(Attendence.this,"Errorcode-522 Attendence getEMPIPListResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;

                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(Attendence.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
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
        }catch (Exception e)
        {
            dialog.dismiss();
            Toast.makeText(Attendence.this,"Errorcode-521 Attendence getEMPIPList "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//getEmpIPList
    public void getCurrentDate()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=140";
            Log.d("CurrentDateUrl", url);
            if (CheckInternet.checkInternet(Attendence.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("CurrentDateResponse", response);
                                try {

                                   // dialog.dismiss();

                                      //  txtLogin.setVisibility(View.GONE);
                                      //  txtLogout.setVisibility(View.VISIBLE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("Json", jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        Log.d("Length", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            currentDate=jsonObject1.getString("login Time");
                                        }
                                   /* txtNotConnected.setVisibility(View.GONE);
                                    txtLogin.setVisibility(View.VISIBLE);
                                    txtLogout.setVisibility(View.VISIBLE);*/
                                        getLoginLogoutInfo();
                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    Toast.makeText(Attendence.this,"Errorcode-524 Attendence getCurrentDateResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;

                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(Attendence.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();

                                dialog.dismiss();

                            }
                        }).show();
            }
        }catch (Exception e)
        {
            dialog.dismiss();
            Toast.makeText(Attendence.this,"Errorcode-523 Attendence getCurrentDate "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//getCurrentDate

    public void getLoginLogoutInfo()
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=142&CounselorID="+counselorid;
            Log.d("LoginLogoutUrl", url);
            if (CheckInternet.checkInternet(Attendence.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("LoginLogoutResponse", response);
                                try {
                                   // dialog.dismiss();
                                    txtNotConnected.setVisibility(View.GONE);
                                        if(response.contains("[]"))
                                        {
                                            imgLogout.setVisibility(View.GONE);
                                            imgLogin.setVisibility(View.VISIBLE);
                                        }
                                        else {

                                            JSONObject jsonObject = new JSONObject(response);
                                            Log.d("Json", jsonObject.toString());
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.d("Length", String.valueOf(jsonArray.length()));
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                 logintime = jsonObject1.getString("Android Time");
                                            }
                                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                          /*  try {
                                                date11 = simpleDateFormat.format(new Date());
                                                editor = sp.edit();
                                                //String date12 = simpleDateFormat.format(date11);

                                                Log.d("LoginDate**", date11);
                                                editor.putString("LoginTime", date11);
                                                editor.commit();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            */


                                            Date date = null;
                                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try {
                                                date = simpleDateFormat.parse(logintime);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(date);
                                            calendar.add(Calendar.HOUR,5 );
                                            Calendar calendar1=Calendar.getInstance();
                                            calendar1.setTime(date);
                                            calendar1.add(Calendar.HOUR,9);
                                            String halfday=simpleDateFormat.format(calendar.getTime());
                                            String fullday=simpleDateFormat.format(calendar1.getTime());

                                            System.out.println("Time here "+calendar.getTime());
                                            txtHalfDay.setText(halfday);
                                            txtFullDay.setText(fullday);
                                            imgLogin.setVisibility(View.GONE);
                                            imgLogout.setVisibility(View.VISIBLE);
                                            linearLoginTime.setVisibility(View.VISIBLE);
                                            linearHalfFullTime.setVisibility(View.VISIBLE);

                                            txtLoginTime.setText(date11);
                                            editor.putString("HalfTime",halfday);
                                            editor.putString("FullTime",fullday);
                                            editor.commit();

                                           /* imgLogin.setVisibility(View.GONE);
                                            imgLogout.setVisibility(View.VISIBLE);*/
                                            hftime=sp.getString("HalfTime","");
                                            fltime=sp.getString("FullTime","");
                                            String ltime=sp.getString("LoginTime","");
                                            txtLoginTime.setText(logintime);
                                            txtHalfDay.setText(hftime);
                                            txtFullDay.setText(fltime);
                                            linearLoginTime.setVisibility(View.VISIBLE);
                                            linearHalfFullTime.setVisibility(View.VISIBLE);
                                        }
                                    dialog.dismiss();

                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    Toast.makeText(Attendence.this,"Errorcode-526 Attendence getLoginLogoutInfoResponse "+e.toString(),Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;

                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(Attendence.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();

                                dialog.dismiss();

                            }
                        }).show();
            }
        }catch (Exception e)
        {
            dialog.dismiss();
            Toast.makeText(Attendence.this,"Errorcode-525 Attendence getLoginLogoutInfo "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//getLoginLogoutInfo

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
                    Toast.makeText(Attendence.this, "Source File not exist :"
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
                if(serverResponseCode==500)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtLoginThrough.setText("File not Uploaded");
                            Toast.makeText(Attendence.this, "File not uploaded.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if (serverResponseCode == 200) {

                    //getLoginLogoutInfo();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            linearLoginProcess.setVisibility(View.VISIBLE);
                            txtLoginThrough.setText("File Uploaded Succesfully");
                            Toast.makeText(Attendence.this, "File Upload Complete.",
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
                        Toast.makeText(Attendence.this, "MalformedURLException",
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
                        Toast.makeText(Attendence.this, "Got Exception while uploading record ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }//uploadFile

   /* public void insertLoginDetails(String url, final String imgName1)
    {
        try {
            Log.d("InsertLoginTimeUrl", url);
            if (CheckInternet.checkInternet(Attendence.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                Log.d("InsertLoginTimeResponse", response);
                                if(response.contains("Data inserted successfully"))
                                {
                                    //  String currentDateandTime = simpleDateFormat.format(new Date());
                                    upLoadServerUri = clienturl + "?clientid=" + clientid + "&caseid=178&FileName=" + imgName1;
                                    Log.d("UploadUrl",upLoadServerUri+" "+uploadFilePath+" "+uploadFileName);
                                    if (temp == 1) {
                                        dialog = ProgressDialog.show(Attendence.this, "", "Uploading photo", true);

                                        new Thread(new Runnable() {
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                    }
                                                });
                                                //to upload photo of receipt selected to server
                                                uploadFile(uploadFilePath + "" + uploadFileName);

                                            }
                                        }).start();
                                    }
                                    if (btnClicked.equals("Login")) {
                                        Toast.makeText(Attendence.this, "Logged in", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(Attendence.this, "Logged out", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    imgLogin.setVisibility(View.VISIBLE);
                                    imgLogout.setVisibility(View.GONE);
                                    Toast.makeText(Attendence.this,"Log in failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;
                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(Attendence.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();

                                dialog.dismiss();

                            }
                        }).show();
            }
        }catch (Exception e)
        {
            dialog.dismiss();
            Toast.makeText(Attendence.this,"Errorcode-527 Attendence insertLoginTime "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString()); }
    }//insertLoginDetails*/
   public void insertLoginTime(String loginImage)
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=143&CounselorID="+counselorid+"&IPaddress1="+systemIp+"&LoginImage="+loginImage;
            Log.d("InsertLoginTimeUrl", url);
            if (CheckInternet.checkInternet(Attendence.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                Log.d("InsertLoginTimeResponse", response);
                              if(response.contains("Data inserted successfully"))
                                {
                                  //  String currentDateandTime = simpleDateFormat.format(new Date());

                                    getLoginLogoutInfo();
                                    Toast.makeText(Attendence.this,"Logged in",Toast.LENGTH_SHORT).show();
                                }
                              else {
                                  imgLogin.setVisibility(View.VISIBLE);
                                  imgLogout.setVisibility(View.GONE);
                                  Toast.makeText(Attendence.this,"Log in failed",Toast.LENGTH_SHORT).show();
                              }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;
                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(Attendence.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();

                                dialog.dismiss();

                            }
                        }).show();
            }
        }catch (Exception e)
        {
            dialog.dismiss();
            Toast.makeText(Attendence.this,"Errorcode-527 Attendence insertLoginTime "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//insertLoginTime
    public void updateLogout(String logoutImage)
    {
        try {
            url = clienturl + "?clientid=" + clientid + "&caseid=144&CounselorID="+counselorid+"&IPaddress1="+systemIp+"&LogoutImage="+logoutImage;
            Log.d("InsertLoginTimeUrl", url);
            if (CheckInternet.checkInternet(Attendence.this)) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                Log.d("InsertLoginTimeResponse", response);
                                if(response.contains("Data inserted successfully"))
                                {

                                    //txtLogout.setVisibility(View.GONE);
                                   /* editor.putString("LoginTime","");
                                    editor.commit();*/
                                   // txtLogin.setVisibility(View.VISIBLE);
                                    Toast.makeText(Attendence.this,"Logged out",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(Attendence.this,"Log out failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;

                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(Attendence.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }
                            }
                        });
                requestQueue.add(stringRequest);
            } else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
                alertDialogBuilder.setTitle("No Internet connection!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();

                                dialog.dismiss();

                            }
                        }).show();
            }
        }catch (Exception e)
        {
            dialog.dismiss();
            Toast.makeText(Attendence.this,"Errorcode-528 Attendence updateLogout "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcClgList",e.toString());
        }
    }//updateLogout

    @Override
    public void processFinish(String output) {
        systemIp="";
        systemIp=output;
        if (CheckInternetSpeed.checkInternet(Attendence.this).contains("0")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
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
        } else if (CheckInternetSpeed.checkInternet(Attendence.this).contains("1")) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Attendence.this);
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
            //to get approved ip and location list
            getEmpIPList();
        }
    }//processFinish

}
