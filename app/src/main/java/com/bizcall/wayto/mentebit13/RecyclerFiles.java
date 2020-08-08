package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerFiles extends RecyclerView.Adapter<RecyclerFiles.MyHolder> {
    public static ArrayList<DataFileList> arrayList;
    Context context;
    String isUploaded = "0";
    int pos;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String filename, f1, fc1;
    ArrayList<DataUploadedList> arr1;
    MediaPlayer mp;

    public RecyclerFiles(Context context, ArrayList<DataFileList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

   /* public RecyclerFiles(ArrayList<DataUploadedList> arr1) {

        this.arr1= arr1;
    }*/

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_filelist, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
        sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        myHolder.chkboxSelectedFile.setChecked(false);
        //editor=sp.edit();
        //final DataFileList currentItem = arrayList.get(i);
        DataFileList dataFileList = arrayList.get(i);
        filename = dataFileList.getFilename();
        Log.d("Name", "" + filename);
        // isUploaded=sp.getString("ServerResponse",null);
        // pos=sp.getInt("Position",0);
        arr1 = new ArrayList<>();

        Log.d("FileName#", filename);
        myHolder.chkboxSelectedFile.setChecked(arrayList.get(i).getSelected());
        myHolder.txtFileName.setText(filename);
        mp = new MediaPlayer();
        try {
            mp.setDataSource(arrayList.get(i).getFilename());//Write your location here
            mp.prepare();

        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }

        myHolder.txtFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (mp.isPlaying()) {
                        mp.pause();
                    } else {
                        mp.start();
                    }
                   /* Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(filename));
                    intent.addCategory(Intent.CATEGORY_APP_MUSIC);
                    context.startActivity(intent);*/
                } catch (Exception e) {
                    Log.d("Exception", String.valueOf(e));
                }
            }
        });
        myHolder.chkboxSelectedFile.setTag(i);
        myHolder.chkboxSelectedFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) myHolder.chkboxSelectedFile.getTag();
                Toast.makeText(context, arrayList.get(pos).getFilename() + " clicked!", Toast.LENGTH_SHORT).show();

                if (arrayList.get(i).getSelected()) {
                    arrayList.get(pos).setSelected(false);
                } else {
                    arrayList.get(pos).setSelected(true);
                }
            }
        });
    }
   /* public boolean appIsPurchased(){
        return sp.getBoolean("Uploaded",false);
    }*/

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CheckBox chkboxSelectedFile;
        TextView txtFileName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            chkboxSelectedFile = itemView.findViewById(R.id.chkboxSelectFiles);
            txtFileName = itemView.findViewById(R.id.txtFilename);
        }
    }
}
