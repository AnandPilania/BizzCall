package com.bizcall.wayto.mentebit13;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataMessage {
    private String messageUser;
    private String messageText;
    private String messageUserId;
    private String messageTime;
    public DataMessage(String messageUser, String messageText, String messageUserId) {
        this.messageUser = messageUser;
        this.messageText = messageText;
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        messageTime =strDate;
        this.messageUserId = messageUserId;

    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }


}
