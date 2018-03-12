package com.zishan.sardinemerchant.entity;

import com.example.wislie.rxjava.model.page.TableBoxEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 桌台包厢的数据
 * Created by wislie on 2017/11/6.
 */

public class TableBoxData{

    private ArrayList<TableBoxEntity> tableBoxList;

    private int type;

    public TableBoxData(ArrayList<TableBoxEntity> tableBoxList, int type) {
        this.tableBoxList = tableBoxList;
        this.type = type;
    }

    public ArrayList<TableBoxEntity> getTableBoxList() {
        return tableBoxList;
    }

    public void setTableBoxList(ArrayList<TableBoxEntity> tableBoxList) {
        this.tableBoxList = tableBoxList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
