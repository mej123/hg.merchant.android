package com.zishan.sardinemerchant.entity;

/**
 * 门店状态
 * Created by wislie on 2018/1/16.
 */

public class MyStoreStateEntity {

    private Integer state;
    private String name;

    public MyStoreStateEntity(Integer state, String name) {
        this.state = state;
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
