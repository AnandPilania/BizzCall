package com.bizcall.wayto.mentebit13;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.MyHolder> {
   Context context;
   ArrayList<DataCategoryName> arrayList;

    public AdapterCategory(Context context, ArrayList<DataCategoryName> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_category,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        DataCategoryName dataCategoryName=arrayList.get(i);
        myHolder.txtCategoryName.setText(dataCategoryName.getCategoryname());
        myHolder.txtCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName=itemView.findViewById(R.id.txtCategoryName);

        }
    }
}
