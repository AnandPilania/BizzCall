package com.bizcall.wayto.mentebit;

import java.io.Serializable;

public class DetailStatuswiseLeadReportData implements Serializable {
    String mSrNo, mRefNo, mCandName, mCourse, mMobile, mAddress, mCity, mState, mPinCode, mParentNo, mEmail, mDataFrom,
            mAllocatedTo, mAllocatedDate, mStatus, mRemark, mCreatedDate;

    public DetailStatuswiseLeadReportData(String mSrNo, String mRefNo, String mCandName, String mCourse, String mMobile,
                                          String mAddress, String mCity, String mState, String mPinCode, String mParentNo,
                                          String mEmail, String mDataFrom, String mAllocatedTo, String mAllocatedDate,
                                          String mStatus, String mRemark, String mCreatedDate) {
        this.mSrNo = mSrNo;
        this.mRefNo = mRefNo;
        this.mCandName = mCandName;
        this.mCourse = mCourse;
        this.mMobile = mMobile;
        this.mAddress = mAddress;
        this.mCity = mCity;
        this.mState = mState;
        this.mPinCode = mPinCode;
        this.mParentNo = mParentNo;
        this.mEmail = mEmail;
        this.mDataFrom = mDataFrom;
        this.mAllocatedTo = mAllocatedTo;
        this.mAllocatedDate = mAllocatedDate;
        this.mStatus = mStatus;
        this.mRemark = mRemark;
        this.mCreatedDate = mCreatedDate;
    }

    public String getmSrNo() {
        return mSrNo;
    }

    public String getmRefNo() {
        return mRefNo;
    }

    public String getmCandName() {
        return mCandName;
    }

    public String getmCourse() {
        return mCourse;
    }

    public String getmMobile() {
        return mMobile;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmState() {
        return mState;
    }

    public String getmPinCode() {
        return mPinCode;
    }

    public String getmParentNo() {
        return mParentNo;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmDataFrom() {
        return mDataFrom;
    }

    public String getmAllocatedTo() {
        return mAllocatedTo;
    }

    public String getmAllocatedDate() {
        return mAllocatedDate;
    }

    public String getmStatus() {
        return mStatus;
    }

    public String getmRemark() {
        return mRemark;
    }

    public String getmCreatedDate() {
        return mCreatedDate;
    }
}
