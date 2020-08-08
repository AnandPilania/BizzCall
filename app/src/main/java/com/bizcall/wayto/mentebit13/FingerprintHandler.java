package com.bizcall.wayto.mentebit13;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    UrlRequest urlRequest;
    String clienturl, clientid, IMEINumber;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

/*
public FingerprintHandler(Context context) {
        this.context = context;
        clientid=Login.clientid;
        clienturl=Login.clienturl;
        IMEINumber=Login.IMEINumber1;
        sp=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        editor=sp.edit();
        }
*/

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an Authontication Error : " + errString, false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error : " + helpString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Authontication Fail", false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Now you can access the Application.", true);
        loginUsingFingerPrint();
    }

    private void update(String s, boolean b) {
        TextView txtResponce = ((Activity) context).findViewById(R.id.txtResponse);
        txtResponce.setText(s);

        if (b == false) {
            txtResponce.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else
            txtResponce.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public void loginUsingFingerPrint() {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(context);
        //String clienturl="http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php";
        urlRequest.setUrl(clienturl + "?clientid=" + clientid + "&caseid=41&IMEI=" + IMEINumber);
        Log.d("IMEIUrl", clienturl + "?clientid=" + clientid + "&caseid=41&IMEI=" + IMEINumber);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

                Log.d("IMEIResponse", response);

                if (!response.contains("[]")) {
                    JSONObject jsonObject = new JSONObject(response);
                    // Log.d("Json",jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String counselorId = jsonObject1.getString("cCounselorID");
                        String uname = jsonObject1.getString("UserName");
                        String emailID = jsonObject1.getString("cEmailAdd");
                        String role1 = jsonObject1.getString("UserRole");
                        String mobile = jsonObject1.getString("cMobileNO");
                        String statusid = jsonObject1.getString("Status");
                        editor.putString("Name", uname);
                        editor.putString("Id", counselorId);
                        editor.putString("EmailId", emailID);
                        editor.putString("Role", role1);
                        editor.putString("MobileNo", mobile);
                        editor.putString("StatusId", statusid);
                        editor.commit();
                        Intent intent = new Intent(context, Home.class);
                        intent.putExtra("Activity", "Login");
                        context.startActivity(intent);
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "This phone is registered only for Couunselor id 8 ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
