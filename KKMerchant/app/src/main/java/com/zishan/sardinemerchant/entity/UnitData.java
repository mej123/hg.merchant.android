package com.zishan.sardinemerchant.entity;

/**
 * 菜品单位
 * Created by wislie on 2017/10/30.
 */

public class UnitData {

    private boolean isSelected;
    private String unitName;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
