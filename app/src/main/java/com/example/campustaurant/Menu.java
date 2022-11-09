package com.example.campustaurant;

public class Menu {
    public String food;

    public Menu() {
    } // default 생성자를 만들어주지 않으면 database에서 값을 읽어오지 못함

    public Menu(String food) {
        this.food = food;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
