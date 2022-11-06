package com.example.campustaurant;

public class Match {
    public String userId;
    public String restaurant;

    public Match() {}

    public Match(String userId, String restaurant) {
        this.userId = userId;
        this.restaurant = restaurant;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
}