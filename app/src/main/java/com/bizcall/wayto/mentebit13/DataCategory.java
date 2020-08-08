package com.bizcall.wayto.mentebit13;

public class DataCategory {
    String categoryid,categoryname,categorytype,createddate;

    public DataCategory(String categoryid, String categoryname, String categorytype, String createddate) {
        this.categoryid = categoryid;
        this.categoryname = categoryname;
        this.categorytype = categorytype;
        this.createddate = createddate;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public String getCategorytype() {
        return categorytype;
    }

    public String getCreateddate() {
        return createddate;
    }
}
