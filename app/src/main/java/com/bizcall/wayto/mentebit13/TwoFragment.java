package com.bizcall.wayto.mentebit13;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {

    String sr_no,clientid,clienturl;
    UrlRequest urlRequest;
    SharedPreferences sp;
    ProgressDialog dialog;
    RecyclerView recyclerStatus;
    AdapterStatus adapterStatus;
    ArrayList<DataStatus> arrayListStatus;
    TextView txtNoStatus;

    LinearLayout linearStatusTitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_two, container, false);
        try {

        view.setBackgroundColor(getResources().getColor(R.color.white));
        sp = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        recyclerStatus=view.findViewById(R.id.recyclerStatus);
        txtNoStatus=view.findViewById(R.id.txtNoStatus);
        linearStatusTitle=view.findViewById(R.id.TitleStatus);
        arrayListStatus=new ArrayList<>();
        sr_no = sp.getString("SelectedSrNo", null);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        dialog = ProgressDialog.show(getActivity(), "", "Loading..", false,true);
        getStatusBackup();

        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
        return view;
    }
    public void getStatusBackup()
    {
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getActivity());
        urlRequest.setUrl(clienturl+"?clientid="+clientid+"&caseid=22&SrNo="+sr_no);
        Log.d("StatusUrl1", clienturl+"?clientid="+clientid+"&caseid=22&SrNo="+sr_no);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                dialog.dismiss();

                Log.d("StatusBackResponse1", response);
                try {
                    if (response.contains("[]"))
                    {
                        linearStatusTitle.setVisibility(View.GONE);
                        txtNoStatus.setVisibility(View.VISIBLE);
                    } else {
                        linearStatusTitle.setVisibility(View.VISIBLE);
                        txtNoStatus.setVisibility(View.GONE);
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Json", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String status = jsonObject1.getString("cStatus");
                        String statusdate=jsonObject1.getString("Status Change Date");
                        DataStatus dataStatus=new DataStatus(status,statusdate);
                        arrayListStatus.add(dataStatus);
                        // Log.d("Json11111",arrayList1.toString());
                    }
                    adapterStatus=new AdapterStatus(getActivity(),arrayListStatus);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerStatus.setLayoutManager(linearLayoutManager);
                    recyclerStatus.setAdapter(adapterStatus);
                    adapterStatus.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.d("Exception", String.valueOf(e));
                }
            }
        });
    }

}
