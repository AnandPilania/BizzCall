package com.bizcall.wayto.mentebit13;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddCategoryType extends AppCompatActivity
{
ProgressDialog dialog;
RequestQueue requestQueue;
String clienturl,clientid,counselorid;
SharedPreferences sp;
ArrayList<DataCategory> arrayListCategory;
Spinner spinner;
ArrayList<String> arrayList;
ArrayList<DataCategoryName> arrayListExpenses;
ArrayList<DataCategoryName> arrayListInward;
DataCategory dataCategory;
AdapterCategory adapterCategory;
RecyclerView recyclerCategoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);
        requestQueue= Volley.newRequestQueue(AddCategoryType.this);
        sp=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        clientid=sp.getString("ClientId","");
        clienturl=sp.getString("ClientUrl","");
        counselorid=sp.getString("Id","");
        spinner=findViewById(R.id.spinnerCategory);
        recyclerCategoryName=findViewById(R.id.recyclerCategoryName);
        arrayList=new ArrayList<>();
        arrayListExpenses=new ArrayList<>();
        arrayListInward=new ArrayList<>();
        arrayList.add(0,"Expenses");
        arrayList.add(1,"Inward");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(AddCategoryType.this, R.layout.spinner_item1, arrayList);
        // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        dialog=ProgressDialog.show(AddCategoryType.this,"","Loading expenses",true);
        getExpenses();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(spinner.getSelectedItemPosition()==0) {
                    adapterCategory = new AdapterCategory(AddCategoryType.this, arrayListExpenses);
                    //LinearLayoutManager layoutManager = new LinearLayoutManager(AddExpenses.this);
                    //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerCategoryName.setLayoutManager(new GridLayoutManager(AddCategoryType.this, 2));
                    recyclerCategoryName.setAdapter(adapterCategory);
                    adapterCategory.notifyDataSetChanged();
                }else {

                    adapterCategory = new AdapterCategory(AddCategoryType.this, arrayListInward);
                   // LinearLayoutManager layoutManager = new LinearLayoutManager(AddExpenses.this);
                    //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerCategoryName.setLayoutManager(new GridLayoutManager(AddCategoryType.this, 2));
                    recyclerCategoryName.setAdapter(adapterCategory);
                    adapterCategory.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }
        });

    }
    public void getExpenses() {
        try {
            if(CheckServer.isServerReachable(AddCategoryType.this)) {
                String url = clienturl + "?clientid=" + clientid +"&caseid=A12";
                Log.d("CategoryUrl", url);
                arrayListCategory=new ArrayList<>();
                arrayListCategory.clear();
                arrayListInward.clear();
                arrayListExpenses.clear();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Log.d("CategoryResponse1", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Json", jsonObject.toString());
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String categoryid=jsonObject1.getString("AccountCategoryID");
                                        String categoryname=jsonObject1.getString("CategoryName");
                                        String categorytype=jsonObject1.getString("CategoryType");
                                        String date=jsonObject1.getString("dtcreatedDate");
                                      //  String dt21=date.substring(date.indexOf("date")+7,date.indexOf(","));
                                         dataCategory=new DataCategory(categoryid,categoryname,categorytype,date);
                                        arrayListCategory.add(dataCategory);
                                        }
                                    for(int i=0;i<arrayListCategory.size();i++)
                                    {
                                        if(arrayListCategory.get(i).getCategorytype().contains("Expenses"))
                                        {
                                            String name=arrayListCategory.get(i).getCategoryname();
                                            DataCategoryName dataCategoryName=new DataCategoryName(name);
                                            arrayListExpenses.add(dataCategoryName);
                                        }
                                        else {
                                            String name1=arrayListCategory.get(i).getCategoryname();
                                            DataCategoryName dataCategoryName=new DataCategoryName(name1);
                                            arrayListInward.add(dataCategoryName);
                                        }
                                    }
                                    if(spinner.getSelectedItemPosition()==0)
                                    {
                                        adapterCategory = new AdapterCategory(AddCategoryType.this, arrayListExpenses);
                                        //LinearLayoutManager layoutManager = new LinearLayoutManager(AddExpenses.this);
                                       // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        recyclerCategoryName.setLayoutManager(new GridLayoutManager(AddCategoryType.this, 2));
                                        recyclerCategoryName.setAdapter(adapterCategory);
                                        adapterCategory.notifyDataSetChanged();
                                    }

                                    Log.d("CategorySize",arrayListExpenses.size()+" "+arrayListInward.size());

                                } catch (JSONException e) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(AddCategoryType.this, "Errorcode-205 CounselorContact statusResponse " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("Exception", String.valueOf(e));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error == null || error.networkResponse == null)
                                    return;
                                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                //get response body and parse with appropriate encoding
                                if (error.networkResponse != null || error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof AuthFailureError || error instanceof ServerError || error instanceof NetworkError || error instanceof ParseError) {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddCategoryType.this);
                                    alertDialogBuilder.setTitle("Network issue!!!")


                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.dismiss();
                                    Toast.makeText(AddCategoryType.this, "Network issue", Toast.LENGTH_SHORT).show();
                                    // showCustomPopupMenu();
                                    Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode);
                                }

                            }
                        });
                requestQueue.add(stringRequest);
            }else {
                if(dialog.isShowing()) {
                    dialog.dismiss();
                }
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddCategoryType.this);
                alertDialogBuilder.setTitle("Network issue!!!!")
                        .setMessage("Try after some time!")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        /*edtName.setText("");
                                        edtPassword.setText("");*/
                                dialog.dismiss();

                            }
                        }).show();
            }
        }catch (Exception e)
        {
            if(dialog.isShowing())
            {
                dialog.dismiss();
            }
            Toast.makeText(AddCategoryType.this,"Errorcode-204 CounselorContact getStatus1 "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("ExcStatus", String.valueOf(e));
        }
    }
}
