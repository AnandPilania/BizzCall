package com.bizcall.wayto.mentebit;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/*import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;*/

public class Login extends AppCompatActivity /*implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener*/  {

    public static TextView edtName, edtPassword,edtClientId,txtWebsite;
    RequestQueue requestQueue;
    Button btnLogin,btnVerify;
    Intent intent;

    //SMS ////
    ArrayList<SMSDetails> mSMSArrayList;
    RecyclerView recyclerView;
    SMSDetails smsDetails;

    String urlCallDate;
    private ArrayList<CallDetialswithIMEI> mCallArrayList;
    //private ArrayList<SMSDetails> mSMSArrayList;
    private CallDetialswithIMEI callDetails;
    private SimpleDateFormat sdfSaveArray;
    private Calendar calendar;
   // private RequestQueue requestQueue;
    private TelephonyManager telephonyManager;
    //private LatLng latlang;
    //private GoogleMap mMap;

    int counturl = 0,countsmsurl=0;
    int day, month, year;
    double currentLat, currentLong;
    int resultImeiexist=0;

    String callPhoneno, finalCallType, strdateFormated, callDuration, strImei1, strImei2, callphAccID;



    private ArrayList<CallDetialswithIMEI>  mUploadCallArrayList;
    private ArrayList<CallDetailsFetch> mUrlCallArrayList;
    private ArrayList<SMSDetails> mUploadSMSArrayList;
    private ArrayList<SMSDetailsFetch> mUrlSMSArrayList;
    private CallDetialswithIMEI callDetialswithIMEI;
    private CallDetailsFetch callDetailsFetch;
    private SMSDetailsFetch smsDetailsFetch;
    private SimpleDateFormat sdfComapre;
    //private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    String urlSMSDate;
    String  strIMEI1, strIMEI2;
    ProgressDialog dialog;
    String username, pwd, url;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    UrlRequest urlRequest;
    int flag = 0,temp=0;
    LinearLayout linearLogin,linearClientVerify;
        int conditon=1;

    ImageView imgError1, imgError2;
    String clienturl, IMEINumber1,IMEINumber2, clientid;
    String ip,login,counselorId,clientid1;
    long timeout=10000;
    int loginCount;
    TextView txtResponse,txtClientID;


    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    int reqcode=1;
    ArrayList<String> arrayListUrl;
    LinearLayout linearContact;
    String checkPermission;
    final int TIME_INTERVAL = 1800000;
    Timer timer = new Timer();
    String website_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);
            linearClientVerify = findViewById(R.id.linearClientVerify);
            linearLogin = findViewById(R.id.linearLogin);
            txtResponse = findViewById(R.id.txtResponse);
            btnVerify = findViewById(R.id.btnVerify);
            edtClientId = findViewById(R.id.edtClientId);
            linearContact=findViewById(R.id.linearContact);
            txtWebsite=findViewById(R.id.txtWebsite);
            sp = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            requestQueue = Volley.newRequestQueue(Login.this);
            edtName = findViewById(R.id.edtUsername);
            edtPassword = findViewById(R.id.edtPassword);
            txtClientID = findViewById(R.id.txtClientId);
            arrayListUrl = new ArrayList<>();
            if (checkPermission()) {
                Toast.makeText(Login.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            }// if (checkPermission())
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAPTURE_AUDIO_OUTPUT}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
                String manufacturer = "xiaomi";
                if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                    //this will open auto start screen where user can enable permission for your app
                    Intent intent1 = new Intent();
                    intent1.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                    startActivity(intent1);
                }
            }
            //  clientid = "AnDe828500";
            //  timeout=50000L;
            // clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/AnDe828500.php";
            //  loginCount=(sp.getInt("LoginCount",0));

            btnLogin = findViewById(R.id.btnLogin);
            onClick();
            website_url=txtWebsite.getText().toString();
            website_url=website_url.substring(website_url.indexOf("http"));
            Log.d("Website",website_url);
            txtWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website_url));
                    startActivity(browserIntent);
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] per = {Manifest.permission.READ_PHONE_STATE};
                requestPermissions(per, reqcode);

                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

                        IMEINumber1 = tm.getImei(1);
                        IMEINumber2=tm.getImei(2);
                        editor.putString("IMEI1",IMEINumber1);
                        editor.putString("IMEI2",IMEINumber2);
                        editor.commit();
                          Log.d("IMEI**",IMEINumber1+IMEINumber2);
                    }//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                } //(ActivityCompat.checkSelfPermission(
                 else {

                    IMEINumber1 = tm.getDeviceId(2);
                    IMEINumber2=tm.getDeviceId(1);
                        editor.putString("IMEI1",IMEINumber1);
                        editor.putString("IMEI2",IMEINumber2);
                        editor.commit();
                       Log.d("IMEIO**",IMEINumber1+" "+IMEINumber2);
                }// else (ActivityCompat.checkSelfPermission(
            }//(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                if (!fingerprintManager.isHardwareDetected()) {
                    txtResponse.setText("Fingerprint Scanner is not detected in your Device.");
                } else if (ContextCompat.checkSelfPermission(Login.this,
                        Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    txtResponse.setText("Permission not granted to use Fingerprint Scanner.");
                } else if (!keyguardManager.isKeyguardSecure()) {
                    txtResponse.setText("Add Lock to your Mobile from Setting.");
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    txtResponse.setText("You should add atleast 1 fingerprint to use this Application.");
                } else {
                    txtResponse.setText("Place your finger on scanner to access this Application");
                    generateKey();
                    if (cipherInit()) {
                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                        fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                    }
                }
            }//(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)*/
        }//try
        catch (Exception e)
        {
            Toast.makeText(Login.this, "Errorcode-101 Login OnCreate "+e.toString(), Toast.LENGTH_SHORT).show();
        }//catch (Exception e)


    }//onCreate close

    private void setAlarm(){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Login.this, MyAlarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(Login.this,0,intent,0);
        manager.set(AlarmManager.RTC,0,pendingIntent);

       // Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }
    private boolean checkPermission() {
        String[] perm = {Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_SMS,Manifest.permission.READ_CALL_LOG,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE,Manifest.permission.READ_CALL_LOG,Manifest.permission.WRITE_CALL_LOG,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR};
        List<String> reqPerm = new ArrayList<>();
        try {
            int i = 0;
            for (String permis : perm) {
            int resultPhone = ContextCompat.checkSelfPermission(Login.this, permis);
            if (resultPhone == PackageManager.PERMISSION_GRANTED)
                i++;
            else {
                reqPerm.add(permis);
            }
        }
            if (i == 1)
            return true;
        else
            return requestPermission(reqPerm);
        }
        catch (Exception e)
        {
            Log.d("Exception", String.valueOf(e));
            Toast.makeText(Login.this, "Errorcode-102 Login check Permission "+e.toString(), Toast.LENGTH_SHORT).show();
            return requestPermission(reqPerm);
        }
    }


    private boolean requestPermission(List<String> perm) {
        try
        {
        // String[] permissions={Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String[] listReq = new String[perm.size()];
        listReq = perm.toArray(listReq);
        for (String permissions : listReq) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissions)) {
                Toast.makeText(getApplicationContext(), "Phone Permissions needed for " + permissions, Toast.LENGTH_LONG);
            }
        }
        ActivityCompat.requestPermissions(Login.this, listReq, 1);
        return false;
        }
        catch (Exception e)
        {
            Toast.makeText(Login.this, "Errorcode-103 Login Request Permission "+e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
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
    public void getClientDetails(String url) {
        try {
            Log.d("OnlineLeadUrl", url);
            if (CheckInternet.checkInternet(Login.this)) {
                if(CheckServer.isServerReachable(Login.this))
                {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                dialog.dismiss();
                                Log.d("LoginResponse", response);
                                try {
                                    dialog.dismiss();
                                    Log.d("ClientDetailsResponse", response);
                                    if (!response.contains("User not Identified")) {
                                        JSONObject jsonObject = new JSONObject(response);
                                        // Log.d("Json",jsonObject.toString());
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            clienturl = jsonObject1.getString("ClientUrl");
                                            String timeout1 = jsonObject1.getString("TimeOut");
                                            clientid = jsonObject1.getString("ClientId");
                                            timeout = Long.parseLong(timeout1);
                                            Log.d("ClientUrl**", clienturl);
                                            //  editor = sp.edit();
                                            editor.putString("ClientId", clientid);
                                            editor.putLong("TimeOut", timeout);
                                            editor.putString("ClientUrl", clienturl);
                                            editor.commit();
                                        }

                                        linearClientVerify.setVisibility(View.GONE);
                                        if (linearLogin.getVisibility() == View.GONE) {
                                            edtClientId.setText("");
                                            txtClientID.setText("ClientID:" + clientid);
                                            linearLogin.setVisibility(View.VISIBLE);
                                            linearContact.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "Invalid Client", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(Login.this, "Errorcode-104 Login ClientVerifyResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("OnlineLeadException", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if (error == null || error.networkResponse == null)
                                    return;

                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error.equals("TimeoutError") || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
                                    alertDialogBuilder.setTitle("Server Error!!!")
                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }
                            }
                        });
                requestQueue.add(stringRequest);
            }else{
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
                }} else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
           /* if(dialog.isShowing())
            {
                dialog.dismiss();
            }*/
            Toast.makeText(Login.this, "Errorcode-105 Login ClientDetails function"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

        public void onClick ()
        {
            try {
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            username = edtName.getText().toString();
                            Log.d("Name111", username);
                            pwd = edtPassword.getText().toString();
                            // url="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?caseid=4";
                            Log.d("Url111", clienturl + "?clientid=" + clientid + "&caseid=1&UserName=" + username + "&UserPassword=" + pwd);
                            if (username.length() == 0) {
                                edtName.setError("Please enter name");
                                flag = 1;
                            }
                            if (pwd.length() == 0) {
                                edtPassword.setError("Please enter password");
                                flag = 1;
                            }
                            if (flag == 0) {
                                if (CheckInternetSpeed.checkInternet(Login.this).contains("0")) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
                                } else if (CheckInternetSpeed.checkInternet(Login.this).contains("1")) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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

                                    dialog = ProgressDialog.show(Login.this, "", "Logging in", false, true);
                                    checkData();
                                }

                                //refreshWhenLoading();
                            }
                            flag = 0;
                        } catch (Exception e) {
                            Toast.makeText(Login.this, "Errorcode-107 Login BtnLogin clicked  "+e.toString(), Toast.LENGTH_SHORT).show();

                            Log.d("Exception", String.valueOf(e));
                        }
                    }
                });

                btnVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        try {
                            clientid1 = edtClientId.getText().toString();

                            Log.d("CId", clientid1);
                            if (clientid1.length() == 0) {
                                edtClientId.setError("Invalid client id");
                                flag = 1;
                            }
                            if (flag == 0) {
                                if (CheckInternetSpeed.checkInternet(Login.this).contains("0")) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
                                } else if (CheckInternetSpeed.checkInternet(Login.this).contains("1")) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
                                    dialog = ProgressDialog.show(Login.this, "", "Verifying client", false, true);
                                    url = "http://anilsahasrabuddhe.in/CRM/ClientDetails.php?clientid=" + clientid1;
                                    getClientDetails(url);
                                }

                            }

                        } catch (Exception e) {
                            Toast.makeText(Login.this, "Errorcode-108 Login BtnVerify clicked "+e.toString(), Toast.LENGTH_SHORT).show();

                            Log.d("Exception", String.valueOf(e));
                        }
                    }
                });
            }catch (Exception e)
            {
                Toast.makeText(Login.this, "Errorcode-106 Login onclick function "+e.toString(), Toast.LENGTH_SHORT).show();
            }

        }


        @TargetApi(Build.VERSION_CODES.M)
        private void generateKey () {
            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

                keyStore.load(null);
                keyGenerator.init(new
                        KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
                keyGenerator.generateKey();
            } catch (KeyStoreException | IOException | CertificateException
                    | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                    | NoSuchProviderException e) {
                e.printStackTrace();
            }
        }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }


    public void checkData() {
        try {
            if(CheckServer.isServerReachable(Login.this)) {
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(getApplicationContext());
                url = clienturl + "?clientid=" + clientid + "&caseid=1&UserName=" + username + "&UserPassword=" + pwd;
                urlRequest.setUrl(url);
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {

                        //temp=1;
                        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                        Log.d("IPP", ip);
                        // getLocalIpAddress();

                        dialog.dismiss();
                        Log.d("LoginResponse", response);
                        if (response.startsWith("Array")) {
                            String uname = response.substring(response.indexOf("[UserName] =>") + 13, response.indexOf("[UserPassword] =>"));
                            String emailID = response.substring(response.indexOf("[cEmailAdd] =>") + 14, response.indexOf("[cEmailPassword] =>"));
                            String mobile = response.substring(response.indexOf("[cMobileNO] =>") + 14, response.indexOf("[cProfilePhoto] => "));
                            String role = response.substring(response.indexOf("[UserRole] =>") + 13, response.indexOf("[Status] =>"));
                            counselorId = response.substring(response.indexOf("[cCounselorID] =>") + 17, response.indexOf(" [cCounselorName] =>"));
                            String statusid = response.substring(response.indexOf("[Status] =>") + 11, response.indexOf("[cEmailAdd] =>"));
                            strImei1 = response.substring(response.indexOf("[IMEI1] =>") + 10, response.indexOf("[IMEI2]")).trim();
                            strImei2 = response.substring(response.indexOf("[IMEI2] =>") + 10);
                            strImei2 = strImei2.substring(1, strImei2.length() - 4).trim();

                            String role1 = "";

                            if (strImei1.equals("0") || strImei2.equals("0")) {
                                Log.d("IMEI", strImei1 + " " + strImei2);
                                int condition = 0;
                                try {
                                    if (IMEINumber1.length() > 4 || IMEINumber2.length() > 4) {
                                        condition = 1;
                                    } else {
                                        condition = 0;
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(Login.this, "Errorcode-110 Login LoginResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                    //condition = 0;
                                }

                                if (condition == 1) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                                    alertDialogBuilder.setTitle("Your IMEI No is:" + IMEINumber1 + " " + IMEINumber2)

                                            .setMessage("No mobile attached with this ID.  Do you want to add this device as Primary device?")

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    insertIMEI();
                                                    edtName.setText("");
                                                    edtPassword.setText("");
                                                    dialog.dismiss();
                                                }
                                            })

                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    linearClientVerify.setVisibility(View.VISIBLE);
                                                    linearLogin.setVisibility(View.GONE);
                                                    edtName.setText("");
                                                    edtPassword.setText("");
                                                }
                                            })
                                            .show();
                                } else {
                                    Log.d("Alert", "ALert111");
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                                    alertDialogBuilder.setTitle("Phone permission is not allowed for BizcallCRM")
                                            .setMessage("Goto->Settings->App Permission->Bizcall and allow phone permission")

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    intent = new Intent(Intent.ACTION_MAIN);
                                                    intent.addCategory(Intent.CATEGORY_HOME);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })

                                            // A null listener allows the button to dismiss the dialog and  take no further action.

                                            .show();

                                }
                            } else if (strImei1.equals(IMEINumber1) || strImei2.equals(IMEINumber2)) {
                                editor.putString("Name", uname);
                                editor.putString("Id", counselorId);
                                editor.putString("EmailId", emailID);
                                editor.putString("Role", role1);
                                editor.putString("MobileNo", mobile);
                                editor.putString("StatusId", statusid);
                                editor.commit();
                                try {
                                    ip = URLEncoder.encode(ip, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                counselorId = counselorId.replace(" ", "");
                                if (CheckInternetSpeed.checkInternet(Login.this).contains("0")) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
                                } else if (CheckInternetSpeed.checkInternet(Login.this).contains("1")) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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

                                    insertLoginInfo();
                                    insertPointCollection();
                                }

                                Log.d("Logincount", String.valueOf(loginCount));

                                Log.d("Role1111", role1);
                                if(Splash.flag == 0)
                                {
                                    timer.scheduleAtFixedRate(new TimerTask() {
                                        @Override
                                        public void run() {
                                            setAlarm();
                                            Log.d("flagLogin", ":flagLogin");
                                        }
                                    }, 0, TIME_INTERVAL);
                                }
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                //getCallDetails();
                                //getSMSDetails();
                                // getUserLocation();
                                editor=sp.edit();
                                editor.putString("CheckPermission","Granted");
                                editor.commit();
                                intent = new Intent(Login.this, Home.class);
                                intent.putExtra("Activity", "Login");
                                startActivity(intent);
                                finish();
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                                alertDialogBuilder.setTitle("Device Authorization Failed")
                                        .setMessage("Your ID is registered on device IMEI ending with" + strImei1.substring(strImei1.length() - 4, strImei1.length()))

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                                dialog.dismiss();
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                linearClientVerify.setVisibility(View.VISIBLE);
                                                linearLogin.setVisibility(View.GONE);
                                                edtName.setText("");
                                                edtPassword.setText("");
                                            }
                                        })*/
                                        .show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
        } catch (Exception e) {
            Toast.makeText(Login.this, "Errorcode-109 Login Login function "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

   public void refreshWhenLoading()
   {
       final Timer t = new Timer();
       t.schedule(new TimerTask() {
           public void run() {
               if(dialog.isShowing()) {
                   Intent intent = new Intent(Login.this, Login.class);
                   startActivity(intent);// when the task active then close the dialog
                   t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
               }
           }
       }, 12000); // after 12 second (or 2000 miliseconds), the task will be active.

   }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.d("InMain", "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("InMain", ex.toString());
        }
        return null;
    }

    public void insertLoginInfo()
    {
        try {
            if(CheckServer.isServerReachable(Login.this)) {
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(getApplicationContext());
                urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=16&CounsellorId=" + counselorId + "&IpAddress1=" + ip);
                Log.d("LoginInfoResponse", clienturl + "?clientid=" + clientid + "&caseid=16&CounsellorId=" + counselorId + "&IpAddress=" + ip);
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d("LoginInfoResponse", response);
                        if (response.contains("Data inserted successfully")) {
                            Toast.makeText(Login.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                        }
                        //   Log.d("Size**", String.valueOf(arrayList.size()));
                    }
                });
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
        }catch(Exception e){
            Toast.makeText(Login.this, "Errorcode-111 Login InsertLoginInfo "+e.toString(), Toast.LENGTH_SHORT).show();

            Log.d("Exception", String.valueOf(e));
        }
    }
    public void insertPointCollection()
    {
       try {
           if(CheckServer.isServerReachable(Login.this)) {
               urlRequest = UrlRequest.getObject();
               urlRequest.setContext(getApplicationContext());
               urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=36&nCounsellorId=" + counselorId + "&nEventId=1");
               Log.d("PointCollectionResponse", clienturl + "?clientid=" + clientid + "&caseid=36&nCounsellorId=" + counselorId + "&nEventId=1");
               urlRequest.getResponse(new ServerCallback() {
                   @Override
                   public void onSuccess(String response) throws JSONException {
                       if (dialog.isShowing()) {
                           dialog.dismiss();
                       }
                       Log.d("PointCollectionResponse", response);
                       if (response.contains("Data inserted successfully")) {
                           Toast.makeText(Login.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(Login.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                       }
                       //   Log.d("Size**", String.valueOf(arrayList.size()));
                   }
               });
           }else {
               dialog.dismiss();
               android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
       }catch(Exception e){
           Toast.makeText(Login.this, "Errorcode-112 Login InsertPoint "+e.toString(), Toast.LENGTH_SHORT).show();
           Log.d("Exception", String.valueOf(e));
       }
    }
    public void insertIMEI()
    {
        try {
            if(CheckServer.isServerReachable(Login.this)) {
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(getApplicationContext());
                String urlIMEI = clienturl + "?clientid=" + clientid + "&caseid=49&CounselorID=" + counselorId + "&IMEI1=" + IMEINumber1 + "&IMEI2=" + IMEINumber2;

                String checIMEIResponse = checkIMEI(IMEINumber1);
                Log.d("checIMEIResponse", checIMEIResponse);
                if (checIMEIResponse.equals("0")) {

                    urlRequest.setUrl(urlIMEI);
                    Log.d("insertIMEIUrl", urlIMEI);
                    urlRequest.getResponse(new ServerCallback() {
                        @Override
                        public void onSuccess(String response) throws JSONException {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Log.d("insertIMEIResponse", response);
                            if (response.contains("Data inserted successfully")) {
                                linearClientVerify.setVisibility(View.VISIBLE);
                                linearLogin.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                            }
                            //   Log.d("Size**", String.valueOf(arrayList.size()));
                        }
                    });
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                    alertDialogBuilder.setTitle("Device authorization failed")
                            .setMessage("This IMEI" + IMEINumber1 + " is already assigned. Contact admin")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //insertIMEI();

                                    dialog.dismiss();
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            /* .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.dismiss();
                                     linearClientVerify.setVisibility(View.VISIBLE);
                                     linearLogin.setVisibility(View.GONE);
                                     edtName.setText("");
                                     edtPassword.setText("");
                                 }
                             })*/
                            .show();
                }
            }else
            {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
        }catch(Exception e){
            Toast.makeText(Login.this, "Errorcode-113 Login InsertIMEINo "+e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Exception", String.valueOf(e));
        }
    }
    public String checkIMEI(String strIMEI) {
        try {
            if(CheckServer.isServerReachable(Login.this)) {
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(getApplicationContext());
                String checkIMEIurl = clienturl + "?clientid=" + clientid + "&caseid=50&IMEI=" + strIMEI;
                urlRequest.setUrl(checkIMEIurl);
                Log.d("IMEIurl", checkIMEIurl);
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {

                        dialog.dismiss();
                        Log.d("IEMIResponse", response);
                        try {
                            if (!response.contains("User not Identified")) {
                                JSONObject jsonObject = new JSONObject(response);
                                // Log.d("Json",jsonObject.toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (response.contains("[]")) {
                                    Log.d("resultImeiexist", "1");
                                    resultImeiexist = 1;
                                }

                            } else {
                                resultImeiexist = 1;
                                Toast.makeText(Login.this, "Invalid Client", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Login.this, "Errorcode-115 Login CheckIMEIResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Exception", String.valueOf(e));
                        }
                    }
                });
            }else {
                dialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Login.this);
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
        } catch (Exception e) {
            Toast.makeText(Login.this, "Errorcode-114 Login check IMEI " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        if (resultImeiexist == 1) {
            return "1";
        } else {
            return "0";
        }

    }

   /* public  void loginUsingFingerPrint() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        //String clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php";
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=41&IMEI=" + IMEINumber);
        Log.d("CojnUrl", clienturl+"?clientid=" + clientid + "&caseid=41&IMEI=" +IMEINumber);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

                Log.d("IMEIResponse", response);

                    if(!response.contains("[]"))
                    {
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Login not successful", Toast.LENGTH_SHORT).show();

                    }

            }
        });
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor = sp.edit();
        editor.putString("UserName", null);
        editor.putString("Id", null);
        editor.commit();
    }





}