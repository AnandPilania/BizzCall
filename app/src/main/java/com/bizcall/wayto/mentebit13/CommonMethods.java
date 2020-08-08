package com.bizcall.wayto.mentebit13;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.util.Calendar;

public class CommonMethods
{

    final String TAGCM = "Inside Service";
    Calendar cal = Calendar.getInstance();
    String path;
    public String getDate() {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        String date = String.valueOf(day) + "_" + String.valueOf(month) + "_" + String.valueOf(year);

        Log.d(TAGCM, "Date " + date);
        return date;
    }

    public String getTIme() {
        String am_pm = "";
        int sec = cal.get(Calendar.SECOND);
        int min = cal.get(Calendar.MINUTE);
        int hr = cal.get(Calendar.HOUR);
        int amPm = cal.get(Calendar.AM_PM);
        if (amPm == 1)
            am_pm = "PM";
        else if (amPm == 0)
            am_pm = "AM";

        String time = String.valueOf(hr) + "_" + String.valueOf(min) + "_" + String.valueOf(sec) + "_" + am_pm;

        Log.d(TAGCM, "Date " + time);
        return time;
    }

    public String getPath()
    {
        String internalFile = getDate();
       // File mydir = context.getDir("users", Context.MODE_PRIVATE);
        File file = new File(Environment.getExternalStorageDirectory()+"/Bizcall/CallRecording/");
       // File fileWithinMyDir = new File(file, "/Bizcall/CallRecording");
        // File file1=new File(Environment.getExternalStorageDirectory()+"/My Records/");
        // File file1=new File(Environment.getExternalStorageDirectory()+"/My Records/"+internalFile+"/");
        if (!file.exists()) {
            file.mkdirs();
        }

            path = file.getAbsolutePath();

            Log.d("Path111", path);
        return path;
    }


    public String getContactName(final String number, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }
        if (contactName != null && !contactName.equals(""))
            return contactName;
        else
            return "";
    }
}
