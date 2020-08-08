package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterSearchCounselorData extends RecyclerView.Adapter<AdapterSearchCounselorData.ViewHolder> {
    Context context;
    ArrayList<DataCounselor> arrayList;
    ArrayList<String> arraySms;
    Intent intent;
    AlertDialog alertDialog;
    long no, no1;
    AudioManager audioManager;
    String mbl, course, sr_no, name, email,status,statusid,remarks;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int selectedposition = -1, count = 0;
    ProgressDialog dialog;
    UrlRequest urlRequest;
    String msg, mblno;
    String sms, clientid, parentno;
    TextView txtName, txtCloseWindow;
    TextView edtMessage, edtMobile;
    Spinner spinnerTemplate;
    ImageView imgCloseWindow, imgClearText;
    Button btnSms, btnWhatsapp;
    boolean isClicked = false;
    int p1 = 1;

    String allocatedDate;

    public AdapterSearchCounselorData(Context context, ArrayList<DataCounselor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdapterSearchCounselorData.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_search_counselor, viewGroup, false);
        //  audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        //   audioManager.setMode(AudioManager.MODE_IN_CALL);

        return new AdapterSearchCounselorData.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSearchCounselorData.ViewHolder viewHolder, final int i) {
        if (i % 2 == 0) {
            viewHolder.cardInfo.setBackgroundResource(R.color.off_white);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            viewHolder.cardInfo.setBackgroundResource(R.color.off_white1);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

       final DataCounselor dataCounselor = arrayList.get(i);
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        clientid = sp.getString("ClientId", null);
        parentno = dataCounselor.getParentno();


        Log.d("ParentNo", parentno);

        // viewHolder.txtSrno.setText(dataCounselor.getSr_no());
        viewHolder.txtCName.setText(dataCounselor.getCname());
        viewHolder.txtCourse.setText(dataCounselor.getCourse());
        viewHolder.txtSrno.setText(dataCounselor.getSr_no());
        //viewHolder.txtEmail.setText(dataCounselor.getEmail());
        viewHolder.txtMobile.setText(dataCounselor.getMobile());
        viewHolder.txtStatus.setText(dataCounselor.getStatus1());
        mbl = dataCounselor.getMobile();
        name = viewHolder.txtCName.getText().toString();
        course = viewHolder.txtCourse.getText().toString();
        sr_no = dataCounselor.getSr_no();
        email = dataCounselor.getEmail();
        allocatedDate = dataCounselor.getAllocationdate();

        viewHolder.txtCount1.setText((i + 1) + ".");
        viewHolder.cardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StatusAdapter",dataCounselor.getStatus1());

                editor.putString("SelectedMobile", dataCounselor.getMobile());
                editor.putString("SelectedParentNo", dataCounselor.getParentno());
                editor.putString("SelectedName", dataCounselor.getCname());
                editor.putString("SelectedCourse", dataCounselor.getCourse());
                editor.putString("SelectedSrNo", dataCounselor.getSr_no());
                editor.putString("SelectedEmail", dataCounselor.getEmail());
                editor.putString("AllocatedDate", dataCounselor.getAllocationdate());
                editor.putString("SelectedAddress", dataCounselor.getAdrs());
                editor.putString("SelectedCity", dataCounselor.getCity());
                editor.putString("SelectedState", dataCounselor.getState1());
                editor.putString("SelectedPinCode", dataCounselor.getPincode());
                editor.putString("SelectedStatus",dataCounselor.getStatus1());
                editor.putString("SelectedStatusId",dataCounselor.getStatusid());
                editor.putString("SelectedRemark",dataCounselor.getRemarks());
                editor.putString("ActivityContact","AdapterSearch");
                editor.commit();
                Intent intent = new Intent(context, CounselorContactActivity.class);
                intent.putExtra("ActivityName", "AdapterSearch");
                   /* intent.putExtra("Name",name);
                    intent.putExtra("Course",course);
                    intent.putExtra("SrNo",sr_no);
                    intent.putExtra("Email",email);*/
                context.startActivity(intent);
            }
        });



      /*  final SpannableString content = new SpannableString(mbl);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        viewHolder.txtMobile.setText(content);*/

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSrno, txtCName, txtCourse, txtCount1, txtMobile, txtStatus;
        CardView cardAction, cardInfo;
        Button btnCall, btnMsg, btnMail;

        LinearLayout linearLayout, linearCounselor;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtCount1 = itemView.findViewById(R.id.txtCount1);
            txtCName = itemView.findViewById(R.id.txtCandidateName);
            txtCourse = itemView.findViewById(R.id.txtCourse);
            txtSrno = itemView.findViewById(R.id.txtSerial);
            txtMobile = itemView.findViewById(R.id.txtMobile1);
            cardAction = itemView.findViewById(R.id.cardAction);
            cardInfo = itemView.findViewById(R.id.cardInfo);
           txtStatus=itemView.findViewById(R.id.txtStatusS);
            linearLayout = itemView.findViewById(R.id.linearCounselorInfo);
            linearCounselor = itemView.findViewById(R.id.linearCounselor);
            // txtSMS = itemView.findViewById(R.id.txtSMS);
            // imgWhatsapp = itemView.findViewById(R.id.imgWhatsapp);
        }
    }
}
