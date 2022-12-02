package com.example.campustaurant;

public class Chat {
    private String userToken;
    private String datetime;
    private String userId;
    private String userName = "익명";
    private String text;
    private String uri;

    public Chat() {}

    public Chat(String userToken, String datetime, String userId, String userName, String text, String uri) {
        this.userToken = userToken;
        this.datetime = datetime;
        this.userId = userId;
        this.userName = userName;
        this.text = text;
        this.uri = uri;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
