package com.bizcall.wayto.mentebit;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class ActivityLeadsDetailsReport extends AppCompatActivity {
    TextView mSrNo, mRefNo, mCandName, mCourse, mMobile, mAddress, mCity, mState, mPinCode, mParentNo, mEmail, mDataFrom,
            mAllocatedTo, mAllocatedDate, mStatus, mRemark, mCreatedDate;
    ImageView imgBack;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads_details_report);

        mSrNo = findViewById(R.id.txtSrNo);
        mRefNo = findViewById(R.id.txtrefNo);
        mCandName = findViewById(R.id.txtcandidatename);
        mCourse = findViewById(R.id.txtcourse);
        mMobile = findViewById(R.id.txtmobile);
        mAddress = findViewById(R.id.txtaddress);
        mCity = findViewById(R.id.txtcity);
        mState = findViewById(R.id.txtstate);
        mPinCode = findViewById(R.id.txtpincode);
        mParentNo = findViewById(R.id.txtparentno);
        mEmail = findViewById(R.id.txtemail);
        mDataFrom = findViewById(R.id.txtdatafrom);
        mAllocatedTo = findViewById(R.id.txtallocatedto);
        mAllocatedDate = findViewById(R.id.txtallocationdate);
        mStatus = findViewById(R.id.txtstatus);
        mRemark = findViewById(R.id.txtremark);
        mCreatedDate = findViewById(R.id.txtcreateddate);
        imgBack = findViewById(R.id.img_back);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mSrNo.setText(getIntent().getStringExtra("mSrNo"));
        mRefNo.setText(getIntent().getStringExtra("mRefNo"));
        mCandName.setText(getIntent().getStringExtra("mCandName"));
        mCourse.setText(getIntent().getStringExtra("mCourse"));
        mMobile.setText(getIntent().getStringExtra("mMobile"));
        mAddress.setText(getIntent().getStringExtra("mAddress"));
        mCity.setText(getIntent().getStringExtra("mCity"));
        mState.setText(getIntent().getStringExtra("mState"));
        mPinCode.setText(getIntent().getStringExtra("mPinCode"));
        mParentNo.setText(getIntent().getStringExtra("mParentNo"));
        mEmail.setText(getIntent().getStringExtra("mEmail"));
        mDataFrom.setText(getIntent().getStringExtra("mDataFrom"));
        mAllocatedTo.setText(getIntent().getStringExtra("mAllocatedTo"));
        mAllocatedDate.setText(getIntent().getStringExtra("mAllocatedDate"));
        mStatus.setText(getIntent().getStringExtra("mStatus"));
        mRemark.setText(getIntent().getStringExtra("mRemark"));
        mCreatedDate.setText(getIntent().getStringExtra("mCreatedDate"));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                onBackPressed();
                Animatoo.animateSlideRight(ActivityLeadsDetailsReport.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            ActivityLeadsDetailsReport.this.overridePendingTransition(0,
                    R.anim.play_panel_close_background);
            /*Intent intent = new Intent(ActivityLeadsDetailsReport.this, ActivityStatuswiseLeadDetailRecords.class);
            intent.putExtra("Activity", "GraphReport");
            startActivity(intent);*/
            finish();
            //Animatoo.animateSlideRight(ActivityLeadsDetailsReport.this);
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
    }
}
