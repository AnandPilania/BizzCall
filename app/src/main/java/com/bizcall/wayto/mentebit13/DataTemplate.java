package com.bizcall.wayto.mentebit13;

public class DataTemplate {
    String template;
       int smsid;

    public DataTemplate(int smsid,String template) {
        this.smsid=smsid;
        this.template = template;
    }

    public int getSmsid() {
        return smsid;
    }

    public String getTemplate() {
        return template;
    }
}
