package com.bizcall.wayto.mentebit13;

public class DataMasterEntry
{

    private String mFileno, mSrno, mFirstname, mLastname, mDob, mSex, mParentname, mAddress, mCity, mState,
            mPincode,mobile,parentno,email,remarks,status;

    public DataMasterEntry(String mFileno, String mSrno, String mFirstname, String mLastname, String mDob, String mSex, String mParentname, String mAddress, String mCity, String mState, String mPincode,String mobile,String parentno,String email,String remarks,String status) {
        this.mFileno = mFileno;
        this.mSrno = mSrno;
        this.mFirstname = mFirstname;
        this.mLastname = mLastname;
        this.mDob = mDob;
        this.mSex = mSex;
        this.mParentname = mParentname;
        this.mAddress = mAddress;
        this.mCity = mCity;
        this.mState = mState;
        this.mPincode = mPincode;
        this.mobile=mobile;
        this.parentno=parentno;
        this.email=email;
        this.remarks=remarks;
        this.status=status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getParentno() {
        return parentno;
    }

    public void setParentno(String parentno) {
        this.parentno = parentno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getmFileno() {
        return mFileno;
    }

    public void setmFileno(String mFileno) {
        this.mFileno = mFileno;
    }

    public String getmSrno() {
        return mSrno;
    }

    public void setmSrno(String mSrno) {
        this.mSrno = mSrno;
    }

    public String getmFirstname() {
        return mFirstname;
    }

    public void setmFirstname(String mFirstname) {
        this.mFirstname = mFirstname;
    }

    public String getmLastname() {
        return mLastname;
    }

    public void setmLastname(String mLastname) {
        this.mLastname = mLastname;
    }

    public String getmDob() {
        return mDob;
    }

    public void setmDob(String mDob) {
        this.mDob = mDob;
    }

    public String getmSex() {
        return mSex;
    }

    public void setmSex(String mSex) {
        this.mSex = mSex;
    }

    public String getmParentname() {
        return mParentname;
    }

    public void setmParentname(String mParentname) {
        this.mParentname = mParentname;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmPincode() {
        return mPincode;
    }

    public void setmPincode(String mPincode) {
        this.mPincode = mPincode;
    }

}
