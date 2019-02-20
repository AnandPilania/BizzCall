package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

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
    int flag = 0;
    String clientid;
    ImageView imgError1, imgError2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(Login.this);
        edtName = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        clientid = "AnDe828500";
        editor = sp.edit();
        editor.putString("ClientId", clientid);
        editor.commit();

       /* edtName.getText().clear();
        edtPassword.getText().clear();
        edtName.clearFocus();
        edtPassword.clearFocus();*/
        //imgError1=findViewById(R.id.imgError1);
        //imgError2=findViewById(R.id.imgError2);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtName.getText().toString();
                Log.d("Name111", username);
                pwd = edtPassword.getText().toString();
                url = "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=1&UserName=" + username + "&UserPassword=" + pwd;
                // url="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?caseid=4";
                Log.d("Url111", "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=15&caseid=1&UserName=" + username + "&UserPassword=" + pwd);
                if (username.length() == 0) {
                    edtName.setError("Please enter name");
                    flag = 1;
                }
                if (pwd.length() == 0) {
                    edtPassword.setError("Please enter password");
                    flag = 1;
                }
                if (flag == 0) {
                    checkData();
                }
                flag = 0;

            }
        });
    }

    public void checkData() {
        dialog = ProgressDialog.show(Login.this, "Loading", "Please wait.....", false, true);
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl(url);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

                //  dialog.dismiss();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("LoginResponse", response);
                if (response.startsWith("Array")) {
                    String uname = response.substring(response.indexOf("[UserName] =>") + 13, response.indexOf("[UserPassword] =>"));
                    String emailID = response.substring(response.indexOf("[cEmailAdd] =>") + 14, response.indexOf("[cEmailPassword] =>"));
                    String mobile = response.substring(response.indexOf("[cMobileNO] =>") + 14, response.indexOf("[cProfilePhoto] => "));
                    String role = response.substring(response.indexOf("[UserRole] =>") + 13, response.indexOf("[Status] =>"));
                    String counselorId = response.substring(response.indexOf("[cCounselorID] =>") + 17, response.indexOf(" [cCounselorName] =>"));
                    String statusid = response.substring(response.indexOf("[Status] =>") + 11, response.indexOf("[cEmailAdd] =>"));
                    Log.d("Res***", counselorId);
                    String role1 = "";
                    editor = sp.edit();
                    editor.putString("Name", uname);
                    editor.putString("Id", counselorId);
                    editor.putString("EmailId", emailID);
                    editor.putString("Role", role1);
                    editor.putString("MobileNo", mobile);
                    editor.putString("StatusId", statusid);
                    editor.commit();

                    if (role.contains("1")) {
                        role1 = "User";
                    } else if (role.contains("2")) {
                        role1 = "Admin";
                    } else if (role.contains("3")) {
                        role1 = "Accountant";
                    } else if (role.contains("4")) {
                        role1 = "College";
                    }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor = sp.edit();
        editor.putString("UserName", null);
        editor.putString("Id", null);
        editor.commit();
    }
}