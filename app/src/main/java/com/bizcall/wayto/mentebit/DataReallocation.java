package com.bizcall.wayto.mentebit;

public class DataReallocation
{
    String serial,sr_no, cname,couselorname, course,status1;

    public DataReallocation(String serial,String sr_no, String cname, String course, String status1,String couselorname) {

        this.sr_no = sr_no;
        this.cname = cname;
        this.course = course;
        this.status1=status1;
        this.couselorname=couselorname;
        this.serial=serial;


    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getCouselorname() {
        return couselorname;
    }

    public void setCouselorname(String couselorname) {
        this.couselorname = couselorname;
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

}
