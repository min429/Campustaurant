package com.example.campustaurant;

public class User {
    public String userId;
    public String password;

    public User() {} // Firebase이용 모델 클래스 -> 무조건 빈 생성자 작성해 놔야함


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
}
