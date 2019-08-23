package com.bizcall.wayto.mentebit;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CheckInternetSpeed {
   // Context context;
    public static  String speed;
   public static WifiManager wifiManager;
   public static NetworkInfo info;
    public static String checkInternet(Context context) {
        info=getInfo(context);
        if (!CheckInternet.checkInternet(context) ){
            speed="0";
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("No Internet connection!!!")
                    .setMessage("Can't do further process")

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


            //  Toast.makeText(context, "Service Unavailable", Toast.LENGTH_SHORT).show();
        }
        else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            int linkspeed=wifiInfo.getLinkSpeed();
            Log.d("InternetLevel",level+""+" "+linkspeed);
            if (level == 2||level==1) {
                speed="1";
               /* android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Home.this);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        *//*edtName.setText("");
                                        edtPassword.setText("");*//*
                                dialog.dismiss();

                            }
                        })


                        .show();*/

                //    Toast.makeText(context, "Wifi POOR", Toast.LENGTH_SHORT).show();
            }/* else if (level == 3) {
                Toast.makeText(context, "Wifi MODERATE", Toast.LENGTH_SHORT).show();
            }*/ else if (level==3||level == 4||level == 5) {
                    speed="2";

                // Toast.makeText(context, "Wifi GOOD", Toast.LENGTH_SHORT).show();
            } /*else if (level == 5) {
                Toast.makeText(context, "Wifi EXCELLENT", Toast.LENGTH_SHORT).show();
            }*//* else {
                Toast.makeText(context, "Wifi Slow", Toast.LENGTH_SHORT).show();
            }*/
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkClass = getNetworkClass(getNetworkType(context));
            if (networkClass == 1) {
                speed="1";
               /* android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Slow Internet speed!!!")
                        .setMessage("Can't do further process")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //insertIMEI();
                                        *//*edtName.setText("");
                                        edtPassword.setText("");*//*
                                dialog.dismiss();

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        *//* .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();

                             }
                         })*//*
                        .show();*/
                Toast.makeText(context, "Mobile Data POOR", Toast.LENGTH_SHORT).show();
            } else if (networkClass == 2||networkClass == 3) {

                speed="2";
                //  Toast.makeText(context, "Mobile Data GOOD", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(context, "Check Your Service", Toast.LENGTH_SHORT).show();
        return speed;
    }

    public static NetworkInfo getInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    public static int getNetworkClass(int networkType) {
        try {
            return getNetworkClassReflect(networkType);
        } catch (Exception ignored) {
        }

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case 16: // TelephonyManager.NETWORK_TYPE_GSM:
                return 1;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case 17: // TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return 2;
            case TelephonyManager.NETWORK_TYPE_LTE:
            case 18: // TelephonyManager.NETWORK_TYPE_IWLAN:
                return 3;
            default:
                return 0;
        }
    }

    private static int getNetworkClassReflect(int networkType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getNetworkClass = TelephonyManager.class.getDeclaredMethod("getNetworkClass", int.class);
        if (!getNetworkClass.isAccessible()) {
            getNetworkClass.setAccessible(true);
        }
        return (Integer) getNetworkClass.invoke(null, networkType);
    }

    public static int getNetworkType(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType();
    }
}
