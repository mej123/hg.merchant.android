package com.kuku.kkmerchant.entity;

/**
 * Created by yang on 2017/9/13.
 */

public class StoreTablesEntity {


    private String tableNumber;
    private int takeState;

    public StoreTablesEntity(String tableNumber, int takeState) {
        this.tableNumber = tableNumber;
        this.takeState = takeState;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getTakeState() {
        return takeState;
    }

    public void setTakeState(int takeState) {
        this.takeState = takeState;
    }
}
