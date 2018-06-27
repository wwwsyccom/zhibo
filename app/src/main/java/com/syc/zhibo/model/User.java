package com.syc.zhibo.model;

public class User {
    private String name;
    private String photo;
    private int price;  //单价
    private int access;    //接通率
    public User(String name, String photo, int price, int access){
        this.name = name;
        this.photo = photo;
        this.price = price;
        this.access = access;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getPhoto() {
        return photo;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getAccess() {
        return access;
    }
}
