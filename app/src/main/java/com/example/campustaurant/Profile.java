package com.example.campustaurant;

public class Profile {
    private String name;
    private String sex;
    private String old;
    private String uri;
    private int rating = 0;

    public Profile() {}

    public Profile(String name, String sex, String old, String uri, int rating) {
        this.name = name;
        this.sex = sex;
        this.old = old;
        this.uri = uri;
        this.rating = rating;
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

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
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
