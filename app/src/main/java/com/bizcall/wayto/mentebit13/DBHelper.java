package com.bizcall.wayto.mentebit13;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;
    public static final String db_name = "MobContacts.db";
    public static final String bizcontact_table = "BizContact_table";
    public static final String bizleadinfo_table = "BizLeadInfo_table";
    public static final String bizcalllog_table = "BizCallLog_table";

    public static final String ContactName = "ContactName";
    public static final String ContactNo = "ContactNo";
    public static final String ContactEmail = "ContactEmail";
    public static final String ContactCompNM = "ContactCompNM";
    public static final String ContactUploaded = "ContactUploaded";
    public static final String cRemarks = "cRemarks";

    public static final String leadContactName = "leadContactName";
    public static final String leadMobileNo = "leadMobileNo";
    public static final String leadCallType = "leadCallType";
    public static final String leadCallDate = "leadCallDate";
    public static final String leadCallTime = "leadCallTime";
    public static final String leadCallRecording = "leadCallRecording";
    public static final String leadUploaded = "leadUploaded";


    public static final String CallLogID = "CallLogID";
    public static final String callLogName = "callLogName";
    public static final String callLogMobNo = "callLogMobNo";
    public static final String callLogType = "callLogType";
    public static final String callLogDate = "callLogDate";
    public static final String callLogDuration = "callLogDuration";
    public static final String callLogCounselorID = "callLogCounselorID";
    public static final String callLogUploaded = "callLogUploaded";

    public DBHelper(Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + bizcontact_table + " (ContactID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ContactName TEXT, ContactNo INTEGER, ContactEmail TEXT, ContactCompNM TEXT, ContactUploaded TEXT, cRemarks TEXT)");
        db.execSQL("create table " + bizleadinfo_table + " (CallLeadID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, leadContactName TEXT, leadMobileNo TEXT, leadCallType TEXT, leadCallDate TEXT, leadCallTime TEXT, leadCallRecording TEXT, leadUploaded TEXT)");
        db.execSQL("create table " + bizcalllog_table + " (CallLogID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, callLogName TEXT, callLogMobNo TEXT, callLogType TEXT, callLogDate TEXT, callLogDuration TEXT, callLogCounselorID TEXT, callLogUploaded TEXT)");
        Log.d("dbase", "Table Created..");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);*/
    }

    //----------------------insert contact no-----------------------------
    public long insertNumber(DataContactList dataContactList) {
        sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ContactName", dataContactList.getConName());
        cv.put("ContactNo", dataContactList.getConNumber());
        cv.put("ContactEmail", dataContactList.getConEmail());
        cv.put("ContactCompNM", dataContactList.getConCompName());
        cv.put("ContactUploaded", dataContactList.getConUploaded());
        cv.put("cRemarks", dataContactList.getConRemark());
        return sqLiteDatabase.insert(bizcontact_table, null, cv);
    }

    public ArrayList<DataContactList> getAllDBContacts() {
        sqLiteDatabase = getReadableDatabase();

        Cursor c = sqLiteDatabase.query(bizcontact_table, null, null, null, null, null, "ContactID DESC");

        ArrayList<DataContactList> logArrayList = new ArrayList<>();

        while (c.moveToNext()) {
            logArrayList.add(new DataContactList(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6)));
        }
        c.close();
        sqLiteDatabase.close();
        return logArrayList;
    }

    //----------------------insert lead info------------------------------
    public long insertLeadInfoNumber(DataLeadInfo dataContactLogs) {
        sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("leadContactName", dataContactLogs.getLeadName());
        cv.put("leadMobileNo", dataContactLogs.getLeadNumber());
        cv.put("leadCallType", dataContactLogs.getLeadType());
        cv.put("leadCallDate", dataContactLogs.getLeadDate());
        cv.put("leadCallTime", dataContactLogs.getLeadTime());
      //  cv.put("leadCallRecording", dataContactLogs.getLeadRecording());
       // cv.put("leadUploaded", dataContactLogs.getLeadUploaded());
        return sqLiteDatabase.insert(bizleadinfo_table, null, cv);
    }

    public ArrayList<DataLeadInfo> getAllDBLeadInfo() {
        sqLiteDatabase = getReadableDatabase();

        Cursor c = sqLiteDatabase.query(bizleadinfo_table, null, null, null, null, null, "CallLeadID DESC");

        ArrayList<DataLeadInfo> logArrayList = new ArrayList<>();

        while (c.moveToNext()) {
            logArrayList.add(new DataLeadInfo(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7)));
        }
        c.close();
        sqLiteDatabase.close();
        return logArrayList;
    }

    //----------------------insert call log-------------------------------
    public long insertCallLogs(DataLeadInfo dataContactLogs) {
        sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("callLogName", dataContactLogs.getLeadName());
        cv.put("callLogMobNo", dataContactLogs.getLeadNumber());
        cv.put("callLogType", dataContactLogs.getLeadType());
       cv.put("callLogDate", dataContactLogs.getLeadDate());
        cv.put("callLogDuration", dataContactLogs.getLeadTime());
       // cv.put("callLogUploaded", dataContactLogs.getLeadUploaded());
        return sqLiteDatabase.insert(bizcalllog_table, null, cv);
    }

    public ArrayList<DataLeadInfo> getAllDBCallLogs() {
        sqLiteDatabase = getReadableDatabase();

        Cursor c = sqLiteDatabase.query(bizcalllog_table, null, null, null, null, null, "CallLogID DESC");

        ArrayList<DataLeadInfo> logArrayList = new ArrayList<>();

        while (c.moveToNext()) {
            logArrayList.add(new DataLeadInfo(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7)));
        }
        c.close();
        sqLiteDatabase.close();
        return logArrayList;
    }

    //----------------------Search contact logs--------------------------------
    public Cursor getContactLogs(String mobNo) {
        sqLiteDatabase = getReadableDatabase();

        String[] projections = {leadCallType, leadCallDate, leadCallTime};
        String selection = leadMobileNo + " LIKE ?";
        String[] selection_args = {mobNo};
        Cursor cursor = sqLiteDatabase.query(bizleadinfo_table, projections, selection, selection_args, null, null, "CallLeadID DESC", "2");
        return cursor;
    }

    //----------------------Get Contact NmNo---------------------------------
    public Cursor getContactName(String mobNo) {
        sqLiteDatabase = getReadableDatabase();

        String[] projections = {ContactName};
        String selection = ContactNo + " LIKE ?";
        String[] selection_args = {mobNo};
        Cursor cursor = sqLiteDatabase.query(bizcontact_table, projections, selection, selection_args, null, null, null);
        return cursor;
    }

    //----------------------update contact-------------------------------
    public Cursor findContactUpload() {
        sqLiteDatabase = getReadableDatabase();

        String[] projections = {ContactName,ContactNo,ContactEmail,ContactCompNM,ContactUploaded,cRemarks};
        String selection = ContactUploaded + " LIKE ?";
        String[] selection_args = {"0"};
        Cursor cursor = sqLiteDatabase.query(bizcontact_table, projections, selection, selection_args, null, null, null);
        return cursor;
    }

    public void updateContact(String uploaded) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues data = new ContentValues();
        data.put("ContactUploaded", "1");

        sqLiteDatabase.update(bizcontact_table, data, "ContactNo = '" + uploaded + "'", null);
    }

    //----------------------update leads-------------------------------
    public Cursor findLeadUpload() {
        sqLiteDatabase = getReadableDatabase();

        String[] projections = {leadContactName,leadMobileNo,leadCallType,leadCallDate,leadCallTime,leadCallRecording,leadUploaded};
        String selection = leadUploaded + " LIKE ?";
        String[] selection_args = {"0"};
        Cursor cursor = sqLiteDatabase.query(bizleadinfo_table, projections, selection, selection_args, null, null, null);
        return cursor;
    }

    public void updateLead(String uploaded) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues data = new ContentValues();
        data.put("leadUploaded", "1");

        sqLiteDatabase.update(bizleadinfo_table, data, "leadCallRecording = '" + uploaded + "'", null);
    }

    public void updateLeadNotFound(String uploaded) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues data = new ContentValues();
        data.put("leadUploaded", "3");

        sqLiteDatabase.update(bizleadinfo_table, data, "leadCallRecording = '" + uploaded + "'", null);
    }

    //----------------------update call log-------------------------------
    public Cursor findCallLogUpload() {
        sqLiteDatabase = getReadableDatabase();

        String[] projections = {CallLogID,callLogName,callLogMobNo,callLogType,callLogDate,callLogDuration,callLogUploaded};
        String selection = callLogUploaded + " LIKE ?";
        String[] selection_args = {"0"};
        Cursor cursor = sqLiteDatabase.query(bizcalllog_table, projections, selection, selection_args, null, null, null);
        return cursor;
    }

    public void updateCallLog(String uploaded) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues data = new ContentValues();
        data.put("callLogUploaded", "1");

        sqLiteDatabase.update(bizcalllog_table, data, "CallLogID = '" + uploaded + "'", null);
    }
}
