package com.example.lululab.Model;

import java.io.Serializable;

public class Message implements Serializable {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";

    public static String SENT_BY_SYSTEM = "system";

    private String message;
    private String imageUrl;
    private String sentBy;
    private boolean isScrab;

    public Message(String message, String sentBy, String imageUrl) {
        this.message = message;
        this.sentBy = sentBy;
        this.imageUrl = imageUrl;
        this.isScrab = false;
    }

    // ... 기존 getter 및 setter 메소드 ...

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public boolean getIsScrab() {
        return isScrab;
    }

    public void setIsScrab(boolean isScrab) {
        this.isScrab = isScrab;
    }
}