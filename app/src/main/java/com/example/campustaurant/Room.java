package com.example.campustaurant;

public class Room {
    public String userToken;
    public String roomName;
    public String userId;
    public String food;
    public String restaurant;

    public Room() {}

    public Room(String userToken, String roomName, String userId, String food, String restaurant) {
        this.userToken = userToken;
        this.roomName = roomName;
        this.userId = userId;
        this.food = food;
        this.restaurant = restaurant;
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
}