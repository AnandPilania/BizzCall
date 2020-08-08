package com.bizcall.wayto.mentebit13;

public class DataEmpDetails {
    String empid,name,dob,imgurl;

    public DataEmpDetails(String empid, String name, String dob, String imgurl) {
        this.empid = empid;
        this.name = name;
        this.dob = dob;
        this.imgurl = imgurl;
    }

    public String getEmpid() {
        return empid;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getImgurl() {
        return imgurl;
    }
}
