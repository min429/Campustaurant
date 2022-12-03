package com.example.campustaurant;

public class Notification {
    private String datetime;
    private String otherToken;
    private String userName;
    private String uri;
    private String rate;
    private String review;
    private int rating = 0;

    public Notification() {}

    public Notification(String datetime, String otherToken, String userName, String uri, String rate, String review, int rating) {
        this.datetime = datetime;
        this.otherToken = otherToken;
        this.userName = userName;
        this.uri = uri;
        this.rate = rate;
        this.review = review;
        this.rating = rating;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOtherToken() {
        return otherToken;
    }

    public void setOtherToken(String otherToken) {
        this.otherToken = otherToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
