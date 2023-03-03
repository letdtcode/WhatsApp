package com.mascara.whatsapp.Models;

import com.google.firebase.database.PropertyName;

public class Messages {

    @PropertyName("uId")
    private String uId;

    @PropertyName("message")
    private String message;

    @PropertyName("messageId")
    private String messageId;

    @PropertyName("timestamp")
    private Long timestamp;

    @PropertyName("sttMessage")
    private int sttMessage;

    public int getSttMessage() {
        return sttMessage;
    }

    public void setSttMessage(int sttMessage) {
        this.sttMessage = sttMessage;
    }

    public Messages(String uId, String message, Long timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Messages(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public Messages() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
