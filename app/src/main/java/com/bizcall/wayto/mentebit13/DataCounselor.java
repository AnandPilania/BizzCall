package com.bizcall.wayto.mentebit13;

public class DataCounselor {
    String strSno,sr_no, cname, course, mobile, parentno, email, allocationdate, adrs, city, state1, pincode,statusid,status1,remarks;

    public DataCounselor(String strSno,String sr_no, String cname, String course, String mobile, String parentno, String email, String allocationdate, String adrs, String city, String state1, String pincode,String statusid,String status1,String remarks) {
       this.strSno=strSno;
        this.sr_no = sr_no;
        this.cname = cname;
        this.course = course;
        this.mobile = mobile;
        this.email = email;
        this.parentno = parentno;
        this.allocationdate = allocationdate;
        this.adrs = adrs;
        this.city = city;
        this.state1 = state1;
        this.pincode = pincode;
        this.status1=status1;
        this.statusid=statusid;
        this.remarks=remarks;

    }

    public String getStrSno() {
        return strSno;
    }

    public void setStrSno(String strSno) {
        this.strSno = strSno;
    }

    public String getAdrs() {
        return adrs;
    }

    public void setAdrs(String adrs) {
        this.adrs = adrs;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState1() {
        return state1;
    }

    public String getStatusid() {
        return statusid;
    }

    public void setStatusid(String statusid) {
        this.statusid = statusid;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setState1(String state1) {
        this.state1 = state1;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAllocationdate() {
        return allocationdate;
    }

    public void setAllocationdate(String allocationdate) {
        this.allocationdate = allocationdate;
    }

    public String getParentno() {
        return parentno;
    }

    public void setParentno(String parentno) {
        this.parentno = parentno;
    }

    public String getSr_no() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
