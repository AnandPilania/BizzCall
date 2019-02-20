package com.example.wayto.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

public class AdapterCounselorData extends RecyclerView.Adapter<AdapterCounselorData.ViewHolder> {
    Context context;
    ArrayList<DataCounselor> arrayList;
    ArrayList<String> arraySms;
    Intent intent;
    AlertDialog alertDialog;
    long no, no1;
    AudioManager audioManager;
    String mbl, course, sr_no, name, email;
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        if (i % 2 == 0) {
            viewHolder.cardInfo.setBackgroundResource(R.color.white);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            viewHolder.cardInfo.setBackgroundResource(R.color.old_lace);
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
        //viewHolder.txtEmail.setText(dataCounselor.getEmail());
        viewHolder.txtMobile.setText(dataCounselor.getMobile());
        mbl = dataCounselor.getMobile();
        name = viewHolder.txtCName.getText().toString();
        course = viewHolder.txtCourse.getText().toString();
        sr_no = dataCounselor.getSr_no();
        email = dataCounselor.getEmail();
        allocatedDate = dataCounselor.getAllocationdate();


        viewHolder.txtCount1.setText((i + 1) + ".");
        viewHolder.linearCounselor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                editor.commit();
                Intent intent = new Intent(context, CounselorContactActivity.class);
                intent.putExtra("ActivityName", "AdapterCounsellor");
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
        // viewHolder.cardAction.setVisibility(View.GONE);
        // viewHolder.txtDataFrom.setText(dataCounselor.getDatafrom());
      /*  int id=viewHolder.cardInfo.getId();
        Log.d("CardId", String.valueOf(id));*/

      /*  viewHolder.cardInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    selectedposition=i;
                    count=count+1;

                notifyDataSetChanged();
            }
            });

        if(i==selectedposition)
        {
            if(count%2==0)
            {
                viewHolder.cardAction.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.cardAction.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            viewHolder.cardAction.setVisibility(View.GONE);
        }

        viewHolder.btnCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                DataCounselor dataCounselor1 = arrayList.get(pos);
                editor.putString("Sr.No", dataCounselor1.getSr_no());
                editor.putString("CName", dataCounselor1.getCname());
                editor.putString("MobileNo", dataCounselor1.getMobile());
                editor.commit();
                // Toast.makeText(context,"SrNo:"+dataCounselor1.getSr_no(),Toast.LENGTH_SHORT).show();
                LayoutInflater li = LayoutInflater.from(context);
                //Creating a view to get the dialog box
                View confirmCall = li.inflate(R.layout.layout_confirm_call, null);

                //confirmCall.setClipToOutline(true);

                TextView txtYes = (TextView) confirmCall.findViewById(R.id.txtYes);
                TextView txtNo = (TextView) confirmCall.findViewById(R.id.txtNo);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                //Adding our dialog box to the view of alert dialog
                alert.setView(confirmCall);
                //Creating an alert dialog
                alertDialog = alert.create();
                alertDialog.show();
                str = "9096844152";
                str1 = "9970575362";
                no = Long.parseLong(str);
                txtYes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+mbl+""));

                        //intent.setAction("android.intent.action.CALL");
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            context.startActivity(intent);
                            // audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
                            alertDialog.dismiss();
                        }
                    }
                });

                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        viewHolder.btnMsg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater li = LayoutInflater.from(context);
            //Creating a view to get the dialog box
             View template = li.inflate(R.layout.layout_msg_template, null);
             txtCloseWindow=template.findViewById(R.id.txtCloseWindow);
             imgCloseWindow=template.findViewById(R.id.imgCloseWindow);
             edtMobile=template.findViewById(R.id.edtMobileNo);
             txtName=template.findViewById(R.id.txtUserName1);
             edtMessage=template.findViewById(R.id.edtMessage);
             spinnerTemplate=template.findViewById(R.id.spinnerMsgTemplate);
             btnSms=template.findViewById(R.id.btnSMS);
             imgClearText=template.findViewById(R.id.imgClearMsg);
             btnWhatsapp=template.findViewById(R.id.btnWhatsapp);

                int pos = viewHolder.getAdapterPosition();
                DataCounselor dataCounselo = arrayList.get(pos);
                String mobile,cname;
                mobile=dataCounselo.getMobile();
                Log.d("Mbl",mobile);
                cname=dataCounselo.getCname();
                Log.d("cname",cname);
             String cid=sp.getString("Id",null);
             Log.d("CID",cid);
             cid=cid.replace(" ","");

             edtMobile.setText("+91 "+mobile);
             txtName.setText(cname);

             dialog = ProgressDialog.show(context, "Loading", "Please wait.....", false, true);
            arraySms=new ArrayList<>();
                urlRequest = UrlRequest.getObject();
                urlRequest.setContext(context);
                urlRequest.setUrl("http://anilsahasrabuddhe.in/CRM/AnDe828500/cases.php?clientid="+clientid+"caseid=7&nCounselorID="+cid+"&IsActive=1");
               Log.d("TemplatUrl","http://anilsahasrabuddhe.in/CRM/AnDe828500/MessageTemplate.php?nCounselorID="+cid+"&IsActive=1");
                urlRequest.getResponse(new ServerCallback() {
                    @Override
                    public void onSuccess(String response) throws JSONException {

                        if (dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Log.d("TemplateResponse", response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Log.d("Json",jsonObject.toString());
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                               sms =jsonObject1.getString("cSmsText");
                                arraySms.add(sms);
                            }
                            arraySms.add(0,"Select Message From Template");
                            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context,R.layout.spinner_item1,arraySms);

                            // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTemplate.setAdapter(arrayAdapter);
                            Log.d("Size**", String.valueOf(arraySms.size()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // String msg=response.substring(response.indexOf( "( [cSmsText] =>"+15),response.indexOf(")"));


                    }
                });
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
            //Adding our dialog box to the view of alert dialog
            alert.setView(template);
            //Creating an alert dialog
            alertDialog = alert.create();
            alertDialog.show();
                imgCloseWindow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();

                    }
                });
                imgClearText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtMessage.getText().clear();
                    }
                });

            spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                   if(position!=0) {

                       String text=spinnerTemplate.getSelectedItem().toString();
                           edtMessage.setText(text);

                   }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                        isClicked=false;
                }
            });



            btnWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg = edtMessage.getText().toString();
                    int pos = viewHolder.getAdapterPosition();
                  //  DataCounselor dataCounselor1 = arrayList.get(pos);
                    mblno = edtMobile.getText().toString();
                    if (msg.length() != 0) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + mblno + "&text=" + msg));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        edtMessage.setError("Please enter message");
                    }
                }

            });
            btnSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg=edtMessage.getText().toString();
                    int pos = viewHolder.getAdapterPosition();
                    DataCounselor dataCounselor1 = arrayList.get(pos);
                    mblno=edtMobile.getText().toString();
                    if (msg.length() != 0) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mblno));
                        intent.putExtra("sms_body", msg);
                        context.startActivity(intent);
                    }
                    else
                    {
                        edtMessage.setError("Please enter message");
                    }
                }
            });

            //sendWhatsappMsg();
            }
        });
        viewHolder.btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=dataCounselor.getEmail();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mail});
              //  i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
               // i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                   // context.startActivity(i);
                    context.startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void sendWhatsappMsg() {

        try {
            String text = "This is a test";// Replace with your message.

            String toNumber = "7350937455"; // Replace with mobile phone number without +Sign or leading zeros.


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+917387384877&text=I%27m%20interested%20in%20MBBS%20Admission%20with%20lowest%20fees"));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // try {

           /* Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSrno, txtCName, txtCourse, txtCount1, txtMobile, txtSMS;
        CardView cardAction, cardInfo;
        Button btnCall, btnMsg, btnMail;
        LinearLayout linearLayout, linearCounselor;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtCount1 = itemView.findViewById(R.id.txtCount1);
            txtCName = itemView.findViewById(R.id.txtCandidateName);
            txtCourse = itemView.findViewById(R.id.txtCourse);
            // txtEmail = itemView.findViewById(R.id.txtEmail);
            txtMobile = itemView.findViewById(R.id.txtMobile1);
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
