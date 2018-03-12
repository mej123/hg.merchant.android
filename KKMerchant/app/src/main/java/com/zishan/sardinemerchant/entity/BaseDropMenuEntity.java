package com.zishan.sardinemerchant.entity;

/**
 * 下拉框基类
 * Created by wislie on 2017/12/28.
 */

public class BaseDropMenuEntity {

    private String content;
    private boolean isSelected;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
