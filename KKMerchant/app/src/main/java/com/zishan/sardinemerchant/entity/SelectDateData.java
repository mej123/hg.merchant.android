package com.zishan.sardinemerchant.entity;

/**
 * 店铺订单 选择日期
 * Created by wislie on 2017/11/8.
 */

public class SelectDateData {
    //是否选中
    private boolean isSelected;
    //显示的内容
    private String content;
    //开始时间
    private Long start_time;
    //结束时间
    private Long end_time;

    public SelectDateData(String content){
        this.content = content;
    }

    public SelectDateData(String content, Long start_time, Long end_time){
        this.content = content;
        this.start_time = start_time;
        this.end_time = end_time;
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

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }
}
