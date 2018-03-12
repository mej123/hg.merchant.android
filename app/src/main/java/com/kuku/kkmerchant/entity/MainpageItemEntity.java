package com.kuku.kkmerchant.entity;

/**
 * Created by yang on 2017/9/12.
 */

public  class MainpageItemEntity {

  private   String number;
  private   String time;
  private   String money;
    private int state;

    public MainpageItemEntity(String number, String time, String money, int state) {
        this.number = number;
        this.time = time;
        this.money = money;
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
