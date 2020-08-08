package com.bizcall.wayto.mentebit13;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;
    private Timer timer = new Timer();
    private TelephonyManager telephonyManager;
    private RequestQueue requestQueue;
    private LatLng latlang;
    private Calendar calendar;
    private SimpleDateFormat sdfComapre;
    private ProgressDialog progressBar;

    double currentLat, currentLong;
    private int day, month, year;
    String strIMEI1, strIMEI2, stredtDate;
    EditText edtDatePicker;
    Button btnSearchMarker;
    AlertDialog.Builder builder;
    AlertDialog alert;
    ArrayList<LatLng> listPoints;
    public static  SupportMapFragment mapFragment;
    TextView txtMin,txtMax,txtShowInfo;
    Button btnNext,btnPrevious;
    String clienturl,clientid,counselorid,IMEINumber1,IMEINumber2,strMin,strMax;
    SharedPreferences sp;
    ImageView imgBack,imgRefresh;
    LinearLayout linearSpinner,linearUnderSpinner;

    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        txtMax=findViewById(R.id.txtMax);
        txtMin=findViewById(R.id.txtMin);
        btnPrevious=findViewById(R.id.btnPrevious);
        btnNext=findViewById(R.id.btnNext);
        txtShowInfo=findViewById(R.id.txtShowInfo);
        imgBack=findViewById(R.id.img_back);
        imgRefresh=findViewById(R.id.imgRefresh);
        linearSpinner=findViewById(R.id.linearSpinner);
        linearUnderSpinner=findViewById(R.id.linearUnderCounselor);
        linearUnderSpinner.setVisibility(View.VISIBLE);
        linearSpinner.setVisibility(View.GONE);

        sp=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid=sp.getString("ClientId",null);
        clienturl=sp.getString("ClientUrl",null);
        IMEINumber1 = sp.getString("IMEI1", null);
        IMEINumber2 = sp.getString("IMEI2", null);
        counselorid = sp.getString("Id", null);

        strMin="1";
        strMax="25";
        txtMin.setText(strMin);
        txtMax.setText(strMax);
        txtShowInfo.setText("Displaying "+txtMin.getText().toString()+"-"+txtMax.getText().toString());


        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            }
        }


        edtDatePicker = findViewById(R.id.edt_datepicker);
        btnSearchMarker = findViewById(R.id.btn_markers);
        listPoints = new ArrayList<>();

        calendar = Calendar.getInstance();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapsActivity.this,MapsActivity.class);
               // intent.putExtra("Activity","Location");
                startActivity(intent);
            }
        });
        edtDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtDatePicker.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                        sdfComapre = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            String strDate = edtDatePicker.getText().toString();
                            Date dtMydate = sdfComapre.parse(strDate);
                            stredtDate = sdfComapre.format(dtMydate);

                            Log.d("dates", stredtDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };
                DatePickerDialog dpDialog = new DatePickerDialog(MapsActivity.this, listener, year, month, day);
                dpDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                dpDialog.show();
            }
        });

        btnSearchMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(MapsActivity.this);
                listPoints.clear();
                  if (edtDatePicker.getText().toString().isEmpty()) {
                    //edtDatePicker.setError("Select date first");
                    Toast.makeText(MapsActivity.this, "Select date first", Toast.LENGTH_SHORT).show();
                   // progressBar.dismiss();
                } else {
                    if(CheckInternetSpeed.checkInternet(MapsActivity.this).contains("0")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MapsActivity.this);
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
                    else if(CheckInternetSpeed.checkInternet(MapsActivity.this).contains("1")) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MapsActivity.this);
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
                        progressBar =ProgressDialog.show(MapsActivity.this,""," Fetching Records...",true);
                        getLocation(strMin, strMax);
                    }
                   /* String strFetchUrl = "http://anilsahasrabuddhe.in/rohit-testing/locationshownew.php";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strFetchUrl,
                            null, new success(), new fail());
                    requestQueue.add(jsonObjectRequest);*/
                }
                  btnNext.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          btnNextClicked();
                      }
                  });
                  btnPrevious.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          btnPreviousClicked();
                      }
                  });

                for (int i = 0; i < listPoints.size(); i++) {
                    mMap.addPolyline(new PolylineOptions().add(
                            //listPoints.get(i)
                            new LatLng(18.5081, 73.8313),
                            new LatLng(18.452906, 73.861175)
                    ).width(10).color(Color.BLUE));
                    Log.d("newpoint", listPoints.get(i) + "");
                }

                /*if (listPoints.size()<2){
                    listPoints.clear();
                    mMap.clear();
                } else {
                    for (int i=0; i<listPoints.size()-1; i++) {
                        String url = getRequestUrl(listPoints.get(i), listPoints.get(i+1));
                        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                        taskRequestDirections.execute(url);
                        Log.d("sendurl", url + "");
                    }
                }*/

                /*String url = getRequestUrl(new LatLng(18.5081,73.8313), new LatLng(18.452906, 73.861175));
                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                taskRequestDirections.execute(url);*/
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //-----------------------------------Start Service--------------------------------------------
      /*  Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("inputExtra", "Location Fetch");
        ContextCompat.startForegroundService(MapsActivity.this, serviceIntent);*/
    }

    //-----------------------------------Start Service--------------------------------------------
    /*public void startService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("inputExtra", "Location Fetch");
        ContextCompat.startForegroundService(MapsActivity.this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(MapsActivity.this, ExampleService.class);
        stopService(serviceIntent);
    }*/
    //--------------------------------------------------------------------------------------------

    @SuppressLint("HardwareIds")
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final int TIME_INTERVAL = 1800000; //half hour.1800000
        init();

       /* timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        // GPS location can be null if GPS is switched off
                        try {
                            currentLat = location.getLatitude();
                            currentLong = location.getLongitude();
                            Log.d("coor", currentLat + " / " + currentLong);

                            latlang = new LatLng(location.getLatitude(), location.getLongitude());
                            //mMap.addMarker(new MarkerOptions().position(latlang).title("My position"));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latlang));

                            String uploadLatitude = String.valueOf(currentLat);
                            String uploadLongitude = String.valueOf(currentLong);

                            //Upload Data using WebService.
                            uploadLocation(strIMEI2, strIMEI1, uploadLatitude, uploadLongitude);
                            //Toast.makeText(MapsActivity.this, "Location Upload"+"\nlat    : " + currentLat + "\nlong : " + currentLong, Toast.LENGTH_SHORT).show();

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            //Toast.makeText(MapsActivity.this, "Enable the Location Service", Toast.LENGTH_SHORT).show();
                            builder = new AlertDialog.Builder(MapsActivity.this);
                            builder.setTitle("Location Service")
                                    .setMessage("Please Enable your location service.")
                                    .setCancelable(false)
                                    .setIcon(R.drawable.exclamationicon)
                                    .setCancelable(false);
                            // Setting Positive "OK" Button
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
                            alert = builder.create();
                            try {
                                alert.show();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(MapsActivity.this, "ErrorCode-1708: Reference Location Permission Alert", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 0, TIME_INTERVAL);*/
        mMap.clear();
    }

    @Override
    public void onBackPressed() {
       /* if(progressBar.isShowing())
        {
            progressBar.dismiss();
        }*/
        Intent intent=new Intent(MapsActivity.this, Home.class);
        intent.putExtra("Activity","Location");
        startActivity(intent);
        finish();
    }

    public void btnPreviousClicked(){
        try {
            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) - 25);
            strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) - 25);
            txtMin.setText(strMin);
            txtMax.setText(strMax);
                     /* searchbool=1;
                        searchbooltext=searchtext;
                        searchboolAs=searchAs;
                        orderboolAs=searchAs + " ASC";*/

            txtShowInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
            if(CheckInternetSpeed.checkInternet(MapsActivity.this).contains("0")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MapsActivity.this);
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
            else if(CheckInternetSpeed.checkInternet(MapsActivity.this).contains("1")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MapsActivity.this);
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
                progressBar =ProgressDialog.show(MapsActivity.this,""," Fetching Records...",true);
                getLocation(txtMin.getText().toString(), txtMax.getText().toString());
            }
        }catch (Exception e)
        {
            Toast.makeText(MapsActivity.this,"Errorcode-392 SearchAllActivity btnPreviousClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void  btnNextClicked() {
        try {

            strMin = String.valueOf(Integer.parseInt(txtMin.getText().toString()) + 25);
                strMax = String.valueOf(Integer.parseInt(txtMax.getText().toString()) + 25);
                txtMin.setText(strMin);
                txtMax.setText(strMax);
                if(CheckInternetSpeed.checkInternet(MapsActivity.this).contains("0")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MapsActivity.this);
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
                else if(CheckInternetSpeed.checkInternet(MapsActivity.this).contains("1")) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MapsActivity.this);
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
                    progressBar =ProgressDialog.show(MapsActivity.this,""," Fetching Records...",true);
                    getLocation(txtMin.getText().toString(), txtMax.getText().toString());
                }


                // refreshWhenLoading();


            txtShowInfo.setText("Displaying " + txtMin.getText().toString() + "-" + txtMax.getText().toString());
        }catch (Exception e)
        {
            Toast.makeText(MapsActivity.this,"Errorcode-393 SearchAllActivity btnNextClicked "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    public void getLocation(  String strMin, String strMax) {
        try {
            if(CheckServer.isServerReachable(MapsActivity.this)) {
                //dialog = ProgressDialog.show(CounsellorData.this, "Loading", "Please wait.....", false, true);

               String url = clienturl + "?clientid=" + clientid + "&caseid=92&CounselorID="+counselorid+"&CreatedDate="+stredtDate+"&MinVal=" + strMin + "&MaxVal=" + strMax;
                Log.d("GetLocationurl",url);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.e("Success", String.valueOf(response));
                               // int count = 0;
                                try {
                                    txtShowInfo.setVisibility(View.VISIBLE);
                                    progressBar.dismiss();
                                    if(response.contains("[]"))
                                    {
                                        txtShowInfo.setText("No Record Found");
                                        Toast.makeText(MapsActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        txtShowInfo.setText("Displaying "+txtMin.getText().toString()+"-"+txtMax.getText().toString());

                                        JSONObject jsonObject1 = new JSONObject(response);
                                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String strLatitude = jsonObject.getString("latitude");
                                            String strLongitude = jsonObject.getString("longitude");
                                            // String mIMEI1 = jsonObject.getString("IMEI1");
                                            //String mIMEI2 = jsonObject.getString("IMEI2");

                                      /*  JSONObject mjsonDate = jsonObject.getJSONObject("dtcreatedDate");
                                        String strDate = mjsonDate.getString("date");*/

                                       /* String mDate = strDate.substring(0, 10);
                                        String mFulldate = strDate.substring(0, 19);*/
                                            if (!strLatitude.equals("") && !strLongitude.equals("")) {
                                                double mLatitude = Double.valueOf(strLatitude);
                                                double mLongitude = Double.valueOf(strLongitude);
                                                // Log.d("matchlatlong", stredtDate + " / " + mDate);
                                                /*if (stredtDate.compareTo(mDate) == 0 && strIMEI1.equals(mIMEI2)) {*/
                                                latlang = new LatLng(mLatitude, mLongitude);
                                                Log.d("matchlatlong", latlang + " / " + strIMEI1);

                                                mMap.addMarker(new MarkerOptions().position(latlang));//.title(mFulldate));
                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlang));

                                                listPoints.add(latlang);
                                                //count++;
                                                // }
                                                //progressBar.dismiss();
                                            }
                                        }
                                        // Log.d("matchrecords", count + "");
                                        Log.d("listpoints", listPoints.size() + "");

                                        if (txtMin.getText().toString().equals("1")) {
                                            btnNext.setVisibility(View.GONE);
                                            btnPrevious.setVisibility(View.GONE);
                                            if (Integer.parseInt(txtMax.getText().toString()) <= listPoints.size()) {
                                                btnNext.setVisibility(View.VISIBLE);
                                            } else {
                                                btnNext.setVisibility(View.GONE);

                                            }
                                        } else if (25 > listPoints.size()) {
                                            btnPrevious.setVisibility(View.VISIBLE);
                                            btnNext.setVisibility(View.GONE);
                                        } else {
                                            btnNext.setVisibility(View.VISIBLE);
                                            btnPrevious.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    //Log.d("Size**", String.valueOf(arrayList.size()));
                                } catch (JSONException e) {
                                    progressBar.dismiss();
                                    Toast.makeText(MapsActivity.this, "Errorcode-156 CounselorData getCounselorResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("AllCounselorException", String.valueOf(e));
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
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MapsActivity.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                       progressBar.dismiss();
                                    Toast.makeText(MapsActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                progressBar.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MapsActivity.this);
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
            Toast.makeText(MapsActivity.this,"Errorcode-155 CounselorData getCounselor "+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

   /* private class success implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            Log.e("Success", String.valueOf(response));
            int count = 0;

            try {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String strLatitude = jsonObject.getString("latitude");
                    String strLongitude = jsonObject.getString("longitude");
                    String mIMEI1 = jsonObject.getString("IMEI1");
                    String mIMEI2 = jsonObject.getString("IMEI2");

                    JSONObject mjsonDate = jsonObject.getJSONObject("dtcreatedDate");
                    String strDate = mjsonDate.getString("date");

                    String mDate = strDate.substring(0, 10);
                    String mFulldate = strDate.substring(0, 19);
                    if (!strLatitude.equals("") && !strLongitude.equals("")) {
                        double mLatitude = Double.valueOf(strLatitude);
                        double mLongitude = Double.valueOf(strLongitude);
                        Log.d("matchlatlong", stredtDate + " / " + mDate);
                       *//* if (stredtDate.compareTo(mDate) == 0 && strIMEI1.equals(mIMEI2)) {*//*
                            latlang = new LatLng(mLatitude, mLongitude);
                            Log.d("matchlatlong", latlang + " / " + strIMEI1);

                            mMap.addMarker(new MarkerOptions().position(latlang).title(mFulldate));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlang));

                            listPoints.add(latlang);
                            count++;
                       // }
                        progressBar.dismiss();
                    }
                }
                Log.d("matchrecords", count + "");
                Log.d("listpoints", listPoints.size() + "");

                if (count == 0) {
                    Toast.makeText(MapsActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                progressBar.dismiss();
            }
        }
    }

    private class fail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressBar.dismiss();
            Log.e("Fail", String.valueOf(error));
        }
    }*/

    @SuppressLint("MissingPermission")
    private void init() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }
}