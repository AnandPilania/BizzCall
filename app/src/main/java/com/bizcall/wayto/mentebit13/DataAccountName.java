package com.bizcall.wayto.mentebit13;

public class DataAccountName {
    String accountno,accountname,accountid,servicetax;

    public DataAccountName(String accountname, String accountid, String servicetax,String accountno) {
        this.accountname = accountname;
        this.accountid = accountid;
        this.servicetax = servicetax;
        this.accountno=accountno;
    }

    public String getAccountno() {
        return accountno;
    }

    public String getAccountname() {
        return accountname;
    }

    public String getAccountid() {
        return accountid;
    }

    public String getServicetax() {
        return servicetax;
    }
}
