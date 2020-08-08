package com.bizcall.wayto.mentebit13;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterMailImage extends RecyclerView.Adapter<AdapterMailImage.MyHolder> {
   Context context;
   ArrayList<DataMailImage> arrayListMail;

    public AdapterMailImage(Context context, ArrayList<DataMailImage> dataMailImages) {
        this.context = context;
        this.arrayListMail = dataMailImages;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_mail_image,viewGroup,false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        DataMailImage dataMailImage=arrayListMail.get(i);
        myHolder.txtMailImageName.setText(dataMailImage.getImageName());
    }

    @Override
    public int getItemCount() {
        return arrayListMail.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtMailImageName;
        ImageView imgMailImage;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtMailImageName=itemView.findViewById(R.id.txtMailImageName);
            imgMailImage=itemView.findViewById(R.id.imgMailImage);
        }
    }
}
