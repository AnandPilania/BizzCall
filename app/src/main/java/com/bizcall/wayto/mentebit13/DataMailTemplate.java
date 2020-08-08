package com.bizcall.wayto.mentebit13;

public class DataMailTemplate
{
    String mailid,mailsubject,mailbody,attachments1,attachments2,attachments3,attachments4,attachments5,mailImage;

    public DataMailTemplate(String mailid,String mailsubject, String mailbody, String attachments1, String attachments2, String attachments3, String attachments4, String attachments5, String mailImage) {
        this.mailid=mailid;
        this.mailsubject = mailsubject;
        this.mailbody = mailbody;
        this.attachments1 = attachments1;
        this.attachments2 = attachments2;
        this.attachments3 = attachments3;
        this.attachments4 = attachments4;
        this.attachments5 = attachments5;
        this.mailImage = mailImage;
    }

    public String getMailid() {
        return mailid;
    }

    public String getMailsubject() {
        return mailsubject;
    }

    public String getMailbody() {
        return mailbody;
    }

    public String getAttachments1() {
        return attachments1;
    }

    public String getAttachments2() {
        return attachments2;
    }

    public String getAttachments3() {
        return attachments3;
    }

    public String getAttachments4() {
        return attachments4;
    }

    public String getAttachments5() {
        return attachments5;
    }

    public String getMailImage() {
        return mailImage;
    }
}
