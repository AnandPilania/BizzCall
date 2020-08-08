package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCounselorData extends RecyclerView.Adapter<AdapterCounselorData.ViewHolder> {
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
    boolean isClicked = false;
    int p1 = 1;

    String allocatedDate;

    public AdapterCounselorData(Context context, ArrayList<DataCounselor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter_counselor_data, viewGroup, false);
        //  audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        //   audioManager.setMode(AudioManager.MODE_IN_CALL);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i)
    {
        final DataCounselor dataCounselor = arrayList.get(i);
        if (i % 2 == 0) {
            viewHolder.cardInfo.setBackgroundResource(R.color.off_white);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            viewHolder.cardInfo.setBackgroundResource(R.color.off_white1);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid = sp.getString("ClientId", null);
        parentno = dataCounselor.getParentno();
        editor = sp.edit();
       // editor.putString("ActivityContact","CounselorData");
        editor.commit();
     //   Log.d("ParentNo", parentno);

        // viewHolder.txtSrno.setText(dataCounselor.getSr_no());
        viewHolder.txtCName.setText(dataCounselor.getCname());
        viewHolder.txtCourse.setText(dataCounselor.getCourse());
        viewHolder.txtSrno.setText(dataCounselor.getSr_no());
        viewHolder.txtDate.setText(dataCounselor.getAllocationdate());
        viewHolder.txtMobile.setText(dataCounselor.getMobile());
        /*mbl = dataCounselor.getMobile();
        name = viewHolder.txtCName.getText().toString();
        course = viewHolder.txtCourse.getText().toString();
        sr_no = dataCounselor.getSr_no();
        email = dataCounselor.getEmail();
        allocatedDate = dataCounselor.getAllocationdate();*/

        viewHolder.txtCount1.setText((i + 1) + ".");
       // viewHolder.txtCount1.setText(dataCounselor.getStrSno());
        viewHolder.cardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StatusAdapter",dataCounselor.getStatus1());
                /* editor.putString("SelectedMobile", dataCounselor.getMobile());
                editor.putString("SelectedParentNo", dataCounselor.getParentno());
                editor.putString("SelectedName", dataCounselor.getCname());
                editor.putString("SelectedCourse", dataCounselor.getCourse());*/
                editor.putString("SelectedSrNo", dataCounselor.getSr_no());
                editor.putString("ActivityContact","CounselorData");
             /* editor.putString("SelectedEmail", dataCounselor.getEmail());
                editor.putString("AllocatedDate", dataCounselor.getAllocationdate());
                editor.putString("SelectedAddress", dataCounselor.getAdrs());
                editor.putString("SelectedCity", dataCounselor.getCity());
                editor.putString("SelectedState", dataCounselor.getState1());
                editor.putString("SelectedPinCode", dataCounselor.getPincode());
                editor.putString("SelectedStatus",dataCounselor.getStatus1());
                editor.putString("SelectedStatusId",dataCounselor.getStatusid());
                editor.putString("SelectedRemark",dataCounselor.getRemarks());*/
                editor.commit();
                Log.d("SrNo&&&",dataCounselor.getSr_no());
                Intent intent = new Intent(context, CounselorContactActivity.class);
               intent.putExtra("ActivityName", "CounselorData");
                context.startActivity(intent);
            }
        });



      /*  final SpannableString content = new SpannableString(mbl);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        viewHolder.txtMobile.setText(content);*/
        // viewHolder.cardAction.setVisibility(View.GONE);
        // viewHolder.txtDataFrom.setText(dataCounselor.getDatafrom());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSrno, txtCName, txtCourse, txtCount1, txtMobile, txtDate;
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
            txtDate=itemView.findViewById(R.id.txtDate);
            cardAction = itemView.findViewById(R.id.cardAction);
            cardInfo = itemView.findViewById(R.id.cardInfo);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnMsg = itemView.findViewById(R.id.btnMessage);
            btnMail = itemView.findViewById(R.id.btnMail);
            linearLayout = itemView.findViewById(R.id.linearCounselorInfo);
            linearCounselor = itemView.findViewById(R.id.linearCounselor);
            // txtSMS = itemView.findViewById(R.id.txtSMS);
            // imgWhatsapp = itemView.findViewById(R.id.imgWhatsapp);
        }
    }
}
