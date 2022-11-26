package com.example.campustaurant;

import java.util.HashMap;

public class Room {
    private String userToken;
    private String roomName;
    private String userId;
    private String food;
    private String restaurant;
    private HashMap<String, String> tag;

    public Room() {}

    public Room(String userToken, String roomName, String userId, String food, String restaurant, HashMap<String, String> tag) {
        this.userToken = userToken;
        this.roomName = roomName;
        this.userId = userId;
        this.food = food;
        this.restaurant = restaurant;
        this.tag = tag;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public HashMap<String, String> getTag() {
        return tag;
    }

    public void setTag(HashMap<String, String> tag) {
        this.tag = tag;
    }
}