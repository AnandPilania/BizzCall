package com.example.wayto.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GraphReport extends AppCompatActivity {


    private Button mbtn1, mbtn2, mbtn3, mbtn4, mbtn5, mbtn6, mbtn7;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_report);
        imgBack=findViewById(R.id.img_back);
        mbtn1 = findViewById(R.id.btn1);
        mbtn2 = findViewById(R.id.btn2);
        mbtn3 = findViewById(R.id.btn3);
        mbtn4 = findViewById(R.id.btn4);
        mbtn5 = findViewById(R.id.btn5);
        mbtn6 = findViewById(R.id.btn6);
        mbtn7 = findViewById(R.id.btn7);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphReport.this,GraphDetails.class);
                intent.putExtra("url","http://www.mentebit.com/bizcallreport/index.php?clientid=AnDe828500&reportid=6");
                startActivity(intent);
            }
        });

        mbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphReport.this,GraphDetails.class);
                intent.putExtra("url","http://www.mentebit.com/bizcallreport/index.php?clientid=AnDe828500&reportid=2");
                startActivity(intent);
            }
        });

        mbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphReport.this,GraphDetails.class);
                intent.putExtra("url","http://www.mentebit.com/bizcallreport/index.php?clientid=AnDe828500&reportid=3");
                startActivity(intent);
            }
        });

        mbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphReport.this,GraphDetails.class);
                intent.putExtra("url","http://www.mentebit.com/bizcallreport/index.php?clientid=AnDe828500&reportid=4");
                startActivity(intent);
            }
        });

        mbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphReport.this,GraphDetails.class);
                intent.putExtra("url","http://www.mentebit.com/bizcallreport/index.php?clientid=AnDe828500&reportid=5");
                startActivity(intent);
            }
        });

        mbtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphReport.this,GraphDetails.class);
                intent.putExtra("url","http://www.mentebit.com/bizcallreport/index.php?clientid=AnDe828500&reportid=1");
                startActivity(intent);
            }
        });

        mbtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphReport.this,GraphDetails.class);
                intent.putExtra("url","http://www.mentebit.com/bizcallreport/index.php?clientid=AnDe828500&reportid=7&userid=8");
                startActivity(intent);
            }
        });
    }


}
