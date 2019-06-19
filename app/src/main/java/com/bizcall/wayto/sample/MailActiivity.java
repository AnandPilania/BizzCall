package com.bizcall.wayto.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MailActiivity extends AppCompatActivity {


    EditText edtEmailAddress, edtEmailSubject, edtEmailText;
    TextView textImagePath;
    ImageView imgBack;
    Button btnSelectImage, btnSendEmail_intent;
    final int RQS_LOADIMAGE = 0;
    Uri imageUri = null;
    ArrayList<Uri>  arrayList;
    Vibrator vibrator;
    ListView listViewImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        imgBack=findViewById(R.id.img_back);
        edtEmailAddress = (EditText) findViewById(R.id.email_address);
        edtEmailSubject = (EditText) findViewById(R.id.email_subject);
        edtEmailText = (EditText) findViewById(R.id.email_text);
        textImagePath = (TextView) findViewById(R.id.imagepath);
        btnSelectImage = (Button) findViewById(R.id.selectimage);
        btnSendEmail_intent = (Button) findViewById(R.id.sendemail_intent);
        arrayList=new ArrayList<>();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);

            }
        });

        btnSelectImage.setOnClickListener(buttonSelectImageOnClickListener);
        btnSendEmail_intent.setOnClickListener(buttonSendEmail_intentOnClickListener);
    }


    View.OnClickListener buttonSelectImageOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RQS_LOADIMAGE);
        }
    };

    View.OnClickListener buttonSendEmail_intentOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (edtEmailAddress.getText().toString().isEmpty() || edtEmailSubject.getText().toString().isEmpty() || edtEmailText.getText().toString().isEmpty()) {
               edtEmailSubject.setError("Enter subject");
                edtEmailText.setError("Enter text");
                edtEmailAddress.setError("Enter address");

                Toast.makeText(MailActiivity.this, "Please enter valid info", Toast.LENGTH_SHORT).show();
            } else {

                String emailAddress = edtEmailAddress.getText().toString();
                String emailSubject = edtEmailSubject.getText().toString();
                String emailText = edtEmailText.getText().toString();
                String emailAddressList[] = {emailAddress};


                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.putExtra(Intent.EXTRA_EMAIL, emailAddressList);
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, emailText);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");

              //  startActivity(Intent.createChooser(intent, "Send mail"));


                if (arrayList.isEmpty()) {
                    intent.setAction(Intent.ACTION_SEND);

                    intent.setType("plain/text");
                }else if(arrayList.size()==1){
                    Log.d("imageuri", imageUri + " ");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, arrayList.get(0));
                intent.setType("image/png");
                } else {
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_STREAM, arrayList);
                intent.setType("image/png");
                   // intent.setType("plain/text");
                }

                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQS_LOADIMAGE:
                    imageUri = data.getData();
                    arrayList.add(imageUri);
                    listViewImages=findViewById(R.id.listImages);
                    listViewImages.setAdapter(new ArrayAdapter<Uri>(MailActiivity.this,android.R.layout.simple_list_item_1,arrayList));
                    /*for(int i=0;i<arrayList.size();i++) {
                        String path11= String.valueOf(arrayList.get(i));
                        textImagePath.setText("\n"+path11);

                    }*/

                    break;


            }

        }
    }
}