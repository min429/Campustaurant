package com.example.campustaurant;

public class Chat {
    String userToken;
    String datetime;
    String userId;
    String text;

    public Chat() {}

    public Chat(String userToken, String datetime, String userId, String text) {
        this.userToken = userToken;
        this.datetime = datetime;
        this.userId = userId;
        this.text = text;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
