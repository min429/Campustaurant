package com.example.campustaurant;

public class User {
    private String userId;
    private String password;
    private String room;

    public User() {} // Firebase이용 모델 클래스 -> 무조건 빈 생성자 작성해 놔야함

    public User(String userId, String password, String room) {
        this.userId = userId;
        this.password = password;
        this.room = room;
    }

    // Getter & Setter -> 파이어베이스에서 데이터를 읽기/쓰기 위해서는 모두 필요함

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
