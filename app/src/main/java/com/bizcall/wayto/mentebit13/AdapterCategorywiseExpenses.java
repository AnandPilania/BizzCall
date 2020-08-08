package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCategorywiseExpenses extends RecyclerView.Adapter<AdapterCategorywiseExpenses.MyHolder> {
    Context context;
    ArrayList<DataCategorywiseExpenses> arrayListCategorywise;
    DataCategorywiseExpenses dataCategorywiseExpenses;

    public AdapterCategorywiseExpenses(Context context, ArrayList<DataCategorywiseExpenses> arrayListCategorywise) {
        this.context = context;
        this.arrayListCategorywise = arrayListCategorywise;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_categorywise_expenses,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        dataCategorywiseExpenses=arrayListCategorywise.get(i);
        myHolder.txtCategoryName.setText(dataCategorywiseExpenses.getCategoryname());
        myHolder.txtAmount.setText(dataCategorywiseExpenses.getAmount());
        myHolder.linearCategoryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BalanceDetails.class);
                intent.putExtra("AmountType",dataCategorywiseExpenses.getAmounttype());
                intent.putExtra("CategoryType", dataCategorywiseExpenses.getCategorytype());
                intent.putExtra("Amount", dataCategorywiseExpenses.getAmount());
                context.startActivity(intent);
               /* Intent intent=new Intent(context,CategorywiseDetails.class);
                intent.putExtra("AmountType",dataCategorywiseExpenses.getAmounttype());
                intent.putExtra("CategoryType",dataCategorywiseExpenses.getCategorytype());
                intent.putExtra("CategoryName",dataCategorywiseExpenses.getCategoryname());
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListCategorywise.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName,txtAmount;
        LinearLayout linearCategoryDetails;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName=itemView.findViewById(R.id.txtCategoryName);
            txtAmount=itemView.findViewById(R.id.txtTotalAmount);
            linearCategoryDetails=itemView.findViewById(R.id.linearCategoryDetails);
        }
    }
}
