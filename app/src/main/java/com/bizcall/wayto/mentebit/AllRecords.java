package com.bizcall.wayto.mentebit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Spinner;

public class AllRecords extends AppCompatActivity {

    Spinner spinnerSearchAs;
    Button btnSearch,btnAllDetails;
    RecyclerView recyclerDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_records);

        recyclerDetails=findViewById(R.id.recyclerAllDetails);
        spinnerSearchAs=findViewById(R.id.spinnerSearchAs);
        btnSearch=findViewById(R.id.btn_search);
        btnAllDetails=findViewById(R.id.btn_alldetails);
    }
}
