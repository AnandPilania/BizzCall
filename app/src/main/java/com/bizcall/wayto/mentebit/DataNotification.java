package com.bizcall.wayto.mentebit;

public class DataNotification {
    String strNotificaion,srno,notificationId;

    public DataNotification(String strNotificaion,String srno,String notificationId) {
        this.strNotificaion = strNotificaion;
        this.srno=srno;
        this.notificationId=notificationId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getStrNotificaion() {
        return strNotificaion;
    }

    public void setStrNotificaion(String strNotificaion) {
        this.strNotificaion = strNotificaion;
    }
}
