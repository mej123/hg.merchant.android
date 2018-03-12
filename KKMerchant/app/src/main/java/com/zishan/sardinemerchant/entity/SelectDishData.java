package com.zishan.sardinemerchant.entity;

/**
 * 菜品筛选
 * Created by wislie on 2017/11/9.
 */

public class SelectDishData {

    //是否选中
    private boolean isSelected;
    //显示的内容
    private String content;

    public SelectDishData(String content) {
        this.content = content;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
