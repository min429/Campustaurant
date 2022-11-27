package com.example.campustaurant;

import java.util.HashMap;

public class Room {
    private String hostToken;
    private String roomName;
    private String hostId;
    private String food;
    private String restaurant;
    private HashMap<String, String> tag;
    private HashMap<String, String> ban;

    public Room() {}

    public Room(String hostToken, String roomName, String hostId, String food, String restaurant, HashMap<String, String> tag, HashMap<String, String> ban) {
        this.hostToken = hostToken;
        this.roomName = roomName;
        this.hostId = hostId;
        this.food = food;
        this.restaurant = restaurant;
        this.tag = tag;
        this.ban = ban;
    }

    public String getHostToken() {
        return hostToken;
    }

    public void setHostToken(String hostToken) {
        this.hostToken = hostToken;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
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

    public HashMap<String, String> getBan() {
        return ban;
    }

    public void setBan(HashMap<String, String> ban) {
        this.ban = ban;
    }
}