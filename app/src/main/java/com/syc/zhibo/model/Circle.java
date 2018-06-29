package com.syc.zhibo.model;

public class Circle {
    private int userId;
    private String name;
    private String time;
    private String msg;
    private String[] imgs;
    private String photo;

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {

        return msg;
    }

    public String[] getImgs() {
        return imgs;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {

        return photo;
    }

    public int getUserId() {

        return userId;
    }

    public Circle(int userId, String name, String time, String msg, String[] imgs, String photo) {
        this.userId = userId;
        this.name = name;
        this.time = time;
        this.msg = msg;
        this.imgs = imgs;
        this.photo = photo;
    }
}
