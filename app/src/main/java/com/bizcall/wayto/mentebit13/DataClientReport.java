package com.bizcall.wayto.mentebit13;

public class DataClientReport
{
    String fileID,name,counselorname,totalINR,totalUSD,discountINR,discountUSD;

    public DataClientReport(String fileID, String name, String counselorname, String totalINR, String totalUSD, String discountINR, String discountUSD) {
        this.fileID = fileID;
        this.name = name;
        this.counselorname = counselorname;
        this.totalINR = totalINR;
        this.totalUSD = totalUSD;
        this.discountINR = discountINR;
        this.discountUSD = discountUSD;
    }

    public String getFileID() {
        return fileID;
    }

    public String getName() {
        return name;
    }

    public String getCounselorname() {
        return counselorname;
    }

    public String getTotalINR() {
        return totalINR;
    }

    public String getTotalUSD() {
        return totalUSD;
    }

    public String getDiscountINR() {
        return discountINR;
    }

    public String getDiscountUSD() {
        return discountUSD;
    }
}

