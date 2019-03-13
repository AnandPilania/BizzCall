package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;

public class Login extends AppCompatActivity {

    public static TextView edtName, edtPassword;
    RequestQueue requestQueue;
    Button btnLogin;
    Intent intent;
    ProgressDialog dialog;
    String username, pwd, url;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    UrlRequest urlRequest;
    int flag = 0,temp=0;
    String counselorId;
    String clientid;
    ImageView imgError1, imgError2;
    String clienturl,ip,login;
    long timeout;
    int loginCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(Login.this);
        edtName = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        clientid = "AnDe828500";
        timeout=50000L;
        clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php";
        loginCount=(sp.getInt("LoginCount",0));


        editor = sp.edit();
        editor.putString("ClientId", clientid);
        editor.putLong("TimeOut", timeout);
        editor.putString("ClientUrl",clienturl);
        editor.commit();

       /* edtName.getText().clear();
        edtPassword.getText().clear();
        edtName.clearFocus();
        edtPassword.clearFocus();*/
        //imgError1=findViewById(R.id.imgError1);
        //imgError2=findViewById(R.id.imgError2);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
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
                    dialog = ProgressDialog.show(Login.this, "Loading", "Please wait.....", false, true);
                    new CountDownTimer(timeout, 1000) {

                        public void onTick(long millisUntilFinished) {
                            //mtextView.setText("seconds remaining: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            if(dialog.isShowing()) {
                                // mtextView.setText("done!");
                                dialog.dismiss();
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }.start();

                    checkData();

                }
                flag = 0;


            }
        });
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
               Log.d("IPP",ip);
               // getLocalIpAddress();

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("LoginResponse", response);
                if (response.startsWith("Array"))
                {
                    String uname = response.substring(response.indexOf("[UserName] =>") + 13, response.indexOf("[UserPassword] =>"));
                    String emailID = response.substring(response.indexOf("[cEmailAdd] =>") + 14, response.indexOf("[cEmailPassword] =>"));
                    String mobile = response.substring(response.indexOf("[cMobileNO] =>") + 14, response.indexOf("[cProfilePhoto] => "));
                    String role = response.substring(response.indexOf("[UserRole] =>") + 13, response.indexOf("[Status] =>"));
                    counselorId = response.substring(response.indexOf("[cCounselorID] =>") + 17, response.indexOf(" [cCounselorName] =>"));
                    String statusid = response.substring(response.indexOf("[Status] =>") + 11, response.indexOf("[cEmailAdd] =>"));
                    Log.d("Res***", counselorId);
                    String role1 = "";

                    editor.putString("Name", uname);
                    editor.putString("Id", counselorId);
                    editor.putString("EmailId", emailID);
                    editor.putString("Role", role1);
                    editor.putString("MobileNo", mobile);
                    editor.putString("StatusId", statusid);
                    editor.commit();
                    try {
                        ip=URLEncoder.encode(ip, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    counselorId=counselorId.replace(" ","");
                    insertLoginInfo();
                    insertPointCollection();

                    Log.d("Logincount", String.valueOf(loginCount));

                    Log.d("Role1111", role1);
                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();

                    intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=16&CounsellorId=" + counselorId + "&IpAddress1=" + ip);
        Log.d("LoginInfoResponse", clienturl+"?clientid=" + clientid + "&caseid=16&CounsellorId=" + counselorId + "&IpAddress=" + ip);
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
    }
    public void insertPointCollection()
    {
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
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor = sp.edit();
        editor.putString("UserName", null);
        editor.putString("Id", null);
        editor.commit();
    }
}