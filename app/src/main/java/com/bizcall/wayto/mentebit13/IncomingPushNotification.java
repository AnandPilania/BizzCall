package com.bizcall.wayto.mentebit13;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IncomingPushNotification extends AppCompatActivity {

   // public static ArrayList<DataMessage> arrayListMessage;
    RecyclerView recyclerMessage;
    MessageAdapter messageAdapter;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_incoming_push_notification);
       recyclerMessage=findViewById(R.id.recyclerMessage);
       imgBack=findViewById(R.id.img_back);
        // REST OF YOUR CODE
        //arrayListMessage=new ArrayList<>();
        messageAdapter=new MessageAdapter(IncomingPushNotification.this,MyFireBaseMessagingService.arrayListMessage);
        LinearLayoutManager layoutManager=new LinearLayoutManager(IncomingPushNotification.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerMessage.setLayoutManager(layoutManager);
        recyclerMessage.setAdapter(messageAdapter);
        messageAdapter.notifyDataSetChanged();
        IntentFilter ifilter= new IntentFilter("com.myApp.CUSTOM_EVENT");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, ifilter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(IncomingPushNotification.this,Home.class);
                intent.putExtra("Activity","Notification");
                startActivity(intent);
            }
        });
    }
    private BroadcastReceiver onNotice= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update your RecyclerView here using notifyItemInserted(position);
            messageAdapter=new MessageAdapter(IncomingPushNotification.this,MyFireBaseMessagingService.arrayListMessage);
            LinearLayoutManager layoutManager=new LinearLayoutManager(IncomingPushNotification.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerMessage.setLayoutManager(layoutManager);
            recyclerMessage.setAdapter(messageAdapter);
            messageAdapter.notifyDataSetChanged();
        }};
}
