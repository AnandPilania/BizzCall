package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class MessageTemplate extends AppCompatActivity {

    TextView edtMeggase;
    Button btnSubmit;
    SharedPreferences sp;
    String counselorid, msg, clientid;
    UrlRequest urlRequest;
    ProgressDialog dialog;
    int flag = 0;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_template);
        edtMeggase = findViewById(R.id.edtMessage1);
        btnSubmit = findViewById(R.id.btnSubmitMsg);
        imageBack = findViewById(R.id.img_back);
        sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        counselorid = sp.getString("Id", null);
        clientid = sp.getString("ClientId", null);
       /* SpannableString content = new SpannableString(mbl);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        viewHolder.txtMobile.setText(content);*/
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                msg = edtMeggase.getText().toString();
                if (msg.length() == 0) {
                    edtMeggase.setError("Please enter message");
                    flag = 1;
                }
                if (flag == 0) {
                    insertMsgTemplate();
                }
            }
        });
    }

    public void insertMsgTemplate() {
        dialog = ProgressDialog.show(MessageTemplate.this, "Loading", "Please wait.....", false, true);
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getApplicationContext());
        urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=8&cSmsText=" + msg + "&IsActive=1&nCounselorID=" + counselorid);
        Log.d("Url11", "http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid=" + clientid + "&caseid=8&cSmsText=" + msg + "&IsActive=1&nCounselorID=" + counselorid);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("TemplateMsgResponse", response);
                if (response.contains("Data inserted successfully")) {
                    Toast.makeText(MessageTemplate.this, "Template saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MessageTemplate.this, "Template not saved", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent=new Intent(MessageTemplate.this,Home.class);
        startActivity(intent);*/
    }
}
