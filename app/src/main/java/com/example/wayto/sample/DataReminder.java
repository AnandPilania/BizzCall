package com.example.wayto.sample;

public class DataReminder
{

    private String mId, mName, mCourse, mMobile1, mMobile2, mRemarks, mDate, mTime,callingId;

    public DataReminder(String mId, String mName, String mCourse, String mMobile1, String mMobile2,
                   String mRemarks, String mDate, String mTime,String callingId) {
        this.mId = mId;
        this.mName = mName;
        this.mCourse = mCourse;
        this.mMobile1 = mMobile1;
        this.mMobile2 = mMobile2;
        this.mRemarks = mRemarks;
        this.mDate = mDate;
        this.mTime = mTime;
        this.callingId=callingId;
    }

    public String getCallingId() {
        return callingId;
    }

    public void setCallingId(String callingId) {
        this.callingId = callingId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmCourse() {
        return mCourse;
    }

    public void setmCourse(String mCourse) {
        this.mCourse = mCourse;
    }

    public String getmMobile1() {
        return mMobile1;
    }

    public void setmMobile1(String mMobile1) {
        this.mMobile1 = mMobile1;
    }

    public String getmMobile2() {
        return mMobile2;
    }

    public void setmMobile2(String mMobile2) {
        this.mMobile2 = mMobile2;
    }

    public String getmRemarks() {
        return mRemarks;
    }

    public void setmRemarks(String mRemarks) {
        this.mRemarks = mRemarks;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

}
