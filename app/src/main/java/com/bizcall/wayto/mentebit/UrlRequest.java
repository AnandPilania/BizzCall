package com.bizcall.wayto.mentebit;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

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

public class UrlRequest {
    static UrlRequest myObj;
    public static String responsecode1="";
    String result;
    RequestQueue requestQueue;
    Context context;
    ProgressDialog dialog;
    String url = "http://anilsahasrabuddhe.in/monalitesting/test3.php";
    long startTime;
    long endTime;
   AlertDialog alertDialog1;
   Home home;

    // bandwidth in kbps
    private int POOR_BANDWIDTH = 50000;
    private int AVERAGE_BANDWIDTH = 100000;
    private int GOOD_BANDWIDTH = 200000;

    /*   public UrlRequest(Context context)
       {
           this.context = context;

       }
   */
    public UrlRequest() {
    }

    public static UrlRequest getObject() {

        if (myObj == null)
            myObj = new UrlRequest();
        return myObj;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return responsecode1;
    }

    public String getResponse(final ServerCallback callback) {
        if (CheckInternet.checkInternet(context)) {
            startTime = System.currentTimeMillis();
            //dialog=new ProgressDialog(context);
            requestQueue = Volley.newRequestQueue(context);
           // result = "omg";
            responsecode1="Normal";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("*******", response.toString());
                            try {
                                callback.onSuccess(response);

                            } catch (Exception e) {
                                e.printStackTrace();
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
                            if (error.networkResponse != null||error instanceof TimeoutError||error instanceof NoConnectionError||error instanceof AuthFailureError||error instanceof ServerError||error instanceof NetworkError||error instanceof ParseError) {
                                responsecode1="ServerError";
                             //   dialog.dismiss();
                                Toast.makeText(context,"Server Error",Toast.LENGTH_SHORT).show();
                              // showCustomPopupMenu();
                                Log.e("Volley", "Error.HTTP Status Code:" + error.networkResponse.statusCode+" "+responsecode1);
                            }
                         /*   if (error instanceof TimeoutError) {
                                Log.e("Volley", "TimeoutError");
                            } else if (error instanceof NoConnectionError) {
                                Log.e("Volley", "NoConnectionError");
                            } else if (error instanceof AuthFailureError) {
                                Log.e("Volley", "AuthFailureError");
                            } else if (error instanceof ServerError) {
                                Log.e("Volley", "ServerError");
                            } else if (error instanceof NetworkError) {
                                Log.e("Volley", "NetworkError");
                            } else if (error instanceof ParseError) {
                                Log.e("Volley", "ParseError");
                            }*/
                            //dialog.dismiss();
                        }
                    });
            requestQueue.add(stringRequest);
            endTime = System.currentTimeMillis();

            double timeTakenMills = Math.floor(endTime - startTime);  // time taken in milliseconds
            double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds
            Log.d("NetTime", String.valueOf(timeTakenSecs));
            final int kilobytePerSec = (int) Math.round(1024 / timeTakenSecs);
            Log.d("Kilobytes", String.valueOf(kilobytePerSec));

            if(kilobytePerSec <= POOR_BANDWIDTH){
                Log.d("Entered","Slow");
              //  Toast.makeText(context,"Slow internet speed",Toast.LENGTH_SHORT).show();
            }
            else if(kilobytePerSec<=AVERAGE_BANDWIDTH)
            {
                Log.d("Entered","Average");
              //  Toast.makeText(context,"Average internet speed",Toast.LENGTH_SHORT).show();

            }
            else if(kilobytePerSec<=GOOD_BANDWIDTH)
            {
                Log.d("Entered","Good");
               // Toast.makeText(context,"Good internet speed",Toast.LENGTH_SHORT).show();
            }
        } else {

           /* TastyToast.makeText(context, "No Internet Connection..!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            @SuppressLint("ResourceAsColor") NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(context).setBgGradientCenter(R.color.ni_bg4).setCancelable(true).build();*/


            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        return responsecode1;
    }

}
