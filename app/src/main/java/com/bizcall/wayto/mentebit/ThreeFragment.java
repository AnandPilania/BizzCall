package com.bizcall.wayto.mentebit;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class ThreeFragment extends Fragment {

    UrlRequest urlRequest;
    RecyclerView recyclerView;
    AdapterTotalCallMade adapterTotalCallMade;
    ArrayList<DataTotalCallMade> arrayList;
    ProgressDialog dialog;
    LinearLayout linearLayout;
    SharedPreferences sp;
    String clientid,sr_no,clienturl;
    TextView txtMsg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_three, container, false);
        try{
        view.setBackgroundColor(getResources().getColor(R.color.white));

        linearLayout = view.findViewById(R.id.linearCallColumns);
        txtMsg = view.findViewById(R.id.txtNoCallMadeMsg);
        recyclerView = view.findViewById(R.id.recycleTotalCallMade);
        //dialog = ProgressDialog.show(getActivity(), "Loading", "Please wait.....", false, true);
        sp = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        sr_no = sp.getString("SelectedSrNo", null);
        clientid = sp.getString("ClientId", null);
        clienturl=sp.getString("ClientUrl",null);
        dialog = ProgressDialog.show(getActivity(), "", "Loading ...", false,true);
        getTotalCallMade();
        } catch (Exception e) {
            Log.d("Exception", String.valueOf(e));
        }
        return view;
    }
    public void getTotalCallMade()
    {
        arrayList = new ArrayList<>();
        urlRequest = UrlRequest.getObject();
        urlRequest.setContext(getActivity());
        urlRequest.setUrl(clienturl+"?clientid=" + clientid + "&caseid=23&SrNo=" + sr_no);
        Log.d("TotalCallMadeUrl", clienturl+"?clientid=" + clientid + "&caseid=23&SrNo=" +sr_no);
        urlRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
               // linearLayout.setVisibility(View.VISIBLE);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("TotalCallResponse", response);

                if (response.contains("[]"))
                {
                    linearLayout.setVisibility(View.GONE);
                    txtMsg.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    txtMsg.setVisibility(View.GONE);
                }
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        // Log.d("Json",jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String srno = jsonObject1.getString("nSrNo");
                            String duration = jsonObject1.getString("cCallDuration");
                            String cdate = jsonObject1.getString("Call date");
                            String filename = jsonObject1.getString("cFileName");
                            DataTotalCallMade dataTotalCallMade = new DataTotalCallMade(srno, duration, cdate, filename);
                            arrayList.add(dataTotalCallMade);
                        }
                        adapterTotalCallMade = new AdapterTotalCallMade(getActivity(), arrayList);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapterTotalCallMade);
                        adapterTotalCallMade.notifyDataSetChanged();

                        Log.d("Size**", String.valueOf(arrayList.size()));
                    } catch (Exception e) {
                        Log.d("Exception", String.valueOf(e));
                    }

            }
        });
    }

}
