package com.example.campustaurant;

import java.util.HashMap;

public class Room {
    private String hostToken;
    private String roomName;
    private String hostId;
    private String food;
    private String restaurant;
    private String uri;
    private HashMap<String, String> tag;
    private HashMap<String, String> ban;
    private HashMap<String, String> guest;

    public Room() {}

    public Room(String hostToken, String roomName, String hostId, String food, String restaurant, String uri, HashMap<String, String> tag, HashMap<String, String> ban, HashMap<String, String> guest) {
        this.hostToken = hostToken;
        this.roomName = roomName;
        this.hostId = hostId;
        this.food = food;
        this.restaurant = restaurant;
        this.uri = uri;
        this.tag = tag;
        this.ban = ban;
        this.guest = guest;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public HashMap<String, String> getGuest() {
        return guest;
    }

    public void setGuest(HashMap<String, String> guest) {
        this.guest = guest;
    }
}