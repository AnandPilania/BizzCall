package com.bizcall.wayto.mentebit;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;

public class MyAlarm extends BroadcastReceiver {
    double currentLat, currentLong;
    AlertDialog.Builder builder;
    AlertDialog alert;
    private Timer timer = new Timer();
    private LatLng latlang;
    String clienturl,clientid,strIMEI1,strIMEI2,counselorid;
    RequestQueue requestQueue;
    SharedPreferences sp;

    final int TIME_INTERVAL =5000;
    int i=0,i1=0;
    MapsActivity mapsActivity;
    GoogleMap googleMap;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    GPSTracker gps,gps1;
    Context context1;
    CountDownTimer countDownTimer;
     // Handler used to execute code on the UI thread

    @Override
    public void onReceive(final Context context, Intent intent) {
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        strIMEI1 = sp.getString("IMEI1", null);
        strIMEI2 = sp.getString("IMEI2", null);
        clientid = sp.getString("ClientId", null);
        clienturl = sp.getString("ClientUrl", null);
        counselorid=sp.getString("Id",null);
        counselorid=counselorid.replace(" ","");

        context1=context;
        gps = new GPSTracker(context);
        requestQueue = Volley.newRequestQueue(context);

        countDownTimer=new CountDownTimer(500,500) {
            @Override
            public void onTick(long millisUntilFinished) {
                fetch(context);
            }
            @Override
            public void onFinish() {
                if(countDownTimer!=null)
                {
                    countDownTimer.cancel();
                    countDownTimer=null;
                }
            }
        }.start();

                //Toast.makeText(context, "Toast from broadcast receiver", Toast.LENGTH_SHORT).show();





        /* if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);*/
              //  mFusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null);

              /*  Task<Location> task=mFusedLocationClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        // GPS location can be null if GPS is switched off

                        try {
                            currentLat = location.getLatitude();
                            currentLong = location.getLongitude();
                            Log.d("coor", currentLat + " / " + currentLong);

                            latlang = new LatLng(location.getLatitude(), location.getLongitude());

                            String uploadLatitude = String.valueOf(currentLat);
                            String uploadLongitude = String.valueOf(currentLong);

                            //Upload Data using WebService.
                            uploadLocation(context,strIMEI2, strIMEI1, uploadLatitude, uploadLongitude);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            //Toast.makeText(MapsActivity.this, "Enable the Location Service", Toast.LENGTH_SHORT).show();
                            builder = new AlertDialog.Builder(context);
                            builder.setTitle("Location Service")
                                    .setMessage("Please Enable your location service.")
                                    .setCancelable(false)
                                    .setCancelable(false);
                            // Setting Positive "OK" Button
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    context.startActivity(callGPSSettingIntent);
                                }
                            });
                            alert = builder.create();
                            try {
                                alert.show();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(context, "ErrorCode-1708: Reference Location Permission Alert", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });*/

    }
    public void fetch(final Context context1){
        // Check if GPS enabled
        if (gps.canGetLocation()) {
                           double latitude = gps.getLatitude();
                     double longitude = gps.getLongitude();
                     String uploadLatitude = String.valueOf(latitude);
                     String uploadLongitude = String.valueOf(longitude);

                     // \n is for new line
                     Log.d("laglng", latitude + " / " + longitude+"\n"+i++);
                    //  Toast.makeText(context1, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
                     uploadLocation(context1,counselorid, uploadLatitude, uploadLongitude);
                 } else {
                        Toast.makeText(context1,"Please turn on location",Toast.LENGTH_SHORT).show();
                     // Can't get location.
                     // GPS or network is not enabled.
                     // Ask user to enable GPS/network in settings.
                     //gps.showSettingsAlert();
                 }


    /* }
        }, 0, TIME_INTERVAL);*/
    }

    public void uploadLocation(final Context context,String counselorid, final String latitude, final String longitude) {

       /* if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        } else {*/
            String strurl = clienturl + "?clientid=" + clientid + "&caseid=93&latitude=" + latitude + "&longitude=" + longitude+"&CounselorID=" + counselorid;
            Log.d("uploadurl", strurl);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.contains("Data inserted successfully")) {
                        Log.d("sts", "harsha"+i1++);
                       // Toast.makeText(context, "Location Upload"+"\nlat    : " + currentLat + "\nlong : " + currentLong, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Record not inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("urlcount ", "&latitude=" + latitude + " / " + "&longitude=" + longitude + " / " + response);
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
    //}
}

