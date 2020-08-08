package com.bizcall.wayto.mentebit13;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PushNotificationActivity extends AppCompatActivity
{
    EditText edtTitle,edtBody,edtTopic;
    Button btnSend;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAASJLgxiE:APA91bEK9rhnZ6XlNQgtrVcCk53wvGAk56nkToqPehg_PCMpSAI8OrTNXioN8zsFbt1q0fD-vHA6keY2czIypd0UfbWYo7wencywWMJtPZjU237aB8dsRh9z1vkMrkHSq165pXNVQK58";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    int temp=0;
    ImageView imgBack,imgRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        initialize();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PushNotificationActivity.this,PushNotificationActivity.class);
                startActivity(intent);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic=edtTopic.getText().toString();
                TOPIC = "/topics/"+topic; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = edtTitle.getText().toString();
                NOTIFICATION_MESSAGE = edtBody.getText().toString();
                temp=0;
                if(topic.length()==0)
                {
                    edtTopic.setError("Invalid topic");
                    temp=1;
                }
                if(NOTIFICATION_TITLE.length()==0)
                {
                    edtTitle.setError("Invalid title");
                    temp=1;
                }
                if(NOTIFICATION_MESSAGE.length()==0)
                {
                    edtBody.setError("Invalid Message");
                    temp=1;
                }

                if(temp==0) {
                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                        notification.put("to", TOPIC);
                        notification.put("data", notifcationBody);
                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage());
                    }
                    sendNotification(notification);
                }
            }
        });
    }

    private void initialize()
    {
        edtBody=findViewById(R.id.edtBody);
        edtTitle=findViewById(R.id.edtTitle);
        btnSend=findViewById(R.id.btnSend);
        edtTopic=findViewById(R.id.edtTopic);
        imgBack=findViewById(R.id.img_back);
        imgRefresh=findViewById(R.id.imgRefresh);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        Toast.makeText(PushNotificationActivity.this,"Message sent",Toast.LENGTH_SHORT).show();
                        edtTopic.setText("");
                        edtTitle.setText("");
                        edtBody.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PushNotificationActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
