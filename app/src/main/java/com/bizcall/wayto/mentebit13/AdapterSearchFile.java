package com.bizcall.wayto.mentebit13;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterSearchFile extends RecyclerView.Adapter<AdapterSearchFile.UserHolder> {
    private ArrayList<DataMasterEntry> arrayListMasterEntry;
    private Context mContext;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public AdapterSearchFile(ArrayList<DataMasterEntry> arrayListMasterEntry, Context mContext) {
        this.arrayListMasterEntry = arrayListMasterEntry;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterSearchFile.UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_search_file, null);
        return  new AdapterSearchFile.UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSearchFile.UserHolder userHolder, int i) {
        DataMasterEntry dataMasterEntry = arrayListMasterEntry.get(i);
        userHolder.txtFileno.setText(dataMasterEntry.getmFileno());
        userHolder.txtSrno.setText(dataMasterEntry.getmSrno());
        userHolder.txtFirstname.setText(dataMasterEntry.getmFirstname());
        userHolder.txtLastname.setText(dataMasterEntry.getmLastname());

        sp=mContext.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        editor=sp.edit();

        userHolder.txtFileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("FileNo", userHolder.txtFileno.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext,"File ID copied",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListMasterEntry.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView txtFileno, txtSrno, txtFirstname, txtLastname;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            txtFileno = itemView.findViewById(R.id.txtUser_fileno);
            txtSrno = itemView.findViewById(R.id.txtUser_srno);
            txtFirstname = itemView.findViewById(R.id.txtUser_firstname);
            txtLastname = itemView.findViewById(R.id.txtUser_lastname);

        }
    }
}
