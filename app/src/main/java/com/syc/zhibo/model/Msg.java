package com.syc.zhibo.model;

public class Msg {
    private int userId;
    private String photo;
    private String name;
    private String msg;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {

        return userId;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public Msg(int userId, String photo, String name, String msg, String time) {

        this.userId = userId;
        this.photo = photo;
        this.name = name;
        this.msg = msg;
        this.time = time;
    }

    private String time;
}
