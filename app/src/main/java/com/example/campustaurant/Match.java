package com.example.campustaurant;

public class Match {
    public String roomName;
    public String userId;
    public String food;
    public String restaurant;

    public Match() {}

    public Match(String roomName, String userId, String food, String restaurant) {
        this.roomName = roomName;
        this.userId = userId;
        this.food = food;
        this.restaurant = restaurant;
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
}