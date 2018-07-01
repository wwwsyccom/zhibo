package com.syc.zhibo.model;

public class Gift {
    private String icon;
    private String name;

    public Gift() {
    }

    public void setPrice(int price) {

        this.price = price;
    }

    public int getPrice() {

        return price;
    }

    private int price;
    private int num;
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getIcon() {

        return icon;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    public Gift(String icon, String name, int num) {
        this.icon = icon;
        this.name = name;
        this.num = num;
    }
}
