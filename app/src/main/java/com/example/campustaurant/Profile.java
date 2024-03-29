package com.example.campustaurant;

import java.io.Serializable;

public class Profile implements Serializable {
    private String userToken;
    private String name = "익명";
    private String sex = "";
    private String introduce = "";
    private String uri = "https://firebasestorage.googleapis.com/v0/b/campustaurant.appspot.com/o/profile%2Fbasic.png?alt=media&token=8787b786-3c2b-4253-ad90-4bf293b07ee8";
    private int rating = 0;

    public Profile() {}

    public Profile(String userToken, String name, String sex, String introduce, String uri, int rating) {
        this.userToken = userToken;
        this.name = name;
        this.sex = sex;
        this.introduce = introduce;
        this.uri = uri;
        this.rating = rating;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
