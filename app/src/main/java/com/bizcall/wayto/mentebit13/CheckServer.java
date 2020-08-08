package com.bizcall.wayto.mentebit13;

import android.content.Context;

public class CheckServer {
    static int temp=0;
    public static boolean isServerReachable(Context context)
    // To check if server is reachable
    {
       /* new Thread(new Runnable() {
            public void run() {
                try {
                    if(InetAddress.getByName("anilsahasrabuddhe.in").isReachable(8000)) {//Replace with your name
                        temp = 0;
                    }
                    else
                    {
                        temp=1;
                    }
                } catch (Exception e) {
                    temp=1;
                    Log.d("ServerException",e.toString());
                }
            }
        }).start();
        Log.d("Temp",temp+"");
        if(temp==0) {
            return true;
        }
        else {
            return true;
        }*/
       return true;
    }
}
