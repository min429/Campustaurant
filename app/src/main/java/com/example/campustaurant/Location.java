package com.example.campustaurant;

import java.io.Serializable;

public class Location implements Serializable { // Serializable : ArrayList를 intent를 통해 넘겨줄 때 직렬화 되어있어야 함
    public String name;
    public Double longitude;
    public Double latitude;

    public Location() {}

    public Location(String name, Double longitude, Double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
