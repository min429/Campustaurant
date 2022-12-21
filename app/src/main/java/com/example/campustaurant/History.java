package com.example.campustaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class History {
    private String date;
    private String restaurant;
    private HashMap<String, String> user;

    public History() {}

    public History(String date, String restaurant, HashMap<String, String> user) {
        this.date = date;
        this.restaurant = restaurant;
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public HashMap<String, String> getUser() {
        return user;
    }

    public void setUser(HashMap<String, String> user) {
        this.user = user;
    }
}
