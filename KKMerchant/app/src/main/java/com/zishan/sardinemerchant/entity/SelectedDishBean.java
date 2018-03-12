package com.zishan.sardinemerchant.entity;

import java.io.Serializable;

/**
 * 选中的商品
 * Created by wislie on 2017/12/7.
 */

public class SelectedDishBean implements Serializable{

    private Integer num;
    private Long price;
    private String productName;
    private Long productId;

    public SelectedDishBean(Integer num, Long price, String productName) {
        this.num = num;
        this.price = price;
        this.productName = productName;
    }

    public SelectedDishBean(Integer num, Long price, String productName, Long productId) {
        this.num = num;
        this.price = price;
        this.productName = productName;
        this.productId = productId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


}
